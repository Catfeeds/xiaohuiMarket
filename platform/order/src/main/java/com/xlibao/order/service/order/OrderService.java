package com.xlibao.order.service.order;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/2/8.
 */
public interface OrderService {

    /**
     * <pre>
     *     <b>预创建订单</b>
     *     主要是生成一个订单序列号，且为该序列号生成一个控制期；<b>注意：</b>该序列号只对指定的订单类型有效
     * </pre>
     */
    JSONObject prepareCreateOrder();

    /**
     * <pre>
     *     <b>生成订单</b>
     * </pre>
     */
    JSONObject generateOrder();

    JSONObject unifiedPayment();

    JSONObject callbackPaymentOrder();

    /**
     * <pre>
     *     <b>获取订单</b>
     * </pre>
     */
    JSONObject getOrder();

    JSONObject getOrderForSequenceNumber();

    /**
     * <pre>
     *     <b>获取订单集合</b>
     * </pre>
     */
    JSONObject getOrders();

    JSONObject getOrderForSequenceSet();

    /**
     * <pre>
     *     <b>取消订单</b>
     * </pre>
     */
    JSONObject cancelOrder();

    /**
     * <pre>
     *     <b>支付订单</b>
     * </pre>
     */
    JSONObject paymentOrder();

    /**
     * <pre>
     *     <b>推送订单信息</b>
     * </pre>
     */
    JSONObject pushOrderMsg();

    JSONObject unAcceptOrderSize();

    /**
     * <pre>
     *     <b>接受订单</b>
     * </pre>
     */
    JSONObject acceptOrder();

    /**
     * <pre>
     *     <b>发货</b>
     * </pre>
     */
    JSONObject deliverOrder();

    /**
     * <pre>
     *     <b>配送</b>
     * </pre>
     */
    JSONObject distributionOrder();

    /**
     * <pre>
     *     <b>送达</b>
     * </pre>
     */
    JSONObject arriveOrder();

    JSONObject confirmOrder();

    JSONObject confirmOfPayment();

    JSONObject batchReceipt();

    JSONObject refreshOrderStatus();

    /**
     * <pre>
     *     <b>展示订单</b>
     * </pre>
     */
    JSONObject showOrders();

    /**
     * <pre>
     *     <b>搜索订单</b>
     * </pre>
     */
    JSONObject searchOrders();

    /**
     * <pre>
     *     <b>修改商品快照信息</b>
     * </pre>
     */
    JSONObject modifyItemSnapshot();

    /**
     * <pre>
     *     <b>修正订单费用</b>
     * </pre>
     */
    JSONObject correctOrderPrice();

    JSONObject calculationRowNumber();

    JSONObject findInvalidOrderSize();

    JSONObject batchResetOverdueOrderStatus();

    JSONObject searchPageOrders();

    JSONObject modifyReceivingData();

    JSONObject applyRefund();

    JSONObject refund();
}