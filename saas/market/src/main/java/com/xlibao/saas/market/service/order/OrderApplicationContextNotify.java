package com.xlibao.saas.market.service.order;

import com.xlibao.saas.market.service.payment.PaymentEventListenerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/26.
 */
@Component
public class OrderApplicationContextNotify {

    private static final String MODULE_NAME = "order";

    @Autowired
    private OrderEventListenerManager orderEventListenerManager;
    @Autowired
    private InternalOrderEventListenerImpl internalOrderEventListener;

    @Autowired
    private PaymentEventListenerManager paymentEventListenerManager;
    @Autowired
    private OrderPaymentEventListenerImpl orderPaymentEventListener;

    public void loaderNotify() {
        orderEventListenerManager.registerOrderEventListener(MODULE_NAME, internalOrderEventListener);
        paymentEventListenerManager.registerPaymentEventListener(MODULE_NAME, orderPaymentEventListener);
    }
}