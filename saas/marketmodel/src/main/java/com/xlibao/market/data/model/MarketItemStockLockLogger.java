package com.xlibao.market.data.model;

import java.util.Date;

public class MarketItemStockLockLogger {

    private Long id;
    private String orderSequenceNumber;
    private Long itemId;
    private Long itemTemplateId;
    private Integer lockQuantity;
    private Integer lockType;
    private Integer lockStatus;
    private Date lockTime;

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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getLockQuantity() {
        return lockQuantity;
    }

    public void setLockQuantity(Integer lockQuantity) {
        this.lockQuantity = lockQuantity;
    }

    public Integer getLockType() {
        return lockType;
    }

    public void setLockType(Integer lockType) {
        this.lockType = lockType;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }
}