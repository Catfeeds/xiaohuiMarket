package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.passport.service.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/7.
 */
@Controller
@RequestMapping(value = "/passport/sms")
public class SmsController extends BasicWebService {

    @Autowired
    private SmsService smsService;

    /**
     * <pre>
     *     <b>获取验证码</b>
     *
     *     访问地址：http://domainName/passport/sms/requestVerificationCode
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>phoneNumber</b> - String 手机号 必填参数
     *     <b>type</b> - int 验证码类型 非必填参数；具体参考{@linkplain com.xlibao.common.constant.sms.SmsCodeTypeEnum}
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "requestVerificationCode")
    public JSONObject requestVerificationCode() {
        return smsService.sendSmsCode();
    }

    /**
     * <pre>
     *     <b>获取验证码</b>
     *
     *     访问地址：http://domainName/sms/verifySmsCode
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>phone</b> - String 手机号，必填参数。
     *          <b>smsCode</b> - String 验证码，必填参数。
     *          <b>smsType</b> - int 验证码类型，非必填参数；具体参考{@linkplain com.xlibao.common.constant.sms.SmsCodeTypeEnum}。
     *
     *     <b>返回：</b>仅返回成功或失败的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "verifySmsCode")
    public JSONObject verifySmsCode() {
        String phone = getUTF("phone");
        String smsCode = getUTF("smsCode");
        int smsType = getIntParameter("smsType");
        return smsService.verifySmsCode(phone, smsCode, smsType);
    }
}