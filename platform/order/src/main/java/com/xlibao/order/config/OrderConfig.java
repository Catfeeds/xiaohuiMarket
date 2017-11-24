package com.xlibao.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/7/31.
 */
@Configuration
public class OrderConfig {

    @Value("${partnerId}")
    private String partnerId;

    @Value("${paymentAppId}")
    private String paymentAppId;
    @Value("${paymentAppkey}")
    private String paymentAppkey;

    public String getPartnerId() {
        return partnerId;
    }

    public String getPaymentAppId() {
        return paymentAppId;
    }

    public String getPaymentAppkey() {
        return paymentAppkey;
    }
}