package com.xlibao.common.constant.passport;

/**
 * @author chinahuangxc on 2017/4/8.
 */
public enum PropertiesTypeEnum {

    /** 1 -- 角色 */
    ROLE(1, "角色"),
    /** 2 -- 个人信息 */
    PERSONAL(2, "个人信息"),
    ;

    private int key;
    private String value;

    PropertiesTypeEnum(int key, String value) {
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