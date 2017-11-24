package com.xlibao.saas.market.core.message;

import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.io.service.netty.NettySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/12.
 */
@Component
public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    // 硬件的消息会话，建立通道后可直接使用；由硬件的心跳机制来保持链接的有效性
    private NettySession hardwareSession;
    // 商店业务消息会话，必须是通过验证后才能使用；由本进程的心跳机制来保持链接的有效性
    private NettySession marketSession;

    @Autowired
    private MessageService messageService;

    public void setHardwareSession(NettySession session) {
        hardwareSession = session;
    }

    public void setMarketSession(NettySession session) {
        marketSession = session;
    }

    public NettySession getMarketSession() {
        return marketSession;
    }

    public NettySession getHardwareSession() {
        return hardwareSession;
    }

    public void closeMarketSession() {
        if (marketSession == null) {
            return;
        }
        try {
            marketSession.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendHardwareMessage(String content) {
        if (hardwareSession == null) {
            // 无法发送，需记录内容后在硬件连接成功时补发
            logger.error("发送消息到硬件失败，消息内容：" + content + "；失败原因：硬件未与主机连接");
            return;
        }
        hardwareSession.send(content);
    }

    public boolean sendMarketMessage(MessageOutputStream message) {
        if (marketSession == null) {
            if (MessageEventFactory.isHexOffline()) {
                logger.error("----------- 由于硬件连接已断开，本次发送请求已被拒绝 -----------");
                return false;
            }
            // 发起建立链接的请求
            messageService.reconnector(null);
            return false;
        }
        return marketSession.send(message);
    }
}