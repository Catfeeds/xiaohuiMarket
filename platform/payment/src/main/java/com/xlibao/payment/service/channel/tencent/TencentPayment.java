package com.xlibao.payment.service.channel.tencent;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.DefineRandom;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.file.XMLSupport;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.payment.config.ConfigFactory;
import com.xlibao.payment.data.mapper.PaymentDataAccessManager;
import com.xlibao.payment.data.model.PaymentTransactionLogger;
import com.xlibao.payment.service.currency.CurrencyEventListenerManager;
import com.xlibao.payment.service.trans.TransactionEventListenerManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Component
public class TencentPayment extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(TencentPayment.class);

    // 微信统一下单接口
    private static final String TENCENT_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 微信退款接口
    private static final String TENCENT_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    @Autowired
    private PaymentDataAccessManager paymentDataAccessManager;
    @Autowired
    private TransactionEventListenerManager transactionEventListenerManager;
    @Autowired
    private CurrencyEventListenerManager currencyEventListenerManager;

    public JSONObject weixinAppPaymentParameters(String tradeNumber, long totalAmount, String function, String desc, String attach, String remoteIP) {
        return weixinPaymentParameters(PaymentTypeEnum.WEIXIN_APP, "", tradeNumber, totalAmount, function, desc, attach, remoteIP);
    }

    public JSONObject weixinNativePaymentParameters(String tradeNumber, long totalAmount, String function, String desc, String attach, String remoteIP) {
        return weixinPaymentParameters(PaymentTypeEnum.WEIXIN_NATIVE, "", tradeNumber, totalAmount, function, desc, attach, remoteIP);
    }

    public JSONObject weixinJSPaymentParameters(String openId, String tradeNumber, long totalAmount, String function, String desc, String attach, String remoteIP) {
        return weixinPaymentParameters(PaymentTypeEnum.WEIXIN_JS, openId, tradeNumber, totalAmount, function, desc, attach, remoteIP);
    }

    public JSONObject weixinAppletPaymentParameters(String openId, String tradeNumber, long totalAmount, String function, String desc, String attach, String remoteIP) {
        return weixinPaymentParameters(PaymentTypeEnum.WEIXIN_APPLET, openId, tradeNumber, totalAmount, function, desc, attach, remoteIP);
    }

    public JSONObject refund(PaymentTransactionLogger transactionLogger) {
        Map<String, String> parameters = new HashMap<>();
        // 小程序ID appid 微信分配的小程序ID
        parameters.put("appid", ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_ID);
        // 商户号 mch_id 微信支付分配的商户号
        parameters.put("mch_id", ConfigFactory.getTencentWeixinPaymentConfig().WX_PARTNER_ID);
        // 设备号 device_info 终端设备号
        // parameters.put("device_info", deviceInfo);
        // 随机字符串 nonce_str 随机字符串，不长于32位。推荐随机数生成算法
        parameters.put("nonce_str", DefineRandom.randomChar(32).toUpperCase());
        // 签名类型	sign_type HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
        parameters.put("sign_type", "MD5");
        // 微信订单号 transaction_id 1217752501201407033233368018 微信生成的订单号，在支付通知中有返回
        parameters.put("transaction_id", transactionLogger.getChannelTradeNumber());
        // 商户订单号 out_trade_no 1217752501201407033233368018	商户侧传给微信的订单号 与 微信订单号 二选一即可
        // 商户退款单号 out_refund_no 1217752501201407033233368018 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
        parameters.put("out_refund_no", transactionLogger.getTransSequenceNumber());
        // 订单金额	total_fee 100 订单总金额，单位为分，只能为整数，详见支付金额
        parameters.put("total_fee", String.valueOf(transactionLogger.getTransTotalAmount()));
        // 退款金额	refund_fee 100 退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
        parameters.put("refund_fee", String.valueOf(transactionLogger.getTransTotalAmount()));
        // 货币种类	refund_fee_type CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        parameters.put("refund_fee_type", "CNY");
        // 操作员 op_user_id 1900000109	操作员帐号, 默认为商户号
        parameters.put("op_user_id", ConfigFactory.getTencentWeixinPaymentConfig().WX_PARTNER_ID);
        // 退款资金来源	refund_account	否	String(30)	REFUND_SOURCE_RECHARGE_FUNDS
        //          仅针对老资金流商户使用 REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
        //                                 REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
        parameters.put("refund_account", "REFUND_SOURCE_UNSETTLED_FUNDS");
        // 签名	sign C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名生成算法
        CommonUtils.fillSignature(parameters, ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_KEY);

        try {
            String result = HttpRequest.pkcs12Post(ConfigFactory.getTencentWeixinPaymentConfig().WX_CERTIFICATE_PATH, ConfigFactory.getTencentWeixinPaymentConfig().WX_PARTNER_ID, TENCENT_REFUND_URL, XMLSupport.mapToXML("xml", parameters));
            Map<String, String> responseResult = XMLSupport.xmlToMap(result);
            logger.info("return " + responseResult);

            String returnCode = responseResult.get("return_code");
            if (!"SUCCESS".equals(returnCode)) {
                logger.error("退款结果(return code) : " + result);
                return fail("退款失败，失败原因：" + responseResult.get("return_msg"));
            }
            String resultCode = responseResult.get("result_code");
            if (!"SUCCESS".equals(resultCode)) {
                logger.error("退款结果(result code) : " + result);
                return fail("退款失败，失败原因：" + responseResult.get("err_code_des") + "；错误码：" + responseResult.get("err_code"));
            }
            // 执行退款后的流程
            afterRefund(transactionLogger, parameters, result);
            return success("退款成功，本次退款将按微信退款流程执行");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fail();
    }

    private void findCertificate(String path) throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream inputStream = new FileInputStream(new File(path))) {
            keyStore.load(inputStream, "111111".toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "111111".toCharArray()).build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    private void afterRefund(PaymentTransactionLogger transactionLogger, Map<String, String> parameters, String channelParameters) {
        // 主要是记录交易状态
        /**
         * TODO 签名验证
         * String sign = parameters.remove("sign");
         * boolean result = CommonUtils.matchSignature(parameters, sign, ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_KEY);
         */
        transactionLogger.setRefundStatus(GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());
        transactionLogger.setRefundTime(new Date());
        transactionLogger.setChannelRefundParameters(channelParameters);

        paymentDataAccessManager.updateRefundParameter(transactionLogger);
        // 小程序ID	appid wx8888888888888888 微信分配的小程序ID
        // 商户号 mch_id 1900000109	微信支付分配的商户号
        // 设备号 device_info 013467007045764 微信支付分配的终端设备号，与下单一致
        // 随机字符串 nonce_str 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 随机字符串，不长于32位
        // 签名 sign 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 签名，详见签名算法
        // 微信订单号 transaction_id 4007752501201407033233368018 微信订单号
        // 商户订单号 out_trade_no 33368018 商户系统内部的订单号
        // 商户退款单号 out_refund_no 121775250 商户退款单号
        // 微信退款单号 refund_id 2007752501201407033233368018 微信退款单号
        // 退款渠道	refund_channel
        //          ORIGINAL—原路退款
        //          BALANCE—退回到余额
        // 申请退款金额 refund_fee 100 退款总金额,单位为分,可以做部分退款
        // 退款金额	settlement_refund_fee 100 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额 <= 申请退款金额
        // 订单金额 total_fee 100 订单总金额，单位为分，只能为整数，详见支付金额
        // 应结订单金额 settlement_total_fee 100 去掉非充值代金券金额后的订单总金额，应结订单金额 = 订单金额 - 非充值代金券金额，应结订单金额 <= 订单金额。
        // 订单金额货币种类 fee_type CNY 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        // 现金支付金额 cash_fee 100 现金支付金额，单位为分，只能为整数，详见支付金额
        // 现金退款金额 cash_refund_fee 100 现金退款金额，单位为分，只能为整数，详见支付金额
        // 代金券类型 coupon_type_$n
        //            CASH -- 充值代金券
        //            NO_CASH -- 非充值代金券
        //          订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
        // 代金券退款金额 coupon_refund_fee_$n 100 代金券退款金额 <= 退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
        //          退款代金券使用数量 coupon_refund_count_$n 1 退款代金券使用数量 ,$n为下标,从0开始编号
        // 退款代金券批次ID coupon_refund_batch_id_$n_$m 100 退款代金券批次ID ,$n为下标，$m为下标，从0开始编号
        // 退款代金券ID coupon_refund_id_$n_$m 10000 退款代金券ID, $n为下标，$m为下标，从0开始编号
        // 单个退款代金券支付金额 coupon_refund_fee_$n_$m 100 单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号
    }

    private JSONObject weixinPaymentParameters(PaymentTypeEnum paymentType, String openId, String tradeNumber, long totalAmount, String function, String desc, String attach, String remoteIP) {
        String[] payParameter = generationPrePaymentId(paymentType, openId, tradeNumber, totalAmount, function, desc, attach, remoteIP);
        if (payParameter == null) {
            throw new XlibaoRuntimeException("无法生成微信预支付ID");
        }
        JSONObject parameters = new JSONObject();
        Map<String, String> signParameters = new HashMap<>();

        if (paymentType == PaymentTypeEnum.WEIXIN_NATIVE) {
            parameters.put("codeUrl", payParameter[0]);
            return parameters;
        }

        if (paymentType == PaymentTypeEnum.WEIXIN_APPLET) {
            signParameters.put("appId", ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_ID);
            // 时间戳 timestamp 时间戳
            parameters.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            signParameters.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));

            // 随机字符串 nonceStr 不长于32位
            parameters.put("nonceStr", payParameter[1]);
            signParameters.put("nonceStr", payParameter[1]);

            // 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=wx2017033010242291fcfe0db70013231072
            parameters.put("package", "prepay_id=" + payParameter[0]);
            signParameters.put("package", "prepay_id=" + payParameter[0]);

            // 签名算法，暂支持 MD5
            parameters.put("signType", "MD5");
            signParameters.put("signType", "MD5");

            String paySign = CommonUtils.signature(signParameters, ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_KEY);
            parameters.put("paySign", paySign);
            return parameters;
        }
        // 微信分配的小程序ID
        parameters.put("appid", ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_ID);
        signParameters.put("appid", ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_ID);

        // 时间戳 timestamp 时间戳
        parameters.put("timestamp", System.currentTimeMillis() / 1000);
        signParameters.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        // 随机字符串 noncestr 不长于32位
        parameters.put("noncestr", payParameter[1]);
        signParameters.put("noncestr", payParameter[1]);

        // 商户号 partnerid 微信支付分配的商户号
        parameters.put("partnerid", ConfigFactory.getTencentWeixinPaymentConfig().WX_PARTNER_ID);
        signParameters.put("partnerid", ConfigFactory.getTencentWeixinPaymentConfig().WX_PARTNER_ID);

        // 预支付交易会话ID prepayid 微信返回的支付交易会话ID
        parameters.put("prepayid", payParameter[0]);
        signParameters.put("prepayid", payParameter[0]);

        // 扩展字段 package 暂填写固定值Sign=WXPay
        parameters.put("package", "Sign=WXPay");
        signParameters.put("package", "Sign=WXPay");

        // 通用方法 填充签名字段
        CommonUtils.fillSignature(signParameters, ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_KEY);
        parameters.put("sign", signParameters.get("sign"));
        return parameters;
    }

    private String[] generationPrePaymentId(PaymentTypeEnum paymentType, String openId, String tradeNumber, long totalAmount, String body, String detail, String attach, String remoteIp) {
        String nonceValue = DefineRandom.randomChar(32).toUpperCase();
        Map<String, String> parameters = new HashMap<>();
        logger.info("App id is " + ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_ID);
        // 公众账号ID appid 微信分配的公众账号ID（企业号corpid即为此appId） 小程序ID 微信分配的小程序ID
        parameters.put("appid", ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_ID);
        // 商户号 mch_id 微信支付分配的商户号
        parameters.put("mch_id", ConfigFactory.getTencentWeixinPaymentConfig().WX_PARTNER_ID);
        // 随机字符串 nonce_str 不长于32位。推荐随机数生成算法
        parameters.put("nonce_str", nonceValue);
        // 商品描述 body 商品或支付单简要描述 如：腾讯充值中心-QQ会员充值 该字段须严格按照规范传递，具体请见参数规定
        parameters.put("body", body);
        // 商品详情 detail 商品名称明细列表
        parameters.put("detail", detail);
        // 附加数据 attach 在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        parameters.put("attach", attach);
        // 商户订单号 out_trade_no 32个字符内、可包含字母, 其他说明见商户订单号
        parameters.put("out_trade_no", tradeNumber + "");
        // 订单总金额 total_fee 单位为分，详见支付金额
        parameters.put("total_fee", totalAmount + "");
        // 终端IP spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        parameters.put("spbill_create_ip", remoteIp);
        // 交易起始时间 time_start 格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
        parameters.put("time_start", CommonUtils.defineDateFormat(System.currentTimeMillis(), "yyyyMMddHHmmss"));
        // 交易结束时间 time_expire 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。注意：最短失效时间间隔必须大于5分钟
        parameters.put("time_expire", CommonUtils.defineDateFormat(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30), "yyyyMMddHHmmss"));
        // 通知地址 notify_url 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        parameters.put("notify_url", ConfigFactory.getDomainNameConfig().paymentRemoteURL + "payment/channelCallback/notifyWeixinNativePayment");
        // 交易类型 trade_type 取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
        parameters.put("trade_type", "APP");
        if (paymentType == PaymentTypeEnum.WEIXIN_NATIVE) {
            parameters.put("trade_type", "NATIVE");
            parameters.put("product_id", tradeNumber);
        }
        // JSAPI时需要传openId
        if (paymentType == PaymentTypeEnum.WEIXIN_JS || paymentType == PaymentTypeEnum.WEIXIN_APPLET) {
            parameters.put("trade_type", "JSAPI");
            // 用户标识	openid	trade_type=JSAPI时此参数必传，用户在商户appid下的唯一标识。
            parameters.put("openid", openId);
        }
        // 指定支付方式	limit_pay	否	String(32)	no_credit	no_credit--指定不能使用信用卡支付
        // parameters.put("limit_pay", "no_credit");
        String parameterSort = CommonUtils.parameterSort(parameters, new ArrayList<>());
        String sign = parameterSort + "key=" + ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_KEY;
        logger.info("pre before sign " + sign);
        sign = CommonUtils.md5(sign).toUpperCase();
        logger.info("pre after sign " + sign);
        // 签名 sign 签名，详见签名生成算法
        parameters.put("sign", sign);
        // 设备号 device_info 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
        try {
            String result = HttpRequest.sendPost(TENCENT_ORDER_URL, XMLSupport.mapToXML("xml", parameters));
            Map<String, String> responseResult = XMLSupport.xmlToMap(result);
            String returnCode = responseResult.get("return_code");
            logger.info("return " + responseResult);
            if (!"SUCCESS".equals(returnCode)) {
                logger.error(result);
                return null;
            }
            String resultCode = responseResult.get("result_code");
            if (!"SUCCESS".equals(resultCode)) {
                logger.error(result);
                return null;
            }
            if (paymentType == PaymentTypeEnum.WEIXIN_NATIVE) {
                return new String[]{responseResult.get("code_url")};
            }
            // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
            return new String[]{responseResult.get("prepay_id"), nonceValue};
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void notifyWeixinNativePayment() {
        try {
            SAXReader reader = new SAXReader();

            Document document = reader.read(getHttpServletRequest().getInputStream());
            logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 微信支付回调参数 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓" +
                    "\r\n" + document.asXML() + "\r\n" +
                    "↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 微信支付回调参数 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑");

            List<Element> elements = document.getRootElement().elements();
            Map<String, String> parameters = new HashMap<>();
            for (Element element : elements) {
                parameters.put(element.getName(), element.getTextTrim());
            }
            // 返回状态码 return_code String(16) 包含：SUCCESS SUCCESS/FAIL
            // 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
            String returnCode = parameters.get("return_code");
            // 返回信息 return_msg 非必填字段 String(128)
            // 如非空，为错误原因 如：签名失败、参数格式校验错误等
            String returnMsg = parameters.get("return_msg");

            if (returnMsg != null && returnMsg.length() > 0) {
                // 错误描述 这里后期要做记录 但由于无法获取到我方的订单号 无法记录到对应的交易记录上
                logger.info("充值失败：" + returnMsg);
            }
            if (!"SUCCESS".equals(returnCode)) {
                return;
            }
            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 执行签名验证的流程 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            // 签名 sign
            String sign = parameters.get("sign");
            // 验证签名
            if (!verifyWeixinSign(parameters, sign)) {
                responseWeixinResult("FAIL", "签名失败");
                return;
            }
            // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 完成签名验证的流程 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

            // 总金额 total_fee 订单总金额，单位为分
            int totalFee = Integer.parseInt(parameters.get("total_fee"));
            // 商户订单号 out_trade_no 商户系统的订单号，与请求一致。
            String outTradeNo = parameters.get("out_trade_no");

            // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 以下为业务逻辑程序代码 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            // 通过我方的参数内容获取交易记录
            PaymentTransactionLogger transactionLogger = paymentDataAccessManager.getTransactionLogger(outTradeNo);
            if (transactionLogger == null) {
                responseWeixinResult("FAIL", "非法请求，订单号不存在");
                return;
            }
            // 判断该笔订单是否已经做过处理
            if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_FINISHED.getKey()) == TransStatusEnum.TRADE_FINISHED.getKey()) { // 已处于交易完成状态
                responseWeixinResult("SUCCESS", "FINISHED");
                return;
            }
            // 如果有做过处理，不执行业务程序
            if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) == TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) { // 已处于支付成功状态(仅针对服务器状态)
                responseWeixinResult("SUCCESS", "S_FINISHED");
                return;
            }
            /**
             * <pre>
             * 如果没有做过处理，根据订单号（out_trade_no）在订单系统中查到该笔订单的详细，并执行业务程序
             *
             * <b>注意：</b>
             * 付款完成后，该交易状态通知相关的系统
             * 最终支付金额不能小于发起时的额度(不包括充值)
             * </pre>
             */
            if (transactionLogger.getTransType() != TransTypeEnum.RECHARGE.getKey() && transactionLogger.getTransTotalAmount() > totalFee) {
                responseWeixinResult("FAIL", "支付金额不符合要求");
                return;
            }
            // 填充微信回调的数据
            fillWeixinNotifyTransData(parameters, transactionLogger);
            // 通知监听系统
            transactionEventListenerManager.notifyFinishPayment(transactionLogger, TransStatusEnum.TRADE_SUCCESS_SERVER, transactionLogger.getTransType() != TransTypeEnum.RECHARGE.getKey());

            // 通知额度偏移，这里主要是做一个流水记录
            currencyEventListenerManager.notifyOffsetCurrencyAmount(transactionLogger.getPassportId(), transactionLogger.getChannelId(), CurrencyTypeEnum.BALANCE.getKey(), 0, -Math.abs(transactionLogger.getTransTotalAmount()),
                    0, transactionLogger.getPaymentType(), transactionLogger.getTransTitle(), transactionLogger.getTransType(), transactionLogger.getTransSequenceNumber());
            responseWeixinResult("SUCCESS", "OK");
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        responseWeixinResult("FAIL", "ERROR");
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 以上为业务逻辑程序代码 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    }

    public void notifyWeixinRefund() {
    }

    private boolean verifyWeixinSign(Map<String, String> params, String sign) {
        params.remove("sign");
        String parameterSort = CommonUtils.parameterSort(params, new ArrayList<>()) + "key=" + ConfigFactory.getTencentWeixinPaymentConfig().WX_APP_KEY;

        String preSignValue = CommonUtils.md5(parameterSort).toUpperCase();

        return sign.equals(preSignValue);
    }

    private void fillWeixinNotifyTransData(Map<String, String> parameters, PaymentTransactionLogger transactionLogger) {
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 以下字段在return_code为SUCCESS的时候有返回 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        // 公众账号ID appid 微信分配的公众账号ID（企业号corpid即为此appId）
        // String appId = parameters.get("appid");
        // 商户号 mch_id 微信支付分配的商户号
        // String mchId = parameters.get("mch_id");
        // 设备号 device_info 微信支付分配的终端设备号
        String deviceInfo = parameters.get("device_info");
        // 随机字符串 nonce_str 随机字符串，不长于32位
        String nonceStr = parameters.get("nonce_str");
        // 错误代码 err_code 否 String(32) SYSTEMERROR 错误返回的信息描述
        // String errCode = parameters.get("err_code");
        // 错误代码描述 err_code_des 错误返回的信息描述
        // String errCodeDes = parameters.get("err_code_des");
        // 用户标识 openid 用户在商户appid下的唯一标识
        String openId = parameters.get("openid");
        // 是否关注公众账号 is_subscribe 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
        String isSubscribe = parameters.get("is_subscribe");
        // 交易类型 trade_type JSAPI JSAPI、NATIVE、APP
        String tradeType = parameters.get("trade_type");
        // 付款银行 bank_type 银行类型，采用字符串类型的银行标识，银行类型见银行列表
        String bankType = parameters.get("bank_type");
        // 货币种类 fee_type 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        String feeType = parameters.get("fee_type");
        // 现金支付金额 cash_fee 现金支付金额订单现金支付金额，详见支付金额
        // String cashFee = parameters.get("cash_fee");
        // 现金支付货币类型 cash_fee_type 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        // String cashFeeType = parameters.get("cash_fee_type");
        // 代金券或立减优惠金额 coupon_fee 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
        // int couponFee = Integer.parseInt(parameters.get("coupon_fee")) / 100;
        // 代金券或立减优惠使用数量 coupon_count 代金券或立减优惠使用数量
        // int couponCount = Integer.parseInt(parameters.get("coupon_count"));
        // 代金券或立减优惠ID coupon_id_$n 代金券或立减优惠ID,$n为下标，从0开始编号
        // String couponIdNum = parameters.get("coupon_id_$n");
        // 单个代金券或立减优惠支付金额 coupon_fee_$n 单个代金券或立减优惠支付金额,$n为下标，从0开始编号
        // int couponFeeSingle = Integer.parseInt(parameters.get("coupon_fee_$n"));
        // 微信支付订单号 transaction_id
        String transactionId = parameters.get("transaction_id");
        // 商家数据包 attach 商家数据包，原样返回
        // String attach = parameters.get("attach");
        // 支付完成时间 time_end 格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
        String timeEnd = parameters.get("time_end");
        // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 业务数据 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
        String body = "deviceInfo:" + deviceInfo +
                CommonUtils.SPACE +
                "nonceValue:" + nonceStr +
                CommonUtils.SPACE +
                "tradeType:" + tradeType +
                CommonUtils.SPACE +
                "bankType:" + bankType +
                CommonUtils.SPACE +
                "feeType:" + feeType +
                CommonUtils.SPACE +
                "subscribe:" + isSubscribe;

        // 修改对象的状态
        transactionLogger.setTransStatus(transactionLogger.getTransStatus() | TransStatusEnum.TRADE_SUCCESS_SERVER.getKey());
        transactionLogger.setChannelUserId(openId);
        transactionLogger.setChannelUserName(openId);
        transactionLogger.setChannelTradeNumber(transactionId);
        transactionLogger.setTransCreateTime(transactionLogger.getCreateTime());
        transactionLogger.setPaymentTime(new Date(CommonUtils.dateFormatToLong(timeEnd)));
        transactionLogger.setChannelRemark(body);
    }

    public static void main(String[] args) {
        String value = "<xml><appid>wxf3c01ed21e6e9c6f</appid><body><![CDATA[密码]]></body><mch_id>1230969802</mch_id><nonce_str>teHNxDNASW6Q6VAf</nonce_str><notify_url>http://apptest.0085.com/pay/pay/wechat/payCallBack.action</notify_url><openid>ow1CduOTc0yLr40kdMSxgWSgNe0c</openid><out_trade_no>20171281erq2504810540</out_trade_no><sign><![CDATA[6910EDC3E7828D77310E92E2CE3E7EC0]]></sign><spbill_create_ip>14.150.64.118</spbill_create_ip><total_fee>3</total_fee><trade_type>JSAPI</trade_type></xml>";
        Map<String, String> parameters = XMLSupport.xmlToMap(value);

        TencentPayment tencentPayment = new TencentPayment();

        JSONObject response = tencentPayment.weixinNativePaymentParameters(parameters.get("out_trade_no"), 1, "password", parameters.get("mch_id"), parameters.get("spbill_create_ip"), "");
        System.out.println(response);
    }
}