package com.xlibao.common.constant.payment;

/**
 * @author chinahuangxc on 2016/12/3.
 */
public enum TransStatusEnum {

    /** 1 -- 未支付(审核中) */
    TRADE_DEFAULT(1, "未支付"),
    /** 2 -- 交易创建 */
    TRADE_CREATED(2, "交易创建"),
    /** 4 -- 支付成功 -- 客户端 */
    TRADE_SUCCESS_CLIENT(4, "支付成功 -- 客户端"),
    /** 8 -- 支付成功 -- 服务器 */
    TRADE_SUCCESS_SERVER(8, "支付成功 -- 服务器"),
    /** 12 -- 交易成功 */
    TRADE_FINISHED(12, "交易成功"),
    /** 16 -- 交易关闭 */
    TRADE_CLOSED(16, "交易关闭"),
    /** 32 -- 拒绝交易 */
    TRADE_REJECT(32, "拒绝交易"),
    ;

    private int key;
    private String value;

    TransStatusEnum(int key, String value) {
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
