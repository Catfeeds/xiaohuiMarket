package com.xlibao.saas.market.service.order;

import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.listener.OrderEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/15.
 */
@Transactional
@Component
public class OrderEventListenerManager {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListenerManager.class);

    private static final Map<String, OrderEventListener> ORDER_EVENT_LISTENER_MAP = new LinkedHashMap<>();

    public void registerOrderEventListener(String name, OrderEventListener orderEventListener) {
        logger.info(name + "注册订单动作监听器");
        ORDER_EVENT_LISTENER_MAP.put(name, orderEventListener);
    }

    public void notifyCreatedOrder(OrderEntry orderEntry) {
        for (Map.Entry<String, OrderEventListener> entrys : ORDER_EVENT_LISTENER_MAP.entrySet()) {
            try {
                entrys.getValue().notifyCreatedOrder(orderEntry);
            } catch (Exception ex) {
                logger.error("【" + orderEntry.getOrderSequenceNumber() + "】通知订单创建完成的后续操作发生了异常，正在执行的系统：" + entrys.getKey(), ex);
            }
        }
    }

    public void notifyOrderPayment(OrderEntry orderEntry) {
        for (Map.Entry<String, OrderEventListener> entrys : ORDER_EVENT_LISTENER_MAP.entrySet()) {
            try {
                entrys.getValue().notifyOrderPayment(orderEntry);
            } catch (Exception ex) {
                logger.error("【" + orderEntry.getOrderSequenceNumber() + "】通知订单支付完成的后续操作发生了异常，正在执行的系统：" + entrys.getKey(), ex);
            }
        }
    }

    public void notifyAcceptedOrder(OrderEntry orderEntry) {
        for (Map.Entry<String, OrderEventListener> entrys : ORDER_EVENT_LISTENER_MAP.entrySet()) {
            try {
                entrys.getValue().notifyAcceptedOrder(orderEntry);
            } catch (Exception ex) {
                logger.error("【" + orderEntry.getOrderSequenceNumber() + "】通知订单接单完成的后续操作发生了异常，正在执行的系统：" + entrys.getKey(), ex);
            }
        }
    }

    public void notifyDeliveredOrderEntry(OrderEntry orderEntry) {
        for (Map.Entry<String, OrderEventListener> entrys : ORDER_EVENT_LISTENER_MAP.entrySet()) {
            try {
                entrys.getValue().notifyDeliveredOrderEntry(orderEntry);
            } catch (Exception ex) {
                logger.error("【" + orderEntry.getOrderSequenceNumber() + "】通知订单开始配送的后续操作发生了异常，正在执行的系统：" + entrys.getKey(), ex);
            }
        }
    }

    public void notifyConfirmedOrderEntry(OrderEntry orderEntry) {
        for (Map.Entry<String, OrderEventListener> entrys : ORDER_EVENT_LISTENER_MAP.entrySet()) {
            try {
                entrys.getValue().notifyConfirmedOrderEntry(orderEntry);
            } catch (Exception ex) {
                logger.error("【" + orderEntry.getOrderSequenceNumber() + "】通知订单开始配送的后续操作发生了异常，正在执行的系统：" + entrys.getKey(), ex);
            }
        }
    }

    public void notifyCanceledOrderEntry(OrderEntry orderEntry) {
        for (Map.Entry<String, OrderEventListener> entrys : ORDER_EVENT_LISTENER_MAP.entrySet()) {
            try {
                entrys.getValue().notifyCanceledOrderEntry(orderEntry);
            } catch (Exception ex) {
                logger.error("【" + orderEntry.getOrderSequenceNumber() + "】通知订单取消的后续操作发生了异常，正在执行的系统：" + entrys.getKey(), ex);
            }
        }
    }
}