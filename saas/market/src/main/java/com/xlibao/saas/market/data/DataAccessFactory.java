package com.xlibao.saas.market.data;

import com.xlibao.saas.market.data.mapper.activity.ActivityDataAccessManager;
import com.xlibao.saas.market.data.mapper.item.ItemDataAccessManager;
import com.xlibao.saas.market.data.mapper.market.MarketDataAccessManager;
import com.xlibao.saas.market.data.mapper.message.MessageDataAccessManager;
import com.xlibao.saas.market.data.mapper.order.OrderDataAccessManager;
import com.xlibao.saas.market.data.mapper.question.QuestionDataAccessManager;
import com.xlibao.saas.market.service.item.MarketItemDataCacheService;
import com.xlibao.saas.market.service.market.MarketDataCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/7/16.
 */
@Component
public class DataAccessFactory {

    @Autowired
    private ActivityDataAccessManager activityDataAccessManager;
    @Autowired
    private ItemDataAccessManager itemDataAccessManager;
    @Autowired
    private MarketDataAccessManager marketDataAccessManager;
    @Autowired
    private OrderDataAccessManager orderDataAccessManager;
    @Autowired
    private MessageDataAccessManager messageDataAccessManager;
    @Autowired
    private QuestionDataAccessManager questionDataAccessManager;

    @Autowired
    private MarketDataCacheService marketDataCacheService;
    @Autowired
    private MarketItemDataCacheService marketItemDataCacheService;

    public ActivityDataAccessManager getActivityDataAccessManager() {
        return activityDataAccessManager;
    }

    public ItemDataAccessManager getItemDataAccessManager() {
        return itemDataAccessManager;
    }

    public MarketDataAccessManager getMarketDataAccessManager() {
        return marketDataAccessManager;
    }

    public OrderDataAccessManager getOrderDataAccessManager() {
        return orderDataAccessManager;
    }

    public MarketDataCacheService getMarketDataCacheService() {
        return marketDataCacheService;
    }

    public MarketItemDataCacheService getMarketItemDataCacheService() {
        return marketItemDataCacheService;
    }

    public MessageDataAccessManager getMessageDataAccessManager() {
        return messageDataAccessManager;
    }

    public QuestionDataAccessManager getQuestionDataAccessManager() {
        return questionDataAccessManager;
    }
}