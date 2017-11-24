package com.xlibao.order.service.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.order.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/3/13.
 */
public class LogisticsRemoteService extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(LogisticsRemoteService.class);

    public static JSONObject pushOrderMsg(String orderParameters, int pushType, byte target, String receiptId, String title, String content, String voice, String message) {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("order", orderParameters);
        parameters.put("pushType", String.valueOf(pushType));
        parameters.put("target", String.valueOf(target));
        parameters.put("receiptId", receiptId);
        parameters.put("title", title);
        parameters.put("content", content);
        parameters.put("voice", voice);
        parameters.put("message", CommonUtils.nullToEmpty(message));
        // TODO 必须考虑推送失败的情况 暂时预留
        try {
            String data = HttpRequest.post(ConfigFactory.getDomainNameConfig().logisticsRemoteURL + "messageController/pushOrderMsg", parameters);

            logger.info("订单推送结果：" + data);

            JSONObject response = JSONObject.parseObject(data);

            int code = response.getIntValue("code");
            if (code != 0) {
                throw new XlibaoRuntimeException(code, response.getString("msg"));
            }
            return response.getJSONObject("response");
        } catch (Throwable cause) {
            return fail("推送订单失败");
        }
    }
}