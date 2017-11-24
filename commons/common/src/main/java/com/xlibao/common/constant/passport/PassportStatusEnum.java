package com.xlibao.common.constant.passport;

/**
 * @author chinahuangxc on 2016/11/7.
 */
public enum PassportStatusEnum {

    /** 0 -- 正常 */
    NORMAL(0, "正常"),
    /** 1 -- 禁言 */
    FORBID_TALK(1, "禁言"),
    /** 2 -- 禁登 */
    FORBID_LOGIN(2, "禁登"),
    /** 3 -- 黑名单 */
    BACK_LIST(3, "黑名单"),
    /** 4 -- 未完善资料 */
    UN_PERFECT_INFORMATION(4, "未完善资料"),
    ;

    private int key;
    private String value;

    PassportStatusEnum(int key, String value) {
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