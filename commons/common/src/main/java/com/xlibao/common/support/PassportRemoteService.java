package com.xlibao.common.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.metadata.passport.Passport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/23.
 */
public class PassportRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(PassportRemoteService.class);

    private static String passportRemoteServiceURL = "";

    public static void setPassportRemoteServiceURL(String passportRemoteServiceURL) {
        PassportRemoteService.passportRemoteServiceURL = passportRemoteServiceURL;
    }

    public static void signatureSecurity(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> signParameters = new HashMap<>();

        String partnerId = request.getParameter("partnerId");
        String appId = request.getParameter("appId");

        String signature = request.getParameter("sign");
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            if ("sign".equals(name)) {
                continue;
            }
            signParameters.put(name, valueStr);
        }
        // 参数排序(去除内容为空的参数)
        String parameterSort = CommonUtils.parameterSort(signParameters, new ArrayList<>());

        signatureSecurity(partnerId, appId, signature, parameterSort);
    }

    public static void signatureSecurity(String partnerId, String appId, String signature, String signatureParameters) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("partnerId", String.valueOf(partnerId));
        parameters.put("appId", appId);
        parameters.put("signature", signature);
        try {
            signatureParameters = URLEncoder.encode(signatureParameters, "utf-8");
            parameters.put("signatureParameters", signatureParameters);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String data = HttpRequest.post(passportRemoteServiceURL + "passport/partner/signatureSecurity", parameters);
        try {
            JSONObject response = JSONObject.parseObject(data);
            logger.info("签名验证结果：" + response);
            if (response == null) {
                throw new XlibaoRuntimeException("验签错误");
            }
            int code = response.getIntValue("code");
            if (code != 0) {
                throw new XlibaoRuntimeException(code, response.getString("msg"));
            }
        } catch (Exception ex) {
            throw new XlibaoRuntimeException("验签失败");
        }
    }

    public static String signatureParameters(JSONObject parameters) {
        Map<String, String> p = new HashMap<>();
        p.put("data", parameters.toJSONString());

        String result = HttpRequest.post(passportRemoteServiceURL + "passport/partner/signatureParameters", p);

        JSONObject response = JSONObject.parseObject(result);
        if (response == null) {
            throw new XlibaoRuntimeException("参数加密失败");
        }
        if (response.getIntValue("code") != 0) {
            throw new XlibaoRuntimeException(response.getIntValue("code"), response.getString("msg"));
        }
        response = response.getJSONObject("response");
        return response.getString("signature");
    }

    public static Passport getPassport(long passportId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        String data = HttpRequest.post(passportRemoteServiceURL + "passport/openApi/getPassport", parameters);

        JSONObject response = JSONObject.parseObject(data);
        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return JSONObject.parseObject(response.getString("response"), Passport.class);
    }

    public static JSONObject loginPassport(String username, String password, int deviceType, int versionIndex) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        parameters.put("deviceType", String.valueOf(deviceType));
        parameters.put("versionIndex", String.valueOf(versionIndex));

        String data = HttpRequest.post(passportRemoteServiceURL + "passport/loginPassport/", parameters);

        JSONObject response = JSONObject.parseObject(data);
        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return response;
    }

    public static String changeAccessToken(long passportId, String accessToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("accessToken", accessToken);

        String data = HttpRequest.post(passportRemoteServiceURL + "passport/changeAccessToken", parameters);

        JSONObject response = JSONObject.parseObject(data);
        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return response.getString("accessToken");
    }

    public static JSONObject verifySmsCode(String phone, String smsCode, int smsType) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("phone", phone);
        parameters.put("smsCode", smsCode);
        parameters.put("smsType", String.valueOf(smsType));
        String data = HttpRequest.post(passportRemoteServiceURL + "passport/sms/verifySmsCode", parameters);

        JSONObject response = JSONObject.parseObject(data);
        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        return response;
    }
}