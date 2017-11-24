package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.passport.ClientTypeEnum;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.config.LogicConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/marketmanager/passport")
public class PassportManagerController extends BaseController {

    @RequestMapping("/main")
    public String main(ModelMap map) {
        return jumpPage(map, LogicConfig.FTL_MAIN, LogicConfig.TAB_INDEX, LogicConfig.TAB_INDEX);
    }
}