package com.xlibao.saas.market.shop.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.shop.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/8/8.
 */
@Controller
@RequestMapping(value = "/marketshop/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ResponseBody
    @RequestMapping(value = "sendHardwarePush")
    public JSONObject sendHardwarePush() {
        return messageService.sendHardwarePush();
    }
}