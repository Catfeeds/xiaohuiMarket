package com.xlibao.passport.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;

/**
 * @author chinahuangxc on 2017/5/31.
 */
@Configuration
public class PartnerParameters {

    @Value("${aliyun_access_key_id}")
    private String aliyunAccessKeyId;
    @Value("${aliyun_access_key_secret}")
    private String aliyunAccessKeySecret;
    @Value("${aliyun_account_endpoint}")
    private String aliyunAccountEndpoint;
    @Value("${aliyun_sms_sign_name}")
    private String aliyunSmsSignName;
    @Value("${aliyun_sms_topic}")
    private String aliyunSmsTopic;

    @Value("${sms_open}")
    private String smsOpen;

    @Value("${rbp_account}")
    private String rbpAccount;
    @Value("${rbp_password}")
    private String rbpPassword;
    @Value("${rbp_sign_name}")
    private String rbpSignName;
    @Value("${rbp_report}")
    private String rbpReport;

    @Value("${weixin_mp_app_id}")
    private String weixinMpAppId;
    @Value("${weixin_mp_app_secret}")
    private String weixinMpAppSecret;

    @Value("${weixin_applet_app_id}")
    private String weixinAppletAppId;
    @Value("${weixin_applet_app_secret}")
    private String weixinAppletAppSecret;

    public String getAliyunAccessKeyId() {
        return aliyunAccessKeyId;
    }

    public String getAliyunAccessKeySecret() {
        return aliyunAccessKeySecret;
    }

    public String getAliyunAccountEndpoint() {
        return aliyunAccountEndpoint;
    }

    public String getAliyunSmsSignName() {
        return aliyunSmsSignName;
    }

    public String getAliyunSmsTopic() {
        return aliyunSmsTopic;
    }

    public boolean isSmsOpen() {
        return "true".equals(smsOpen);
    }

    public String getRbpAccount() {
        return rbpAccount;
    }

    public String getRbpPassword() {
        return rbpPassword;
    }

    public String getRbpSignName() {
        try {
            return new String(rbpSignName.getBytes("ISO-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isRbpReport() {
        return "true".equals(rbpReport);
    }

    public String getWeixinMpAppId() {
        return weixinMpAppId;
    }

    public String getWeixinMpAppSecret() {
        return weixinMpAppSecret.trim();
    }

    public String getWeixinAppletAppId() {
        return weixinAppletAppId;
    }

    public String getWeixinAppletAppSecret() {
        return weixinAppletAppSecret.trim();
    }
}