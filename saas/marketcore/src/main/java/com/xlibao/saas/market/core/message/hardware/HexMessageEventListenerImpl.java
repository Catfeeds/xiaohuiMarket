package com.xlibao.saas.market.core.message.hardware;

import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.service.netty.MessageEventListener;
import com.xlibao.io.service.netty.NettyConfig;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.io.service.netty.filter.HexMessageDecoder;
import com.xlibao.io.service.netty.filter.HexMessageEncoder;
import com.xlibao.market.protocol.HardwareMessageType;
import com.xlibao.saas.market.core.message.MarketConnectorListener;
import com.xlibao.saas.market.core.message.MessageEventFactory;
import com.xlibao.saas.market.core.message.MessageService;
import com.xlibao.saas.market.core.message.SessionManager;
import com.xlibao.saas.market.core.service.MessageHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/12.
 */
@Component
public class HexMessageEventListenerImpl implements MessageEventListener {

    private static final Logger logger = LoggerFactory.getLogger(HexMessageEventListenerImpl.class);

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private MessageHandlerAdapter messageHandlerAdapter;
    @Autowired
    private MessageService messageService;

    private boolean activeClose = false;

    @Override
    public ByteToMessageDecoder newDecoder() {
        return new HexMessageDecoder(HardwareMessageType.HARDWARE_MSG_END);
    }

    @Override
    public MessageToMessageEncoder newEncoder() {
        return new HexMessageEncoder();
    }

    @Override
    public void notifyChannelActive(NettySession session) throws Exception {
        logger.info("作为服务端(与硬件交互) 建立一个Socket监听通道 " + session.netTrack());
        if (sessionManager.getHardwareSession() != null) {
            activeClose = true;
            session.close();
            logger.info("发现存在目标客户端，本次将释放原来持有的连接，重新设置全新的连接会话");
            return;
//            try {
//                activeClose = true;
//                MessageEventFactory.setHexOffline(true);
//
//                sessionManager.setHardwareSession(null);
//
//                nettySession.close();
//                sessionManager.closeMarketSession();
//            } catch (Exception ex) {
//                logger.error("重复发起连接关闭原来的连接发生了异常", ex);
//                ex.printStackTrace();
//            }
        }
        sessionManager.setHardwareSession(session);

        MessageEventFactory.setHexOffline(false);
        // 硬件连接成功时 发起连接业务服务器
        messageService.reconnector(new MarketConnectorListenerImpl());
    }

    @Override
    public void notifyMessageReceived(NettySession session, MessageInputStream message) throws Exception {
        byte[] bytes = new byte[message.getBodyDataSize()];
        // 填充消息
        message.readBytes(bytes);
        // 硬件消息内容
        String parameters = new String(bytes);
        // 通知消息处理适配器
        messageHandlerAdapter.hardwareMessageExecute(session, parameters);
    }

    @Override
    public void notifySessionClosed(NettySession session) {
        // 硬件断连时 将服务连接同时断开
        if (activeClose) {
            logger.error("会话被主动关闭，因为已存在一个会话，本次关闭不对业务进行任何处理");
            activeClose = false;
            return;
        }
        logger.error("会话通道被关闭，本次非主动关闭，将释放业务的连接，并清理当前系统内存");
        try {
            MessageEventFactory.setHexOffline(true);
            sessionManager.setHardwareSession(null);
            sessionManager.closeMarketSession();
        } catch (Exception ex) {
            logger.error("连接关闭发生了异常", ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyExceptionCaught(Throwable cause) {
    }

    @Override
    public void notifySessionIdle(NettySession session, int idleType, int idleTimes) {
        if (idleType == NettyConfig.TIME_OUT_BOTH && idleTimes >= 20) {
            logger.error("【硬件健康】心跳空闲次数已达20次，暂时关闭硬件通讯通道，将于1分钟后发起重连");
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initShelvesDatas() {
        String content = HardwareMessageType.HARDWARE_MSG_START + HardwareMessageType.SHELVES + "CC" + HardwareMessageType.HARDWARE_MSG_END;
        sessionManager.sendHardwareMessage(content);
    }

    private class MarketConnectorListenerImpl implements MarketConnectorListener {

        @Override
        public void onSuccess() {
            initShelvesDatas();
        }
    }
}