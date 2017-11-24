package com.xlibao.saas.market.service.item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc  on 2017/2/28.
 */
public enum ItemSortEnum {

    /** 0 -- 默认 */
    DEFAULT(0, "默认"),
    /** 1 -- 价格 */
    MONEY(1, "价格"),
    /** 2 -- 销量 */
    SALES(2, "销量"),
    ;

    private int key;
    private String value;

    ItemSortEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, ItemSortEnum> itemSorts = new HashMap<Integer, ItemSortEnum>();

    static {
        ItemSortEnum[] itemSortEnumArray = ItemSortEnum.values();
        for (ItemSortEnum itemSortEnum : itemSortEnumArray) {
            itemSorts.put(itemSortEnum.getKey(), itemSortEnum);
        }
    }

    public static ItemSortEnum getItemSortEnum(int key) {
        return itemSorts.get(key);
    }
}