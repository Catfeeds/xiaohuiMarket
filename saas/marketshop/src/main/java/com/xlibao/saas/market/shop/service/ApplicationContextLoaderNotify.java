package com.xlibao.saas.market.shop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/26.
 */
@Component
public class ApplicationContextLoaderNotify {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextLoaderNotify.class);

    /**
     * <pre>
     * 该方法作为系统加载的全局入口
     * 可在该方法执行如下的操作：
     *      1、加载系统模版数据
     *      2、启动定时/延迟任务
     *      3、未完成的数据任务重载
     *      4、过期数据的清理
     *      5、加载/检查自定义数据模版的合法性(这里不需要处理Spring加载的配置文件)
     *      6、其他相关的数据加载，如：从配置服下载全局常量等
     * </pre>
     */
    public void applicationContextLoaderNotify() {
        logger.info("启动应用容器数据加载");
    }
}