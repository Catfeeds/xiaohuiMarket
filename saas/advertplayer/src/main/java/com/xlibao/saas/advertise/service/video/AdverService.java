package com.xlibao.saas.advertise.service.video;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.advertise.config.Advertise;
import com.xlibao.saas.advertise.player.Player;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 2017/8/23.
 */
public interface AdverService {

    /**
     * 读取广告列表写入本地配置文件中
     * @param jsonObject
     */
    void writeConfig(JSONObject jsonObject);

    /**
     * 创建播放列表
     */
    boolean createPlayList(int style, String  time);

    /**
     * 获取服务器广告信息与配置信息比较是否有更新
     * @param jsonObject
     * @return boolean
     */
    boolean adverArithmetic(JSONObject jsonObject);

    /**
     * 下载广告
     * @param jsonObject
     */
    void adverDownLoad(JSONObject jsonObject);

    /**
     *根据MAC获取播放列表
     * @param mac
     * @return
     */
    String getAdvertsByMac(String mac);

    /**
     * 根据MAC获取屏幕请求时间
     * @param localMAC
     * @return
     */
    int getReqTime(String localMAC);

    /**
     * 获取插播广告
     * @return
     */
    ArrayList<Advertise> getCutInAdvert(JSONObject jsonObject);

}
