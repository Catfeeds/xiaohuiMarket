package com.xlibao.saas.market.shop.service;

import com.xlibao.common.CommonUtils;
import com.xlibao.io.entry.MessageFactory;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.market.protocol.ShopProtocol;
import com.xlibao.saas.market.shop.service.shop.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/9.
 */
@Component
public class MessageHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerAdapter.class);

    @Autowired
    private ShopService shopService;

    public void platformMessageExecute(NettySession session, MessageInputStream message) {
        logger.info("【心跳消息】" + session.getAttribute("passportId") + "发起心跳消息");
        short msgId = message.getMsgId();

        MessageOutputStream response = null;
        switch (msgId) {
            case MessageFactory.MSG_ID_HEARTBEAT: // 心跳 回复当前服务器时间
                response = MessageFactory.createResponseMessage(message);
                response.writeUTF(CommonUtils.nowFormat());
                break;
        }
        session.send(response);
    }

    public void shopMessageExecute(NettySession session, MessageInputStream message) {
        short msgId = message.getMsgId();

        logger.info("来自硬件的消息，消息ID：" + msgId + "；" + session.netTrack());
        switch (msgId) {
            case ShopProtocol.CS_HARDWARE:
                shopService.hardwareMessage(message, session);
                break;
        }
    }

    public void logicMessageExecute(NettySession session, MessageInputStream message) {
        short msgId = message.getMsgId();

        MessageOutputStream response = null;

        switch (msgId) {
            case ShopProtocol.CS_SECURITY_VERIFICATION:
                response = shopService.securityVerification(message, session);
                break;
        }
        session.send(response);
    }
}