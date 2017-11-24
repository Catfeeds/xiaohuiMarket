package com.xlibao.saas.market;

import com.xlibao.saas.market.service.ApplicationContextLoaderNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * @author chinahuangxc on 2017/2/16.
 */
@Configuration
@Lazy(false)
public class XMarketApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(XMarketApplicationContext.class);

    @Autowired
    private ApplicationContextLoaderNotify applicationContextLoaderNotify;

    @PostConstruct
    public void applicationContextInit() {
        logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 智能超市(xmarket)系统开始初始化状态 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        applicationContextLoaderNotify.applicationContextLoaderNotify();
        logger.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 智能超市(xmarket)系统初始化状态完成 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
    }
}
