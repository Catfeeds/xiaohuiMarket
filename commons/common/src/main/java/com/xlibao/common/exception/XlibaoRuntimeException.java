package com.xlibao.common.exception;

import com.xlibao.common.BasicWebService;

/**
 * @author chinahuangxc on 2017/1/26.
 */
public class XlibaoRuntimeException extends RuntimeException {

    private int code = BasicWebService.FAIL_CODE;

    public XlibaoRuntimeException() {
        super();
    }

    public XlibaoRuntimeException(String message) {
        super(message);
    }

    public XlibaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XlibaoRuntimeException(Throwable cause) {
        super(cause);
    }

    public XlibaoRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
