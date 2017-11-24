package com.xlibao.saas.market.service.market;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoRuntimeException;

/**
 * @author chinahuangxc on 2017/7/19.
 */
public enum MarketErrorCodeEnum {

    /** 1000 -- 您所在区域暂时未找到合适的商店 */
    CAN_NOT_FIND_MARKET(1000, "您所在区域暂时未找到合适的商店"),
    /** 1001 -- 错误的商店信息 */
    ERROR_MARKET_INFORMATION(1001, "错误的商店信息"),
    /** 1002 -- 不处于维护中 */
    DON_NOT_MAINTAIN(1002, "不处于维护中"),
    /** 1003 -- 商店状态错误 */
    MARKET_STATUS_ERROR(1003, "商店状态错误"),
    /** 1100 -- 货架信息有误 */
    SHELVES_LOCATION_ERROR(1100, "货架信息有误"),
    /** 1101 -- 货架位置上的任务有误 */
    SHELVES_LOCATION_TASK_ERROR(1101, "货架位置上的任务有误"),
    /** 1200 -- 找不到关注关系记录 */
    CAN_NOT_FOUND_FOCUS_RELATIONSHIP(1200, "找不到关注关系记录"),
    ;

    private int key;
    private String value;

    MarketErrorCodeEnum(int key, String value) {
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