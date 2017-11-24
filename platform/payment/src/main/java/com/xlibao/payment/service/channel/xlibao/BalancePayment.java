package com.xlibao.payment.service.channel.xlibao;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.DefineRandom;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.payment.data.mapper.PaymentDataAccessManager;
import com.xlibao.payment.data.model.PaymentCurrencyAccount;
import com.xlibao.payment.data.model.PaymentTransactionLogger;
import com.xlibao.payment.service.currency.CurrencyEventListenerManager;
import com.xlibao.payment.service.trans.TransactionEventListenerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author chinahuangxc on 2017/2/3.
 */
@Transactional
@Component
public class BalancePayment {

    @Autowired
    private PaymentDataAccessManager paymentDataAccessManager;
    @Autowired
    private TransactionEventListenerManager transactionEventListenerManager;
    @Autowired
    private CurrencyEventListenerManager currencyEventManager;

    public JSONObject prePayment(PaymentTransactionLogger transactionLogger) {
        JSONObject response = new JSONObject();
        response.put("prePaymentId", DefineRandom.randomNumber(8) + "" + transactionLogger.getId());
        return response;
    }

    public void decreaseBalanceAmount(long passportId, String prePaymentId) {
        // 扣除余额费用
        decreaseCurrencyAmount(passportId, prePaymentId);
    }

    private void decreaseCurrencyAmount(long passportId, String prePaymentId) {
        long tid = Long.parseLong(prePaymentId.substring(8));
        PaymentTransactionLogger transactionLogger = paymentDataAccessManager.getTransactionLoggerById(tid);
        if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) == TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) {
            throw new XlibaoRuntimeException("交易已为支付状态，请不要重复支付！");
        }
        long transTotalAmount = transactionLogger.getTransTotalAmount();
        Passport passport = PassportRemoteService.getPassport(passportId); // 获取用户信息 -- 远程
        // 本次支付的货币类型
        CurrencyTypeEnum currencyTypeEnum = Objects.equals(transactionLogger.getPaymentType(), PaymentTypeEnum.BALANCE.getKey()) ? CurrencyTypeEnum.BALANCE : CurrencyTypeEnum.VIP_BALANCE;
        // 发生变化的货币记录
        PaymentCurrencyAccount currencyAccount = paymentDataAccessManager.getCurrencyAccount(passportId, passport.getFromChannel(), currencyTypeEnum);

        if (currencyAccount.getCurrentAmount() < transTotalAmount) {
            throw new XlibaoRuntimeException("您的余额不足！");
        }
        long offsetAmount = -Math.abs(transTotalAmount);
        int result = paymentDataAccessManager.offsetCurrencyAmount(passportId, passport.getFromChannel(), currencyTypeEnum.getKey(), offsetAmount);
        if (result <= 0) {
            throw new XlibaoRuntimeException("扣除余额失败，请检查是否余额不足！");
        }
        // 修改对象的状态
        transactionLogger.setPassportId(passportId);
        transactionLogger.setTransStatus(transactionLogger.getTransStatus() | TransStatusEnum.TRADE_SUCCESS_SERVER.getKey());
        transactionLogger.setChannelUserId(String.valueOf(passportId));
        transactionLogger.setChannelUserName(passport.getDefaultName());
        transactionLogger.setChannelTradeNumber(transactionLogger.getTransSequenceNumber());
        transactionLogger.setTransCreateTime(transactionLogger.getCreateTime());
        transactionLogger.setPaymentTime(new Date());
        transactionLogger.setChannelRemark("");
        transactionEventListenerManager.notifyFinishPayment(transactionLogger, TransStatusEnum.TRADE_SUCCESS_SERVER, true);

        currencyEventManager.notifyOffsetCurrencyAmount(transactionLogger.getPassportId(), transactionLogger.getChannelId(), currencyTypeEnum.getKey(), currencyAccount.getCurrentAmount(), offsetAmount,
                (currencyAccount.getCurrentAmount() + offsetAmount), transactionLogger.getPaymentType(), transactionLogger.getTransTitle(), transactionLogger.getTransType(), transactionLogger.getTransSequenceNumber());
    }
}