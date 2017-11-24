package com.xlibao.saas.market.service.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.saas.market.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/3/26.
 */
public class BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(BasicRemoteService.class);

    protected static Map<String, String> initialParameter() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("partnerId", ConfigFactory.getXMarketConfig().getPartnerId());
        parameters.put("appId", ConfigFactory.getXMarketConfig().getOrderAppId());
        return parameters;
    }

    protected static JSONObject postOrderMsg(String url, Map<String, String> parameters) {
        try {
            // 填充签名参数 参数key为：sign 参与的密钥字段名：key
            CommonUtils.fillSignature(parameters, ConfigFactory.getXMarketConfig().getOrderAppkey());
            String data = HttpRequest.post(ConfigFactory.getDomainNameConfig().orderRemoteURL + url, parameters);

            JSONObject response = JSONObject.parseObject(data);
            int code = response.getIntValue("code");
            if (code != 0) {
                throw new XlibaoRuntimeException(code, response.getString("msg"));
            }
            return response;
        } catch (Exception ex) {
            if (ex instanceof XlibaoRuntimeException) {
                throw ex;
            }
            logger.error("执行" + url + "操作发生异常，请求参数为：" + parameters + "，异常信息：" + ex.getMessage(), ex);
            throw new XlibaoRuntimeException("远程服务器无响应");
        }
    }
}