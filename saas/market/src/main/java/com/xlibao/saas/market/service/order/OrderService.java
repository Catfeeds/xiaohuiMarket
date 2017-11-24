package com.xlibao.saas.market.service.order;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface OrderService {

    JSONObject prepareCreateOrder();

    JSONObject generateOrder();

    JSONObject modifyReceivingData();

    JSONObject paymentOrder();

    JSONObject showOrders();

    JSONObject orderDetail();

    JSONObject acceptOrder();

    JSONObject notifyAcceptedOrder();

    JSONObject deliverOrder();

    JSONObject arriveOrder();

    JSONObject refundOrder();

    JSONObject findContainerData();

    JSONObject askOrderStatus();
}