package com.xlibao.saas.market.manager.service.marketmanager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.market.data.model.MarketEntry;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface MarketManagerService {

    //返回所有店铺
    public JSONObject getAllMarkets();

    public JSONObject searchMarkets(MarketEntry entry, int pageSize, int pageIndex);

    public MarketEntry getMarket(long id);

    public JSONObject getMarketItem(long id);

    //搜索店铺商品
    public JSONObject searchMarketItems(long marketId, String searchType, String searchKey, int pageSize, int pageIndex);
}