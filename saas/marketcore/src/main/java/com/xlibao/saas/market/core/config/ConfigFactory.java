package com.xlibao.saas.market.core.config;

import com.xlibao.io.service.netty.NettyConfig;
import com.xlibao.saas.market.core.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * @author chinahuangxc on 2017/8/19.
 */
@Configuration
@Lazy(false)
public class ConfigFactory {

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private DomainNameConfig domainNameConfig;

    @Autowired
    private MessageService messageService;

    private static ServerConfig server;
    private static DomainNameConfig domainName;

    @PostConstruct
    public void initialization() {
        server = serverConfig;
        domainName = domainNameConfig;

        messageService.startListener();
    }

    public static NettyConfig newNettyConfig() {
        NettyConfig config = new NettyConfig();
        config.setReadTimeout(server.getReadTimeout());
        config.setWriteTimeout(server.getWriteTimeout());
        config.setBothTimeout(server.getBothTimeout());
        return config;
    }

    public static ServerConfig getServer() {
        return server;
    }

    public static DomainNameConfig getDomainName() {
        return domainName;
    }
}