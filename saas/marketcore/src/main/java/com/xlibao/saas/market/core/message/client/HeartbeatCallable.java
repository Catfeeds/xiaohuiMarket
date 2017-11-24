package com.xlibao.saas.market.core.message.client;

import com.xlibao.io.entry.MessageFactory;
import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.saas.market.core.config.ConfigFactory;
import com.xlibao.saas.market.core.message.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/12.
 */
@Component
public class HeartbeatCallable extends Thread {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * ConfigFactory.getServer().getBothTimeout());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            MessageOutputStream message = MessageFactory.createPlatformMessage(MessageFactory.MSG_ID_HEARTBEAT);
            message.writeUTF("商店心跳信息");

            sessionManager.sendMarketMessage(message);
        }
    }
}
