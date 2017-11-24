package com.xlibao.advert.data.model;

/**
 * Created by admin on 2017/9/21.
 */
public class AdvertScreenInfo {
    private Long id;
    private Integer screenID;
    private Integer advertID;
    private String sCode;
    private String marketID;
    private String marketName;
    private String title;
    private String videoName;
    private String beginTime;
    private String endTime;
    private Integer isDown;
    private String url;
    private String successDownTime="";
    private Integer playStatus;
    private Integer playOrder;
    private Integer status;
    private String requireTime;
    private String advertRemark="";
    private String remark="";
    private String createTime;
    private String mac;
    private String timeSize;
    private Integer requestDownStatus = 0;
    private String requestDownTime = "";
    private Integer groupID;
    private  String groupName;
    private Integer style;

   /* private List<AdvertInfo> advertInfo;
    private List<ScreenInfo> screenInfo;*/


    /**
     *id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * 屏幕id
     */
    public Integer getScreenID() {
        return screenID;
    }

    public void setScreenID(Integer screenID) {
        this.screenID = screenID;
    }


    /**
     * 广告id
     */
    public Integer getAdvertID() {
        return advertID;
    }

    public void setAdvertID(Integer advertID) {
        this.advertID = advertID;
    }

    /**
     * 屏幕编号
     */
    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    /**
     * 广告名称
     */
    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }


    /**
     * 播放开始时间
     */
    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }


    /**
     * 播放结束时间
     */
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    /**
     * 广告是否下载
     */
    public Integer getIsDown() {
        return isDown;
    }

    public void setIsDown(Integer isDown) {
        this.isDown = isDown;
    }

    /**
     * 广告下载地址
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 播放备注
     */
    public String getAdvertRemark() {
        return advertRemark;
    }

    public void setAdvertRemark(String advertRemark) {
        this.advertRemark = advertRemark;
    }

    /**
     * 商店Id
     */
    public String getMarketID() {
        return marketID;
    }

    public void setMarketID(String marketID) {
        this.marketID = marketID;
    }

    /**
     * 商店名称
     */
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * 标题
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 广告下载成功时间
     */
    public String getSuccessDownTime() {
        return successDownTime;
    }

    public void setSuccessDownTime(String successDownTime) {
        this.successDownTime = successDownTime;
    }

    /**
     * 广告时长
     */
    public String getTimeSize() {
        return timeSize;
    }

    public void setTimeSize(String timeSize) {
        this.timeSize = timeSize;
    }

    /**
     * 广告播放状态 0-待播放 1-播放中 2-已停止
     */
    public Integer getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(Integer playStatus) {
        this.playStatus = playStatus;
    }


    /**
     * 广告播放顺序
     */
    public Integer getPlayOrder() {
        return playOrder;
    }

    public void setPlayOrder(Integer playOrder) {
        this.playOrder = playOrder;
    }


    /**
     * 广告状态 0-已删除 1-未删除
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    /**
     * 下次请求服务器时间
     */
    public String getRequireTime() {
        return requireTime;
    }

    public void setRequireTime(String requireTime) {
        this.requireTime = requireTime;
    }


    /**
     * 播放备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    /**
     * 广告创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 广告的基本信息
     */
    /*public List<AdvertInfo> getAdvertInfo() {
        return advertInfo;
    }
    public void setAdvertInfo(List<AdvertInfo> advertInfo) {
        this.advertInfo = advertInfo;
    }*/

    /**
     * 屏幕的基本信息
     */
    /*public List<ScreenInfo> getScreenInfo() {
        return screenInfo;
    }
    public void setScreenInfo(List<ScreenInfo> screenInfo) {
        this.screenInfo = screenInfo;
    }*/

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     *  请求下载状态
     */
    public Integer getRequestDownStatus() {
        return requestDownStatus;
    }

    public void setRequestDownStatus(Integer requestDownStatus) {
        this.requestDownStatus = requestDownStatus;
    }

    /**
     *  请求下载时间
     */
    public String getRequestDownTime() {
        return requestDownTime;
    }

    public void setRequestDownTime(String requestDownTime) {
        this.requestDownTime = requestDownTime;
    }

    /**
     * 组名称
     * @return
     */
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 组ID
     * @return
     */
    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    /**
     * 播放类型
     * @return
     */
    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }
}
