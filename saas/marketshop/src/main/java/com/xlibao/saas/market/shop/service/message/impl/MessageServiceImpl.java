package com.xlibao.saas.market.shop.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.saas.market.shop.service.MarketSessionManager;
import com.xlibao.saas.market.shop.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/8/8.
 */
@Transactional
@Service("messageService")
public class MessageServiceImpl extends BasicWebService implements MessageService {

    @Autowired
    private MarketSessionManager marketSessionManager;

    @Override
    public JSONObject sendHardwarePush() {
        // 目标用户
        long passportId = getLongParameter("passportId");
        // 发送到硬件的内容
        String messageContent = getUTF("messageContent");
        // 仅发送 是否成功 必须等回报
        marketSessionManager.sendHardwarePush(passportId, messageContent);
        return success();
    }
}