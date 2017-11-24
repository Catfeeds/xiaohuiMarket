package com.xlibao.market.data.model;

import java.util.Date;

public class MarketOrderUnacceptLogger {

    private Long id;
    private String orderSequenceNumber;
    private Long targetPassportId;
    private Long marketId;
    private Date pushedTime = new Date();

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

    public Long getTargetPassportId() {
        return targetPassportId;
    }

    public void setTargetPassportId(Long targetPassportId) {
        this.targetPassportId = targetPassportId;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Date getPushedTime() {
        return pushedTime;
    }

    public void setPushedTime(Date pushedTime) {
        this.pushedTime = pushedTime;
    }
}