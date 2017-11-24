package com.xlibao.saas.market.service.order;

import cn.jpush.api.JPushClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.order.DeliverTypeEnum;
import com.xlibao.common.constant.order.OrderPermissionTypeEnum;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.io.service.jpush.JPushConfig;
import com.xlibao.io.service.jpush.JPushService;
import com.xlibao.market.data.model.MarketRelationship;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderItemSnapshot;
import com.xlibao.saas.market.config.ConfigFactory;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.listener.OrderEventListener;
import com.xlibao.saas.market.service.XMarketServiceConfig;
import com.xlibao.saas.market.service.market.MarketRelationshipTypeEnum;
import com.xlibao.saas.market.service.order.properties.PropertiesKeyEnum;
import com.xlibao.saas.market.service.support.remote.OrderRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author chinahuangxc on 2017/7/27.
 */
@Transactional
@Component
public class InternalOrderEventListenerImpl implements OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(InternalOrderEventListenerImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;

    private ItemLengthComparator itemLengthComparator = new ItemLengthComparator();

    @Override
    public void notifyCreatedOrder(OrderEntry orderEntry) {
        List<OrderItemSnapshot> itemSnapshots = orderEntry.getItemSnapshots();
        // 按最短的进行排序
        Collections.sort(itemSnapshots, itemLengthComparator);
        // 分组
        Map<Integer, JSONArray> groupOrders = new HashMap<>();
        // 计数器
        int counter = 1;

        int currentLength = 0;
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(itemSnapshot.getItemTemplateId());

            int productNumber = 0;
            for (int i = 0; i < itemSnapshot.totalQuantity(); i++) {
                if (currentLength >= XMarketServiceConfig.ORDER_ITEM_MAX_LENGTH) {
                    groupItemSnapshot(groupOrders, counter, itemSnapshot, productNumber);

                    counter++;
                    currentLength = 0;
                    productNumber = 0;
                    continue;
                }
                currentLength += itemTemplate.minimumLength();
                productNumber++;
            }
            if (productNumber > 0) {
                groupItemSnapshot(groupOrders, counter, itemSnapshot, productNumber);

                if (currentLength >= XMarketServiceConfig.ORDER_ITEM_MAX_LENGTH) {
                    counter++;
                    currentLength = 0;
                }
            }
        }
        logger.info("对本次购买商品进行分组，分组结果数量：" + counter);

        for (Map.Entry<Integer, JSONArray> entry : groupOrders.entrySet()) {
            int code = entry.getKey();
            JSONArray value = entry.getValue();

            String serialCode = CommonUtils.toHexString(code, 4, "0");

            dataAccessFactory.getOrderDataAccessManager().createSplitOrder(orderEntry.getId(), orderEntry.getOrderSequenceNumber(), serialCode, value.toString());
        }
        JSONObject containerData = new JSONObject();
        containerData.put("containerCount", counter);
        containerData.put("containers", new JSONArray());

        dataAccessFactory.getOrderDataAccessManager().createOrderProperties(orderEntry.getId(), orderEntry.getOrderSequenceNumber(), PropertiesKeyEnum.CONTAINER_DATA.getTypeEnum().getKey(), PropertiesKeyEnum.CONTAINER_DATA.getKey(), containerData.toJSONString());
    }

    @Override
    public void notifyOrderPayment(OrderEntry order) {
        if (order.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey()) { // 自提时 支付完成后将订单设置为出货状态
            OrderRemoteService.refreshOrderStatus(order.getOrderSequenceNumber(), OrderPermissionTypeEnum.CONSUMER.getKey(), order.getPartnerUserId(), OrderStatusEnum.ORDER_STATUS_DELIVER.getKey(), String.valueOf(OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()));
            return;
        }
        pushCourierMessage(order);
    }

    @Override
    public void notifyAcceptedOrder(OrderEntry orderEntry) {
    }

    @Override
    public void notifyDeliveredOrderEntry(OrderEntry orderEntry) {
    }

    @Override
    public void notifyConfirmedOrderEntry(OrderEntry orderEntry) {
    }

    @Override
    public void notifyCanceledOrderEntry(OrderEntry orderEntry) {
    }

    private void groupItemSnapshot(Map<Integer, JSONArray> groupOrders, int counter, OrderItemSnapshot itemSnapshot, int productNumber) {
        JSONArray groupOrder = groupOrders.get(counter);
        if (groupOrder == null) {
            groupOrder = new JSONArray();
        }
        JSONObject data = new JSONObject();

        data.put("itemId", itemSnapshot.getItemId());
        data.put("itemTemplateId", itemSnapshot.getItemTemplateId());
        data.put("quantity", productNumber);

        groupOrder.add(data);
        groupOrders.put(counter, groupOrder);
    }

    private void pushCourierMessage(OrderEntry orderEntry) {
        final String msgTitle = "您有一个新的配送订单";
        final String msgContent = "【" + orderEntry.getShippingNickName() + "】的订单";
        Runnable runnable = () -> {
            List<MarketRelationship> marketRelationships = dataAccessFactory.getMarketDataAccessManager().getRelationships(orderEntry.getShippingPassportId(), MarketRelationshipTypeEnum.COURIER.getKey());
            if (CommonUtils.isEmpty(marketRelationships)) {
                return;
            }
            Date date = new Date();
            Map<String, String> messageEntry = new HashMap<>();
            messageEntry.put("type", "1");
            messageEntry.put("orderType", String.valueOf(orderEntry.getType()));
            messageEntry.put("orderSequenceNumber", orderEntry.getOrderSequenceNumber());

            // 移除原来所有的推送记录(不管是否存在)
            dataAccessFactory.getOrderDataAccessManager().removeUnAcceptLoggers(orderEntry.getOrderSequenceNumber());
            String[] targets = new String[marketRelationships.size()];
            for (int i = 0; i < marketRelationships.size(); i++) {
                MarketRelationship marketRelationship = marketRelationships.get(i);
                targets[i] = marketRelationship.getK();
                // 建立推送记录
                dataAccessFactory.getOrderDataAccessManager().createUnAcceptLogger(orderEntry.getOrderSequenceNumber(), Long.parseLong(marketRelationship.getK()), orderEntry.getShippingPassportId());
            }
            // 执行推送
            JPushClient pushClient = JPushConfig.initialJPushClient(ConfigFactory.getXMarketConfig().getJPushAppSecret(), ConfigFactory.getXMarketConfig().getJPushAppKey());
            String pushResult = JPushService.pushMessage(pushClient, msgTitle, msgContent, null, messageEntry, targets);

            for (MarketRelationship marketRelationship : marketRelationships) {
                dataAccessFactory.getOrderDataAccessManager().createOrderPushedLogger(orderEntry.getOrderSequenceNumber(), Long.parseLong(marketRelationship.getK()), orderEntry.getType(), msgTitle, msgContent,
                        pushResult, date, GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
            }
        };
        AsyncScheduledService.submitImmediateRemoteNotifyTask(runnable);
    }

    private class ItemLengthComparator implements Comparator<OrderItemSnapshot> {

        @Override
        public int compare(OrderItemSnapshot o1, OrderItemSnapshot o2) {
            ItemTemplate i1 = ItemDataCacheService.getItemTemplate(o1.getItemTemplateId());
            ItemTemplate i2 = ItemDataCacheService.getItemTemplate(o2.getItemTemplateId());

            if (i1.minimumLength() < i2.minimumLength()) {
                return -1;
            }
            return i1.minimumLength() > i2.minimumLength() ? 1 : 0;
        }
    }
}