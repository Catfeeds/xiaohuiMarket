package com.xlibao.saas.advertise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * @author chinahuangxc on 2017/1/24.
 */
@Configuration
@Lazy(false)
public class ConfigFactory {

    @Autowired
    private DomainNameConfig domainNameConfig;
    @Autowired
    private XMarketConfig xmarketConfig;

    private static DomainNameConfig domainName;
    private static XMarketConfig xmarket;

    @PostConstruct
    public void initialization() {
        domainName = domainNameConfig;
        xmarket = xmarketConfig;
    }

    public static DomainNameConfig getDomainNameConfig() {
        return domainName;
    }

    public static XMarketConfig getXMarketConfig() {
        return xmarket;
    }
}