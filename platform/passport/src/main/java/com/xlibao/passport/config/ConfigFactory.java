package com.xlibao.passport.config;

import com.xlibao.common.service.sms.partner.AliyunMessageService;
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
    private PartnerParameters partnerParameters;

    private static PartnerParameters partner;

    @PostConstruct
    public void initialization() {
        partner = partnerParameters;
        //AliyunMessageService.setAliyunAccessParameters(partnerParameters.getAliyunAccessKeyId(), partnerParameters.getAliyunAccessKeySecret(), partnerParameters.getAliyunAccountEndpoint(),
        //        partnerParameters.getAliyunSmsSignName(), partnerParameters.getAliyunSmsTopic());
    }

    public static PartnerParameters getPartner() {
        return partner;
    }
}