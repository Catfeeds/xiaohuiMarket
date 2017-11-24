package com.xlibao.saas.market.manager.utils;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sun.tools.javac.util.Constants.format;

/**
 * Created by Administrator on 2017/8/11 0011.
 */
public class Utils {

    public static void main(String[] args) {
        double price2 = (double) 100 / (double) 100;
        System.err.println("price2 = " + changePrice(price2));
    }

    /**
     * 从json里删除指定价格的key，并将key转为元重新存进去
     */
    public static void changePrice(JSONObject json, String key) {
        Object obj = json.remove(key);
        if (obj != null) {
            long price = Long.parseLong(obj.toString());
            double price2 = (double) price / (double) 100;
            //取2位
            json.put(key, changePrice(price2));
        } else {
            json.put(key, "0");
        }
    }

    private static String changePrice(double price) {
        String vs = String.valueOf(price);
        //拿到点的位置
        int pointIndex = vs.indexOf(".");
        StringBuilder endSb = new StringBuilder();
        if (pointIndex != -1) {
            if (pointIndex + 1 < vs.length()) {
                char c = vs.charAt(pointIndex + 1);
                if (c != '0') {
                    endSb.append('.').append(c);
                }
            }
            if (pointIndex + 2 < vs.length()) {
                char c = vs.charAt(pointIndex + 2);
                if (c != '0') {
                    if (endSb.length() < 1) {
                        endSb.append('.').append('0');
                    }
                    endSb.append(c);
                }
            }
            return vs.substring(0, pointIndex) + endSb.toString();
        }
        return vs;

//
//
//        StringBuilder sb = new StringBuilder();
//        boolean point = false;
//        int pointCount = 0;
//        for (int i = 0; i < vs.length(); i++) {
//            char c = vs.charAt(i);
//            if (c == '.') {
//                point = true;
//                sb.append(c);
//                continue;
//            }
//            if (point) {
//                pointCount++;
//                //只截2位
//                if (pointCount == 3) {
//                    break;
//                }
//            }
//            sb.append(c);
//        }
//        //如果未够2位数
//        if (pointCount != 2) {
//            int c = 2 - pointCount;
//            for (int i = 0; i < c; i++) {
//                sb.append("0");
//            }
//        }
//        return sb.toString();
    }

    /**
     * 从json里删除指定日期毫秒值，并将key转为中文日期值
     */
    public static void changeData(JSONObject json, String key) {
        Object obj = json.remove(key);
        String str = obj != null ? obj.toString() : null;
        if (obj != null && str.length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sdf.format(new Date(Long.parseLong(str)));
            json.put(key, date);
        } else {
            json.put(key, "0");
        }
    }

    public static void appendStr(StringBuilder sb, String key, String value) {
        if (value != null && value.length() > 0) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);
        }
    }

    public static void appendInt(StringBuilder sb, String key, int value) {
        if (value != -1) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);
        }
    }

    public static void appendLong(StringBuilder sb, String key, long value) {
        if (value != -1) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);
        }
    }
}
