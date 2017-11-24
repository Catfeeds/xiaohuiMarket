package com.xlibao.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * <pre>
 *     <b>Spring容器加载后的通知入口</b>
 * </pre>
 *
 * @author chinahuangxc on 2017/2/4.
 */
@Configuration
@Lazy(false)
public class XlibaoPaymentApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(XlibaoPaymentApplicationContext.class);

    @Autowired
    private ApplicationContextLoaderNotify applicationContextLoaderNotify;

    @PostConstruct
    public void applicationContextInit() {
        logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 支付(Payment)系统开始初始化状态 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        applicationContextLoaderNotify.applicationContextLoaderNotify();
        logger.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 支付(Payment)系统初始化状态完成 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
    }
}