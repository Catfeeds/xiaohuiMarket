package com.xlibao.saas.market.shop.service.message.example;

import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.service.netty.MessageEventListener;
import com.xlibao.io.service.netty.NettyConfig;
import com.xlibao.io.service.netty.NettyNetServer;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.io.service.netty.filter.HexMessageDecoder;
import com.xlibao.io.service.netty.filter.HexMessageEncoder;
import com.xlibao.market.protocol.HardwareMessageType;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author chinahuangxc on 2017/8/8.
 */
public class NettyExample {

    public static void main(String[] args) throws Exception {
        NettyConfig nettyConfig = new NettyConfig();
        nettyConfig.setReadTimeout(10);
        nettyConfig.setWriteTimeout(10);
        nettyConfig.setBothTimeout(15);

        NettyNetServer.getInstance().init(nettyConfig);
        NettyNetServer.getInstance().start(9527, new MessageEventListenerDemo());
    }

    private static class MessageEventListenerDemo implements MessageEventListener {

        @Override
        public ByteToMessageDecoder newDecoder() {
            return new HexMessageDecoder(HardwareMessageType.HARDWARE_MSG_END);
        }

        @Override
        public MessageToMessageEncoder newEncoder() {
            return new HexMessageEncoder();
        }

        @Override
        public void notifyChannelActive(NettySession session) throws Exception {
        }

        @Override
        public void notifyMessageReceived(NettySession session, MessageInputStream message) throws Exception {
            byte[] bytes = new byte[message.getBodyDataSize()];
            message.readBytes(bytes);
            String parameters = new String(bytes);
        }

        @Override
        public void notifySessionClosed(NettySession session) {
        }

        @Override
        public void notifyExceptionCaught(Throwable cause) {
        }

        @Override
        public void notifySessionIdle(NettySession session, int idleType, int idleTimes) {
        }
    }
}