package com.xlibao.payment.service.trans;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.payment.data.mapper.PaymentDataAccessManager;
import com.xlibao.payment.data.model.PaymentCurrencyAccount;
import com.xlibao.payment.data.model.PaymentRechargePresent;
import com.xlibao.payment.data.model.PaymentTransactionLogger;
import com.xlibao.payment.listener.TransactionEventListener;
import com.xlibao.payment.service.currency.CurrencyEventListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/3.
 */
@Component
public class InternalTransactionEventListenerImpl implements TransactionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(InternalTransactionEventListenerImpl.class);

    @Autowired
    private PaymentDataAccessManager paymentDataAccessManager;
    @Autowired
    private CurrencyEventListenerManager currencyEventListenerManager;

    @Override
    public void notifyFinishPayment(PaymentTransactionLogger transactionLogger, TransStatusEnum transStatusEnum, boolean callback) {
        if (transactionLogger.getTransType() == TransTypeEnum.RECHARGE.getKey()) {
            Passport passport = PassportRemoteService.getPassport(transactionLogger.getPassportId());
            // 执行充值流程
            long offsetAmount = rechargeBalance(passport, transactionLogger, transStatusEnum);
            int currencyType = Integer.parseInt(transactionLogger.getExtendParameter());

            PaymentCurrencyAccount currencyAccount = paymentDataAccessManager.getCurrencyAccount(passport.getId(), passport.getFromChannel(), CurrencyTypeEnum.getCurrencyTypeEnum(currencyType));
            long afterAmount = currencyAccount.getCurrentAmount();
            long beforeAmount = afterAmount - offsetAmount;
            // 通知额度偏移，这里主要是做一个流水记录
            currencyEventListenerManager.notifyOffsetCurrencyAmount(transactionLogger.getPassportId(), transactionLogger.getChannelId(), currencyType, beforeAmount, offsetAmount,
                    afterAmount, transactionLogger.getPaymentType(), transactionLogger.getTransTitle(), transactionLogger.getTransType(), transactionLogger.getTransSequenceNumber());
            // 设置最终的交易总额
            transactionLogger.setTransTotalAmount(offsetAmount);
        }
        // 执行更新
        int result = paymentDataAccessManager.modifyTransactionStatus(transactionLogger);
        if (result < 1) { // 更新状态失败
            throw new XlibaoRuntimeException("通知支付完成发生异常，无法更新交易流水，流水ID：" + transactionLogger.getId());
        }
        // 通知监听服务
        if (callback) {
            callback(transactionLogger, transStatusEnum);
        }
    }

    private long rechargeBalance(Passport passport, PaymentTransactionLogger transactionLogger, TransStatusEnum transStatusEnum) {
        if (transStatusEnum != TransStatusEnum.TRADE_SUCCESS_SERVER) {
            return 0;
        }
        PaymentRechargePresent rechargePresent = paymentDataAccessManager.getRechargePresent(transactionLogger.getTransTotalAmount());
        long presentAmount = rechargePresent == null ? 0 : rechargePresent.getPresentAmount();
        long offsetAmount = transactionLogger.getTransTotalAmount() + presentAmount;
        // 执行充值操作
        paymentDataAccessManager.offsetCurrencyAmount(passport.getId(), passport.getFromChannel(), Integer.parseInt(transactionLogger.getExtendParameter()), offsetAmount);

        return offsetAmount;
    }

    private void callback(PaymentTransactionLogger transactionLogger, TransStatusEnum transStatusEnum) {
        if (CommonUtils.isNullString(transactionLogger.getNotifyUrl())) {
            return;
        }
        JSONObject data = new JSONObject();

        data.put("partnerId", transactionLogger.getPartnerId());
        data.put("appId", transactionLogger.getAppId());
        data.put("transSequenceNumber", transactionLogger.getTransSequenceNumber());
        data.put("transStatus", transStatusEnum.getKey());
        data.put("paymentType", transactionLogger.getPaymentType());
        data.put("transType", transactionLogger.getTransStatus());
        data.put("partnerUserId", transactionLogger.getPartnerUserId());
        data.put("partnerTradeNumber", transactionLogger.getPartnerTradeNumber());
        data.put("channelTradeNumber", transactionLogger.getChannelTradeNumber());
        data.put("channelUserId", transactionLogger.getChannelUserId());
        data.put("channelUserName", CommonUtils.nullToEmpty(transactionLogger.getChannelUserName()));
        data.put("channelRemark", CommonUtils.nullToEmpty(transactionLogger.getChannelRemark()));
        data.put("transUnitAmount", transactionLogger.getTransUnitAmount());
        data.put("transNumber", transactionLogger.getTransNumber());
        data.put("transTotalAmount", transactionLogger.getTransTotalAmount());
        data.put("transCreateTime", transactionLogger.getTransCreateTime().getTime());
        data.put("paymentTime", transactionLogger.getPaymentTime().getTime());
        data.put("useCoupon", transactionLogger.getUseCoupon());
        data.put("discountAmount", transactionLogger.getDiscountAmount());
        data.put("extendParameter", CommonUtils.nullToEmpty(transactionLogger.getExtendParameter()));

        String signatureParameters = PassportRemoteService.signatureParameters(data);
        data.put("sign", signatureParameters);

        logger.info("支付回调地址：" + transactionLogger.getNotifyUrl() + "\r\n回调内容：" + data.toJSONString());

        Map<String, String> parameters = new HashMap<>();

        parameters.put("data", data.toJSONString());

        try {
            String result = HttpRequest.post(transactionLogger.getNotifyUrl(), parameters);

            JSONObject response = JSONObject.parseObject(result);
            logger.info(transactionLogger.getTransSequenceNumber() + " 通知支付结果：" + response);
            if (response.getIntValue("code") != BasicWebService.SUCCESS_CODE) {
                throw new XlibaoRuntimeException(response.getIntValue("code"), response.getString("msg"));
            }
        } catch (Exception ex) {
            logger.error(transactionLogger.getTransSequenceNumber() + "支付通知发生异常，通知内容：" + data.toJSONString());
            throw new XlibaoRuntimeException("支付失败，请稍后重试！", ex);
        }
    }
}