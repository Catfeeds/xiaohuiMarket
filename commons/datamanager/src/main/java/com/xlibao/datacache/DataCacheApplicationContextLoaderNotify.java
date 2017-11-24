package com.xlibao.datacache;

import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/4/13.
 */
public class DataCacheApplicationContextLoaderNotify {

    public static final long DELAY = 10;
    public static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    public static final long WAIT_LOCK_TIME_OUT = 2;
    public static final TimeUnit WAIT_LOCK_TIME_UNIT = TimeUnit.SECONDS;

    private static String itemRemoteServiceURL = "";
    private static String locationRemoteServiceURL = "";

    /**
     * <pre>
     *      <b>设置当前环境下的商品服务地址</b>
     *      <b>如：</b>DataCacheApplicationContextLoaderNotify.setItemRemoteServiceURL("http://item.xlibao.com/");
     *  </pre>
     */
    public static void setItemRemoteServiceURL(String itemRemoteServiceURL) {
        DataCacheApplicationContextLoaderNotify.itemRemoteServiceURL = itemRemoteServiceURL;
    }

    /**
     * <pre>
     *     <b>获取当前环境下的商品服务地址</b>
     * </pre>
     */
    public static String getItemRemoteServiceURL() {
        return itemRemoteServiceURL;
    }

    public static void setLocationRemoteServiceURL(String locationRemoteServiceURL) {
        DataCacheApplicationContextLoaderNotify.locationRemoteServiceURL = locationRemoteServiceURL;
    }

    public static String getLocationRemoteServiceURL() {
        return locationRemoteServiceURL;
    }
}