package com.xlibao.common.constant.passport;

/**
 * @author chinahuangxc on 2017/3/29.
 */
public enum AddressStatusEnum {

    /** 0 -- 启用 */
    ENABLE(0, "启用"),
    /** 1 -- 无效 */
    INVALID(1, "无效"),
    /** 2 -- 默认 */
    DEFAULT(2, "默认"),;

    private byte key;
    private String value;

    AddressStatusEnum(int key, String value) {
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