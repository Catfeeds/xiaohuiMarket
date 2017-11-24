package com.xlibao.metadata.advert;

import java.util.Date;

/**
 * Created by admin on 2017/8/28.
 */
public class AdvertInfo {

    private Long id;
    private Integer advertID;
    private String title;
    private String videoName;
    private Integer timeSize;
    private Integer timeType;//广告时长类型
    private String remark;
    private String url;
    private Integer used;
    private Integer isDelete;
    private String createTime;


    /**
     * id
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
     * 广告标题
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 广告时长
     */
    public Integer getTimeSize() {
        return timeSize;
    }

    public void setTimeSize(Integer timeSize) {
        this.timeSize = timeSize;
    }

    /**
     * 时间类型
     */
    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    /**
     * 第三方视频名称
     */
    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    /**
     * 广告备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    /**
     * 广告地址
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * 广告是否被投放
     */
    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }


    /**
     * 广告是否被删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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
}
