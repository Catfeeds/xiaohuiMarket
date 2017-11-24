package com.xlibao.payment.service.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/4.
 */
@Component
public class TransApplicationContextNotify {

    private static final String MODULE_NAME = "transaction";

    @Autowired
    private TransactionEventListenerManager transactionEventListenerManager;
    @Autowired
    private InternalTransactionEventListenerImpl transactionEventListener;

    public void loaderNotify() {
        transactionEventListenerManager.registerTransactionEventListener(MODULE_NAME, transactionEventListener);
    }
}
