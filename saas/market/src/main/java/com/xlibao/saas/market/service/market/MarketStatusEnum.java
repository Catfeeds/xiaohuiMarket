package com.xlibao.saas.market.service.market;

/**
 * @author chinahuangxc on 2017/7/19.
 */
public enum MarketStatusEnum {

    INVALID(0, "无效"),
    NORMAL(1, "正常"),
    CLOSE(2, "关闭"),
    MAINTAIN(4, "作业中"),
    INITIALIZATION(8, "初始化"),
    ;

    private int key;
    private String value;

    MarketStatusEnum(int key, String value) {
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
