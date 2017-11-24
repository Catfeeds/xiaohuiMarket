package com.xlibao.payment.data.model;

import java.util.Date;

public class PaymentTakeRule {

    private Long id;
    private Integer takeMode;
    private String takeDesc;
    private Integer targetType;
    private Long lowCost;
    private Long highCost;
    private Integer rate;
    private Long singleAmountLimit;
    private Long dayAmountLimit;
    private Integer dayCountLimit;
    private Byte holidayDelay;
    private Integer delayHour;
    private Date ruleEffectTime;
    private Date ruleInvalidTime;
    private String showImage;
    private Byte defaultOption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTakeMode() {
        return takeMode;
    }

    public void setTakeMode(Integer takeMode) {
        this.takeMode = takeMode;
    }

    public String getTakeDesc() {
        return takeDesc;
    }

    public void setTakeDesc(String takeDesc) {
        this.takeDesc = takeDesc == null ? null : takeDesc.trim();
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Long getLowCost() {
        return lowCost;
    }

    public void setLowCost(Long lowCost) {
        this.lowCost = lowCost;
    }

    public Long getHighCost() {
        return highCost;
    }

    public void setHighCost(Long highCost) {
        this.highCost = highCost;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Long getSingleAmountLimit() {
        return singleAmountLimit;
    }

    public void setSingleAmountLimit(Long singleAmountLimit) {
        this.singleAmountLimit = singleAmountLimit;
    }

    public Long getDayAmountLimit() {
        return dayAmountLimit;
    }

    public void setDayAmountLimit(Long dayAmountLimit) {
        this.dayAmountLimit = dayAmountLimit;
    }

    public Integer getDayCountLimit() {
        return dayCountLimit;
    }

    public void setDayCountLimit(Integer dayCountLimit) {
        this.dayCountLimit = dayCountLimit;
    }

    public Byte getHolidayDelay() {
        return holidayDelay;
    }

    public void setHolidayDelay(Byte holidayDelay) {
        this.holidayDelay = holidayDelay;
    }

    public Integer getDelayHour() {
        return delayHour;
    }

    public void setDelayHour(Integer delayHour) {
        this.delayHour = delayHour;
    }

    public Date getRuleEffectTime() {
        return ruleEffectTime;
    }

    public void setRuleEffectTime(Date ruleEffectTime) {
        this.ruleEffectTime = ruleEffectTime;
    }

    public Date getRuleInvalidTime() {
        return ruleInvalidTime;
    }

    public void setRuleInvalidTime(Date ruleInvalidTime) {
        this.ruleInvalidTime = ruleInvalidTime;
    }

    public String getShowImage() {
        return showImage;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage == null ? null : showImage.trim();
    }

    public Byte getDefaultOption() {
        return defaultOption;
    }

    public void setDefaultOption(Byte defaultOption) {
        this.defaultOption = defaultOption;
    }
}