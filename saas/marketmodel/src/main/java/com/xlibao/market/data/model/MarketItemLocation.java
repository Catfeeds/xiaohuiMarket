package com.xlibao.market.data.model;

import java.util.Date;

public class MarketItemLocation {

    private Long id;
    private Long marketId;
    private Long itemId;
    private Long itemTemplateId;
    private String locationCode;
    private Integer stock;
    private Date createTime = new Date();
    private Date lastModifyTime = new Date();

    public static MarketItemLocation newInstance(long marketId, long itemId, long itemTemplateId, String location, int stock) {
        MarketItemLocation itemLocation = new MarketItemLocation();
        itemLocation.setMarketId(marketId);
        itemLocation.setItemId(itemId);
        itemLocation.setItemTemplateId(itemTemplateId);
        itemLocation.setLocationCode(location);
        itemLocation.setStock(stock);

        return itemLocation;
    }

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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode == null ? null : locationCode.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}