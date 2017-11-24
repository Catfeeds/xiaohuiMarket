package com.market.courier.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhumg on 9/5.
 */
public enum  ClientTypeEnum {

    /**
     * 1、快递员
     */
    COURIER(1, "courier", "快递员", true),
    /**
     * 5、仓管
     */
    WAREHOUSE(5, "warehouse", "仓管", true),

;

    private int key;
    private String code;
    private String name;
    private boolean check;

    ClientTypeEnum(int key, String code, String name, boolean check) {
        this.key = key;
        this.code = code;
        this.name = name;
        this.check = check;
    }

    public int getKey() {
        return key;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return check;
    }

    private static final Map<Integer, ClientTypeEnum> CLIENT_TYPE_ENUM_MAP = new HashMap<>();

    static {
        ClientTypeEnum[] clientTypeEnums = ClientTypeEnum.values();
        for (ClientTypeEnum clientTypeEnum : clientTypeEnums) {
            CLIENT_TYPE_ENUM_MAP.put(clientTypeEnum.getKey(), clientTypeEnum);
        }
    }

    public static ClientTypeEnum getClientTypeEnum(int key) {
        return CLIENT_TYPE_ENUM_MAP.get(key);
    }
}
