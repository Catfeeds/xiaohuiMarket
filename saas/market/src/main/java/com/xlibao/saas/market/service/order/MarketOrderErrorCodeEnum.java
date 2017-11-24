package com.xlibao.saas.market.service.order;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoRuntimeException;

/**
 * @author chinahuangxc on 2017/8/11.
 */
public enum MarketOrderErrorCodeEnum {

    /** 3000 - 错误的订单状态 */
    ORDER_STATUS_ERROR(3000, "错误的订单状态"),
    /** 3001 - 订单已被接取 */
    ORDER_HAS_ACCEPT(3001, "订单已被接取"),
    /** 3002 - 非本店订单，不能在本店取货 */
    NON_MARKET_ORDER (3002, "非本店订单，不能在本店取货"),
    /** 3003 - 已不能退款 */
    CANNOT_REFUND(3003, "已不能退款"),
    /** 3004 -- 取货数据有误 */
    PICK_UP_DATA_ERROR(3004, "取货数据有误"),
    /** 3005 -- 未完成取货 */
    UN_SUCCESS_PICK_UP(3005, "未完成取货"),
    ;

    private int key;
    private String value;

    MarketOrderErrorCodeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public JSONObject response(String errorMsg) {
        return BasicWebService.fail(getKey(), errorMsg);
    }

    public JSONObject response() {
        return response(getValue());
    }

    public XlibaoRuntimeException throwException() {
        return throwException(getValue());
    }

    public XlibaoRuntimeException throwException(String message) {
        throw new XlibaoRuntimeException(getKey(), message);
    }
}