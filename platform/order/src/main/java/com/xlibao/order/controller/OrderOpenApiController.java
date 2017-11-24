package com.xlibao.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.order.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/3/27.
 */
@Controller
@RequestMapping("/order/openapi")
public class OrderOpenApiController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "callbackPaymentOrder")
    public JSONObject callbackPaymentOrder() {
        return orderService.callbackPaymentOrder();
    }
}