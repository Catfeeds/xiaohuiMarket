package com.xlibao.common.thread;

import com.xlibao.common.DefineThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class AsyncScheduledService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncScheduledService.class);

    // 数据库写操作专用调度任务 -- 线程池
    private static final ScheduledThreadPoolExecutor databaseThreadPoolExecutor = new ScheduledThreadPoolExecutor(16, new DefineThreadFactory("DatabasePool"), new ThreadPoolExecutor.CallerRunsPolicy());
    // 远程调用操作专用调度任务 -- 线程池
    private static final ScheduledThreadPoolExecutor remoteThreadPoolExecutor = new ScheduledThreadPoolExecutor(16, new DefineThreadFactory("DatabasePool"), new ThreadPoolExecutor.CallerRunsPolicy());
    // 缓存操作专用调度任务 -- 线程池
    private static final ScheduledThreadPoolExecutor cacheThreadPoolExecutor = new ScheduledThreadPoolExecutor(16, new DefineThreadFactory("cachePool"), new ThreadPoolExecutor.CallerRunsPolicy());
    // 公用的任务调度处理器 -- 线程池
    private static final ScheduledThreadPoolExecutor commonThreadPoolExecutor = new ScheduledThreadPoolExecutor(16, new DefineThreadFactory("CommonPool"), new ThreadPoolExecutor.CallerRunsPolicy());
    // 线程池 主要用于执行需要等待结果的任务
    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(16, 32, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new DefineThreadFactory("FutureTaskPool"), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void submitImmediateSaveTask(Runnable command) {
        submitDataSaveTask(command, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * <pre>
     *     数据库数据保存的专用调度任务池 -- 延迟执行
     *     <b>注意：</b>严禁重复执行同一个任务，逻辑使用时绝对不允许在任务执行方法中发起任务的重新加载
     * </pre>
     *
     * @param command
     * @param delay
     * @param timeUnit
     */
    public static void submitDataSaveTask(Runnable command, long delay, TimeUnit timeUnit) {
        databaseThreadPoolExecutor.schedule(command, delay, timeUnit);
    }

    /**
     * <pre>
     *     远程通知的专用调度任务池 -- 即时执行
     *     <b>注意：</b>严禁重复执行同一个任务，逻辑使用时绝对不允许在任务执行方法中发起任务的重新加载
     * </pre>
     */
    public static void submitImmediateRemoteNotifyTask(Runnable command) {
        submitRemoteNotifyTask(command, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * <pre>
     *     远程通知的专用调度任务池 -- 延迟执行
     *     <b>注意：</b>严禁重复执行同一个任务，逻辑使用时绝对不允许在任务执行方法中发起任务的重新加载
     * </pre>
     */
    public static void submitRemoteNotifyTask(Runnable command, long delay, TimeUnit timeUnit) {
        remoteThreadPoolExecutor.schedule(command, delay, timeUnit);
    }

    public static void submitImmediateCommonTask(Runnable command) {
        submitCommonTask(command, 0, TimeUnit.MILLISECONDS);
    }

    public static void submitCommonTask(Runnable command, long delay, TimeUnit timeUnit) {
        commonThreadPoolExecutor.schedule(command, delay, timeUnit);
    }

    public static void submitDelayCacheTask(Callable<Boolean> callable, long delay, TimeUnit timeUnit) {
        cacheThreadPoolExecutor.schedule(callable, delay, timeUnit);
    }

    public static boolean waitFutureCacheTask(Callable<Boolean> callable) {
        Boolean b = defineFutureCacheTask(callable);
        return b == null ? false : b;
    }

    public static <T> T defineFutureCacheTask(Callable<T> callable) {
        Future<T> future = cacheThreadPoolExecutor.submit(callable);
        try {
            return future.get();
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        return null;
    }

    public static <T> Future<T> executorFutureTask(Callable<T> callable) {
        return EXECUTOR_SERVICE.submit(callable);
    }

    public static <T> T executorCallable(Callable<T> callable) {
        try {
            return executorFutureTask(callable).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getDatabaseScheduledActiveTaskCount() {
        return databaseThreadPoolExecutor.getActiveCount();
    }

    public static long getDatabaseScheduledCompletedTaskCount() {
        return databaseThreadPoolExecutor.getCompletedTaskCount();
    }

    public static long getDatabaseScheduledTaskCount() {
        return databaseThreadPoolExecutor.getTaskCount();
    }

    public static int getCacheScheduledActiveTaskCount() {
        return cacheThreadPoolExecutor.getActiveCount();
    }

    public static long getCacheScheduledCompletedTaskCount() {
        return cacheThreadPoolExecutor.getCompletedTaskCount();
    }

    public static long getCacheScheduledTaskCount() {
        return cacheThreadPoolExecutor.getTaskCount();
    }

    public static int getCommonScheduledActiveTaskCount() {
        return cacheThreadPoolExecutor.getActiveCount();
    }

    public static long getCommonScheduledCompletedTaskCount() {
        return cacheThreadPoolExecutor.getCompletedTaskCount();
    }

    public static long getCommonScheduledTaskCount() {
        return cacheThreadPoolExecutor.getTaskCount();
    }

    /**
     * <pre>
     *     停服时调用，主要目的用于执行所有未完成的操作；更多时候仅针对数据库的保存操作，或远程的通知任务；本地的缓存与其他操作可忽略
     * </pre>
     */
    public static void destory() {
        BlockingQueue<Runnable> databaseRunnables = databaseThreadPoolExecutor.getQueue();
        logger.info("等待完成的数据库任务数量：" + databaseRunnables.size());
        databaseRunnables.forEach(Runnable::run);
        databaseRunnables.clear();
        databaseThreadPoolExecutor.shutdown();

        BlockingQueue<Runnable> remoteRunnables = remoteThreadPoolExecutor.getQueue();
        logger.info("等待完成的远程调用任务数量：" + remoteRunnables.size());
        remoteRunnables.forEach(Runnable::run);
        remoteRunnables.clear();
        remoteThreadPoolExecutor.shutdown();
    }
}