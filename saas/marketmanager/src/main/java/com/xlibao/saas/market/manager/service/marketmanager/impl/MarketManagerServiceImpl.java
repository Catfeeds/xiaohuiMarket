package com.xlibao.saas.market.manager.service.marketmanager.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.service.marketmanager.MarketManagerService;
import com.xlibao.saas.market.manager.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("marketManagerService")
public class MarketManagerServiceImpl implements MarketManagerService {

    public JSONObject getAllMarkets() {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/getAllMarkets.do");
        JSONObject response = JSONObject.parseObject(json);
        return response;
    }

    public JSONObject searchMarkets(MarketEntry entry, int pageSize, int pageIndex) {

        StringBuilder sb = new StringBuilder();
        Map map = new HashMap<>();
        Utils.appendStr(sb, "province", entry.getProvince());
        Utils.appendStr(sb, "city", entry.getCity());
        Utils.appendStr(sb, "district", entry.getDistrict());
        Utils.appendStr(sb, "street", entry.getStreet());
        Utils.appendLong(sb, "streetId", entry.getStreetId());
        Utils.appendStr(sb, "streetNumber", entry.getStreetNumber());
        Utils.appendInt(sb, "type", entry.getType());
        Utils.appendInt(sb, "status", entry.getStatus());
        Utils.appendInt(sb, "deliveryMode", entry.getDeliveryMode());
        Utils.appendInt(sb, "pageSize", pageSize);
        Utils.appendInt(sb, "pageIndex", pageIndex);
        String json = HttpRequest.post(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/searchMarkets.do?" + sb.toString(), map);
        JSONObject response = JSONObject.parseObject(json);
        return response;
    }

    public MarketEntry getMarket(long id) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/getMarket.do?id=" + id);
        JSONObject response = JSONObject.parseObject(json);
        if(response.getIntValue("code") != 0) {
            return null;
        }
        return JSONObject.parseObject(response.getString("response"), MarketEntry.class);
    }


    public JSONObject getMarketItem(long id) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/item/manager/getMarketItem.do?id=" + id);
        JSONObject response = JSONObject.parseObject(json);
        return response;
    }

    @Override
    public JSONObject searchMarketItems(long marketId, String searchType, String searchKey, int pageSize, int pageIndex) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/item/manager/searchMarketItems.do?marketId=" + marketId + "&searchType=" + searchType + "&searchKey=" + searchKey + "&pageSize=" + pageSize + "&pageIndex=" + pageIndex);
        JSONObject response = JSONObject.parseObject(json);
        return response;
    }


}