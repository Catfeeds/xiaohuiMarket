package com.xlibao.payment.service.currency;

/**
 * @author chinahuangxc on 2017/2/3.
 */
public enum CurrencyPropertiesTypeEnum {

    SECURITY(1, "安全参数"),
    ;

    private int key;
    private String value;

    CurrencyPropertiesTypeEnum(int key, String value) {
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
