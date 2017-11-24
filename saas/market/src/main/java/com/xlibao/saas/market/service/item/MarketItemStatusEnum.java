package com.xlibao.saas.market.service.item;

/**
 * @author chinahuangxc on 2016/10/27.
 */
public enum MarketItemStatusEnum {

    /** -1 -- 隐藏(逻辑删除) */
    HIDE(-1, "删除"),
    /** 0 -- 下架 */
    OFF_SALE(0, "下架"),
    /** 1 -- 正常 */
    NORMAL(1, "正常"),
    /** 2 -- 无效 */
    INVALID(2, "无效"),;

    private int key;
    private String value;

    MarketItemStatusEnum(int key, String value) {
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
