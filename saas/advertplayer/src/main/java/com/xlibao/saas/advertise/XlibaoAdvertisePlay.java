package com.xlibao.saas.advertise;

import com.xlibao.saas.advertise.service.AdvertisePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * <pre>
 *     <b>Spring容器加载后的通知入口</b>
 * </pre>
 *
 * @author chinahuangxc on 2017/2/4.
 */
@Configuration
@Lazy(false)
public class XlibaoAdvertisePlay {

    private static final Logger logger = LoggerFactory.getLogger(XlibaoAdvertisePlay.class);

    @Autowired
    private AdvertisePlayService advertisePlayService;


    @PostConstruct
    public void applicationContextInit() {
        logger.info("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 广告播放开始初始化状态 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        advertisePlayService.adverStart();
        logger.info("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 广告播放初始化状态完成 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
    }
}