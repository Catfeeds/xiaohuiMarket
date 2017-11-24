package com.xlibao.common.exception.code;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.exception.XlibaoRuntimeException;

/**
 * <pre>
 *     <b>通行证错误码枚举</b>
 *     <b>注意：</b>设定范围为 -- 10000至19999
 * </pre>
 *
 * @author chinahuangxc on 2017/8/17.
 */
public enum PassportErrorCodeEnum {

    /** 10000 -- 没有找到通行证记录 */
    NOT_FOUND_PASSPORT(10000, "没有找到通行证记录"),
    ;

    private int key;
    private String value;

    PassportErrorCodeEnum(int key, String value) {
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
