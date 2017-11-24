package com.xlibao.market.data.model;

import java.util.Date;

public class MarketPrepareAction {

    private Long id;
    private Long marketId;
    private Long actionPassportId;
    private Long executorPassportId;
    private String itemLocation;
    private Long hopeItemTemplateId;
    private String hopeItemBarcode;
    private Integer hasCompleteQuantity;
    private Integer hopeItemQuantity;
    private Date hopeExecutorDate;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Date completeTime;

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

    public Long getActionPassportId() {
        return actionPassportId;
    }

    public void setActionPassportId(Long actionPassportId) {
        this.actionPassportId = actionPassportId;
    }

    public Long getExecutorPassportId() {
        return executorPassportId;
    }

    public void setExecutorPassportId(Long executorPassportId) {
        this.executorPassportId = executorPassportId;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation == null ? null : itemLocation.trim();
    }

    public Long getHopeItemTemplateId() {
        return hopeItemTemplateId;
    }

    public void setHopeItemTemplateId(Long hopeItemTemplateId) {
        this.hopeItemTemplateId = hopeItemTemplateId;
    }

    public String getHopeItemBarcode() {
        return hopeItemBarcode;
    }

    public void setHopeItemBarcode(String hopeItemBarcode) {
        this.hopeItemBarcode = hopeItemBarcode == null ? null : hopeItemBarcode.trim();
    }

    public Integer getHasCompleteQuantity() {
        return hasCompleteQuantity;
    }

    public void setHasCompleteQuantity(Integer hasCompleteQuantity) {
        this.hasCompleteQuantity = hasCompleteQuantity;
    }

    public Integer getHopeItemQuantity() {
        return hopeItemQuantity;
    }

    public void setHopeItemQuantity(Integer hopeItemQuantity) {
        this.hopeItemQuantity = hopeItemQuantity;
    }

    public Date getHopeExecutorDate() {
        return hopeExecutorDate;
    }

    public void setHopeExecutorDate(Date hopeExecutorDate) {
        this.hopeExecutorDate = hopeExecutorDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
}