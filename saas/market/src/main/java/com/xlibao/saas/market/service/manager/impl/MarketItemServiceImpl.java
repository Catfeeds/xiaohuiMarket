package com.xlibao.saas.market.service.manager.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.market.data.model.MarketItem;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.saas.market.config.ConfigFactory;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.service.item.ItemService;
import com.xlibao.saas.market.service.item.MarketItemErrorCodeEnum;
import com.xlibao.saas.market.service.manager.MarketItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/27 0027.
 */
@Transactional
@Service("marketItemService")
public class MarketItemServiceImpl extends BasicWebService implements MarketItemService {

    @Autowired
    private DataAccessFactory dataAccessFactory;

    //按弹夹搜索分页的店铺商品
    public JSONObject searchMarketItems() {
        long marketId = getLongParameter("marketId", 0);
        String searchType = getUTF("searchType", null);
        String searchKey = getUTF("searchKey", null);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        if (searchKey == null) {
            searchType = null;
        }

        int count = dataAccessFactory.getItemDataAccessManager().searchMarketItemCount(marketId, searchType, searchKey, pageStartIndex, pageSize);

        List<MarketItem> items = dataAccessFactory.getItemDataAccessManager().searchMarketItems(marketId, searchType, searchKey, pageStartIndex, pageSize);
        if (items != null && items.size() > 0) {
            JSONObject response = new JSONObject();
            response.put("count", count);
            response.put("datas", items);
            return success(response);
        } else {
            return fail("该店铺没有商品");
        }
    }

    public JSONObject marketItemUpdateStatus() {
        long itemId = getLongParameter("itemId");
        byte status = getByteParameter("status");
        if (dataAccessFactory.getItemDataAccessManager().marketItemUpdateStatus(itemId, status) > 0) {
            return success("修改成功");
        } else {
            return fail("修改失败");
        }
    }

    public JSONObject getMarketItem() {
        long id = getLongParameter("id");
        MarketItem item = dataAccessFactory.getItemDataAccessManager().getMarketItem(id);
        if (item != null) {
            return success(item);
        } else {
            return fail("没有该商品");
        }
    }

    public JSONObject marketItemEditSave() {

        long itemId = getLongParameter("itemId");
        long costPrice = CommonUtils.changeMoney(getUTF("costPrice"));
        long sellPrice = CommonUtils.changeMoney(getUTF("sellPrice"));
        long marketPrice = CommonUtils.changeMoney(getUTF("marketPrice"));
        long discountPrice = CommonUtils.changeMoney(getUTF("discountPrice"));
        byte status = getByteParameter("status", (byte) 1);//默认可售状态
        String des = getUTF("description", "");

        if (dataAccessFactory.getItemDataAccessManager().updateItem(itemId, costPrice, sellPrice, marketPrice, discountPrice, status, des) > 0) {
            return success("修改成功");
        } else {
            return fail("修改失败");
        }
    }


    public JSONObject marketItemAddSave() {

        long marketId = getLongParameter("marketId");
        long itemTemplateId = getLongParameter("itemTemplateId");

        long costPrice = CommonUtils.changeMoney(getUTF("costPrice"));
        long sellPrice = CommonUtils.changeMoney(getUTF("sellPrice"));
        long marketPrice = CommonUtils.changeMoney(getUTF("marketPrice"));
        long discountPrice = CommonUtils.changeMoney(getUTF("discountPrice"));
        byte status = getByteParameter("status", (byte) 1);//默认可售状态
        String des = getUTF("description", "");

        //取得商店
        MarketEntry entry = dataAccessFactory.getMarketDataAccessManager().getMarket(marketId);
        if (entry == null) {
            return fail("没有指定的店铺信息，店铺ID=" + marketId);
        }

        //如果该店已有商品
        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplateId);
        if (item != null) {
            return fail("店铺已存在该商品");
        }

        String itemJson = HttpRequest.get(ConfigFactory.getDomainNameConfig().itemRemoteURL + "/item/getItemTemplate?itemTemplateId=" + itemTemplateId);
        JSONObject itemResponse = JSONObject.parseObject(itemJson);
        if (itemResponse.getIntValue("code") != 0) {
            return fail("没有指定的商品模板信息，商品模板ID=" + itemTemplateId);
        }
        ItemTemplate itemTemplate = JSONObject.parseObject(itemResponse.getString("response"), ItemTemplate.class);

        MarketItem marketItem = new MarketItem();
        marketItem.setOwnerId(marketId);
        marketItem.setItemTemplateId(itemTemplateId);
        marketItem.setDefineName(itemTemplate.getName());
        marketItem.setDefineImage(itemTemplate.getImageUrl());
        marketItem.setCreateTime(new Date(System.currentTimeMillis()));
        marketItem.setCostPrice(costPrice);
        marketItem.setSellPrice(sellPrice);
        marketItem.setMarketPrice(marketPrice);
        marketItem.setDiscountPrice(discountPrice);
        marketItem.setStatus(status);
        marketItem.setDescription(des);

        if (dataAccessFactory.getItemDataAccessManager().createItem(marketItem) > 0) {
            return success("添加成功");
        } else {
            return fail("添加失败");
        }
    }

    @Override
    public JSONObject existItemTemplate() {
        long marketId = getLongParameter("marketId");
        long itemTemplateId = getLongParameter("itemTemplateId");

        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplateId);
        return item == null ? MarketItemErrorCodeEnum.NOT_FOUND_ITEM.response() : success();
    }
}
