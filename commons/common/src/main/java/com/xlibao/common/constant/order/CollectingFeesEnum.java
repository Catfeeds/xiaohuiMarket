package com.xlibao.common.constant.order;

/**
 * @author chinahuangxc on 2017/3/7.
 */
public enum CollectingFeesEnum {

    /** 0 -- 不代收 */
    UN_COLLECTION(0, "不代收"),
    /** 1 -- 代收(货到付款) */
    COLLECTION(1, "代收"),
    ;

    private byte key;
    private String value;

    CollectingFeesEnum(int key, String value) {
        this.key = (byte) key;
        this.value = value;
    }

    public byte getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}