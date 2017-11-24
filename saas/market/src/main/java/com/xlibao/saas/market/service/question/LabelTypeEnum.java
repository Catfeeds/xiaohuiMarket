package com.xlibao.saas.market.service.question;

/**
 * @author chinahuangxc on 2017/9/7.
 */
public enum LabelTypeEnum {

    /** 1 -- 退款 */
    REFUND(1, "退款"),
    ;

    private int key;
    private String value;

    LabelTypeEnum(int key, String value) {
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
