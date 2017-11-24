package com.xlibao.common.constant.payment;

/**
 * @author chinahuangxc on 2017/5/5.
 */
public enum BalanceOfPaymentsEnterTypeEnum {

    /** 0 -- 全部 */
    ALL(0, "全部"),
    /** 1 -- 今日 */
    DAY(1, "今日"),
    /** 2 -- 本月 */
    MONTH(2, "本月"),;

    private int key;
    private String value;

    BalanceOfPaymentsEnterTypeEnum(int key, String value) {
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
