package com.xlibao.common.constant.message;

/**
 * @author chinahuangxc on 2017/5/3.
 */
public enum TargetTypeEnum {

    /** 1、目标类型 -- 快递员 */
    TARGET_TYPE_COURIER(1, "快递员"),
    /** 2、目标类型 -- 销售者 */
    TARGET_TYPE_MERCHANT(2, "销售者"),
    /** 3、目标类型 -- 消费者 */
    TARGET_TYPE_CONSUMER(3, "消费者"),
    ;

    private int key;
    private String value;

    TargetTypeEnum(int key, String value) {
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