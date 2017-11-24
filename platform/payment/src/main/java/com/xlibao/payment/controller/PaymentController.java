package com.xlibao.payment.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.constant.payment.*;
import com.xlibao.common.constant.sms.SmsCodeTypeEnum;
import com.xlibao.payment.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Controller
@RequestMapping(value = "/payment")
public class PaymentController extends BasicWebService {

    @Autowired
    private PaymentService paymentService;

    /**
     * <pre>
     *     <b>统一下单接口(支付专用)</b>
     * </pre>
     *
     * @deprecated 仅提供内部服务使用
     */
    @ResponseBody
    @RequestMapping(value = "unifiedOrder")
    public JSONObject unifiedOrder() {
        return paymentService.unifiedOrder();
    }

    /**
     * <pre>
     *     <b>余额支付</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/balancePayment
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>passportId</b> - long 通行证ID，必填参数；本次支付的用户。
     *     <b>paymentParameter</b> - String 支付参数，必填参数；由具体业务服务生成的交易参数。
     *     <b>paymentPassword</b> - String 支付密码，必填参数。
     *
     *     <b>返回：</b>支付结果描述
     *          <b>注意：</b>未设置交易密码时，服务器返回错误码：100
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "balancePayment")
    public JSONObject balancePayment() {
        return paymentService.balancePayment();
    }

    /**
     * <pre>
     *     <b>余额偏移</b>
     * </pre>
     *
     * @deprecated 仅提供内部服务使用
     */
    @ResponseBody
    @RequestMapping(value = "offsetBalance")
    public JSONObject offsetBalance() {
        return paymentService.offsetBalance();
    }

    /**
     * <pre>
     *     <b>检查用户是否已设置了支付密码</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/isSetPaymentPassword
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *
     *     <b>返回：</b>
     *          <b>isSet</b> - byte 是否已设置；具体参考：{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_TRUE} 已设置
     *                                             {@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_FALSE} 未设置
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "isSetPaymentPassword")
    public JSONObject isSetPaymentPassword() {
        return paymentService.isSetPaymentPassword();
    }

    /**
     * <pre>
     *     <b>首次设置支付密码</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/firstSetPaymentPassword
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>password</b> - String 本次设置的密码，必填参数。
     *          <b>confirmPassword</b> - String 确认密码，必填参数。
     *
     *     <b>返回：</b>仅提示成功或失败。
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "firstSetPaymentPassword")
    public JSONObject firstSetPaymentPassword() {
        return paymentService.firstSetPaymentPassword();
    }

    /**
     * <pre>
     *     <b>重设支付密码</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/setPaymentPassword
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>phone</b> - String 请求验证码的手机号，必填参数。
     *          <b>smsCode</b> - String 收到的验证码，必填参数。
     *          <b>smsType</b> - int 验证码类型，必填参数。参考：{@link SmsCodeTypeEnum#PAYMENT_WORKS}
     *          <b>paymentPassword</b> - String 支付密码，必填参数。
     *
     *     <b>返回结果：</b>仅返回成功或失败的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "setPaymentPassword")
    public JSONObject setPaymentPassword() {
        return paymentService.setPaymentPassword();
    }

    /**
     * <pre>
     *     <b>获取用户所有货币信息</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/passportCurrency
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *
     *     <b>返回结果：</b>
     *          <b>datas</b> - JSONArray 每个元素都为JSONObject，数据结构：
     *              <b>id</b> - long 货币的ID
     *              <b>name</b> - long 货币名字
     *              <b>type</b> - long 类型值，参考：{@link com.xlibao.common.constant.payment.CurrencyTypeEnum}
     *              <b>channelId</b> - long 所属渠道(用户进件时决定，不能修改)
     *              <b>currentAmount</b> - long 可用余额
     *              <b>freezeAmount</b> - long 冻结余额
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "passportCurrency")
    public JSONObject passportCurrency() {
        return paymentService.passportCurrency();
    }

    /**
     * <pre>
     *     <b>充值活动模版</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/rechargeActivityTemplates
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>请求参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>currencyType</b> - int 充值的货币类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.payment.CurrencyTypeEnum}
     *
     *     <b>返回：</b>
     *          <b>rechargeArray</b> - JSONArray 充值活动模版数据，每个元素为JSONObject结构
     *              <b>minimumRechargeAmount</b> - long 充值起步价(临界值)，单位：分
     *              <b>presentAmount</b> - long 可获得的赠送额度，单位：分
     *              <b>totalAmount</b> - long 充值后总共可获得的额度，单位：分
     *          <b>vipBalance</b> - long 当前会员余额的额度，单位：分
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "rechargeActivityTemplates")
    public JSONObject rechargeActivityTemplates() {
        return paymentService.rechargeActivityTemplates();
    }

    /**
     * <pre>
     *     <b>充值余额</b>
     *
     *     <b>请求地址：</b>http://domainName/payment/rechargeBalance
     *     <b>请求方式：</b>GET/POST 推荐POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>paymentType</b> - String 支付类型，必填参数；参考：{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum}。
     *          <b>rechargeCurrencyType</b> - int 充值的货币类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.payment.CurrencyTypeEnum}
     *          <b>rechargeAmount</b> - long 充值额度，必填参数；单位：分。
     *
     *     <b>返回结果：</b>
     *
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
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "rechargeBalance")
    public JSONObject rechargeBalance() {
        return paymentService.rechargeBalance();
    }

    /**
     * <pre>
     *     <b>充值流水</b>
     *
     *     访问地址：http://domainName/payment/rechargeFlows
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long  通行证ID，必填参数。
     *          <b>currencyType</b> - int 查看的货币类型，非必填参数；当为0时，表示查看所有类型；其他参考：{@link CurrencyTypeEnum}
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 充值流水记录集合，每个元素为JSONObject
     *              <b>transSequenceNumber</b> - String 交易序列号
     *              <b>transType</b> - String 交易类型，参考：{@link PaymentTypeEnum}
     *              <b>title</b> - String 标题
     *              <b>time</b> - String 充值时间，已格式化
     *              <b>transAmount</b> - String 充值金额，已格式化
     *              <b>presentAmount</b> - String 获得赠送金额，已格式化
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "rechargeFlows")
    public JSONObject rechargeFlows() {
        return paymentService.rechargeFlows();
    }

    /**
     * <pre>
     *     <b>充值详情</b>
     *
     *     访问地址：http://domainName/payment/rechargeDetail
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long  通行证ID，必填参数。
     *          <b>transSequenceNumber</b> - String 交易序列号，必填参数。
     *
     *     <b>返回：</b>
     *          <b>transAmount</b> - long 交易额度，单位：分
     *          <b>transSequenceNumber</b> - String 交易序列号
     *          <b>title</b> - String 标题
     *          <b>transType</b> - String 交易类型
     *          <b>transTime</b> - String 交易时间，已格式化
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "rechargeDetail")
    public JSONObject rechargeDetail() {
        return paymentService.rechargeDetail();
    }

    /**
     * <pre>
     *     <b>获取收支明细</b>
     *
     *     访问地址：http://domainName/payment/showAmountBalanceOfPayments
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     访问参数：
     *
     *     <b>passportId</b> - long 通行证ID，必填参数。
     *     <b>currencyType</b> - int 货币类型，非必填参数；默认为余额，具体参考：{@linkplain CurrencyTypeEnum}
     *     <b>enterType</b> - int 入口类型，非必填参数；默认为全部，具体参考：{@link BalanceOfPaymentsEnterTypeEnum}
     *     <b>date</b> - String 指定日期的流水，非必填参数；如果查看的是当天或当月时，该参数请不要提供，格式为：yyyy-MM-dd或yyyy-MM
     *     <b>pageIndex</b> - int 页码，非必填参数；默认为1，具体参考：{@linkplain GlobalConstantConfig#DEFAULT_PAGE_INDEX}
     *     <b>pageSize</b> - int 记录数量，非必填参数；默认为10条，具体参考：{@linkplain GlobalConstantConfig#DEFAULT_PAGE_SIZE}
     *
     *     返回结果：
     *     <b>currencyArray</b> - JSONArray 请求页面的记录数据，每个元素均为JSONObject结构
     *          <b>id</b> - long 流水ID
     *          <b>title</b> - String 标题
     *          <b>time</b> - String 发生时间
     *          <b>amount</b> - long 偏移额度(正数 - 收入，负数 - 支出)
     *          <b>transType</b> - String 自定义类型(如：微信支付，支付宝支付等) 参考：{@link PaymentTypeEnum}
     *          <b>transSequence</b> - String 交易关联的序列号
     *     <b>statisticsArray</b> - JSONArray 汇总数据，按日期划分；每个元素均为JSONObject结构
     *          <b>title</b> - String 日期标题
     *          <b>time</b> - String 时间，已经格式化，格式为：yyyy-MM-dd
     *          <b>count</b> - int 笔数
     *          <b>totalAmount</b> - long 总额度，单位：分
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showAmountBalanceOfPayments")
    public JSONObject showAmountBalanceOfPayments() {
        return paymentService.showAmountBalanceOfPayments();
    }

    /**
     * <pre>
     *     <b>余额流水</b>
     *
     *     访问地址：http://domainName/payment/balanceFlows
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     访问参数：
     *
     *     <b>passportId</b> - long 通行证ID，必填参数。
     *     <b>balanceType</b> - int 查看余额类型，必填参数；具体参考：{@link BalanceTypeEnum}
     *     <b>pageIndex</b> - int 页码，非必填参数；默认为1，具体参考：{@linkplain GlobalConstantConfig#DEFAULT_PAGE_INDEX}
     *     <b>pageSize</b> - int 记录数量，非必填参数；默认为10条，具体参考：{@linkplain GlobalConstantConfig#DEFAULT_PAGE_SIZE}
     *
     *     返回：
     *          <b>datas</b> - JSONArray 请求页面的记录数据，每个元素均为JSONObject结构
     *              <b>id</b> - long 流水ID
     *              <b>title</b> - String 标题
     *              <b>time</b> - String 发生时间
     *              <b>amount</b> - long 偏移额度(正数 - 收入，负数 - 支出)
     *              <b>transType</b> - String 自定义类型(如：微信支付，支付宝支付等)
     *              <b>transSequence</b> - String 交易关联的序列号
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "balanceFlows")
    public JSONObject balanceFlows() {
        return paymentService.balanceFlows();
    }

    /**
     * <pre>
     *     <b>提现流水</b>
     *
     *     访问地址：http://domainName/payment/drawCashFlows
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>访问参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>pageIndex</b> - int 页码，非必填参数；默认为1，具体参考：{@linkplain com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_INDEX}。
     *          <b>pageSize</b> - int 记录数量，非必填参数；默认为10条，具体参考：{@linkplain com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_SIZE}。
     *
     *     <b>返回结果：</b>
     *          <b>datas</b> - JSONArray 提现记录数据集合，单个元素为JSONObject结构
     *              <b>id</b> - long 交易记录ID
     *              <b>transStatus</b> - int 交易状态，0(默认值) -- 审核中；具体参考：{@link com.xlibao.common.constant.payment.TransStatusEnum}，其中{@link com.xlibao.common.constant.payment.TransStatusEnum#TRADE_SUCCESS_SERVER}表示交易完成
     *              <b>bankName</b> - String 银行名字
     *              <b>bankSimpleName</b> - String 银行简称
     *              <b>transAmount</b> - long 交易额度，单位：分
     *              <b>transTime</b> - String 交易时间，格式：yyyy-MM-dd HH:mm:ss
     *              <b>bankAccount</b> - String 交易帐号(后4位)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "drawCashFlows")
    public JSONObject drawCashFlows() {
        return paymentService.drawCashFlows();
    }

    /**
     * <pre>
     *     <b>查看支付流水详情</b>
     *
     *     访问地址：http://domainName/payment/paymentDetail
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>访问参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>currencyOffsetId</b> - long 支付流水ID，必填参数。
     *
     *     <b>返回结果：</b>
     *          <b>transAmount</b> - long 交易额度，单位：分
     *          <b>transSequenceNumber</b> - String 交易序列号
     *          <b>title</b> - String 标题
     *          <b>transType</b> - String 交易类型
     *          <b>transTime</b> - String 交易时间
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "paymentDetail")
    public JSONObject paymentDetail() {
        return paymentService.paymentDetail();
    }

    /**
     * <pre>
     *     <b>收入统计</b>
     *
     *     访问地址：http://domainName/payment/incomeStatistics
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>访问参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *
     *     <b>返回</b>
     *          <b>todayTotalAmount</b> - long 今日收入，单位：分
     *          <b>monthTotalAmount</b> - long 本月收入，单位：分
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "incomeStatistics")
    public JSONObject incomeStatistics() {
        return paymentService.incomeStatistics();
    }

    /**
     * <pre>
     *     <b>显示银行模版</b>
     *
     *     访问地址：http://domainName/payment/showBankTemplates
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>无需访问参数
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 模版集合，每个元素为JSONObject
     *              <b>id</b> - long 模版ID
     *              <b>name</b> - String 银行名
     *              <b>simpleCode</b> - String 简称，如：ICBC
     *              <b>bankCode</b> - String 银行编码
     *              <b>logo</b> - String 图标
     *              <b>createTime</b> - String 建立时间，客户端忽略该参数
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showBankTemplates")
    public JSONObject showBankTemplates() {
        return paymentService.showBankTemplates();
    }

    /**
     * <pre>
     *     <b>绑定银行账户</b>
     *
     *     访问地址：http://domainName/payment/bindBankAccount
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>bankTemplateId</b> - long 银行模版ID，必填参数。
     *
     *          <b>bankAccount</b> - String 银行帐号，必填参数。
     *          <b>accountName</b> - String 归属者名字，必填参数。
     *          <b>reservePhone</b> - String 银行预留手机号，必填参数；必须符合规范。
     *          <b>branchName</b> - String 支行名，非必填参数。
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "bindBankAccount")
    public JSONObject bindBankAccount() {
        return paymentService.bindBankAccount();
    }

    /**
     * <pre>
     *     <b>展示提现方式</b>
     *
     *     访问地址：http://domainName/payment/showDrawCashMode
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>roleType</b> - int 角色类型，必填参数；具体参考：{@link com.xlibao.common.constant.passport.ClientTypeEnum}
     *
     *     <b>返回：</b>
     *          <b>mode</b> - int 提现方式，具体参考：{@link com.xlibao.common.constant.payment.DrawCashModeEnum}
     *          <b>name</b> - String 展示的内容(用户可视)
     *          <b>lowCost</b> - long 最低收费，单位：分
     *          <b>highCost</b> - long 最高收费，单位：分
     *          <b>rate</b> - int 费率(万份比)
     *          <b>showImage</b> - String 展示的图片
     *          <b>defaultOption</b> - byte 是否默认选项，参考：{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_FALSE} 非默认选项
     *                                                     {@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_TRUE} 默认选项
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showDrawCashMode")
    public JSONObject showDrawCashMode() {
        return paymentService.showDrawCashMode();
    }

    /**
     * <pre>
     *     <b>提现前的规则检测</b>
     *
     *     访问地址：http://domainName/payment/beforeTakeCash
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>mode</b> - int 提现方式，具体参考：{@link com.xlibao.common.constant.payment.DrawCashModeEnum}
     *          <b>roleType</b> - int 角色类型，必填参数；具体参考：{@link com.xlibao.common.constant.passport.ClientTypeEnum}
     *
     *     <b>返回：</b>
     *          <b>rateValue</b> - String 费率提示内容
     *          <b>maxTakeCash</b> - String 最多提现提示内容
     *          <b>maxTakeCashValue</b> - long 最多提现额度，单位：分
     *          <b>remainTakeCashCount</b> - int 剩余提现次数
     *          <b>tip</b> - String 确认时的提示内容
     *          <b>tipMode</b> - int 确认时的提示方式；1：仅存在“确认”按钮  2：存在“确认”、“取消”按钮
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "beforeTakeCash")
    public JSONObject beforeTakeCash() {
        return paymentService.beforeTakeCash();
    }

    /**
     * <pre>
     *     <b>提现</b>
     *
     *     访问地址：http://domainName/payment/drawCash
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>roleType</b> - int 角色类型，必填参数；具体参考：{@link com.xlibao.common.constant.passport.ClientTypeEnum}
     *          <b>paymentPassword</b> - String 支付密码，必填参数。
     *          <b>smsCode</b> - String 短信验证码，必填参数。
     *          <b>bankId</b> - long 目标收款银行卡ID，必填参数。
     *          <b>amount</b> - long 提取额度，单位：分，必填参数。
     *          <b>mode</b> - byte 提现方式，非必填参数；具体参考：
     *
     *     <b>返回：</b>成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "drawCash")
    public JSONObject drawCash() {
        return paymentService.drawCash();
    }

    /**
     * <pre>
     *     <b>获取个人的所有银行卡</b>
     *
     *     访问地址：http://domainName/payment/showBanks
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 银行卡信息集合，每个元素为JSONObject
     *              <b>bankId</b> - long 银行卡ID
     *              <b>bankTemplateId</b> - long 银行模版ID
     *              <b>bankName</b> - String 银行名字
     *              <b>accountType</b> - int 账户类型，参考：{@link BankTypeEnum}
     *              <b>bankAccount</b> - String 银行卡号(已隐藏部分字符)
     *              <b>accountName</b> - String 收款人姓名
     *              <b>logo</b> - String 图标
     *              <b>status</b> - int 状态，参考：{@link BankStatusEnum}
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showBanks")
    public JSONObject showBanks() {
        return paymentService.showBanks();
    }

    /**
     * <pre>
     *     <b>设置默认银行卡</b>
     *
     *     访问地址：http://domainName/payment/setDefaultBank
     *     访问方式：POST/GET(推荐使用POST)
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>bankId</b> - long 银行卡ID，必填参数。
     *
     *     <b>返回：</b>成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "setDefaultBank")
    public JSONObject setDefaultBank() {
        return paymentService.setDefaultBank();
    }

    /**
     * <pre>
     *     <b>退款</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "refund")
    public JSONObject refund() {
        return paymentService.refund();
    }
}