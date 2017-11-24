package com.xlibao.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import okhttp3.*;
import okhttp3.FormBody.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


/**
 * http request 工具类
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;
    private static OkHttpClient httpsClient;

    static {
        //设置单域名最大请求数20个
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(20);
        // init http
        client = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).dispatcher(dispatcher).build();

        // init https
        ConnectionSpec allSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).allEnabledTlsVersions().allEnabledCipherSuites().build();
        httpsClient = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).connectionSpecs(Collections.singletonList(allSpec)).build();
    }

    private static String getStr(String url, Map<String, Object> params) {
        return getStr(url, JSON.parseObject(JSON.toJSONString(params)), false);
    }

    public static JSONObject get(String url, JSONObject params) {
        String ret = getStr(url, params);
        return null == ret ? null : JSON.parseObject(ret, JSONObject.class);
    }

    public static String get(String url) {
        return getStr(url, null);
    }

    /**
     * get请求
     *
     * @param url      请求url
     * @param params   需要附加的参数
     * @param useHttps 使用https
     */
    private static String getStr(String url, JSONObject params, boolean useHttps) {
        try {
            long start = System.currentTimeMillis();

            url = buildGetUrl(url, params);

            Request request = new Request.Builder().url(url).build();
            Response response = useHttps ? httpsClient.newCall(request).execute() : client.newCall(request).execute();

            String ret = response.body().string();
            logger.debug("Get耗时：" + (System.currentTimeMillis() - start) + "ms URL ------> " + url + " response ------> " + ret);
            return ret;
        } catch (Exception e) {
            logger.warn("错误请求 url:" + url + ",params:" + (params == null ? "" : params.toJSONString()) + " 异常:" + e.getMessage(), e);
            return null;
        }
    }

    private static String buildGetUrl(String url, JSONObject params) {
        if (null != params) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Object> entry : params.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(0);
                if (url.contains("?")) url += "&" + sb.toString();
                else url += "?" + sb.toString();
            }
        }
        return url;
    }

    public static JSONObject post(String url, JSONObject params, boolean isFrom) {
        return post(url, params, isFrom, JSONObject.class);
    }

    /**
     * post请求
     *
     * @param url    请求url
     * @param params 参数
     * @param isFrom isFrom=true form方式请求/false body方式请求
     * @param c      返回类型
     * @return 失败返回null, 成功返回json格式字符串
     */
    public static <T> T post(String url, JSONObject params, boolean isFrom, Class<T> c) {
        String retStr = postStr(url, params, isFrom);
        try {
            return JSON.parseObject(retStr, c);
        } catch (Exception e) {
            logger.warn("错误请求 URL: " + url + ", params: " + params.toJSONString(), e);
            return null;
        }
    }

    /**
     * post请求
     *
     * @param url    地址
     * @param params 参数
     * @param isFrom 是否form编码
     */
    private static String postStr(String url, JSONObject params, boolean isFrom) {
        try {
            long start = System.currentTimeMillis();
            RequestBody body = buildBody(params, isFrom);
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            String ret = response.body().string();
            logger.debug("Post 耗时" + (System.currentTimeMillis() - start) + "ms URL ------> " + url + ", params------>" + params + ", response ------> " + ret);
            return ret;
        } catch (Exception e) {
            logger.warn("错误请求 URL: " + url + ", params: " + params.toJSONString(), e);
            return null;
        }
    }

    /**
     * 生成post body
     */
    private static RequestBody buildBody(JSONObject params, boolean isFrom) {
        if (isFrom) {
            Builder builder = new FormBody.Builder();
            for (Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return builder.build();
        } else {
            String body = buildNotNullString(params);
            return RequestBody.create(mediaType, body);
        }
    }

    private static String buildNotNullString(Object o) {
        return JSON.toJSONString(o, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteMapNullValue);
    }
}