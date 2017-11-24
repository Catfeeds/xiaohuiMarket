package com.xlibao.payment.listener;

/**
 * @author chinahuangxc on 2017/2/3.
 */
public interface CurrencyEventListener {

    void notifyOffsetCurrencyAmount(long passportId, int channelId, int currencyType, long beforeAmount, long offsetAmount, long afterAmount, String transType, String transTitle, int relationTransType, String relationTransSequence);
}