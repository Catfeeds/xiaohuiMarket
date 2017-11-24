package com.xlibao.order.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/24.
 */
@Component
public class OrderApplicationContextNotify {

    private static final String MODULE_NAME = "order";

    @Autowired
    private OrderEventListenerManager orderEventListenerManager;
    @Autowired
    private InternalOrderEventListenerImpl orderEventListener;

    public void loaderNotify() {
        orderEventListenerManager.registerOrderEventListener(MODULE_NAME, orderEventListener);
    }
}