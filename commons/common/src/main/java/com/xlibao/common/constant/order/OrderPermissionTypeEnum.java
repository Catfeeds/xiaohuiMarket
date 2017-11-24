package com.xlibao.common.constant.order;

/**
 * @author chinahuangxc on 2017/9/4.
 */
public enum OrderPermissionTypeEnum {

    /** 1、下单方 */
    CONSUMER(1, "下单方"),
    /** 2、销售方 */
    SALE(2, "销售方"),
    /** 4、收货方 */
    CONSIGNEE(4, "收货方"),
    /** 8、发货方 */
    SHIPPER(8, "发货方"),
    /** 16、配送方 */
    DELIVERY(16, "配送方"),;

    private int key;
    private String value;

    OrderPermissionTypeEnum(int key, String value) {
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