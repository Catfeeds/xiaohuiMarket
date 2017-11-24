package com.xlibao.saas.market.service.order;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.TimeTaskLockTypeEnum;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.market.data.model.MarketItemStockLockLogger;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.data.model.MarketTimeTaskLock;
import com.xlibao.saas.market.service.XMarketTimeConfig;
import com.xlibao.saas.market.service.item.ItemStockLockStatusEnum;
import com.xlibao.saas.market.service.support.remote.OrderRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/5/7.
 */
@Component
public class OrderStatusCorrectTask {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusCorrectTask.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;

    private volatile boolean isExecutor = false;

    public void start() {
        // 当任务已处于执行状态 不再接受启动任务的调用
        if (isExecutor) {
            return;
        }
        isExecutor = true;
        Timer timer = new Timer();

        int minute = CommonUtils.currentMinute();
        int remain = minute % 5;
        if (remain > 0) {
            remain = 5 - remain;
        }
        minute += remain;
        long firstTime = CommonUtils.getTimeMillisecond(CommonUtils.currentHours(), minute);
        // 0 点的任务 -- 直接延迟
        timer.schedule(new Executor(), new Date(firstTime), TimeUnit.MINUTES.toMillis(5));
        logger.info("修复订单状态任务触发时间：" + CommonUtils.dateFormat(firstTime));
    }

    private class Executor extends TimerTask {

        @Override
        public void run() {
            int minute = CommonUtils.currentMinute();
            int remain = minute % 5;
            if (remain > 0) {
                logger.info("系统发起修复订单状态任务，但当前未到系统设定时间点，任务的下一个更新时间点为：" + CommonUtils.getTimeMillisecond(CommonUtils.currentHours(), minute + 5 - remain));
                return;
            }
            try {
                // 发生时间
                String happenDate = CommonUtils.defineDateFormat(System.currentTimeMillis(), CommonUtils.Y_M_D_HH_MM) + ":00";
                logger.info("开始执行[" + TimeTaskLockTypeEnum.MARKET_ORDER_STATUS.getValue() + "]，本次执行时间：" + happenDate);
                while (true) {
                    try {
                        // 数据库检查任务是否已被执行
                        int count = dataAccessFactory.getActivityDataAccessManager().hasExecutor(TimeTaskLockTypeEnum.MARKET_ORDER_STATUS.getKey(), happenDate);
                        if (count > 0) {
                            // 任务已执行 无需再发起 等待调度系统下次发起
                            break;
                        }
                        // 由数据库保证的任务锁
                        MarketTimeTaskLock timeTaskLock = new MarketTimeTaskLock();
                        timeTaskLock.setType(TimeTaskLockTypeEnum.MARKET_ORDER_STATUS.getKey());
                        timeTaskLock.setTitle(TimeTaskLockTypeEnum.MARKET_ORDER_STATUS.getValue());
                        timeTaskLock.setHappenTime(happenDate);
                        // 记录获取任务成功服务器的IP地址 -- 这里注意获取的为局域网地址
                        timeTaskLock.setWinnerAddress(InetAddress.getLocalHost().getHostAddress());

                        int result = dataAccessFactory.getActivityDataAccessManager().createTimeTaskLock(timeTaskLock);
                        if (result == 1) {
                            executorUpdateTask();
                            break;
                        }
                    } catch (Throwable cause) {
                        logger.error("执行任务发生异常，异常信息：" + cause.getMessage(), cause);
                    }
                    // 失败时 1分钟后重试
                    Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                }
            } catch (Throwable cause) {
                logger.error("【非常严重】执行修复订单状态任务发生了异常，谁看到这个日志，请第一时间通知", cause);
            }
        }
    }

    private void executorUpdateTask() {
        try {
            long timeout = System.currentTimeMillis() - XMarketTimeConfig.ITEM_STOCK_LOCK_TIME;
            JSONObject response = OrderRemoteService.findInvalidOrderSize(OrderTypeEnum.SALE_ORDER_TYPE.getKey(), OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey(), timeout);
            logger.info("准备执行自动修复订单状态任务，等待修复的订单数量为：" + response.getJSONObject("response").getIntValue("size"));

            response = OrderRemoteService.batchResetOverdueOrderStatus(OrderTypeEnum.SALE_ORDER_TYPE.getKey(), OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey(), OrderStatusEnum.ORDER_STATUS_CANCEL.getKey(), timeout);
            logger.info("完成自动修复订单状态任务，本次修复的订单数量为：" + response.getJSONObject("response").getIntValue("size"));

            List<MarketItemStockLockLogger> itemStockLockLoggers = dataAccessFactory.getItemDataAccessManager().findInvalidItemStockLockLoggers(ItemStockLockStatusEnum.LOCK.getKey(), CommonUtils.dateFormat(timeout));
            if (CommonUtils.isEmpty(itemStockLockLoggers)) {
                return;
            }
            dataAccessFactory.getItemDataAccessManager().releaseTimeoutItemLockStock(ItemStockLockStatusEnum.RELEASE.getKey(), CommonUtils.dateFormat(timeout), ItemStockLockStatusEnum.LOCK.getKey());

            Map<Long, Integer> releaseItemQuantity = new HashMap<>();
            long totalQuantity = 0;
            for (MarketItemStockLockLogger itemStockLockLogger : itemStockLockLoggers) {
                Integer quantity = releaseItemQuantity.get(itemStockLockLogger.getItemId());
                if (quantity == null) {
                    quantity = 0;
                }
                quantity += itemStockLockLogger.getLockQuantity();
                releaseItemQuantity.put(itemStockLockLogger.getItemId(), quantity);
                totalQuantity += itemStockLockLogger.getLockQuantity();
            }
            logger.info("准备释放被锁定的商品记录，本次释放的商品记录数：" + releaseItemQuantity.size() + "；释放的商品总数量：" + totalQuantity);
            for (Map.Entry<Long, Integer> entry : releaseItemQuantity.entrySet()) {
                dataAccessFactory.getItemDataAccessManager().releaseItemLockQuantity(entry.getKey(), entry.getValue());
            }
        } catch (Exception ex) {
            logger.error("准备执行自动修复订单状态任务时发生了异常", ex);
        }
    }
}