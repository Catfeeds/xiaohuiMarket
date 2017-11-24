package com.xlibao.saas.market.service.item;

/**
 * @author chinahuangxc on 2017/7/25.
 */
public enum BeyondControllTypeEnum {

    /** 0 - 不可超出限制 */
    CAN_NOT_BEYOND(0, "不可超出限制"),
    /** 1 - 超出时以最高价购买 */
    MAX_PRICE_BUY(1, "超出时以最高价购买"),;

    private int key;
    private String value;

    BeyondControllTypeEnum(int key, String value) {
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