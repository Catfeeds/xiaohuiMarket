package com.xlibao.common.constant.version;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/4/17.
 */
public enum VersionTypeEnum {

    /** 1 - 已是最新版本（无需更新）*/
    UN_UPGRADE(1, "已是最新版本"),
    /** 2 - 您有一个新的版本可更新，是否更新？（提示更新）*/
    UN_FORCE_UPGRADE(2, "更新提示"),
    /** 3 - 请更新至最新版本（强制更新）*/
    FORCE_UPGRADE(3, "请更新至最新版本"),;

    private int key;
    private String title;

    VersionTypeEnum(int key, String title) {
        this.key = key;
        this.title = title;
    }

    public int getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    private static final Map<Integer, VersionTypeEnum> VERSION_TYPE_ENUM_MAP = new HashMap<>();

    static {
        VersionTypeEnum[] versionTypes = VersionTypeEnum.values();
        for (VersionTypeEnum versionTypeEnum : versionTypes) {
            VERSION_TYPE_ENUM_MAP.put(versionTypeEnum.getKey(), versionTypeEnum);
        }
    }

    public static VersionTypeEnum getVersionTypeEnum(int key) {
        return VERSION_TYPE_ENUM_MAP.get(key);
    }
}