package com.xlibao.payment.data.model;

import java.util.Date;

public class PaymentCurrencyAccount {

    private Long id;
    private Long passportId;
    private Long channelId;
    private Integer currencyType;
    private String name;
    private Long currentAmount = 0L;
    private Long freezeAmount = 0L;
    private Long totalIntoAmount = 0L;
    private Long totalOutputAmount = 0L;
    private Date createTime = new Date();

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

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Long getTotalIntoAmount() {
        return totalIntoAmount;
    }

    public void setTotalIntoAmount(Long totalIntoAmount) {
        this.totalIntoAmount = totalIntoAmount;
    }

    public Long getTotalOutputAmount() {
        return totalOutputAmount;
    }

    public void setTotalOutputAmount(Long totalOutputAmount) {
        this.totalOutputAmount = totalOutputAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}