package com.xlibao.saas.market.service.market;

/**
 * @author chinahuangxc on 2017/9/14.
 */
public enum ShelvesTaskTypeEnum {

    /** 1 -- 上架任务 */
    ON_SHELVES(1, "上架任务"),
    /** 2 -- 下架任务 */
    OFF_SHELVES(2, "下架任务"),
    ;

    private int key;
    private String value;

    ShelvesTaskTypeEnum(int key, String value) {
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
