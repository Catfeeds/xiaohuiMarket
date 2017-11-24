package com.xlibao.common.constant.payment;

/**
 * @author chinahuangxc on 2016/11/29.
 */
public enum TransTypeEnum {

    /** 1 -- 支付 */
    PAYMENT(1, "支付"),
    /** 2 -- 充值 */
    RECHARGE(2, "充值"),
    /** 3 -- 代付 */
    OTHER_PAYMENT(3, "代付"),
    /** 4 -- 便利店收入 */
    MARKET_INCOME(4, "便利店收入"),
    /** 5 -- 便利店支付 */
    MARKET_PAYMENT(5, "便利店支付"),
    /** 6 -- 提现 */
    DRAW_CASH(6, "提现"),
    ;

    private int key;
    private String value;

    TransTypeEnum(int key, String value) {
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