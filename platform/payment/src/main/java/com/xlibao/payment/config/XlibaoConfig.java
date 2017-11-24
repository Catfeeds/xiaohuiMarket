package com.xlibao.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/3/6.
 */
@Configuration
public class XlibaoConfig {

    @Value("${partnerId}")
    private String partnerId;
    @Value("${appId}")
    private String appId;
    @Value("${appKey}")
    private String appKey;

    public String getPartnerId() {
        return partnerId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }
}