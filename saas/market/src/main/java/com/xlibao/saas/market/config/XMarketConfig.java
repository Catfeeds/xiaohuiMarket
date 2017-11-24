package com.xlibao.saas.market.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/3/6.
 */
@Configuration
public class XMarketConfig {

    @Value("${partnerId}")
    private String partnerId;

    @Value("${orderAppId}")
    private String orderAppId;
    @Value("${orderAppkey}")
    private String orderAppkey;

    @Value("${marketShopAppId}")
    private String marketShopAppId;
    @Value("${marketShopAppkey}")
    private String marketShopAppkey;

    @Value("${jpush_app_secret}")
    private String jpushAppSecret;
    @Value("${jpush_app_key}")
    private String jpushAppKey;

    public String getPartnerId() {
        return partnerId;
    }

    public String getOrderAppId() {
        return orderAppId;
    }

    public String getOrderAppkey() {
        return orderAppkey;
    }

    public String getMarketShopAppId() {
        return marketShopAppId;
    }

    public String getMarketShopAppkey() {
        return marketShopAppkey;
    }

    public String getJPushAppSecret() {
        return jpushAppSecret;
    }

    public String getJPushAppKey() {
        return jpushAppKey;
    }
}
