package com.xlibao.saas.market.service.item;

import com.xlibao.common.constant.order.DeliverTypeEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.market.data.model.MarketItemStockLockLogger;
import com.xlibao.metadata.order.OrderEntry;
import com.xlibao.metadata.order.OrderItemSnapshot;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.listener.OrderEventListener;
import com.xlibao.saas.market.service.support.remote.MarketShopRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author chinahuangxc on 2017/2/28.
 */
@Transactional
@Component
public class ItemOrderEventListenerImpl implements OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ItemOrderEventListenerImpl.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;
    @Autowired
    private MarketShopRemoteService marketShopRemoteService;

    @Override
    public void notifyCreatedOrder(OrderEntry orderEntry) {
        if (orderEntry.getType() != OrderTypeEnum.SALE_ORDER_TYPE.getKey()) {
            return;
        }
        logger.info("订单建立完成，正在锁定库存，订单序列号为：" + orderEntry.getOrderSequenceNumber());

        List<OrderItemSnapshot> itemSnapshots = orderEntry.getItemSnapshots();
        for (OrderItemSnapshot itemSnapshot : itemSnapshots) {
            logger.info("正在对订单[" + orderEntry.getOrderSequenceNumber() + "]的商品进行锁定操作，当前商品：" + itemSnapshot.getItemName() + "(" + itemSnapshot.getItemId() + ")；锁定数量：" + itemSnapshot.totalQuantity());
            // 锁定库存
            int result = dataAccessFactory.getItemDataAccessManager().lockItemStock(itemSnapshot.getItemId(), itemSnapshot.totalQuantity());
            if (result <= 0) {
                // 干脆整批不锁定
                logger.error("【更新锁定库存】对订单[" + orderEntry.getOrderSequenceNumber() + "]的商品进行锁定操作时发生了错误，当前商品：" + itemSnapshot.getItemName() + "(" + itemSnapshot.getItemId() + ")；锁定数量：" + itemSnapshot.totalQuantity());
                throw new XlibaoRuntimeException("锁定商品库存失败");
            }
            MarketItemStockLockLogger itemStockLockLogger = new MarketItemStockLockLogger();
            itemStockLockLogger.setOrderSequenceNumber(orderEntry.getOrderSequenceNumber());
            itemStockLockLogger.setItemId(itemSnapshot.getItemId());
            itemStockLockLogger.setItemTemplateId(itemSnapshot.getItemTemplateId());
            itemStockLockLogger.setLockQuantity(itemSnapshot.totalQuantity());
            itemStockLockLogger.setLockType(ItemLockTypeEnum.CREATE_ORDER.getKey());
            itemStockLockLogger.setLockStatus(ItemStockLockStatusEnum.LOCK.getKey());
            itemStockLockLogger.setLockTime(new Date());
            result = dataAccessFactory.getItemDataAccessManager().createItemStockLogger(itemStockLockLogger);
            if (result <= 0) {
                // 干脆整批不锁定
                logger.error("【添加锁定记录】对订单[" + orderEntry.getOrderSequenceNumber() + "]的商品进行锁定操作时发生了错误，当前商品：" + itemSnapshot.getItemName() + "(" + itemSnapshot.getItemId() + ")；锁定数量：" + itemSnapshot.totalQuantity());
                throw new XlibaoRuntimeException("锁定商品库存失败");
            }
        }
    }

    @Override
    public void notifyOrderPayment(OrderEntry orderEntry) {
        if (orderEntry.getDeliverType() == DeliverTypeEnum.PICKED_UP.getKey()) {
            marketShopRemoteService.sendShipmentMessage(orderEntry);
        }
    }

    @Override
    public void notifyAcceptedOrder(OrderEntry orderEntry) {
        if (orderEntry.getDeliverType() == DeliverTypeEnum.DISTRIBUTION.getKey()) {
            marketShopRemoteService.sendShipmentMessage(orderEntry);
        }
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