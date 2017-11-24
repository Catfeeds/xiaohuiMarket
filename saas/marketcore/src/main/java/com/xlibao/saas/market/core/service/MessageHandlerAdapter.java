package com.xlibao.saas.market.core.service;

import com.xlibao.io.entry.MessageFactory;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.saas.market.core.service.application.ApplicationService;
import com.xlibao.saas.market.core.service.hardware.HardwareService;
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
    private ApplicationService applicationService;
    @Autowired
    private HardwareService hardwareService;

    public void hardwareMessageExecute(NettySession session, String content) {
        logger.info("收到来自硬件的消息，由硬件服务处理");
        hardwareService.fromHardwareMessageExecute(session, content);
    }

    public void marketMessageExecute(NettySession session, MessageInputStream message) {
        // logger.info("收到来自商店业务的消息，由应用程序的服务处理");
        byte messageType = message.getMsgType();

        switch (messageType) {
            case MessageFactory.MSG_TYPE_INTERNAL: // 与硬件交互的消息(发送给硬件)
                applicationService.toHardwareMessageExecute(session, message);
                break;
            case MessageFactory.MSG_TYPE_PLATFORM: // 仅存在心跳消息(保持与商店业务的交互)
                applicationService.platformMessageExecute(session, message);
                break;
            case MessageFactory.MSG_TYPE_LOGIC: // 逻辑消息(仅存在权限验证)
                applicationService.logicMessageExecute(session, message);
                break;
        }
    }
}