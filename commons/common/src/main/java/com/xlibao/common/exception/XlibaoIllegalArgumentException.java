package com.xlibao.common.exception;

import com.xlibao.common.BasicWebService;

/**
 * @author chinahuangxc on 2017/1/26.
 */
public class XlibaoIllegalArgumentException extends IllegalArgumentException {

    private int code = BasicWebService.FAIL_CODE;

    public XlibaoIllegalArgumentException() {
        super();
    }

    public XlibaoIllegalArgumentException(String message) {
        super(message);
    }

    public XlibaoIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public XlibaoIllegalArgumentException(Throwable cause) {
        super(cause);
    }

    public XlibaoIllegalArgumentException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}