package com.xlibao.payment.service.currency;

import com.xlibao.payment.data.mapper.PaymentDataAccessManager;
import com.xlibao.payment.data.model.PaymentCurrencyOffsetLogger;
import com.xlibao.payment.listener.CurrencyEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/4.
 */
@Component
public class InternalCurrencyEventListenerImpl implements CurrencyEventListener {

    @Autowired
    private PaymentDataAccessManager paymentDataAccessManager;

    @Override
    public void notifyOffsetCurrencyAmount(long passportId, int channelId, int currencyType, long beforeAmount, long offsetAmount, long afterAmount, String transType, String transTitle, int relationTransType, String relationTransSequence) {
        PaymentCurrencyOffsetLogger currencyOffsetLogger = new PaymentCurrencyOffsetLogger();

        currencyOffsetLogger.setPassportId(passportId);
        currencyOffsetLogger.setChannelId(channelId);
        currencyOffsetLogger.setCurrencyType(currencyType);
        currencyOffsetLogger.setBeforeAmount(beforeAmount);
        currencyOffsetLogger.setOffsetAmount(offsetAmount);
        currencyOffsetLogger.setAfterAmount(afterAmount);
        currencyOffsetLogger.setTransType(transType);
        currencyOffsetLogger.setTransTitle(transTitle);
        currencyOffsetLogger.setRelationTransType(relationTransType);
        currencyOffsetLogger.setRelationTransSequence(relationTransSequence);

        paymentDataAccessManager.createCurrencyOffsetLogger(currencyOffsetLogger);
    }
}