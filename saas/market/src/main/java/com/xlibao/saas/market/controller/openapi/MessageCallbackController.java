package com.xlibao.saas.market.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/8/10.
 */
@Controller
@RequestMapping(value = "/market/message/callback")
public class MessageCallbackController {

    @Autowired
    private MessageService messageService;

    @ResponseBody
    @RequestMapping(value = "notifyShipment")
    public JSONObject notifyShipment() {
        return messageService.notifyShipment();
    }

    @ResponseBody
    @RequestMapping(value = "notifyShelvesData")
    public JSONObject notifyShelvesData() {
        return messageService.notifyShelvesData();
    }

    @ResponseBody
    @RequestMapping(value = "notifyOrderData")
    public JSONObject notifyOrderData() {
        return messageService.notifyOrderData();
    }

    @ResponseBody
    @RequestMapping(value = "notifyRefund")
    public JSONObject notifyRefund() {
        return messageService.notifyRefund();
    }

    @ResponseBody
    @RequestMapping(value = "notifyPickUp")
    public JSONObject notifyPickUp() {
        return messageService.notifyPickUp();
    }

    @ResponseBody
    @RequestMapping(value = "askOrderPickUp")
    public JSONObject askOrderPickUp() {
        return messageService.askOrderPickUp();
    }
}