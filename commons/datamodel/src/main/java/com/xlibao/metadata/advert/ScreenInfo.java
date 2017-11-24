package com.xlibao.metadata.advert;

import java.util.Date;

/**
 * Created by admin on 2017/8/28.
 */
public class ScreenInfo {

    private Long id;
    private Integer screenID;
    private Integer marketId;
    private String marketName;
    private String requireTime;
    private String size;
    private String code;
    private String mac;
    private Integer advertCount;
    private Integer used;
    private Date createTime;
    private String screenRemark;


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
     * 屏幕id
     */
    public Integer getScreenID() {
        return screenID;
    }

    public void setScreenID(Integer screenID) {
        this.screenID = screenID;
    }


    /**
     * 商店id
     */
    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
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
     * 屏幕尺寸
     */
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    /**
     * 屏幕编号
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * 屏幕mac
     */
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * 屏幕请求时间
     */
    public String getRequireTime() {
        return requireTime;
    }

    public void setRequireTime(String requireTime) {
        this.requireTime = requireTime;
    }

    /**
     * 投放广告数量
     */
    public Integer getAdvertCount() {
        return advertCount;
    }

    public void setAdvertCount(Integer advertCount) {
        this.advertCount = advertCount;
    }


    /**
     * 屏幕是否使用
     */
    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }


    /**
     * 屏幕备注
     */
    public String getScreenRemark() {
        return screenRemark;
    }

    public void setScreenRemark(String screenRemark) {
        this.screenRemark = screenRemark;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
