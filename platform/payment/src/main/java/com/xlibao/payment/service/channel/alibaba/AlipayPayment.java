package com.xlibao.payment.service.channel.alibaba;

import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.payment.config.AlipayPaymentConfig;
import com.xlibao.payment.config.ConfigFactory;
import com.xlibao.payment.data.mapper.PaymentDataAccessManager;
import com.xlibao.payment.data.model.PaymentTransactionLogger;
import com.xlibao.payment.service.channel.alibaba.rsa.SignUtils;
import com.xlibao.payment.service.channel.alibaba.util.AlipayNotify;
import com.xlibao.payment.service.currency.CurrencyEventListenerManager;
import com.xlibao.payment.service.trans.TransactionEventListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Component
public class AlipayPayment extends BasicWebService {

    private static final Logger logger = LoggerFactory.getLogger(AlipayPayment.class);

    @Autowired
    private PaymentDataAccessManager paymentDataAccessManager;
    @Autowired
    private TransactionEventListenerManager transactionEventListenerManager;
    @Autowired
    private CurrencyEventListenerManager currencyEventListenerManager;

    public String generationEncryptionPaymentURL(String transSequenceNumber, String subject, String body, String price) {
        String paymentURL = generationPaymentURL(transSequenceNumber, subject, body, price);
        logger.info(transSequenceNumber + "生成支付参数：" + paymentURL);

        String sign = SignUtils.sign(paymentURL, ConfigFactory.getAlipayPaymentConfig().privateKey);
        try {
            assert sign != null;
            sign = URLEncoder.encode(sign, AlipayPaymentConfig.INPUT_CHARSET);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paymentURL + ("&sign=\"" + sign + "\"&sign_type=\"RSA\"");
    }

    private String generationPaymentURL(String transSequenceNumber, String subject, String body, String price) {
        // 签约合作者身份ID
        StringBuilder payURL = new StringBuilder("partner=");
        payURL.append("\"").append(ConfigFactory.getAlipayPaymentConfig().partnerId).append("\"");

        // 签约卖家支付宝账号
        payURL.append("&seller_id=\"").append(ConfigFactory.getAlipayPaymentConfig().seller).append("\"");
        // 商户网站唯一订单号
        payURL.append("&out_trade_no=\"").append(transSequenceNumber).append("\"");
        // 商品名称
        payURL.append("&subject=\"").append(subject).append("\"");
        // 商品详情
        payURL.append("&body=\"").append(body).append("\"");
        // 商品金额
        payURL.append("&total_fee=\"").append(price).append("\"");
        // 服务器异步通知页面路径
        payURL.append("&notify_url=\"").append(ConfigFactory.getDomainNameConfig().paymentRemoteURL).append("payment/channelCallback/notifyAlipayNativePayment\"");
        // 服务接口名称， 固定值
        payURL.append("&service=\"mobile.securitypay.pay\"");
        // 支付类型， 固定值
        payURL.append("&payment_type=\"1\"");
        // 参数编码， 固定值
        payURL.append("&_input_charset=\"utf-8\"");
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        payURL.append("&it_b_pay=\"30m\"");
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // payURL.append("&extern_token=" + "\"" + extern_token + "\"");
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        // payURL.append("&return_url=\"m.alipay.com\"");

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // payURL.append("&paymethod=\"expressGateway\"");
        return payURL.toString();
    }

    public void notifyAlipayNativePaymented() {
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse response = getHttpServletResponse();

        logger.info(CommonUtils.dateFormat(System.currentTimeMillis()) + "接受到支付宝回调请求：" + request.getRemoteAddr());
        try {
            request.setCharacterEncoding("UTF-8");

            String outTradeNo = new String(getUTF("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            Map<String, String[]> requestParams = request.getParameterMap();

            Map<String, String> params = new HashMap<>();
            for (String name : requestParams.keySet()) {
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            // 通过我方的参数内容获取交易记录
            PaymentTransactionLogger transactionLogger = paymentDataAccessManager.getTransactionLogger(outTradeNo);
            if (AlipayNotify.verify(params)) {
                // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
                String tradeNo = new String(getUTF("trade_no").getBytes("ISO-8859-1"), "UTF-8");
                String tradeStatus = new String(getUTF("trade_status").getBytes("ISO-8859-1"), "UTF-8");

                String buyerId = request.getParameter("buyer_id");
                String buyerEmail = request.getParameter("buyer_email");
                String totalFee = request.getParameter("total_fee");
                String body = request.getParameter("body");
                String gmtCreate = request.getParameter("gmt_create");
                String gmtPayment = request.getParameter("gmt_payment");

                switch (tradeStatus) {
                    case "WAIT_BUYER_PAY": {
                        if (transactionLogger.getTransStatus() != TransStatusEnum.TRADE_DEFAULT.getKey()) {
                            print(response, "fail");
                            return;
                        }
                        fillAliNotiryTransData(transactionLogger, TransStatusEnum.TRADE_DEFAULT.getKey(), CommonUtils.nullToEmpty(buyerId), CommonUtils.nullToEmpty(buyerEmail), CommonUtils.nullToEmpty(tradeNo), body);
                        transactionLogger.setTransCreateTime(new Date(CommonUtils.dateFormatToLong(gmtCreate)));
                    }
                    break;
                    case "TRADE_SUCCESS": {
                        if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_FINISHED.getKey()) == TransStatusEnum.TRADE_FINISHED.getKey()) {
                            print(response, "success");
                            return;
                        }
                        if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) == TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) {
                            print(response, "success");
                            return;
                        }
                        fillAliNotiryTransData(transactionLogger, TransStatusEnum.TRADE_SUCCESS_SERVER.getKey(), CommonUtils.nullToEmpty(buyerId), CommonUtils.nullToEmpty(buyerEmail), CommonUtils.nullToEmpty(tradeNo), body);
                        transactionLogger.setTransCreateTime(new Date(CommonUtils.dateFormatToLong(gmtCreate)));
                        transactionLogger.setPaymentTime(new Date(CommonUtils.dateFormatToLong(gmtPayment)));

                        transactionEventListenerManager.notifyFinishPayment(transactionLogger, TransStatusEnum.TRADE_SUCCESS_SERVER, true);
                        // 通知额度偏移，这里主要是做一个流水记录
                        currencyEventListenerManager.notifyOffsetCurrencyAmount(transactionLogger.getPassportId(), transactionLogger.getChannelId(), CurrencyTypeEnum.BALANCE.getKey(), 0,
                                -Math.abs(transactionLogger.getTransTotalAmount()), 0, transactionLogger.getPaymentType(), transactionLogger.getTransTitle(), transactionLogger.getTransType(), transactionLogger.getTransSequenceNumber());
                    }
                    break;
                    case "TRADE_FINISHED": {
                        if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_FINISHED.getKey()) == TransStatusEnum.TRADE_FINISHED.getKey()) {
                            print(response, "success");
                            return;
                        }
                        if (!transactionLogger.getChannelUserId().trim().equals(buyerId)) {
                            print(response, "fail");
                            return;
                        }
                        if (transactionLogger.getTransTotalAmount() != (long) Float.parseFloat(totalFee) * 100) {
                            print(response, "fail");
                            return;
                        }
                        fillAliNotiryTransData(transactionLogger, TransStatusEnum.TRADE_FINISHED.getKey(), CommonUtils.nullToEmpty(buyerId), CommonUtils.nullToEmpty(buyerEmail), CommonUtils.nullToEmpty(tradeNo), body);
                        transactionLogger.setTransCreateTime(new Date(CommonUtils.dateFormatToLong(gmtCreate)));
                        transactionLogger.setPaymentTime(new Date(CommonUtils.dateFormatToLong(gmtPayment)));

                        transactionEventListenerManager.notifyFinishPayment(transactionLogger, TransStatusEnum.TRADE_FINISHED, false);
                    }
                    break;
                    case "TRADE_CLOSED": {
                        fillAliNotiryTransData(transactionLogger, TransStatusEnum.TRADE_CLOSED.getKey(), CommonUtils.nullToEmpty(buyerId), CommonUtils.nullToEmpty(buyerEmail), CommonUtils.nullToEmpty(tradeNo), body);
                        transactionLogger.setTransCreateTime(new Date(CommonUtils.dateFormatToLong(gmtCreate)));
                        transactionLogger.setPaymentTime(new Date(CommonUtils.dateFormatToLong(gmtPayment)));

                        transactionEventListenerManager.notifyFinishPayment(transactionLogger,  TransStatusEnum.TRADE_CLOSED, true);
                    }
                    break;
                }
                // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                print(response, "success");
                return;
            }
            logger.error("支付宝回调参数验证失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        print(response, "fail");
    }

    private void fillAliNotiryTransData(PaymentTransactionLogger transactionLogger, int transStatus, String channelUserId, String channelUserName, String channelTransSequenceNumber, String body) {
        // 修改对象的状态
        transactionLogger.setTransStatus(transactionLogger.getTransStatus() | transStatus);
        transactionLogger.setPaymentType(PaymentTypeEnum.ALIPAY.getKey());
        transactionLogger.setChannelUserId(channelUserId);
        transactionLogger.setChannelUserName(channelUserName);
        transactionLogger.setChannelTradeNumber(channelTransSequenceNumber);
        transactionLogger.setChannelRemark(body);
    }
}
