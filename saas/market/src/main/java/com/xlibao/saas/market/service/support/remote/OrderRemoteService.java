package com.xlibao.saas.market.service.support.remote;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.support.ShareOrderRemoteService;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.config.ConfigFactory;
import com.xlibao.saas.market.service.support.BasicRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/26.
 */
public class OrderRemoteService extends BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(OrderRemoteService.class);

    public static JSONObject prepareCreateOrder(long roleId, OrderTypeEnum orderTypeEnum) {
        Map<String, String> parameters = initialParameter();

        parameters.put("partnerUserId", String.valueOf(roleId));
        parameters.put("orderType", String.valueOf(orderTypeEnum.getKey()));

        JSONObject response = postOrderMsg("order/prepareCreateOrder", parameters);
        logger.info("预下单结果：" + response);
        return response;
    }

    public static JSONArray generateOrder(String sequenceNumber, long passportId, List<OrderEntry> orders) {
        JSONArray orderArray = new JSONArray();
        for (OrderEntry orderEntry : orders) {
            JSONObject order = JSONObject.parseObject(JSONObject.toJSONString(orderEntry));
            orderArray.add(order);
        }
        Map<String, String> parameters = initialParameter();

        parameters.put("partnerUserId", String.valueOf(passportId));
        parameters.put("sequenceNumber", sequenceNumber);
        parameters.put("orders", orderArray.toJSONString());

        JSONObject response = postOrderMsg("order/generateOrder", parameters);
        logger.info("下单结果：" + response);

        response = response.getJSONObject("response");
        return response.getJSONArray("orderArray");
    }

    public static JSONObject modifyReceivingData(String orderSequenceNumber, String currentLocation, byte collectingFees, String receiptProvince, String receiptCity, String receiptDistrict, String receiptAddress,
                                                 String receiptNickName, String receiptPhone, String receiptLocation, String remark) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderSequenceNumber", orderSequenceNumber);
        parameters.put("currentLocation", currentLocation);
        parameters.put("collectingFees", String.valueOf(collectingFees));
        parameters.put("receiptProvince", receiptProvince);
        parameters.put("receiptCity", receiptCity);
        parameters.put("receiptDistrict", receiptDistrict);
        parameters.put("receiptAddress", receiptAddress);
        parameters.put("receiptNickName", receiptNickName);
        parameters.put("receiptPhone", receiptPhone);
        parameters.put("receiptLocation", receiptLocation);
        parameters.put("remark", remark);

        JSONObject response = postOrderMsg("order/modifyReceivingData", parameters);
        logger.info("修改配送数据：" + response);

        return response;
    }

    public static JSONObject refreshOrderStatus(String orderSequenceNumber, int permissionType, String operationPassportId, int targetStatus, String validStatusSet) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderSequenceNumber", orderSequenceNumber);
        parameters.put("permissionType", String.valueOf(permissionType));
        parameters.put("operationPassportId", operationPassportId);
        parameters.put("targetStatus", String.valueOf(targetStatus));
        parameters.put("validStatusSet", validStatusSet);

        JSONObject response = postOrderMsg("order/refreshOrderStatus", parameters);
        logger.info("订单状态刷新结果：" + response);

        return response;
    }

    public static JSONObject distributionOrder(long orderId, long courierPassportId, byte self, boolean refreshOrderStatus) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderId", String.valueOf(orderId));
        parameters.put("courierPassportId", String.valueOf(courierPassportId));
        parameters.put("self", String.valueOf(self));
        parameters.put("refreshOrderStatus", String.valueOf(refreshOrderStatus ? GlobalAppointmentOptEnum.LOGIC_TRUE.getKey() : GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()));

        JSONObject response = postOrderMsg("order/distributionOrder", parameters);
        logger.info("配送结果：" + response);

        return response;
    }

    public static JSONObject confirmOrder(long orderId, long passportId, byte self) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderId", String.valueOf(orderId));
        parameters.put("partnerUserId", String.valueOf(passportId));
        parameters.put("self", String.valueOf(self));

        JSONObject response = postOrderMsg("order/confirmOrder", parameters);
        logger.info("确认结果：" + response);

        return response;
    }

    public static OrderEntry getOrder(long orderId) {
        return ShareOrderRemoteService.getOrder(ConfigFactory.getXMarketConfig().getPartnerId(), ConfigFactory.getXMarketConfig().getOrderAppId(), ConfigFactory.getXMarketConfig().getOrderAppkey(),
                ConfigFactory.getDomainNameConfig().orderRemoteURL, orderId);
    }

    public static OrderEntry getOrder(String orderSequenceNumber) {
        return ShareOrderRemoteService.getOrder(ConfigFactory.getXMarketConfig().getPartnerId(), ConfigFactory.getXMarketConfig().getOrderAppId(), ConfigFactory.getXMarketConfig().getOrderAppkey(),
                ConfigFactory.getDomainNameConfig().orderRemoteURL, orderSequenceNumber);
    }

    public static List<OrderEntry> getOrders(List<String> orderSequences) {
        StringBuilder orderSequenceSet = new StringBuilder();
        for (String sequenceNumber : orderSequences) {
            orderSequenceSet.append("'").append(sequenceNumber).append("'").append(CommonUtils.SPLIT_COMMA);
        }
        if (orderSequenceSet.toString().endsWith(CommonUtils.SPLIT_COMMA)) {
            orderSequenceSet.deleteCharAt(orderSequenceSet.length() - 1);
        }
        return ShareOrderRemoteService.getOrderForSequenceSet(ConfigFactory.getXMarketConfig().getPartnerId(), ConfigFactory.getXMarketConfig().getOrderAppId(), ConfigFactory.getXMarketConfig().getOrderAppkey(),
                ConfigFactory.getDomainNameConfig().orderRemoteURL, orderSequenceSet.toString());
    }

    public static List<OrderEntry> showOrders(long passportId, long appointFriendPassportId, byte target, int roleType, String orderStatusSet, int type, int pageIndex, int pageSize) {
        Map<String, String> parameters = initialParameter();

        parameters.put("targetUserId", String.valueOf(passportId));
        parameters.put("target", String.valueOf(target));
        parameters.put("appointFriendPassportId", String.valueOf(appointFriendPassportId));
        parameters.put("roleType", String.valueOf(roleType));
        parameters.put("orderStatusSet", orderStatusSet);
        parameters.put("type", String.valueOf(type));
        parameters.put("pageIndex", String.valueOf(pageIndex));
        parameters.put("pageSize", String.valueOf(pageSize));

        JSONObject response = postOrderMsg("order/showOrders", parameters);

        response = response.getJSONObject("response");
        JSONArray orderArray = response.getJSONArray("orderArray");
        List<OrderEntry> orders = new ArrayList<>();
        for (int i = 0; i < orderArray.size(); i++) {
            orders.add(JSONObject.parseObject(orderArray.getString(i), OrderEntry.class));
        }
        return orders;
    }

    public static void modifyItemSnapshot(long orderId, long itemId, long normalPrice, long discountPrice, int normalQuantity, int discountQuantity, long totalPrice) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderId", String.valueOf(orderId));
        parameters.put("itemId", String.valueOf(itemId));
        parameters.put("normalPrice", String.valueOf(normalPrice));
        parameters.put("discountPrice", String.valueOf(discountPrice));
        parameters.put("normalQuantity", String.valueOf(normalQuantity));
        parameters.put("discountQuantity", String.valueOf(discountQuantity));
        parameters.put("totalPrice", String.valueOf(totalPrice));

        JSONObject response = postOrderMsg("order/modifyItemSnapshot", parameters);

        logger.info("修改商品快照结果：" + response);
    }

    public static void correctOrderPrice(long orderId, long actualPrice, long totalPrice, long discountPrice, long distributionFee, int deliverType) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderId", String.valueOf(orderId));
        parameters.put("actualPrice", String.valueOf(actualPrice));
        parameters.put("totalPrice", String.valueOf(totalPrice));
        parameters.put("discountPrice", String.valueOf(discountPrice));
        parameters.put("distributionFee", String.valueOf(distributionFee));
        parameters.put("deliverType", String.valueOf(deliverType));

        JSONObject response = postOrderMsg("order/correctOrderPrice", parameters);

        logger.info("修正订单价格结果：" + response);
    }

    public static JSONObject cancelOrder(long orderId, String partnerUserId, int beforeStatus, String reason) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderId", String.valueOf(orderId));
        parameters.put("partnerUserId", partnerUserId);
        parameters.put("beforeStatus", String.valueOf(beforeStatus));
        parameters.put("reason", reason);
        return postOrderMsg("order/cancelOrder", parameters);
    }

    public static JSONObject findInvalidOrderSize(int orderType, int matchStatus, long timeout) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderType", String.valueOf(orderType));
        parameters.put("matchStatus", String.valueOf(matchStatus));
        parameters.put("timeout", String.valueOf(timeout));

        return postOrderMsg("order/findInvalidOrderSize", parameters);
    }

    public static JSONObject batchResetOverdueOrderStatus(int orderType, int matchStatus, int expectStatus, long timeout) {
        Map<String, String> parameters = initialParameter();

        parameters.put("orderType", String.valueOf(orderType));
        parameters.put("matchStatus", String.valueOf(matchStatus));
        parameters.put("expectStatus", String.valueOf(expectStatus));
        parameters.put("timeout", String.valueOf(timeout));

        return postOrderMsg("order/batchResetOverdueOrderStatus", parameters);
    }

    public static JSONObject acceptOrder(long passportId, long orderId, int forceAppoint) {
        Map<String, String> parameters = initialParameter();

        parameters.put("courierPassportId", String.valueOf(passportId));
        parameters.put("orderId", String.valueOf(orderId));
        parameters.put("forceAppoint", String.valueOf(forceAppoint));

        return postOrderMsg("order/acceptOrder", parameters);
    }
}