package com.xlibao.saas.market.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.order.OrderService;
import com.xlibao.saas.market.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/7/26.
 */
@Controller
@RequestMapping(value = "/market/order/listener")
public class OrderEventListenerController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "notifyPaymentOrder")
    public JSONObject notifyPaymentOrder() {
        // 支付通知
        return paymentService.notifyPaymentOrder();
    }

    @ResponseBody
    @RequestMapping(value = "notifyAcceptedOrder")
    public JSONObject notifyAcceptedOrder() {
        return orderService.notifyAcceptedOrder();
    }
}