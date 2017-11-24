package com.xlibao.saas.market.service.order;

import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.listener.PaymentEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/7/27.
 */
@Transactional
@Component
public class OrderPaymentEventListenerImpl implements PaymentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderPaymentEventListenerImpl.class);

    @Override
    public void notifyPayment(OrderEntry order, String paymentType) {
        // 支付完成的回调通知
    }
}