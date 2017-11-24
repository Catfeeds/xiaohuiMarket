package com.xlibao.saas.market.service.item.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.item.ItemRequestSourceEnum;
import com.xlibao.common.constant.item.ItemStatusEnum;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.exception.code.ItemErrorCodeEnum;
import com.xlibao.common.lbs.baidu.AddressComponent;
import com.xlibao.common.lbs.baidu.BaiduWebAPI;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.market.data.model.*;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.item.ItemType;
import com.xlibao.metadata.item.ItemUnit;
import com.xlibao.metadata.order.OrderItemSnapshot;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.data.model.MarketAccessLogger;
import com.xlibao.saas.market.service.XMarketTimeConfig;
import com.xlibao.saas.market.service.activity.RecommendItemTypeEnum;
import com.xlibao.saas.market.service.item.*;
import com.xlibao.saas.market.service.market.MarketErrorCodeEnum;
import com.xlibao.saas.market.service.market.MarketStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("itemService")
public class ItemServiceImpl extends BasicWebService implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;

    @Override
    public JSONObject homepage() {
        long passportId = getLongParameter("passportId", 0);
        long marketId = getLongParameter("marketId", 0);

        double longitude = getDoubleParameter("longitude", 0.0);
        double latitude = getDoubleParameter("latitude", 0.0);
        // 匹配可用的商店ID
        marketId = matchMarketId(passportId, marketId);

        // Banner
        JSONArray bannerMsg = bannerMsg(marketId, latitude, longitude);
        // SpecialButton
        JSONArray specialButtonMsg = specialButtonMsg();
        // RecommendItemTypes
        JSONArray recommendItemTypeMsg = recommendItemTypeMsg(marketId, latitude, longitude);

        JSONObject response = new JSONObject();
        response.put("bannerMsg", bannerMsg);
        response.put("specialButtonMsg", specialButtonMsg);
        response.put("recommendItemTypeMsg", recommendItemTypeMsg);
        return success(response);
    }

    @Override
    public JSONObject itemTypes() {
        int requestSource = getIntParameter("requestSource", ItemRequestSourceEnum.ON_LINE.getKey());

        JSONArray response = fillItemTypeMsg(ItemDataCacheService.getItemTypes(), requestSource, false);

        return success(response);
    }

    @Override
    public JSONObject findSubItemTypes() {
        long itemTypeId = getLongParameter("itemTypeId");
        int requestSource = getIntParameter("requestSource", ItemRequestSourceEnum.ON_LINE.getKey());

        ItemType itemType = ItemDataCacheService.getItemType(itemTypeId);
        if (itemType == null) {
            return MarketItemErrorCodeEnum.ITEM_TYPE_ERROR.response();
        }
        List<ItemType> itemTypes = ItemDataCacheService.relationItemTypes(itemTypeId);
        JSONArray response = fillItemTypeMsg(itemTypes, requestSource, true);
        return success(response);
    }

    @Override
    public JSONObject findRecommendItems() {
        long passportId = getLongParameter("passportId", 0);
        long marketId = getLongParameter("marketId", 0);

        double longitude = getDoubleParameter("longitude", 0.0);
        double latitude = getDoubleParameter("latitude", 0.0);

        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        marketId = matchMarketId(passportId, marketId);
        if (marketId == 0) {
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response();
        }
        JSONObject recommendItemMsg = recommendItemMsg(passportId, marketId, latitude, longitude, pageStartIndex, pageSize);
        return success(recommendItemMsg);
    }

    @Override
    public JSONObject pageItems() {
        long passportId = getLongParameter("passportId", 0);
        long marketId = getLongParameter("marketId");

        long itemTypeId = getLongParameter("itemTypeId", 0);
        int sortType = getIntParameter("sortType", ItemSortEnum.DEFAULT.getKey());
        int sortValue = getIntParameter("sortValue", 0);
        String searchKeyValue = getUTF("searchKeyValue", null);

        int requestSource = getIntParameter("requestSource", ItemRequestSourceEnum.ON_LINE.getKey());

        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() != MarketStatusEnum.NORMAL.getKey()) {
            return MarketErrorCodeEnum.MARKET_STATUS_ERROR.response("商店正在升级维护中，请稍后再来！");
        }
        List<MarketItem> items;
        if (CommonUtils.isNotNullString(searchKeyValue)) {
            items = conditionPageItems(marketId, ItemDataCacheService.fuzzyQueryItemTemplates(searchKeyValue), sortType, sortValue, requestSource, pageStartIndex, pageSize);
            if (!CommonUtils.isEmpty(items)) {
                // 记录搜索次数
                incrementSearchTimes(marketId, searchKeyValue);
            }
        } else {
            ItemSpecialTypeEnum itemSpecialTypeEnum = ItemSpecialTypeEnum.getItemSpecialTypeEnum(itemTypeId);
            if (itemSpecialTypeEnum != null) {
                items = specialPageItems(itemSpecialTypeEnum, marketId, sortType, sortValue, requestSource, pageStartIndex, pageSize);
            } else {
                items = conditionPageItems(marketId, ItemDataCacheService.appointItemType(itemTypeId), sortType, sortValue, requestSource, pageStartIndex, pageSize);
            }
        }
        if (CommonUtils.isEmpty(items)) {
            // 没有更多数据
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        Map<Long, List<MarketItemLadderPrice>> itemLadderPriceMap = loadItemLadderPrices(items);

        JSONObject response = new JSONObject();

        response.put("buyMessage", fillBuyMessage(passportId, items));
        response.put("pageItemMsg", fillPageItemMessage(items, itemLadderPriceMap));
        return success(response);
    }

    @Override
    public JSONObject splitItems() {
        String items = getUTF("items");
        List<MarketItem> itemList = dataAccessFactory.getItemDataAccessManager().getItems(items);

        Map<Long, JSONArray> groupItems = new HashMap<>();

        for (MarketItem item : itemList) {
            JSONArray itemArray = groupItems.get(item.getOwnerId());
            if (itemArray == null) {
                itemArray = new JSONArray();
                groupItems.put(item.getOwnerId(), itemArray);
            }
            itemArray.add(item.getId());
        }
        JSONArray marketItems = new JSONArray();
        for (Map.Entry<Long, JSONArray> entry : groupItems.entrySet()) {
            MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(entry.getKey());

            JSONObject marketData = new JSONObject();
            marketData.put("marketId", marketEntry.getId());
            marketData.put("marketName", marketEntry.getName());
            marketData.put("marketImage", marketEntry.getImage());
            marketData.put("items", entry.getValue());

            marketItems.add(marketData);
        }
        return success(marketItems);
    }

    @Override
    public OrderItemSnapshot fillOrderItemSnapshot(MarketItem item, MarketItemDailyPurchaseLogger itemDailyPurchaseLogger, int buyCount, List<MarketItemLadderPrice> itemLadderPrices) {
        OrderItemSnapshot orderItemSnapshot = fillBaseOrderItemSnapshot(item);

        MarketItemLadderPrice optimalItemLadderPrice = findOptimalItemLadderPrice(itemLadderPrices, buyCount);

        int normalQuantity = buyCount;
        int discountQuantity = 0;
        long normalPrice = item.getSellPrice();
        long discountPrice = normalPrice;

        if (optimalItemLadderPrice != null) { // 存在阶梯价时
            normalQuantity = 0;
            discountQuantity = buyCount;
            discountPrice = optimalItemLadderPrice.getExpectPrice();
        }
        if (item.getDiscountType() != DiscountTypeEnum.NORMAL.getKey()) {
            normalPrice = item.maxPrice(); // 以高价出售
            if (item.getRestrictionQuantity() == RestrictionQuantityEnum.UN_LIMIT.getKey()) {
                // 不限购
                normalQuantity = 0;
                discountQuantity = buyCount;
            } else {
                int remainDiscountCount = item.getRestrictionQuantity() - (itemDailyPurchaseLogger == null ? 0 : itemDailyPurchaseLogger.getHasBuyCount());
                if (remainDiscountCount < 0) {
                    remainDiscountCount = 0;
                }
                normalQuantity = buyCount - remainDiscountCount;
                if (normalQuantity < 0) {
                    normalQuantity = 0;
                }
                discountQuantity = buyCount - normalQuantity;
                if (discountQuantity < 0) {
                    discountQuantity = 0;
                }
            }
            // 优惠价始终已最低价存在
            discountPrice = discountPrice > item.getDiscountPrice() ? item.getDiscountPrice() : discountPrice;
        }
        long totalPrice = normalQuantity * normalPrice + discountQuantity * discountPrice;

        orderItemSnapshot.setNormalQuantity(normalQuantity);
        orderItemSnapshot.setDiscountQuantity(discountQuantity);
        orderItemSnapshot.setNormalPrice(normalPrice);
        orderItemSnapshot.setDiscountPrice(discountPrice);
        orderItemSnapshot.setTotalPrice(totalPrice);
        return orderItemSnapshot;
    }

    @Override
    public Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap(List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers) {
        Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap = new HashMap<>();
        if (!CommonUtils.isEmpty(itemDailyPurchaseLoggers)) {
            for (MarketItemDailyPurchaseLogger roleDailyBuyLogger : itemDailyPurchaseLoggers) {
                itemDailyPurchaseLoggerMap.put(roleDailyBuyLogger.getItemId(), roleDailyBuyLogger);
            }
        }
        return itemDailyPurchaseLoggerMap;
    }

    @Override
    public void buyQualifications(List<MarketItem> items, List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers, JSONObject buyItems) {
        Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap = itemDailyPurchaseLoggerMap(itemDailyPurchaseLoggers);
        for (MarketItem item : items) {
            ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(item.getItemTemplateId());
            ItemUnit itemUnit = ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());
            if (item.getStatus() != MarketItemStatusEnum.NORMAL.getKey()) {
                logger.error("用户购买【" + itemTemplate.getName() + "】，目前处于下架状态(状态值：" + item.getStatus() + ")，暂不通过！");
                throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.ITEM_STATUS_ERROR_OFF_LINE.getKey(), "抱歉，您购买的【" + itemTemplate.getName() + "】已下架！");
            }
            if (item.getRestrictionQuantity() == RestrictionQuantityEnum.UN_SELL.getKey()) {
                logger.error("用户购买【" + itemTemplate.getName() + "】，目前处于不出售状态，暂不通过！");
                throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.ITEM_UN_SELL.getKey(), "抱歉，您购买的【" + itemTemplate.getName() + "】已下架！");
            }
            // 本次购买的数量
            int buyCount = buyItems.getIntValue(String.valueOf(item.getItemTemplateId()));
            if (buyCount < item.getMinimumSellCount()) {
                throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.LESS_THAN_MINIMUM_SELL.getKey(), itemTemplate.getName() + "最低购买数量：" + item.getMinimumSellCount() + itemUnit.getTitle());
            }
            if (item.getMaximumSellCount() != RestrictionQuantityEnum.UN_LIMIT.getKey() && buyCount > item.getMaximumSellCount()) {
                throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.GREATER_THAN_MAXIMUM_SELL.getKey(), itemTemplate.getName() + "最多购买数量：" + item.getMaximumSellCount() + itemUnit.getTitle());
            }
            if (item.getRestrictionQuantity() == RestrictionQuantityEnum.UN_LIMIT.getKey()) { // 不限购
                if (buyCount > item.getShowStock()) { // 超出库存
                    throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.ITEM_STOCK_NOT_ENOUGH.getKey(), "您购买的【" + itemTemplate.getName() + "】库存不足，本次最多可购买：" + item.getShowStock() + itemUnit.getTitle());
                }
                continue;
            }
            // 限购时获取今天的购买量
            MarketItemDailyPurchaseLogger itemDailyPurchaseLogger = itemDailyPurchaseLoggerMap.get(item.getId());
            int hasBuyCount = itemDailyPurchaseLogger == null ? 0 : itemDailyPurchaseLogger.getHasBuyCount();
            if ((buyCount + hasBuyCount) > item.getRestrictionQuantity()) { // 超过限购量
                if (item.getBeyondControl() == BeyondControllTypeEnum.CAN_NOT_BEYOND.getKey()) {
                    throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.BUY_BEYOND_CONTROL.getKey(), "您购买的【" + itemTemplate.getName() + "】超出限购数量" +
                            "（限购" + item.getRestrictionQuantity() + itemUnit.getTitle() + "，已购买" + hasBuyCount + itemUnit.getTitle() + "，本次购买" + buyCount + itemUnit.getTitle() + "）");
                }
            }
        }
    }

    @Override
    public JSONObject existItemTemplate() {
        long marketId = getLongParameter("marketId");
        long itemTemplateId = getLongParameter("itemTemplateId");

        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplateId);
        return item == null ? MarketItemErrorCodeEnum.NOT_FOUND_ITEM.response() : success();
    }

    @Override
    public JSONObject editItem() {
        // 编辑商品
        long passportId = getLongParameter("passportId");
        long marketId = getLongParameter("marketId");
        long itemTemplateId = getLongParameter("itemTemplateId");
        long costPrice = getLongParameter("costPrice");
        long sellPrice = getLongParameter("sellPrice");
        long marketPrice = getLongParameter("marketPrice", sellPrice);
        String description = getUTF("description", "");
        int status = getIntParameter("status", MarketItemStatusEnum.OFF_SALE.getKey());

        if (costPrice < 0) {
            return fail(3, "成本价不能小于0分");
        }
        if (sellPrice < 0) {
            return fail(4, "售价不能小于0分");
        }
        if (marketPrice < 0) {
            return fail(5, "市场售价不能小于0分");
        }
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry == null) {
            logger.error("1 -- 用户[" + passportId + "]正在编辑商品[" + itemTemplateId + "]，但用户不存在供应商仓库记录");
            return MarketErrorCodeEnum.CAN_NOT_FIND_MARKET.response("找不到商店，错误码：" + marketId);
        }
        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(itemTemplateId);
        if (itemTemplate == null) {
            logger.error("2 -- 用户[" + passportId + "]正在编辑商品[" + itemTemplateId + "]，但无法获取商品模版记录");
            return ItemErrorCodeEnum.NOT_FOUND_ITEM_TEMPLATE.response("商品模版不存在，错误码：" + itemTemplateId);
        }
        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplateId);
        if (status == MarketItemStatusEnum.HIDE.getKey()) {
            status = MarketItemStatusEnum.OFF_SALE.getKey();
        }
        int result;
        if (item == null) {
            // 未有商品存在
            item = MarketItem.newInstance(marketId, itemTemplate, costPrice, sellPrice, marketPrice, sellPrice, description, (byte) status);
            result = dataAccessFactory.getItemDataAccessManager().createItem(item);
        } else {
            result = dataAccessFactory.getItemDataAccessManager().updateItem(item.getId(), costPrice, item.getSellPrice(), item.getMarketPrice(), item.getDiscountPrice(), (byte) status, description);
        }
        return result <= 0 ? fail("编辑商品失败") : success("编辑商品成功");
    }

    @Override
    public JSONObject findItemLocation() {
        long marketId = getLongParameter("marketId");
        long itemTemplateId = getLongParameter("itemTemplateId");

        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(itemTemplateId);
        if (itemTemplate == null) {
            return ItemErrorCodeEnum.NOT_FOUND_ITEM_TEMPLATE.response("商品模版不存在，错误码：" + itemTemplateId);
        }

        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplateId);
        if (item == null) {
            return MarketItemErrorCodeEnum.NOT_FOUND_ITEM.response("商店未收录该商品【" + itemTemplate.getName() + "】");
        }
        List<MarketItemLocation> itemLocations = dataAccessFactory.getItemDataAccessManager().getItemLocations(item.getId());
        if (CommonUtils.isEmpty(itemLocations)) {
            return MarketItemErrorCodeEnum.ITEM_LOCATION_ERROR.response("找不到商品【" + itemTemplate.getName() + "】的位置信息，请确认商品是否已上架！");
        }
        JSONArray response = new JSONArray();

        for (MarketItemLocation itemLocation : itemLocations) {
            JSONObject data = new JSONObject();

            data.put("locationCode", itemLocation.getLocationCode());
            data.put("stock", itemLocation.getStock());

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject loaderHotSearch() {
        long marketId = getLongParameter("marketId");
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        List<String> searchHistories = dataAccessFactory.getItemDataAccessManager().loaderHotSearch(marketId, pageStartIndex, pageSize);
        if (CommonUtils.isEmpty(searchHistories)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        return success(searchHistories);
    }

    @Override
    public JSONObject fuzzyMatchItemName() {
        long marketId = getLongParameter("marketId");
        String itemName = getUTF("fuzzyItemName");
        int requestSource = getIntParameter("requestSource", ItemRequestSourceEnum.ON_LINE.getKey());

        List<ItemTemplate> itemTemplates = ItemDataCacheService.fuzzyQueryItemTemplates(itemName);
        if (CommonUtils.isEmpty(itemTemplates)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        String itemTemplateSet = ItemDataCacheService.assembleItemTemplateSet(itemTemplates);

        List<Long> itemTemplateIdSet = dataAccessFactory.getItemDataAccessManager().existItemTemplates(marketId, itemTemplateSet, requestSource);

        JSONArray itemNameSet = new JSONArray();
        for (ItemTemplate itemTemplate : itemTemplates) {
            if (!itemTemplateIdSet.contains(itemTemplate.getId())) {
                continue;
            }
            itemNameSet.add(itemTemplate.getName());
        }
        if (itemNameSet.size() <= 0) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        return success(itemNameSet);
    }

    @Override
    public JSONObject itemLadderPrices() {
        long itemId = getLongParameter("itemId");

        List<MarketItemLadderPrice> itemLadderPrices = dataAccessFactory.getItemDataAccessManager().loadItemLadderPrices(String.valueOf(itemId));

        MarketItem item = dataAccessFactory.getItemDataAccessManager().getMarketItem(itemId);
        if (item == null) {
            return MarketItemErrorCodeEnum.NOT_FOUND_ITEM.response();
        }

        JSONArray response = new JSONArray();
        for (MarketItemLadderPrice itemLadderPrice : itemLadderPrices) {
            JSONObject data = new JSONObject();
            data.put("id", itemLadderPrice.getId());
            data.put("minQuantity", itemLadderPrice.getMinQuantity());
            data.put("maxQuantity", itemLadderPrice.getMaxQuantity());
            data.put("expectPrice", itemLadderPrice.getExpectPrice());
            data.put("status", itemLadderPrice.getStatus());
            data.put("mark", itemLadderPrice.getMark());

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject createItemLadderPrice() {
        long itemId = getLongParameter("itemId");
        int minQuantity = getIntParameter("minQuantity");
        int maxQuantity = getIntParameter("maxQuantity");
        long expectPrice = getLongParameter("expectPrice", 1);
        String mark = getUTF("mark");

        MarketItem item = dataAccessFactory.getItemDataAccessManager().getMarketItem(itemId);
        if (item == null) {
            return MarketItemErrorCodeEnum.NOT_FOUND_ITEM.response();
        }
        List<MarketItemLadderPrice> itemLadderPrices = dataAccessFactory.getItemDataAccessManager().loadItemLadderPrices(String.valueOf(itemId));
        if (!CommonUtils.isEmpty(itemLadderPrices)) {
            return MarketItemErrorCodeEnum.EXIST_ITEM_LADDER_PRICE.response();
        }
        MarketItemLadderPrice itemLadderPrice = new MarketItemLadderPrice();
        itemLadderPrice.setItemId(itemId);
        itemLadderPrice.setMinQuantity(minQuantity);
        itemLadderPrice.setMaxQuantity(maxQuantity);
        itemLadderPrice.setExpectPrice(expectPrice);
        itemLadderPrice.setStatus((int) GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
        itemLadderPrice.setMark(mark);

        int result = dataAccessFactory.getItemDataAccessManager().createItemLadderPrice(itemLadderPrice);
        return result <= 0 ? PlatformErrorCodeEnum.DB_ERROR.response() : success();
    }

    @Override
    public JSONObject removeItemLadderPrice() {
        long itemId = getLongParameter("itemId");
        long itemLadderId = getLongParameter("itemLadderId");

        int result = dataAccessFactory.getItemDataAccessManager().removeItemLadderPrice(itemId, itemLadderId);
        return result <= 0 ? PlatformErrorCodeEnum.DB_ERROR.response() : success();
    }

    @Override
    public Map<Long, List<MarketItemLadderPrice>> loadItemLadderPrices(List<MarketItem> items) {
        String itemSet = processItem(items);
        if (CommonUtils.isNullString(itemSet)) {
            return Collections.emptyMap();
        }
        List<MarketItemLadderPrice> itemLadderPrices = dataAccessFactory.getItemDataAccessManager().loadItemLadderPrices(itemSet);

        Map<Long, List<MarketItemLadderPrice>> itemLadderPriceMap = new HashMap<>();
        for (MarketItemLadderPrice itemLadderPrice : itemLadderPrices) {
            List<MarketItemLadderPrice> ladderPrices = itemLadderPriceMap.get(itemLadderPrice.getItemId());
            if (ladderPrices == null) {
                ladderPrices = new ArrayList<>();
                itemLadderPriceMap.put(itemLadderPrice.getItemId(), ladderPrices);
            }
            ladderPrices.add(itemLadderPrice);
        }
        return itemLadderPriceMap;
    }

    private MarketItemLadderPrice findOptimalItemLadderPrice(List<MarketItemLadderPrice> itemLadderPrices, int quantity) {
        if (CommonUtils.isEmpty(itemLadderPrices)) {
            return null;
        }
        for (MarketItemLadderPrice itemLadderPrice : itemLadderPrices) {
            if (quantity >= itemLadderPrice.getMinQuantity()) {
                return itemLadderPrice;
            }
        }
        return null;
    }

    private long matchMarketId(long passportId, long marketId) {
        if (marketId == 0) { // 若没有指定商店 那么寻找上次访问的商店
            MarketAccessLogger accessLogger = dataAccessFactory.getMarketDataAccessManager().getLastAccessLogger(passportId);
            if (accessLogger != null) {
                marketId = accessLogger.getMarketId();
            }
        }
        return marketId;
    }

    private JSONArray specialButtonMsg() {
        List<MarketSpecialButton> buttons = dataAccessFactory.getMarketItemDataCacheService().getButtons();
        if (CommonUtils.isEmpty(buttons)) {
            return new JSONArray();
        }
        JSONArray response = new JSONArray();
        for (MarketSpecialButton button : buttons) {
            JSONObject data = new JSONObject();
            data.put("id", button.getId());
            data.put("name", button.getName());
            data.put("icon", button.getImage());
            data.put("type", button.getType());
            data.put("jumpUrl", button.getJumpUrl());

            response.add(data);
        }
        return response;
    }

    private JSONArray bannerMsg(long marketId, double latitude, double longitude) {
        List<MarketBanner> banners = dataAccessFactory.getActivityDataAccessManager().getBannerByMarket(marketId);
        logger.info("通过商店ID(" + marketId + ")获取Banner数量为：" + (banners == null ? 0 : banners.size()));
        if (CommonUtils.isEmpty(banners)) {
            logger.error("无法通过商店ID(" + marketId + ")获取Banner记录，尝试通过定位方式来获取，经纬度为：" + longitude + "," + latitude);
            AddressComponent addressComponent = BaiduWebAPI.geocoding(latitude, longitude);
            logger.info("通过经纬度(" + longitude + "," + latitude + ")获取地址信息为：" + addressComponent);
            if (addressComponent != null) {
                String adcode = addressComponent.getAdcode();
                banners = dataAccessFactory.getActivityDataAccessManager().getAdcodeDefaultBanners(adcode);
                logger.info("通过地址信息(" + addressComponent + ")获取Banner数量为：" + (banners == null ? 0 : banners.size()));
            }
        }
        if (CommonUtils.isEmpty(banners)) { // 直接获取系统的默认banner
            logger.error("通过经纬度(" + longitude + "," + latitude + ")信息无法获取到Banner记录，尝试获取默认的Banner记录");
            banners = dataAccessFactory.getActivityDataAccessManager().getDefaultBanners();
            if (CommonUtils.isEmpty(banners)) {
                logger.info("无法获取默认的Banner记录");
            }
        }
        JSONArray response = new JSONArray();
        if (!CommonUtils.isEmpty(banners)) {
            for (MarketBanner banner : banners) {
                JSONObject bannerMsg = new JSONObject();

                bannerMsg.put("id", banner.getId());
                bannerMsg.put("imageURL", banner.getImageUrl());
                bannerMsg.put("type", banner.getType());
                bannerMsg.put("clickURL", banner.getClickUrl());

                response.add(bannerMsg);
            }
        }
        return response;
    }

    private JSONArray recommendItemTypeMsg(long marketId, double latitude, double longitude) {
        List<MarketRecommendItem> recommendItems = findRecommendItems(marketId, RecommendItemTypeEnum.ITEM_TYPE, longitude, latitude);
        if (CommonUtils.isEmpty(recommendItems)) {
            return new JSONArray();
        }
        JSONArray response = new JSONArray();
        for (MarketRecommendItem recommendItem : recommendItems) {
            ItemType itemType = ItemDataCacheService.getItemType(recommendItem.getEntryId());
            if (itemType == null) {
                continue;
            }
            response.add(fillItemTypeMsg(itemType, true));
        }
        return response;
    }

    private JSONObject recommendItemMsg(long passportId, long marketId, double latitude, double longitude, int pageStartIndex, int pageSize) {
        List<MarketRecommendItem> recommendItems = findRecommendItems(marketId, RecommendItemTypeEnum.ITEM, longitude, latitude);

        JSONObject response = new JSONObject();
        if (marketId == 0) { // 仅为保护，实际作用不大
            return response;
        }
        String itemTemplates = null;
        if (!CommonUtils.isEmpty(recommendItems)) {
            StringBuilder itemTemplateSet = new StringBuilder();
            for (MarketRecommendItem recommendItem : recommendItems) {
                itemTemplateSet.append(recommendItem.getEntryId()).append(CommonUtils.SPLIT_COMMA);
            }
            itemTemplateSet.deleteCharAt(itemTemplateSet.length() - 1);

            itemTemplates = itemTemplateSet.toString();
        }
        List<MarketItem> items = dataAccessFactory.getItemDataAccessManager().getItemsForItemTemplateSet(marketId, itemTemplates, pageStartIndex, pageSize);

        Map<Long, List<MarketItemLadderPrice>> itemLadderPriceMap = loadItemLadderPrices(items);

        response.put("buyMessage", fillBuyMessage(passportId, items));
        response.put("pageItemMsg", fillPageItemMessage(items, itemLadderPriceMap));
        return response;
    }

    private List<MarketRecommendItem> findRecommendItems(long marketId, RecommendItemTypeEnum recommendItemTypeEnum, double longitude, double latitude) {
        List<MarketRecommendItem> recommendItems = marketId == 0 ? null : dataAccessFactory.getActivityDataAccessManager().getRecommendItemsByMarket(marketId, recommendItemTypeEnum.getKey());
        logger.info("通过商店ID(" + marketId + ")获取推荐" + recommendItemTypeEnum.getValue() + "数量为：" + (recommendItems == null ? 0 : recommendItems.size()));

        if (CommonUtils.isEmpty(recommendItems)) {
            logger.error("无法通过商店ID(" + marketId + ")获取推荐" + recommendItemTypeEnum.getValue() + "记录，尝试通过定位方式来获取，经纬度为：" + longitude + "," + latitude);
            AddressComponent addressComponent = BaiduWebAPI.geocoding(latitude, longitude);
            logger.info("通过经纬度(" + longitude + "," + latitude + ")获取地址信息为：" + addressComponent);
            if (addressComponent != null) {
                String adcode = addressComponent.getAdcode();
                recommendItems = dataAccessFactory.getActivityDataAccessManager().getAdcodeDefaultRecommendItems(adcode, recommendItemTypeEnum.getKey());
                logger.info("通过地址信息(" + addressComponent + ")获取推荐" + recommendItemTypeEnum.getValue() + "数量为：" + (recommendItems == null ? 0 : recommendItems.size()));
            }
        }
        if (CommonUtils.isEmpty(recommendItems)) { // 直接获取系统的默认推荐商品或分类
            logger.error("通过经纬度(" + longitude + "," + latitude + ")信息无法获取推荐" + recommendItemTypeEnum.getValue() + "记录，尝试获取默认的Banner记录");
            recommendItems = dataAccessFactory.getActivityDataAccessManager().getDefaultRecommendItems(recommendItemTypeEnum.getKey());
            if (CommonUtils.isEmpty(recommendItems)) {
                logger.info("无法获取默认的推荐" + recommendItemTypeEnum.getValue() + "记录");
            }
        }
        return recommendItems;
    }

    private JSONArray fillItemTypeMsg(List<ItemType> itemTypes, int requestSource, boolean fillImage) {
        if (CommonUtils.isEmpty(itemTypes)) {
            return new JSONArray();
        }
        JSONArray response = new JSONArray();
        for (ItemType itemType : itemTypes) {
            if (itemType.getStatus() != ItemStatusEnum.NORMAL.getKey()) {
                continue;
            }
            if ((itemType.getRequestSource() & requestSource) != requestSource) {
                continue;
            }
            List<ItemType> subItemTypes = ItemDataCacheService.relationItemTypes(itemType.getId());
            if (CommonUtils.isEmpty(subItemTypes)) {
                continue;
            }
            JSONObject data = fillItemTypeMsg(itemType, fillImage);

            JSONArray subItemTypeMsg = subItemTypes.stream().map(it -> fillItemTypeMsg(it, fillImage)).collect(Collectors.toCollection(JSONArray::new));
            data.put("subItemTypes", subItemTypeMsg);
            response.add(data);
        }
        return response;
    }

    private JSONObject fillItemTypeMsg(ItemType itemType, boolean fillImage) {
        JSONObject response = new JSONObject();

        response.put("id", itemType.getId());
        response.put("name", itemType.getTitle());
        response.put("icon", itemType.getIcon());
        response.put("image", itemType.getImage());
        response.put("fillImage", fillImage ? GlobalAppointmentOptEnum.LOGIC_FALSE.getKey() : GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());

        return response;
    }

    private List<MarketItem> conditionPageItems(long marketId, List<ItemTemplate> itemTemplates, int sortType, int sortValue, int requestSource, int pageStartIndex, int pageSize) {
        if (CommonUtils.isEmpty(itemTemplates)) {
            return null;
        }
        // 拼装商品集合
        String itemTemplateSet = ItemDataCacheService.assembleItemTemplateSet(itemTemplates);
        // 按前端指定的排序条件查找对应的商品列表
        return dataAccessFactory.getItemDataAccessManager().conditionOrdering(marketId, itemTemplateSet, sortType, sortValue, requestSource, pageStartIndex, pageSize);
    }

    private List<MarketItem> specialPageItems(ItemSpecialTypeEnum itemSpecialTypeEnum, long marketId, int sortType, int sortValue, int requestSource, int pageStartIndex, int pageSize) {
        return dataAccessFactory.getItemDataAccessManager().specialProducts(marketId, itemSpecialTypeEnum.getKey(), XMarketTimeConfig.ITEM_NEW_TIME, sortType, sortValue, requestSource, pageStartIndex, pageSize);
    }

    private OrderItemSnapshot fillBaseOrderItemSnapshot(MarketItem item) {
        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(item.getItemTemplateId());
        ItemType itemType = ItemDataCacheService.getItemType(itemTemplate.getTypeId());
        ItemUnit itemUnit = ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());

        OrderItemSnapshot orderItemSnapshot = new OrderItemSnapshot();

        orderItemSnapshot.setItemId(item.getId());
        orderItemSnapshot.setItemTemplateId(itemTemplate.getId());
        orderItemSnapshot.setItemName(itemTemplate.getName());
        orderItemSnapshot.setItemTypeId(itemType.getId());
        orderItemSnapshot.setItemTypeName(itemType.getTitle());
        orderItemSnapshot.setItemUnitId(itemUnit.getId());
        orderItemSnapshot.setItemUnitName(itemUnit.getTitle());
        orderItemSnapshot.setItemBarcode(itemTemplate.getBarcode());
        orderItemSnapshot.setItemCode(itemTemplate.getDefineCode());
        orderItemSnapshot.setItemBatches(item.getProductBatches());
        orderItemSnapshot.setIntroductionPage(itemTemplate.getIntroductionPage());
        orderItemSnapshot.setCostPrice(item.getCostPrice());
        orderItemSnapshot.setMarketPrice(item.getMarketPrice());

        return orderItemSnapshot;
    }

    private JSONObject fillBuyMessage(long passportId, List<MarketItem> items) {
        if (CommonUtils.isEmpty(items) || passportId <= 0) {
            return new JSONObject();
        }
        String itemTemplateSet = itemTemplateSet(items);
        // 获取用户当天的购买记录
        List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers = dataAccessFactory.getItemDataAccessManager().passportDailyBuyLoggers(passportId, itemTemplateSet);
        Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap = itemDailyPurchaseLoggerMap(itemDailyPurchaseLoggers);
        JSONObject response = new JSONObject();
        for (MarketItem item : items) {
            MarketItemDailyPurchaseLogger roleDailyBuyLogger = itemDailyPurchaseLoggerMap.get(item.getId());
            ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(item.getItemTemplateId());

            ItemUnit itemUnit = ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());

            JSONObject dailyData = new JSONObject();
            dailyData.put("showDiscount", showDiscount(item, itemUnit));
            dailyData.put("showHasBuy", "您已购买" + (roleDailyBuyLogger == null ? 0 : roleDailyBuyLogger.getHasBuyCount()) + itemUnit.getTitle());
            dailyData.put("hasBuy", roleDailyBuyLogger == null ? 0 : roleDailyBuyLogger.getHasBuyCount());
            dailyData.put("beyondControl", item.getBeyondControl());

            response.put(String.valueOf(item.getId()), dailyData);
        }
        return response;
    }

    private JSONArray fillPageItemMessage(List<MarketItem> items, Map<Long, List<MarketItemLadderPrice>> itemLadderPriceMap) {
        JSONArray itemArray = new JSONArray();
        for (MarketItem item : items) {
            ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(item.getItemTemplateId());
            if (itemTemplate == null) {
                continue;
            }
            ItemUnit itemUnit = ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());

            JSONObject itemMsg = new JSONObject();

            itemMsg.put("itemId", item.getId());
            itemMsg.put("itemTemplateId", item.getItemTemplateId());
            itemMsg.put("name", itemTemplate.getName());
            itemMsg.put("imageUrl", itemTemplate.getImageUrl());
            itemMsg.put("unitName", itemUnit == null ? "" : itemUnit.getTitle());
            itemMsg.put("stock", item.getShowStock());
            itemMsg.put("maximumSellCount", item.getMaximumSellCount());
            itemMsg.put("minimumSellCount", item.getMinimumSellCount());
            itemMsg.put("barcode", itemTemplate.getBarcode());
            itemMsg.put("batchesCode", CommonUtils.nullToEmpty(item.getBatchesCode()));
            itemMsg.put("sellPrice", item.getSellPrice());
            itemMsg.put("maxPrice", item.maxPrice());
            itemMsg.put("discountType", item.getDiscountType());
            itemMsg.put("discountPrice", (item.getDiscountType() == DiscountTypeEnum.NORMAL.getKey()) ? 0 : (item.getDiscountPrice() <= 0 ? item.getSellPrice() : item.getDiscountPrice()));
            itemMsg.put("restrictionQuantity", item.getRestrictionQuantity());
            itemMsg.put("actualSales", item.getActualSales());
            itemMsg.put("deliveryDelay", item.getDeliveryDelay());
            itemMsg.put("status", item.getStatus());

            itemMsg.put("itemLadderPrices", fillItemLadderPriceMsg(itemLadderPriceMap.get(item.getId()), itemUnit));

            itemArray.add(itemMsg);
        }
        return itemArray;
    }

    private JSONArray fillItemLadderPriceMsg(List<MarketItemLadderPrice> itemLadderPrices, ItemUnit itemUnit) {
        if (CommonUtils.isEmpty(itemLadderPrices)) {
            return new JSONArray();
        }
        JSONArray response = new JSONArray();

        for (MarketItemLadderPrice itemLadderPrice : itemLadderPrices) {
            JSONObject data = new JSONObject();
            data.put("minQuantity", itemLadderPrice.getMinQuantity());
            data.put("maxQuantity", itemLadderPrice.getMaxQuantity());
            data.put("expectPrice", itemLadderPrice.getExpectPrice());
            data.put("mark", itemLadderPrice.getMark());
            data.put("showMsg", "满" + itemLadderPrice.getMinQuantity() + itemUnit.getTitle() + "，￥" + CommonUtils.formatAmount(itemLadderPrice.getExpectPrice()) + "/" + itemUnit.getTitle());
            response.add(data);
        }
        return response;
    }

    private String itemTemplateSet(List<MarketItem> items) {
        StringBuilder itemSet = new StringBuilder();
        for (MarketItem item : items) {
            itemSet.append(item.getItemTemplateId()).append(CommonUtils.SPLIT_COMMA);
        }
        itemSet.deleteCharAt(itemSet.length() - 1);

        return itemSet.toString();
    }

    private String showDiscount(MarketItem item, ItemUnit itemUnit) {
        if (item.getDiscountType() == DiscountTypeEnum.NORMAL.getKey() || item.getDiscountPrice() >= item.getSellPrice()) {
            return "暂无优惠";
        }
        long maxPrice = item.getSellPrice();
        if (item.getMarketPrice() > maxPrice) {
            maxPrice = item.getMarketPrice();
        }
        float discount = (item.getDiscountPrice()) / (maxPrice * 1.0f);
        DiscountTypeEnum discountTypeEnum = DiscountTypeEnum.getDiscountTypeEnum(item.getDiscountType());
        String limit = (item.getRestrictionQuantity() == -1 ? "无限购" : "每天限购" + item.getRestrictionQuantity() + itemUnit.getTitle());
        return (discountTypeEnum == null ? "" : discountTypeEnum.getValue()) + CommonUtils.formatNumber(discount * 10, "0.00") + "折" + "(" + limit + ")";
    }

    private String processItem(List<MarketItem> items) {
        if (CommonUtils.isEmpty(items)) {
            return "";
        }
        StringBuilder itemSet = new StringBuilder();
        for (MarketItem item : items) {
            itemSet.append(item.getId()).append(CommonUtils.SPLIT_COMMA);
        }
        itemSet.deleteCharAt(itemSet.length() - 1);
        return itemSet.toString();
    }

    private void incrementSearchTimes(long marketId, String searchKey) {
        Runnable runnable = () -> {
            try {
                int result = dataAccessFactory.getItemDataAccessManager().incrementSearchTimes(marketId, searchKey);
                if (result <= 0) {
                    dataAccessFactory.getItemDataAccessManager().createHistorySearch(marketId, searchKey);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                dataAccessFactory.getItemDataAccessManager().incrementSearchTimes(marketId, searchKey);
            }
        };
        AsyncScheduledService.submitImmediateSaveTask(runnable);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(URLEncoder.encode("{\"101027\":3}", "UTF-8"));
    }
}