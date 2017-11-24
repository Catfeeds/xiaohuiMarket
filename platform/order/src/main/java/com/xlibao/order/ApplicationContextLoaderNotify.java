package com.xlibao.order;

import com.xlibao.common.servlet.HealthyMonitor;
import com.xlibao.order.service.order.OrderApplicationContextNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *     <b>应用程序容器加载通知器</b>
 *     可执行如下的操作：
 *      1、加载系统模版数据
 *      2、启动定时/延迟任务
 *      3、未完成的数据任务重载
 *      4、过期数据的清理
 *      5、加载/检查自定义数据模版的合法性(这里不需要处理Spring加载的配置文件)
 *      6、其他相关的数据加载，如：从配置服下载全局常量等
 *
 *      具体的方法入口为{@linkplain #applicationContextLoaderNotify()}
 * </pre>
 *
 * @author chinahuangxc on 2017/2/4.
 */
@Component
public class ApplicationContextLoaderNotify {

    @Autowired
    private OrderApplicationContextNotify orderApplicationContextNotify;

    void applicationContextLoaderNotify() {
        orderApplicationContextNotify.loaderNotify();

        HealthyMonitor.healthyInformation();
    }
}