package com.market.courier.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhumg on 9/26.
 */
public enum OrderRoleTypeEnum {

    /** 1、消费者 */
    CONSUMER(1, "消费者"), // toc时为c端 tob为发起方
    /** 2、商家 */
    MERCHANT(2, "商家"), // 商家或仓库
    /** 3、快递员 */
    COURIER(3, "快递员"),;

    private int key;
    private String value;

    OrderRoleTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, OrderRoleTypeEnum> ORDER_ROLE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        OrderRoleTypeEnum[] orderRoleTypeEnums = OrderRoleTypeEnum.values();
        for (OrderRoleTypeEnum orderRoleTypeEnum : orderRoleTypeEnums) {
            ORDER_ROLE_TYPE_ENUM_MAP.put(orderRoleTypeEnum.getKey(), orderRoleTypeEnum);
        }
    }

    public static OrderRoleTypeEnum getOrderRoleTypeEnum(int key) {
        return ORDER_ROLE_TYPE_ENUM_MAP.get(key);
    }
}
