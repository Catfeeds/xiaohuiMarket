package com.xlibao.saas.market.core.message.impl;

import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.io.service.netty.MessageEventListener;
import com.xlibao.io.service.netty.NettyClient;
import com.xlibao.io.service.netty.NettyConfig;
import com.xlibao.io.service.netty.NettyNetServer;
import com.xlibao.saas.market.core.config.ConfigFactory;
import com.xlibao.saas.market.core.message.MarketConnectorListener;
import com.xlibao.saas.market.core.message.MessageEventFactory;
import com.xlibao.saas.market.core.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/8/19.
 */
@Transactional
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageEventFactory messageEventFactory;

    public void reconnector(MarketConnectorListener connectorListener) {
        if (MessageEventFactory.isHexOffline()) {
            logger.error("======= 由于硬件连接已断开，本次重连请求已被拒绝 =======");
            return;
        }
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (!connectorMarketServer(connectorListener)) {
                    AsyncScheduledService.submitCommonTask(this, ConfigFactory.getServer().getReconnectorPeriod(), TimeUnit.MINUTES);
                }
            }
        };
        AsyncScheduledService.submitImmediateCommonTask(runnable);
    }

    private boolean connectorMarketServer(MarketConnectorListener connectorListener) {
        NettyConfig config = ConfigFactory.newNettyConfig();
        // 商店业务消息监听器
        MessageEventListener marketMessageEventListener = messageEventFactory.getMarketMessageEventListener();
        // 启动客户端
        NettyClient.getInstance().init(config);
        try {
            NettyClient.getInstance().start(ConfigFactory.getServer().getServerIP(), ConfigFactory.getServer().getServerPort(), marketMessageEventListener);
            if (connectorListener != null) {
                connectorListener.onSuccess();
            }
            return true;
        } catch (Exception ex) {
            ex.getMessage();
        }
        return false;
    }

    @Override
    public void startListener() {
        // 硬件业务消息监听器
        MessageEventListener hexMessageEventListener = messageEventFactory.getHexMessageEventListener();

        NettyConfig config = ConfigFactory.newNettyConfig();
        // 启动服务器
        NettyNetServer.getInstance().init(config);
        try {
            NettyNetServer.getInstance().start(ConfigFactory.getServer().getListenerPort(), hexMessageEventListener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
