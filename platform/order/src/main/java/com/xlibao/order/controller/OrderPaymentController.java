package com.xlibao.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.order.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/7/31.
 */
@Controller
@RequestMapping("/order/payment")
public class OrderPaymentController {

    @Autowired
    private OrderService orderService;

    /**
     * <pre>
     *     <b>统一下单接口(支付专用)</b>
     * </pre>
     *
     * @deprecated 仅提供内部服务使用
     */
    @ResponseBody
    @RequestMapping(value = "unifiedPayment")
    public JSONObject unifiedPayment() {
        return orderService.unifiedPayment();
    }

    @ResponseBody
    @RequestMapping(value = "applyRefund")
    public JSONObject applyRefund() {
        return orderService.applyRefund();
    }

    @ResponseBody
    @RequestMapping(value = "refund")
    public JSONObject refund() {
        return orderService.refund();
    }
}