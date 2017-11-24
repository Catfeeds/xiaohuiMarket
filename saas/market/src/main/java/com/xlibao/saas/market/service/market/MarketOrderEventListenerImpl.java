package com.xlibao.saas.market.service.market;

import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.listener.OrderEventListener;
import com.xlibao.saas.market.service.support.remote.MarketShopRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/9/24.
 */
@Transactional
@Component
public class MarketOrderEventListenerImpl implements OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(MarketOrderEventListenerImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;
    @Autowired
    private MarketShopRemoteService shopRemoteService;

    @Override
    public void notifyCreatedOrder(OrderEntry orderEntry) {
    }

    @Override
    public void notifyOrderPayment(OrderEntry orderEntry) {
        if (orderEntry.getUserSource() != DeviceTypeEnum.DEVICE_TYPE_AUTO.getKey()) {
            return;
        }
        logger.info(orderEntry.getOrderSequenceNumber() + "支付完成，正在执行自动出货操作");

        MarketEntry marketEntry = dataAccessFactory.getMarketDataCacheService().getMarket(orderEntry.getShippingPassportId());

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (!shopRemoteService.autoPickupMessage(marketEntry, orderEntry)) {
                    return;
                }
                AsyncScheduledService.submitRemoteNotifyTask(this, 5, TimeUnit.SECONDS);
            }
        };
        AsyncScheduledService.submitRemoteNotifyTask(runnable, 5, TimeUnit.SECONDS);
    }

    @Override
    public void notifyAcceptedOrder(OrderEntry orderEntry) {
    }

    @Override
    public void notifyDeliveredOrderEntry(OrderEntry orderEntry) {
    }

    @Override
    public void notifyConfirmedOrderEntry(OrderEntry orderEntry) {
    }

    @Override
    public void notifyCanceledOrderEntry(OrderEntry orderEntry) {
    }
}