package com.xlibao.saas.market.service.item;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.market.data.model.MarketItemLadderPrice;
import com.xlibao.metadata.order.OrderItemSnapshot;
import com.xlibao.market.data.model.MarketItem;
import com.xlibao.market.data.model.MarketItemDailyPurchaseLogger;

import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface ItemService {

    JSONObject homepage();

    JSONObject itemTypes();

    JSONObject findSubItemTypes();

    JSONObject findRecommendItems();

    JSONObject pageItems();

    JSONObject splitItems();

    Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap(List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers);

    OrderItemSnapshot fillOrderItemSnapshot(MarketItem item, MarketItemDailyPurchaseLogger itemDailyPurchaseLogger, int buyCount, List<MarketItemLadderPrice> itemLadderPrices);

    void buyQualifications(List<MarketItem> items, List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers, JSONObject buyItems);

    JSONObject existItemTemplate();

    JSONObject editItem();

    JSONObject findItemLocation();

    JSONObject loaderHotSearch();

    JSONObject fuzzyMatchItemName();

    JSONObject createItemLadderPrice();

    JSONObject itemLadderPrices();

    JSONObject removeItemLadderPrice();

    Map<Long, List<MarketItemLadderPrice>> loadItemLadderPrices(List<MarketItem> items);
}
