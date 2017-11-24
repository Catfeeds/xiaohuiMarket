package com.xlibao.common.constant.payment;

/**
 * @author chinahuangxc on 2017/5/4.
 */
public enum BankStatusEnum {

    /** -1 -- 失效 */
    INVALID(-1, "失效"),
    /** 0 -- 普通 */
    NORMAL(0, "普通"),
    /** 1 -- 默认 */
    DEFAULT(1, "默认"),
    /** 2 -- 审核 */
    EXAMINE(2, "审核中"),
    ;

    private int key;
    private String value;

    BankStatusEnum(int key, String value) {
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
