package com.xlibao.order.service.order;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderStateLogger;
import com.xlibao.metadata.order.OrderStatusListener;
import com.xlibao.order.data.mapper.order.OrderDataAccessManager;
import com.xlibao.order.listener.OrderEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/24.
 */
@Component
public class InternalOrderEventListenerImpl implements OrderEventListener {

    @Autowired
    private OrderDataAccessManager orderDataAccessManager;

    @Override
    public void notifyCreatedOrderEntry(OrderEntry orderEntry) {
        // 新增新建订单日志
        createOrderStateLogger(orderEntry, 0, orderEntry.getPartnerUserId(), orderEntry.getReceiptNickName(), "新建“" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyPaymentOrderEntry(OrderEntry orderEntry, int beforeStatus) {
        // 新增支付订单日志
        createOrderStateLogger(orderEntry, beforeStatus, orderEntry.getPartnerUserId(), orderEntry.getReceiptNickName(), "支付“" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”，支付方式：" + orderEntry.getTransType());

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyPushedOrderEntry(OrderEntry orderEntry, int pushType, String pushTitle, String pushContent, byte write, String... targets) {
        for (String s : targets) { // 建立未接单记录
            if (write == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) {
                orderDataAccessManager.createUnacceptLogger(orderEntry.getId(), Long.parseLong(s));
            }
            orderDataAccessManager.createPushedLogger(orderEntry.getId(), Long.parseLong(s), pushType, pushTitle, pushContent);
        }
    }

    @Override
    public void notifyAcceptedOrderEntry(OrderEntry orderEntry, int beforeStatus) {
        // 新增支付订单日志
        createOrderStateLogger(orderEntry, beforeStatus, String.valueOf(orderEntry.getCourierPassportId()), orderEntry.getCourierNickName(), "接取“" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyDistributionOrder(OrderEntry orderEntry, int beforeStatus) {
        createOrderStateLogger(orderEntry, beforeStatus, String.valueOf(orderEntry.getCourierPassportId()), orderEntry.getCourierNickName(), "配送“" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyArrivedOrderEntry(OrderEntry orderEntry, int beforeStatus) {
        createOrderStateLogger(orderEntry, beforeStatus, String.valueOf(orderEntry.getCourierPassportId()), orderEntry.getCourierNickName(), "送达“" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyConfirmedOrderEntry(OrderEntry orderEntry, int beforeStatus) {
        createOrderStateLogger(orderEntry, beforeStatus, orderEntry.getPartnerUserId(), orderEntry.getCourierNickName(), "确认“" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyCanceledOrderEntry(OrderEntry orderEntry, int beforeStatus, boolean isAuto) {
        // 新增取消订单日志
        createOrderStateLogger(orderEntry, beforeStatus, orderEntry.getPartnerUserId(), orderEntry.getReceiptNickName(), (isAuto ? "自动取消“" : "取消“") + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    @Override
    public void notifyOrderRefund(OrderEntry orderEntry, int beforeStatus) {
        // 新增退款订单日志
        createOrderStateLogger(orderEntry, beforeStatus, orderEntry.getPartnerUserId(), orderEntry.getReceiptNickName(), "发起退款请求 “" + OrderTypeEnum.getOrderTypeEnum(orderEntry.getType()).getValue() + "”");

        notifyEventListener(orderEntry);
    }

    private void notifyEventListener(final OrderEntry orderEntry) {
        Runnable runnable = () -> {
            try {
                OrderStatusListener statusListener = orderDataAccessManager.getOrderStatusListener(orderEntry.getPartnerId(), orderEntry.getStatus());
                if (statusListener == null) {
                    return;
                }
                String callbackURL = statusListener.getCallbackUrl();

                Map<String, String> parameters = new HashMap<>();
                parameters.put("data", JSONObject.toJSONString(orderEntry));

                HttpRequest.post(callbackURL, parameters);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
        AsyncScheduledService.submitImmediateRemoteNotifyTask(runnable);
    }

    private void createOrderStateLogger(OrderEntry orderEntry, int beforeStatus, String operatorPassportId, String operatorName, String remark) {
        OrderStateLogger orderStateLogger = new OrderStateLogger();
        orderStateLogger.setOrderId(orderEntry.getId());
        orderStateLogger.setShippingPassportId(orderEntry.getShippingPassportId());
        orderStateLogger.setOrderType(orderEntry.getType());
        orderStateLogger.setBeforeStatus(beforeStatus);
        orderStateLogger.setStatus(orderEntry.getStatus());
        orderStateLogger.setOperatorPassportId(operatorPassportId);
        orderStateLogger.setOperatorName(operatorName);
        orderStateLogger.setOperatorDescribe(remark);
        orderDataAccessManager.createOrderStateLogger(orderStateLogger);
    }
}