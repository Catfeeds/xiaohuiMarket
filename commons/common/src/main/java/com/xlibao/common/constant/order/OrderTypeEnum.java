package com.xlibao.common.constant.order;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/9.
 */
public enum OrderTypeEnum {

    /** 1 - 销售订单 */
    SALE_ORDER_TYPE(1, "销售订单", true),
    /** 2 - 调拨订单 */
    ALLOCATION_ORDER_TYPE(2, "调拨订单", true),
    /** 3 - 采购订单 */
    PURCHASE_ORDER_TYPE(3, "采购订单", true),
    /** 4 - 扫码订单 */
    SCAN_ORDER_TYPE(4, "扫码订单", false),
    /** 5 - 渠道订单 */
    CHANNEL_ORDER_TYPE(5, "渠道订单", false),
    /** 6 - 合作订单 */
    PARTNER_ORDER_TYPE(6, "合作订单", true),
    ;

    // 订单类型索引值
    private int key;
    // 订单类型
    private String value;
    // 拒绝重复提交 false时可重复提交
    private boolean rejectDuplicate;

    OrderTypeEnum(int key, String value, boolean rejectDuplicate) {
        this.key = key;
        this.value = value;
        this.rejectDuplicate = rejectDuplicate;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isRejectDuplicate() {
        return rejectDuplicate;
    }

    private static final Map<Integer, OrderTypeEnum> orderTypes = new HashMap<>();

    static {
        OrderTypeEnum[] orderTypeArray = OrderTypeEnum.values();

        for (OrderTypeEnum orderTypeEnum : orderTypeArray) {
            orderTypes.put(orderTypeEnum.getKey(), orderTypeEnum);
        }
    }

    public static OrderTypeEnum getOrderTypeEnum(int key) {
        return orderTypes.get(key);
    }
}