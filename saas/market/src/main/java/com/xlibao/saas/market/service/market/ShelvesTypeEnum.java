package com.xlibao.saas.market.service.market;

/**
 * @author chinahuangxc on 2017/8/21.
 */
public enum ShelvesTypeEnum {

    /** 1 -- 商店 */
    MARKET(1, "商店"),
    /** 2 -- 组 */
    GROUP(2, "组"),
    /** 3 -- 单元 */
    UNIT(3, "单元"),
    /** 4 -- 楼层 */
    FLOOR(4, "楼层"),
    /** 5 -- 弹夹 */
    CLIP(5, "弹夹"),
    ;

    private int key;
    private String value;

    ShelvesTypeEnum(int key, String value) {
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