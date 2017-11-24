package com.xlibao.saas.advertise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Configuration
public class DomainNameConfig {

    @Value("${advertiseRemoteURL}")
    public String advertiseRemoteURL;

    @Value("${downLoadPATH}")
    public String downLoadPATH;

    /****FTP连接属性***/
    @Value("${ftpHost}")
    public String ftpHost;
    @Value("${ftpPort}")
    public int ftpPort;
    @Value("${ftpUsername}")
    public String ftpUsername;
    @Value("${ftpPassword}")
    public String ftpPassword;

}
