package com.xlibao.saas.market.service.item;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.market.data.model.MarketSpecialButton;
import com.xlibao.saas.market.service.XMarketTimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author chinahuangxc on 2017/7/20.
 */
@Component
public class MarketItemDataCacheService {

    private static final Logger logger = LoggerFactory.getLogger(MarketItemDataCacheService.class);

    @Autowired
    private DataAccessFactory dataAccessFactory;

    // 特殊按钮数据专用读写锁
    private static final ReentrantReadWriteLock SPECIAL_BUTTON_READ_WRITE_LOCK = new ReentrantReadWriteLock();
    // 特殊按钮数据专用读锁
    private static final ReentrantReadWriteLock.ReadLock SPECIAL_BUTTON_READ_LOCK = SPECIAL_BUTTON_READ_WRITE_LOCK.readLock();
    // 特殊按钮数据专用写锁
    private static final ReentrantReadWriteLock.WriteLock SPECIAL_BUTTON_WRITE_LOCK = SPECIAL_BUTTON_READ_WRITE_LOCK.writeLock();

    private static final Map<Long, MarketSpecialButton> MARKET_SPECIAL_BUTTON_MAP = new ConcurrentHashMap<>();

    private MarketSpecialButtonComparator specialButtonComparator = new MarketSpecialButtonComparator();

    void initMarketItemCache() {
        Callable<Boolean> loaderMarketCallable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                try {
                    logger.info("系统正在加载特殊按钮到缓存中......");
                    loaderSpecialButton();
                } catch (Throwable cause) {
                    cause.printStackTrace();
                    return false;
                }
                AsyncScheduledService.submitDelayCacheTask(this, XMarketTimeConfig.DELAY, XMarketTimeConfig.TIME_UNIT);
                return true;
            }
        };
        boolean result = AsyncScheduledService.waitFutureCacheTask(loaderMarketCallable);
        if (!result) {
            // 一般不能让服务器正常启动
            System.exit(0);
        }
    }

    private void loaderSpecialButton() {
        List<MarketSpecialButton> buttons = dataAccessFactory.getItemDataAccessManager().getButtons();

        Map<Long, MarketSpecialButton> marketSpecialButtonMap = new ConcurrentHashMap<>();
        Map<Integer, Map<String, MarketSpecialButton>> marketSpecialButtonTypeCache = new ConcurrentHashMap<>();

        if (!CommonUtils.isEmpty(buttons)) {
            for (MarketSpecialButton button : buttons) {
                marketSpecialButtonMap.put(button.getId(), button);

                Map<String, MarketSpecialButton> map = marketSpecialButtonTypeCache.get(button.getType());
                if (map == null) {
                    map = new ConcurrentHashMap<>();
                    marketSpecialButtonTypeCache.put(button.getType(), map);
                }
                map.put(button.getJumpUrl(), button);
            }
        }
        try {
            if (SPECIAL_BUTTON_WRITE_LOCK.tryLock(XMarketTimeConfig.WAIT_LOCK_TIME_OUT, XMarketTimeConfig.WAIT_LOCK_TIME_UNIT)) {
                try {
                    MARKET_SPECIAL_BUTTON_MAP.clear();

                    MARKET_SPECIAL_BUTTON_MAP.putAll(marketSpecialButtonMap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    SPECIAL_BUTTON_WRITE_LOCK.unlock();
                }
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }

    public List<MarketSpecialButton> getButtons() {
        try {
            if (!SPECIAL_BUTTON_READ_LOCK.tryLock(XMarketTimeConfig.WAIT_LOCK_TIME_OUT, XMarketTimeConfig.WAIT_LOCK_TIME_UNIT)) {
                return dataAccessFactory.getItemDataAccessManager().getButtons();
            }
            try {
                List<MarketSpecialButton> buttons = new ArrayList<>(MARKET_SPECIAL_BUTTON_MAP.values());
                Collections.sort(buttons, specialButtonComparator);
                return buttons;
            } finally {
                SPECIAL_BUTTON_READ_LOCK.unlock();
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return dataAccessFactory.getItemDataAccessManager().getButtons();
    }

    private class MarketSpecialButtonComparator implements Comparator<MarketSpecialButton> {

        @Override
        public int compare(MarketSpecialButton o1, MarketSpecialButton o2) {
            if (o1.getSort() < o2.getSort()) {
                return -1;
            }
            if (o1.getSort() > o2.getSort()) {
                return 1;
            }
            return o1.getCreateTime().getTime() > o2.getCreateTime().getTime() ? 1 : (o1.getCreateTime().getTime() < o2.getCreateTime().getTime() ? -1 : 0);
        }
    }
}