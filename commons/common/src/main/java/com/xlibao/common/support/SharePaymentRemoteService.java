package com.xlibao.common.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/5/24.
 */
public class SharePaymentRemoteService extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(SharePaymentRemoteService.class);

    public static JSONObject paymentOrder(Map<String, String> signParameters, String partnerId, String appId, String appKey, String urlPrefix, String paymentType) {
        signParameters.put("partnerId", partnerId);
        signParameters.put("appId", appId);
        signParameters.put("randomParameter", DefineRandom.randomString(32));

        CommonUtils.fillSignature(signParameters, appKey);

        String parameter = HttpRequest.post(urlPrefix + "payment/unifiedOrder", signParameters);

        JSONObject response = JSONObject.parseObject(parameter);

        logger.info("请求支付订单结果：" + response);

        if (response.getIntValue("code") == 0 && PaymentTypeEnum.BALANCE.getKey().equals(paymentType)) {
            return success(fillBalanceParameter(partnerId, appId, appKey, response.getJSONObject("response").getString("prePaymentId"), DefineRandom.randomChar(32)));
        }
        return response;
    }

    public static JSONObject fillBalanceParameter(String partnerId, String appId, String appKey, String prePaymentId, String randomParameter) {
        JSONObject parameters = new JSONObject();
        Map<String, String> signParameters = new HashMap<>();
        // 统一支付中心分配的合作商户ID
        parameters.put("partnerId", partnerId);
        signParameters.put("partnerId", partnerId);
        // 统一支付中心分配的AppId
        parameters.put("appId", appId);
        signParameters.put("appId", appId);
        // 统一支付中心进行余额支付时生成的预支付ID
        parameters.put("prePaymentId", prePaymentId);
        signParameters.put("prePaymentId", prePaymentId);
        // 随机参数，保证消息的安全性
        parameters.put("randomParameter", randomParameter);
        signParameters.put("randomParameter", randomParameter);
        // 当前时间戳 -- 毫秒
        parameters.put("timeStamp", System.currentTimeMillis());
        signParameters.put("timeStamp", String.valueOf(System.currentTimeMillis()));

        CommonUtils.fillSignature(signParameters, appKey);
        parameters.put("sign", signParameters.get("sign"));

        return parameters;
    }

    public static JSONObject refund(String partnerId, String appId, String appKey, String orderSequenceNumber, String urlPrefix) {
        Map<String, String> signParameters = new HashMap<>();
        signParameters.put("partnerId", partnerId);
        signParameters.put("appId", appId);
        signParameters.put("randomParameter", DefineRandom.randomString(32));
        signParameters.put("partnerSequenceNumber", orderSequenceNumber);

        CommonUtils.fillSignature(signParameters, appKey);

        String response = HttpRequest.post(urlPrefix + "payment/refund", signParameters);

        return JSONObject.parseObject(response);
    }
}