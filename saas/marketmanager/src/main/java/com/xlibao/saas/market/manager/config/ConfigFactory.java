package com.xlibao.saas.market.manager.config;

import com.xlibao.common.support.PassportRemoteService;
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

        //DataCacheApplicationContextLoaderNotify.setLocationRemoteServiceURL(domainName.passportRemoteURL);
        //LocationDataCacheService.initLocationCache();

        //DataCacheApplicationContextLoaderNotify.setItemRemoteServiceURL(domainName.itemRemoteURL);
        //ItemDataCacheService.initItemCache();


        PassportRemoteService.setPassportRemoteServiceURL(domainName.passportRemoteURL);

        xmarket = xmarketConfig;
    }

    public static DomainNameConfig getDomainNameConfig() {
        return domainName;
    }

    public static XMarketConfig getXMarketConfig() {
        return xmarket;
    }
}