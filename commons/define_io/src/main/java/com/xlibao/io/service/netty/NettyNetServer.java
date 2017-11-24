package com.xlibao.io.service.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *     <b>Netty网络服务端</b>
 *     <b>注意：</b>必须在启动服务器时启动服务；若存在于web服务中，请在初始化servlet时进行初始化
 *
 *     <b>Demo</b>
 *          NettyConfig nettyConfig = new NettyConfig();
 *          nettyConfig.setReadOutTime(10);
 *          nettyConfig.setWriteOutTime(10);
 *          nettyConfig.setBothOutTime(15);
 *
 *          NettyNetServer.getInstance().init(nettyConfig);
 *          // 其中 ----
 *          // <b>9527</b>为监听端口，可自定义；
 *          // <b>MessageEventListener</b>为具体的业务监听器
 *          NettyNetServer.getInstance().start(9527, new MessageEventListenerImpl());
 * </pre>
 */
public class NettyNetServer {

    private static Logger logger = LoggerFactory.getLogger(NettyNetServer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private NettyConfig config;

    // 单例
    private static final NettyNetServer instance = new NettyNetServer();

    private NettyNetServer() {
    }

    public static NettyNetServer getInstance() {
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

    /**
     * <pre>
     *     <b>启动监听服务</b>
     * </pre>
     *
     * @param port                 监听端口
     * @param messageEventListener 消息动作监听器
     * @throws Exception 异常
     */
    public void start(int port, MessageEventListener messageEventListener) throws Exception {
        if (config == null) { // 保证NettyConfig不为null
            config = new NettyConfig();
        }
        AbstractChannelInitializer channelInitializer = new AbstractChannelInitializer(messageEventListener, config);

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).localAddress(port).childHandler(channelInitializer);

        ChannelFuture future = serverBootstrap.bind(port).sync();
        logger.info("-----------------------------> 服务器启动成功(" + future + ")，当前监听端口：" + port + " <-----------------------------");
    }

    /**
     * <pre>
     *     <b>关闭端口监听器</b>
     *     <b>注意：</b>该方法仅能在停服时使用
     * </pre>
     */
    public void shutdown() {
        try {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            bossGroup = null;
            workerGroup = null;
        }
    }
}