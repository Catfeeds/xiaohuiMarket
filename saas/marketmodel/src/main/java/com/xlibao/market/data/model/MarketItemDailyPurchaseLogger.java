package com.xlibao.market.data.model;

import java.util.Date;

public class MarketItemDailyPurchaseLogger {

    private Long id;
    private Long passportId;
    private Long itemId;
    private Long itemTemplateId;
    private Integer hasBuyCount;
    private Date happenDate;

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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(Long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public Integer getHasBuyCount() {
        return hasBuyCount;
    }

    public void setHasBuyCount(Integer hasBuyCount) {
        this.hasBuyCount = hasBuyCount;
    }

    public Date getHappenDate() {
        return happenDate;
    }

    public void setHappenDate(Date happenDate) {
        this.happenDate = happenDate;
    }
}