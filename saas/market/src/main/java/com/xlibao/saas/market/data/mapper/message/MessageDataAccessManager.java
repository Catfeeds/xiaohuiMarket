package com.xlibao.saas.market.data.mapper.message;

import com.xlibao.saas.market.data.model.MarketCoreMessageLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/30.
 */
@Component
public class MessageDataAccessManager {

    @Autowired
    private MarketCoreMessageLoggerMapper coreMessageLoggerMapper;

    public int createMessageLogger(MarketCoreMessageLogger coreMessageLogger) {
        return coreMessageLoggerMapper.createMessageLogger(coreMessageLogger);
    }

    public MarketCoreMessageLogger getMessageLogger(long marketId, String keyword) {
        return coreMessageLoggerMapper.getMessageLogger(marketId, keyword);
    }
}