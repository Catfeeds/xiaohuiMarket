package com.xlibao.saas.market.service.item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/21.
 */
public enum DiscountTypeEnum {

    /** 0 - 不参与促销 */
    NORMAL(0, "无促销活动"),
    /** 1 - 促销类型 */
    DISCOUNT(1, "促销"),
    /** 2 - 特价类型 */
    SPECIAL_OFFER(2, "特价"),;

    private int key;
    private String value;

    DiscountTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, DiscountTypeEnum> discountTypeMap = new HashMap<>();

    static {
        DiscountTypeEnum[] discountTypeEnums = DiscountTypeEnum.values();
        for (DiscountTypeEnum discountTypeEnum : discountTypeEnums) {
            discountTypeMap.put(discountTypeEnum.getKey(), discountTypeEnum);
        }
    }

    public static DiscountTypeEnum getDiscountTypeEnum(int key) {
        return discountTypeMap.get(key);
    }
}