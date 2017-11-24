package com.xlibao.common.exception.code;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoRuntimeException;

/**
 * <pre>
 *     <b>订单错误码枚举类</b>
 *     <b>注意：</b>设定范围为 -- 20000至29999
 * </pre>
 *
 * @author chinahuangxc on 2017/7/31.
 */
public enum OrderErrorCodeEnum {

    /** 20000 -- 找不到订单 */
    NOT_FOUND_ORDER(20000, "找不到订单"),
    /** 20001 -- 订单已支付 */
    HAS_PAYMENT_ORDER(20001, "订单已支付"),
    /** 20002 -- 订单已完成 */
    COMPLETE_ORDER(20002, "订单已完成"),
    /** 20003 -- 支付订单失败 */
    FAIL_PAYMENT_ORDER(20003, "支付订单失败"),
    /** 20004 -- 订单已取消 */
    CANCELED_ORDER(20004, "订单已被取消"),
    /** 20005 -- 订单未支付 */
    UN_PAYMENT_ORDER(20005, "订单未支付"),
    /** 20006 -- 订单已在配送中 */
    HAS_DISTRIBUTION_ORDER(20006, "订单已在配送中"),
    /** 20007 -- 订单已送达 */
    HAS_ARRIVE_ORDER(20007, "订单已送达"),
    /** 20008 -- 请完善收货地址 */
    PERFECT_RECEIPT_ADDRESS(20008, "请完善收货地址"),
    /** 20009 -- 不能退款 */
    CANNOT_REFUND(20009, "不能退款"),
    /** 20010 -- 退款失败 */
    REFUND_FAIL(20010, "退款失败"),
    /** 20011 -- 刷新订单状态错误 */
    REFRESH_STATUS_ERROR(20011, "刷新订单状态错误"),
    /** 20012 -- 订单状态有误 */
    ORDER_STATUS_ERROR(20012, "订单状态有误"),
    ;

    private int key;
    private String value;

    OrderErrorCodeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public JSONObject response() {
        return response(getValue());
    }

    public JSONObject response(String errorMsg) {
        return BasicWebService.fail(getKey(), errorMsg);
    }

    public XlibaoRuntimeException throwException() {
        return throwException(getValue());
    }

    public XlibaoRuntimeException throwException(String message) {
        return new XlibaoRuntimeException(getKey(), message);
    }
}