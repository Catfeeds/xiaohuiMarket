package com.xlibao.saas.advertise.service.video.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;

import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.saas.advertise.config.Advertise;
import com.xlibao.saas.advertise.config.ConfigFactory;
import com.xlibao.saas.advertise.config.LocalMAC;
import com.xlibao.saas.advertise.file.JsonFileTool;
import com.xlibao.saas.advertise.file.PlayListFile;
import com.xlibao.saas.advertise.service.video.AdverService;
import com.xlibao.saas.advertise.util.DateUtil;
import com.xlibao.saas.advertise.util.DownLoadFTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.enterprise.inject.New;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2017/8/23.
 */
@Transactional
@Service("videoService")
public class AdverServiceImpl extends BasicWebService implements AdverService {
    private static final Logger logger = LoggerFactory.getLogger(AdverServiceImpl.class);
    boolean loadDown=false;//视频下载中

    @Override
    public void writeConfig(JSONObject jsonObject) {
        try {
            //将获取的广告写入配置文件
            JsonFileTool jsonFileTool = new JsonFileTool();
            jsonFileTool.WriteJson(jsonObject);
            logger.info("播放列表写入配置文件");
        }catch (Exception ex){
            logger.error("播放列表写入配置文件：" + ex.getMessage());
        }
    }


    @Override
    public boolean adverArithmetic(JSONObject jsonObject){
        boolean isupdate=false;
        JsonFileTool jsonFileTool = new JsonFileTool();
        //读取配置文件,播放列表信息
        JSONArray fileArray =jsonFileTool.ReaderJsont();
       /**保留插播广告**/
        for (int i=0;i<fileArray.size();i++){
            JSONObject object = fileArray.getJSONObject(i);
            int style= object.getInteger("style");
            if(style==1){
                fileArray.remove(object);
                i--;
            }
        }

        //根据服务器广告信息，获取本机广告信息
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray jsonArray = response.getJSONArray("data");
        /**保留插播广告**/
        for (int i=0;i<jsonArray.size();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            int style= object.getInteger("style");
            if(style==1){
                jsonArray.remove(object);
                i--;
            }
        }

        /**
         * jsonArray新播放列表
         * fileArray原播放列表
         */
        JSONArray arrayCopy =new JSONArray();
        arrayCopy.addAll(fileArray);
        //差集=>比较两个数据是否发生变化
        fileArray.removeAll(jsonArray);
        //差集=>比较两个数据是否发生变化
        jsonArray.removeAll(arrayCopy);
        if(fileArray.size()>0||jsonArray.size()>0){
            isupdate = true;
        }
        logger.info("广告播放数据是否有更新："+isupdate);
        /**********逻辑判断是否有广告更新**********/
        return isupdate;
    }


    public boolean adverArithmetic_bak(JSONObject jsonObject){
        boolean isupdate=false;
        JsonFileTool jsonFileTool = new JsonFileTool();
        //读取配置文件,播放列表信息
        JSONArray fileArray =jsonFileTool.ReaderJsont();

        String localMAC= LocalMAC.getLocalMac();
        //根据服务器广告信息，获取本机广告信息
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray jsonArray = response.getJSONArray("data");

        /**
         * jsonArray新播放列表
         * fileArray原播放列表
         */
        JSONArray arrayCopy =new JSONArray();
        arrayCopy.addAll(fileArray);
        //差集=>比较两个数据是否发生变化
        fileArray.removeAll(jsonArray);
        //差集=>比较两个数据是否发生变化
        jsonArray.removeAll(arrayCopy);
        if(fileArray.size()>0||jsonArray.size()>0){
            isupdate = true;
        }
        logger.info("广告播放数据是否有更新："+isupdate);
        /**********逻辑判断是否有广告更新**********/
        return isupdate;
    }

    @Override
    public void adverDownLoad(JSONObject jsonObject) {
        //下载广告文件
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray jsonArray = response.getJSONArray("data");
        int size = jsonArray.size();
        for(int  i = 0; i < size; i++) {
            JSONObject adverJson = jsonArray.getJSONObject(i);
            int requestDownStatus = adverJson.getInteger("requestDownStatus");
            String requestDownTime = adverJson.getString("requestDownTime");
            if(requestDownStatus==0){
            if (adverJson.getInteger("isDown") == 0 && !loadDown) {
                loadDown = true;
                Runnable runnable = () -> {

                        String urlStr = adverJson.getString("url");
                        String advertID = adverJson.getString("advertID");
                        String screenID = adverJson.getString("screenID");
                        String groupID = adverJson.getString("groupID");
                        String marketID = adverJson.getString("marketID");
                        String savePath = ConfigFactory.getDomainNameConfig().downLoadPATH;
                        String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
                        if (fileName.indexOf(".") <= 0) {
                           // fileName = fileName + ".mp4";
                        }
                    try {
                        logger.info(urlStr + "开始广告下载" + new Date());
                        DownLoadFTP downLoadFTP = new DownLoadFTP();
                        //连接FTP
                        logger.info("连接FTP");
                        downLoadFTP.connect(ConfigFactory.getDomainNameConfig().ftpHost, ConfigFactory.getDomainNameConfig().ftpPort, ConfigFactory.getDomainNameConfig().ftpUsername, ConfigFactory.getDomainNameConfig().ftpPassword);
                        logger.info("连接FTP成功");
                        DownLoadFTP.UploadStatus status = downLoadFTP.upload(urlStr,fileName);
                        logger.info("上传FTP成功");
                        downLoadFTP.disconnect();
                        logger.info("断开连接");
                        if(DownLoadFTP.UploadStatus.Upload_New_File_Success.equals(status)||DownLoadFTP.UploadStatus.File_Exits.equals(status)){
                            loadDown = false;
                            logger.info(urlStr + "结束广告下载" + new Date());
                            try {
                                String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/saveDownload.do?advertID=" + advertID + "&groupID=" + groupID + "&isDown=1");
                                JSONObject response2 = JSONObject.parseObject(json);
                                logger.info(urlStr + "更新下载状态：" + response2.getString("msg"));
                            } catch (Exception ex) {
                                logger.error("更新下载状态失败：" + ex.getMessage());
                            }
                        }else {
                            loadDown = false;
                            try {
                                String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/saveDownload.do?advertID=" + advertID + "&groupID=" + groupID + "&isDown=2");
                                JSONObject response2 = JSONObject.parseObject(json);
                                logger.error("视频下载操作失败。更新下载状态为2");
                            } catch (Exception ex) {
                                logger.error("更新下载状态失败：" + ex.getMessage());
                            }
                        }
                    } catch (IOException ex) {
                        loadDown = false;
                        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/saveDownload.do?advertID=" + advertID + "&groupID=" + groupID + "&isDown=2");
                        JSONObject response2 = JSONObject.parseObject(json);
                        logger.error("视频下载操作失败。更新下载状态为2。：" + ex.getMessage());
                        // throw new XlibaoRuntimeException("视频下载操作失败");
                    }
                };
                AsyncScheduledService.submitCommonTask(runnable, 0, TimeUnit.SECONDS);
            }
        }else{
                if (adverJson.getInteger("isDown") == 0 && !loadDown) {
                    if (!"".equals(requestDownTime)) {
                        Date downTime = DateUtil.doFormatDate(requestDownTime, "yyyy-MM-dd HH:mm:ss");
                        Date nowDate = new Date();
                        //三小时有效期
                        if (nowDate.getTime() >= downTime.getTime() && (nowDate.getTime() - downTime.getTime()) <= (1000 * 3600 * 3)) {
                            loadDown = true;
                            Runnable runnable = () -> {
                                    String urlStr = adverJson.getString("url");
                                    String advertID = adverJson.getString("advertID");
                                    String screenID = adverJson.getString("screenID");
                                    String groupID = adverJson.getString("groupID");
                                    String marketID = adverJson.getString("marketID");
                                    String savePath = ConfigFactory.getDomainNameConfig().downLoadPATH;
                                    String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
                                    if (fileName.indexOf(".") <= 0) {
                                        //fileName = fileName + ".mp4";
                                    }
                                try {
                                    logger.info(urlStr + "开始广告下载" + new Date());
                                    DownLoadFTP downLoadFTP = new DownLoadFTP();
                                    //连接FTP
                                    downLoadFTP.connect(ConfigFactory.getDomainNameConfig().ftpHost, ConfigFactory.getDomainNameConfig().ftpPort, ConfigFactory.getDomainNameConfig().ftpUsername, ConfigFactory.getDomainNameConfig().ftpPassword);
                                    DownLoadFTP.UploadStatus status = downLoadFTP.upload(urlStr,fileName);
                                    downLoadFTP.disconnect();
                                    if(DownLoadFTP.UploadStatus.Upload_New_File_Success.equals(status)||DownLoadFTP.UploadStatus.File_Exits.equals(status)){
                                        loadDown = false;
                                        logger.info(urlStr + "结束广告下载" + new Date());
                                        try {
                                            String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/saveDownload.do?advertID=" + advertID + "&groupID=" + groupID + "&isDown=1");
                                            JSONObject response2 = JSONObject.parseObject(json);
                                            logger.info(urlStr + "更新下载状态：" + response2.getString("msg"));
                                        } catch (Exception ex) {
                                            logger.error("更新下载状态失败：" + ex.getMessage());
                                        }
                                    }else {
                                        loadDown = false;
                                        try {
                                            String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/saveDownload.do?advertID=" + advertID + "&groupID=" + groupID + "&isDown=2");
                                            JSONObject response2 = JSONObject.parseObject(json);
                                            logger.error("视频下载操作失败。更新下载状态为2");
                                        } catch (Exception ex) {
                                            logger.error("更新下载状态失败：" + ex.getMessage());
                                        }
                                    }
                                } catch (Exception ex) {
                                    loadDown = false;
                                    String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/saveDownload.do?advertID=" + advertID + "&groupID=" + groupID + "&isDown=2");
                                    JSONObject response2 = JSONObject.parseObject(json);
                                    logger.error("视频下载操作失败。更新下载状态为2。：" + ex.getMessage());
                                }
                            };
                            AsyncScheduledService.submitCommonTask(runnable, 0, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        }
    }

    public boolean createPlayList(int style,String time) {
        boolean isFlag=true;
        JsonFileTool jsonFileTool = new JsonFileTool();
        PlayListFile playListFile = new PlayListFile();
        //创建播放列表前清除列表
        playListFile.clearInfoForFile();
        //读取配置文件,根据配置信息逻辑将视频加入播放列表
        ArrayList<Advertise>  adverList =jsonFileTool.ReaderJson();

        for (int i=0;i<adverList.size();i++) {
            Advertise advertise = adverList.get(i);
            if (advertise.getIsDown()==1) {
                String savePath = ConfigFactory.getDomainNameConfig().downLoadPATH;
                String urlStr = advertise.getUrl();
                urlStr = urlStr.trim();
                String fileName = urlStr.substring(urlStr.lastIndexOf("/")+1);
                if(fileName.indexOf(".")<=0){
                    fileName = fileName+".mp4";
                }
                String filePath =savePath + File.separator + fileName;
                if(advertise.getStyle()==style) {
                    if(style==1) {
                        playListFile.println(filePath);
                    }else if(style==2){
                        if(time.equals(advertise.getBeginTime())) {
                            playListFile.println(filePath);
                        }
                    }
                }
            }
        }

       // logger.info("播放列表是否有更新："+isFlag);
        return isFlag;
    }
    public boolean createPlayList_bak() {
        boolean isFlag=false;
        JsonFileTool jsonFileTool = new JsonFileTool();
        PlayListFile playListFile = new PlayListFile();
        //播放列表数据
        ArrayList<String>  playList =  playListFile.readTxtFile();
        //创建播放列表前清除列表
        playListFile.clearInfoForFile();
        //读取配置文件,根据配置信息逻辑将视频加入播放列表
        ArrayList<Advertise>  adverList =jsonFileTool.ReaderJson();
        Date nowDate = new Date();
        ArrayList<String>  planList = new ArrayList<String>();
        for (int i=0;i<adverList.size();i++) {
            boolean isexist=false;//判断当前广告是否在配置中存在
            Advertise advertise = adverList.get(i);
            if (advertise.getIsDown()==1) {
                Date endTime = DateUtil.doFormatDate(advertise.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                if (endTime.getTime() > nowDate.getTime()) {
                    Date beginTime = DateUtil.doFormatDate(advertise.getBeginTime(), "yyyy-MM-dd HH:mm:ss");
                    if (nowDate.getTime() >= beginTime.getTime()) {
                        String savePath = ConfigFactory.getDomainNameConfig().downLoadPATH;
                        String urlStr = advertise.getUrl();
                        urlStr = urlStr.trim();
                        String fileName = urlStr.substring(urlStr.lastIndexOf("/")+1);
                        if(fileName.indexOf(".")<=0){
                            fileName = fileName+".mp4";
                        }
                        String filePath =savePath + File.separator + fileName;
                        playListFile.println(filePath);
                        planList.add(filePath);
                    }
                }
            }
        }
        /**
         * playList原播放列表
         * planList新播放列表
         */
        List listCopy =new ArrayList();
        listCopy.addAll(playList);
        //差集=>比较两个数据是否发生变化
        playList.removeAll(planList);
        //差集=>比较两个数据是否发生变化
        planList.removeAll(listCopy);
        if(playList.size()>0||planList.size()>0){
            isFlag = true;
        }
        logger.info("播放列表是否有更新："+isFlag);
        return isFlag;
    }
    @Override
    public String getAdvertsByMac(String mac){
        String json="";
        try {
            json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/getAllAdvertInfo.do?mac=" + mac);
        }catch (Exception ex) {
            logger.error("获取播放广告列表异常：" + ex.getMessage());
            json="";
        }
        return json;
    }
    @Override
    public int getReqTime(String localMAC){
        int reqTime=0;
        try {
            /***********获取屏幕请求时间BEGIN*********/
            String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().advertiseRemoteURL + "advert/getScreenInfoFromMAC.do?mac=" + localMAC);
            JSONObject response = JSONObject.parseObject(json);
            if (response.getIntValue("code") == 0) {
                JSONObject jsonObject = response.getJSONObject("response");
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.size() > 0) {
                    JSONObject screenJson = jsonArray.getJSONObject(0);
                    reqTime = screenJson.getInteger("requireTime");
                }else {
                    reqTime=300;
                }
            }
            /***********获取屏幕请求时间END*********/
        }catch (Exception ex) {
            logger.error("获取屏幕请求时间异常：" + ex.getMessage());
            throw new XlibaoRuntimeException("获取屏幕请求时间异常");
        }
        return reqTime;
    }

    /**
     * 保留插播广告
     * @return
     */
    @Override
    public ArrayList<Advertise> getCutInAdvert(JSONObject jsonObject){
        JsonFileTool jsonFileTool = new JsonFileTool();

        //读取配置文件,根据配置信息逻辑将视频加入播放列表
        ArrayList<Advertise>  adverList = new ArrayList<Advertise>();
        JSONArray jsonArray = jsonFileTool.ReaderJsont();
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject adverJson = jsonArray.getJSONObject(i);
            int style = adverJson.getInteger("style");
            Advertise adver = new Advertise();
            if(style==2) {
                String beginDate = adverJson.getString("beginTime").trim();
                adver.setBeginTime(beginDate);
            }
            adver.setScreenID(adverJson.getString("screenID"));
            adver.setGroupID(adverJson.getInteger("groupID"));
            adver.setStyle(style);
            if(style==2) {
                adverList.add(adver);
            }
        }
        //删除重复创建定时任务数据
        for  ( int i = 0;i<adverList.size() - 1;i++)  {
            for  ( int j=adverList.size()-1;j>i;j--)  {
                Advertise advertisei = adverList.get(i);
                Advertise advertisej = adverList.get(j);
                if  (advertisei.getBeginTime().equals(advertisej.getBeginTime()))  {
                    adverList.remove(j);
                }
            }
        }

        //获取最新插播广告
        ArrayList<Advertise>  adverListNew = new ArrayList<Advertise>();
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray jsonArray2 = response.getJSONArray("data");
        int size2 = jsonArray2.size();
        for (int i = 0; i < size2; i++) {
            JSONObject adverJson = jsonArray2.getJSONObject(i);
            int style = adverJson.getInteger("style");
            Advertise adver = new Advertise();
            if(style==2) {
                String beginDate = adverJson.getString("beginTime").trim();
                adver.setBeginTime(beginDate);
            }
            adver.setScreenID(adverJson.getString("screenID"));
            adver.setGroupID(adverJson.getInteger("groupID"));
            adver.setStyle(style);
            if(style==2) {
                adverListNew.add(adver);
            }
        }
        //删除重复创建定时任务数据
        for  ( int i = 0;i<adverListNew.size() - 1;i++)  {
            for  ( int j=adverListNew.size()-1;j>i;j--)  {
                Advertise advertisei = adverListNew.get(i);
                Advertise advertisej = adverListNew.get(j);
                if  (advertisei.getBeginTime().equals(advertisej.getBeginTime()))  {
                    adverListNew.remove(j);
                }
            }
        }

        //删除已经存在的，保留最新的
        adverListNew.removeAll(adverList);
        return  adverListNew;
    }

}
