package com.xlibao.purchase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Configuration
public class DomainNameConfig {

    @Value("${passportRemoteURL}")
    public String passportRemoteURL;

    @Value("${wmsRemoteURL}")
    public String wmsRemoteURL;

}
