package com.xlibao.common.constant.item;

/**
 * @author chinahuangxc on 2017/9/12.
 */
public enum ItemRequestSourceEnum {

    /** 1 -- 线上 */
    ON_LINE(1, "线上"),
    /** 2 -- 线下 */
    OFF_LINE(2, "线下"),
    ;

    private int key;
    private String value;

    ItemRequestSourceEnum(int key, String value) {
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