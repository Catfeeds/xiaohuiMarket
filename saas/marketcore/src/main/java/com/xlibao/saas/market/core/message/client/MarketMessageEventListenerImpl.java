package com.xlibao.saas.market.core.message.client;

import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.io.entry.MessageFactory;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.io.service.netty.MessageEventListener;
import com.xlibao.io.service.netty.NettyConfig;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.io.service.netty.filter.DefaultMessageDecoder;
import com.xlibao.io.service.netty.filter.DefaultMessageEncoder;
import com.xlibao.market.protocol.ShopProtocol;
import com.xlibao.saas.market.core.config.ConfigFactory;
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
public class MarketMessageEventListenerImpl implements MessageEventListener {

    private static final Logger logger = LoggerFactory.getLogger(MarketMessageEventListenerImpl.class);

    @Autowired
    private MessageHandlerAdapter messageHandlerAdapter;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SessionManager sessionManager;

    @Override
    public ByteToMessageDecoder newDecoder() {
        return new DefaultMessageDecoder();
    }

    @Override
    public MessageToMessageEncoder newEncoder() {
        return new DefaultMessageEncoder();
    }

    @Override
    public void notifyChannelActive(NettySession session) throws Exception {
        logger.info("【商店业务】作为客户端(与业务服务交互) 建立一个Socket通道 " + session.netTrack());
        // 自动登录
        login(session);
    }

    @Override
    public void notifyMessageReceived(NettySession session, MessageInputStream message) throws Exception {
        // 由消息处理适配器处理消息
        messageHandlerAdapter.marketMessageExecute(session, message);
    }

    @Override
    public void notifySessionClosed(NettySession session) {
        sessionManager.setMarketSession(null);
        // 执行重连功能
        messageService.reconnector(null);
    }

    @Override
    public void notifyExceptionCaught(Throwable cause) {
    }

    @Override
    public void notifySessionIdle(NettySession session, int idleType, int idleTimes) {
        if (idleType == NettyConfig.TIME_OUT_BOTH && idleTimes >= 20) {
            logger.error("【商店业务】心跳空闲次数已达20次，暂时关闭业务通讯通道，将于1分钟后发起重连");
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void login(NettySession session) {
        MessageOutputStream message = MessageFactory.createLogicMessage(ShopProtocol.CS_SECURITY_VERIFICATION);
        message.writeUTF(ConfigFactory.getServer().getUsername());
        message.writeUTF(ConfigFactory.getServer().getPassword());
        message.writeInt(DeviceTypeEnum.DEVICE_TYPE_HARD_WARE.getKey());
        message.writeInt(1);
        session.send(message);
    }
}