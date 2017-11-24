package com.xlibao.saas.market.manager.controller.open;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.passport.ClientTypeEnum;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.config.LogicConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhumg on 9/7.
 */
@Controller
@RequestMapping(value = "/marketmanager/passportopen")
public class PassportOpenController extends BaseController {

    @ResponseBody
    @RequestMapping("/login")
    public JSONObject login() {

        String userName = getUTF("userName");
        String passWord = getUTF("passWord");

        String url = ConfigFactory.getDomainNameConfig().passportRemoteURL
                + "passport/loginPassport?username=" + userName + "&password=" + passWord
                + "&deviceType=" + DeviceTypeEnum.DEVICE_TYPE_HTML.getKey()
                + "&clientType=" + ClientTypeEnum.BACKSTAGE.getKey()
                + "&versionIndex=1";
        //登陆
        String json = HttpRequest.get(url);

        JSONObject response = JSONObject.parseObject(json);
        return response;
    }

    @RequestMapping("/index")
    public String index() {
        return LogicConfig.FTL_LOGIN;
    }

    @RequestMapping("/register")
    public String register() {
        return LogicConfig.FTL_LOGIN;
    }

    @ResponseBody
    @RequestMapping("/registerTo")
    public JSONObject registerTo() {
        return null;
    }

    @RequestMapping("/findpassport")
    public String findpassport(ModelMap map) {
        return LogicConfig.FTL_LOGIN;
    }

    @ResponseBody
    @RequestMapping("/findpassportTo")
    public JSONObject findpassport() {
        return null;
    }

}
