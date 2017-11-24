package com.xlibao.saas.market.service.manager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.market.data.model.MarketItem;
import com.xlibao.saas.market.service.item.MarketItemErrorCodeEnum;

/**
 * Created by Administrator on 2017/8/27 0027.
 */
public interface MarketItemService {

    //按弹夹搜索分页的店铺商品
    JSONObject searchMarketItems();

    JSONObject getMarketItem();

    JSONObject marketItemEditSave();

    JSONObject marketItemAddSave();

    JSONObject existItemTemplate();

    JSONObject marketItemUpdateStatus();
}
