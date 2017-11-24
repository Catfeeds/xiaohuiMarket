package com.xlibao.common.constant.device;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/1/26.
 */
public enum DeviceTypeEnum {

    /** -1 - 设备类型 -- 未知 */
    DEVICE_TYPE_UN_KNOW(-1, "未知设备"),
    /** 1 - 设备类型 -- Android */
    DEVICE_TYPE_ANDROID(1, "Android"),
    /** 2 - 设备类型 -- IOS */
    DEVICE_TYPE_IOS(2, "IOS"),
    /** 3 - 设备类型 -- H5 */
    DEVICE_TYPE_H5(3, "H5"),
    /** 4 - 设备类型 -- HTML */
    DEVICE_TYPE_HTML(4, "HTML"),
    /** 5 - 设备类型 -- APPLET(小程序) */
    DEVICE_TYPE_APPLET(5, "APPLET"),
    /** 6 - 设备类型 -- AUTO */
    DEVICE_TYPE_AUTO(6, "AUTO"),
    /** 7 - 设备类型 -- HARD_WARE */
    DEVICE_TYPE_HARD_WARE(7, "HARD_WARE"),
    ;

    private int key;
    private String value;

    DeviceTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, DeviceTypeEnum> DEVICE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        DeviceTypeEnum[] deviceTypeEnums = DeviceTypeEnum.values();
        for (DeviceTypeEnum deviceTypeEnum : deviceTypeEnums) {
            DEVICE_TYPE_ENUM_MAP.put(deviceTypeEnum.getKey(), deviceTypeEnum);
        }
    }

    public static DeviceTypeEnum getDeviceTypeEnum(int key) {
        return DEVICE_TYPE_ENUM_MAP.get(key);
    }
}