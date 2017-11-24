package com.xlibao.common.constant.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/8.
 */
public enum SmsCodeTypeEnum {

    /** 1 -- 登录验证码 */
    LOGIN(1, "(登录验证码，5分钟内有效)", ""),
    /** 2 -- 注册验证码 */
    REGISTER(2, "(注册验证码，5分钟内有效)", "SMS_67176279"),
    /** 3 -- 重置密码验证码 */
    MODIFY_PASSWORD(3, "(修改密码验证码，5分钟内有效)", "SMS_67141076"),
    /** 4 -- 支付验证码 */
    PAYMENT_WORKS(4, "(支付验证码，5分钟内有效)", "SMS_67216227"),
    /** 5 -- 提现验证码 */
    DRAW_CASH(5, "(提现验证码，5分钟内有效)", "SMS_67151232"),
    /** 6 -- 重置手机号 */
    RESET_MOBILE_NUMBER(6, "(重置手机验证码，5分钟内有效)", "SMS_67286220"),
    /** 7 -- 绑定手机号 */
    BINDING_MOBILE_NUMBER(7, "(绑定手机号，验证码5分钟内有效)", ""),
    ;

    private int key;
    private String value;
    // 接入阿里云专用
    private String templateCode;

    SmsCodeTypeEnum(int key, String value, String templateCode) {
        this.key = key;
        this.value = value;
        this.templateCode = templateCode;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    private static final Map<Integer, SmsCodeTypeEnum> CODE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        SmsCodeTypeEnum[] codeTypeEnums = SmsCodeTypeEnum.values();
        for (SmsCodeTypeEnum codeTypeEnum : codeTypeEnums) {
            CODE_TYPE_ENUM_MAP.put(codeTypeEnum.getKey(), codeTypeEnum);
        }
    }

    public static SmsCodeTypeEnum getSmsCodeTypeEnum(int key) {
        return CODE_TYPE_ENUM_MAP.get(key);
    }

    @Override
    public String toString() {
        return value;
    }
}