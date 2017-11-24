package com.xlibao.saas.market.service;

import com.xlibao.saas.market.service.order.OrderStatusCorrectTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/5/7.
 */
@Component
public class TimeTaskLockScheduledService {

    @Autowired
    private OrderStatusCorrectTask orderStatusCorrectTask;

    void executorTask() {
        orderStatusCorrectTask.start();
    }
}