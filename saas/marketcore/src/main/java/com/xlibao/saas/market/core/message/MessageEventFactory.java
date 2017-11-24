package com.xlibao.saas.market.core.message;

import com.xlibao.io.service.netty.MessageEventListener;
import com.xlibao.saas.market.core.message.client.MarketMessageEventListenerImpl;
import com.xlibao.saas.market.core.message.hardware.HexMessageEventListenerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/12.
 */
@Component
public class MessageEventFactory {

    @Autowired
    private MarketMessageEventListenerImpl marketMessageEventListener;
    @Autowired
    private HexMessageEventListenerImpl hexMessageEventListener;

    private static volatile boolean hexOffline = true;

    public MessageEventListener getMarketMessageEventListener() {
        return marketMessageEventListener;
    }

    public MessageEventListener getHexMessageEventListener() {
        return hexMessageEventListener;
    }

    public static void setHexOffline(boolean hexOffline) {
        MessageEventFactory.hexOffline = hexOffline;
    }

    public static boolean isHexOffline() {
        return hexOffline;
    }
}