package com.xlibao.saas.market.data.model;

import java.util.Date;

public class MarketOrderStatusLogger {

    private Long id;
    private String orderSequenceNumber;
    private String mark;
    private Integer notifyType;
    private Integer localStatus;
    private Integer remoteStatus;
    private Date localCompleteTime;
    private Date remoteCompleteTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber == null ? null : orderSequenceNumber.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Integer notifyType) {
        this.notifyType = notifyType;
    }

    public Integer getLocalStatus() {
        return localStatus;
    }

    public void setLocalStatus(Integer localStatus) {
        this.localStatus = localStatus;
    }

    public Integer getRemoteStatus() {
        return remoteStatus;
    }

    public void setRemoteStatus(Integer remoteStatus) {
        this.remoteStatus = remoteStatus;
    }

    public Date getLocalCompleteTime() {
        return localCompleteTime;
    }

    public void setLocalCompleteTime(Date localCompleteTime) {
        this.localCompleteTime = localCompleteTime;
    }

    public Date getRemoteCompleteTime() {
        return remoteCompleteTime;
    }

    public void setRemoteCompleteTime(Date remoteCompleteTime) {
        this.remoteCompleteTime = remoteCompleteTime;
    }
}