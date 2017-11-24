package com.xlibao.saas.market.adver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * Created by admin on 2017/8/24.
 */
@Configuration
@Lazy(false)
public class ConfigFactory {

    @Autowired
    private QiNiuConfig qiNiuConfig;

    private static QiNiuConfig qiNiu;

    @PostConstruct
    public void initialization() {
        qiNiu = qiNiuConfig;
    }

    public static QiNiuConfig getQiNiuConfig(){
        return qiNiu;
    }
}
