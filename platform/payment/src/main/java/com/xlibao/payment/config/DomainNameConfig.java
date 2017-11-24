package com.xlibao.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Configuration
public class DomainNameConfig {

    @Value("${passportRemoteURL}")
    public String passportRemoteURL;

    @Value("${paymentRemoteURL}")
    public String paymentRemoteURL;

    @Value("${orderRemoteURL}")
    public String orderRemoteURL;

    @Value("${itemRemoteURL}")
    public String itemRemoteURL;
}
