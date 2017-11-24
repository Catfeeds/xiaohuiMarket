package com.xlibao.saas.market.service.item;

/**
 * @author chinahuangxc on 2017/7/27.
 */
public enum ItemLockTypeEnum {

    CREATE_ORDER(1, "创建订单"),
    ;

    private int key;
    private String value;

    ItemLockTypeEnum(int key, String value) {
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
