package com.xlibao.saas.market.service.item;

/**
 * @author chinahuangxc on 2017/8/10.
 */
public enum ItemStockLockStatusEnum {

    /** 1 -- 锁定中 */
    LOCK(1, "锁定中"),
    /** 2 -- 释放 */
    RELEASE(2, "释放"),
    /** 3 -- 出货 */
    SHIPMENT(3, "出货"),
    ;

    private int key;
    private String value;

    ItemStockLockStatusEnum(int key, String value) {
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
