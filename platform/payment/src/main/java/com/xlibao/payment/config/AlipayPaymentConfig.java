package com.xlibao.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/2/3.
 */
@Configuration
public class AlipayPaymentConfig {

    @Value("${ali_access_key_id}")
    public String ACCESS_KEY_ID;
    @Value("${ali_access_key_secret}")
    public String ACCESS_KEY_SECRET;
    // ↓↓↓↓↓↓↓↓↓↓ 请在这里读取您的基本信息 ↓↓↓↓↓↓↓↓↓↓
    // 支付宝的公钥，无需修改该值
    @Value("${ali_public_key}")
    public String aliPublicKey;
    // 商户的私钥
    @Value("${ali_private_key}")
    public String privateKey;
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 请在这里读取您的基本信息 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    @Value("${ali_partner_id}")
    public String partnerId;
    // 用户
    @Value("${ali_seller}")
    public String seller;

    /** 字符编码格式 目前支持 gbk 或 utf-8 */
    public static final String INPUT_CHARSET = "utf-8";
    /** 签名方式 不需修改 */
    public static String sign_type = "RSA";
}