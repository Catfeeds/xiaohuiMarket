package com.xlibao.common.constant.order;

/**
 * @author chinahuangxc on 2017/3/13.
 */
public enum PushTypeEnum {

    /** 0 -- 不进行推送 */
    UN_PUSH(0, "不进行推送"),
    /** 1 -- 下单后推送 */
    AFTER_CREATE(1, "下单后推送"),
    /** 2 -- 支付后推送 */
    AFTER_PAYMENT(2, "支付后推送"),
    ;

    private int key;
    private String value;

    PushTypeEnum(int key, String value) {
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
