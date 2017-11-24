package com.xlibao.common.constant.payment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/3.
 */
public enum CurrencyTypeEnum {

    /** 1 -- 余额 */
    BALANCE(1, "余额"),
    /** 2 -- 会员余额 */
    VIP_BALANCE(2, "会员余额"),;

    private int key;
    private String value;

    CurrencyTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, CurrencyTypeEnum> CURRENCY_TYPE_ENUM_MAP = new HashMap<>();

    static {
        CurrencyTypeEnum[] currencyTypeEnums = CurrencyTypeEnum.values();
        for (CurrencyTypeEnum currencyTypeEnum : currencyTypeEnums) {
            CURRENCY_TYPE_ENUM_MAP.put(currencyTypeEnum.getKey(), currencyTypeEnum);
        }
    }

    public static CurrencyTypeEnum getCurrencyTypeEnum(int key) {
        return CURRENCY_TYPE_ENUM_MAP.get(key);
    }
}