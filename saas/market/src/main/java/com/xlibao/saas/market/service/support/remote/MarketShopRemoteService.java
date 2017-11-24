package com.xlibao.saas.market.service.support.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.support.BasicRemoteService;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.market.data.model.MarketItemLocation;
import com.xlibao.market.data.model.MarketItemStockLockLogger;
import com.xlibao.market.data.model.MarketSplitOrder;
import com.xlibao.market.protocol.HardwareMessageType;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.config.ConfigFactory;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.data.model.MarketOrderStatusLogger;
import com.xlibao.saas.market.service.item.ItemLockTypeEnum;
import com.xlibao.saas.market.service.item.ItemStockLockStatusEnum;
import com.xlibao.saas.market.service.item.ItemStockOffsetTypeEnum;
import com.xlibao.saas.market.service.item.MarketItemErrorCodeEnum;
import com.xlibao.saas.market.service.message.MessageService;
import com.xlibao.saas.market.service.order.OrderNotifyTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/8/10.
 */
@Transactional
@Component
public class MarketShopRemoteService extends BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(MarketShopRemoteService.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;
    @Autowired
    private MessageService messageService;

    public void shipmentMessage(long passportId, String mark, String content, boolean repairShipment) {
        // 记录发送状态
        logger.info("推送出货消息到商店系统，passport id is " + passportId + ", content is " + content);

        if (!repairShipment) {
            MarketOrderStatusLogger orderStatusLogger = dataAccessFactory.getOrderDataAccessManager().getOrderStatusLogger(mark, OrderNotifyTypeEnum.HARDWARE.getKey(), OrderStatusEnum.ORDER_STATUS_PAYMENT);
            if (orderStatusLogger != null && orderStatusLogger.getRemoteStatus() == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) {
                logger.error("订单重复请求出货功能，mark : " + mark);
                throw new XlibaoRuntimeException("不能重复出货");
            }
            if (orderStatusLogger == null) {
                dataAccessFactory.getOrderDataAccessManager().createOrderStatusLogger(mark, content, OrderNotifyTypeEnum.HARDWARE.getKey(), OrderStatusEnum.ORDER_STATUS_PAYMENT, GlobalAppointmentOptEnum.LOGIC_FALSE.getKey(), System.currentTimeMillis());
            }
        }
        String finalContent = HardwareMessageType.SHIPMENT + content;
        Runnable runnable = () -> sendMessage(passportId, finalContent);
        // 执行即时任务
        AsyncScheduledService.submitImmediateRemoteNotifyTask(runnable);
    }

    public void shelvesMessage(long passportId, String content) {
        content = HardwareMessageType.SHELVES + content;
        // 记录发送状态
        logger.info("推送货柜消息到商店系统 passport id is " + passportId + ", content is " + content);

        String finalContent = content;
        Runnable runnable = () -> sendMessage(passportId, finalContent);
        // 执行即时任务
        AsyncScheduledService.submitImmediateRemoteNotifyTask(runnable);
    }

    public void orderDataMessage(long passportId, String content) {
        content = HardwareMessageType.ORDER + content;
        // 记录发送状态
        logger.info("推送获取订单货柜消息到商店系统 passport id is " + passportId + ", content is " + content);

        String finalContent = content;
        Runnable runnable = () -> sendMessage(passportId, finalContent);
        // 执行即时任务
        AsyncScheduledService.submitImmediateRemoteNotifyTask(runnable);
    }

    public void refundMessage(long passportId, String content) {
        content = HardwareMessageType.REFUND + content;
        // 记录发送状态
        logger.info("推送退货消息到商店系统 passport id is " + passportId + ", content is " + content);

        String finalContent = content;
        Runnable runnable = () -> sendMessage(passportId, finalContent);
        // 执行即时任务
        AsyncScheduledService.submitImmediateRemoteNotifyTask(runnable);
    }

    public void sendShipmentMessage(OrderEntry orderEntry) {
        // 推送给硬件进行拣货操作(缓存)
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(orderEntry.getShippingPassportId());

        List<MarketSplitOrder> splitOrders = dataAccessFactory.getOrderDataAccessManager().getSplitOrders(orderEntry.getId());
        Map<String, String> messages = new HashMap<>();
        for (MarketSplitOrder splitOrder : splitOrders) {
            StringBuilder message = new StringBuilder().append(orderEntry.getOrderSequenceNumber()).append(splitOrder.getSerialCode());
            int locationCount = 0;

            JSONArray itemSet = JSONObject.parseArray(splitOrder.getItemSet());
            for (int i = 0; i < itemSet.size(); i++) {
                JSONObject data = itemSet.getJSONObject(i);

                long itemId = data.getLongValue("itemId");
                long itemTemplateId = data.getLongValue("itemTemplateId");
                int quantity = data.getIntValue("quantity");

                dataAccessFactory.getItemDataAccessManager().decrementItemStock(itemId, quantity);
                String[] msg = decrementItemLocationStock(marketEntry, orderEntry.getOrderSequenceNumber(), itemId, itemTemplateId, quantity);
                message.append(msg[0]);

                locationCount += Integer.parseInt(msg[1]);
            }
            message.append(CommonUtils.toHexString(locationCount, 4, "0"));
            messages.put(orderEntry.getOrderSequenceNumber() + CommonUtils.SPLIT_UNDER_LINE + splitOrder.getSerialCode(), message.toString());
        }
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            // 发送消息给硬件做出货操作
            shipmentMessage(marketEntry.getPassportId(), entry.getKey(), entry.getValue(), false);
        }
        // 释放锁定库存
        releaseItemLock(orderEntry);
    }

    public boolean autoPickupMessage(MarketEntry marketEntry, OrderEntry orderEntry) {
        int errorCode = messageService.askOrderPickUp(orderEntry);
        if (errorCode == 1) { // 已完成取货
            return false;
        }
        if (errorCode == 2) { // 未完成配货，通知上层继续等待
            return true;
        }
        String content = HardwareMessageType.PICK_UP + orderEntry.getOrderSequenceNumber();
        // 可取货 并通知上层稍后继续询问
        sendMessage(marketEntry.getPassportId(), content);
        return true;
    }

    private void releaseItemLock(OrderEntry orderEntry) {
        List<MarketItemStockLockLogger> itemStockLockLoggers = dataAccessFactory.getItemDataAccessManager().getItemStockLockLoggers(orderEntry.getOrderSequenceNumber(), ItemLockTypeEnum.CREATE_ORDER, ItemStockLockStatusEnum.LOCK.getKey());

        if (CommonUtils.isEmpty(itemStockLockLoggers)) {
            return;
        }
        // 原来存在锁定的记录 进行解锁
        for (MarketItemStockLockLogger itemStockLockLogger : itemStockLockLoggers) {
            // 设定锁定记录为：出货状态
            dataAccessFactory.getItemDataAccessManager().modifyStockLockStatus(itemStockLockLogger.getId(), ItemStockLockStatusEnum.SHIPMENT.getKey());
        }
    }

    private String[] decrementItemLocationStock(MarketEntry marketEntry, String orderSequenceNumber, long itemId, long itemTemplateId, int quantity) {
        List<MarketItemLocation> itemLocations = dataAccessFactory.getItemDataAccessManager().getItemLocations(itemId);

        ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(itemTemplateId);
        // 组合硬件消息
        StringBuilder msgBuilder = new StringBuilder();
        int locationCount = 0;
        for (MarketItemLocation itemLocation : itemLocations) {
            int decrementStock = quantity;
            if (itemLocation.getStock() < quantity) { // 库存不足时 将库存清空
                decrementStock = itemLocation.getStock();
            }
            int result = dataAccessFactory.getItemDataAccessManager().offsetItemLocationStock(itemLocation, decrementStock, ItemStockOffsetTypeEnum.BUY.getKey(), marketEntry.getPassportId(), marketEntry.getName());
            if (result <= 0) {
                continue;
            }
            // 涉及的位置 +1
            locationCount++;
            quantity -= decrementStock;
            // 商品位置 数量(16进制，4位 不足时前面补0)
            msgBuilder.append(itemLocation.getLocationCode()).append(CommonUtils.toHexString(decrementStock, 4, "0"));
            if (quantity <= 0) {
                break;
            }
        }
        if (quantity > 0) {
            logger.error("【" + orderSequenceNumber + "】严重问题，商品库存不足，商品ID：" + itemId + "(" + itemTemplate.getName() + ")，需要扣除数量：" + quantity);
            throw MarketItemErrorCodeEnum.ITEM_STOCK_NOT_ENOUGH.throwException();
        }
        return new String[]{msgBuilder.toString(), String.valueOf(locationCount)};
    }

    private void sendMessage(long passportId, String content) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("partnerId", ConfigFactory.getXMarketConfig().getPartnerId());
        parameters.put("appId", ConfigFactory.getXMarketConfig().getMarketShopAppId());
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("messageContent", HardwareMessageType.HARDWARE_MSG_START + content + HardwareMessageType.HARDWARE_MSG_END);

        CommonUtils.fillSignature(parameters, ConfigFactory.getXMarketConfig().getMarketShopAppkey());

        String url = ConfigFactory.getDomainNameConfig().marketShopRemoteURL + "marketshop/message/sendHardwarePush.do";
        executor(url, parameters);
    }
}