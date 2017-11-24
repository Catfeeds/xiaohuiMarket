package com.xlibao.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.order.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/8.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * <pre>
     *     <b>预创建订单</b>
     *
     *     <b>请求地址：</b>http://domainName.com/order/prepareCreateOrder
     *
     *     <b>注意：</b>该序列号只对指定的订单类型有效
     *     主要是生成一个订单序列号，且为该序列号生成一个控制期；
     *
     *     <b>入参：</b>
     *          <b>partnerId</b> - String 下单的商户号，必填参数
     *          <b>appId</b> - String 分配的合作商户应用ID，必填参数
     *          <b>partnerUserId</b> - String 下单商户的用户ID，必填参数；
     *          <b>orderType</b> - int 订单的类型，具体参考{@linkplain com.xlibao.common.constant.order.OrderTypeEnum}
     *          <b>sign</b> -- String 消息签名，签名方式详见{@linkplain com.xlibao.common.GlobalConstantConfig#signatureRule}
     *
     *     <b>返回：</b>
     *          <b>sequenceNumber</b> - String 可用的序列号；序列号有效期为 {@linkplain com.xlibao.common.GlobalConstantConfig#SEQUENCE_NUMBER_VALIDITY_TERM_SECOND}(单位：秒)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "prepareCreateOrder")
    public JSONObject prepareCreateOrder() {
        return orderService.prepareCreateOrder();
    }

    /**
     * <pre>
     *      <b>生成一个订单</b>
     *
     *      <b>请求地址：</b>http://domainName/order/generateOrder
     *
     *      需检查序列号是否已被使用，若序列号已被使用，直接通知重复下单；
     *      若序列号过期，则通知前端重新发起序列号请求；
     *      下单完成后需将序列号置为已使用状态；{@linkplain com.xlibao.common.constant.order.OrderSequenceNumberStatusEnum#INVALID}
     *      收货地址可以部分为默认值，但具体收货地址必须提供；默认来源为安卓；
     *      前端必须提供用户当前所在地址经纬度，主要是为了后期的数据检查使用(埋点)
     *
     *      <b>默认非代收业务</b>
     *
     *      <b>入参：</b>
     *
     *      <b>partnerId</b> - String 下单的商户号，必填参数；
     *      <b>appId</b> - String 分配的合作商户应用ID
     *      <b>sequenceNumber</b> - String 预下单的序列号，必填参数
     *      <b>partnerUserId</b> - String 下游商户的用户ID，非必填参数；若可提供，请尽量提供，后期统计和数据校验需要用到
     *
     *      当提供的<b>sequenceNumber</b>(预生成时决定)
     *          为：<b>{@linkplain com.xlibao.common.constant.order.OrderTypeEnum#SCAN_ORDER_TYPE}</b>时：
     *          为：<b>{@linkplain com.xlibao.common.constant.order.OrderTypeEnum#ALLOCATION_ORDER_TYPE}</b>时：
     *          为：<b>{@linkplain com.xlibao.common.constant.order.OrderTypeEnum#PURCHASE_ORDER_TYPE}</b>时：
     *          为：<b>{@linkplain com.xlibao.common.constant.order.OrderTypeEnum#SCAN_ORDER_TYPE}</b>时：
     *              <b>userSource</b> - int 用户来源，具体参考：{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum}
     *                          默认为{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum#DEVICE_TYPE_ANDROID}
     *              <b>transType</b> - String 交易的类型，非必填，如微信的JSAPI、APP等
     *              <b>shippingPassportId</b> - long 交易的主人方通行证ID，必填
     *              <b>shippingNickName</b> - String 交易方发起者的角色昵称，必须是我方体系内的用户；必填
     *
     *              <b>receiptNickName</b> - String 收货人昵称，非必填
     *              <b>receiptPhone</b> - String 收货人联系电话，非必填
     *              <b>receiptLocation</b> - String 收货地址经纬度
     *
     *              <b>totalAmount</b> - long 交易的总额度，必填
     *              <b>actualAmount</b> - long 实收额度，非必填
     *              <b>discountAmount</b> - long 优惠的额度，非必填
     *              <b>priceLogger</b> - String 价格的说明内容，非必填
     *
     *          为：<b>{@linkplain com.xlibao.common.constant.order.OrderTypeEnum#CHANNEL_ORDER_TYPE}</b>时：
     *          为：<b>{@linkplain com.xlibao.common.constant.order.OrderTypeEnum#PARTNER_ORDER_TYPE}</b>时：
     *
     *          <b>body</b> - String 商品的介绍内容，非必填
     *          <b>detail</b> - String 具体的商品内容，非我方体系内的数据，非必填
     *
     *
     *      <b>sign</b> -- String 消息签名
     *
     *      返回：
     *
     *      <b>code</b> - int 0：成功 其他值：失败
     *      <b>msg</b> - String 结果提示内容
     *      <b>response</b> - JSONObject
     *          <b>orderId</b> - long 生成成功后的订单ID
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "generateOrder")
    public JSONObject generateOrder() {
        return orderService.generateOrder();
    }

    /**
     * <pre>
     *     <b>修改收货数据</b>
     *
     *     <b>请求地址：</b>http://domainName/order/modifyReceivingData
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>orderSequenceNumber</b> - String 订单序号
     *          <b>currentLocation</b> - String 当前位置信息，非必填参数；尽量提供，方便后期跟踪；格式为：latitude,longitude。
     *          <b>collectingFees</b> - byte 代收费用，非必填参数；默认为{@linkplain com.xlibao.common.constant.order.CollectingFeesEnum#UN_COLLECTION} 不代收。
     *          <b>receiptProvince</b> - String 收货省份，必填参数。
     *          <b>receiptCity</b> - String 收货城市，必填参数。
     *          <b>receiptDistrict</b> - String 收货区域，必填参数
     *          <b>receiptAddress</b> - String 具体收货地址，必填参数。
     *          <b>receiptNickName</b> - String 收货人昵称，必填参数。
     *          <b>receiptPhone</b> - String 收货人联系号码，必填参数。
     *          <b>receiptLocation</b> - String 收货地址经纬度，非必填参数；请尽量提供，方便距离跟踪；格式为：latitude,longitude。
     *          <b>remark</b> - String 描述内容(备注)。
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "modifyReceivingData")
    public JSONObject modifyReceivingData() {
        return orderService.modifyReceivingData();
    }

    /**
     * <pre>
     *     <b>获取订单详情</b>
     *
     *     <b>请求地址：</b>http://domainName.com/order/getOrder
     *
     *     <b>参数：</b>
     *          <b>partnerId</b> - String 下单的商户号，必填参数
     *          <b>appId</b> - String 分配的合作商户应用ID，必填参数
     *          <b>orderId</b> -- long 订单ID
     *          <b>sign</b> -- String 消息签名，签名方式详见{@linkplain com.xlibao.common.GlobalConstantConfig#signatureRule}
     *
     *     <b>返回：</b>
     *          <b>完整的订单实体对象；</b>参考：{@linkplain com.xlibao.metadata.order.OrderEntry}
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getOrder")
    public JSONObject getOrder() {
        return orderService.getOrder();
    }

    @ResponseBody
    @RequestMapping(value = "getOrderForSequenceNumber")
    public JSONObject getOrderForSequenceNumber() {
        return orderService.getOrderForSequenceNumber();
    }

    @ResponseBody
    @RequestMapping(value = "getOrderForSequenceSet")
    public JSONObject getOrderForSequenceSet() {
        return orderService.getOrderForSequenceSet();
    }

    /**
     * <pre>
     *     <b>获取订单详情</b>
     *
     *     <b>请求地址：</b>http://domainName.com/order/getOrders
     *
     *     <b>参数：</b>
     *          <b>partnerId</b> - String 下单的商户号，必填参数
     *          <b>appId</b> - String 分配的合作商户应用ID，必填参数
     *          <b>sequenceNumber</b> -- String 订单序号
     *          <b>sign</b> -- String 消息签名，签名方式详见{@linkplain com.xlibao.common.GlobalConstantConfig#signatureRule}
     *
     *     <b>返回：</b>
     *          <b>orderArray</b> - JSONArray 订单集合
     *              <b>完整的订单实体对象；</b>参考：{@linkplain com.xlibao.metadata.order.OrderEntry}
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getOrders")
    public JSONObject getOrders() {
        return orderService.getOrders();
    }

    /**
     * <pre>
     *     <b>取消订单</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "cancelOrder")
    public JSONObject cancelOrder() {
        return orderService.cancelOrder();
    }

    /**
     * <pre>
     *     <b>支付订单</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "paymentOrder")
    public JSONObject paymentOrder() {
        return orderService.paymentOrder();
    }

    /**
     * <pre>
     *     <b>推送订单</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "pushOrderMsg")
    public JSONObject pushOrderMsg() {
        return orderService.pushOrderMsg();
    }

    @ResponseBody
    @RequestMapping(value = "unAcceptOrderSize")
    public JSONObject unAcceptOrderSize() {
        return orderService.unAcceptOrderSize();
    }

    /**
     * <pre>
     *     <b>统一接单入口</b>
     *
     *     <b>请求地址：</b>http://domainName/order/acceptOrder
     *     <b>请求方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>courierPassportId</b> - long 快递员通行证ID
     *
     *     <b>结果：</b>仅通知成功或失败
     *          <b>orderId</b> - long 被接的订单ID
     *          <b>partnerId</b> - String 订单归属的合作商户号
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "acceptOrder")
    public JSONObject acceptOrder() {
        return orderService.acceptOrder();
    }

    /**
     * <pre>
     *     <b>订单发货</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "deliverOrder")
    public JSONObject deliverOrder() {
        return orderService.deliverOrder();
    }

    /**
     * <pre>
     *     <b>配送订单</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "distributionOrder")
    public JSONObject distributionOrder() {
        return orderService.distributionOrder();
    }

    /**
     * <pre>
     *     <b>订单送达</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "arriveOrder")
    public JSONObject arriveOrder() {
        return orderService.arriveOrder();
    }

    /**
     * <pre>
     *     <b>确认订单</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "confirmOrder")
    public JSONObject confirmOrder() {
        return orderService.confirmOrder();
    }

    @ResponseBody
    @RequestMapping(value = "confirmOfPayment")
    public JSONObject confirmOfPayment() {
        return orderService.confirmOfPayment();
    }

    @ResponseBody
    @RequestMapping(value = "batchReceipt")
    public JSONObject batchReceipt() {
        return orderService.batchReceipt();
    }

    @ResponseBody
    @RequestMapping(value = "refreshOrderStatus")
    public JSONObject refreshOrderStatus() {
        return orderService.refreshOrderStatus();
    }

    /**
     * <pre>
     *     <b>显示订单列表</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showOrders")
    public JSONObject showOrders() {
        return orderService.showOrders();
    }

    /**
     * <pre>
     *     <b>搜索订单</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "searchOrders")
    public JSONObject searchOrders() {
        return orderService.searchOrders();
    }

    /**
     * <pre>
     *     <b>修改商品快照数据</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "modifyItemSnapshot")
    public JSONObject modifyItemSnapshot() {
        return orderService.modifyItemSnapshot();
    }

    @ResponseBody
    @RequestMapping(value = "correctOrderPrice")
    public JSONObject correctOrderPrice() {
        return orderService.correctOrderPrice();
    }

    @ResponseBody
    @RequestMapping(value = "calculationRowNumber")
    public JSONObject calculationRowNumber() {
        return orderService.calculationRowNumber();
    }

    @ResponseBody
    @RequestMapping(value = "findInvalidOrderSize")
    public JSONObject findInvalidOrderSize() {
        return orderService.findInvalidOrderSize();
    }

    @ResponseBody
    @RequestMapping(value = "batchResetOverdueOrderStatus")
    public JSONObject batchResetOverdueOrderStatus() {
        return orderService.batchResetOverdueOrderStatus();
    }

    @ResponseBody
    @RequestMapping(value = "searchPageOrders")
    public JSONObject searchPageOrders() {
        return orderService.searchPageOrders();
    }
}