package com.xlibao.market.data.model;

import java.util.Date;

public class MarketShelvesDailyTaskLogger {

    private Long id;
    private Long passportId;
    private String passportName;
    private Long marketId;
    private Integer hopeTotalItemQuantity = 1;
    private Integer actualItemQuantity = 1;
    private Integer hopeTotalBarcodeQuantity = 1;
    private Integer actualBarcodeQuantity = 1;
    private String mark;
    private Date hopeExecutorDate;
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

    public String getPassportName() {
        return passportName;
    }

    public void setPassportName(String passportName) {
        this.passportName = passportName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Integer getHopeTotalItemQuantity() {
        return hopeTotalItemQuantity;
    }

    public void setHopeTotalItemQuantity(Integer hopeTotalItemQuantity) {
        this.hopeTotalItemQuantity = hopeTotalItemQuantity;
    }

    public Integer getActualItemQuantity() {
        return actualItemQuantity;
    }

    public void setActualItemQuantity(Integer actualItemQuantity) {
        this.actualItemQuantity = actualItemQuantity;
    }

    public Integer getHopeTotalBarcodeQuantity() {
        return hopeTotalBarcodeQuantity;
    }

    public void setHopeTotalBarcodeQuantity(Integer hopeTotalBarcodeQuantity) {
        this.hopeTotalBarcodeQuantity = hopeTotalBarcodeQuantity;
    }

    public Integer getActualBarcodeQuantity() {
        return actualBarcodeQuantity;
    }

    public void setActualBarcodeQuantity(Integer actualBarcodeQuantity) {
        this.actualBarcodeQuantity = actualBarcodeQuantity;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public Date getHopeExecutorDate() {
        return hopeExecutorDate;
    }

    public void setHopeExecutorDate(Date hopeExecutorDate) {
        this.hopeExecutorDate = hopeExecutorDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}