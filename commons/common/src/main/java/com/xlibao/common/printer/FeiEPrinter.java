package com.xlibao.common.printer;

import com.xlibao.common.http.HttpRequest;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class FeiEPrinter {

    private static final String URL = "http://api.feieyun.cn/Api/Open/"; // 不需要修改

    private static boolean open = false;
    private static String userName = "1392660499@qq.com"; // *必填*：账号名
    private static String userKey = "xJGLVVgJA5BHL2yx"; // *必填*: 注册账号后生成的UKEY
    // private static final String SN = "817800284"; // *必填*：打印机编号，必须要在管理后台里添加打印机之后，才能调用API

    public static void setPrinterParameters(String userName, String userKey, boolean open) {
        FeiEPrinter.userName = userName;
        FeiEPrinter.userKey = userKey;
        FeiEPrinter.open = open;
    }

    public static String print(String sequenceNumber, String content, int times) {
        if (!open) {
            return "没有开启打印功能";
        }
        String time = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", userName);
        parameters.put("stime", time);
        parameters.put("sig", signature(userName, userKey, time));
        // 固定值,不需要修改
        parameters.put("apiname", "Open_printMsg");
        parameters.put("sn", sequenceNumber);
        parameters.put("content", content);
        // 打印联数
        parameters.put("times", String.valueOf(times));

        return HttpRequest.post(URL, parameters);
    }

    /**
     * 打开注释，执行main函数即可测试
     */
    public static void main(String[] args) throws Exception {
        // 标签说明：
        // 单标签:
        // "<BR>"为换行,
        // "<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
        // "<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),
        // "<PLUGIN>"为钱箱或者外置音响指令

        // 成对标签：
        // "<CB></CB>"为居中放大一倍,
        // "<B></B>"为放大一倍,
        // "<C></C>"为居中,
        // "<L></L>"字体变高一倍
        // "<W></W>"字体变宽一倍,
        // "<QR></QR>"为二维码,
        // "<BOLD></BOLD>"为字体加粗,
        // "<RIGHT></RIGHT>"为右对齐
        String content = "<CB>测试打印</CB><BR>";
        content += "名称　　　　　 单价  数量 金额<BR>";
        content += "--------------------------------<BR>";
        content += "饭　　　　　　 1.0    1   1.0<BR>";
        content += "炒饭　　　　　 10.0   10  10.0<BR>";
        content += "蛋炒饭　　　　 10.0   10  100.0<BR>";
        content += "鸡蛋炒饭　　　 100.0  1   100.0<BR>";
        content += "番茄蛋炒饭　　 1000.0 1   100.0<BR>";
        content += "西红柿蛋炒饭　 1000.0 1   100.0<BR>";
        content += "西红柿鸡蛋炒饭 100.0  10  100.0<BR>";
        content += "备注：加辣<BR>";
        content += "--------------------------------<BR>";
        content += "合计：xx.0元<BR>";
        content += "送货地点：广州市南沙区xx路xx号<BR>";
        content += "联系电话：13888888888888<BR>";
        content += "订餐时间：2016-08-08 08:08:08<BR>";
        content += "<QR>http://www.dzist.com</QR>";

        // ===================== 方法1.打印 =====================
        // ***返回值JSON字符串***
        // 成功：{"msg":"ok","ret":0,"data":"xxxxxxx_xxxxxxxx_xxxxxxxx","serverExecutedTime":5}
        // 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
        String response = print("817800284", content, 2);

        // ===================== 方法2.查询某订单是否打印成功 =====================
        // ***返回值JSON字符串***
        // 成功：{"msg":"ok","ret":0,"data":true,"serverExecutedTime":2}//data:true为已打印,false为未打印
        // 失败：{"msg":"错误描述","ret":非0, "data":null,"serverExecutedTime":7}
        // String orderid = "xxxxxxx_xxxxxxxx_xxxxxxxx";// 订单ID，从方法1返回值data获取
        // String method2 = queryOrderState(orderid);
        // System.out.println(method2);

        // ===================== 方法3.查询指定打印机某天的订单详情 =====================
        // 成功：{"msg":"ok","ret":0,"data":{"print":6,"waiting":1},"serverExecutedTime":9}//print已打印，waiting为打印
        // 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
        //	String strdate = "2016-11-12";//注意时间格式为"yyyy-MM-dd"
        //	String method3 = queryOrderInfoByDate(strdate);
        //	System.out.println(method3);

        // ===================== 方法4.查询打印机的状态 =====================
        // ***返回的状态有如下几种***
        // 成功：{"msg":"ok","ret":0,"data":"状态","serverExecutedTime":4}
        // 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
        // String method4 = queryPrinterStatus();
        // System.out.println(method4);
    }

    // 方法2
    private static String queryOrderState(String orderId) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", userName);
        parameters.put("stime", time);
        parameters.put("sig", signature(userName, userKey, time));
        // 固定值,不需要修改
        parameters.put("apiname", "Open_queryOrderState");
        parameters.put("orderid", orderId);

        return HttpRequest.post(URL, parameters);
    }

    // 方法3
    private static String queryOrderInfoByDate(String date, String sequenceNumber) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", userName);
        parameters.put("stime", time);
        parameters.put("sig", signature(userName, userKey, time));
        // 固定值,不需要修改
        parameters.put("apiname", "Open_queryOrderInfoByDate");
        parameters.put("sn", sequenceNumber);
        parameters.put("date", date);

        return HttpRequest.post(URL, parameters);
    }

    // 方法4
    private static String queryPrinterStatus(String sequenceNumber) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", userName);
        parameters.put("stime", time);
        parameters.put("sig", signature(userName, userKey, time));
        // 固定值,不需要修改
        parameters.put("apiname", "Open_queryPrinterStatus");
        parameters.put("sn", sequenceNumber);

        return HttpRequest.post(URL, parameters);
    }

    // 生成签名字符串
    private static String signature(String user, String ukey, String time) {
        return DigestUtils.sha1Hex(user + ukey + time);
    }
}