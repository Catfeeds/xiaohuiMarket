package com.xlibao.saas.advertise.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.common.support.BasicRemoteService;
import com.xlibao.saas.advertise.config.Advertise;
import com.xlibao.saas.advertise.config.LocalMAC;
import com.xlibao.saas.advertise.player.Player;
import com.xlibao.saas.advertise.service.video.AdverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2017/8/22.
 */
@Component
public class AdvertisePlayService extends BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(AdvertisePlayService.class);
    @Autowired
    private AdverService adverService;
    int first = 0;//重启系统数据没有更新，通过first=0启动播放视频
    int reqTime=300;//屏幕间隔请求时间

    public void adverStart() {
        Player player = new Player().loadPanel();
        String localMAC="";
        try {
             localMAC= LocalMAC.getLocalMac();
        }catch (Exception ex){
            logger.error("获取MAC地址异常：" + ex.getMessage());
            localMAC="";
        }

        /**获取请求时间*/
        try{
            reqTime= adverService.getReqTime(localMAC);
        }catch (Exception ex){
            logger.error("获取屏幕请求时间异常：" + ex.getMessage());
            reqTime=300;
        }

        String finalLocalMAC = localMAC;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /**
                 * TODO 业务代码片段
                 */
                System.out.println("线程次数:" + first);
                try {
                    playAdver(player, finalLocalMAC);
                }catch (Exception ex){
                    logger.error("播放线程异常：" + ex.getMessage());
                }
                first+=1;
                // ***之后 继续执行任务
                AsyncScheduledService.submitCommonTask(this, reqTime, TimeUnit.SECONDS);
            }
        };
        AsyncScheduledService.submitCommonTask(runnable, 0, TimeUnit.SECONDS);
    }

    /**
     *播放广告
     */
    private void playAdver(Player player,String localMAC) throws ParseException {
        String json="";
        boolean flag =false;
        try {
             json = adverService.getAdvertsByMac(localMAC);
            logger.info("播放广告数据："+json);
        }catch (Exception ex) {
             logger.error("获取播放广告列表异常：" + ex.getMessage());
             json="";
         }
         if(!"".equals(json)&&json!=null) {
             JSONObject response = JSONObject.parseObject(json);
             JSONObject response2 = JSONObject.parseObject(json);

             if (response.getIntValue("code") == 0) {
                 JSONObject jsonObject = response.getJSONObject("response");
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 //if (jsonArray.size() > 0) {
                 //监测插播是否有广告更新
                 boolean isUpdate = adverService.adverArithmetic(response2);

                 if (isUpdate) {
                     ArrayList<Advertise> advertises = adverService.getCutInAdvert(response2);
                     for (int i = 0; i < advertises.size(); i++) {
                         Advertise advertise = advertises.get(i);
                         logger.info("创建定时任务组ID:"+advertise.getGroupID());
                         String time = advertise.getBeginTime();
                         Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                         Timer timer = new Timer();
                         timer.schedule(new TimerTask() {
                             public void run() {
                                 logger.info("执行任务组ID:"+advertise.getGroupID());
                                 adverService.createPlayList(2,time);
                                 // 广告更新重新播放
                                 Process proc = player.play(1, 0);
                                 //监视mplayer退出
                                 Thread waitThread = new Thread() {
                                     public void run() {
                                         try {
                                             proc.waitFor();
                                             adverService.createPlayList(1,null);
                                             player.play(0, 0);
                                         } catch (InterruptedException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 };
                                 waitThread.start();
                             }
                         }, d);// 设定指定的时间time,此处为2000毫秒
                     }
                 }
                 //下载广告
                 adverService.adverDownLoad(response);
                 //if (isUpdate) {
                 //播放信息写入配置
                 adverService.writeConfig(response);
                 //  }
                     //创建播放列表并判断是否有更新
                      flag = adverService.createPlayList(1,null);
                // }
             }
         }
        if (first == 0) {
            // 广告更新重新播放
            player.play(0, 0);
        }
    }
}
