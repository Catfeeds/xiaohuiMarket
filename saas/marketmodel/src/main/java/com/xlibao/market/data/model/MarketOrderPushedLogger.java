package com.xlibao.market.data.model;

import java.util.Date;

public class MarketOrderPushedLogger {

    private Long id;
    private String orderSequenceNumber;
    private Long targetPassportId;
    private Integer pushType;
    private String pushTitle;
    private String pushMsgContent;
    private String pushResult;
    private Date pushTime;
    private Byte status;
    private String feedbackMsgContent;
    private Integer feedbackStatus;
    private Date feedbackTime;

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
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public Long getTargetPassportId() {
        return targetPassportId;
    }

    public void setTargetPassportId(Long targetPassportId) {
        this.targetPassportId = targetPassportId;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle == null ? null : pushTitle.trim();
    }

    public String getPushMsgContent() {
        return pushMsgContent;
    }

    public void setPushMsgContent(String pushMsgContent) {
        this.pushMsgContent = pushMsgContent == null ? null : pushMsgContent.trim();
    }

    public String getPushResult() {
        return pushResult;
    }

    public void setPushResult(String pushResult) {
        this.pushResult = pushResult == null ? null : pushResult.trim();
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getFeedbackMsgContent() {
        return feedbackMsgContent;
    }

    public void setFeedbackMsgContent(String feedbackMsgContent) {
        this.feedbackMsgContent = feedbackMsgContent == null ? null : feedbackMsgContent.trim();
    }

    public Integer getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(Integer feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}