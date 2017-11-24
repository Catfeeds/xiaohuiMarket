package com.xlibao.io.service.jpush;

import cn.jpush.api.JPushClient;
import org.apache.log4j.Logger;

/**
 * @author chinahuangxc on 2016/11/14.
 */
public class JPushConfig {

    private static final Logger logger = Logger.getLogger(JPushConfig.class);

    // 开启APN的推送环境，默认是false
    static final boolean APN_PRODUCTION = true;
    // 消息缓存时常，默认24小数
    static final int TIME_LIVE = 3600;
    // 最多的重试次数
    private static final int MAX_RETRY_TIMES = 5;
    // 默认标题内容
    static final String DEFAULT_TITLE = "消息提醒";
    // 默认提醒内容
    static final String DEFAULT_CONTENT = "您有一条提醒消息";

    public static JPushClient initialJPushClient(String appSecret, String appKey) {
        logger.info("JPush app secret " + appSecret + "; app key " + appKey);

        return new JPushClient(appSecret, appKey, JPushConfig.MAX_RETRY_TIMES);
    }
}