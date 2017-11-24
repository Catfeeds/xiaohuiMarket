package com.xlibao.saas.market.shop.config;

import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.saas.market.shop.service.message.MessageApplicationContextNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * @author chinahuangxc on 2017/8/8.
 */
@Configuration
@Lazy(false)
public class ConfigFactory {

    @Autowired
    private SocketServiceConfig socketServiceConfig;
    @Autowired
    private DomainNameConfig domainNameConfig;

    private static SocketServiceConfig socketService;
    private static DomainNameConfig domainName;

    @Autowired
    private MessageApplicationContextNotify messageApplicationContextNotify;

    @PostConstruct
    public void initialization() {
        socketService = socketServiceConfig;
        domainName = domainNameConfig;

        PassportRemoteService.setPassportRemoteServiceURL(domainName.passportRemoteURL);
        messageApplicationContextNotify.init();
    }

    public static SocketServiceConfig getSocketServiceConfig() {
        return socketService;
    }

    public static DomainNameConfig getDomainNameConfig() {
        return domainName;
    }
}