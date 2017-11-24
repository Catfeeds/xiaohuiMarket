package com.xlibao.common.constant.logistics;

/**
 * @author chinahuangxc on 2017/5/10.
 */
public enum AttendanceRecodeEnum {

    /** 1 -- 当天 */
    TODAY(1, "当天"),
    /** 2 -- 全部 */
    ALL(2, "全部"),;

    private int key;
    private String value;

    AttendanceRecodeEnum(int key, String value) {
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