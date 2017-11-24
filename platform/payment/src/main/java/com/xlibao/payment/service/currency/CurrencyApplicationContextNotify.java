package com.xlibao.payment.service.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/4.
 */
@Component
public class CurrencyApplicationContextNotify {

    private static final String MODULE_NAME = "currency";

    @Autowired
    private CurrencyEventListenerManager currencyEventListenerManager;
    @Autowired
    private InternalCurrencyEventListenerImpl currencyEventListener;

    public void loaderNotify() {
        currencyEventListenerManager.registerCurrencyEventListener(MODULE_NAME, currencyEventListener);
    }
}