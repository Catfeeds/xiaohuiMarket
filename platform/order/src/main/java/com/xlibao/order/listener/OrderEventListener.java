package com.xlibao.order.listener;


import com.xlibao.metadata.order.OrderEntry;

/**
 * @author chinahuangxc on 2017/2/20.
 */
public interface OrderEventListener {

    void notifyCreatedOrderEntry(OrderEntry orderEntry);

    void notifyPaymentOrderEntry(OrderEntry orderEntry, int beforeStatus);

    void notifyPushedOrderEntry(OrderEntry orderEntry, int pushType, String pushTitle, String pushContent, byte write, String... targets);

    void notifyAcceptedOrderEntry(OrderEntry orderEntry, int beforeStatus);

    void notifyDistributionOrder(OrderEntry orderEntry, int beforeStatus);

    void notifyArrivedOrderEntry(OrderEntry orderEntry, int beforeStatus);

    void notifyConfirmedOrderEntry(OrderEntry orderEntry, int beforeStatus);

    void notifyCanceledOrderEntry(OrderEntry orderEntry, int beforeStatus, boolean isAuto);

    void notifyOrderRefund(OrderEntry orderEntry, int beforeStatus);
}