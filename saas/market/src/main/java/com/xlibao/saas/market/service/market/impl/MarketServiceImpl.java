package com.xlibao.saas.market.service.market.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.passport.ClientTypeEnum;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.datacache.location.LocationDataCacheService;
import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.market.data.model.MarketRelationship;
import com.xlibao.metadata.passport.*;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.data.model.MarketAccessLogger;
import com.xlibao.saas.market.service.market.*;
import com.xlibao.saas.market.service.support.remote.MarketShopRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("marketService")
public class MarketServiceImpl extends BasicWebService implements MarketService {

    private static final Logger logger = LoggerFactory.getLogger(MarketServiceImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;
    @Autowired
    private MarketShopRemoteService marketShopRemoteService;

    @Override
    public JSONObject findMarket() {
        long passportId = getLongParameter("passportId", 0);
        long marketId = getLongParameter("marketId", 0);

        double longitude = getDoubleParameter("longitude", 0.0);
        double latitude = getDoubleParameter("latitude", 0.0);

        MarketEntry marketEntry = null;
        MarketFindTypeEnum findType = MarketFindTypeEnum.CLIENT_PROVIDE;
        if (marketId != 0) {
            marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        }
        if (marketEntry == null) {
            logger.error("没有找到指定的商店(market id:" + marketId + ", passport id:" + passportId + ")，尝试获取上次访问的商店");
            MarketAccessLogger accessLogger = dataAccessFactory.getMarketDataAccessManager().getLastAccessLogger(passportId);
            if (accessLogger != null) { // 寻找上次访问的商店
                logger.info("找到用户最后一次访问的商店(market id:" + accessLogger.getMarketId() + ", passport id:" + passportId + ")");
                marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(accessLogger.getMarketId());
                findType = MarketFindTypeEnum.RECENT_ACCESS;
            }
        }
        if (marketEntry == null) {
            logger.error("没法找到任何一个适合用户的商店(passport id:" + passportId + ")，用户只能通过过滤方式进行选择商店");
            // 1000 -- 您所在区域暂时未找到合适的商店
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response();
        }
        JSONObject response = marketEntry.message(latitude, longitude);
        response.put("findType", findType.getKey());
        return success(response);
    }

    @Override
    public JSONObject filterMarket() {
        int type = getIntParameter("type", ChoiceMarketTypeEnum.PROVINCE.getKey());
        long parentId = getLongParameter("parentId", 0);

        double longitude = getDoubleParameter("longitude", 0.0);
        double latitude = getDoubleParameter("latitude", 0.0);

        if (type == ChoiceMarketTypeEnum.PROVINCE.getKey()) {
            return success(provinceMessage(LocationDataCacheService.getProvinces()));
        }
        if (type == ChoiceMarketTypeEnum.CITY.getKey()) {
            return success(cityMessage(LocationDataCacheService.getCitys(parentId)));
        }
        if (type == ChoiceMarketTypeEnum.DISTRICT.getKey()) {
            return success(districtMessage(LocationDataCacheService.getDistricts(parentId)));
        }
        if (type == ChoiceMarketTypeEnum.STREET.getKey()) {
            return success(streetMessage(LocationDataCacheService.getStreets(parentId)));
        }
        if (type == ChoiceMarketTypeEnum.MARKET.getKey()) {
            List<MarketEntry> marketEntries = dataAccessFactory.getMarketDataCacheService().getMarkets(parentId);
            JSONArray response = marketEntries.stream().map(marketEntry -> marketEntry.message(latitude, longitude)).collect(Collectors.toCollection(JSONArray::new));
            return success(response);
        }
        // 1001 -- 错误的商店信息
        return MarketErrorCodeEnum.ERROR_MARKET_INFORMATION.response();
    }

    @Override
    public JSONObject showMarkets() {
        // long passportId = getLongParameter("passportId", 0);
        double longitude = getDoubleParameter("longitude", 0.0);
        double latitude = getDoubleParameter("latitude", 0.0);

        List<MarketEntry> marketEntries = dataAccessFactory.getMarketDataCacheService().findRecentMarket(longitude, latitude);
        if (CommonUtils.isEmpty(marketEntries)) {
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response();
        }
        JSONArray response = marketEntries.stream().map(marketEntry -> marketEntry.message(latitude, longitude)).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    @Override
    public JSONObject searchMarkets() {
        String province = getUTF("province", null);
        String city = getUTF("city", null);
        String district = getUTF("district", null);
        String street = getUTF("street", null);
        long streetId = getIntParameter("streetId", 0);

        int type = getIntParameter("type", -1);
        int status = getIntParameter("status", -1);
        int deliveryMode = getIntParameter("deliveryMode", -1);

        MarketEntry searchModel = new MarketEntry();
        searchModel.setProvince(province);
        searchModel.setCity(city);
        searchModel.setDistrict(district);
        searchModel.setStreet(street);
        searchModel.setStreetId(streetId);
        searchModel.setType(type);
        searchModel.setStatus(status);
        searchModel.setDeliveryMode(deliveryMode);

        int pageSize = getPageSize();//必须分页
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<MarketEntry> marketEntries = dataAccessFactory.getMarketDataAccessManager().searchMarkets(searchModel, pageSize, pageStartIndex);
        int count = dataAccessFactory.getMarketDataAccessManager().searchMarketsCount(searchModel);

        JSONObject response = new JSONObject();
        response.put("data", marketEntries);
        response.put("count", count);

        return success(response);
    }

    public JSONObject getMarket() {
        long marketId = getLongParameter("id", 0);
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry == null) {
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response("找不到店铺记录，店铺ID：" + marketId);
        }
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(marketEntry));
        return success(response);
    }

    @Override
    public JSONObject marketResponse() {
        long passportId = getLongParameter("passportId");
        byte responseStatus = getByteParameter("responseStatus", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarketForPassport(passportId);
        if (marketEntry == null) {
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response("找不到商店，通行证ID：" + passportId);
        }
        if (responseStatus == GlobalAppointmentOptEnum.LOGIC_FALSE.getKey() && marketEntry.getOnlineStatus() == MarketOnlineStatusEnum.OFFLINE.getKey()) {
            return success();
        }
        if (responseStatus == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey() && marketEntry.getOnlineStatus() == MarketOnlineStatusEnum.ONLINE.getKey()) {
            return success();
        }
        int targetStatus = (responseStatus == GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()) ? MarketOnlineStatusEnum.OFFLINE.getKey() : MarketOnlineStatusEnum.ONLINE.getKey();
        int matchStatus = (responseStatus == GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()) ? MarketOnlineStatusEnum.ONLINE.getKey() : MarketOnlineStatusEnum.OFFLINE.getKey();

        dataAccessFactory.getMarketDataAccessManager().changeOnlineStatus(marketEntry.getId(), targetStatus, matchStatus);
        return success();
    }

    public JSONObject getAllMarkets() {
        List<MarketEntry> marketEntries = dataAccessFactory.getMarketDataCacheService().getMarketEntries();
        if (CommonUtils.isEmpty(marketEntries)) {
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response("系统不存在商店记录，请联系管理员！");
        }
        return success(marketEntries);
    }

    @Override
    public JSONObject myFocusMarkets() {
        long passportId = getLongParameter("passportId");
        int roleType = getIntParameter("roleType", ClientTypeEnum.WAREHOUSE.getKey());
        List<MarketRelationship> marketRelationships = dataAccessFactory.getMarketDataAccessManager().myFocusMarkets(String.valueOf(passportId),
                roleType == ClientTypeEnum.WAREHOUSE.getKey() ? MarketRelationshipTypeEnum.FOCUS.getKey() : MarketRelationshipTypeEnum.COURIER.getKey());
        if (CommonUtils.isEmpty(marketRelationships)) {
            return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("您的帐号未绑定任何商店，请联系管理员！");
        }
        JSONArray response = new JSONArray();
        for (MarketRelationship marketRelationship : marketRelationships) {
            MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketRelationship.getMarketId());
            if (marketEntry == null) {
                continue;
            }
            response.add(marketEntry.message(Double.parseDouble(marketEntry.getLocation().split(CommonUtils.SPLIT_COMMA)[1]), Double.parseDouble(marketEntry.getLocation().split(CommonUtils.SPLIT_COMMA)[0])));
        }
        return success(response);
    }

    @Override
    public JSONObject macRelationMarket() {
        String mac = getUTF("macAddress");

        List<MarketRelationship> marketRelationships = dataAccessFactory.getMarketDataAccessManager().myFocusMarkets(mac, MarketRelationshipTypeEnum.MAC.getKey());
        if (CommonUtils.isEmpty(marketRelationships)) {
            return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("该Mac地址未绑定任何商店，请联系管理员！");
        }
        MarketRelationship marketRelationship = marketRelationships.get(0);

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketRelationship.getMarketId());
        if (marketEntry == null) {
            return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("该Mac地址关联的商店不存在，错误码：" + marketRelationship.getMarketId() + "；请联系管理员！");
        }
        Passport passport = PassportRemoteService.getPassport(marketEntry.getPassportId());
        setAccessToken(passport.getAccessToken());

        JSONObject response = new JSONObject();
        response.put("marketId", marketEntry.getId());
        response.put("marketName", marketEntry.getName());
        response.put("marketPassportId", marketEntry.getPassportId());

        return success(response);
    }

    @Override
    public JSONObject marketEditSave() {
        long marketId = getLongParameter("marketId", 0);
        String marketName = getUTF("name");
        String provinceName = getUTF("province");
        String cityName = getUTF("city");
        String district = getUTF("district");
        long streetId = getLongParameter("streetId");
        String streetName = getUTF("streetName");
        String streetNumber = getUTF("streetNumber", "");
        String address = getUTF("address", "");
        String location = getUTF("location", "0");
        int deliveryMode = getIntParameter("deliveryMode", 0);
        int distance = getIntParameter("distance", 0);
        long deliveryCost = CommonUtils.changeMoney(getUTF("deliveryCost", "0"));
        long freeDeliveryCost = CommonUtils.changeMoney(getUTF("freeDeliveryCost", "0"));

        MarketEntry entry = new MarketEntry();
        entry.setId(marketId);
        entry.setName(marketName);
        entry.setProvince(provinceName);
        entry.setCity(cityName);
        entry.setDistrict(district);
        entry.setStreetId(streetId);
        entry.setStreet(streetName);
        entry.setStreetNumber(streetNumber);
        entry.setAddress(address);
        entry.setLocation(location);
        entry.setDeliveryMode(deliveryMode);
        entry.setDistance(distance);
        entry.setDeliveryCost(deliveryCost);
        entry.setFreeDeliveryFee(freeDeliveryCost);

        if (marketId == 0) {

            //如果是新增
            entry.setType(0);//默认是自动化的类型店铺
            entry.setStatus(2);//默认是关店状态

            if (dataAccessFactory.getMarketDataAccessManager().createMarket(entry) > 0) {
                return success("添加成功");
            } else {
                return fail("添加失败");
            }
        } else {
            // 修改
            if (dataAccessFactory.getMarketDataAccessManager().updateMarket(entry) > 0) {
                return success("修改成功");
            } else {
                return fail("修改失败");
            }
        }
    }

    @Override
    public JSONObject marketUpdateStatus() {
        long marketId = getLongParameter("marketId");
        int status = getIntParameter("status");
        int beforeStatus = getIntParameter("beforeStatus");

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry == null) {
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response();
        }
        int result = dataAccessFactory.getMarketDataAccessManager().marketResponse(marketId, status, beforeStatus);
        if (result > 0) {
            JSONObject response = new JSONObject();
            response.put("status", status);
            return success(response);
        }
        return fail("修改商店状态失败");
    }


    private JSONArray provinceMessage(List<PassportProvince> provinces) {
        JSONArray response = new JSONArray();
        for (PassportProvince province : provinces) {
            JSONObject data = new JSONObject();

            data.put("id", province.getId());
            data.put("name", province.getName());

            response.add(data);
        }
        return response;
    }

    private JSONArray cityMessage(List<PassportCity> cities) {
        JSONArray response = new JSONArray();
        for (PassportCity city : cities) {
            JSONObject data = new JSONObject();

            data.put("id", city.getId());
            data.put("provinceId", city.getProvinceId());
            data.put("name", city.getName());

            response.add(data);
        }
        return response;
    }

    private JSONArray districtMessage(List<PassportArea> districts) {
        JSONArray response = new JSONArray();
        for (PassportArea district : districts) {
            JSONObject data = new JSONObject();

            data.put("id", district.getId());
            data.put("cityId", district.getCityId());
            data.put("name", district.getName());

            response.add(data);
        }
        return response;
    }

    private JSONArray streetMessage(List<PassportStreet> streets) {
        JSONArray response = new JSONArray();
        for (PassportStreet street : streets) {
            JSONObject data = new JSONObject();

            data.put("id", street.getId());
            data.put("areaId", street.getAreaId());
            data.put("name", street.getName());

            response.add(data);
        }
        return response;
    }
}