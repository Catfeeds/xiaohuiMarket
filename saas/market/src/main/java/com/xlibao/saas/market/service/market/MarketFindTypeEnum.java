package com.xlibao.saas.market.service.market;

/**
 * @author chinahuangxc on 2017/7/21.
 */
public enum MarketFindTypeEnum {

    /** 1 -- 选择商店 */
    CLIENT_PROVIDE(1, "选择商店"),
    /** 2 -- 最近访问 */
    RECENT_ACCESS(2, "最近访问"),
    /** 3 -- 距离最近 */
    LOCATION(3, "距离最近"),
    ;

    private int key;
    private String value;

    MarketFindTypeEnum(int key, String value) {
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