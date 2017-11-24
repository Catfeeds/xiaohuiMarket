package com.xlibao.order.service.order.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.message.TargetTypeEnum;
import com.xlibao.common.constant.order.*;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.exception.code.OrderErrorCodeEnum;
import com.xlibao.common.lbs.SimpleLocationUtils;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.common.support.SharePaymentRemoteService;
import com.xlibao.metadata.order.*;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.order.config.ConfigFactory;
import com.xlibao.order.data.mapper.order.OrderDataAccessManager;
import com.xlibao.order.service.order.OrderEventListenerManager;
import com.xlibao.order.service.order.OrderService;
import com.xlibao.order.service.support.LogisticsRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/2/8.
 */
@Transactional
@Service("orderService")
public class OrderServiceImpl extends BasicWebService implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDataAccessManager orderDataAccessManager;
    @Autowired
    private OrderEventListenerManager orderEventListenerManager;

    @Override
    public JSONObject prepareCreateOrder() {
        String partnerId = getUTF("partnerId");
        String partnerUserId = getUTF("partnerUserId");
        int orderType = getIntParameter("orderType");

        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderType);
        if (orderTypeEnum == null) {
            return fail("订单类型出错，无法预创建订单，类型值：" + orderType);
        }
        JSONObject response = new JSONObject();
        if (orderTypeEnum.isRejectDuplicate()) { // 是否拒绝重复提交的情况
            OrderSequence orderSequence = orderDataAccessManager.lastOrderSequence(partnerId, partnerUserId, orderTypeEnum);
            if (orderSequence != null && orderSequence.getStatus() == OrderSequenceNumberStatusEnum.NORMAL.getKey()) {
                // 检查上次获取的序列号是否仍处于可用状态 若已过期 则提交更新行为
                if (System.currentTimeMillis() <= (orderSequence.getCreateTime().getTime() + orderSequence.getValidityTermSecond() * 1000)) {
                    response.put("sequenceNumber", orderSequence.getSequenceNumber());
                    return success(response);
                }
                // 超过有效期时设置为无效状态
                int result = orderDataAccessManager.updateOrderSequence(orderSequence.getSequenceNumber(), OrderSequenceNumberStatusEnum.INVALID);
                if (result <= 0) {
                    logger.error("修改预创建订单状态时发生异常，无法更新记录，合作者ID：" + partnerId + "，合作方用户ID：" + partnerUserId + "，订单类型：" + orderTypeEnum.getValue());
                    return fail("获取预创建订单序列号失败！");
                }
            }
        }
        String sequenceNumber = uniquePrimaryKey();

        OrderSequence orderSequence = new OrderSequence();
        orderSequence.setPartnerId(partnerId);
        orderSequence.setPartnerUserId(partnerUserId);
        orderSequence.setSequenceNumber(sequenceNumber);
        orderSequence.setType(orderTypeEnum.getKey());
        orderSequence.setStatus((byte) OrderSequenceNumberStatusEnum.NORMAL.getKey());
        orderSequence.setValidityTermSecond((int) GlobalConstantConfig.SEQUENCE_NUMBER_VALIDITY_TERM_SECOND);

        int result = orderDataAccessManager.createOrderSequence(orderSequence);
        if (result <= 0) {
            return fail("预创建订单失败，请稍后重试！");
        }
        response.put("sequenceNumber", sequenceNumber);
        return success(response);
    }

    @Override
    public JSONObject generateOrder() {
        String sequenceNumber = getUTF("sequenceNumber");
        String partnerId = getUTF("partnerId");
        String partnerUserId = getUTF("partnerUserId");
        OrderSequence orderSequence = usePreparedOrder(partnerId, partnerUserId, sequenceNumber);

        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderSequence.getType());
        switch (orderTypeEnum) {
            case SALE_ORDER_TYPE: // 销售订单
            case ALLOCATION_ORDER_TYPE:  // 调拨订单
            case PURCHASE_ORDER_TYPE:  // 采购订单
                return generateSaleOrder(orderSequence);
            default: {
                throw new XlibaoRuntimeException("错误的订单类型，错误码：" + orderSequence.getType());
            }
        }
    }

    @Override
    public JSONObject unifiedPayment() {
        Map<String, String> signParameters = new HashMap<>();

        String paymentType = getUTF("paymentType", PaymentTypeEnum.WEIXIN_APP.getKey());

        signParameters.put("passportId", getUTF("passportId"));
        signParameters.put("paymentType", paymentType);
        signParameters.put("transType", getUTF("transType", String.valueOf(TransTypeEnum.PAYMENT.getKey())));
        signParameters.put("partnerUserId", getUTF("partnerUserId", ""));
        signParameters.put("partnerTradeNumber", getUTF("partnerTradeNumber"));
        signParameters.put("transUnitAmount", getUTF("transUnitAmount"));
        signParameters.put("transNumber", getUTF("transNumber"));
        signParameters.put("transTotalAmount", getUTF("transTotalAmount", "0"));
        signParameters.put("transTitle", getUTF("transTitle", ""));
        signParameters.put("remark", getUTF("remark", ""));
        signParameters.put("useCoupon", getUTF("useCoupon", ""));
        signParameters.put("discountAmount", getUTF("discountAmount", "0"));
        signParameters.put("extendParameter", getUTF("extendParameter", ""));
        signParameters.put("notifyUrl", ConfigFactory.getDomainNameConfig().orderRemoteURL + "order/openapi/callbackPaymentOrder");

        return SharePaymentRemoteService.paymentOrder(signParameters, ConfigFactory.getOrderConfig().getPartnerId(), ConfigFactory.getOrderConfig().getPaymentAppId(), ConfigFactory.getOrderConfig().getPaymentAppkey(),
                ConfigFactory.getDomainNameConfig().paymentRemoteURL, paymentType);
    }

    @Override
    public JSONObject callbackPaymentOrder() {
        String data = getUTF("data");
        JSONObject parameters = JSONObject.parseObject(data);

        Map<String, String> p = new HashMap<>();
        for (String k : parameters.keySet()) {
            p.put(k, parameters.getString(k));
        }
        String remoteSign = p.remove("sign");
        // 验证签名 本地验证
        if (!CommonUtils.matchSignature(p, remoteSign, ConfigFactory.getOrderConfig().getPaymentAppkey())) {
            return PlatformErrorCodeEnum.SIGN_ERROR.response();
        }
        String partnerId = parameters.getString("partnerId");
        int transStatus = parameters.getIntValue("transStatus");
        String paymentType = parameters.getString("paymentType");
        String partnerTradeNumber = parameters.getString("partnerTradeNumber");

        logger.info("通知订单支付结果，订单标志为：" + partnerTradeNumber + "，支付类型：" + paymentType + "，支付状态：" + transStatus + "，合作商户号：" + partnerId);

        if (!partnerId.equals(ConfigFactory.getOrderConfig().getPartnerId())) {
            logger.error("非本商户订单[" + partnerTradeNumber + "]，错误的支付回调，我方商户号：" + ConfigFactory.getOrderConfig().getPartnerId() + "；回调商户号：" + partnerId);
            return success("非法请求");
        }
        if ((transStatus & TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) != TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) {
            return success("success");
        }
        OrderEntry orderEntry = getOrder(partnerTradeNumber);
        paymentOrder(orderEntry, orderEntry.getStatus(), paymentType, 0);
        return success("success");
    }

    @Override
    public JSONObject getOrder() {
        long orderId = getLongParameter("orderId");
        OrderEntry order = getOrder(orderId);

        return success(fillOrderMsg(order));
    }

    @Override
    public JSONObject getOrderForSequenceNumber() {
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        OrderEntry orderEntry = getOrder(orderSequenceNumber);

        return success(fillOrderMsg(orderEntry));
    }

    @Override
    public JSONObject getOrders() {
        String sequenceNumber = getUTF("sequenceNumber");

        List<OrderEntry> orderEntries = getOrders(sequenceNumber);

        fillOrdersItemSnapshots(orderEntries);

        JSONArray orderArray = orderEntries.stream().map(orderEntry -> JSONObject.parseObject(JSONObject.toJSONString(orderEntry))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("orderArray", orderArray);
        return success(response);
    }

    @Override
    public JSONObject getOrderForSequenceSet() {
        String orderSequenceSet = getUTF("orderSequenceSet");

        List<OrderEntry> orderEntries = getOrderForSequenceSet(orderSequenceSet);

        fillOrdersItemSnapshots(orderEntries);

        JSONArray orderArray = orderEntries.stream().map(orderEntry -> JSONObject.parseObject(JSONObject.toJSONString(orderEntry))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("orderArray", orderArray);
        return success(response);
    }

    @Override
    public JSONObject cancelOrder() {
        long orderId = getLongParameter("orderId");
        String partnerId = getUTF("partnerId");
        String partnerUserId = getUTF("partnerUserId");
        int beforeStatus = getIntParameter("beforeStatus", OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        String reason = getUTF("reason", "");

        OrderEntry orderEntry = getOrder(orderId);

        if (!orderEntry.getPartnerId().equals(partnerId) || !orderEntry.getPartnerUserId().equals(partnerUserId)) {
            return fail("您没有权限取消该订单");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) {
            return fail("订单已支付，不能取消！若您仍需取消该订单，请联系客服人员！");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey()) {
            return fail("订单正在配送中，不能取消！若您仍需取消该订单，请联系快递人员！");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey()) {
            return fail("订单正在配送中，不能取消！若您仍需取消该订单，请联系快递人员！");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_ARRIVE.getKey()) {
            return fail("不能取消已送达的订单！若您仍需取消该订单，请先执行退货！");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey()) {
            return fail("订单已完成！");
        }
        if (beforeStatus != orderEntry.getStatus()) {
            return fail("根据您的应用设置，当前状态不能进行支付操作");
        }
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), OrderStatusEnum.ORDER_STATUS_CANCEL.getKey(), beforeStatus, orderEntry.getDeliverStatus(), orderEntry.getDeliverStatus());
        if (result <= 0) {
            throw new XlibaoRuntimeException("取消订单失败");
        }
        orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_CANCEL.getKey());
        orderEntry.setCancelLogger(reason);

        orderEventListenerManager.notifyCanceledOrderEntry(orderEntry, beforeStatus, false);
        return success("订单取消成功");
    }

    @Override
    public JSONObject paymentOrder() {
        long orderId = getLongParameter("orderId");
        String transType = getUTF("transType");
        int daySortNumber = getIntParameter("daySortNumber", 0);
        int beforeStatus = getIntParameter("beforeStatus", OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());

        OrderEntry orderEntry = getOrder(orderId);

        return paymentOrder(orderEntry, beforeStatus, transType, daySortNumber);
    }

    private JSONObject paymentOrder(OrderEntry orderEntry, int beforeStatus, String transType, int daySortNumber) {
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) { // 已取消
            throw OrderErrorCodeEnum.CANCELED_ORDER.throwException();
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) { // 已支付
            throw OrderErrorCodeEnum.HAS_PAYMENT_ORDER.throwException();
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey()) { // 已完成
            throw OrderErrorCodeEnum.COMPLETE_ORDER.throwException();
        }
        if (beforeStatus != orderEntry.getStatus()) {
            throw PlatformErrorCodeEnum.SETTING_ERROR.throwException("根据您的应用设置，当前状态不能进行支付操作");
        }
        int result = orderDataAccessManager.paymentOrder(orderEntry.getId(), OrderStatusEnum.ORDER_STATUS_PAYMENT, beforeStatus, transType, daySortNumber);
        if (result <= 0) {
            throw OrderErrorCodeEnum.FAIL_PAYMENT_ORDER.throwException();
        }
        orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey());
        orderEntry.setTransType(transType);
        orderEventListenerManager.notifyPaymentOrderEntry(orderEntry, beforeStatus);
        return success("订单支付成功");
    }

    @Override
    public JSONObject pushOrderMsg() {
        String orderParameters = getUTF("order");

        int pushType = getIntParameter("pushType", TargetTypeEnum.TARGET_TYPE_COURIER.getKey());
        String title = getUTF("title", "您有一个新的订单");
        String content = getUTF("content", "您有一个新的订单，请注意查收！");
        String voice = getUTF("voice", "new.mp3");
        byte target = getByteParameter("target", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        String message = getUTF("message", new JSONObject().toJSONString());
        String receiptId = getUTF("receiptId", "");
        byte write = getByteParameter("write", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());

        JSONObject response = LogisticsRemoteService.pushOrderMsg(orderParameters, pushType, target, receiptId, title, content, voice, message);

        JSONArray targetArray = response.getJSONArray("targetArray");

        String[] targets = new String[targetArray.size()];

        for (int i = 0; i < targets.length; i++) {
            targets[i] = targetArray.getString(i);
        }
        OrderEntry orderEntry = JSONObject.parseObject(orderParameters, OrderEntry.class);
        orderEventListenerManager.notifyPushedOrderEntry(orderEntry, pushType, title, content, write, targets);
        return success();
    }

    @Override
    public JSONObject unAcceptOrderSize() {
        long courierPassportId = getLongParameter("courierPassportId");
        int size = orderDataAccessManager.unAcceptOrderSize(courierPassportId);

        JSONObject response = new JSONObject();
        response.put("size", size);
        return success(response);
    }

    @Override
    public JSONObject acceptOrder() {
        long courierPassportId = getLongParameter("courierPassportId");
        long orderId = getLongParameter("orderId", 0);
        int forceAppoint = getIntParameter("forceAppoint", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());

        if (forceAppoint == GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()) {
            OrderUnacceptLogger unacceptLogger = (orderId == 0) ? orderDataAccessManager.getNewestUnacceptLogger(courierPassportId) : orderDataAccessManager.getUnacceptLogger(orderId, courierPassportId);
            if (unacceptLogger == null) {
                return fail("没有可接取的订单记录");
            }
            orderId = unacceptLogger.getOrderId();
        }
        // 获取订单记录
        OrderEntry orderEntry = getOrder(orderId);
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
            return fail("订单已被取消");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey()) {
            return fail("订单已被接取");
        }
        if (orderEntry.getCollectingFees() == 0 && orderEntry.getStatus() != OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) {
            return fail("订单未支付且不支持“货到付款”");
        }
        Passport passport = PassportRemoteService.getPassport(courierPassportId);
        int beforeStatus = orderEntry.getStatus();

        orderEntry.setCourierPassportId(courierPassportId);
        orderEntry.setCourierNickName(passport.getRealName());
        orderEntry.setCourierPhone(passport.getPhoneNumber());
        orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey());

        // 更新时判断状态和接单人是否已存在 避免多线程时其他用户接单造成数据覆盖
        int result = orderDataAccessManager.acceptOrder(orderEntry.getId(), courierPassportId, passport.getRealName(), passport.getPhoneNumber(), OrderStatusEnum.ORDER_STATUS_ACCEPT, beforeStatus);
        if (result <= 0) {
            throw new XlibaoRuntimeException("接单失败，订单已被其他用户接取");
        }
        // 移除未接单记录 避免其他用户做出无谓的抢单操作
        orderDataAccessManager.removeUnacceptLoggers(orderEntry.getId());

        orderEventListenerManager.notifyAcceptedOrderEntry(orderEntry, beforeStatus);

        JSONObject response = new JSONObject();
        response.put("orderId", orderEntry.getId());
        response.put("partnerId", orderEntry.getPartnerId());
        return success("接单成功", response);
    }

    @Override
    public JSONObject deliverOrder() {
        long orderId = getLongParameter("orderId");
        long shippingPassportId = getLongParameter("shippingPassportId");
        // 本次发货的商品 格式{"itemSnapshotId" : "deliveryQuantity"} -- 商品快照ID : 发货数量
        String itemSet = getUTF("itemSet");

        OrderEntry orderEntry = getOrder(orderId);
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
            return fail("订单已被取消");
        }
        if (orderEntry.getStatus() != OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey() && (orderEntry.getStatus() & OrderStatusEnum.ORDER_STATUS_BATCH.getKey()) != OrderStatusEnum.ORDER_STATUS_BATCH.getKey()) {
            return fail("当前状态不能进行发货操作，当前状态：" + orderEntry.getStatus());
        }
        if (orderEntry.getShippingPassportId() != shippingPassportId) {
            return fail("您没有权限操作此订单！");
        }
        // 填充商品快照信息
        fillOrderItemSnapshots(orderEntry);

        JSONObject deliveryItems = JSONObject.parseObject(itemSet);
        if (deliveryItems.isEmpty()) {
            return fail("发货数据错误，请选择本次的发货商品");
        }
        Map<Long, OrderItemSnapshot> itemSnapshotMap = getItemSnapshotMap(orderEntry);
        for (String itemSnapshotId : deliveryItems.keySet()) {
            OrderItemSnapshot itemSnapshot = itemSnapshotMap.get(Long.parseLong(itemSnapshotId));
            if (itemSnapshot == null) {
                logger.error(orderEntry.getId() + "发货信息出错，本次发货部分商品不为订单内的商品，快照ID：" + itemSnapshotId);
                throw new XlibaoRuntimeException("商品信息有误，请刷新后重试！");
            }
            int deliveryQuantity = deliveryItems.getIntValue(itemSnapshotId);
            if (deliveryQuantity <= 0) {
                throw new XlibaoRuntimeException("发货数量异常，“" + itemSnapshot.getItemName() + "”发货数量必须大于0");
            }
            if ((itemSnapshot.totalShippingQuantity() + deliveryQuantity) > itemSnapshot.totalQuantity()) {
                throw new XlibaoRuntimeException("发货数量异常，“" + itemSnapshot.getItemName() + "”未发货数量：" + itemSnapshot.unDeliveryQuantity() + "，本次发货：" + deliveryQuantity);
            }
            int result = orderDataAccessManager.shippingItem(Long.parseLong(itemSnapshotId), deliveryQuantity);
            if (result <= 0) {
                throw new XlibaoRuntimeException("【" + itemSnapshot.getItemName() + "】发货数量有误，请检查！");
            }
            itemSnapshot.setShipmentQuantity(deliveryQuantity);
        }
        int deliverStatus = OrderStatusEnum.ORDER_STATUS_DELIVER.getKey();
        if (orderEntry.getDeliverStatus() == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey()) {
            deliverStatus = OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey() | OrderStatusEnum.ORDER_STATUS_DELIVER.getKey();
        }
        // 更新订单的发货状态
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), orderEntry.getStatus(), orderEntry.getStatus(), deliverStatus, orderEntry.getDeliverStatus());
        if (result <= 0) {
            throw new XlibaoRuntimeException("发货失败，请稍后重试");
        }
        return success("发货成功");
    }

    @Override
    public JSONObject distributionOrder() {
        long orderId = getLongParameter("orderId");
        long courierPassportId = getLongParameter("courierPassportId");
        // 是否自营订单
        byte self = getByteParameter("self", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        byte refreshOrderStatus = getByteParameter("refreshOrderStatus", GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());

        OrderEntry orderEntry = getOrder(orderId);

        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
            return fail("订单已被取消");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey()) {
            return fail("订单已在配送中");
        }
        if (orderEntry.getCollectingFees() == 0 && orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey()) {
            return fail("订单未支付且不支持“货到付款”");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey()) {
            return fail("订单已完成，无需再次配送");
        }
        if (self == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) {
            if (orderEntry.getDeliverStatus() != OrderStatusEnum.ORDER_STATUS_DELIVER.getKey()) {
                return fail("不为发货状态，暂时不能进行配送操作");
            }
            if (orderEntry.getCourierPassportId() != courierPassportId) {
                return fail("该订单已被其他人接取，您不能进行配送操作");
            }
            Passport passport = PassportRemoteService.getPassport(courierPassportId);
            if (passport == null) {
                return fail("您的帐号信息有误，错误码：" + courierPassportId);
            }
        }
        int beforeStatus = orderEntry.getStatus();
        // 填充商品快照信息
        List<OrderItemSnapshot> itemSnapshots = fillOrderItemSnapshots(orderEntry);
        boolean isBatch = false;
        if (!CommonUtils.isEmpty(itemSnapshots)) { // 检查是否使用了体系内的商品系统
            for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
                int result;
                if (self == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) { // 自营仓库时 需要进行检查
                    if (itemSnapshot.getShipmentQuantity() < itemSnapshot.totalQuantity()) {
                        isBatch = true;
                    }
                    if (itemSnapshot.getShipmentQuantity() == 0) { // 没有发货
                        continue;
                    }
                    result = orderDataAccessManager.distributionItem(itemSnapshot.getId(), itemSnapshot.getShipmentQuantity());
                } else { // 非自营仓库时 执行全量发货
                    result = orderDataAccessManager.totalDistributionItems(itemSnapshot.getId());
                }
                if (result <= 0) {
                    throw new XlibaoRuntimeException("【" + itemSnapshot.getItemName() + "】配送失败");
                }
                itemSnapshot.setDistributionQuantity(itemSnapshot.getShipmentQuantity());
                itemSnapshot.setShipmentQuantity(0);
            }
        }
        // 设置订单状态
        orderEntry.setStatus(isBatch ? OrderStatusEnum.ORDER_STATUS_BATCH.getKey() :
                (refreshOrderStatus == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey() ? OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey() : orderEntry.getStatus()));
        int deliverStatus = OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey();
        // 更新订单状态和配送状态
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), orderEntry.getStatus(), beforeStatus, deliverStatus, orderEntry.getDeliverStatus());
        if (result <= 0) {
            throw new XlibaoRuntimeException("配送失败");
        }
        // 设置为配送中
        orderEntry.setDeliverStatus(deliverStatus);
        orderEventListenerManager.notifyDistributionOrderEntry(orderEntry, beforeStatus);
        return success("配送成功");
    }

    @Override
    public JSONObject arriveOrder() {
        long orderId = getLongParameter("orderId");
        long courierPassportId = getLongParameter("courierPassportId");

        OrderEntry orderEntry = getOrder(orderId);

        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
            return fail("订单已被取消");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_ARRIVE.getKey()) {
            return fail("订单已送达");
        }
        if (orderEntry.getCollectingFees() == 0 && orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey()) {
            return fail("订单未支付且不支持“货到付款”");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey()) {
            return fail("订单已完成，无需再次配送");
        }
        if ((orderEntry.getDeliverStatus() & OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey()) != OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey()) {
            return fail("不为配送状态，暂时不能执行送达操作");
        }
        if (orderEntry.getCourierPassportId() != courierPassportId) {
            return fail("您没有权限操作该订单");
        }
        if (courierPassportId > 0) {
            Passport passport = PassportRemoteService.getPassport(courierPassportId);
            if (passport == null) {
                return fail("您的帐号信息有误，错误码：" + courierPassportId);
            }
        }
        int beforeStatus = orderEntry.getStatus();
        // 填充商品快照信息
        List<OrderItemSnapshot> itemSnapshots = fillOrderItemSnapshots(orderEntry);
        boolean arrive = true;
        JSONObject arriveItems = new JSONObject();
        if (!CommonUtils.isEmpty(itemSnapshots)) { // 检查是否使用了体系内的商品系统
            for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
                if (itemSnapshot.getDistributionQuantity() == 0) {
                    continue;
                }
                int result = orderDataAccessManager.arriveItem(itemSnapshot.getId());
                if (result <= 0) {
                    throw new XlibaoRuntimeException("【" + itemSnapshot.getItemName() + "】送达失败");
                }
                arriveItems.put(String.valueOf(itemSnapshot.getId()), itemSnapshot.getDistributionQuantity());

                itemSnapshot.setReceiptQuantity(itemSnapshot.getReceiptQuantity() + itemSnapshot.getDistributionQuantity());
                itemSnapshot.setDistributionQuantity(0);

                if (itemSnapshot.totalQuantity() > (itemSnapshot.getReceiptQuantity() + itemSnapshot.getReturnQuantity())) {
                    arrive = false;
                }
            }
        }
        // 如果包含了发货 那么必须设置为发货状态 否则为送达状态
        int deliverStatus = ((orderEntry.getDeliverStatus() & OrderStatusEnum.ORDER_STATUS_DELIVER.getKey()) == OrderStatusEnum.ORDER_STATUS_DELIVER.getKey()) ?
                OrderStatusEnum.ORDER_STATUS_DELIVER.getKey() : OrderStatusEnum.ORDER_STATUS_ARRIVE.getKey();
        // 更新订单状态和配送状态
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), (arrive ? (OrderStatusEnum.ORDER_STATUS_ARRIVE.getKey() | beforeStatus) : beforeStatus), beforeStatus, deliverStatus, orderEntry.getDeliverStatus());
        if (result <= 0) {
            throw new XlibaoRuntimeException("送达失败");
        }
        // 设置为送达
        orderEntry.setDeliverStatus(deliverStatus);
        orderEntry.setStatus(arrive ? (OrderStatusEnum.ORDER_STATUS_ARRIVE.getKey() | beforeStatus) : beforeStatus);

        orderEventListenerManager.notifyArrivedOrderEntry(orderEntry, beforeStatus);
        return success(arriveItems);
    }

    @Override
    public JSONObject confirmOrder() {
        long orderId = getLongParameter("orderId");
        String partnerId = getUTF("partnerId");
        String partnerUserId = getUTF("partnerUserId");
        // 是否自营订单
        byte self = getByteParameter("self", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());

        OrderEntry orderEntry = getOrder(orderId);
        fillOrderItemSnapshots(orderEntry);

        if (self == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) {
            if (!endArriver(fillOrderItemSnapshots(orderEntry))) {
                return fail("未送达订单不能确认收货");
            }
        }
        if (!orderEntry.getPartnerId().equals(partnerId) || (!orderEntry.getReceiptUserId().equals(partnerUserId) && !orderEntry.getPartnerUserId().equals(partnerUserId))) {
            return fail("您没有权限确认该订单");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey()) {
            return fail("订单已确认");
        }
        int beforeStatus = orderEntry.getStatus();
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey(), beforeStatus, orderEntry.getDeliverStatus(), orderEntry.getDeliverStatus());
        if (result <= 0) {
            throw new XlibaoRuntimeException("收货失败");
        }
        orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey());

        orderEventListenerManager.notifyConfirmedOrderEntry(orderEntry, beforeStatus);
        return success();
    }

    @Override
    public JSONObject confirmOfPayment() {
        long orderId = getLongParameter("orderId");
        String partnerId = getUTF("partnerId");
        String partnerUserId = getUTF("partnerUserId");

        OrderEntry orderEntry = getOrder(orderId);

        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
            return fail("订单已被取消");
        }
        if (!orderEntry.getPartnerId().equals(partnerId) || !orderEntry.getPartnerUserId().equals(partnerUserId)) {
            return fail("您没有权限操作此订单！");
        }
        if ((orderEntry.getRefundStatus() & OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) == OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()) {
            return fail("该订单已支付");
        }
        int result = orderDataAccessManager.refreshRefundStatus(orderId, OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey());
        return result > 0 ? success("付款成功") : fail("付款失败");
    }

    @Override
    public JSONObject batchReceipt() {
        long orderId = getLongParameter("orderId");
        String partnerId = getUTF("partnerId");
        String partnerUserId = getUTF("partnerUserId");
        String itemSnapshotSet = getUTF("itemSnapshotSet");

        OrderEntry orderEntry = getOrder(orderId);
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
            return fail("订单已被取消");
        }
        if (orderEntry.getStatus() == OrderStatusEnum.ORDER_STATUS_ARRIVE.getKey()) {
            return fail("订单已完成收货");
        }
        if (!orderEntry.getPartnerId().equals(partnerId) || !orderEntry.getReceiptUserId().equals(partnerUserId)) {
            return fail("您没有权限操作此订单！");
        }
        if (orderEntry.getStatus() != OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getKey() && orderEntry.getStatus() != OrderStatusEnum.ORDER_STATUS_BATCH.getKey()) {
            return fail("当前状态不能进行收货操作，当前状态：" + OrderStatusEnum.getOrderStatusEnum(orderEntry.getStatus()).getValue());
        }
        // 填充商品快照信息
        fillOrderItemSnapshots(orderEntry);

        JSONObject receiptItems = JSONObject.parseObject(itemSnapshotSet);
        if (receiptItems.isEmpty()) {
            return fail("收货数据错误，请选择本次的收货商品");
        }
        Map<Long, OrderItemSnapshot> itemSnapshotMap = getItemSnapshotMap(orderEntry);
        boolean isBatch = false;
        for (OrderItemSnapshot itemSnapshot : itemSnapshotMap.values()) {
            Object quantity = receiptItems.remove(String.valueOf(itemSnapshot.getId()));
            if (quantity == null) {
                if (itemSnapshot.getReceiptQuantity() >= itemSnapshot.totalQuantity()) { // 已完成收货
                    continue;
                }
                isBatch = true;
                continue;
            }
            int receiptQuantity = Integer.parseInt((String) quantity);
            if (receiptQuantity <= 0) {
                throw new XlibaoRuntimeException("收货数量异常，“" + itemSnapshot.getItemName() + "”收货数量必须大于0");
            }
            // 允许多收货
            // if ((itemSnapshot.getReceiptQuantity() + receiptQuantity) > itemSnapshot.totalQuantity()) {
            //     throw new XlibaoRuntimeException("收货数量异常，“" + itemSnapshot.getItemName() + "”未收货数量：" + itemSnapshot.unDeliveryQuantity() + "，本次收货：" + receiptQuantity);
            // }
            if (itemSnapshot.totalQuantity() > (receiptQuantity + itemSnapshot.getReceiptQuantity())) { // 本次收货少于总数 说明为分批收货
                isBatch = true;
            }
            int result = orderDataAccessManager.receiptItem(itemSnapshot.getId(), receiptQuantity);
            if (result <= 0) {
                throw new XlibaoRuntimeException("【" + itemSnapshot.getItemName() + "】收货数量有误，请检查！");
            }
            itemSnapshot.setReceiptQuantity(itemSnapshot.getReturnQuantity() + receiptQuantity);
        }
        if (!receiptItems.isEmpty()) {
            throw new XlibaoRuntimeException("部分收货商品不在本次的订单中，错误内容：" + receiptItems.toJSONString());
        }
        int beforeStatus = orderEntry.getStatus();
        if (isBatch && (beforeStatus == OrderStatusEnum.ORDER_STATUS_BATCH.getKey())) { // 分批收货
            return success("收货成功");
        }
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), (isBatch ? OrderStatusEnum.ORDER_STATUS_BATCH : OrderStatusEnum.ORDER_STATUS_CONFIRM).getKey(), beforeStatus, orderEntry.getDeliverStatus(), orderEntry.getDeliverStatus());
        if (!isBatch) {
            orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_CONFIRM.getKey());
            orderEventListenerManager.notifyConfirmedOrderEntry(orderEntry, beforeStatus);
        }
        return result > 0 ? success("收货成功") : fail("收货失败");
    }

    @Override
    public JSONObject refreshOrderStatus() {
        String orderSequenceNumber = getUTF("orderSequenceNumber"); // 订单序列号
        String operationPassportId = getUTF("operationPassportId"); // 操作者通行证ID
        int permissionType = getIntParameter("permissionType", 1);  // 等于或包含以下值时：1、下单方 2、销售方 4、收货方 8、发货方 16、配送方
        String validStatusSet = getUTF("validStatusSet");           // 有效的状态集合，即处于指定状态下才可执行该请求的意思
        int targetStatus = getIntParameter("targetStatus");         // 目标状态值

        OrderEntry orderEntry = getOrder(orderSequenceNumber);
        if (!validStatusSet.contains(String.valueOf(orderEntry.getStatus()))) {
            return OrderErrorCodeEnum.REFRESH_STATUS_ERROR.response("当前状态不能执行刷新请求");
        }
        int result = -1;
        if ((permissionType & OrderPermissionTypeEnum.CONSUMER.getKey()) == OrderPermissionTypeEnum.CONSUMER.getKey()) {
            if (operationPassportId.equals(orderEntry.getPartnerUserId())) { // 通过
                result = orderDataAccessManager.refreshOrderStatus(orderSequenceNumber, targetStatus, validStatusSet);
            }
        }
        if (((permissionType & OrderPermissionTypeEnum.SALE.getKey()) == OrderPermissionTypeEnum.SALE.getKey() || (permissionType & OrderPermissionTypeEnum.SHIPPER.getKey()) == OrderPermissionTypeEnum.SHIPPER.getKey()) && result == -1) {
            if (Long.parseLong(operationPassportId) == orderEntry.getShippingPassportId()) { // 通过
                result = orderDataAccessManager.refreshOrderStatus(orderSequenceNumber, targetStatus, validStatusSet);
            }
        }
        if ((permissionType & OrderPermissionTypeEnum.CONSIGNEE.getKey()) == OrderPermissionTypeEnum.CONSIGNEE.getKey() && result == -1) {
            if (operationPassportId.equals(orderEntry.getReceiptUserId())) { // 通过
                result = orderDataAccessManager.refreshOrderStatus(orderSequenceNumber, targetStatus, validStatusSet);
            }
        }
        if ((permissionType & OrderPermissionTypeEnum.DELIVERY.getKey()) == OrderPermissionTypeEnum.DELIVERY.getKey() && result == -1) {
            if (Long.parseLong(operationPassportId) == orderEntry.getCourierPassportId()) { // 通过
                result = orderDataAccessManager.refreshOrderStatus(orderSequenceNumber, targetStatus, validStatusSet);
            }
        }
        if (result == -1) {
            throw PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.throwException();
        }
        return result >= 1 ? success() : fail();
    }

    @Override
    public JSONObject showOrders() {
        String partnerId = getUTF("partnerId");
        String targetUserId = getUTF("targetUserId");
        long appointFriendPassportId = getLongParameter("appointFriendPassportId", 0);
        byte target = getByteParameter("target");
        int roleType = getIntParameter("roleType");
        int type = getIntParameter("type", -1);
        String orderStatusSet = getUTF("orderStatusSet");
        int pageSize = getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        OrderRoleTypeEnum roleTypeEnum = OrderRoleTypeEnum.getOrderRoleTypeEnum(roleType);
        if (roleTypeEnum == null) {
            throw new XlibaoIllegalArgumentException("错误的角色类型，错误码：" + roleType);
        }
        List<OrderEntry> orders;
        switch (roleTypeEnum) {
            case CONSUMER: // 消费者
                orders = orderDataAccessManager.showConsumerOrders(partnerId, targetUserId, appointFriendPassportId, target, orderStatusSet, type, pageStartIndex, pageSize);
                break;
            case MERCHANT: // 商家
                orders = orderDataAccessManager.showMerchantOrders(partnerId, Long.parseLong(targetUserId), orderStatusSet, type, pageStartIndex, pageSize);
                break;
            case COURIER: // 快递员
            {
                if (target == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) {
                    // 查询未接订单记录
                    List<OrderUnacceptLogger> orderUnacceptLoggers = orderDataAccessManager.getUnacceptLoggers(Long.parseLong(targetUserId), pageStartIndex, pageSize);
                    StringBuilder orderSet = new StringBuilder();
                    for (OrderUnacceptLogger unacceptLogger : orderUnacceptLoggers) {
                        orderSet.append(unacceptLogger.getOrderId()).append(CommonUtils.SPLIT_COMMA);
                    }
                    orderSet.deleteCharAt(orderSet.length() - 1);
                    // TODO
                    orders = orderDataAccessManager.showCourierOrders(partnerId, Long.parseLong(targetUserId), orderStatusSet, type, pageStartIndex, pageSize);
                } else {
                    orders = orderDataAccessManager.showCourierOrders(partnerId, Long.parseLong(targetUserId), orderStatusSet, type, pageStartIndex, pageSize);
                }
            }
            break;
            default:
                throw new XlibaoIllegalArgumentException("错误的角色类型，错误码：" + roleType);
        }
        if (CommonUtils.isEmpty(orders)) {
            throw PlatformErrorCodeEnum.NO_MORE_DATA.throwException(pageStartIndex == 0 ? "没有更多数据" : "已展示全部数据");
        }
        fillOrdersItemSnapshots(orders);

        JSONArray orderArray = orders.stream().map(orderEntry -> JSONObject.parseObject(JSONObject.toJSONString(orderEntry))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("orderArray", orderArray);
        return success(response);
    }

    @Override
    public JSONObject searchOrders() {
        // 搜索订单
        String partnerId = getUTF("partnerId");
        long passportId = getLongParameter("passportId");
        int roleType = getIntParameter("roleType", OrderRoleTypeEnum.MERCHANT.getKey());
        String searchKeyValue = getUTF("searchKeyValue");
        int orderType = getIntParameter("orderType", -1);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        List<OrderEntry> orderEntries = orderDataAccessManager.searchOrders(partnerId, passportId, searchKeyValue, orderType, roleType, pageStartIndex, pageSize);

        if (CommonUtils.isEmpty(orderEntries)) {
            throw PlatformErrorCodeEnum.NO_MORE_DATA.throwException(pageStartIndex == 0 ? "没有更多数据" : "已展示全部数据");
        }
        fillOrdersItemSnapshots(orderEntries);

        JSONArray orderArray = orderEntries.stream().map(orderEntry -> JSONObject.parseObject(JSONObject.toJSONString(orderEntry))).collect(Collectors.toCollection(JSONArray::new));
        return success(orderArray);
    }

    @Override
    public JSONObject modifyItemSnapshot() {
        long orderId = getLongParameter("orderId");
        long itemId = getLongParameter("itemId");
        long normalPrice = getLongParameter("normalPrice");
        long discountPrice = getLongParameter("discountPrice");
        int normalQuantity = getIntParameter("normalQuantity");
        int discountQuantity = getIntParameter("discountQuantity");
        long totalPrice = getLongParameter("totalPrice");

        int result = orderDataAccessManager.modifyItemSnapshot(orderId, itemId, normalPrice, discountPrice, normalQuantity, discountQuantity, totalPrice);

        return result <= 0 ? fail("修改订单商品快照失败") : success("修改订单商品快照成功");
    }

    @Override
    public JSONObject correctOrderPrice() {
        long orderId = getLongParameter("orderId");
        long actualPrice = getLongParameter("actualPrice");
        long totalPrice = getLongParameter("totalPrice");
        long discountPrice = getLongParameter("discountPrice");
        long distributionFee = getLongParameter("distributionFee");
        int deliverType = getIntParameter("deliverType", DeliverTypeEnum.PICKED_UP.getKey());

        int result = orderDataAccessManager.correctOrderPrice(orderId, actualPrice, totalPrice, discountPrice, distributionFee, deliverType);

        return result <= 0 ? fail("修改订单价格失败") : success("修改订单费用成功");
    }

    @Override
    public JSONObject calculationRowNumber() {
        long orderId = getLongParameter("orderId");
        int matchStatus = getIntParameter("matchStatus");

        OrderEntry orderEntry = getOrder(orderId);

        OrderStateLogger orderStateLogger = orderDataAccessManager.getOrderStateLogger(orderId, matchStatus);
        if (orderStateLogger == null) {
            return fail("无法获取订单的状态记录");
        }
        String matchTime = CommonUtils.defineDateFormat(orderStateLogger.getOperatorTime().getTime(), CommonUtils.Y_M_D);

        int beforeRows = orderDataAccessManager.getRowSortStatistics(orderEntry.getShippingPassportId(), orderEntry.getType(), matchStatus, matchTime, orderStateLogger.getId());
        int currentRows = beforeRows + 1;

        int result = orderDataAccessManager.fillDailyRowSort(orderId, currentRows);

        JSONObject response = new JSONObject();
        response.put("rowSort", currentRows);
        return result > 0 ? success(response) : fail("设置排号失败");
    }

    @Override
    public JSONObject findInvalidOrderSize() {
        String partnerId = getUTF("partnerId");
        int orderType = getIntParameter("orderType");
        int matchStatus = getIntParameter("matchStatus");
        long timeout = getLongParameter("timeout");

        int size = orderDataAccessManager.findInvalidOrderSize(partnerId, orderType, matchStatus, CommonUtils.dateFormat(timeout));

        JSONObject response = new JSONObject();
        response.put("size", size);
        return success(response);
    }

    @Override
    public JSONObject batchResetOverdueOrderStatus() {
        String partnerId = getUTF("partnerId");
        int orderType = getIntParameter("orderType");
        long timeout = getLongParameter("timeout");
        int matchStatus = getIntParameter("matchStatus");
        int expectStatus = getIntParameter("expectStatus");

        List<OrderEntry> orders = orderDataAccessManager.findInvalidOrders(partnerId, orderType, matchStatus, CommonUtils.dateFormat(timeout));
        int size = 0;
        for (OrderEntry order : orders) {
            int result = orderDataAccessManager.updateOrderStatus(order.getId(), expectStatus, matchStatus, order.getDeliverStatus(), order.getDeliverStatus());
            if (result > 0) {
                size++;
                if (expectStatus == OrderStatusEnum.ORDER_STATUS_CANCEL.getKey()) {
                    orderEventListenerManager.notifyCanceledOrderEntry(order, matchStatus, true);
                }
            }
        }
        JSONObject response = new JSONObject();
        response.put("size", size);
        return success(response);
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

        int result = orderDataAccessManager.modifyReceivingData(orderSequenceNumber, currentLocation, collectingFees, receiptProvince, receiptCity, receiptDistrict, receiptAddress, receiptNickName, receiptPhone, receiptLocation, remark);
        return result <= 0 ? fail("更新收货地址失败，订单编号：" + orderSequenceNumber) : success("更新收货数据成功");
    }

    @Override
    public JSONObject applyRefund() {
        long passportId = getLongParameter("passportId");
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        String matchStatusSet = getUTF("matchStatusSet", String.valueOf(OrderStatusEnum.ORDER_STATUS_PAYMENT.getKey()));
        String refundReason = getUTF("refundReason", "");

        OrderEntry orderEntry = getOrder(orderSequenceNumber);
        if (!matchStatusSet.contains(String.valueOf(orderEntry.getStatus()))) {
            // 必须处于支付状态才能进行退款
            return OrderErrorCodeEnum.CANNOT_REFUND.response("当前状态不能执行退款操作，状态值：" + orderEntry.getStatus());
        }
        if (passportId != Long.parseLong(orderEntry.getPartnerUserId())) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        int result = orderDataAccessManager.applyRefund(orderEntry.getId(), OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey(), matchStatusSet, refundReason);
        if (result <= 0) { // 预操作，当执行失败时，回滚该操作；否则提交
            return OrderErrorCodeEnum.REFUND_FAIL.response("申请退款失败，请稍后重试！");
        }
        return success("申请成功，请稍后查看订单状态");
    }

    @Override
    public JSONObject refund() {
        long passportId = getLongParameter("passportId");
        String orderSequenceNumber = getUTF("orderSequenceNumber");
        int matchStatus = getIntParameter("matchStatus", OrderStatusEnum.ORDER_STATUS_APPLY_REFUND.getKey());

        OrderEntry orderEntry = getOrder(orderSequenceNumber);
        if (orderEntry.getStatus() != matchStatus) {
            // 必须处于支付状态才能进行退款
            return OrderErrorCodeEnum.CANNOT_REFUND.response("当前状态不能执行退款操作，状态值：" + orderEntry.getStatus());
        }
        if (passportId != Long.parseLong(orderEntry.getPartnerUserId())) {
            return PlatformErrorCodeEnum.NOT_HAVE_PERMISSION.response();
        }
        int result = orderDataAccessManager.updateOrderStatus(orderEntry.getId(), OrderStatusEnum.ORDER_STATUS_REFUND.getKey(), matchStatus, orderEntry.getDeliverStatus(), orderEntry.getDeliverStatus());
        if (result <= 0) { // 预操作，当远程失败时，回滚该操作；否则提交
            return OrderErrorCodeEnum.REFUND_FAIL.response("退款失败，请稍后重试！");
        }
        // 远程操作
        JSONObject response = SharePaymentRemoteService.refund(ConfigFactory.getOrderConfig().getPartnerId(), ConfigFactory.getOrderConfig().getPaymentAppId(), ConfigFactory.getOrderConfig().getPaymentAppkey(),
                orderSequenceNumber, ConfigFactory.getDomainNameConfig().paymentRemoteURL);
        if (response.getIntValue("code") != BasicWebService.SUCCESS_CODE) { // 失败时，抛异常
            throw new XlibaoRuntimeException(response.getIntValue("code"), response.getString("msg"));
        }
        orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_REFUND.getKey()); // 更新为退款状态
        orderEventListenerManager.notifyOrderRefund(orderEntry, matchStatus); // 通知退款结果
        return response;
    }

    private OrderEntry getOrder(String orderSequenceNumber) {
        OrderEntry order = orderDataAccessManager.getOrder(orderSequenceNumber);

        if (order == null) {
            logger.error("订单不存在，错误订单序号：" + orderSequenceNumber);
            throw new XlibaoIllegalArgumentException("订单不存在，错误订单序号：" + orderSequenceNumber);
        }
        if (order.getStatus() == OrderStatusEnum.ORDER_STATUS_INVALID.getKey()) {
            logger.error("订单已被逻辑移除，错误订单序号：" + orderSequenceNumber);
            throw new XlibaoIllegalArgumentException("订单不存在，错误订单序号：" + orderSequenceNumber);
        }
        return order;
    }

    private OrderEntry getOrder(long orderId) {
        OrderEntry order = orderDataAccessManager.getOrder(orderId);
        if (order == null) {
            logger.error("订单不存在，错误订单号：" + orderId);
            throw new XlibaoIllegalArgumentException("订单不存在，错误订单号：" + orderId);
        }
        if (order.getStatus() == OrderStatusEnum.ORDER_STATUS_INVALID.getKey()) {
            logger.error("订单已被逻辑移除，错误订单号：" + orderId);
            throw new XlibaoIllegalArgumentException("订单不存在，错误订单号：" + orderId);
        }
        return order;
    }

    private List<OrderEntry> getOrders(String sequenceNumber) {
        List<OrderEntry> orders = orderDataAccessManager.getOrders(sequenceNumber);
        if (CommonUtils.isEmpty(orders)) {
            logger.error("订单不存在，错误订单序列号：" + sequenceNumber);
            throw new XlibaoIllegalArgumentException("订单不存在，错误订单序列号：" + sequenceNumber);
        }
        orders.stream().filter(order -> order.getStatus() == OrderStatusEnum.ORDER_STATUS_INVALID.getKey()).forEach(orders::remove);
        if (CommonUtils.isEmpty(orders)) {
            logger.error("订单已被全部逻辑移除，订单序列号：" + sequenceNumber);
            throw new XlibaoRuntimeException("订单不存在或已被删除，订单序列号：" + sequenceNumber);
        }
        return orders;
    }

    private List<OrderEntry> getOrderForSequenceSet(String orderSequenceSet) {
        return orderDataAccessManager.getOrderForSequenceSet(orderSequenceSet);
    }

    private OrderSequence usePreparedOrder(String partnerId, String partnerUserId, String sequenceNumber) {
        OrderSequence orderSequence = orderDataAccessManager.findOrderSequence(sequenceNumber);
        if (orderSequence == null) {
            logger.error("合作商户(" + partnerId + ")的用户(" + partnerUserId + ")请求下单，但提供的序列号(" + sequenceNumber + ")无法找到预下单记录");
            throw new XlibaoRuntimeException(1, "无效的订单序列号[" + sequenceNumber + "]");
        }
        if (orderSequence.getStatus() == OrderSequenceNumberStatusEnum.HAS_USE.getKey()) {
            logger.error("合作商户(" + partnerId + ")的用户(" + partnerUserId + ")请求下单，但提供的序列号(" + sequenceNumber + ")已被使用，可能重复提交了请求");
            throw new XlibaoRuntimeException(2, "下单失败，序列号已被使用！");
        }
        if (orderSequence.getStatus() == OrderSequenceNumberStatusEnum.INVALID.getKey()) {
            logger.error("合作商户(" + partnerId + ")的用户(" + partnerUserId + ")请求下单，但提供的序列号(" + sequenceNumber + ")已过期");
            throw new XlibaoRuntimeException(3, "序列号已失效，尝试返回购物车后重试！");
        }
        if ((orderSequence.getValidityTermSecond() * 1000 + orderSequence.getCreateTime().getTime()) < System.currentTimeMillis()) {
            logger.error("合作商户(" + partnerId + ")的用户(" + partnerUserId + ")请求下单，但提供的序列号(" + sequenceNumber + ")已过期");
            // 超过有效期时设置为无效状态
            throw new XlibaoRuntimeException(3, "序列号已失效，尝试返回购物车后重试！");
        }
        int result = orderDataAccessManager.updateOrderSequence(sequenceNumber, OrderSequenceNumberStatusEnum.HAS_USE);
        if (result <= 0) {
            logger.error("设置序列号状态失败，合作商户(" + partnerId + ")的用户(" + partnerUserId + ")，sequenceNumber: " + sequenceNumber);
            throw new XlibaoRuntimeException("重复下单，请到订单列表查看是否已下单成功！");
        }
        return orderSequence;
    }

    private JSONObject generateSaleOrder(OrderSequence orderSequence) {
        String orders = getUTF("orders");
        logger.info("生成订单，订单请求数据：" + orders);
        JSONArray orderArray = JSONArray.parseArray(orders);

        JSONArray orderEntries = new JSONArray();
        for (int i = 0; i < orderArray.size(); i++) {
            JSONObject order = orderArray.getJSONObject(i);

            OrderEntry orderEntry = createOrder(orderSequence, order);
            orderEntries.add(JSONObject.parseObject(JSONObject.toJSONString(orderEntry)));
        }
        JSONObject response = new JSONObject();
        response.put("orderArray", orderEntries);
        return success("订单创建成功", response);
    }

    private JSONObject generateScanOrder(OrderSequence orderSequence) {
        int userSource = getIntParameter("userSource", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey());
        String transType = getUTF("transType", "NATIVE");

        // 发货用户的通行证ID(合作商户ID)
        long shippingPassportId = getLongParameter("shippingPassportId", 0);
        String shippingNickName = getUTF("shippingNickName");

        String receiptNickName = getUTF("receiptNickName", "");
        String receiptPhone = getUTF("receiptPhone", "");
        String receiptLocation = getUTF("receiptLocation", GlobalConstantConfig.INVALID_LOCATION);

        long totalAmount = getLongParameter("totalAmount", 0);
        long actualAmount = getLongParameter("actualAmount", totalAmount);
        long discountAmount = getLongParameter("discountAmount", 0);
        String priceLogger = getUTF("priceLogger", "");

        String body = getUTF("body", "");
        String detail = getUTF("detail", "");

        OrderEntry orderEntry = fillScanOrderEntry(orderSequence, userSource, transType, body, detail, totalAmount, actualAmount, discountAmount, priceLogger, shippingPassportId, shippingNickName, receiptNickName, receiptPhone, receiptLocation);
        return createOrder(orderEntry);
    }

    private OrderEntry createOrder(OrderSequence orderSequence, JSONObject order) {
        OrderEntry orderEntry = JSONObject.parseObject(order.toJSONString(), OrderEntry.class);

        orderEntry.setSequenceNumber(orderSequence.getSequenceNumber());
        orderEntry.setOrderSequenceNumber(uniquePrimaryKey(String.valueOf(orderSequence.getType())));
        orderEntry.setPartnerId(orderSequence.getPartnerId());
        orderEntry.setPartnerUserId(orderSequence.getPartnerUserId());

        // orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        orderEntry.setDeliverStatus(0);
        orderEntry.setTransType(PaymentTypeEnum.UNKNOWN.getKey());
        orderEntry.setType(orderSequence.getType());

        String currentLocation = order.getString("currentLocation");
        String receiptLocation = order.getString("receiptLocation");

        long totalDistance = SimpleLocationUtils.simpleDistance(Double.parseDouble(currentLocation.split(CommonUtils.SPLIT_COMMA)[0]), Double.parseDouble(currentLocation.split(CommonUtils.SPLIT_COMMA)[1]), Double.parseDouble(receiptLocation.split(CommonUtils.SPLIT_COMMA)[0]), Double.parseDouble(receiptLocation.split(CommonUtils.SPLIT_COMMA)[1]));
        orderEntry.setTotalDistance(totalDistance);

        createOrder(orderEntry);

        return orderEntry;
    }

    private OrderEntry fillScanOrderEntry(OrderSequence orderSequence, int userSource, String transType, String body, String detail, long totalPrice, long actualPrice, long discountPrice, String priceLogger, long shippingRoleId, String shippingNickName, String receiptNickName, String receiptPhone, String receiptLocation) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setSequenceNumber(orderSequence.getSequenceNumber());
        orderEntry.setPartnerId(orderSequence.getPartnerId());
        orderEntry.setPartnerUserId(orderSequence.getPartnerUserId());
        orderEntry.setType(OrderTypeEnum.SCAN_ORDER_TYPE.getKey());
        orderEntry.setUserSource(userSource);
        orderEntry.setTransType(transType);
        orderEntry.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        orderEntry.setBody(body);
        orderEntry.setDetail(detail);

        orderEntry.setTotalPrice(totalPrice);
        orderEntry.setActualPrice(actualPrice);
        orderEntry.setDiscountPrice(discountPrice);
        orderEntry.setPriceLogger(CommonUtils.nullToEmpty(priceLogger));

        orderEntry.setShippingPassportId(shippingRoleId);
        orderEntry.setShippingNickName(shippingNickName);

        orderEntry.setReceiptNickName(receiptNickName);
        orderEntry.setReceiptPhone(CommonUtils.nullToEmpty(receiptPhone));
        orderEntry.setReceiptLocation(receiptLocation);
        return orderEntry;
    }

    private JSONObject fillOrderMsg(OrderEntry orderEntry) {
        fillOrderItemSnapshots(orderEntry);

        List<OrderStateLogger> stateLoggers = orderDataAccessManager.getOrderStateLoggers(orderEntry.getId());
        orderEntry.setStateLoggers(stateLoggers);
        return JSONObject.parseObject(JSONObject.toJSONString(orderEntry));
    }

    private List<OrderItemSnapshot> fillOrderItemSnapshots(OrderEntry orderEntry) {
        List<OrderItemSnapshot> itemSnapshots = orderDataAccessManager.getOrderItemSnapshots(orderEntry.getId());
        orderEntry.setItemSnapshots(itemSnapshots);

        return itemSnapshots == null ? Collections.emptyList() : itemSnapshots;
    }

    private void fillOrdersItemSnapshots(List<OrderEntry> orderEntries) {
        StringBuilder orderSet = new StringBuilder();
        for (OrderEntry orderEntry : orderEntries) {
            orderSet.append(orderEntry.getId()).append(CommonUtils.SPLIT_COMMA);
        }
        orderSet.deleteCharAt(orderSet.length() - 1);

        List<OrderItemSnapshot> orderItemSnapshots = orderDataAccessManager.batchGetOrderItemSnapshots(orderSet.toString());
        if (CommonUtils.isEmpty(orderItemSnapshots)) {
            return;
        }
        Map<Long, List<OrderItemSnapshot>> orderItemSnapshotMap = new HashMap<>();

        for (OrderItemSnapshot itemSnapshot : orderItemSnapshots) {
            List<OrderItemSnapshot> l = orderItemSnapshotMap.get(itemSnapshot.getOrderId());
            if (l == null) {
                l = new ArrayList<>();
                orderItemSnapshotMap.put(itemSnapshot.getOrderId(), l);
            }
            l.add(itemSnapshot);
        }
        for (OrderEntry orderEntry : orderEntries) {
            List<OrderItemSnapshot> itemSnapshots = orderItemSnapshotMap.get(orderEntry.getId());
            orderEntry.setItemSnapshots(itemSnapshots);
        }
    }

    private JSONObject createOrder(OrderEntry orderEntry) {
        int result = orderDataAccessManager.createOrderEntry(orderEntry);
        if (result >= 1) {
            // 通知监听系统
            orderEventListenerManager.notifyCreatedOrderEntry(orderEntry);
            JSONObject response = new JSONObject();
            response.put("orderId", orderEntry.getId());
            return success("创建订单成功", response);
        }
        return fail("创建订单失败");
    }

    private Map<Long, OrderItemSnapshot> getItemSnapshotMap(OrderEntry orderEntry) {
        List<OrderItemSnapshot> itemSnapshots = orderEntry.getItemSnapshots();
        if (CommonUtils.isEmpty(itemSnapshots)) {
            return Collections.emptyMap();
        }
        Map<Long, OrderItemSnapshot> itemSnapshotMap = new HashMap<>();
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            itemSnapshotMap.put(itemSnapshot.getId(), itemSnapshot);
        }
        return itemSnapshotMap;
    }

    private boolean endArriver(List<OrderItemSnapshot> itemSnapshots) {
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            if (itemSnapshot.totalQuantity() > (itemSnapshot.getReceiptQuantity() + itemSnapshot.getReturnQuantity())) {
                return false;
            }
        }
        return true;
    }


    @Override
    public JSONObject searchPageOrders() {
        //店铺ID
        long marketId = getLongParameter("marketId", -1);
        int orderState = getIntParameter("orderState", -1);
        String startTime = getUTF("sTime", null);
        String endTime = getUTF("eTime", null);
        String searchType = getUTF("searchType", null);
        String searchKey = getUTF("searchKey", null);

        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<OrderEntry> orderEntries = orderDataAccessManager.searchPageOrders(marketId, orderState, startTime, endTime, searchType, searchKey, pageSize, pageStartIndex);
        int count = orderDataAccessManager.searchPageOrderCount(marketId, orderState, startTime, endTime, searchType, searchKey, pageSize, pageStartIndex);

        JSONObject response = new JSONObject();
        response.put("data", orderEntries);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

}