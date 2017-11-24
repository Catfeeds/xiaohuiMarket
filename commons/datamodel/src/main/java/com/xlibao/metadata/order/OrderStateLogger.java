package com.xlibao.metadata.order;

import java.util.Date;

public class OrderStateLogger {

    private Long id;
    private Long orderId;
    private Long shippingPassportId;
    private Integer orderType;
    private Integer beforeStatus;
    private Integer status;
    private Date operatorTime;
    private String operatorPassportId;
    private String operatorName;
    private String operatorDescribe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getShippingPassportId() {
        return shippingPassportId;
    }

    public void setShippingPassportId(Long shippingPassportId) {
        this.shippingPassportId = shippingPassportId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getBeforeStatus() {
        return beforeStatus;
    }

    public void setBeforeStatus(Integer beforeStatus) {
        this.beforeStatus = beforeStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public String getOperatorPassportId() {
        return operatorPassportId;
    }

    public void setOperatorPassportId(String operatorPassportId) {
        this.operatorPassportId = operatorPassportId == null ? null : operatorPassportId.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getOperatorDescribe() {
        return operatorDescribe;
    }

    public void setOperatorDescribe(String operatorDescribe) {
        this.operatorDescribe = operatorDescribe == null ? null : operatorDescribe.trim();
    }
}