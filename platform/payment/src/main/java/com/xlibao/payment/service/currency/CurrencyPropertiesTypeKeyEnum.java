package com.xlibao.payment.service.currency;

/**
 * @author chinahuangxc on 2017/2/3.
 */
public enum CurrencyPropertiesTypeKeyEnum {

    PAYMENT_PASSWORD(CurrencyPropertiesTypeEnum.SECURITY.getKey(), "paymentPassword", "支付密码"),
    ;

    private int type;
    private String key;
    private String value;

    CurrencyPropertiesTypeKeyEnum(int type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}