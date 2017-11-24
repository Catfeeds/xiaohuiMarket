package com.xlibao.market.data.model;

import java.util.Date;

public class MarketItemLocationStockLogger {

    private Long id;
    private Long itemId;
    private String locationCode;
    private Integer offsetStock;
    private Integer beforeStock;
    private Integer afterStock;
    private Long operatorPassportId;
    private String operatorPassportName;
    private Integer operationType;
    private Date operationTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode == null ? null : locationCode.trim();
    }

    public Integer getOffsetStock() {
        return offsetStock;
    }

    public void setOffsetStock(Integer offsetStock) {
        this.offsetStock = offsetStock;
    }

    public Integer getBeforeStock() {
        return beforeStock;
    }

    public void setBeforeStock(Integer beforeStock) {
        this.beforeStock = beforeStock;
    }

    public Integer getAfterStock() {
        return afterStock;
    }

    public void setAfterStock(Integer afterStock) {
        this.afterStock = afterStock;
    }

    public Long getOperatorPassportId() {
        return operatorPassportId;
    }

    public void setOperatorPassportId(Long operatorPassportId) {
        this.operatorPassportId = operatorPassportId;
    }

    public String getOperatorPassportName() {
        return operatorPassportName;
    }

    public void setOperatorPassportName(String operatorPassportName) {
        this.operatorPassportName = operatorPassportName == null ? null : operatorPassportName.trim();
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}