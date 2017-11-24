package com.xlibao.saas.market.service.item;

/**
 * @author chinahuangxc on 2017/9/8.
 */
public enum ItemStockOffsetTypeEnum {

    BUY(1, "购买"),
    OFF_SHELVES(2, "下架"),
    ON_SHELVES(3, "上架"),

    ;
    private int key;
    private String value;

    ItemStockOffsetTypeEnum(int key, String value) {
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