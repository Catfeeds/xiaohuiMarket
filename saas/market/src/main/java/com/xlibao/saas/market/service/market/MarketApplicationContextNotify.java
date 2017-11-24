package com.xlibao.saas.market.service.market;

import com.xlibao.saas.market.service.order.OrderEventListenerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/7/20.
 */
@Component
public class MarketApplicationContextNotify {

    private static final String MODULE_NAME = "MARKET";

    @Autowired
    private MarketDataCacheService marketDataCacheService;

    @Autowired
    private OrderEventListenerManager orderEventListenerManager;
    @Autowired
    private MarketOrderEventListenerImpl marketOrderEventListener;

    public void loaderNotify() {
        marketDataCacheService.initMarketCache();

        orderEventListenerManager.registerOrderEventListener(MODULE_NAME, marketOrderEventListener);
    }
}