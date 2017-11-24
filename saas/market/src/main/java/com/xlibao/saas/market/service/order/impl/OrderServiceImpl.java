package com.xlibao.saas.market.service.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.order.*;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.exception.code.OrderErrorCodeEnum;
import com.xlibao.datacache.item.ItemDataCacheService;
import com.xlibao.market.data.model.*;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderItemSnapshot;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.data.model.MarketOrderProperties;
import com.xlibao.saas.market.service.XMarketTimeConfig;
import com.xlibao.saas.market.service.item.MarketItemErrorCodeEnum;
import com.xlibao.saas.market.service.market.MarketErrorCodeEnum;
import com.xlibao.saas.market.service.market.MarketStatusEnum;
import com.xlibao.saas.market.service.order.MarketOrderErrorCodeEnum;
import com.xlibao.saas.market.service.order.OrderEventListenerManager;
import com.xlibao.saas.market.service.order.OrderService;
import com.xlibao.saas.market.service.order.StatusEnterEnum;
import com.xlibao.saas.market.service.order.properties.PropertiesKeyEnum;
import com.xlibao.saas.market.service.support.ItemSupport;
import com.xlibao.saas.market.service.support.remote.MarketShopRemoteService;
import com.xlibao.saas.market.service.support.remote.OrderRemoteService;
import com.xlibao.saas.market.service.support.remote.PaymentRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("orderService")
public class OrderServiceImpl extends BasicWebService implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;
    @Autowired
    private OrderEventListenerManager orderEventListenerManager;
    @Autowired
    private ItemSupport itemSupport;
    @Autowired
    private MarketShopRemoteService marketShopRemoteService;

    @Override
    public JSONObject prepareCreateOrder() {
        long passportId = getPassportId();
        int orderType = getIntParameter("orderType");
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderType);
        if (orderTypeEnum == null) {
            return fail("订单类型出错，无法预创建订单，类型值：" + orderType);
        }
        return OrderRemoteService.prepareCreateOrder(passportId, orderTypeEnum);
    }

    @Override
    public JSONObject generateOrder() {
        long passportId = getPassportId();
        long marketId = getLongParameter("marketId");
        int deliverType = getIntParameter("deliverType", DeliverTypeEnum.PICKED_UP.getKey());
        int deviceType = getIntParameter("deviceType", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey());
        String sequenceNumber = getUTF("sequenceNumber");
        String currentLocation = getUTF("currentLocation", GlobalConstantConfig.INVALID_LOCATION);
        byte collectingFees = getByteParameter("collectingFees", CollectingFeesEnum.UN_COLLECTION.getKey());
        String receiptProvince = getUTF("receiptProvince", "");
        String receiptCity = getUTF("receiptCity", "");
        String receiptDistrict = getUTF("receiptDistrict", "");
        String receiptAddress = getUTF("receiptAddress", "");
        String receiptNickName = getUTF("receiptNickName", "");
        String receiptPhone = getUTF("receiptPhone", "");
        String receiptLocation = getUTF("receiptLocation", GlobalConstantConfig.INVALID_LOCATION);
        // 备注
        String remark = getUTF("remark", "");
        // 商品集合
        String itemTemplateSet = getUTF("itemTemplateSet");

        if (deliverType == DeliverTypeEnum.DISTRIBUTION.getKey()) {
            if (!CommonUtils.isMobileNum(receiptPhone)) {
                return PlatformErrorCodeEnum.PHONE_NUMBER_ERROR.response("手机号[" + receiptPhone + "]无效");
            }
            if (CommonUtils.isNullString(receiptAddress) || CommonUtils.isNullString(receiptNickName) || CommonUtils.isNullString(receiptPhone)) {
                throw OrderErrorCodeEnum.PERFECT_RECEIPT_ADDRESS.throwException("请填写收货地址、收货人姓名、收货人联系电话等信息");
            }
        }
        // 用户购买的商品信息
        JSONObject buyItemTemplates = JSONObject.parseObject(itemTemplateSet);
        if (buyItemTemplates.isEmpty()) {
            logger.error("下单时无法获取到购买的商品信息，passport id: " + passportId + "，sequenceNumber: " + sequenceNumber + " itemTemplateSet: " + itemTemplateSet);
            return MarketItemErrorCodeEnum.INVALID_ITEMS.response("下单失败，请选择有效的商品");
        }
        List<OrderItemSnapshot> itemSnapshots = generateItemSnapshots(passportId, marketId, buyItemTemplates);

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);

        OrderEntry orderEntry = fillOrderData(passportId, deviceType, deliverType, currentLocation, collectingFees, receiptProvince, receiptCity, receiptDistrict, receiptAddress,
                String.valueOf(passportId), receiptNickName, receiptPhone, receiptLocation, marketEntry, remark, itemSnapshots);

        List<OrderEntry> orders = new ArrayList<>();
        orders.add(orderEntry);

        JSONArray orderEntries = OrderRemoteService.generateOrder(sequenceNumber, passportId, orders);
        // 获取最新订单数据
        orderEntry = JSONObject.toJavaObject(orderEntries.getJSONObject(0), OrderEntry.class);
        // 下单成功后 进行商品库存锁定
        orderEventListenerManager.notifyCreatedOrder(orderEntry);

        JSONObject response = new JSONObject();
        response.put("orderSequenceNumber", orderEntry.getOrderSequenceNumber());
        return success("订单创建成功", response);
    }

    @Override
    public JSONObject modifyReceivingData() {
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        String currentLocation = getUTF("currentLocation", GlobalConstantConfig.INVALID_LOCATION);
        byte collectingFees = getByteParameter("collectingFees", CollectingFeesEnum.UN_COLLECTION.getKey());
        String receiptProvince = getUTF("receiptProvince", "");
        String receiptCity = getUTF("receiptCity", "");
        String receiptDistrict = getUTF("receiptDistrict", "");
        String receiptAddress = getUTF("receiptAddress");
        String receiptNickName = getUTF("receiptNickName");
        String receiptPhone = getUTF("receiptPhone");
        String receiptLocation = getUTF("receiptLocation", GlobalConstantConfig.INVALID_LOCATION);
        // 备注
        String remark = getUTF("remark", "");

        return OrderRemoteService.modifyReceivingData(orderSequenceNumber, currentLocation, collectingFees, receiptProvince, receiptCity, receiptDistrict, receiptAddress, receiptNickName, receiptPhone, receiptLocation, remark);
    }

    @Override
    public JSONObject paymentOrder() {
        long passportId = getPassportId();
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        String paymentType = getUTF("paymentType");
        int deliverType = getIntParameter("deliverType", DeliverTypeEnum.PICKED_UP.getKey());
        String partnerUserId = getUTF("partnerUserId", String.valueOf(passportId));

        OrderEntry order = OrderRemoteService.getOrder(orderSequenceNumber);
        if ((order.getStatus() & OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) == OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) {
            throw new XlibaoRuntimeException("订单已支付！");
        }
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(order.getShippingPassportId());
        if (marketEntry.getStatus() != MarketStatusEnum.NORMAL.getKey()) {
            throw MarketErrorCodeEnum.MARKET_STATUS_ERROR.throwException("商店正在升级维护中，请稍后再来！");
        }
        // 重新计价
        correctingOrderCost(passportId, deliverType, order);
        // 总共需要支付的费用
        long paymentAmount = order.getActualPrice();
        // 支付过程由订单中心执行
        return PaymentRemoteService.paymentOrder(passportId, partnerUserId, orderSequenceNumber, paymentType, String.valueOf(TransTypeEnum.MARKET_PAYMENT.getKey()), paymentAmount, TransTypeEnum.PAYMENT.getKey(),
                paymentAmount, TransTypeEnum.PAYMENT.getValue(), "", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey(), 0, "");
    }

    @Override
    public JSONObject showOrders() {
        long passportId = getPassportId();
        long marketId = getLongParameter("marketId", 0);
        int roleType = getIntParameter("roleType");
        int orderType = getIntParameter("orderType");
        int statusEnter = getIntParameter("statusEnter");
        int pageIndex = getIntParameter("pageIndex", GlobalConstantConfig.DEFAULT_PAGE_INDEX);
        int pageSize = getPageSize();

        String statusSet = orderStatusSet(roleType, statusEnter);
        if (roleType == OrderRoleTypeEnum.COURIER.getKey() && statusEnter == StatusEnterEnum.COURIER_WAIT_ACCEPT.getKey()) {
            // 仅展示未接订单的记录
            return showUnacceptOrders(passportId, roleType, marketId, pageSize);
        }
        List<OrderEntry> orders = OrderRemoteService.showOrders(passportId, marketId, GlobalAppointmentOptEnum.LOGIC_FALSE.getKey(), roleType, statusSet, orderType, pageIndex, pageSize);
        JSONArray response = fillShowOrderMsg(roleType, orders);
        return success(response);
    }

    @Override
    public JSONObject orderDetail() {
        long passportId = getPassportId();
        int roleType = getIntParameter("roleType");
        long orderId = getLongParameter("orderId");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderId);

        return fillOrderDetailMsg(orderEntry, roleType, passportId);
    }

    @Override
    public JSONObject acceptOrder() {
        // 接单者
        long passportId = getLongParameter("passportId");
        // 订单ID
        long orderId = getLongParameter("orderId");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderId);
        if (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey()) {
            return MarketOrderErrorCodeEnum.ORDER_STATUS_ERROR.response("不能接取该订单，用户设置为自提状态");
        }
        if (orderEntry.getCourierPassportId() != null && orderEntry.getCourierPassportId() > 0) {
            return MarketOrderErrorCodeEnum.ORDER_HAS_ACCEPT.response();
        }
        MarketOrderUnacceptLogger orderUnacceptLogger = dataAccessFactory.getOrderDataAccessManager().getOrderUnacceptLogger(passportId, orderEntry.getOrderSequenceNumber());
        if (orderUnacceptLogger == null) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response("订单已被接取");
        }
        // 移除所有未接记录
        dataAccessFactory.getOrderDataAccessManager().removeUnAcceptLoggers(orderEntry.getOrderSequenceNumber());
        OrderRemoteService.acceptOrder(passportId, orderId, GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
        return success("接单成功");
    }

    @Override
    public JSONObject notifyAcceptedOrder() {
        String data = getUTF("data");
        OrderEntry orderEntry = JSONObject.toJavaObject(JSONObject.parseObject(data), OrderEntry.class);

        orderEventListenerManager.notifyAcceptedOrder(orderEntry);
        return success();
    }

    @Override
    public JSONObject deliverOrder() {
        long passportId = getLongParameter("passportId");
        String orderSequenceNumber = getUTF("orderSequenceNumber");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderSequenceNumber);
        if (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey()) {
            return OrderErrorCodeEnum.ORDER_STATUS_ERROR.response("自提订单无需进行配送");
        }
        if (orderEntry.getCourierPassportId() != passportId) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        OrderRemoteService.distributionOrder(orderEntry.getId(), orderEntry.getCourierPassportId(), GlobalAppointmentOptEnum.LOGIC_FALSE.getKey(), true);
        return success("配送成功");
    }

    @Override
    public JSONObject arriveOrder() {
        long passportId = getLongParameter("passportId");
        String orderSequenceNumber = getUTF("orderSequenceNumber");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderSequenceNumber);
        if (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey()) {
            return OrderErrorCodeEnum.ORDER_STATUS_ERROR.response("自提订单不能执行送达操作");
        }
        if (orderEntry.getCourierPassportId() != passportId) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        OrderRemoteService.confirmOrder(orderEntry.getId(), Long.parseLong(orderEntry.getPartnerUserId()), GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        return success("已确认订单");
    }

    @Override
    public JSONObject refundOrder() {
        long passportId = getPassportId();
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        String title = getUTF("title"); // 退款原因标题，必填参数。
        String content = getUTF("content", "");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderSequenceNumber);
        if (orderEntry == null) {
            return OrderErrorCodeEnum.NOT_FOUND_ORDER.response();
        }
        String matchStatusSet = OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_DELIVER.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey();
        // 申请退款
        PaymentRemoteService.applyRefund(passportId, orderSequenceNumber, matchStatusSet, title, content);

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(orderEntry.getShippingPassportId());
        // 请求商店进行退货
        marketShopRemoteService.refundMessage(marketEntry.getPassportId(), orderSequenceNumber);
        // 执行退款业务
        return success("已发起退货申请，请稍后刷新订单状态");
    }

    @Override
    public JSONObject findContainerData() {
        long passportId = getPassportId();
        String orderSequenceNumber = getUTF("orderSequenceNumber");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderSequenceNumber);
        if (orderEntry == null) {
            return OrderErrorCodeEnum.NOT_FOUND_ORDER.response();
        }
        if (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() && Long.parseLong(orderEntry.getPartnerUserId()) != passportId) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        if (orderEntry.getDeliverType() == DeliverTypeEnum.DISTRIBUTION.getKey() && orderEntry.getCourierPassportId() != passportId) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        MarketOrderProperties askProperties = dataAccessFactory.getOrderDataAccessManager().getOrderProperties(orderEntry.getId(), PropertiesKeyEnum.ASK_CONTAINER_SET.getTypeEnum().getKey(), PropertiesKeyEnum.ASK_CONTAINER_SET.getKey());
        if (askProperties != null) {
            return MarketOrderErrorCodeEnum.PICK_UP_DATA_ERROR.response("您已取货完成，请不要重复执行询问请求！");
        }
        MarketOrderProperties orderContainerSetProperties = dataAccessFactory.getOrderDataAccessManager().getOrderProperties(orderEntry.getId(), PropertiesKeyEnum.PICK_UP_CONTAINER_SET.getTypeEnum().getKey(), PropertiesKeyEnum.PICK_UP_CONTAINER_SET.getKey());
        if (orderContainerSetProperties == null) {
            return MarketOrderErrorCodeEnum.UN_SUCCESS_PICK_UP.response("商品未完成拣货，请稍候......");
        }
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(orderEntry.getShippingPassportId());

        String statusCode = orderContainerSetProperties.getV().split(CommonUtils.SPLIT_UNDER_LINE)[0];
        String value = orderContainerSetProperties.getV().split(CommonUtils.SPLIT_UNDER_LINE)[1];

        JSONObject response = new JSONObject();
        response.put("mark", "请从" + marketEntry.getName() + "以下出货口取走商品");
        response.put("statusCode", statusCode);

        JSONArray containerData = new JSONArray();
        if ("00".equals(statusCode)) {
            for (int i = 0; i < value.length() / 2; i++) {
                containerData.add(value.substring(i * 2, (i + 1) * 2));
            }
            dataAccessFactory.getOrderDataAccessManager().createOrderProperties(orderEntry.getId(), orderEntry.getOrderSequenceNumber(), PropertiesKeyEnum.ASK_CONTAINER_SET.getTypeEnum().getKey(), PropertiesKeyEnum.ASK_CONTAINER_SET.getKey(), value);
        } else {
            containerData.add(value);
        }
        response.put("containerData", containerData);
        return success(response);
    }

    @Override
    public JSONObject askOrderStatus() {
        long passportId = getLongParameter("passportId");
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        int askType = getIntParameter("askType");

        OrderEntry orderEntry = OrderRemoteService.getOrder(orderSequenceNumber);
        if (orderEntry == null) {
            return OrderErrorCodeEnum.NOT_FOUND_ORDER.response("订单不存在，序列号：" + orderSequenceNumber);
        }
        if (Long.parseLong(orderEntry.getPartnerUserId()) != passportId) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        if (orderEntry.getUserSource() != DeviceTypeEnum.DEVICE_TYPE_AUTO.getKey()) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response("现场订单才能执行此功能");
        }
        JSONObject response = new JSONObject();
        response.put("statusTitle", orderStatusTitle(DeliverTypeEnum.PICKED_UP.getKey(), orderEntry.getStatus()));

        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey() || orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_DELIVER.getKey() || orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey()) {
            response.put("afterAskStatus", OrderStatusEnum.ORDER_STATUS_DELIVER.getKey());
            response.put("msg", askType == 1 ? "订单已支付成功" : "正在出货，请稍候......");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey()) {
            response.put("afterAskStatus", OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey());
            response.put("msg", "订单已完成");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey() || orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_REFUND.getKey() || orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM_REFUND.getKey()) {
            response.put("afterAskStatus", OrderStatusEnum.ORDER_STATUS_REFUND.getKey());
            response.put("msg", "订单已申请退款");
        }
        return success(response);
    }

    private JSONObject showUnacceptOrders(long passportId, int roleType, long marketId, int pageSize) {
        int pageStartIndex = getPageStartIndex(pageSize);
        List<String> orderSequences = dataAccessFactory.getOrderDataAccessManager().getAppointMarketOrderSequences(passportId, marketId, pageStartIndex, pageSize);
        if (CommonUtils.isEmpty(orderSequences)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        List<OrderEntry> orderEntries = OrderRemoteService.getOrders(orderSequences);
        if (CommonUtils.isEmpty(orderEntries)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        Iterator<OrderEntry> iterator = orderEntries.iterator();
        while (iterator.hasNext()) {
            OrderEntry orderEntry = iterator.next();
            if (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey()) {
                iterator.remove();
            }
            if (orderEntry.getCourierPassportId() != null && orderEntry.getCourierPassportId() > 0) {
                iterator.remove();
            }
        }
        JSONArray response = fillShowOrderMsg(roleType, orderEntries);
        return success(response);
    }

    private String orderStatusSet(int roleType, int statusEnter) {
        if (roleType == OrderRoleTypeEnum.CONSUMER.getKey()) { // 消费者
            if (statusEnter == StatusEnterEnum.ALL.getKey()) { // 用户 全部
                return String.valueOf(OrderStatusEnum.ORDER_STATUS_ALL.getKey());
            }
            if (statusEnter == StatusEnterEnum.WAIT_PAYMENT.getKey()) { // 用户 待支付
                return String.valueOf(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
            }
            if (statusEnter == StatusEnterEnum.WAIT_RECEIPT.getKey()) { // 用户 待收货
                return OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_DELIVER.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey()
                        + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey();
            }
            if (statusEnter == StatusEnterEnum.REFUND.getKey()) { // 用户 退款中
                return OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_REFUND.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_CONFIRM_REFUND.getKey();
            }
            if (statusEnter == StatusEnterEnum.RECOMMEND.getKey()) { // 首页推荐
                return OrderStatusEnum.ORDER_STATUS_DELIVER.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey();
            }
            return OrderStatusEnum.ORDER_STATUS_CANCEL.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey();
        }
        if (roleType == OrderRoleTypeEnum.COURIER.getKey()) {
            if (statusEnter == StatusEnterEnum.COURIER_WAIT_ACCEPT.getKey()) {
                return String.valueOf(OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey());
            }
            if (statusEnter == StatusEnterEnum.COURIER_WAIT_PICK_UP.getKey()) {
                return OrderStatusEnum.ORDER_STATUS_DELIVER.getKey() + CommonUtils.SPLIT_COMMA + OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey();
            }
            if (statusEnter == StatusEnterEnum.COURIER_DELIVER.getKey()) {
                return String.valueOf(OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey());
            }
            if (statusEnter == StatusEnterEnum.COURIER_CANCEL.getKey()) {
                return String.valueOf(OrderStatusEnum.ORDER_STATUS_CANCEL.getKey());
            }
        }
        return String.valueOf(OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey());
    }

    private JSONArray fillShowOrderMsg(int roleType, List<OrderEntry> orderEntries) {
        if (roleType == OrderRoleTypeEnum.COURIER.getKey()) {
            return fillCourierOrderMessage(orderEntries);
        }
        if (roleType != OrderRoleTypeEnum.CONSUMER.getKey()) {
            return new JSONArray();
        }
        JSONArray response = new JSONArray();

        for (OrderEntry orderEntry : orderEntries) {
            JSONObject data = new JSONObject();

            data.put("sequenceNumber", orderEntry.getSequenceNumber());
            data.put("orderId", orderEntry.getId());
            data.put("orderSequenceNumber", orderEntry.getOrderSequenceNumber());

            int deliverType = orderEntry.getDeliverType();

            MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(orderEntry.getShippingPassportId());
            data.put("marketId", marketEntry.getId());
            data.put("marketName", marketEntry.getName());
            data.put("marketFormatAddress", marketEntry.formatAddress());
            data.put("actualPrice", orderEntry.getActualPrice());

            data.put("orderStatus", orderEntry.getStatus());
            data.put("orderStatusTitle", orderStatusTitle(deliverType, orderEntry.getStatus()));

            data.put("deliverType", deliverType);
            data.put("deliverTitle", deliverType == DeliverTypeEnum.PICKED_UP.getKey() ? "到店自提" : "小惠配送");
            data.put("paymentTime", orderEntry.getPaymentTime().getTime());

            fillItemSnapshotMsg(data, orderEntry);
            response.add(data);
        }
        return response;
    }

    private JSONArray fillCourierOrderMessage(List<OrderEntry> orderEntries) {
        JSONArray response = new JSONArray();

        for (OrderEntry orderEntry : orderEntries) {
            JSONObject data = new JSONObject();

            data.put("sequenceNumber", orderEntry.getSequenceNumber());
            data.put("orderId", orderEntry.getId());
            data.put("orderSequenceNumber", orderEntry.getOrderSequenceNumber());

            int deliverType = orderEntry.getDeliverType();

            data.put("formatReceiptAddress", orderEntry.formatReceiptAddress());
            data.put("receiptPhone", orderEntry.getReceiptPhone());
            data.put("deliverDistance", CommonUtils.formatDistance(orderEntry.getTotalDistance()));

            data.put("orderStatus", orderEntry.getStatus());
            data.put("orderStatusTitle", orderStatusTitle(deliverType, orderEntry.getStatus()));

            data.put("paymentTime", CommonUtils.dateFormat(orderEntry.getPaymentTime().getTime()));

            response.add(data);
        }
        return response;
    }

    private String orderStatusTitle(int deliverType, int orderStatus) {
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getOrderStatusEnum(orderStatus);
        if (deliverType == DeliverTypeEnum.PICKED_UP.getKey()) {
            switch (orderStatusEnum) {
                case ORDER_STATUS_PAYMENT:
                    return "待出货";
                case ORDER_STATUS_DELIVER:
                    // return "出货中";
                case ORDER_STATUS_DISTRIBUTION:
                    return "待取货";
                case ORDER_STATUS_ARRIVE:
                    return "已取货";
            }
        } else if (deliverType == DeliverTypeEnum.DISTRIBUTION.getKey()) {
            switch (orderStatusEnum) {
                case ORDER_STATUS_PAYMENT:
                    return "待接单";
                case ORDER_STATUS_ACCEPT:
                    return "已接单";
                case ORDER_STATUS_DELIVER:
                case ORDER_STATUS_DISTRIBUTION:
                    return "配送中";
                case ORDER_STATUS_ARRIVE:
                    return "已签收";
            }
        }
        if (orderStatusEnum == OrderStatusEnum.ORDER_STATUS_APPLY_REFUND || orderStatusEnum == OrderStatusEnum.ORDER_STATUS_REFUND) {
            return "退款中";
        }
        return orderStatusEnum.getValue();
    }

    private JSONObject fillOrderDetailMsg(OrderEntry orderEntry, int roleType, long passportId) {
        if (roleType != OrderRoleTypeEnum.CONSUMER.getKey()) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response("您没有权限查看该订单详情");
        }
        if (Long.parseLong(orderEntry.getPartnerUserId()) != passportId && Long.parseLong(orderEntry.getReceiptUserId()) != passportId) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response("您没有权限查看该订单详情");
        }
        JSONObject orderMsg = new JSONObject();

        int orderStatus = orderEntry.getStatus();
        String statusValue = orderStatus == OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey() ? "用户退款" : OrderStatusEnum.getOrderStatusEnum(orderStatus).getValue();

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(orderEntry.getShippingPassportId());
        orderMsg.put("marketId", marketEntry.getId());
        orderMsg.put("marketName", marketEntry.getName());
        orderMsg.put("marketAddress", marketEntry.formatAddress());

        orderMsg.put("orderId", orderEntry.getId());
        orderMsg.put("sequenceNumber", orderEntry.getSequenceNumber());
        orderMsg.put("orderSequenceNumber", orderEntry.getOrderSequenceNumber());
        orderMsg.put("createTime", CommonUtils.dateFormat(orderEntry.getCreateTime().getTime()));
        orderMsg.put("deliverType", orderEntry.getDeliverType());
        orderMsg.put("deliverValue", orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() ? "到店自提" : "小惠配送");
        orderMsg.put("orderStatus", orderStatus);
        orderMsg.put("statusValue", statusValue);

        String deliverTitle = (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey() || orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_REFUND.getKey() || orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM_REFUND.getKey())
                ? "退款进度" : (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() ? "自提进度" : "配送进度");
        orderMsg.put("refundReason", CommonUtils.nullToEmpty(orderEntry.getDetail()));

        orderMsg.put("deliverTitle", deliverTitle);
        orderMsg.put("deliverResult", orderStatusTitle(orderEntry.getDeliverType(), orderEntry.getStatus()));

        // 实收费用
        orderMsg.put("actualPrice", orderEntry.getActualPrice());
        // 配送费
        orderMsg.put("distributionFee", orderEntry.getDistributionFee());
        // 优惠费用
        orderMsg.put("discountPrice", orderEntry.getDiscountPrice());

        orderMsg.put("addressTitle", orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() ? "取货地址：" : "收货地址：");
        orderMsg.put("address", orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() ? orderEntry.formatShippingAddress() : orderEntry.formatReceiptAddress());
        orderMsg.put("targetTitle", orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() ? "取货点：" : "收货人：");
        orderMsg.put("targetName", orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey() ? "小惠便利店" + CommonUtils.nullToEmpty(orderEntry.getShippingNickName()) : CommonUtils.nullToEmpty(orderEntry.getReceiptNickName()));
        orderMsg.put("receiptPhone", CommonUtils.hideChar(orderEntry.getReceiptPhone(), 3, 7));

        fillItemSnapshotMsg(orderMsg, orderEntry);

        return success(orderMsg);
    }

    private void fillItemSnapshotMsg(JSONObject response, OrderEntry orderEntry) {
        int totalItemQuantity = 0;

        List<OrderItemSnapshot> itemSnapshots = orderEntry.getItemSnapshots();

        JSONArray items = new JSONArray();
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            long itemTemplateId = itemSnapshot.getItemTemplateId();

            ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(itemTemplateId);

            JSONObject item = new JSONObject();

            item.put("itemSnapshotId", itemSnapshot.getId());
            item.put("itemId", itemSnapshot.getItemId());
            item.put("itemTemplateId", itemSnapshot.getItemTemplateId());
            item.put("name", itemSnapshot.getItemName());
            item.put("image", itemTemplate == null ? "" : itemTemplate.getImageUrl());
            item.put("price", itemSnapshot.getNormalPrice());
            item.put("quantity", itemSnapshot.totalQuantity());
            item.put("totalPrice", itemSnapshot.getTotalPrice());

            items.add(item);

            totalItemQuantity += itemSnapshot.totalQuantity();
        }
        response.put("items", items);
        response.put("totalItemQuantity", totalItemQuantity);
    }

    private List<OrderItemSnapshot> generateItemSnapshots(long passportId, long marketId, JSONObject buyItemTemplates) {
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(marketId);
        if (marketEntry.getStatus() != MarketStatusEnum.NORMAL.getKey()) {
            throw MarketErrorCodeEnum.MARKET_STATUS_ERROR.throwException("商店正在升级维护中，请稍后再来！");
        }
        String itemTemplateSet = processItemTemplateSet(buyItemTemplates);

        List<MarketItem> items = dataAccessFactory.getItemDataAccessManager().getItemForItemTemplates(marketId, itemTemplateSet);

        Map<Long, List<MarketItemLadderPrice>> itemLadderPriceMap = itemSupport.loadItemLadderPrices(items);

        String itemSet = processItemSet(items);
        List<MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggers = (passportId <= 0 ? null : dataAccessFactory.getItemDataAccessManager().passportDailyBuyLoggers(passportId, itemSet));
        // 购买资格检查
        itemSupport.buyQualifications(items, itemDailyPurchaseLoggers, buyItemTemplates);

        Map<Long, MarketItemDailyPurchaseLogger> itemDailyPurchaseLoggerMap = itemSupport.itemDailyPurchaseLoggerMap(itemDailyPurchaseLoggers);
        List<OrderItemSnapshot> itemSnapshots = new ArrayList<>();
        for (MarketItem item : items) {
            int buyCount = buyItemTemplates.getIntValue(String.valueOf(item.getItemTemplateId()));
            if (buyCount <= 0) {
                ItemTemplate itemTemplate = ItemDataCacheService.getItemTemplate(item.getItemTemplateId());
                throw new XlibaoRuntimeException(MarketItemErrorCodeEnum.BUY_QUANTITY_ERROR.getKey(), "商品" + (itemTemplate == null ? "" : "[" + itemTemplate.getName() + "]") + "的购买数量必须大于0");
            }
            MarketItemDailyPurchaseLogger itemDailyPurchaseLogger = itemDailyPurchaseLoggerMap.get(item.getItemTemplateId());
            itemSnapshots.add(fillOrderItemSnapshot(passportId, item, itemDailyPurchaseLogger, buyCount, itemLadderPriceMap.get(item.getId())));
        }
        return itemSnapshots;
    }

    private OrderEntry fillOrderData(long passportId, int deviceType, int deliverType, String currentLocation, byte collectingFees, String receiptProvince, String receiptCity, String receiptDistrict, String receiptAddress,
                                     String receiptUserId, String receiptNickName, String receiptPhone, String receiptLocation, MarketEntry marketEntry, String remark, List<OrderItemSnapshot> itemSnapshots) {
        OrderEntry order = new OrderEntry();

        String marketLocation = marketEntry.getLocation();

        order.setPartnerUserId(String.valueOf(passportId));
        order.setUserSource(deviceType);
        order.setDeliverType(deliverType);
        order.setPushType(PushTypeEnum.UN_PUSH.getKey());

        order.setShippingPassportId(marketEntry.getId());
        order.setShippingNickName(marketEntry.getName());
        order.setShippingProvince(marketEntry.getProvince());
        order.setShippingCity(marketEntry.getCity());
        order.setShippingDistrict(marketEntry.getDistrict());
        order.setShippingAddress(marketEntry.getAddress());
        order.setShippingPhone(marketEntry.getPhoneNumber());
        order.setShippingLocation(marketLocation);

        order.setReceiptUserId(receiptUserId);
        order.setReceiptNickName(receiptNickName);
        order.setReceiptProvince(receiptProvince);
        order.setReceiptCity(receiptCity);
        order.setReceiptDistrict(receiptDistrict);
        order.setReceiptAddress(receiptAddress);
        order.setReceiptPhone(receiptPhone);
        order.setReceiptLocation(receiptLocation);

        order.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        order.setDeliverType(deliverType);

        order.setRemark(remark);

        order.setCurrentLocation(currentLocation);
        order.setCollectingFees(collectingFees);

        long actualPrice = 0;
        long totalPrice = 0;
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            actualPrice += itemSnapshot.getTotalPrice();
            totalPrice += (itemSnapshot.getNormalPrice() * itemSnapshot.totalQuantity());
        }
        // 自提或达到免配送费额度时，配送费为0
        long deliverPrice = (deliverType == DeliverTypeEnum.PICKED_UP.getKey() || actualPrice >= marketEntry.getFreeDeliveryFee()) ? 0 : marketEntry.getDeliveryCost();
        actualPrice += deliverPrice;
        totalPrice += deliverPrice;

        long discountPrice = totalPrice - actualPrice;
        order.setActualPrice(actualPrice);
        order.setTotalPrice(totalPrice);
        order.setDiscountPrice(discountPrice);
        order.setPriceLogger(discountPrice <= 0 ? "无优惠" : "订单优惠(折扣)：" + CommonUtils.formatNumber(discountPrice / 100f, "#.##") + "元");
        order.setDistributionFee(deliverPrice);

        order.setItemSnapshots(itemSnapshots);
        return order;
    }

    private void correctingOrderCost(long passportId, int deliverType, OrderEntry order) {
        long timeout = (order.getCreateTime().getTime() + XMarketTimeConfig.ORDER_INVALID_TIME) - System.currentTimeMillis();
        if (timeout <= 0) {
            OrderRemoteService.cancelOrder(order.getId(), order.getPartnerUserId(), order.getStatus(), "订单过期");
            orderEventListenerManager.notifyCanceledOrderEntry(order);
            throw new XlibaoRuntimeException("订单已过期，请重新下单！");
        }
        List<OrderItemSnapshot> orderItemSnapshots = order.getItemSnapshots();

        JSONObject buyItemTemplates = new JSONObject();
        Map<Long, OrderItemSnapshot> itemSnapshotMapperMap = new HashMap<>();
        for (OrderItemSnapshot itemSnapshot : orderItemSnapshots) {
            itemSnapshotMapperMap.put(itemSnapshot.getItemId(), itemSnapshot);
            buyItemTemplates.put(String.valueOf(itemSnapshot.getItemTemplateId()), itemSnapshot.totalQuantity());
        }
        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(order.getShippingPassportId());

        boolean isPriceProtectionTime = (System.currentTimeMillis() - XMarketTimeConfig.PRICE_PROTECTION_TIME) < order.getCreateTime().getTime();
        List<OrderItemSnapshot> itemSnapshots = isPriceProtectionTime ? orderItemSnapshots : generateItemSnapshots(passportId, marketEntry.getId(), buyItemTemplates);

        long actualPrice = 0;
        long totalPrice = 0;
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            OrderItemSnapshot snapshot = itemSnapshotMapperMap.get(itemSnapshot.getItemId());
            if (!isPriceProtectionTime && !snapshot.isMatch(itemSnapshot)) {
                // 修改商品快照属性 远程服务
                OrderRemoteService.modifyItemSnapshot(order.getId(), itemSnapshot.getItemId(), itemSnapshot.getNormalPrice(), itemSnapshot.getDiscountPrice(), itemSnapshot.getNormalQuantity(), itemSnapshot.getDiscountQuantity(), itemSnapshot.getTotalPrice());
            }
            actualPrice += itemSnapshot.getTotalPrice();
            totalPrice += (itemSnapshot.getNormalPrice() * (itemSnapshot.totalQuantity()));
        }
        // 自提或达到免配送费额度时，配送费为0
        long deliverPrice = (deliverType == DeliverTypeEnum.PICKED_UP.getKey() || actualPrice >= marketEntry.getFreeDeliveryFee()) ? 0 : marketEntry.getDeliveryCost();
        actualPrice += deliverPrice;
        totalPrice += deliverPrice;

        long discountPrice = totalPrice - actualPrice;
        if (!order.isPriceMatch(actualPrice, totalPrice, discountPrice, deliverPrice, deliverType)) {
            OrderRemoteService.correctOrderPrice(order.getId(), actualPrice, totalPrice, discountPrice, deliverPrice, deliverType);

            order.setActualPrice(actualPrice);
            order.setTotalPrice(totalPrice);
            order.setDiscountPrice(discountPrice);
            order.setDistributionFee(deliverPrice);
            order.setDeliverType(deliverType);
        }
    }

    private OrderItemSnapshot fillOrderItemSnapshot(long passportId, MarketItem item, MarketItemDailyPurchaseLogger roleDailyBuyLogger, int thisBuyCount, List<MarketItemLadderPrice> itemLadderPrices) {
        OrderItemSnapshot orderItemSnapshot = itemSupport.fillOrderItemSnapshot(item, roleDailyBuyLogger, thisBuyCount, itemLadderPrices);

        orderItemSnapshot.setUserMark(String.valueOf(passportId));
        return orderItemSnapshot;
    }

    private String processItemTemplateSet(JSONObject items) {
        StringBuilder itemTemplateSet = new StringBuilder();
        for (String s : items.keySet()) {
            itemTemplateSet.append(s).append(CommonUtils.SPLIT_COMMA);
        }
        itemTemplateSet.deleteCharAt(itemTemplateSet.length() - 1);

        return itemTemplateSet.toString();
    }

    private String processItemSet(List<MarketItem> marketItems) {
        StringBuilder itemSet = new StringBuilder();
        for (MarketItem item : marketItems) {
            itemSet.append(item.getId()).append(CommonUtils.SPLIT_COMMA);
        }
        itemSet.deleteCharAt(itemSet.length() - 1);

        return itemSet.toString();
    }

    private long getPassportId() {
        // TODO 临时做法 需要修改为通过token获取(未有缓存支持)
        long passportId = getLongParameter("passportId", 0);
        if (passportId == 0) {
            throw new XlibaoIllegalArgumentException("请先登录！");
        }
        return passportId;
    }
}