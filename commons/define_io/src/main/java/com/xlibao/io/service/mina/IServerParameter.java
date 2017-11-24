package com.xlibao.io.service.mina;

/**
 * @author huangxc
 */
public interface IServerParameter {

    /**
     * 服务器地址
     */
    String getIp();

    /**
     * 服务器监听端口
     */
    int getPort();

    /**
     * 允许空闲的周期(单个)
     */
    int getAllowedIdelTime();

    /**
     * <pre>
     * 获取消息处理线程数
     *
     * 一般为：
     * Runtime.getRuntime().availableProcessors() - 1; // 处理器-1
     * </pre>
     */
    int getIoThreadSize();

    /**
     * 消息处理核心线程数
     */
    int getMsgHandlerCoreThreadSize();

    /**
     * 消息处理最大线程数
     */
    int getMsgHandlerMaxThreadSize();

    /**
     * 消息线程保持时间
     */
    int getMsgHandlerKeepAliveTime();

    /**
     * 消息处理器
     */
    AbstractMessageHandler getIoHandler();

    /**
     * 最大连接数
     */
    int getConnectorSize();
}
