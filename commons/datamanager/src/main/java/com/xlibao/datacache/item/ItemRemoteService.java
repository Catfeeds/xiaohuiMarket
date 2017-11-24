package com.xlibao.datacache.item;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.http.HttpUtils;
import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.item.ItemType;
import com.xlibao.metadata.item.ItemUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chinahuangxc on 2017/4/13.
 */
class ItemRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(ItemRemoteService.class);

    static List<ItemTemplate> loaderItemTemplates() {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/loaderItemTemplates";
        return getItemTemplates(url);
    }

    static ItemTemplate getItemTemplate(long itemTemplateId) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/getItemTemplate?itemTemplateId=" + itemTemplateId;
        return getItemTemplate(url);
    }

    static ItemTemplate getItemTemplateByName(String itemName) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/getItemTemplateByName?itemName=" + itemName;
        return getItemTemplate(url);
    }

    static ItemTemplate getItemTemplateForBarcode(String barcode) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/getItemTemplateByBarcode?barcode=" + barcode;
        return getItemTemplate(url);
    }

    static List<ItemTemplate> relationItemTemplates(long itemTypeId) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/relationItemTemplate?itemTypeId=" + itemTypeId;
        return getItemTemplates(url);
    }

    private static ItemTemplate getItemTemplate(String url) {
        String result = HttpRequest.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return JSONObject.parseObject(response.getString("response"), ItemTemplate.class);
    }

    private static List<ItemTemplate> getItemTemplates(String url) {
        String result = HttpUtils.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        response = response.getJSONObject("response");

        // logger.info("远程获取商品模版的结果：" + response);

        JSONArray itemTemplateArray = response.getJSONArray("itemTemplateArray");

        List<ItemTemplate> itemTemplates = new ArrayList<>();

        for (int i = 0; i < itemTemplateArray.size(); i++) {
            itemTemplates.add(JSONObject.parseObject(itemTemplateArray.getString(i), ItemTemplate.class));
        }
        return itemTemplates;
    }

    static List<ItemType> loaderItemTypes() {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/loaderItemTypes";
        return getItemTypes(url);
    }

    static ItemType getItemType(long itemTypeId) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/getItemType?itemTypeId=" + itemTypeId;
        String result = HttpUtils.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return JSONObject.parseObject(response.getString("response"), ItemType.class);
    }

    static List<ItemType> relationItemTypes(long parentItemTypeId) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/relationItemTypes?parentItemTypeId=" + parentItemTypeId;
        return getItemTypes(url);
    }

    private static List<ItemType> getItemTypes(String url) {
        String result = HttpUtils.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        response = response.getJSONObject("response");

        // logger.info("远程获取商品类型的结果：" + response);

        JSONArray itemTypeArray = response.getJSONArray("itemTypeArray");

        List<ItemType> itemTypes = new ArrayList<>();

        for (int i = 0; i < itemTypeArray.size(); i++) {
            itemTypes.add(JSONObject.parseObject(itemTypeArray.getString(i), ItemType.class));
        }
        return itemTypes;
    }

    static List<ItemUnit> loaderItemUnits() {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/loaderItemUnits";
        String result = HttpUtils.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        response = response.getJSONObject("response");

        // logger.info("远程获取商品单位的结果：" + response);

        JSONArray itemUnitArray = response.getJSONArray("itemUnitArray");

        List<ItemUnit> itemUnits = new ArrayList<>();

        for (int i = 0; i < itemUnitArray.size(); i++) {
            itemUnits.add(JSONObject.parseObject(itemUnitArray.getString(i), ItemUnit.class));
        }
        return itemUnits;
    }

    static ItemUnit getItemUnit(long itemUnitId) {
        String url = DataCacheApplicationContextLoaderNotify.getItemRemoteServiceURL() + "item/getItemUnit?itemUnitId=" + itemUnitId;
        String result = HttpUtils.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return JSONObject.parseObject(response.getString("response"), ItemUnit.class);
    }
}