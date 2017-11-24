package com.xlibao.saas.market.shop.service.shop.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.io.entry.MessageFactory;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.MessageOutputStream;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.market.protocol.HardwareMessageType;
import com.xlibao.saas.market.shop.service.MarketSessionManager;
import com.xlibao.saas.market.shop.service.shop.ShopService;
import com.xlibao.saas.market.shop.service.support.remote.MarketRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/8/9.
 */
@Transactional
@Service("shopService")
public class ShopServiceImpl extends BasicWebService implements ShopService {

    private static final Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private MarketSessionManager marketSessionManager;

    @Override
    public MessageOutputStream securityVerification(MessageInputStream message, NettySession session) {
        String username = message.readUTF();
        String password = message.readUTF();
        int deviceType = message.readInt();
        int versionIndex = message.readInt();

        MessageOutputStream output = MessageFactory.createResponseMessage(message);
        try {
            JSONObject response = PassportRemoteService.loginPassport(username, password, deviceType, versionIndex);

            JSONObject passportData = response.getJSONObject("response");

            marketSessionManager.put(passportData.getLong("passportId"), session);

            output.writeByte(GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
            output.writeUTF(response.toString());

            MarketRemoteService.marketResponse(passportData.getLong("passportId"), GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
        } catch (Exception ex) {
            output.writeByte(GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
            if (ex instanceof XlibaoRuntimeException) {
                XlibaoRuntimeException xlibaoRuntimeException = (XlibaoRuntimeException) ex;

                output.writeInt(xlibaoRuntimeException.getCode());
                output.writeUTF(xlibaoRuntimeException.getMessage());
            } else {
                output.writeInt(FAIL_CODE);
                output.writeUTF(FAIL_MSG);
            }
        }
        return output;
    }

    @Override
    public void hardwareMessage(MessageInputStream message, NettySession session) {
        String messageType = message.readUTF();
        String hardwareMessage = message.readUTF();

        logger.info("【硬件】消息内容：message type is \"" + messageType + "\"， message content is \"" + hardwareMessage + "\"；" + session.netTrack());

        if (HardwareMessageType.WARN.equals(messageType)) {
            // 警告
        }
    }
}