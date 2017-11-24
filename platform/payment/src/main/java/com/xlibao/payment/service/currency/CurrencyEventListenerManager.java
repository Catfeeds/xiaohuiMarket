package com.xlibao.payment.service.currency;

import com.xlibao.payment.listener.CurrencyEventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/3.
 */
@Component
public class CurrencyEventListenerManager {

    private Map<String, CurrencyEventListener> currencyEventListenerMap = new HashMap<>();

    public void registerCurrencyEventListener(String name, CurrencyEventListener currencyEventListener) {
        currencyEventListenerMap.put(name, currencyEventListener);
    }

    public void notifyOffsetCurrencyAmount(long passportId, int channelId, int currencyType, long beforeAmount, long offsetAmount, long afterAmount, String transType, String transTitle, int relationTransType, String relationTransSequence) {
        // 通知监听系统
        for (CurrencyEventListener currencyEventListener : currencyEventListenerMap.values()) {
            currencyEventListener.notifyOffsetCurrencyAmount(passportId, channelId, currencyType, beforeAmount, offsetAmount, afterAmount, transType, transTitle, relationTransType, relationTransSequence);
        }
    }
}