package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.passport.service.passport.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/3/20.
 */
@Controller
@RequestMapping(value = "/passport/openApi")
public class OpenApiController extends BasicWebService {

    @Autowired
    private PassportService passportService;

    @ResponseBody
    @RequestMapping(value = "getPassport")
    public JSONObject getPassport() {
        long passportId = getLongParameter("passportId");
        Passport passport = passportService.getPassport(passportId);
        passport.setPassword("");

        JSONObject parameters = JSONObject.parseObject(JSONObject.toJSONString(passport));
        return success(parameters);
    }
}