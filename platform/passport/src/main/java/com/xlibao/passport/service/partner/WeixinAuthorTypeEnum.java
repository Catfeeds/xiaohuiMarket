package com.xlibao.passport.service.partner;

/**
 * @author chinahuangxc on 2017/8/23.
 */
public enum WeixinAuthorTypeEnum {

    /** 1 -- 原生授权方式 */
    OAUTH2(1, "oauth2"),
    /** 2 -- 小程序授权方式 */
    JS_CODE_SESSION(2, "jscode2session"),;

    private int key;
    private String value;

    WeixinAuthorTypeEnum(int key, String value) {
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