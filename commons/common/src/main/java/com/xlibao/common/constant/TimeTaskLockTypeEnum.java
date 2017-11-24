package com.xlibao.common.constant;

/**
 * @author chinahuangxc on 2017/5/7.
 */
public enum TimeTaskLockTypeEnum {

    MARKET_ORDER_STATUS(11, "商店订单状态修正"),
    ;

    private int key;
    private String value;

    TimeTaskLockTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}