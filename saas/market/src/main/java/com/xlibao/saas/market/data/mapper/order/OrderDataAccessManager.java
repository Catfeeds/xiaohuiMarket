package com.xlibao.saas.market.data.mapper.order;

import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.market.data.model.MarketOrderPushedLogger;
import com.xlibao.market.data.model.MarketOrderUnacceptLogger;
import com.xlibao.market.data.model.MarketSplitOrder;
import com.xlibao.saas.market.data.model.MarketOrderProperties;
import com.xlibao.saas.market.data.model.MarketOrderStatusLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author chinahuangxc on 2017/7/15.
 */
@Component
public class OrderDataAccessManager {

    @Autowired
    private MarketOrderStatusLoggerMapper orderStatusLoggerMapper;
    @Autowired
    private MarketOrderPropertiesMapper orderPropertiesMapper;
    @Autowired
    private MarketSplitOrderMapper splitOrderMapper;
    @Autowired
    private MarketOrderUnacceptLoggerMapper orderUnacceptLoggerMapper;
    @Autowired
    private MarketOrderPushedLoggerMapper orderPushedLoggerMapper;

    public int createOrderStatusLogger(String orderSequenceNumber, String mark, int notifyType, OrderStatusEnum localStatus, int remoteStatus, long remoteCompleteTime) {
        MarketOrderStatusLogger orderStatusLogger = new MarketOrderStatusLogger();
        orderStatusLogger.setOrderSequenceNumber(orderSequenceNumber);
        orderStatusLogger.setMark(mark);
        orderStatusLogger.setNotifyType(notifyType);
        orderStatusLogger.setLocalStatus(localStatus.getKey());
        orderStatusLogger.setLocalCompleteTime(new Date(System.currentTimeMillis()));
        orderStatusLogger.setRemoteStatus(remoteStatus);
        orderStatusLogger.setRemoteCompleteTime(new Date(remoteCompleteTime));
        return orderStatusLoggerMapper.createOrderStatusLogger(orderStatusLogger);
    }

    public int modifyOrderRemoteStatusLogger(String orderSequenceNumber, int notifyType, OrderStatusEnum localStatus, int remoteStatus, long remoteCompleteTime) {
        return orderStatusLoggerMapper.modifyOrderRemoteStatusLogger(orderSequenceNumber, notifyType, localStatus.getKey(), remoteStatus, new Date(remoteCompleteTime));
    }

    public MarketOrderStatusLogger getOrderStatusLogger(String orderSequenceNumber, int notifyType, OrderStatusEnum localStatus) {
        return orderStatusLoggerMapper.getOrderStatusLogger(orderSequenceNumber, notifyType, localStatus.getKey());
    }

    public MarketOrderProperties createOrderProperties(long orderId, String orderSequenceNumber, int propertiesType, String propertiesKey, String propertiesValue) {
        MarketOrderProperties orderProperties = new MarketOrderProperties();
        orderProperties.setOrderId(orderId);
        orderProperties.setOrderSequenceNumber(orderSequenceNumber);
        orderProperties.setType(propertiesType);
        orderProperties.setK(propertiesKey);
        orderProperties.setV(propertiesValue);

        orderPropertiesMapper.createOrderProperties(orderProperties);
        return orderProperties;
    }

    public MarketOrderProperties getOrderProperties(long orderId, int propertiesType, String propertiesKey) {
        return orderPropertiesMapper.getOrderProperties(orderId, propertiesType, propertiesKey);
    }

    public int updateOrderProperties(long id, String propertiesValue) {
        return orderPropertiesMapper.updateOrderProperties(id, propertiesValue);
    }

    public int createSplitOrder(long orderId, String orderSequenceNumber, String serialCode, String itemSet) {
        MarketSplitOrder splitOrder = new MarketSplitOrder();
        splitOrder.setOrderId(orderId);
        splitOrder.setOrderSequenceNumber(orderSequenceNumber);
        splitOrder.setSerialCode(serialCode);
        splitOrder.setItemSet(itemSet);

        return splitOrderMapper.createSplitOrder(splitOrder);
    }

    public List<MarketSplitOrder> getSplitOrders(long orderId) {
        return splitOrderMapper.getSplitOrders(orderId);
    }

    public int removeUnAcceptLoggers(String orderSequenceNumber) {
        return orderUnacceptLoggerMapper.removeUnAcceptLoggers(orderSequenceNumber);
    }

    public int createUnAcceptLogger(String orderSequenceNumber, long passportId, long marketId) {
        MarketOrderUnacceptLogger orderUnacceptLogger = new MarketOrderUnacceptLogger();
        orderUnacceptLogger.setTargetPassportId(passportId);
        orderUnacceptLogger.setMarketId(marketId);
        orderUnacceptLogger.setOrderSequenceNumber(orderSequenceNumber);

        return orderUnacceptLoggerMapper.createUnAcceptLogger(orderUnacceptLogger);
    }

    public MarketOrderUnacceptLogger getOrderUnacceptLogger(long passportId, String orderSequenceNumber) {
        return orderUnacceptLoggerMapper.getOrderUnacceptLogger(passportId, orderSequenceNumber);
    }

    public List<String> getAppointMarketOrderSequences(long passportId, long marketId, int pageStartIndex, int pageSize) {
        return orderUnacceptLoggerMapper.getAppointMarketOrderSequences(passportId, marketId, pageStartIndex, pageSize);
    }

    public int createOrderPushedLogger(String orderSequenceNumber, long passportId, int type, String msgTitle, String msgContent, String pushResult, Date date, byte status) {
        MarketOrderPushedLogger orderPushedLogger = new MarketOrderPushedLogger();
        orderPushedLogger.setOrderSequenceNumber(orderSequenceNumber);
        orderPushedLogger.setTargetPassportId(passportId);
        orderPushedLogger.setPushType(type);
        orderPushedLogger.setPushTitle(msgTitle);
        orderPushedLogger.setPushMsgContent(msgContent);
        orderPushedLogger.setPushResult(pushResult);
        orderPushedLogger.setPushTime(date);
        orderPushedLogger.setStatus(status);
        return orderPushedLoggerMapper.createOrderPushedLogger(orderPushedLogger);
    }
}