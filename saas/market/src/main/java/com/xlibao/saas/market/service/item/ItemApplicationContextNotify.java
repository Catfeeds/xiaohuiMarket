package com.xlibao.saas.market.service.item;

import com.xlibao.saas.market.service.order.OrderEventListenerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/26.
 */
@Component
public class ItemApplicationContextNotify {

    private static final String MODULE_NAME = "item";

    @Autowired
    private MarketItemDataCacheService marketItemDataCacheService;

    @Autowired
    private OrderEventListenerManager orderEventListenerManager;
    @Autowired
    private ItemOrderEventListenerImpl itemOrderEventListener;

    public void loaderNotify() {
        marketItemDataCacheService.initMarketItemCache();

        orderEventListenerManager.registerOrderEventListener(MODULE_NAME, itemOrderEventListener);
    }
}