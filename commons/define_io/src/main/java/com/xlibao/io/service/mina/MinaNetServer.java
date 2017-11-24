package com.xlibao.io.service.mina;

import com.xlibao.common.DefineThreadFactory;
import com.xlibao.io.service.mina.filter.MessageProtocolCodecFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class MinaNetServer {

    private static final Logger logger = LoggerFactory.getLogger(MinaNetServer.class);

    // 单例模式
    private static final MinaNetServer INSTANCE = new MinaNetServer();
    /**
     * 监听器
     */
    private NioSocketAcceptor acceptor;
    private static final int MSG_BASIC_SIZE = 1024;

    private MinaNetServer() {
    }

    public static MinaNetServer getInstance() {
        return INSTANCE;
    }

    public void startService(IServerParameter param) {
        try {
            /**
             * MINA默认是使用DirectMemory实现ByteBuffer池的方案（以下简称direct buffer），
             * 通过JNI在内存开辟一段空间来使用，该方案在早期的MINA版本中是一个非常好的特性。
             * 那是因为MINA开发初期，JVM并没有现在的强大，带有池效果的direct buffer性能比较好。 但是当我们使用-Xms
             * -Xmx等指令增加JVM可使用的内存，那仅仅增加了堆的内存空间，
             * 而DirectMemory的空间并没有增加，导致MINA实际使用的时候经常出现OutOfMemoryError。
             * 如果你的确想使用DirectMemory ，可以通过-XX:MaxDirectMemorySize选项来设置。
             * 不过不建议这样做，因为最新的测试表明，在现代的JVM里面，DirectMemory比堆的表现更差。
             * 这里可能会觉得奇怪，为什么不用池，而要用堆呢，而且还需要GC。 那是因为现在的JVM GC能力已经很强了，
             * 而且在并发环境里面，pool的同步也是一个性能的问题。
             *
             * 我们可以通过这样的代码进行设置
             */
            IoBuffer.setUseDirectBuffer(false);
            IoBuffer.setAllocator(new SimpleBufferAllocator());

            // 设置默认的消息处理线程数
            acceptor = new NioSocketAcceptor(param.getIoThreadSize());
            // 设置读取数据的缓冲区默认大小
            acceptor.getSessionConfig().setReadBufferSize(MSG_BASIC_SIZE);
            // 设置读取数据的最大缓冲区大小
            acceptor.getSessionConfig().setMaxReadBufferSize(MSG_BASIC_SIZE * 1024);
            // 读写通道均在60 秒内无任何操作就进入空闲状态
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, param.getAllowedIdelTime());
            // 数据的编码（write方向）与解码（read方向）等功能，其中数据的encode与decode是最为重要的、也是在使用Mina时最主要关注的地方
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory()));

            ExecutorFilter executorFilter = new ExecutorFilter(param.getMsgHandlerCoreThreadSize(), param.getMsgHandlerMaxThreadSize(), param.getMsgHandlerKeepAliveTime(), TimeUnit.SECONDS, new DefineThreadFactory("message-nio"));
            // 加入一个线程池到适配器，这里用的是jdk自带的线程池
            acceptor.getFilterChain().addLast("thread", executorFilter);
            // 这个接口负责编写业务逻辑，也就是接收、发送数据的地方。
            acceptor.setHandler(param.getIoHandler());
            // 打印结果检查是否可重用地址
            System.out.println("Before reuse address " + getReuseAddress());
            // 设置的是主服务监听的端口可以重用
            acceptor.setReuseAddress(true);
            // 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出；即时响应
            acceptor.getSessionConfig().setTcpNoDelay(true);
            // 设置主服务监听端口的监听队列的最大值为5000，如果当前已经有5000个连接，再新的连接来将被服务器拒绝
            // acceptor.setBacklog(param.getConnectorSize());
            acceptor.bind(new InetSocketAddress(param.getIp(), param.getPort()));
            System.out.println("After reuse address  " + getReuseAddress());
            System.out.println("服务器启动成功，IP：" + param.getIp() + "，监听端口：" + param.getPort());
        } catch (Exception ex) {
            System.err.println("启动服务器失败，请检查配置！参考信息：" + ex.getMessage());
            System.exit(0);
        }
    }

    // 当前的会话总数(可理解为总连接数)
    public int getSessions() {
        return acceptor.getManagedSessionCount();
    }

    public boolean getReuseAddress() {
        return acceptor.isReuseAddress();
    }

    public void shutdown() {
        acceptor.unbind(); // 停掉网络层
        acceptor.dispose();
    }
}