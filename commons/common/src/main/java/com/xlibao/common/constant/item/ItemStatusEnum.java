package com.xlibao.common.constant.item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/3/2.
 */
public enum ItemStatusEnum {

    /** 0 -- 可用 */
    NORMAL(0, "可用(有效)"),
    /** 1 -- 审核中 */
    EXAMINING(1, "审核中"),
    /** 2 -- 审核不通过 */
    NOT_PASS(2, "审核不通过"),
    /** 3 -- 失效 */
    INVALID(3, "失效"),
    /** -1 -- 所有 */
    ALL(-1, "所有"),
    ;

    private int key;
    private String value;

    ItemStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, ItemStatusEnum> ITEM_STATUS_ENUM_MAP = new HashMap<>();

    static {
        ItemStatusEnum[] itemStatusEnums = ItemStatusEnum.values();
        for (ItemStatusEnum itemStatusEnum : itemStatusEnums) {
            ITEM_STATUS_ENUM_MAP.put(itemStatusEnum.getKey(), itemStatusEnum);
        }
    }

    public static ItemStatusEnum getItemStatusEnum(int key) {
        return ITEM_STATUS_ENUM_MAP.get(key);
    }

    public static Map<Integer, ItemStatusEnum> getItemStatusEnumMap() {
        return ITEM_STATUS_ENUM_MAP;
    }
}