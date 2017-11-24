package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.market.data.model.MarketItem;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.metadata.passport.PassportStreet;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.config.LogicConfig;
import com.xlibao.saas.market.manager.service.itemmanager.ItemManagerService;
import com.xlibao.saas.market.manager.service.marketmanager.MarketManagerService;
import com.xlibao.saas.market.manager.service.passportmanager.PassportManagerService;
import com.xlibao.saas.market.manager.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/marketmanager/market")
public class MarketManagerController extends BaseController {

    @Autowired
    private MarketManagerService marketManagerService;

    @Autowired
    private PassportManagerService passportManagerService;

    @Autowired
    private ItemManagerService itemManagerService;

    //商店 列表 页面
    @RequestMapping("/markets")
    public String markets(ModelMap map) {

        String province = getUTF("province", "");
        String city = getUTF("city", "");
        String district = getUTF("district", "");

        long provinceId = getLongParameter("provinceId", 0);
        long cityId = getLongParameter("cityId", 0);
        long districtId = getLongParameter("districtId", 0);

        String street = getUTF("street", "");
        long streetId = getLongParameter("streetId", 0);

        int type = getIntParameter("type", -1);
        int status = getIntParameter("status", -1);
        int deliveryMode = getIntParameter("deliveryMode", -1);

        MarketEntry entry = new MarketEntry();
        entry.setProvince(province);
        entry.setCity(city);
        entry.setDistrict(district);
        entry.setStreet(street);
        entry.setStreetId(streetId);
        entry.setType(type);
        entry.setStatus(status);
        entry.setDeliveryMode(deliveryMode);

        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject marketJson = marketManagerService.searchMarkets(entry, pageSize, pageIndex);

        if (marketJson.getIntValue("code") != 0) {
            return remoteFail(map, marketJson, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_LIST);
        }

        JSONObject response = marketJson.getJSONObject("response");
        JSONArray entrys = response.getJSONArray("data");
        for (int i = 0; i < entrys.size(); i++) {
            JSONObject ejson = entrys.getJSONObject(i);
            Utils.changeData(ejson, "createTime");
            //状态
            int marketStatus = ejson.getIntValue("status");
            //如果与硬件断连
            ejson.put("statusOffline", 0);
            ejson.put("nowStatus", marketStatus);//当前值
            ejson.put("status", marketStatus);//原状态
        }
        map.put("markets", entrys);
        map.put("count", response.getIntValue("count"));
        //////////////////
        map.put("province", province);
        map.put("provinceId", provinceId);
        map.put("city", city);
        map.put("cityId", cityId);
        map.put("district", district);
        map.put("districtId", districtId);
        map.put("street", street);
        map.put("streetId", streetId);

        JSONObject streetJson;

        if (districtId != 0) {
            streetJson = passportManagerService.getStreets(districtId);
            if (streetJson.getInteger("code") == 0) {
                map.put("streets", streetJson.getJSONObject("response").getJSONArray("datas"));
            }
        }
        map.put("type", type);
        map.put("status", status);
        map.put("deliveryMode", deliveryMode);

        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        return jumpPage(map, LogicConfig.FTL_MARKET_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_LIST);
    }

    //商店 编辑 页面
    @RequestMapping("/marketEdit")
    public String marketEdit(ModelMap map) {
        long id = getLongParameter("marketId", 0);

        if (id != 0) {
            MarketEntry entry = marketManagerService.getMarket(id);
            if (entry == null) {
                return fail(map, "远程调用异常\n无法找到店铺信息,店铺id=" + id, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
            }
            map.put("market", entry);
            //拿街道
            if (entry.getStreetId() != 0) {
                //拿到这个街道
                JSONObject streetJson = passportManagerService.getStreetJson(entry.getStreetId());
                if (streetJson.getIntValue("code") == 0) {
                    PassportStreet street = parseObject(streetJson.getJSONObject("response").getString("data"), PassportStreet.class);
                    if (street != null) {
                        //拿所有数据
                        JSONObject streetsJson = passportManagerService.getStreets(street.getAreaId());
                        if (streetsJson.getIntValue("code") == 0) {
                            JSONArray array = streetsJson.getJSONObject("response").getJSONArray("datas");
                            map.put("streets", array);
                            map.put("streetId", street.getId());
                        }
                        //拿街道对应的区
                        PassportArea area = passportManagerService.getAreaById(street.getAreaId());
                        if (area != null) {
                            map.put("districtId", area.getId());

                            //拿区对应的市
                            PassportCity city = passportManagerService.getCityById(area.getCityId());
                            if (city != null) {
                                map.put("cityId", city.getId());

                                //拿市对应的省
                                PassportProvince province = passportManagerService.getProvinceById(city.getProvinceId());
                                if (province != null) {
                                    map.put("provinceId", province.getId());
                                }
                            }

                        }
                    }
                }
            }
        } else {
            //直接拿到所有数据
            long provinceId = getLongParameter("provinceId", 0);
            long cityId = getLongParameter("cityId", 0);
            long districtId = getLongParameter("districtId", 0);
            long streetId = getLongParameter("streetId", 0);

            map.put("provinceId", provinceId);
            map.put("cityId", cityId);
            map.put("districtId", districtId);
            map.put("streetId", streetId);

            if (provinceId != 0 && cityId != 0 && districtId != 0 && streetId != 0) {
                //拿到这个街道
                JSONObject streetJson = passportManagerService.getStreetJson(streetId);

                if (streetJson.getIntValue("code") == 0) {
                    PassportStreet street = parseObject(streetJson.getJSONObject("response").getString("data"), PassportStreet.class);
                    //拿所有数据
                    JSONObject streetsJson = passportManagerService.getStreets(street.getAreaId());
                    if (streetsJson.getIntValue("code") == 0) {
                        JSONArray array = streetsJson.getJSONObject("response").getJSONArray("datas");
                        map.put("streets", array);
                    }
                }
            }
        }
        return jumpPage(map, LogicConfig.FTL_MARKET_EDIT, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_LIST);
    }


    //商店 编辑 保存
    @ResponseBody
    @RequestMapping("/marketEditSave")
    public JSONObject marketEditSave() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/marketEditSave.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    //商店 编辑 保存
    @ResponseBody
    @RequestMapping("/marketUpdateStatus")
    public JSONObject marketUpdateStatus() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/marketUpdateStatus.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    //商店 编辑 保存
    @ResponseBody
    @RequestMapping("/marketItemUpdateStatus")
    public JSONObject marketItemUpdateStatus() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/item/manager/marketItemUpdateStatus.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    //提供给页面，拿到 街道列表 JSONObject 信息
    @ResponseBody
    @RequestMapping("/getStreets")
    public JSONObject getStreets() {
        JSONObject json = passportManagerService.getStreets(getLongParameter("districtId"));
        if (json.getIntValue("code") == 0) {
            Map map = new HashMap<>();
            JSONArray array = json.getJSONObject("response").getJSONArray("datas");
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                map.put(obj.getInteger("id"), obj.getString("name"));
            }
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("datas", map);
            return result;
        } else {
            JSONObject result = new JSONObject();
            result.put("code", 1);
            result.put("msg", json.getString("msg"));
            return result;
        }
    }

    //街道页面
    @RequestMapping("/streets")
    public String streets(ModelMap map) {

        //如果没有选区域，则默认为0
        long provinceId = getLongParameter("provinceId", 10018);//默认广东省
        long cityId = getLongParameter("cityId", 10212);//默认广州市
        long areaId = getLongParameter("areaId", 11919);//默认天河区

        if (areaId != 0) {
            JSONObject streets = passportManagerService.getStreets(areaId);
            if (streets.getIntValue("code") == 0) {
                map.put("streets", streets.getJSONObject("response").getJSONArray("datas"));
            }
        }

        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        map.put("areaId", areaId);

        return jumpPage(map, LogicConfig.FTL_MARKET_STREET_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_STREET_LIST);
    }

    //街道编辑页面
    @RequestMapping("/streetEdit")
    public String streetEdit(ModelMap map) {

        //如果没有选区域，则默认为0
        long provinceId = getLongParameter("provinceId", 10018);//默认广东省
        long cityId = getLongParameter("cityId", 10212);//默认广州市
        long areaId = getLongParameter("areaId", 11919);//默认天河区
        long id = getLongParameter("id", 0);

        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        map.put("areaId", areaId);

        if (id != 0) {
            PassportStreet street = passportManagerService.getStreet(id);
            if (street == null) {
                return jumpPage(map, LogicConfig.FTL_MARKET_STREET_EDIT, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_STREET_LIST);
            }
            map.put("street", street);
        }

        return jumpPage(map, LogicConfig.FTL_MARKET_STREET_EDIT, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_STREET_LIST);
    }

    //街道编辑保存
    @ResponseBody
    @RequestMapping("/streetEditSave")
    public JSONObject streetEditSave() {
        String title = getUTF("title");
        long areaId = getLongParameter("areaId");
        long id = getLongParameter("id", 0);
        return passportManagerService.streetEditSave(id, areaId, title);
    }

    //店铺任务
    @RequestMapping("/marketTasks")
    public String marketTasks(ModelMap modelMap) {
        long marketId = getLongParameter("marketId", 0);
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        modelMap.put("marketId", marketId);
        //拿到所有店铺
        JSONObject marketResponse = marketManagerService.getAllMarkets();
        if (marketResponse.getIntValue("code") == 0) {
            modelMap.put("markets", marketResponse.getJSONObject("response").getJSONArray("datas"));
        }
        if (marketId != 0) {

            //填充店铺数据
            Map map = new HashMap();
            map.put("marketId", marketId);
            map.put("pageSize", String.valueOf(pageSize));
            map.put("pageStartIndex", getPageStartIndex(pageSize));

            String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/unExecutorTask.do?marketId=" + marketId + "&pageSize=" + pageSize + "&pageStartIndex=" + getPageStartIndex(pageSize));
            JSONObject response = JSONObject.parseObject(json);
            if (response.getIntValue("code") == 0) {
                modelMap.put("tasks", response.getJSONObject("response").getJSONArray("datas"));
            }
        }

        modelMap.put("pageSize", pageSize);
        modelMap.put("pageIndex", pageIndex);

        return jumpPage(modelMap, LogicConfig.FTL_MARKET_TASK_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_TASK_LIST);
    }

    //店铺异常任务记录
    @RequestMapping("/marketErrorTasks")
    public String marketErrorTasks(ModelMap modelMap) {

        long marketId = getLongParameter("marketId", 0);
        String happenDate = getUTF("happenDate", "");
        int pageSize = getPageSize();
        int pageIndex = getPageStartIndex(pageSize);

        //拿到所有店铺
        JSONObject marketResponse = marketManagerService.getAllMarkets();
        if (marketResponse.getIntValue("code") == 0) {
            modelMap.put("markets", marketResponse.getJSONObject("response").getJSONArray("datas"));
        }

        modelMap.put("marketId", marketId);
        modelMap.put("happenDate", happenDate);
        modelMap.put("pageSize", pageSize);
        modelMap.put("pageIndex", pageIndex);

        if(marketId != 0) {

            Map<String, String> map = new HashMap<>();
            map.put("marketId", String.valueOf(marketId));
            map.put("happenDate", happenDate);
            map.put("pageSize", String.valueOf(pageSize));
            map.put("pageIndex", String.valueOf(pageIndex));

            String json = HttpRequest.post(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/openapi/showExceptionTask.do", map);
            JSONObject response = JSONObject.parseObject(json);

            if (response.getIntValue("code") == 0) {
                JSONArray array = response.getJSONObject("response").getJSONArray("data");
                int count = response.getIntValue("maxSize");

                modelMap.put("count", count);
                modelMap.put("tasks", array);
            }
        }

        if(happenDate.length() > 0 && marketId != 0) {
            String marketName = getUTF("marketName", "");
            modelMap.put("marketName", marketName);
            return jumpPage(modelMap, LogicConfig.FTL_MARKET_ERROR_TASK_LIST_2, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_ERROR_TASK_LIST);
        } else {
        return jumpPage(modelMap, LogicConfig.FTL_MARKET_ERROR_TASK_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_ERROR_TASK_LIST);
        }
    }

    //店铺商品
    @RequestMapping("/marketItems")
    public String marketItems(ModelMap map) {

        long marketId = getLongParameter("marketId", 0);
        String searchType = getUTF("searchType", "");
        String searchKey = getUTF("searchKey", "");
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject marketResponse = marketManagerService.getAllMarkets();
        if (marketResponse.getIntValue("code") == 0) {
            map.put("markets", marketResponse.getJSONObject("response").getJSONArray("datas"));
        }

        map.put("marketId", marketId);
        map.put("searchType", searchType);
        map.put("searchKey", searchKey);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);

        //根据搜索内容，查找数据
        JSONObject itemResponse = marketManagerService.searchMarketItems(marketId, searchType, searchKey, pageSize, pageIndex);
        if (itemResponse.getIntValue("code") == 0) {
            //成功，则注入商品
            JSONObject response = itemResponse.getJSONObject("response");
            JSONArray array = response.getJSONArray("datas");

            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                //重置促销
                Utils.changePrice(json, "discountPrice");
                //重置成本
                Utils.changePrice(json, "costPrice");
                //重置市场价
                Utils.changePrice(json, "marketPrice");
                //重置销售价
                Utils.changePrice(json, "sellPrice");
            }
            map.put("items", array);
            map.put("count", response.getIntValue("count"));
        }

        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);

        return jumpPage(map, LogicConfig.FTL_MARKET_ITEM_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_ITEM_LIST);
    }

    //新增 店铺商品
    @RequestMapping("/marketItemAdd")
    public String marketItemAdd(ModelMap map) {

        long marketId = getLongParameter("marketId", 0);

        JSONObject response = marketManagerService.getAllMarkets();
        if (response.getIntValue("code") == 0) {
            map.put("markets", response.getJSONObject("response").getJSONArray("datas"));
        }

        //所有类型
        map.put("itemTypes", itemManagerService.getSelectItemTypes());
        map.put("marketId", marketId);

        return jumpPage(map, LogicConfig.FTL_MARKET_ITEM_ADD, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_ITEM_LIST);
    }

    //新增 店铺商品 保存
    @ResponseBody
    @RequestMapping("/marketItemAddSave")
    public JSONObject marketItemAddSave() {
        Map<String, String> map = getMapParameter();
        String json = HttpRequest.post(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/item/manager/marketItemAddSave.do", map);
        return parseObject(json);
    }


    //编辑 店铺商品
    @RequestMapping("/marketItemEdit")
    public String marketItemEdit(ModelMap map) {

        long id = getLongParameter("id");
        String searchType = getUTF("searchType", "");
        String searchKey = getUTF("searchKey", "");
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        map.put("searchType", searchType);
        map.put("searchKey", searchKey);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);

        JSONObject itemResponse = marketManagerService.getMarketItem(id);
        if (itemResponse.getIntValue("code") != 0) {
            return remoteFail(map, itemResponse, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_ITEM_LIST);
        }
        JSONObject itemJson = itemResponse.getJSONObject("response").getJSONObject("datas");
        MarketItem marketItem = JSONObject.parseObject(itemJson.toJSONString(), MarketItem.class);
        //取得其商店
        MarketEntry entry = marketManagerService.getMarket(marketItem.getOwnerId());
        if (entry == null) {
            return fail(map, "远程调用异常\n无法找到店铺信息,店铺id=" + marketItem.getOwnerId(), LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
        }
        map.put("market", entry);
        //取得其模板
        ItemTemplate itemTemplate = itemManagerService.getItemTemplate(marketItem.getItemTemplateId());
        if (itemTemplate == null) {
            return fail(map, "远程调用异常\n无法找到商品模板信息,商品模板id=" + marketItem.getItemTemplateId(), LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
        }
        map.put("itemTemplate", itemTemplate);

        //替换价格
        //重置促销
        Utils.changePrice(itemJson, "discountPrice");
        //重置成本
        Utils.changePrice(itemJson, "costPrice");
        //重置市场价
        Utils.changePrice(itemJson, "marketPrice");
        //重置销售价
        Utils.changePrice(itemJson, "sellPrice");

        map.put("marketItem", itemJson);

        return jumpPage(map, LogicConfig.FTL_MARKET_ITEM_EDIT, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_ITEM_LIST);
    }


    // 编辑 店铺商品 保存
    @ResponseBody
    @RequestMapping("/marketItemEditSave")
    public JSONObject marketItemEditSave() {
        Map<String, String> map = getMapParameter();
        String json = HttpRequest.post(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/item/manager/marketItemEditSave.do", map);
        return parseObject(json);
    }


    //商店 货架 信息
    @RequestMapping("/marketShelves")
    public String marketShelves(ModelMap map) {
        long marketId = getLongParameter("marketId", 0);

        //拿到所有店铺
        JSONObject response = marketManagerService.getAllMarkets();
        if (response.getIntValue("code") == 0) {
            List<MarketEntry> entrys = JSONObject.parseArray(response.getJSONObject("response").getString("datas"), MarketEntry.class);
            if (entrys.size() > 0) {
                if (marketId == 0) {
                    marketId = entrys.get(0).getId();
                }
                map.put("markets", entrys);
            }
            if (marketId != 0) {
                //拿走道
                String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/getShelvesMarks.do?marketId=" + marketId);
                JSONObject jsonObject = JSONObject.parseObject(json);
                if (jsonObject.getIntValue("code") == 0) {
                    List<String> groups = JSONObject.parseArray(jsonObject.getJSONObject("response").getString("datas"), String.class);
                    map.put("groups", groups);
                }
            }
        }

        //所有类型
        map.put("itemTypes", itemManagerService.getSelectItemTypes());

        map.put("marketId", marketId);

        return jumpPage(map, LogicConfig.FTL_MARKET_SHELVES_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_SHELVES_LIST);
    }

    //拿商店的一些 走道，层 等数据
    @ResponseBody
    @RequestMapping("/getShelvesMarks")
    public JSONObject getShelvesMarks() {
        long marketId = getLongParameter("marketId");
        String groupCode = getUTF("groupCode", "");
        String unitCode = getUTF("unitCode", "");
        int shelvesType = getIntParameter("shelvesType", 0);

        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/getShelvesMarks.do?marketId=" + marketId + "&groupCode=" + groupCode + "&unitCode=" + unitCode + "&shelvesType=" + shelvesType);
        return parseObject(json);
    }

    //获得弹夹数据
    @ResponseBody
    @RequestMapping("/loaderClipDatas")
    public JSONObject loaderClipDatas() {

        long marketId = getLongParameter("marketId");
        String groupCode = getUTF("groupCode");
        String unitCode = getUTF("unitCode");
        String floorCode = getUTF("floorCode");
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/loaderClipDatas.do?marketId=" + marketId + "&groupCode=" + groupCode + "&unitCode=" + unitCode + "&floorCode=" + floorCode + "&pageSize=" + 1000 + "&pageStartIndex=" + pageStartIndex);
        return parseObject(json);
    }

    //检测当天

    //检测并返回商品上架任务详情
    @ResponseBody
    @RequestMapping("/checkPrepareActionTask")
    public JSONObject checkPrepareActionTask() {
        long taskId = getLongParameter("taskId");

        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/checkPrepareActionTask.do?taskId=" + taskId);
        return parseObject(json);
    }

    //取消商品上架任务
    @ResponseBody
    @RequestMapping("/cancelPrepareActionTask")
    public JSONObject cancelPrepareActionTask() {
        long passportId = getLongParameter("passportId");
        long taskId = getLongParameter("taskId");
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/cancelPrepareActionTask.do?taskId=" + taskId + "&passportId=" + passportId);
        return parseObject(json);
    }

    //提交所有上架任务
    @ResponseBody
    @RequestMapping(value = "/prepareAction", method = RequestMethod.POST)
    public JSONObject prepareAction() {

        long marketId = getLongParameter("marketId");
        String actionDatas = getUTF("actionDatas");
        String hopeExecutorDate = getUTF("hopeExecutorDate", null);

        String actionGroup[] = actionDatas.split(",");
        JSONArray array = new JSONArray();
        for (int i = 0; i < actionGroup.length; i++) {
            String action = actionGroup[i];
            String actionPre[] = action.split("-");
            JSONObject obj = new JSONObject();
            obj.put("location", actionPre[0]);
            obj.put("itemTemplateId", Long.parseLong(actionPre[1]));
            obj.put("quantity", Integer.parseInt(actionPre[2]));
            array.add(obj);
        }

        Map map = new HashMap();
        map.put("passportId", String.valueOf(0));
        map.put("marketId", String.valueOf(marketId));
        map.put("actionDatas", array.toJSONString());
        if (hopeExecutorDate != null) {
            map.put("hopeExecutorDate", hopeExecutorDate);
        }

        String json = HttpRequest.post(ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/manager/prepareAction.do", map);
        JSONObject response = JSONObject.parseObject(json);

        return response;
    }

}