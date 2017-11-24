package com.xlibao.io.service.mina;

import com.xlibao.io.service.mina.filter.MessageProtocolCodecFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class MinaNetClient {

    private static final int MSG_BASIC_SIZE = 1024;
    private static IoSession session = null;

    public boolean connectServer(IServerParameter param, boolean startHeartbeat) {
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        // 创建一个TCP/IP 连接
        NioSocketConnector connector = new NioSocketConnector();
        // 设置读取数据的缓冲区默认大小
        connector.getSessionConfig().setReadBufferSize(MSG_BASIC_SIZE);
        // 设置读取数据的最大缓冲区大小
        connector.getSessionConfig().setMaxReadBufferSize(MSG_BASIC_SIZE * 1024);
        // 读写通道均在60 秒内无任何操作就进入空闲状态
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, param.getAllowedIdelTime());
        // 数据的编码（write方向）与解码（read方向）等功能，其中数据的encode与decode是最为重要的、也是在使用Mina时最主要关注的地方
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory()));
        // 设定服务器端的消息处理器:一个 SamplMinaServerHandler 对象,
        connector.setHandler(param.getIoHandler());
        // 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出；即时响应
        connector.getSessionConfig().setTcpNoDelay(true);
        // 设置连接超时时间
        connector.setConnectTimeoutCheckInterval(param.getMsgHandlerKeepAliveTime());
        // 连结到服务器
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(param.getIp(), param.getPort()));
        // 等待连接完成 相当与FutureTask中的get操作
        if (!connectFuture.awaitUninterruptibly(120, TimeUnit.SECONDS)) {
            return false;
        }
        session = connectFuture.getSession();
        if (startHeartbeat) {
            // 是否启动心跳机制
            new Heartbeat().start();
        }
        return true;
    }

    public IoSession getSession() {
        return session;
    }

    private class Heartbeat extends Thread {

        @Override
        public void run() {
            while (true) {
            }
        }
    }
}
