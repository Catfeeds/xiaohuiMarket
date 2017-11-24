package com.xlibao.passport.service.sms.channel;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.http.HttpUtils;
import com.xlibao.passport.config.ConfigFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RBPSMSMessageService {

    private static final Logger logger = Logger.getLogger(RBPSMSMessageService.class);

    // private static final String RBP_SMS_URL = "http://222.73.117.156/msg/HttpBatchSendSM";
    private static final String RBP_SMS_URL = "http://smssh1.253.com/msg/send/json";

    private static final Map<Integer, String> RBP_ERROR_CODE = new HashMap<>();

    static {
        RBP_ERROR_CODE.put(0, "提交成功");
        RBP_ERROR_CODE.put(101, "无此用户");
        RBP_ERROR_CODE.put(102, "密码错误");
        RBP_ERROR_CODE.put(103, "提交过快（提交速度超过流速限制）");
        RBP_ERROR_CODE.put(104, "系统忙（因平台侧原因，暂时无法处理提交的短信）");
        RBP_ERROR_CODE.put(105, "敏感短信（短信内容包含敏感词）");
        RBP_ERROR_CODE.put(106, "消息长度错（>536或<=0）");
        RBP_ERROR_CODE.put(107, "包含错误的手机号码");
        RBP_ERROR_CODE.put(108, "手机号码个数错（群发>50000或<=0;单发>200或<=0）");
        RBP_ERROR_CODE.put(109, "无发送额度（该用户可用短信数已使用完）");
        RBP_ERROR_CODE.put(110, "不在发送时间内");
        RBP_ERROR_CODE.put(111, "超出该账户当月发送额度限制");
        RBP_ERROR_CODE.put(112, "无此产品，用户没有订购该产品");
        RBP_ERROR_CODE.put(113, "extno格式错（非数字或者长度不对）");
        RBP_ERROR_CODE.put(115, "自动审核驳回");
        RBP_ERROR_CODE.put(116, "签名不合法，未带签名（用户必须带签名的前提下）");
        RBP_ERROR_CODE.put(117, "IP地址认证错,请求调用的IP地址不是系统登记的IP地址");
        RBP_ERROR_CODE.put(118, "用户没有相应的发送权限");
        RBP_ERROR_CODE.put(119, "用户已过期");
    }

    public static boolean sendMessage(String mobileNumbers, String messageContent) {
        if (messageContent == null || messageContent.length() <= 0) {
            throw new XlibaoIllegalArgumentException("Message content can't not null");
        }
        JSONObject parameter = new JSONObject();
        parameter.put("account", ConfigFactory.getPartner().getRbpAccount());
        parameter.put("password", ConfigFactory.getPartner().getRbpPassword());
        parameter.put("report", ConfigFactory.getPartner().isRbpReport());
        parameter.put("phone", mobileNumbers);
        parameter.put("msg", ConfigFactory.getPartner().getRbpSignName() + messageContent);

        JSONObject returnParameter = HttpUtils.post(RBP_SMS_URL, parameter, false);

        logger.info(returnParameter);

        String sequenceNumber = returnParameter.getString("msgId");

        int code = returnParameter.getIntValue("code");

        logger.info("创蓝返回结果：序列号 -- " + sequenceNumber + "；返回结果：" + RBP_ERROR_CODE.get(code) + "；结果标志位：" + code);
        // 返回成功给调用者
        return true;
    }
}