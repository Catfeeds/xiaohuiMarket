package com.xlibao.common.constant.logistics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/5/8.
 */
public enum ClockTypeEnum {

    /** 1 -- 工作中 */
    WORKING(1, "工作中"),
    /** 2 -- 休息中 */
    SLEEPING(2, "休息中"),
    ;

    private int key;
    private String value;

    ClockTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, ClockTypeEnum> CLOCK_TYPE_ENUM_MAP = new HashMap<>();

    static {
        ClockTypeEnum[] clockTypeEnums = ClockTypeEnum.values();
        for (ClockTypeEnum clockTypeEnum : clockTypeEnums) {
            CLOCK_TYPE_ENUM_MAP.put(clockTypeEnum.getKey(), clockTypeEnum);
        }
    }

    public static ClockTypeEnum getClockTypeEnum(int key) {
        return CLOCK_TYPE_ENUM_MAP.get(key);
    }
}