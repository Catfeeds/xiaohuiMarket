package com.xlibao.order.config;

import com.xlibao.common.support.PassportRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.net.URLEncoder;

/**
 * @author chinahuangxc on 2017/1/24.
 */
@Configuration
@Lazy(false)
public class ConfigFactory {

    @Autowired
    private DomainNameConfig domainNameConfig;
    @Autowired
    private OrderConfig orderConfig;

    private static DomainNameConfig domainName;
    private static OrderConfig order;

    @PostConstruct
    public void initialization() {
        domainName = domainNameConfig;

        PassportRemoteService.setPassportRemoteServiceURL(domainName.passportRemoteURL);

        order = orderConfig;
    }

    public static DomainNameConfig getDomainNameConfig() {
        return domainName;
    }

    public static OrderConfig getOrderConfig() {
        return order;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(URLEncoder.encode("{\"timeStamp\":1502708373959,\"appId\":\"908100000\",\"sign\":\"901BD0506F4A7D5FB0D316617E15DD75\",\"randomParameter\":\"XPEvYblYq8hqvBE8U5cjRhmeZ93aS8TJ\",\"partnerId\":\"xlb908100000\",\"prePaymentId\":\"40611930100019\"}", "utf-8"));
    }
}