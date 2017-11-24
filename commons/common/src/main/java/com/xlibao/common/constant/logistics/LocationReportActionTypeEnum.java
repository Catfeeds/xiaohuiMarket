package com.xlibao.common.constant.logistics;

/**
 * @author chinahuangxc on 2017/5/8.
 */
public enum LocationReportActionTypeEnum {

    BASIC(1, "基本行为"),
    ;

    private int key;
    private String value;

    LocationReportActionTypeEnum(int key, String value) {
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