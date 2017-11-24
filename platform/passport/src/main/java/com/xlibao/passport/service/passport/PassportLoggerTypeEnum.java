package com.xlibao.passport.service.passport;

/**
 * @author chinahuangxc on 2017/2/8.
 */
public enum PassportLoggerTypeEnum {

    REGISTER(1, "注册帐号"),
    LOGIIN(2, "登录帐号"),;

    private int key;
    private String value;

    PassportLoggerTypeEnum(int key, String value) {
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