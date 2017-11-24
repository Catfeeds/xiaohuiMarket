package com.xlibao.common.constant.passport;

/**
 * @author chinahuangxc on 2017/4/19.
 */
public enum PersonalKeyEnum {

    HEAD_IMAGE("headImage"),
    ID_AUTHENTICATION("IDAuthentication"),
    ;

    private String key;

    PersonalKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}