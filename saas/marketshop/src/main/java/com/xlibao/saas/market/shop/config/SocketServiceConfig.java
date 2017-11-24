package com.xlibao.saas.market.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/8/8.
 */
@Configuration
public class SocketServiceConfig {

    @Value("${readTimeout}")
    private String readTimeout = "10";
    @Value("${writeTimeout}")
    private String writeTimeout = "10";
    @Value("${bothTimeout}")
    private String bothTimeout = "15";

    @Value("${servicePort}")
    private String servicePort;


    public String getReadTimeout() {
        return readTimeout;
    }

    public String getWriteTimeout() {
        return writeTimeout;
    }

    public String getBothTimeout() {
        return bothTimeout;
    }

    public String getServicePort() {
        return servicePort;
    }
}