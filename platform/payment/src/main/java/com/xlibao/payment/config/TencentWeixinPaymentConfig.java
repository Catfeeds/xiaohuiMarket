package com.xlibao.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Configuration
public class TencentWeixinPaymentConfig {

    /** 公众账号ID */
    @Value("${wx_app_id}")
    public String WX_APP_ID = "wx38553758f24a3baa";
    /** 公众账号下的密钥 */
    @Value("${wx_app_secret}")
    public String WX_APP_SECRET = "1e599e69f6a76e399bb80108d9c29217";
    /** 商户号 */
    @Value("${wx_partner_id}")
    public String WX_PARTNER_ID = "1487203162";
    /** 签名KEY */
    @Value("${wx_app_key}")
    public String WX_APP_KEY = "QufeJLjas8UWHAjkibvQ2V60shTGwY6T";
    /** 微信证书地址 */
    @Value("${wx_certificate_path}")
    public String  WX_CERTIFICATE_PATH = "";
}
