package com.xlibao.saas.market.service.message;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.order.OrderEntry;

/**
 * @author chinahuangxc on 2017/8/11.
 */
public interface MessageService {

    JSONObject notifyShipment();

    JSONObject notifyShelvesData();

    JSONObject notifyOrderData();

    JSONObject notifyRefund();

    JSONObject notifyPickUp();

    JSONObject askOrderPickUp();

    int askOrderPickUp(OrderEntry orderEntry);
}