package com.xlibao.common.constant.payment;

/**
 * @author chinahuangxc on 2017/5/5.
 */
public enum BankTypeEnum {

    /** 1--储值卡 */
    COMMON_CARD(1, "储值卡"),
    /** 2--信用卡 */
    CREDIT_CARD(2, "信用卡"),;

    private int key;
    private String value;

    BankTypeEnum(int key, String value) {
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
