package com.xlibao.saas.market.service.order.properties;

/**
 * @author chinahuangxc on 2017/9/3.
 */
public enum PropertiesTypeEnum {

    CONTAINER(1, "货柜"),
    ASK(2, "询问"),
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