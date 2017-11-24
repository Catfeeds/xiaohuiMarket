package com.xlibao.saas.market.service.activity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/19.
 */
public enum RecommendItemTypeEnum {

    ITEM_TYPE(1, "商品分类"),
    ITEM(2, "商品"),;

    private int key;
    private String value;

    RecommendItemTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, RecommendItemTypeEnum> RECOMMEND_ITEM_TYPE_ENUM_MAP = new HashMap<>();

    static {
        RecommendItemTypeEnum[] recommendItemTypeEnums = RecommendItemTypeEnum.values();
        for (RecommendItemTypeEnum recommendItemTypeEnum : recommendItemTypeEnums) {
            RECOMMEND_ITEM_TYPE_ENUM_MAP.put(recommendItemTypeEnum.getKey(), recommendItemTypeEnum);
        }
    }

    public static RecommendItemTypeEnum getRecommendItemTypeEnum(int key) {
        return RECOMMEND_ITEM_TYPE_ENUM_MAP.get(key);
    }
}