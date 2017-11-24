package com.xlibao.common.constant.order;

/**
 * @author chinahuangxc on 2016/12/17.
 */
public enum OrderSequenceNumberStatusEnum {

    /** 0 -- 正常 */
    NORMAL(0, "正常"),
    /** 1 -- 已使用 */
    HAS_USE(1, "已使用"),
    /** 2 -- 失效 */
    INVALID(2, "失效"),;

    private int key;
    private String value;

    OrderSequenceNumberStatusEnum(int key, String value) {
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