package com.xlibao.common.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/3/26.
 */
public class BasicRemoteService extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(BasicRemoteService.class);

    static Map<String, String> initialParameter(String partnerId, String appId) {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("partnerId", partnerId);
        parameters.put("appId", appId);
        return parameters;
    }

    static JSONObject postOrderMsg(String url, String appkey, Map<String, String> parameters) {
        // 填充签名参数 参数key为：sign 参与的密钥字段名：key
        CommonUtils.fillSignature(parameters, appkey);
        String data = HttpRequest.post(url, parameters);
        JSONObject response = JSONObject.parseObject(data);
        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return response;
    }

    protected static JSONObject executor(String url, Map<String, String> parameters) {
        try {
            String data = HttpRequest.post(url, parameters);

            JSONObject response = JSONObject.parseObject(data);
            int code = response.getIntValue("code");
            if (code != 0) {
                throw new XlibaoRuntimeException(code, response.getString("msg"));
            }
            return response;
        } catch (Exception ex) {
            logger.error("执行" + url + "操作发生异常，请求参数为：" + parameters + "，异常信息：" + ex.getMessage(), ex);
            throw new XlibaoRuntimeException(-1, "远程操作失败");
        }
    }
}