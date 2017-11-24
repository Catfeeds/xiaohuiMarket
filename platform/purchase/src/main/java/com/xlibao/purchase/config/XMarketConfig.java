package com.xlibao.purchase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/3/6.
 */
@Configuration
public class XMarketConfig {

    @Value("${ownercode}")
    private String ownercode;
    @Value("${whcode}")
    private String whcode;

    public String getWhcode() {
        return whcode;
    }

    public String getOwnercode() {

        return ownercode;
    }
}
