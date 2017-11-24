package com.xlibao.saas.market.service.support.remote;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;
import com.xlibao.saas.market.service.support.BasicRemoteService;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/26.
 */
public class PaymentRemoteService extends BasicRemoteService {

    private static final Logger logger = Logger.getLogger(PaymentRemoteService.class);

    public static JSONObject paymentOrder(long passportId, String roleId, String partnerTradeNumber, String paymentType, String transType, long transUnitAmount, int transNumber, long transTotalAmount, String transTitle, String remark, byte useCoupon, long discountAmount, String extendParameter) {
        Map<String, String> signParameters = initialParameter();

        String randomParameter = DefineRandom.randomString(32);

        signParameters.put("passportId", String.valueOf(passportId));
        signParameters.put("paymentType", paymentType);
        signParameters.put("transType", transType);
        signParameters.put("partnerUserId", roleId);
        signParameters.put("partnerTradeNumber", partnerTradeNumber);
        signParameters.put("transUnitAmount", String.valueOf(transUnitAmount));
        signParameters.put("transNumber", String.valueOf(transNumber));
        signParameters.put("transTotalAmount", String.valueOf(transTotalAmount));
        signParameters.put("transTitle", transTitle);
        signParameters.put("remark", remark);
        signParameters.put("useCoupon", String.valueOf(useCoupon));
        signParameters.put("discountAmount", String.valueOf(discountAmount));
        signParameters.put("randomParameter", randomParameter);
        signParameters.put("extendParameter", extendParameter);

        JSONObject response = postOrderMsg("order/payment/unifiedPayment", signParameters);

        logger.info("请求支付订单结果：" + response);
        return response;
    }

    public static JSONObject applyRefund(long passportId, String orderSequenceNumber, String matchStatusSet, String title, String content) {
        JSONObject refundReason = new JSONObject();
        refundReason.put("refundType", "用户退款");
        refundReason.put("title", title);
        refundReason.put("content", CommonUtils.isNullString(content) ? "未填写退款说明" : CommonUtils.nullToEmpty(content));

        Map<String, String> parameters = initialParameter();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("orderSequenceNumber", orderSequenceNumber);
        parameters.put("matchStatusSet", matchStatusSet);
        parameters.put("refundReason", refundReason.toJSONString());

        return postOrderMsg("order/payment/applyRefund", parameters);
    }

    public static JSONObject refund(long passportId, String orderSequenceNumber) {
        Map<String, String> parameters = initialParameter();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("orderSequenceNumber", orderSequenceNumber);

        return postOrderMsg("order/payment/refund", parameters);
    }
}