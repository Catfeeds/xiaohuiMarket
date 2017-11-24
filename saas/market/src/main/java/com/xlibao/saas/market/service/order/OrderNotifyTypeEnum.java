package com.xlibao.saas.market.service.order;

/**
 * @author chinahuangxc on 2017/8/14.
 */
public enum OrderNotifyTypeEnum {

    MARKET_LOGIC(1, "商店业务"),
    HARDWARE(2, "硬件业务"),;

    private int key;
    private String value;

    OrderNotifyTypeEnum(int key, String value) {
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