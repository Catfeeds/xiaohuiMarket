package com.xlibao.saas.market.service.market.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.exception.code.ItemErrorCodeEnum;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.market.data.model.*;
import com.xlibao.market.protocol.HardwareMessageType;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.item.ItemUnit;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.service.item.ItemStockOffsetTypeEnum;
import com.xlibao.saas.market.service.item.MarketItemErrorCodeEnum;
import com.xlibao.saas.market.service.item.MarketItemStatusEnum;
import com.xlibao.saas.market.service.item.PrepareActionStatusEnum;
import com.xlibao.saas.market.service.market.*;
import com.xlibao.saas.market.service.support.remote.MarketShopRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/8/16.
 */
@Transactional
@Service("shelvesService")
public class ShelvesServiceImpl extends BasicWebService implements ShelvesService {

    private static final Logger logger = LoggerFactory.getLogger(ShelvesServiceImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;
    @Autowired
    private MarketShopRemoteService marketShopRemoteService;

    private static String VALID_ACTION_STATUS_SET = PrepareActionStatusEnum.UN_EXECUTOR.getKey() + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.EXCEPTION.getKey();

    @Override
    public void builderShelvesData(MarketEntry marketEntry, String content) {
        String[] contentArray = content.split("]");
        for (String c : contentArray) {
            String dataType = c.substring(0, 4);
            if (dataType.endsWith(HardwareMessageType.ERROR_MSG)) { // 表示没有此货架信息 直接跳过
                continue;
            }
            String data = c.substring(4).replaceAll("\\[", "");
            if ("0000".equals(dataType)) { // 反馈整个店铺有多少个组，每组有多少个单元的信息
                // 清空原来的货架数据
                dataAccessFactory.getMarketDataAccessManager().clearShelves(marketEntry.getId());
                groupAdapter(marketEntry, data);
                continue;
            }
            unitAdapter(marketEntry, dataType, data);
        }
    }

    /**
     * <pre>
     *     <b>获取组(走道)、货架(单元)、层(楼层)的值</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/getShelvesMarks.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>groupCode</b> - String 组编码，当 <b>shelvesType</b>为{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum#GROUP}或{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum#UNIT}时必填
     *          <b>unitCode</b> - String 单元编码，当 <b>shelvesType</b>为{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum#UNIT}时必填
     *          <b>shelvesType</b> - int 获取的货架类型，具体参考：{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum}
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 编码组，每个元素为String，即对应请求的数据集合
     * </pre>
     */
    @Override
    public JSONObject getShelvesMarks() {
        long marketId = getLongParameter("marketId");
        String groupCode = getUTF("groupCode", "");
        String unitCode = getUTF("unitCode", "");
        int shelvesType = getIntParameter("shelvesType", ShelvesTypeEnum.MARKET.getKey());

        List<String> marks = dataAccessFactory.getMarketDataAccessManager().getShelvesMarks(marketId, groupCode, unitCode, shelvesType);
        return success(marks);
    }

    @Override
    public JSONObject loaderClipDatas() {
        long marketId = getLongParameter("marketId");
        String groupCode = getUTF("groupCode");
        String unitCode = getUTF("unitCode");
        String floorCode = getUTF("floorCode");
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        List<MarketShelvesManager> shelvesManagers = dataAccessFactory.getMarketDataAccessManager().getClipDatas(marketId, groupCode, unitCode, floorCode, pageStartIndex, pageSize);
        if (CommonUtils.isEmpty(shelvesManagers)) {
            return MarketErrorCodeEnum.SHELVES_LOCATION_ERROR.response("货架信息有误，请检查是否未重置货架信息或提供的信息错误");
        }
        StringBuilder locationSet = new StringBuilder();
        for (MarketShelvesManager shelvesManager : shelvesManagers) {
            locationSet.append("'").append(groupCode).append(unitCode).append(floorCode).append(shelvesManager.getClipCode()).append("'").append(CommonUtils.SPLIT_COMMA);
        }
        locationSet.deleteCharAt(locationSet.length() - 1);

        List<MarketItemLocation> itemLocations = dataAccessFactory.getItemDataAccessManager().matchItemLocationForMarket(marketId, groupCode + unitCode + floorCode, pageStartIndex, pageSize);
        Map<String, MarketItemLocation> itemLocationMap = new HashMap<>();
        for (MarketItemLocation itemLocation : itemLocations) {
            itemLocationMap.put(itemLocation.getLocationCode(), itemLocation);
        }
        List<MarketPrepareAction> prepareActions = dataAccessFactory.getItemDataAccessManager().getPrepareActionsForLocationSet(marketId, locationSet.toString(), VALID_ACTION_STATUS_SET);
        Map<String, MarketPrepareAction> prepareActionMap = new HashMap<>();
        if (!CommonUtils.isEmpty(prepareActions)) {
            for (MarketPrepareAction prepareAction : prepareActions) {
                prepareActionMap.put(prepareAction.getItemLocation(), prepareAction);
            }
        }
        JSONArray response = new JSONArray();
        for (MarketShelvesManager shelvesManager : shelvesManagers) {
            String locationCode = groupCode + unitCode + floorCode + shelvesManager.getClipCode();
            MarketItemLocation itemLocation = itemLocationMap.get(locationCode);

            ItemTemplate itemTemplate = itemLocation == null ? null : ItemDataCacheService.getItemTemplate(itemLocation.getItemTemplateId());
            ItemUnit itemUnit = itemTemplate == null ? null : ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());

            JSONObject data = new JSONObject();
            data.put("locationCode", locationCode);
            data.put("barcode", itemTemplate == null ? "未存放商品" : itemTemplate.getBarcode());
            data.put("itemTemplateId", itemTemplate == null ? 0 : itemTemplate.getId());
            data.put("itemName", itemTemplate == null ? "未存放商品" : itemTemplate.getName());
            data.put("unitName", itemUnit == null ? "未知单位" : itemUnit.getTitle());
            data.put("itemQuantity", itemLocation == null ? 0 : itemLocation.getStock());

            MarketPrepareAction prepareAction = prepareActionMap.get(locationCode);
            data.put("taskId", prepareAction == null ? 0 : prepareAction.getId());
            data.put("taskStatus", prepareAction == null ? 0 : prepareAction.getStatus());
            data.put("hasCompleteQuantity", prepareAction == null ? 0 : prepareAction.getHasCompleteQuantity());
            data.put("hopeItemQuantity", prepareAction == null ? 0 : prepareAction.getHopeItemQuantity());

            response.add(data);
        }
        // TODO 将当前分页，当前页内容数量都要发回去
        return success(response);
    }

    @Override
    public JSONObject scanShelvesTask() {
        long marketId = getLongParameter("marketId");
        String barcode = getUTF("barcode");

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() != MarketStatusEnum.MAINTAIN.getKey()) {
            return MarketErrorCodeEnum.DON_NOT_MAINTAIN.response("[扫描检测]商店[" + marketEntry.getName() + "]必须处于作业状态才能执行上下架操作");
        }
        JSONArray response = new JSONArray();
        int remainTask = dataAccessFactory.getItemDataAccessManager().getRemainActionRows(marketId, ShelvesTaskTypeEnum.OFF_SHELVES.getKey(), VALID_ACTION_STATUS_SET);
        if (remainTask > 0) {
            // 存在下架任务
            MarketPrepareAction prepareAction = dataAccessFactory.getItemDataAccessManager().getPrepareAction(marketId, barcode, ShelvesTaskTypeEnum.OFF_SHELVES.getKey(), VALID_ACTION_STATUS_SET);
            if (prepareAction == null) {
                return MarketErrorCodeEnum.SHELVES_LOCATION_TASK_ERROR.response("位置[编码：" + barcode + "]上不存在下架任务\r\n注意：必须完成所有下架任务才能执行上架任务");
            }
            JSONObject data = fillPrepareActionMsg(prepareAction);
            response.add(data);
            return success(response);
        }

        List<MarketPrepareAction> prepareActions = dataAccessFactory.getItemDataAccessManager().getPrepareActionForBarcode(marketId, barcode, VALID_ACTION_STATUS_SET);
        if (CommonUtils.isEmpty(prepareActions)) {
            return MarketErrorCodeEnum.SHELVES_LOCATION_TASK_ERROR.response("商品[条码：" + barcode + "]不存在上架任务");
        }
        response.addAll(prepareActions.stream().map(this::fillPrepareActionMsg).collect(Collectors.toList()));
        return success(response);
    }

    @Override
    public JSONObject prepareAction() {
        long passportId = getLongParameter("passportId");
        long marketId = getLongParameter("marketId");
        String actionDatas = getUTF("actionDatas"); // 结构：[{"location":"01010101","itemTemplateId":"100000","quantity":"9"}, {"location":"01010102","itemTemplateId":"100000","quantity":"10"}, {"location":"01010103","itemTemplateId":"100001","quantity":"11"}]
        String hopeExecutorDate = getUTF("hopeExecutorDate", CommonUtils.dateFormat(CommonUtils.getTodayEarlyMorningMillisecond()));

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() == MarketStatusEnum.MAINTAIN.getKey()) {
            logger.error("[发布任务]商店[" + marketEntry.getName() + "]处于作业状态时不能执行发布任务操作，系统已拦截该请求！");
            return MarketErrorCodeEnum.DON_NOT_MAINTAIN.response("[发布检测]商店[" + marketEntry.getName() + "]正在作业中，暂时不能提交新的预操作任务");
        }
        JSONArray actionArray = JSONObject.parseArray(actionDatas);

        beforePrepareAction(passportId, marketId, actionArray, hopeExecutorDate); // 执行前的检查 检查本次的数量与原位置上是否存在未完成的任务

        for (int i = 0; i < actionArray.size(); i++) {
            JSONObject data = actionArray.getJSONObject(i);

            createPrepareAction(passportId, marketId, hopeExecutorDate, data, ShelvesTaskTypeEnum.ON_SHELVES);
        }
        return success();
    }

    @Override
    public JSONObject checkPrepareActionTask() {
        long taskId = getLongParameter("taskId");
        MarketPrepareAction prepareAction = dataAccessFactory.getItemDataAccessManager().getPrepareAction(taskId);

        if (prepareAction == null) {
            return MarketErrorCodeEnum.SHELVES_LOCATION_TASK_ERROR.response("没有找到指定的任务，任务ID：" + taskId);
        }
        return success(fillPrepareActionMsg(prepareAction));
    }

    @Override
    public JSONObject unExecutorTask() {
        long marketId = getLongParameter("marketId");
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        return showShelvesTask(0, marketId, VALID_ACTION_STATUS_SET, pageStartIndex, pageSize);
    }

    @Override
    public JSONObject cancelPrepareActionTask() {
        long passportId = getLongParameter("passportId");
        long taskId = getLongParameter("taskId");

        MarketPrepareAction prepareAction = dataAccessFactory.getItemDataAccessManager().getPrepareAction(taskId);
        if (prepareAction == null) {
            return MarketErrorCodeEnum.SHELVES_LOCATION_TASK_ERROR.response("找不到任务，任务ID：" + taskId);
        }
        int result = dataAccessFactory.getItemDataAccessManager().modifyPrepareActionStatus(passportId, prepareAction.getMarketId(), prepareAction.getType(), prepareAction.getItemLocation(), prepareAction.getHasCompleteQuantity(), prepareAction.getHopeItemQuantity(), VALID_ACTION_STATUS_SET, PrepareActionStatusEnum.INVALID.getKey(), CommonUtils.nowFormat());
        return result <= 0 ? fail() : success();
    }

    @Override
    public JSONObject showShelvesTask() {
        long marketId = getLongParameter("marketId");
        long passportId = getLongParameter("passportId");
        int status = getIntParameter("status", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);
        if (status == GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()) {
            // 未完成的列表
            MarketRelationship marketRelationship = dataAccessFactory.getMarketDataAccessManager().getRelationship(String.valueOf(passportId), marketId, MarketRelationshipTypeEnum.FOCUS.getKey());
            if (marketRelationship == null) {
                return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("您没有权限查看该商店的任务，请联系管理员！");
            }
            return showShelvesTask(0, marketId, VALID_ACTION_STATUS_SET, pageStartIndex, pageSize);
        }
        return showShelvesTask(passportId, marketId, PrepareActionStatusEnum.COMMIT.getKey() + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMPLETE.getKey(), pageStartIndex, pageSize);
    }

    @Override
    public JSONObject offShelves() {
        // 主要用于记录操作日志
        long passportId = getLongParameter("passportId");
        long marketId = getLongParameter("marketId");
        String location = getUTF("location");
        String barcode = getUTF("barcode");
        int offShelvesQuantity = getIntParameter("offShelvesQuantity");
        if (offShelvesQuantity <= 0) {
            return PlatformErrorCodeEnum.ILLEGAL_ARGUMENT.response("下架数量必须大于0");
        }
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() != MarketStatusEnum.MAINTAIN.getKey()) {
            logger.error("[下架] " + passportId + "没有先将商店[" + marketEntry.getName() + "]设置为作业状态便执行了下架操作，系统已拦截该请求！");
            return MarketErrorCodeEnum.DON_NOT_MAINTAIN.response("[下架检测]商店[" + marketEntry.getName() + "]必须处于作业状态才能执行下架操作");
        }
        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplateForBarcode(barcode);
        if (itemTemplate == null) {
            throw ItemErrorCodeEnum.BARCODE_NOT_EXIST.throwException("[下架检测]不存在条码为[" + barcode + "]的商品");
        }
        MarketRelationship marketRelationship = dataAccessFactory.getMarketDataAccessManager().getRelationship(String.valueOf(passportId), marketId, MarketRelationshipTypeEnum.FOCUS.getKey());
        if (marketRelationship == null) {
            return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("[下架检测]您没有权限执行该商店的任务，请联系管理员！");
        }
        // 获取下架任务记录(包括未执行或执行一半的记录)
        MarketPrepareAction prepareAction = dataAccessFactory.getItemDataAccessManager().getPrepareAction(marketId, location, ShelvesTaskTypeEnum.OFF_SHELVES.getKey(), VALID_ACTION_STATUS_SET);
        if (prepareAction == null) {
            return MarketItemErrorCodeEnum.NOT_FOUND_PREPARE_ACTION.response("[下架检测]位置" + location + "不存在预下架任务");
        }
        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplate.getId());
        if (item == null) {
            return MarketItemErrorCodeEnum.NOT_FOUND_ITEM.response("[下架检测]商店不存在条码为[" + barcode + "]的商品");
        }
        MarketItemLocation itemLocation = dataAccessFactory.getItemDataAccessManager().getItemLocation(item.getId(), location);
        if (itemLocation == null) {
            return MarketItemErrorCodeEnum.ITEM_LOCATION_ERROR.response("[下架检测]位置[" + location + "]上不存在条码为[" + barcode + "]的商品");
        }
        int offsetLocationStock = itemLocation.getStock(); // 货架上的商品剩余数量(本次需要下架的数量)
        String mark = "[系统]正常完成下架任务";
        if (offsetLocationStock != offShelvesQuantity) { // 当系统要求的下架数量与操作员提供的数量不一致时
            logger.error("[0001]下架数量有误；货架商品剩余数量：" + offsetLocationStock + "；本次操作下架数量：" + offShelvesQuantity);
            mark = "[系统]异常操作：任务要求下架数量：" + offsetLocationStock + "(最终执行的值)，但本次执行下架数量为：" + offShelvesQuantity;
        }
        int offsetItemStock = offsetLocationStock;
        int status = item.getStatus();
        if (item.getStock() <= offsetItemStock) {
            status = MarketItemStatusEnum.OFF_SALE.getKey(); // 商品设置为下架状态
            if (item.getStock() < offsetItemStock) {
                logger.error("[0002]下架数量有误；商品剩余总数量：" + item.getStock() + "(将全部扣除)；本次执行的下架数量：" + offShelvesQuantity + "；本次系统要求商品偏移数量：" + offsetItemStock);
                offsetItemStock = item.getStock();
            }
        }
        Passport passport = PassportRemoteService.getPassport(passportId);
        logger.info("[下架] " + passport.getShowName() + "(" + passportId + ")正在对商品\"" + itemTemplate.getName() + "(条码为：" + barcode + ")\"" + "进行下架操作；商店ID：" + marketId + "，商品ID：" + item.getId()
                + "库存数量：" + item.getStock() + "，所在位置：" + location + "，位置上商品数量：" + offsetLocationStock + "，执行的下架数量：" + offShelvesQuantity + "；执行后商品状态应为：" + status);
        int result = dataAccessFactory.getItemDataAccessManager().offShelves(item.getId(), offsetItemStock, status); // 减少商品库存 当库存为0时 设置为下架
        if (result <= 0) {
            throw PlatformErrorCodeEnum.DB_ERROR.throwException("[0001]更新商品库存失败");
        }
        // 需减少位置上的数量
        result = dataAccessFactory.getItemDataAccessManager().offsetItemLocationStock(itemLocation, offsetLocationStock, ItemStockOffsetTypeEnum.OFF_SHELVES.getKey(), passportId, passport.getShowName());
        if (result <= 0) {
            throw PlatformErrorCodeEnum.DB_ERROR.throwException("[0002]更新商品库存失败");
        }
        completePrepareActionTask(passportId, marketId, location, ShelvesTaskTypeEnum.OFF_SHELVES.getKey(), passportId, itemTemplate.getId(), offShelvesQuantity, 0, offsetLocationStock, mark, prepareAction.getStatus(), PrepareActionStatusEnum.COMPLETE.getKey());
        return success();
    }

    @Override
    public JSONObject onShelves() {
        // 主要用于记录操作日志
        long passportId = getLongParameter("passportId");
        long marketId = getLongParameter("marketId");
        String location = getUTF("location");
        String barcode = getUTF("barcode");
        int onShelvesQuantity = getIntParameter("onShelvesQuantity");
        if (onShelvesQuantity <= 0) {
            return PlatformErrorCodeEnum.ILLEGAL_ARGUMENT.response("上架数量必须大于0");
        }
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() != MarketStatusEnum.MAINTAIN.getKey()) {
            logger.error("[上架] " + passportId + "没有先将商店[" + marketEntry.getName() + "]设置为作业状态便执行了上架操作，系统已拦截该请求！");
            return MarketErrorCodeEnum.DON_NOT_MAINTAIN.response("[上架检测]商店[" + marketEntry.getName() + "]必须处于作业状态才能执行上架操作");
        }
        MarketRelationship marketRelationship = dataAccessFactory.getMarketDataAccessManager().getRelationship(String.valueOf(passportId), marketId, MarketRelationshipTypeEnum.FOCUS.getKey());
        if (marketRelationship == null) {
            return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("[上架检测]您没有权限执行该商店的任务，请联系管理员！");
        }
        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplateForBarcode(barcode);
        if (itemTemplate == null) {
            throw ItemErrorCodeEnum.BARCODE_NOT_EXIST.throwException("[上架检测]不存在条码为[" + barcode + "]的商品");
        }
        // 检查是否存在未完成的下架任务
        int remainRows = dataAccessFactory.getItemDataAccessManager().getRemainActionRows(marketId, ShelvesTaskTypeEnum.OFF_SHELVES.getKey(), VALID_ACTION_STATUS_SET);
        if (remainRows > 0) {
            throw MarketItemErrorCodeEnum.EXIST_PREPARE_ACTION_ERROR.throwException("[上架检测]必须完成所有的下架任务才能执行上架任务，未完成的下架任务数量：" + remainRows);
        }
        MarketPrepareAction prepareAction = dataAccessFactory.getItemDataAccessManager().getPrepareAction(marketId, location, ShelvesTaskTypeEnum.ON_SHELVES.getKey(), VALID_ACTION_STATUS_SET);
        if (prepareAction == null) {
            return MarketItemErrorCodeEnum.NOT_FOUND_PREPARE_ACTION.response("[上架检测]位置" + location + "不存在上架任务");
        }
        if (!barcode.equals(prepareAction.getHopeItemBarcode())) {
            ItemTemplate it = ItemDataCacheService.getItemTemplateForBarcode(prepareAction.getHopeItemBarcode());
            return MarketItemErrorCodeEnum.ERROR_PREPARE_ACTION.response("[上架检测]操作被拒绝；期望该位置存放商品(条码：" + prepareAction.getHopeItemBarcode() + "，名称：[" + it.getName() + "])，" +
                    "与您提供的商品(条码：" + barcode + "，名称：[" + itemTemplate.getName() + "])不一致");
        }
        int hopeExecutorQuantity = (prepareAction.getHopeItemQuantity() - prepareAction.getHasCompleteQuantity());
        if (onShelvesQuantity > hopeExecutorQuantity) {
            return MarketItemErrorCodeEnum.ITEM_LOCATION_QUANTITY_ERROR.response("[上架检测]错误的操作行为，您本次执行的上架数量：" + onShelvesQuantity + "；任务要求上架数量：" + hopeExecutorQuantity);
        }
        MarketItem item = dataAccessFactory.getItemDataAccessManager().getItem(marketId, itemTemplate.getId());
        if (item == null) {
            // 商店未存在该模版的商品 先添加
            item = MarketItem.newInstance(marketId, itemTemplate, (byte) MarketItemStatusEnum.NORMAL.getKey());
            dataAccessFactory.getItemDataAccessManager().createItem(item);
        }
        Passport passport = PassportRemoteService.getPassport(passportId);
        logger.info("[上架] " + passport.getShowName() + "(" + passportId + ")正在对商品\"" + itemTemplate.getName() + "(条码为：" + barcode + ")\"" + "进行上架操作；商店ID：" + marketId + "，商品ID：" + item.getId()
                + "，所在位置：" + location + "，上架数量：" + onShelvesQuantity);
        int taskStatus = PrepareActionStatusEnum.COMMIT.getKey();
        String mark = "[系统]正常完成上架任务";
        if (onShelvesQuantity < hopeExecutorQuantity) {
            taskStatus = PrepareActionStatusEnum.EXCEPTION.getKey();
            mark = "[系统]任务要求上架数量：" + hopeExecutorQuantity + "；实际本次执行上架数量为：" + onShelvesQuantity + "；未完成数量为：" + (hopeExecutorQuantity - onShelvesQuantity);
        }
        // 存在该模版的商品时 更新库存和状态即可 注意：使用了下架的数据库支持，因此偏移值必须为负数
        int result = dataAccessFactory.getItemDataAccessManager().offShelves(item.getId(), -onShelvesQuantity, MarketItemStatusEnum.NORMAL.getKey());
        if (result <= 0) {
            throw PlatformErrorCodeEnum.DB_ERROR.throwException("[0001]更新商品库存失败");
        }

        MarketItemLocation itemLocation = dataAccessFactory.getItemDataAccessManager().getItemLocationForMarket(marketId, location);
        if (itemLocation != null) {
            if (itemLocation.getStock() > 0) {
                if (!Objects.equals(itemLocation.getItemId(), item.getId())) {
                    itemTemplate = ItemDataCacheService.getItemTemplate(item.getItemTemplateId());
                    throw MarketItemErrorCodeEnum.ITEM_LOCATION_ERROR.throwException("位置[" + location + "]上存在条码为[" + itemTemplate.getBarcode() + "]的商品[" + itemTemplate.getName() + "]");
                }
                // 需增加位置上的数量
                result = dataAccessFactory.getItemDataAccessManager().offsetItemLocationStock(itemLocation, -onShelvesQuantity, ItemStockOffsetTypeEnum.ON_SHELVES.getKey(), passportId, passport.getShowName());
                if (result <= 0) {
                    throw PlatformErrorCodeEnum.DB_ERROR.throwException("[0002]更新商品库存失败");
                }
                // 完成了预操作行为
                completePrepareActionTask(passportId, marketId, location, ShelvesTaskTypeEnum.ON_SHELVES.getKey(), passportId, itemTemplate.getId(), onShelvesQuantity, prepareAction.getHasCompleteQuantity(), prepareAction.getHopeItemQuantity(), mark, prepareAction.getStatus(), taskStatus);
                return success();
            }
            // 直接移除原来位置的商品信息
            dataAccessFactory.getItemDataAccessManager().removeItemLocation(itemLocation.getId());
        }
        // 初始化商品位置对象
        itemLocation = MarketItemLocation.newInstance(marketId, item.getId(), item.getItemTemplateId(), location, onShelvesQuantity);
        // 新建位置信息
        result = dataAccessFactory.getItemDataAccessManager().createItemLocation(itemLocation, ShelvesTaskTypeEnum.ON_SHELVES.getKey(), passportId, passport.getShowName());
        if (result <= 0) {
            throw PlatformErrorCodeEnum.DB_ERROR.throwException("建立位置信息记录失败");
        }
        // 完成了预操作行为
        completePrepareActionTask(passportId, marketId, location, ShelvesTaskTypeEnum.ON_SHELVES.getKey(), passportId, itemTemplate.getId(), onShelvesQuantity, prepareAction.getHasCompleteQuantity(), prepareAction.getHopeItemQuantity(), mark, prepareAction.getStatus(), taskStatus);
        return success();
    }

    @Override
    public JSONObject findValidTasks() {
        long marketId = getLongParameter("marketId");
        int remainQuantity = dataAccessFactory.getItemDataAccessManager().getRemainActionRows(marketId, 0, VALID_ACTION_STATUS_SET);
        return remainQuantity > 0 ? MarketItemErrorCodeEnum.EXIST_PREPARE_ACTION_ERROR.response("存在未完成的任务，数量：" + remainQuantity) : success();
    }

    @Override
    public JSONObject finishTodayPrepareActions() {
        long passportId = getLongParameter("passportId");
        long marketId = getLongParameter("marketId");
        String mark = getUTF("mark", "");

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() != MarketStatusEnum.MAINTAIN.getKey()) {
            logger.error("[结案] " + passportId + "没有先将商店[" + marketEntry.getName() + "]设置为作业状态便执行了结案操作，系统已拦截该请求！");
            return MarketErrorCodeEnum.DON_NOT_MAINTAIN.response("[结案检测]商店[" + marketEntry.getName() + "]必须处于作业状态才能执行结案操作");
        }
        MarketRelationship marketRelationship = dataAccessFactory.getMarketDataAccessManager().getRelationship(String.valueOf(passportId), marketId, MarketRelationshipTypeEnum.FOCUS.getKey());
        if (marketRelationship == null) {
            return MarketErrorCodeEnum.CAN_NOT_FOUND_FOCUS_RELATIONSHIP.response("[结案检测]您没有权限执行该商店的任务，请联系管理员！");
        }
        if (CommonUtils.isNullString(mark)) { // 当没有备注信息时，检查是否存在异常的任务
            // 检查是否存在异常的上架任务
            int remainRows = dataAccessFactory.getItemDataAccessManager().getRemainActionRows(marketId, ShelvesTaskTypeEnum.ON_SHELVES.getKey(), VALID_ACTION_STATUS_SET);
            if (remainRows > 0) {
                throw MarketItemErrorCodeEnum.EXIST_PREPARE_ACTION_ERROR.throwException("[结案检测]存在" + remainRows + "个异常的任务，请如实填写异常信息");
            }
        }
        Passport passport = PassportRemoteService.getPassport(passportId);

        List<MarketShelvesDailyTaskLogger> shelvesDailyTaskLoggers = dataAccessFactory.getItemDataAccessManager().preparedSummaryData(passportId, marketId, ShelvesTaskTypeEnum.ON_SHELVES.getKey(), VALID_ACTION_STATUS_SET + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMMIT.getKey());

        for (MarketShelvesDailyTaskLogger shelvesDailyTaskLogger : shelvesDailyTaskLoggers) {
            int distinctItemBarcodeQuantity = dataAccessFactory.getItemDataAccessManager().distinctPrepareItemBarcode(marketId, VALID_ACTION_STATUS_SET + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMMIT.getKey(), passportId,
                    CommonUtils.dateFormat(shelvesDailyTaskLogger.getHopeExecutorDate().getTime()));

            shelvesDailyTaskLogger.setPassportId(passportId);
            shelvesDailyTaskLogger.setPassportName(passport.getShowName());
            shelvesDailyTaskLogger.setMarketId(marketId);
            shelvesDailyTaskLogger.setActualBarcodeQuantity(distinctItemBarcodeQuantity);
            shelvesDailyTaskLogger.setMark(mark);
        }
        // 将该商店的所有任务设置为完成状态
        dataAccessFactory.getItemDataAccessManager().batchModifyPrepareActionStatus(passportId, marketId, PrepareActionStatusEnum.COMPLETE.getKey(), VALID_ACTION_STATUS_SET + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMMIT.getKey());

        for (MarketShelvesDailyTaskLogger shelvesDailyTaskLogger : shelvesDailyTaskLoggers) {
            dataAccessFactory.getItemDataAccessManager().createShelvesDailyTaskLogger(shelvesDailyTaskLogger);
        }
        dataAccessFactory.getMarketDataAccessManager().marketResponse(marketId, MarketStatusEnum.NORMAL.getKey(), 0);

        JSONObject response = new JSONObject();
        response.put("status", MarketStatusEnum.NORMAL.getKey());
        return success(response);
    }

    @Override
    public JSONObject showExceptionTask() {
        long marketId = getLongParameter("marketId");
        String happenDate = getUTF("happenDate", null);

        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        JSONObject response;
        if (CommonUtils.isNullString(happenDate)) {
            List<MarketShelvesDailyTaskLogger> shelvesDailyTaskLoggers = dataAccessFactory.getItemDataAccessManager().getShelvesDailyTaskLoggers(marketId, pageStartIndex, pageSize);
            int size = dataAccessFactory.getItemDataAccessManager().getShelvesDailyTaskLoggerSize(marketId);

            response = CommonUtils.fillPageMessage(size, pageStartIndex, pageSize);

            JSONArray data = new JSONArray();
            for (MarketShelvesDailyTaskLogger shelvesDailyTaskLogger : shelvesDailyTaskLoggers) {
                JSONObject taskMsg = new JSONObject();

                taskMsg.put("id", shelvesDailyTaskLogger.getId());
                taskMsg.put("executorPassportId", shelvesDailyTaskLogger.getPassportId());
                taskMsg.put("executorPassportName", shelvesDailyTaskLogger.getPassportName());

                taskMsg.put("hopeTotalItemQuantity", shelvesDailyTaskLogger.getHopeTotalItemQuantity());
                taskMsg.put("actualItemQuantity", shelvesDailyTaskLogger.getActualItemQuantity());

                taskMsg.put("hopeTotalBarcodeQuantity", shelvesDailyTaskLogger.getHopeTotalBarcodeQuantity());
                taskMsg.put("actualBarcodeQuantity", shelvesDailyTaskLogger.getActualBarcodeQuantity());
                taskMsg.put("happenDate", CommonUtils.defineDateFormat(shelvesDailyTaskLogger.getHopeExecutorDate().getTime(), CommonUtils.Y_M_D));
                taskMsg.put("submitTime", CommonUtils.dateFormat(shelvesDailyTaskLogger.getCreateTime().getTime()));

                data.add(taskMsg);
            }
            response.put("data", data);
            return success(response);
        }
        List<MarketPrepareAction> prepareActions = dataAccessFactory.getItemDataAccessManager().getPrepareActions(0, marketId,
                VALID_ACTION_STATUS_SET + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.INVALID.getKey() + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMPLETE.getKey() + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMMIT.getKey(),
                happenDate, pageStartIndex, pageSize);
        int size = dataAccessFactory.getItemDataAccessManager().getPrepareActionSize(0, marketId, VALID_ACTION_STATUS_SET + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.INVALID.getKey()
                + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMPLETE.getKey() + CommonUtils.SPLIT_COMMA + PrepareActionStatusEnum.COMMIT.getKey(), happenDate);

        response = CommonUtils.fillPageMessage(size, pageStartIndex, pageSize);
        JSONArray data = prepareActions.stream().map(this::fillPrepareActionMsg).collect(Collectors.toCollection(JSONArray::new));

        response.put("data", data);
        return success(response);
    }

    private void completePrepareActionTask(long passportId, long marketId, String location, int type, long executorPassportId, long itemTemplateId, int quantity, int hasCompleteQuantity, int hopeQuantity, String mark, int matchActionStatus, int hopeActionStatus) {
        // 完成了预操作行为
        dataAccessFactory.getItemDataAccessManager().modifyPrepareActionStatus(passportId, marketId, type, location, quantity, hopeQuantity, String.valueOf(matchActionStatus), hopeActionStatus, CommonUtils.nowFormat());

        MarketTaskLogger taskLogger = new MarketTaskLogger();
        taskLogger.setMarketId(marketId);
        taskLogger.setItemLocation(location);
        taskLogger.setType(type);
        taskLogger.setExecutorPassportId(executorPassportId);
        taskLogger.setItemTemplateId(itemTemplateId);
        taskLogger.setItemQuantity(quantity);
        taskLogger.setHopeItemQuantity(hopeQuantity - hasCompleteQuantity);
        taskLogger.setMark(mark);
        dataAccessFactory.getMarketDataAccessManager().createTaskLogger(taskLogger);
    }

    private JSONObject showShelvesTask(long passportId, long marketId, String statusSet, int pageStartIndex, int pageSize) {
        List<MarketPrepareAction> prepareActions = dataAccessFactory.getItemDataAccessManager().getPrepareActions(passportId, marketId, statusSet, null, pageStartIndex, pageSize);

        if (CommonUtils.isEmpty(prepareActions)) {
            throw MarketErrorCodeEnum.SHELVES_LOCATION_TASK_ERROR.throwException("没有存在未执行的任务");
        }
        JSONArray response = prepareActions.stream().map(this::fillPrepareActionMsg).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    private JSONObject fillPrepareActionMsg(MarketPrepareAction prepareAction) {
        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(prepareAction.getHopeItemTemplateId());
        ItemUnit itemUnit = ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());

        String formatDate = CommonUtils.defineDateFormat(prepareAction.getHopeExecutorDate().getTime(), CommonUtils.Y_M_D);

        JSONObject response = new JSONObject();
        response.put("taskId", prepareAction.getId());
        response.put("locationCode", prepareAction.getItemLocation());
        response.put("itemTemplateId", prepareAction.getHopeItemTemplateId());
        response.put("itemName", itemTemplate.getName());
        response.put("unitName", itemUnit.getTitle());
        response.put("hasCompleteQuantity", prepareAction.getHasCompleteQuantity());
        response.put("itemQuantity", prepareAction.getHopeItemQuantity());
        response.put("barcode", prepareAction.getHopeItemBarcode());
        response.put("type", prepareAction.getType());
        response.put("status", prepareAction.getStatus());
        response.put("hopeExecutorDate", formatDate);
        response.put("completeTime", CommonUtils.dateFormat(prepareAction.getCompleteTime() == null ? 0 : prepareAction.getCompleteTime().getTime()));

        if (prepareAction.getType() == ShelvesTaskTypeEnum.OFF_SHELVES.getKey() && prepareAction.getStatus() != PrepareActionStatusEnum.COMPLETE.getKey()) {
            MarketItemLocation itemLocation = dataAccessFactory.getItemDataAccessManager().getItemLocationForMarket(prepareAction.getMarketId(), prepareAction.getItemLocation());
            if (itemLocation == null) {
                throw MarketErrorCodeEnum.SHELVES_LOCATION_ERROR.throwException("商店货架编码：" + prepareAction.getItemLocation() + "没有商品记录");
            }
            response.put("itemQuantity", itemLocation.getStock());
        }

        if (CommonUtils.isToday(prepareAction.getHopeExecutorDate().getTime())) {
            response.put("dateTitle", "今日");
        } else if (CommonUtils.isSameDay(prepareAction.getHopeExecutorDate().getTime(), System.currentTimeMillis() - CommonUtils.DAY_MILLISECOND_TIME)) {
            response.put("dateTitle", "昨日");
        } else {
            response.put("dateTitle", formatDate + " " + CommonUtils.dayOfWeekForTime(prepareAction.getHopeExecutorDate().getTime()));
        }
        return response;
    }

    private void beforePrepareAction(long passportId, long marketId, JSONArray actionArray, String hopeExecutorDate) {
        StringBuilder locationSet = new StringBuilder();
        StringBuilder itemTemplateSet = new StringBuilder();

        Map<String, Long> locationItemTemplateSet = new HashMap<>();
        for (int i = 0; i < actionArray.size(); i++) {
            JSONObject data = actionArray.getJSONObject(i);

            long itemTemplateId = data.getLongValue("itemTemplateId");
            ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(itemTemplateId);
            int quantity = data.getIntValue("quantity");
            if (quantity <= 0) {
                throw MarketItemErrorCodeEnum.PREPARE_QUANTITY_ERROR.throwException("[" + itemTemplate.getName() + "]预上架数量必须大于0");
            }
            locationSet.append("'").append(data.getString("location")).append("'").append(CommonUtils.SPLIT_COMMA);
            itemTemplateSet.append(itemTemplateId).append(CommonUtils.SPLIT_COMMA);

            locationItemTemplateSet.put(data.getString("location"), data.getLongValue("itemTemplateId"));
        }
        locationSet.deleteCharAt(locationSet.length() - 1);
        itemTemplateSet.deleteCharAt(itemTemplateSet.length() - 1);

        List<MarketPrepareAction> prepareActions = dataAccessFactory.getItemDataAccessManager().getPrepareActionsForLocationSet(marketId, locationSet.toString(), VALID_ACTION_STATUS_SET);
        if (!CommonUtils.isEmpty(prepareActions)) { // 指定的位置 存在未执行的任务
            StringBuilder errorMsg = new StringBuilder().append("以下位置上存在未完成的任务：");
            for (MarketPrepareAction prepareAction : prepareActions) {
                ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(prepareAction.getHopeItemTemplateId());
                ItemUnit itemUnit = ItemDataCacheService.getItemUnit(itemTemplate.getUnitId());
                errorMsg.append("\r\n\t");
                errorMsg.append("位置 -- [").append(prepareAction.getItemLocation()).append("]期望存放").append(prepareAction.getHopeItemQuantity()).append(itemUnit.getTitle()).append("【").append(itemTemplate.getName()).append("】");
            }
            throw MarketItemErrorCodeEnum.PREPARE_ACTION_LOCATION_ERROR.throwException(errorMsg.toString());
        }
        // 建立下架任务
        List<MarketItemLocation> itemLocations = dataAccessFactory.getItemDataAccessManager().getItemLocationsForMarket(marketId, locationSet.toString());
        for (MarketItemLocation itemLocation : itemLocations) {
            long itemTemplateId = locationItemTemplateSet.get(itemLocation.getLocationCode());
            if (itemTemplateId == itemLocation.getItemTemplateId()) {
                continue;
            }
            if (itemLocation.getStock() <= 0) {
                continue;
            }
            JSONObject prepareActionData = new JSONObject();
            prepareActionData.put("itemTemplateId", itemLocation.getItemTemplateId());
            prepareActionData.put("location", itemLocation.getLocationCode());
            prepareActionData.put("quantity", itemLocation.getStock()); // 该数量并不能作为最终控制的数量，仅为期望或参考数据

            createPrepareAction(passportId, marketId, hopeExecutorDate, prepareActionData, ShelvesTaskTypeEnum.OFF_SHELVES);
        }
    }

    private void createPrepareAction(long passportId, long marketId, String hopeExecutorDate, JSONObject prepareActionData, ShelvesTaskTypeEnum shelvesTaskTypeEnum) {
        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(prepareActionData.getLongValue("itemTemplateId"));

        MarketPrepareAction prepareAction = new MarketPrepareAction();
        prepareAction.setMarketId(marketId);
        prepareAction.setActionPassportId(passportId);
        prepareAction.setItemLocation(prepareActionData.getString("location"));
        prepareAction.setHopeItemTemplateId(itemTemplate.getId());
        prepareAction.setHopeItemBarcode(itemTemplate.getBarcode());
        prepareAction.setHasCompleteQuantity(0);
        prepareAction.setHopeItemQuantity(prepareActionData.getIntValue("quantity"));
        prepareAction.setHopeExecutorDate(new Date(CommonUtils.dateFormatToLong(hopeExecutorDate)));
        prepareAction.setType(shelvesTaskTypeEnum.getKey());
        prepareAction.setStatus(PrepareActionStatusEnum.UN_EXECUTOR.getKey());
        prepareAction.setCreateTime(new Date());

        if (dataAccessFactory.getItemDataAccessManager().createPrepareAction(prepareAction) <= 0) {
            throw new XlibaoRuntimeException("无法建立预操作记录，请稍后重试");
        }
    }

    private void groupAdapter(MarketEntry marketEntry, String content) {
        String[] groups = content.split(CommonUtils.SPLIT_COMMA);
        for (int i = 0; i < groups.length; i++) {
            String groupCode = CommonUtils.numberToString(String.valueOf(i + 1), 2, "0"); // 当前组的编码

            int unitQuantity = Integer.parseInt(groups[i]); // 当前组的单元数量
            for (int j = 0; j < unitQuantity; j++) {
                String unitCode = CommonUtils.numberToString(String.valueOf(j + 1), 2, "0"); // 当前单元的编码

                createShelves(marketEntry.getId(), marketEntry.getPassportId(), groupCode, unitCode, "01", "01");
            }
            // 刷新单元信息
            refreshUnitDatas(marketEntry, groupCode);
        }
    }

    private void unitAdapter(MarketEntry marketEntry, String dataType, String content) {
        String[] floors = content.split(CommonUtils.SPLIT_COMMA);
        for (int i = 0; i < floors.length; i++) {
            String floorCode = CommonUtils.numberToString(String.valueOf(i + 1), 2, "0"); // 当前楼层的编码

            int clipQuantity = Integer.parseInt(floors[i]); // 当前楼层的弹夹个数
            for (int j = 0; j < clipQuantity; j++) {
                String clipCode = CommonUtils.numberToString(String.valueOf(j + 1), 2, "0");

                if ("01".equals(floorCode) && "01".equals(clipCode)) { // 已初始化
                    continue;
                }
                createShelves(marketEntry.getId(), marketEntry.getPassportId(), dataType.substring(0, 2), dataType.substring(2), floorCode, clipCode);
            }
        }
    }

    private void refreshUnitDatas(MarketEntry marketEntry, String groupCode) {
        marketShopRemoteService.shelvesMessage(marketEntry.getPassportId(), groupCode);
    }

    private void createShelves(long marketId, long passportId, String groupCode, String unitCode, String floorCode, String clipCode) {
        MarketShelvesManager shelvesManager = new MarketShelvesManager();
        shelvesManager.setMarketId(marketId);
        shelvesManager.setGroupCode(groupCode);
        shelvesManager.setUnitCode(unitCode);
        shelvesManager.setFloorCode(floorCode);
        shelvesManager.setClipCode(clipCode);
        shelvesManager.setCreateTime(new Date());
        shelvesManager.setModifyPassportId(passportId);
        shelvesManager.setLastModifyTime(new Date());
        dataAccessFactory.getMarketDataAccessManager().createShelves(shelvesManager);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(URLEncoder.encode("[{\"location\":\"01010101\",\"itemTemplateId\":\"100000\",\"quantity\":\"9\"}, {\"location\":\"01010102\",\"itemTemplateId\":\"100000\",\"quantity\":\"10\"}, {\"location\":\"01010103\",\"itemTemplateId\":\"100001\",\"quantity\":\"11\"}]", "UTF-8"));
    }
}