package com.xlibao.common.support;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.order.OrderEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/4/10.
 */
public class ShareOrderRemoteService extends BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(ShareOrderRemoteService.class);

    public static OrderEntry getOrder(String partnerId, String appId, String appkey, String urlPrefix, long orderId) {
        Map<String, String> parameters = initialParameter(partnerId, appId);

        parameters.put("orderId", String.valueOf(orderId));

        JSONObject response = postOrderMsg(urlPrefix + "order/getOrder", appkey, parameters);

        logger.debug("获取订单数据：" + response);
        return JSONObject.parseObject(response.getString("response"), OrderEntry.class);
    }

    public static OrderEntry getOrder(String partnerId, String appId, String appkey, String urlPrefix, String orderSequenceNumber) {
        Map<String, String> parameters = initialParameter(partnerId, appId);

        parameters.put("orderSequenceNumber", orderSequenceNumber);

        JSONObject response = postOrderMsg(urlPrefix + "order/getOrderForSequenceNumber", appkey, parameters);

        logger.debug("获取订单数据：" + response);
        return JSONObject.parseObject(response.getString("response"), OrderEntry.class);
    }

    public static List<OrderEntry> getOrderForSequenceSet(String partnerId, String appId, String appkey, String urlPrefix, String orderSequenceSet) {
        Map<String, String> parameters = initialParameter(partnerId, appId);

        parameters.put("orderSequenceSet", orderSequenceSet);

        JSONObject response = postOrderMsg(urlPrefix + "order/getOrderForSequenceSet", appkey, parameters);

        logger.debug("获取订单集合数据：" + response);

        response = response.getJSONObject("response");
        JSONArray orderArray = response.getJSONArray("orderArray");
        List<OrderEntry> orders = new ArrayList<>();
        for (int i = 0; i < orderArray.size(); i++) {
            orders.add(JSONObject.parseObject(orderArray.getString(i), OrderEntry.class));
        }
        return orders;
    }
}
