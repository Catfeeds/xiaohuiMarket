package com.xlibao.saas.market.listener;

import com.xlibao.metadata.order.OrderEntry;

/**
 * @author chinahuangxc on 2017/7/14.
 */
public interface PaymentEventListener {

    void notifyPayment(OrderEntry order, String paymentType);
}