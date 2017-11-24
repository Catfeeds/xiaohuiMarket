package com.xlibao.common.constant.payment;

/**
 * @author chinahuangxc on 2017/5/15.
 */
public enum DrawCashModeEnum {

    /** 1 -- 两个工作日内到账 */
    FORTY_EIGHT_HOURS(1, "两个工作日内到账", false),
    /** 2 -- 两个小时内到账(推荐) */
    TWO_HOURS(2, "两个小时内到账(推荐)", true),
    // TWENTY_FOUR_HOURS(3, "24小时内到账"),
    ;

    private int key;
    private String value;
    private boolean recommend;

    DrawCashModeEnum(int key, String value, boolean recommend) {
        this.key = key;
        this.value = value;
        this.recommend = recommend;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isRecommend() {
        return recommend;
    }
}