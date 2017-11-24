package com.xlibao.saas.market.service.market;

/**
 * @author chinahuangxc on 2017/9/21.
 */
public enum MarketOnlineStatusEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    ;

    private int key;
    private String value;

    MarketOnlineStatusEnum(int key, String value) {
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
