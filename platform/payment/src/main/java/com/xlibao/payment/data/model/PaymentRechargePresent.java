package com.xlibao.payment.data.model;

import java.util.Date;

public class PaymentRechargePresent {

    private Long id;
    private Long minimumRechargeAmount;
    private Integer currencyType;
    private Long presentAmount;
    private String instruction;
    private Date effectiveTime;
    private Date failureTime;
    private Integer status;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMinimumRechargeAmount() {
        return minimumRechargeAmount;
    }

    public void setMinimumRechargeAmount(Long minimumRechargeAmount) {
        this.minimumRechargeAmount = minimumRechargeAmount;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Long getPresentAmount() {
        return presentAmount;
    }

    public void setPresentAmount(Long presentAmount) {
        this.presentAmount = presentAmount;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction == null ? null : instruction.trim();
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
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
}