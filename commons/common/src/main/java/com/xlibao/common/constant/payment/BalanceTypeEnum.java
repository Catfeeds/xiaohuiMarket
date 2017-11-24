package com.xlibao.common.constant.payment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/5/9.
 */
public enum BalanceTypeEnum {

    /** 1 -- 全部 */
    ALL(1, "全部"),
    /** 2 -- 收入 */
    INCOME(2, "收入"),
    /** 3 -- 支出 */
    PAYMENT(3, "支出")
    ;

    private int key;
    private String value;

    BalanceTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<Integer, BalanceTypeEnum> BALANCE_TYPE_ENUM_MAP = new HashMap<>();
    static {
        BalanceTypeEnum[] balanceTypeEnums = BalanceTypeEnum.values();
        for (BalanceTypeEnum balanceTypeEnum : balanceTypeEnums) {
            BALANCE_TYPE_ENUM_MAP.put(balanceTypeEnum.getKey(), balanceTypeEnum);
        }
    }

    public static BalanceTypeEnum getBalanceTypeEnum(int key) {
        return BALANCE_TYPE_ENUM_MAP.get(key);
    }
}
