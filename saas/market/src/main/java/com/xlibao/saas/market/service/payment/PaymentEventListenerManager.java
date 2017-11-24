package com.xlibao.saas.market.service.payment;

import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.listener.PaymentEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/15.
 */
@Transactional
@Component
public class PaymentEventListenerManager {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventListenerManager.class);

    private static final Map<String, PaymentEventListener> PAYMENT_EVENT_LISTENER_MAP = new HashMap<>();

    public void registerPaymentEventListener(String name, PaymentEventListener paymentEventListener) {
        logger.info(name + "注册支付动作监听器");
        PAYMENT_EVENT_LISTENER_MAP.put(name, paymentEventListener);
    }

    public void notifyPayment(OrderEntry orderEntry, String paymentType) {
        for (PaymentEventListener paymentEventListener : PAYMENT_EVENT_LISTENER_MAP.values()) {
            paymentEventListener.notifyPayment(orderEntry, paymentType);
        }
    }
}