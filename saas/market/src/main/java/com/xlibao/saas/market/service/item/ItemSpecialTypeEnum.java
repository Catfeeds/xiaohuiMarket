package com.xlibao.saas.market.service.item;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     <b>商品特殊分类</b>
 * </pre>
 *
 * @author chinahuangxc on 2017/7/21.
 */
public enum ItemSpecialTypeEnum {

    DEFAULT(0, "全部"),
    NEW(-100, "新品"),
    DISCOUNT(-99, "促销"),
    OFFER(-98, "特价"),
    RECOMMEND(-97, "推荐"),
    ;

    private long key;
    private String value;

    ItemSpecialTypeEnum(long key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Long, ItemSpecialTypeEnum> itemSpecialTypeEnums = new HashMap<>();

    static {
        ItemSpecialTypeEnum[] itemSpecialTypeEnumArray = ItemSpecialTypeEnum.values();
        for (ItemSpecialTypeEnum itemSpecialTypeEnum : itemSpecialTypeEnumArray) {
            itemSpecialTypeEnums.put(itemSpecialTypeEnum.getKey(), itemSpecialTypeEnum);
        }
    }

    public static ItemSpecialTypeEnum getItemSpecialTypeEnum(long key) {
        return itemSpecialTypeEnums.get(key);
    }
}