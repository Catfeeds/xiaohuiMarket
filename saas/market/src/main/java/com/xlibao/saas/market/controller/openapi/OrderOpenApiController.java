package com.xlibao.saas.market.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/8/10.
 */
@Controller
@RequestMapping(value = "/market/order/openapi")
public class OrderOpenApiController {

    @Autowired
    private OrderService orderService;

    /**
     * <pre>
     *     <b>订单列表展示</b>
     *
     *     <b>访问地址：</b>http://domainName/order/showOrders
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>访问参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>marketId</b> - long 指定的商店ID，非必填参数；为0时表示不指定。
     *          <b>roleType</b> - int 角色类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderRoleTypeEnum}。
     *          <b>orderType</b> - int 订单类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderTypeEnum}。
     *          <b>statusEnter</b> - int 状态入口，必填参数；具体参考：{@linkplain com.xlibao.saas.market.service.order.StatusEnterEnum}。
     *          <b>pageIndex</b> - int 页码，非必填参数；默认为：{@linkplain com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_INDEX}。
     *          <b>pageSize</b> - int 显示数量，非必填参数；默认为：{@linkplain com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_SIZE}。
     *
     *     <b>返回结果：</b>
     *          <b>datas</b> - JSONArray 订单集合，每个元素为一个订单，结构为JSONObject，数据结构：
     *              以下部分为公用数据内容，由于不同的角色需要的数据不同，将会根据请求的角色类型做部分数据的区分
     *              <b>sequenceNumber</b> - String 公用序号，预下单时生产
     *              <b>orderId</b> - long 订单ID
     *              <b>orderSequenceNumber</b> - String 订单序列号，下单时生成，每个订单独立
     *
     *              以下为区分角色的数据内容
     *              当<b>roleType</b>为{@linkplain com.xlibao.common.constant.order.OrderRoleTypeEnum#CONSUMER} -- 消费者时
     *                  <b>marketId</b> - long 店铺ID
     *                  <b>marketName</b> - String 店铺名
     *                  <b>marketFormatAddress</b> - String 店铺地址(格式化后)
     *                  <b>actualPrice</b> - long 实收价格，单位：分
     *                  <b>orderStatus</b> - int 订单状态，参考：{@link com.xlibao.common.constant.order.OrderStatusEnum}
     *                  <b>orderStatusTitle</b> - String 订单状态标题
     *                  <b>deliverType</b> - int 配送类型，参考：{@link com.xlibao.common.constant.order.DeliverTypeEnum}
     *                  <b>deliverTitle</b> - String 配送标题
     *                  <b>paymentTime</b> - long 支付时间，为0时表示未支付
     *                  <b>totalItemQuantity</b> - int 商品数量
     *                  <b>items</b> - JSONArray 商品信息，每个元素为JSONObject结构
     *                      <b>itemSnapshotId</b> - long 商品快照ID
     *                      <b>itemId</b> - long 商品ID
     *                      <b>itemTemplateId</b> - long 商品模版ID
     *                      <b>name</b> - String 商品名
     *                      <b>image</b> - String 商品图片
     *                      <b>price</b> - long 商品单价(原价)，单位：分
     *                      <b>quantity</b> - int 商品数量
     *                      <b>totalPrice</b> - long 商品总价(已计算了优惠)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showOrders")
    public JSONObject showOrders() {
        return orderService.showOrders();
    }

    /**
     * <pre>
     *     <b>查看订单详情</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/orderDetail
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>访问参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>roleType</b> - int 角色类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderRoleTypeEnum}。
     *          <b>orderId</b> - long 订单ID，必填参数。
     *
     *     <b>返回结果：</b>
     *          <b>marketId</b>
     *          <b>marketName</b>
     *          <b>marketAddress</b>
     *
     *          <b>orderId</b> - long 订单ID
     *          <b>sequenceNumber</b> - String 公用序号，预下单时生产
     *          <b>orderSequenceNumber</b> - String 订单序列号
     *          <b>createTime</b> - String 下单时间，格式：yyyy-MM-dd HH:mm:ss
     *          <b>deliverType</b> - int 配送类型，参考：{@link com.xlibao.common.constant.order.DeliverTypeEnum#PICKED_UP} -- 自提
     *                                              {@link com.xlibao.common.constant.order.DeliverTypeEnum#DISTRIBUTION} -- 配送
     *          <b>deliverValue</b> - String 用于展示的配送方式；如："到店自提"、"小惠配送"
     *          <b>orderStatus</b> - int 订单状态，具体参考：{@link com.xlibao.common.constant.order.OrderStatusEnum}中的key
     *          <b>statusValue</b> - String 订单状态值，具体参考：{@link com.xlibao.common.constant.order.OrderStatusEnum}中的value
     *          <b>refundReason</b> - String 退款原因(退款状态时有效)
     *
     *          <b>deliverTitle</b> - String 进度标题；如："配送进度"、"自提进度"
     *          <b>deliverResult</b> - String 配送结果；如："已签收"、"已取货"
     *
     *          <b>actualPrice</b> - long 实收价格，单位：分
     *          <b>distributionFee</b> - long 配送费用，单位：分
     *          <b>discountPrice</b> - long 优惠费用，单位：分
     *
     *          <b>addressTitle</b> - String 地址标题，如： "取货地址：" : "收货地址："
     *          <b>address</b> - String 具体地址，已格式化
     *          <b>targetTitle</b> - String 目标标题，如："取货点：" : "收货人："
     *          <b>targetName</b> - String 目标用户或商店名称
     *          <b>receiptPhone</b> - String 收货人的电话号码，已隐藏信息
     *          <b>totalItemQuantity</b> - int 商品总数量
     *          <b>items</b> - JSONArray 商品信息，每个元素为JSONObject结构
     *                  <b>itemSnapshotId</b> - long 商品快照ID
     *                  <b>itemId</b> - long 商品ID
     *                  <b>itemTemplateId</b> - long 商品模版ID
     *                  <b>name</b> - String 商品名
     *                  <b>image</b> - String 商品图片
     *                  <b>price</b> - long 商品单价，单位：分
     *                  <b>quantity</b> - int 商品数量
     *                  <b>totalPrice</b> - long 商品的总费用
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "orderDetail")
    public JSONObject orderDetail() {
        return orderService.orderDetail();
    }

    /**
     * <pre>
     *     <b>预下单</b>
     *
     *     前端在进行下单前必须先获得预下单序号，建议在进入"确认订单"界面时发起预下单请求；
     *     每个序号有效期为5分钟，5分钟如果未完成下单操作，那么多次请求均为同一序号；
     *     若5分钟内下单后重新请求，则生成新的序号，该过程主要保护用户不会进行重复下单。
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/prepareCreateOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 下单通行证ID，必填参数；未登录时，提示用户登录。
     *          <b>orderType</b> - int 订单类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderTypeEnum}。
     *
     *     <b>返回结果：</b>
     *          <b>sequenceNumber</b> - String 预下单序列号
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "prepareCreateOrder")
    public JSONObject prepareCreateOrder() {
        return orderService.prepareCreateOrder();
    }

    /**
     * <pre>
     *     <b>生成订单</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/generateOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 下单通行证ID，小程序时必填参数；当来自商店购买端时，非必填或为0。
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>deviceType</b> - int 设备类型，非必填参数；默认为{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum#DEVICE_TYPE_ANDROID}，
     *                                              具体参考：{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum}。
     *          <b>sequenceNumber</b> - String 预下单序号，必填参数。
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
     *          <b>itemTemplateSet</b> - String 商品集合，格式为：JSONObject -- {"10000":"2"} ID:数量。
     *
     *     <b>返回结果：</b> 仅返回成功或失败。
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "generateOrder")
    public JSONObject generateOrder() {
        return orderService.generateOrder();
    }

    /**
     * <pre>
     *     <b>支付订单</b>
     *
     *     该方法仅作为供应链的支付入口，具体的支付过程实际由支付中心进行。
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/paymentOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>sequenceNumber</b> - String 订单序列号。
     *          <b>deliverType</b> - int 配送类型，非必填参数；参看：{@link com.xlibao.common.constant.order.DeliverTypeEnum}
     *          <b>paymentType</b> - String 支付类型，必填参数；参考：{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum}
     *          <b>partnerUserId</b> - String 合作方的用户ID，非必填参数；
     *              当为{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#WEIXIN_APPLET}、{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#WEIXIN_JS}时填充openId
     *
     *     <b>返回结果：</b>
     *          当<b>paymentType</b>为{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#BALANCE}时，返回：
     *              <b>partnerId</b> - String 合作商户ID(供应链基于平台的合作商户号)，以xlb908开头。
     *              <b>appId</b> - String 应用ID(平台分配给供应链系统的支付应用ID)，以908开头。
     *              <b>prePaymentId</b> - String 预支付ID，由支付中心生成。
     *              <b>randomParameter</b> - String 供应链本次支付过程生成的一个32位随机数
     *              <b>timeStamp</b> - long 服务器发起支付时的时间
     *              <b>sign</b> - String 签名参数
     *            请求方在获得以上参数后，将上述参数填充到JSONObject数据结构中，如：{"partnerId" : "partnerId", "appId" : "appId", "prePaymentId" : "prePaymentId", "randomParameter" : "randomParameter", "timeStamp" : "timeStamp", "sign" : "sign"}。
     *            然后将上述的JSONObject数据结果当成一个字符串(注意需要进行URLEncode.encode)填充到另一个JSONObject中，key为<b>paymentParameter</b>；连同
     *              <b>passportId</b> - long 通行证ID
     *              <b>paymentPassword</b> - String 支付密码
     *            向支付中心发起余额支付请求，地址为：http://paymentDomainName/payment/balancePayment
     *
     *           支付中心负责返回支付结果的成败
     *
     *          当<b>paymentType</b>为{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#ALIPAY}时，返回：paymentURL(支付宝支付链接)
     *            前端直接将上述参数填充至支付宝提供的SDK中
     *
     *          当<b>paymentType</b>为{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#WEIXIN_APP}时，返回：
     *              <b>appid</b> - String 微信分配的appId。
     *              <b>partnerid</b> - String 微信支付分配的商户号。
     *              <b>prepayid</b> - String 预支付交易会话ID prepayid，微信返回的支付交易会话ID。
     *              <b>package</b> - String 扩展字段 package 暂填写固定值Sign=WXPay
     *              <b>noncestr</b> - String 随机字符串 noncestr 不长于32位
     *              <b>timestamp</b> - int 时间戳
     *              <b>sign</b> - String MD5加密后的签名字符串
     *            前端直接将上述参数填充至微信提供的SDK中
     *
     *          当<b>paymentType</b>为{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#WEIXIN_APPLET}时，返回：
     *              <b>timeStamp</b> - long 时间戳 timestamp 时间戳
     *              <b>nonceStr</b> - String 随机字符串 nonceStr 不长于32位
     *              <b>package</b> - String 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=wx2017033010242291fcfe0db70013231072
     *              <b>signType</b> - String 签名算法，暂支持 MD5
     *              <b>paySign</b> - String 签名内容
     *
     *          当<b>paymentType</b>为{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum#WEIXIN_NATIVE}时，返回：
     *              <b>codeUrl</b> - String 二维码内容，前端将该内容通过第三方控件(亦可前端编码实现)将其显示为二维码
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "paymentOrder")
    public JSONObject paymentOrder() {
        return orderService.paymentOrder();
    }

    /**
     * <pre>
     *     <b>修改收货数据</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/modifyReceivingData.do
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
     *     <b>接单</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/acceptOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "acceptOrder")
    public JSONObject acceptOrder() {
        return orderService.acceptOrder();
    }

    /**
     * <pre>
     *     <b>配送订单</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/deliverOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "deliverOrder")
    public JSONObject deliverOrder() {
        return orderService.deliverOrder();
    }

    /**
     * <pre>
     *     <b>送达订单</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/arriveOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "arriveOrder")
    public JSONObject arriveOrder() {
        return orderService.arriveOrder();
    }

    /**
     * <pre>
     *     <b>退款</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/refundOrder.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数；对应发起退款的用户。
     *          <b>orderSequenceNumber</b> - String 订单序号，必填参数。
     *          <b>title</b> - String 退款原因标题，必填参数。
     *          <b>content</b> - String 退款说明，非必填参数。
     *
     *     <b>返回：</b>仅返回成功或失败的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "refundOrder")
    public JSONObject refundOrder() {
        return orderService.refundOrder();
    }

    /**
     * <pre>
     *     <b>寻找订单的容器数据</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/findContainerData.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>orderSequenceNumber</b> - String 订单序号
     *
     *     <b>返回：</b>
     *          <b>mark</b> - String 文字提示描述内容
     *          <b>containerData</b> - JSONArray 货柜数据，每个元素为String；即：货柜编号
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "findContainerData")
    public JSONObject findContainerData() {
        return orderService.findContainerData();
    }

    /**
     * <pre>
     *     <b>询问当前订单状态</b>
     *
     *     <b>访问地址：</b>http://domainName/market/order/openapi/askOrderStatus.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "askOrderStatus")
    public JSONObject askOrderStatus() {
        return orderService.askOrderStatus();
    }
}