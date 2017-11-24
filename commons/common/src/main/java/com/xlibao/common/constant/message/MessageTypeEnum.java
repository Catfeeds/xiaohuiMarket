package com.xlibao.common.constant.message;

/**
 * @author chinahuangxc on 2017/4/27.
 */
public enum MessageTypeEnum {

    /** 1 - 新订单 */
    NEW_ORDER(1, "新订单"),
    /** 2 - 接受订单 */
    ACCEPT_ORDER(2, "接受订单"),
    /** 3 - 订单发货 */
    DELIVERY_ORDER(3, "订单发货"),
    /** 4 - 配送订单 */
    DISTRIBUTION_ORDER(4, "配送订单"),
    /** 5 - 订单送达 */
    ARRIVE_ORDER(5, "订单送达"),
    /** 6 - 确认订单 */
    CONFIRM_ORDER(6, "确认订单"),
    ;

    private int key;
    private String value;

    MessageTypeEnum(int key, String value) {
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
