package com.xlibao.common.constant.passport;

/**
 * @author chinahuangxc on 2017/8/23.
 */
public enum ChannelTypeEnum {

    /** 100000 渠道 -- 官方 */
    OFFICIAL(100000, "官方"),
    /** 100001 渠道 -- 微信 */
    WEIXIN(100001, "微信"),;

    private int key;
    private String value;

    ChannelTypeEnum(int key, String value) {
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