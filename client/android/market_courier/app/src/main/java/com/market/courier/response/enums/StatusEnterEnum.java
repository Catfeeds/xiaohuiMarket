package com.market.courier.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhumg on 9/26.
 */
public enum StatusEnterEnum {

    /** 4 -- 统一状态(完成) */
    COMPLETE(4, "完成"),

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 用户 使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 11 -- 全部 */
    ALL(11, "全部"),
    /** 12 -- 待处理 */
    WAIT_PAYMENT(12, "待付款"),
    /** 13 -- 待收货 */
    WAIT_RECEIPT(13, "待收货"),
    /** 14 -- 配送中 */
    REFUND(14, "退款"),
    /** 15 -- 首页推荐 */
    RECOMMEND(15, "首页推荐"),
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 用户 使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 快递 使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 21 -- 待抢单 */
    COURIER_WAIT_ACCEPT(21, "待抢单"),
    /** 22 -- 待取货 */
    COURIER_WAIT_PICK_UP(22, "待取货"),
    /** 23 -- 配送中 */
    COURIER_DELIVER(23, "配送中"),
    /** 24 -- 取消 */
    COURIER_CANCEL(24, "取消"),
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 快递 使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    ;

    private int key;
    private String value;

    StatusEnterEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, StatusEnterEnum> statusEnters = new HashMap<>();

    static {
        StatusEnterEnum[] statusEnterEnumses = StatusEnterEnum.values();
        for (StatusEnterEnum statusEnterEnum : statusEnterEnumses) {
            statusEnters.put(statusEnterEnum.getKey(), statusEnterEnum);
        }
    }

    public static StatusEnterEnum getStatusEnterEnum(int key) {
        return statusEnters.get(key);
    }
}
