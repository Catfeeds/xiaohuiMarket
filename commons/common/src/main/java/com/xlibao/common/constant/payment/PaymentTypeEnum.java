package com.xlibao.common.constant.payment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/2.
 */
public enum PaymentTypeEnum {

    /** UNKNOWN - 未知 */
    UNKNOWN(9999, "UNKNOWN", "未知"),
    /** BALANCE - 余额 */
    BALANCE(10000, "BALANCE", "余额"),
    /** VIP_BALANCE - 会员余额 */
    VIP_BALANCE(11000, "VIP_BALANCE", "会员余额"),
    /** ALIPAY - 支付宝 */
    ALIPAY(20000, "ALIPAY", "支付宝"),
    /** WEIXIN_APP- 微信 */
    WEIXIN_APP(30000, "WEIXIN_APP", "微信"),
    /** WEIXIN_APPLET - 微信小程序 */
    WEIXIN_APPLET(32000, "WEIXIN_APPLET", "微信小程序"),
    /** WEIXIN_JS - 微信公众号 */
    WEIXIN_JS(31000, "WEIXIN_JS", "微信公众号"),
    /** WEIXIN_NATIVE - 微信原生扫码 */
    WEIXIN_NATIVE(33000, "WEIXIN_NATIVE", "微信原生扫码"),
    /** DRAW_CASH - 提现 */
    DRAW_CASH(40000, "DRAW_CASH", "提现"),
    ;

    private int channelId;
    private String key;
    private String value;

    PaymentTypeEnum(int channelId, String key, String value) {
        this.channelId = channelId;
        this.key = key;
        this.value = value;
    }

    public int getChannelId() {
        return channelId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, PaymentTypeEnum> PAYMENT_TYPE_ENUM_MAP = new HashMap<>();

    static {
        PaymentTypeEnum[] paymentTypeEnums = PaymentTypeEnum.values();
        for (PaymentTypeEnum paymentTypeEnum : paymentTypeEnums) {
            PAYMENT_TYPE_ENUM_MAP.put(paymentTypeEnum.getKey(), paymentTypeEnum);
        }
    }

    public static PaymentTypeEnum getPaymentTypeEnum(String key) {
        PaymentTypeEnum paymentTypeEnum = PAYMENT_TYPE_ENUM_MAP.get(key);
        if (paymentTypeEnum == null) {
            throw new IllegalArgumentException("支付类型异常，code : " + key);
        }
        return paymentTypeEnum;
    }

    @Override
    public String toString() {
        return key + " - " + value;
    }
}