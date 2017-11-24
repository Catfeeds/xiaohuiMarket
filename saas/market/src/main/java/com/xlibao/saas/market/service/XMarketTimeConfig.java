package com.xlibao.saas.market.service;

import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/3/10.
 */
public class XMarketTimeConfig {

    /** 商品价格保护时间 */
    public static final long PRICE_PROTECTION_TIME = TimeUnit.MINUTES.toMillis(15);

    /** 商品库存保护时间 */
    public static final long ITEM_STOCK_LOCK_TIME = TimeUnit.MINUTES.toMillis(15);

    /** 订单失效时间 -- 1天 */
    public static final long ORDER_INVALID_TIME = TimeUnit.DAYS.toMillis(1);

    /** 新品有效时间 -- 7天 */
    public static final long ITEM_NEW_TIME = TimeUnit.DAYS.toMillis(7);

    public static final long DELAY = 2;
    public static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    public static final long WAIT_LOCK_TIME_OUT = 2;
    public static final TimeUnit WAIT_LOCK_TIME_UNIT = TimeUnit.SECONDS;

    public static final long ORDER_STATUS_RECOVER_DELAY = 10;
    public static final TimeUnit ORDER_STATUS_RECOVER_TIME_UNIT = TimeUnit.MINUTES;
}