package com.xlibao.order.data.mapper.order;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.order.OrderSequenceNumberStatusEnum;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.metadata.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author chinahuangxc on 2017/01/26
 */
@Component
public class OrderDataAccessManager {

    @Autowired
    private OrderSequenceMapper sequenceMapper;
    @Autowired
    private OrderEntryMapper entryMapper;
    @Autowired
    private OrderStateLoggerMapper stateLoggerMapper;
    @Autowired
    private OrderItemSnapshotMapper itemSnapshotMapper;
    @Autowired
    private OrderPushedLoggerMapper pushedLoggerMapper;
    @Autowired
    private OrderUnacceptLoggerMapper unacceptLoggerMapper;
    @Autowired
    private OrderStatusListenerMapper statusListenerMapper;

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 订单序列号访问接口(orderSequenceMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public int createOrderSequence(OrderSequence orderSequence) {
        return sequenceMapper.createOrderSequence(orderSequence);
    }

    public OrderSequence lastOrderSequence(String partnerId, String partnerUserId, OrderTypeEnum orderTypeEnum) {
        return sequenceMapper.lastOrderSequence(partnerId, partnerUserId, orderTypeEnum.getKey());
    }

    public OrderSequence findOrderSequence(String sequenceNumber) {
        return sequenceMapper.findOrderSequence(sequenceNumber);
    }

    public int updateOrderSequence(String sequenceNumber, OrderSequenceNumberStatusEnum statusEnum) {
        return sequenceMapper.updateOrderSequence(sequenceNumber, statusEnum.getKey());
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 订单序列号访问接口(orderSequenceMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 订单访问接口(orderEntryMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public int createOrderEntry(OrderEntry orderEntry) {
        int result = entryMapper.createOrderEntry(orderEntry);
        if (result <= 0) {
            return result;
        }
        List<OrderItemSnapshot> itemSnapshots = orderEntry.getItemSnapshots();
        if (!CommonUtils.isEmpty(itemSnapshots)) {
            for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
                itemSnapshot.setOrderId(orderEntry.getId());
                itemSnapshotMapper.createOrderItemSnapshot(itemSnapshot);
            }
        }
        return result;
    }

    public int modifyReceivingData(String orderSequenceNumber, String currentLocation, byte collectingFees, String receiptProvince, String receiptCity, String receiptDistrict, String receiptAddress, String receiptNickName, String receiptPhone, String receiptLocation, String remark) {
        return entryMapper.modifyReceivingData(orderSequenceNumber, currentLocation, collectingFees, receiptProvince, receiptCity, receiptDistrict, receiptAddress, receiptNickName, receiptPhone, receiptLocation, remark);
    }

    public OrderEntry getOrder(long orderId) {
        return entryMapper.getOrderEntry(orderId);
    }

    public OrderEntry getOrder(String orderSequenceNumber) {
        return entryMapper.getOrderEntryForSequenceNumber(orderSequenceNumber);
    }

    public List<OrderEntry> getOrders(String sequenceNumber) {
        return entryMapper.getOrderEntrys(sequenceNumber);
    }

    public List<OrderEntry> showConsumerOrders(String partnerId, String partnerUserId, long shippingPassportId, byte target, String orderStatusSet, int type, int pageStartIndex, int pageSize) {
        return entryMapper.showConsumerOrders(partnerId, partnerUserId, shippingPassportId, target, orderStatusSet, type, pageStartIndex, pageSize);
    }

    public List<OrderEntry> showMerchantOrders(String partnerId, long shippingPassportId, String orderStatusSet, int type, int pageStartIndex, int pageSize) {
        return entryMapper.showMerchantOrders(partnerId, shippingPassportId, orderStatusSet, type, pageStartIndex, pageSize);
    }

    public List<OrderEntry> showCourierOrders(String partnerId, long courierPassportId, String orderStatusSet, int type, int pageStartIndex, int pageSize) {
        return entryMapper.showCourierOrders(partnerId, courierPassportId, orderStatusSet, type, pageStartIndex, pageSize);
    }

    public List<OrderEntry> searchOrders(String partnerId, long passportId, String searchKeyValue, int type, int roleType, int pageStartIndex, int pageSize) {
        return entryMapper.searchOrders(partnerId, passportId, searchKeyValue, type, roleType, pageStartIndex, pageSize);
    }

    public List<OrderEntry> getOrderForSequenceSet(String orderSequenceSet) {
        return entryMapper.getOrderForSequenceSet(orderSequenceSet);
    }

    public int paymentOrder(long orderId, OrderStatusEnum orderStatusEnum, int matchBeforeStatus, String transType, int daySortNumber) {
        return entryMapper.paymentOrder(orderId, orderStatusEnum.getKey(), matchBeforeStatus, transType, daySortNumber);
    }

    public int acceptOrder(long orderId, long courierPassportId, String courierNickName, String courierPhone, OrderStatusEnum orderStatusEnum, int matchBeforeStatus) {
        return entryMapper.acceptOrder(orderId, courierPassportId, courierNickName, courierPhone, orderStatusEnum.getKey(), matchBeforeStatus);
    }

    public int updateOrderStatus(long orderId, int orderStatus, int matchBeforeStatus, int deliverStatus, int beforeDeliverStatus) {
        return entryMapper.updateOrderStatus(orderId, orderStatus, matchBeforeStatus, deliverStatus, beforeDeliverStatus);
    }

    public int correctOrderPrice(long orderId, long actualPrice, long totalPrice, long discountPrice, long distributionFee, int deliverType) {
        return entryMapper.correctOrderPrice(orderId, actualPrice, totalPrice, discountPrice, distributionFee, deliverType);
    }

    public int fillDailyRowSort(long orderId, int daySortNumber) {
        return entryMapper.fillDailyRowSort(orderId, daySortNumber);
    }

    public int refreshRefundStatus(long orderId, int refundStatus) {
        return entryMapper.refreshRefundStatus(orderId, refundStatus);
    }

    public int findInvalidOrderSize(String partnerId, int orderType, int matchStatus, String timeout) {
        return entryMapper.findInvalidOrderSize(partnerId, orderType, matchStatus, timeout);
    }

    public List<OrderEntry> findInvalidOrders(String partnerId, int orderType, int matchStatus, String timeout) {
        return entryMapper.findInvalidOrders(partnerId, orderType, matchStatus, timeout);
    }

    public int refreshOrderStatus(String orderSequenceNumber, int targetStatus, String validStatusSet) {
        return entryMapper.refreshOrderStatus(orderSequenceNumber, targetStatus, validStatusSet);
    }

    public int applyRefund(long orderId, int refundStatus, String matchStatusSet, String refundReason) {
        return entryMapper.applyRefund(orderId, refundStatus, matchStatusSet, refundReason);
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 订单访问接口(orderEntryMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 订单商品快照(orderItemSnapshotMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public int modifyItemSnapshot(long orderId, long itemId, long normalPrice, long discountPrice, int normalQuantity, int discountQuantity, long totalPrice) {
        return itemSnapshotMapper.modifyItemSnapshot(orderId, itemId, normalPrice, discountPrice, normalQuantity, discountQuantity, totalPrice);
    }

    public int shippingItem(long itemSnapshotId, int shippingQuantity) {
        return itemSnapshotMapper.shippingItem(itemSnapshotId, shippingQuantity);
    }

    public int totalDistributionItems(long itemSnapshotId) {
        return itemSnapshotMapper.totalDistributionItems(itemSnapshotId);
    }

    public int distributionItem(long itemSnapshotId, int distributionCount) {
        return itemSnapshotMapper.distributionItem(itemSnapshotId, distributionCount);
    }

    public int arriveItem(long itemSnapshotId) {
        return itemSnapshotMapper.arriveItem(itemSnapshotId);
    }


    public List<OrderItemSnapshot> getOrderItemSnapshots(long orderId) {
        return itemSnapshotMapper.getOrderItemSnapshots(orderId);
    }

    public List<OrderItemSnapshot> batchGetOrderItemSnapshots(String orderSet) {
        return itemSnapshotMapper.batchGetOrderItemSnapshots(orderSet);
    }

    public int receiptItem(long itemSnapshotId, int receiptQuantity) {
        return itemSnapshotMapper.receiptItem(itemSnapshotId, receiptQuantity);
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 订单商品快照(orderItemSnapshotMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 订单状态日志(orderStateLoggerMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public int createOrderStateLogger(OrderStateLogger orderStateLogger) {
        return stateLoggerMapper.createOrderStateLogger(orderStateLogger);
    }

    public OrderStateLogger getOrderStateLogger(long orderId, int status) {
        return stateLoggerMapper.getOrderStateLogger(orderId, status);
    }

    public List<OrderStateLogger> getOrderStateLoggers(long orderId) {
        return stateLoggerMapper.getOrderStateLoggers(orderId);
    }

    public int getRowSortStatistics(long shippingPassportId, int orderType, int status, String matchTime, long id) {
        return stateLoggerMapper.getRowSortStatistics(shippingPassportId, orderType, status, matchTime, id);
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 订单状态日志(orderStateLoggerMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 订单推送记录(pushedLoggerMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public int createPushedLogger(long orderId, long targetPassportId, int pushType, String pushTitle, String pushContent) {
        OrderPushedLogger pushedLogger = new OrderPushedLogger();
        pushedLogger.setOrderId(orderId);
        pushedLogger.setTargetPassportId(targetPassportId);
        pushedLogger.setPushType(pushType);
        pushedLogger.setPushTitle(pushTitle);
        pushedLogger.setPushMsgContent(pushContent);
        pushedLogger.setPushTime(new Date());
        pushedLogger.setPushResult("success");
        pushedLogger.setStatus(GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
        pushedLogger.setFeedbackMsgContent("");
        pushedLogger.setFeedbackStatus((int) GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
        pushedLogger.setFeedbackTime(new Date());
        return pushedLoggerMapper.createPushedLogger(pushedLogger);
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 订单推送记录(pushedLoggerMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 未接订单日志(orderUnacceptLoggerMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public int createUnacceptLogger(long orderId, long targetPassportId) {
        OrderUnacceptLogger unacceptLogger = new OrderUnacceptLogger();
        unacceptLogger.setOrderId(orderId);
        unacceptLogger.setTargetPassportId(targetPassportId);
        unacceptLogger.setPushedCount(1);
        unacceptLogger.setPushedTime(new Date());
        unacceptLogger.setLastModifyTime(new Date());

        return unacceptLoggerMapper.createUnacceptLogger(unacceptLogger);
    }

    public int unAcceptOrderSize(long courierPassportId) {
        return unacceptLoggerMapper.unAcceptOrderSize(courierPassportId);
    }

    public OrderUnacceptLogger getNewestUnacceptLogger(long courierPassportId) {
        return unacceptLoggerMapper.getNewestUnacceptLogger(courierPassportId);
    }

    public OrderUnacceptLogger getUnacceptLogger(long orderId, long passportId) {
        return unacceptLoggerMapper.getUnacceptLogger(orderId, passportId);
    }

    public List<OrderUnacceptLogger> getUnacceptLoggers(long passportId, int pageStartIndex, int pageSize) {
        return unacceptLoggerMapper.getUnacceptLoggers(passportId, pageStartIndex, pageSize);
    }

    public int removeUnacceptLoggers(long orderId) {
        return unacceptLoggerMapper.removeUnacceptLoggers(orderId);
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 未接订单日志(orderUnacceptLoggerMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 订单状态监听(statusListenerMapper) ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    public OrderStatusListener getOrderStatusListener(String partnerId, int eventType) {
        return statusListenerMapper.getOrderStatusListener(partnerId, eventType);
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 订单状态监听(statusListenerMapper) ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    public List<OrderEntry> searchPageOrders(long marketId, int orderState, String startTime, String endTime, String searchType, String searchKey, int pageSize, int pageStartIndex) {
        return entryMapper.searchPageOrders(marketId, orderState, startTime, endTime, searchType, searchKey, pageSize, pageStartIndex);
    }

    public int searchPageOrderCount(long marketId, int orderState, String startTime, String endTime, String searchType, String searchKey, int pageSize, int pageStartIndex) {
        return entryMapper.searchPageOrderCount(marketId, orderState, startTime, endTime, searchType, searchKey, pageSize, pageStartIndex);
    }
}