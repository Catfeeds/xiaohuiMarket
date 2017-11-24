package com.xlibao.saas.market.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinahuangxc on 2017/8/12.
 */
@Configuration
public class ServerConfig {

    @Value("${serverIP}")
    private String serverIP = "";
    @Value("${serverPort}")
    private int serverPort = 0;
    @Value("${listenerPort}")
    private int listenerPort = 0;

    @Value("${readTimeout}")
    private int readTimeout = 10;
    @Value("${writeTimeout}")
    private int writeTimeout = 10;
    @Value("${bothTimeout}")
    private int bothTimeout = 15;
    @Value("${login_name}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${reconnector_period}")
    private int reconnectorPeriod = 1;

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getListenerPort() {
        return listenerPort;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public int getBothTimeout() {
        return bothTimeout;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getReconnectorPeriod() {
        return reconnectorPeriod;
    }
}