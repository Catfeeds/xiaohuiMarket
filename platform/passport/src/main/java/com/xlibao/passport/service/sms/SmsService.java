package com.xlibao.passport.service.sms;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/2/7.
 */
public interface SmsService {

    JSONObject sendSmsCode();

    JSONObject verifySmsCode(String phone, String smsCode, int smsType);

    void sendSmsVerifyCode(String phone, int type, String smsContent, String smsCode);
}