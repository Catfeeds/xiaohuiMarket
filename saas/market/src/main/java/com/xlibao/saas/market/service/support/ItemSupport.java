package com.xlibao.saas.market.service.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.market.data.model.MarketItemLadderPrice;
import com.xlibao.metadata.order.OrderItemSnapshot;
import com.xlibao.market.data.model.MarketItem;
import com.xlibao.market.data.model.MarketItemDailyPurchaseLogger;
import com.xlibao.saas.market.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/25.
 */
@Transactional
@Component
public class ItemSupport {

    @Autowired
    private ItemService itemService;

    public OrderItemSnapshot fillOrderItemSnapshot(MarketItem item, MarketItemDailyPurchaseLogger itemDailyPurchaseLogger, int buyCount, List<MarketItemLadderPrice> itemLadderPrices) {
        return itemService.fillOrderItemSnapshot(item, itemDailyPurchaseLogger, buyCount, itemLadderPrices);
    }

    public Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap(List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers) {
        return itemService.itemDailyPurchaseLoggerMap(itemDailyPurchaseLoggers);
    }

    public void buyQualifications(List<MarketItem> items, List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers, JSONObject buyItems) {
        itemService.buyQualifications(items, itemDailyPurchaseLoggers, buyItems);
    }

    public Map<Long, List<MarketItemLadderPrice>> loadItemLadderPrices(List<MarketItem> items) {
        return itemService.loadItemLadderPrices(items);
    }
}