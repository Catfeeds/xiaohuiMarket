package com.xlibao.common.constant.order;

/**
 * @author chinahuangxc on 2017/8/2.
 */
public enum DeliverTypeEnum {

    /** 1 -- 自提 */
    PICKED_UP(1, "自提"),
    /** 2 -- 配送 */
    DISTRIBUTION(2, "配送"),
    ;

    private int key;
    private String value;

    DeliverTypeEnum(int key, String value) {
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