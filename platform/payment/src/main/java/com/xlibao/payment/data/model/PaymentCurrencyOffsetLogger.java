package com.xlibao.payment.data.model;

import java.util.Date;

public class PaymentCurrencyOffsetLogger {

    private Long id;
    private Long passportId;
    private Integer channelId;
    private Integer currencyType;
    private Long beforeAmount;
    private Long offsetAmount;
    private Long afterAmount;
    private String transTitle;
    private String transType;
    private Integer relationTransType;
    private String relationTransSequence;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Long getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(Long beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public Long getOffsetAmount() {
        return offsetAmount;
    }

    public void setOffsetAmount(Long offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    public Long getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(Long afterAmount) {
        this.afterAmount = afterAmount;
    }

    public String getTransTitle() {
        return transTitle;
    }

    public void setTransTitle(String transTitle) {
        this.transTitle = transTitle == null ? null : transTitle.trim();
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }

    public Integer getRelationTransType() {
        return relationTransType;
    }

    public void setRelationTransType(Integer relationTransType) {
        this.relationTransType = relationTransType;
    }

    public String getRelationTransSequence() {
        return relationTransSequence;
    }

    public void setRelationTransSequence(String relationTransSequence) {
        this.relationTransSequence = relationTransSequence == null ? null : relationTransSequence.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}