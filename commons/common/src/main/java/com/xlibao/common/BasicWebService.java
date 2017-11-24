package com.xlibao.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     <b>基础服务类</b>
 * </pre>
 *
 * @author chinahuangxc on 2016/11/9.
 */
public class BasicWebService {

    /** 通用成功标志 -- 0 */
    public static final int SUCCESS_CODE = 0;
    /** 通用成功描述 -- 请求成功 */
    private static final String SUCCESS_MSG = "请求成功";

    /** 通用失败标志 -- 1 */
    public static final int FAIL_CODE = 1;
    /** 通用失败描述 -- 请求失败 */
    public static final String FAIL_MSG = "请求失败";

    public static JSONObject success() {
        return success(SUCCESS_MSG);
    }

    public static JSONObject success(String message) {
        return success(message, new JSONObject());
    }

    public static JSONObject success(Object msg) {
        JSONObject response = new JSONObject();
        response.put("datas", msg);
        return success(response);
    }

    public static JSONObject success(JSONObject response) {
        return success(SUCCESS_MSG, response);
    }

    public static JSONObject success(String message, JSONObject response) {
        return message(SUCCESS_CODE, message, response);
    }

    public static JSONObject fail() {
        return fail(FAIL_CODE);
    }

    public static JSONObject fail(int failCode) {
        return fail(failCode, FAIL_MSG);
    }

    public static JSONObject fail(String failMsg) {
        return fail(FAIL_CODE, failMsg);
    }

    public static JSONObject fail(JSONObject response) {
        return fail(FAIL_CODE, FAIL_MSG, response);
    }

    public static JSONObject fail(int failCode, String failMsg) {
        return fail(failCode, failMsg, new JSONObject());
    }

    public static JSONObject fail(int failCode, String failMsg, JSONObject response) {
        return message(failCode, failMsg, response);
    }

    public static JSONObject message(int code, String msg, JSONObject response) {
        JSONObject message = new JSONObject();
        message.put("code", code);
        message.put("msg", msg);
        message.put("response", response);
        return message;
    }

    protected byte getByteParameter(String key) {
        String value = getParamValue(key);
        if (value == null) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        try {
            return Byte.parseByte(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为byte");
        }
    }

    protected byte getByteParameter(String key, byte defaultValue) {
        String value = getParamValue(key);
        if (value == null || value.length() < 1) {
            return defaultValue;
        }
        try {
            return Byte.parseByte(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为byte");
        }
    }

    protected short getShortParameter(String key) {
        String value = getParamValue(key);
        if (value == null) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        try {
            return Short.parseShort(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为short");
        }
    }

    protected short getShotParameter(String key, short defaultValue) {
        String value = getParamValue(key);
        if (value == null || value.length() < 1) {
            return defaultValue;
        }
        try {
            return Short.parseShort(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为short");
        }
    }

    protected int getIntParameter(String key) {
        String value = getParamValue(key);
        if (value == null) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        try {
            return Integer.parseInt(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为int");
        }
    }

    protected int getIntParameter(String key, int defaultValue) {
        String value = getParamValue(key);
        if (value == null || value.length() < 1) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为int");
        }
    }

    protected long getLongParameter(String key) {
        String value = getParamValue(key);
        if (value == null) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        try {
            return Long.parseLong(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为long");
        }
    }

    protected long getLongParameter(String key, long defaultValue) {
        String value = getParamValue(key);
        if (value == null || value.length() < 1) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为long");
        }
    }

    protected float getFloatParameter(String key) {
        String value = getParamValue(key);
        if (value == null) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        try {
            return Float.parseFloat(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为float");
        }
    }

    protected float getFloatParameter(String key, float defaultValue) {
        String value = getParamValue(key);
        if (value == null || value.length() < 1) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为float");
        }
    }

    protected double getDoubleParameter(String key) {
        String value = getParamValue(key);
        if (value == null) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        try {
            return Double.parseDouble(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为double");
        }
    }

    protected double getDoubleParameter(String key, double defaultValue) {
        String value = getParamValue(key);
        if (value == null || value.length() < 1) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (Throwable cause) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "类型出错，期望值为double");
        }
    }

    protected String getUTF(String key) {
        String value = getParamValue(key);
        if (CommonUtils.isNullString(value)) {
            throw new XlibaoIllegalArgumentException("非法参数：" + key + "不能为null");
        }
        return value;
    }

    protected String getUTF(String key, String defaultValue) {
        String value = getParamValue(key);
        if (CommonUtils.isNullString(value)) {
            return defaultValue;
        }
        return value;
    }

    protected int getPageSize() {
        return getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
    }

    protected int getPageStartIndex(int pageSize) {
        return getPageStartIndex("pageIndex", pageSize);
    }

    protected int getPageStartIndex(String key, int pageSize) {
        int pageIndex = getIntParameter(key, GlobalConstantConfig.DEFAULT_PAGE_INDEX);
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        return pageStartIndex(pageIndex, pageSize);
    }

    /**
     * <pre>
     *     <b>分页起始位置</b>
     * </pre>
     *
     * @param pageIndex 页码
     * @param pageSize  单页数量
     * @return int 起始位置
     */
    protected int pageStartIndex(int pageIndex, int pageSize) {
        return (pageIndex - 1) * pageSize;
    }

    private String getParamValue(String key) {
        String value = getHttpServletRequest().getParameter(key);
        if (CommonUtils.isNotNullString(value)) {
            value = value.trim();
        }
        return value;
    }

    protected HttpServletResponse getHttpServletResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setCharacterEncoding("UTF-8");
        return response;
    }

    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return request;
    }

    protected void getEntityParameter(Object entity) {
        Map<String, String[]> map = getHttpServletRequest().getParameterMap();
        try {
            BeanUtils.populate(entity, map);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected JSONObject getJSONParameter() {
        Map<String, String[]> requestParams = getHttpServletRequest().getParameterMap();
        Map<String, Object> parame = new HashMap<>();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();


            if (values.length == 1) {
                parame.put(name, values[0]);
            } else {
                parame.put(name, values);
            }
        }
        return JSONObject.parseObject(JSON.toJSONString(parame));
    }

    private static HttpSession getHttpSession() {
        return getHttpServletRequest().getSession();
    }

    protected void setSessionAttribute(String key, Object value) {
        getHttpSession().setAttribute(key, value);
    }

    protected static Object getSessionAttribute(String key) {
        return getHttpSession().getAttribute(key);
    }

    protected void setAccessToken(String accessToken) {
        setSessionAttribute("accessToken", accessToken);
    }

    /**
     * <pre>
     *     <b>唯一主键</b>
     *     该生成方法不能保证绝对的安全，主要取决于最后的随机算法
     * </pre>
     */
    protected static String uniquePrimaryKey() {
        StringBuilder primaryKey = new StringBuilder(GlobalConstantConfig.UNIQUE_PRIMARY_KEY_PREFIX);
        primaryKey.append(CommonUtils.defineDateFormat(System.currentTimeMillis(), "yyyyMMddHHmmssSSS"));
        primaryKey.append(DefineRandom.randomNumber(6));
        return primaryKey.toString();
    }

    /**
     * <pre>
     *     <b>唯一主键</b>
     *     由使用者确定uniqueMark的内容，一般为用户的唯一ID
     *     基本上可以保证唯一，主要取决于使用者提供的标志位与同一个标志位的并发量(如：1毫秒发起请求超过1次，这种情况绝对重复)
     * </pre>
     *
     * @param uniqueMark 标志位
     */
    public static String uniquePrimaryKey(String uniqueMark) {
        StringBuilder primaryKey = new StringBuilder();
        primaryKey.append(uniqueMark);
        primaryKey.append(DefineRandom.randomNumber(4)); // 4位
        primaryKey.append(String.valueOf(System.currentTimeMillis() + DefineRandom.random(10000, 99999)).substring(2)); // 11位
        return primaryKey.toString(); // 最长不能超过32位(主要是由于微信的限制)
    }

    protected String encryptionPassword(String password) {
        // 密码前缀 + 密码原文 + 密码后缀
        return CommonUtils.md5(GlobalConstantConfig.PASSWORD_PREFIX + password + GlobalConstantConfig.PASSWORD_SUFFIX);
    }

    protected void responseWeixinResult(String returnCode, String returnMsg) {
        String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<xml>"
                + "<return_code><![CDATA[" + returnCode + "]]></return_code>"
                + "<return_msg><![CDATA[" + returnMsg + "]]></return_msg>"
                + "</xml>";

        PrintWriter out = null;
        try {
            out = getHttpServletResponse().getWriter();
            out.write(msg); // 响应回调
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void print(HttpServletResponse response, String callbackParameter) {
        try {
            PrintWriter out = response.getWriter();
            out.print(callbackParameter);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}