package com.xlibao.saas.market.manager.config;

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

    @Value("${paymentAppId}")
    private String paymentAppId;
    @Value("${paymentAppkey}")
    private String paymentAppkey;

    @Value("${itemAppId}")
    private String itemAppId;
    @Value("${itemAppkey}")
    private String itemAppkey;

    public String getPartnerId() {
        return partnerId;
    }

    public String getOrderAppId() {
        return orderAppId;
    }

    public String getOrderAppkey() {
        return orderAppkey;
    }

    public String getPaymentAppId() {
        return paymentAppId;
    }

    public String getPaymentAppkey() {
        return paymentAppkey;
    }

    public String getItemAppId() {
        return itemAppId;
    }

    public String getItemAppkey() {
        return itemAppkey;
    }
}