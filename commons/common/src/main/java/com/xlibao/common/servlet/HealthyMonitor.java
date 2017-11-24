package com.xlibao.common.servlet;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.thread.AsyncScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chinahuangxc on 2017/2/4.
 */
public class HealthyMonitor {

    private static final Logger logger = LoggerFactory.getLogger(HealthyMonitor.class);
    
    public static void healthyInformation() {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    long freeMemory = Runtime.getRuntime().freeMemory();
                    long maxMemory = Runtime.getRuntime().maxMemory();
                    long useMemory = maxMemory - freeMemory;

                    logger.info("当前系统会话总数：" + ApplicationSessionListener.getSessionSize());

                    logger.info("\r\n\t当前进程使用内存总数：" + toB(useMemory) + " -- " + toK(useMemory) + " -- " + toM(useMemory) + " -- " + toG(useMemory)
                            + "\r\n\t当前剩余可用内存总数：" + toB(freeMemory) + " -- " + toK(freeMemory) + " -- " + toM(freeMemory) + " -- " + toG(freeMemory)
                            + "\r\n\t当前分配内存总数：" + toB(maxMemory) + " -- " + toK(maxMemory) + " -- " + toM(maxMemory) + " -- " + toG(maxMemory));

                    logger.info("\r\n\t数据库线程池状态 -- 主动执行任务的近似线程数：" + AsyncScheduledService.getDatabaseScheduledActiveTaskCount()
                            + "\r\n\t已完成执行的近似任务总数：" + AsyncScheduledService.getDatabaseScheduledCompletedTaskCount()
                            + "\r\n\t曾计划执行的近似任务总数：" + AsyncScheduledService.getDatabaseScheduledTaskCount());

                    logger.info("\r\n\t缓存线程池状态 -- 主动执行任务的近似线程数：" + AsyncScheduledService.getCacheScheduledActiveTaskCount()
                            + "\r\n\t已完成执行的近似任务总数：" + AsyncScheduledService.getCacheScheduledCompletedTaskCount()
                            + "\r\n\t曾计划执行的近似任务总数：" + AsyncScheduledService.getCacheScheduledTaskCount());

                    logger.info("\r\n\t开放线程池状态 -- 主动执行任务的近似线程数：" + AsyncScheduledService.getCommonScheduledActiveTaskCount()
                            + "\r\n\t已完成执行的近似任务总数：" + AsyncScheduledService.getCommonScheduledCompletedTaskCount()
                            + "\r\n\t曾计划执行的近似任务总数：" + AsyncScheduledService.getCommonScheduledTaskCount());

                    AsyncScheduledService.submitCommonTask(this, GlobalConstantConfig.HEALTHY_MONITOR_DELAY, GlobalConstantConfig.HEALTHY_MONITOR_TIME_UNIT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            private String toB(long memory) {
                return memory + "B";
            }

            private String toK(long memory) {
                return CommonUtils.formatNumber((memory / 1024f), "#.###") + "K";
            }

            private String toM(long memory) {
                return CommonUtils.formatNumber((memory / 1024f / 1024f), "#.###") + "M";
            }

            private String toG(long memory) {
                return CommonUtils.formatNumber((memory / 1024f / 1024f / 1024f), "#.###") + "G";
            }
        };
        AsyncScheduledService.submitCommonTask(runnable, GlobalConstantConfig.HEALTHY_MONITOR_DELAY, GlobalConstantConfig.HEALTHY_MONITOR_TIME_UNIT);
    }
}
