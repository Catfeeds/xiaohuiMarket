package com.xlibao.saas.market.service.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.service.order.OrderEventListenerManager;
import com.xlibao.saas.market.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("paymentService")
public class PaymentServiceImpl extends BasicWebService implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private OrderEventListenerManager orderEventListenerManager;

    @Override
    public JSONObject notifyPaymentOrder() {
        String data = getUTF("data");

        OrderEntry orderEntry = JSONObject.toJavaObject(JSONObject.parseObject(data), OrderEntry.class);

        logger.info("接收到订单支付通知，订单编号：" + orderEntry.getOrderSequenceNumber());

        orderEventListenerManager.notifyOrderPayment(orderEntry);
        return success("success");
    }
}