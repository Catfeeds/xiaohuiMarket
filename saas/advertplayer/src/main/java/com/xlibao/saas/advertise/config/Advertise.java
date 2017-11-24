package com.xlibao.saas.advertise.config;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2017/8/25.
 */
public class Advertise implements Serializable {
    private static final long serialVersionUID = 1237683979538355923L;

    private String screenID;//屏幕ID
    private String mac;//屏幕MAC
    private String advertID;//广告ID
    private String videoName;//文件名
    private int time;//广告时长
    private String url;//广告地址
    private String beginTime;//广告播放开始时间
    private String endTime;//广告播放结束时间
    private int isDown;//是否下载
    private int playOrder;//播放排序
    private int style;//视频类型1普通类型2插播类型
    private int groupID;//分组ID

    public String getScreenID() {
        return screenID;
    }

    public void setScreenID(String screenID) {
        this.screenID = screenID;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAdvertID() {
        return advertID;
    }

    public void setAdvertID(String advertID) {
        this.advertID = advertID;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getIsDown() {
        return isDown;
    }

    public void setIsDown(int isDown) {
        this.isDown = isDown;
    }

    public int getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(int playOrder) {
        this.playOrder = playOrder;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
