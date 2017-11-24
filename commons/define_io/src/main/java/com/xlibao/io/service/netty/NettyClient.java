package com.xlibao.io.service.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup group;
    private NettyConfig config;

    private static final NettyClient instance = new NettyClient();

    private NettyClient() {
    }

    public static NettyClient getInstance() {
        return instance;
    }

    /**
     * <pre>
     *      <b>初始化配置(超时)</b> 不提供时，按默认的配置
     *      默认值为：<b>读、写超时</b>：10s
     *                <b>读写超时</b>：15s
     * </pre>
     *
     * @param config 超时配置
     */
    public void init(NettyConfig config) {
        this.config = config;
    }

    public void start(String ip, int port, MessageEventListener messageEventListener) throws Exception {
        if (config == null) {
            config = new NettyConfig();
        }
        AbstractChannelInitializer channelInitializer = new AbstractChannelInitializer(messageEventListener, config);

        group = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();

        boot.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(channelInitializer);
        //进行连接操作
        ChannelFuture future = boot.connect(ip, port).sync();
        logger.info("=========================> 链接服务器成功(" + future + ")，服务器地址：" + ip + "，监听端口：" + port + " <=========================");
    }

    public boolean isConnect() {
        return !group.isShutdown();
    }

    public void shutdown() throws Exception {
        group.shutdownGracefully().sync();
    }
}