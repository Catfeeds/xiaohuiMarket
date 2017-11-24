package com.xlibao.saas.market.listener;

import com.xlibao.metadata.order.OrderEntry;

/**
 * @author chinahuangxc on 2017/7/15.
 */
public interface OrderEventListener {

    void notifyCreatedOrder(OrderEntry orderEntry);

    void notifyOrderPayment(OrderEntry orderEntry);

    void notifyAcceptedOrder(OrderEntry orderEntry);

    void notifyDeliveredOrderEntry(OrderEntry orderEntry);

    void notifyConfirmedOrderEntry(OrderEntry orderEntry);

    void notifyCanceledOrderEntry(OrderEntry orderEntry);
}