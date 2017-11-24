package com.xlibao.saas.market.core.service.application.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.io.entry.MessageFactory;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.market.protocol.HardwareMessageType;
import com.xlibao.market.protocol.ShopProtocol;
import com.xlibao.saas.market.core.config.ConfigFactory;
import com.xlibao.saas.market.core.message.SessionManager;
import com.xlibao.saas.market.core.message.client.HeartbeatCallable;
import com.xlibao.saas.market.core.service.application.ApplicationService;
import com.xlibao.saas.market.core.service.support.MarketApplicationRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chinahuangxc on 2017/8/13.
 */
@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private HeartbeatCallable heartbeatCallable;

    @Override
    public void scanPickUp(String orderSequenceNumber) {
        logger.debug("询问是否可进行取货操作，订单序号：" + orderSequenceNumber);
        boolean result = MarketApplicationRemoteService.askOrderPickUp((Long) sessionManager.getMarketSession().getAttribute("passportId"), orderSequenceNumber);
        if (!result) {
            return;
        }
        String content = HardwareMessageType.HARDWARE_MSG_START + HardwareMessageType.PICK_UP + orderSequenceNumber + HardwareMessageType.HARDWARE_MSG_END;
        logger.info("【硬件消息】发送取货消息，消息内容：" + content);

        // 发送消息到硬件处理
        sessionManager.sendHardwareMessage(content);
    }

    @Override
    public void logicMessageExecute(NettySession session, MessageInputStream message) {
        short msgId = message.getMsgId();
        logger.info("【逻辑消息】消息ID：" + msgId);
        if (msgId == ShopProtocol.CS_SECURITY_VERIFICATION) {
            securityVerification(session, message);
        }
    }

    @Override
    public void platformMessageExecute(NettySession session, MessageInputStream message) {
        // logger.info("【平台消息】" + session.getAttribute("passportId") + "，心跳结果：" + message.readUTF() + "；" + session.netTrack());
    }

    @Override
    public void toHardwareMessageExecute(NettySession session, MessageInputStream message) {
        String content = message.readUTF();
        logger.info("【硬件消息】发送给硬件的消息，消息内容：" + content + "；" + session.netTrack());
        // 发送消息到硬件处理
        sessionManager.sendHardwareMessage(content);
    }

    private void securityVerification(NettySession session, MessageInputStream message) {
        byte result = message.readByte();

        if (result == GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()) {
            // 登录错误
            logger.error("权限认证失败，错误码：" + message.readInt() + "；错误内容：" + message.readUTF());
            return;
        }
        String msg = message.readUTF();
        logger.info("权限验证结果消息内容：" + msg);

        JSONObject parameters = JSONObject.parseObject(msg);
        session.setAttribute("passportId", parameters.getJSONObject("response").getLongValue("passportId"));
        sessionManager.setMarketSession(session);
        // 启动心跳线程
        marketKeepAlive();
    }

    private AtomicBoolean connector = new AtomicBoolean(false);

    private void marketKeepAlive() {
        boolean result = connector.compareAndSet(false, true);
        if (!result) {
            logger.info("心跳任务已在执行中，无需再次发起......");
            return;
        }
        logger.info("心跳任务启动成功");
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                MessageOutputStream message = MessageFactory.createPlatformMessage(MessageFactory.MSG_ID_HEARTBEAT);
                message.writeUTF("商店心跳信息");

                boolean result = sessionManager.sendMarketMessage(message);
                if (!result) {
                    logger.error("由于连接已断开，心跳消息发送失败......");
                }
                AsyncScheduledService.submitCommonTask(this, ConfigFactory.getServer().getBothTimeout(), TimeUnit.SECONDS);
            }
        };
        AsyncScheduledService.submitCommonTask(runnable, ConfigFactory.getServer().getBothTimeout(), TimeUnit.SECONDS);
    }
}