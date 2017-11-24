package com.xlibao.common.constant.passport;

/**
 * @author chinahuangxc on 2017/2/13.
 */
public enum PassportTypeEnum {

    DEFAULT(0, "系统默认"),;

    private int key;
    private String value;

    PassportTypeEnum(int key, String value) {
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