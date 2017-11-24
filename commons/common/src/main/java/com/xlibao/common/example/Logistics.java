package com.xlibao.common.example;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.http.HttpRequest;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/28.
 */
public class Logistics {

    public static void main(String[] args) throws Exception {
        System.out.println(URLEncoder.encode("{\"transCreateTime\":1489238751000,\"discountAmount\":0,\"transSequenceNumber\":\"P90820170311212551846020241\",\"extendParameter\":\"2\",\"paymentType\":\"BALANCE\",\"useConpon\":0,\"transType\":8,\"channelTradeNumber\":\"P90820170311212551846020241\",\"channelUserId\":\"100000\",\"partnerUserId\":\"100000\",\"channelUserName\":\"100000\",\"transUnitAmount\":1257600,\"partnerId\":\"xlb908100000\",\"transTotalAmount\":1257600,\"paymentTime\":1489238915573,\"transStatus\":8,\"transNumber\":1,\"channelRemark\":\"\"}"));
        // alideliver("884336775613909383");
//        String param = "{\"com\":\"shunfeng\",\"num\":\"974153766160\"}";
//        String customer = "1FFBA58249A3D613E8AC8DC0257DB631";
//        String key = "jFsygkZd4368";
//        String sign = CommonUtils.md5(param + key + customer).toUpperCase();
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("param", param);
//        params.put("sign", sign);
//        params.put("customer", customer);
//        String response;
//        try {
//            response = HttpUtils.getStr("http://poll.kuaidi100.com/poll/query.do", params);
//            System.out.println(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * <pre>
     *     <b>阿里云服务市场的一个用于查看物流信息的方法</b>
     * </pre>
     */
    public static JSONObject alideliver(String logisticsOrderSequence) {
        String host = "http://ali-deliver.showapi.com/showapi_expInfo";
        // String path = "/fetchCom";
        Map<String, String> headers = new HashMap<>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE a2c7d45f096f4972b12b246c95234ce4");
        Map<String, String> querys = new HashMap<>();
        querys.put("com", "auto");
        querys.put("nu", logisticsOrderSequence);
        try {
            String data = HttpRequest.doGet(host, headers, querys);
            JSONObject response = JSONObject.parseObject(data);

            // int status = response.getIntValue("showapi_res_code");
            System.out.println(response.getJSONObject("showapi_res_body"));
            // 获取response的body
            return response.getJSONObject("showapi_res_body");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
