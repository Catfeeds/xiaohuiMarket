package com.xlibao.payment.config;

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
    private TencentWeixinPaymentConfig tencentWeixinPaymentConfig;
    @Autowired
    private AlipayPaymentConfig alipayPaymentConfig;
    @Autowired
    private XlibaoConfig xlibaoConfig;

    private static DomainNameConfig domainName = new DomainNameConfig();
    private static TencentWeixinPaymentConfig tencentWeixinPayment = new TencentWeixinPaymentConfig();
    private static AlipayPaymentConfig alipayPayment = new AlipayPaymentConfig();
    private static XlibaoConfig xConfig = new XlibaoConfig();

    @PostConstruct
    public void initialization() {
        domainName = domainNameConfig;
        PassportRemoteService.setPassportRemoteServiceURL(domainName.passportRemoteURL);

        tencentWeixinPayment = tencentWeixinPaymentConfig;
        alipayPayment = alipayPaymentConfig;
        xConfig = xlibaoConfig;
    }

    public static DomainNameConfig getDomainNameConfig() {
        return domainName;
    }

    public static TencentWeixinPaymentConfig getTencentWeixinPaymentConfig() {
        return tencentWeixinPayment;
    }

    public static AlipayPaymentConfig getAlipayPaymentConfig() {
        return alipayPayment;
    }

    public static XlibaoConfig getXlibaoConfig() {
        return xConfig;
    }
}