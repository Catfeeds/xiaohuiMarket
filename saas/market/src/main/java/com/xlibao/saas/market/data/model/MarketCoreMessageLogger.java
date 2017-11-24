package com.xlibao.saas.market.data.model;

import java.util.Date;

public class MarketCoreMessageLogger {

    private Long id;
    private Long marketId;
    private String keyword;
    private String originIp;
    private Date launchTime = new Date();
    private Integer launchStatus;
    private String targetIp;
    private String targetMark;
    private Date sendOutTime;
    private Byte needCallback;
    private Date callbackTime;
    private Integer callbackStatus;
    private Date notifyTime;
    private Integer notifyStatus;
    private String notifyMsg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getOriginIp() {
        return originIp;
    }

    public void setOriginIp(String originIp) {
        this.originIp = originIp == null ? null : originIp.trim();
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public Integer getLaunchStatus() {
        return launchStatus;
    }

    public void setLaunchStatus(Integer launchStatus) {
        this.launchStatus = launchStatus;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp == null ? null : targetIp.trim();
    }

    public String getTargetMark() {
        return targetMark;
    }

    public void setTargetMark(String targetMark) {
        this.targetMark = targetMark == null ? null : targetMark.trim();
    }

    public Date getSendOutTime() {
        return sendOutTime;
    }

    public void setSendOutTime(Date sendOutTime) {
        this.sendOutTime = sendOutTime;
    }

    public Byte getNeedCallback() {
        return needCallback;
    }

    public void setNeedCallback(Byte needCallback) {
        this.needCallback = needCallback;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    public Integer getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(Integer callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public String getNotifyMsg() {
        return notifyMsg;
    }

    public void setNotifyMsg(String notifyMsg) {
        this.notifyMsg = notifyMsg == null ? null : notifyMsg.trim();
    }
}