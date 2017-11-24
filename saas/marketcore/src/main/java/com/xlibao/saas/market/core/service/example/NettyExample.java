package com.xlibao.saas.market.core.service.example;

import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.service.netty.MessageEventListener;
import com.xlibao.io.service.netty.NettyClient;
import com.xlibao.io.service.netty.NettyConfig;
import com.xlibao.io.service.netty.NettySession;
import com.xlibao.io.service.netty.filter.HexMessageDecoder;
import com.xlibao.io.service.netty.filter.HexMessageEncoder;
import com.xlibao.market.protocol.HardwareMessageType;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/8/30.
 */
public class NettyExample {

    public static void main(String[] args) throws Exception {
        connector();
    }

    private static boolean connector() {
        NettyConfig nettyConfig = new NettyConfig();
        nettyConfig.setReadTimeout(10);
        nettyConfig.setWriteTimeout(10);
        nettyConfig.setBothTimeout(15);

        try {
            NettyClient.getInstance().start("192.168.1.100", 9529, new MessageEventListenerDemo());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
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
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        @Override
        public void notifyMessageReceived(NettySession session, MessageInputStream message) throws Exception {
            byte[] bytes = new byte[message.getBodyDataSize()];
            message.readBytes(bytes);
            String parameters = new String(bytes);
        }

        @Override
        public void notifySessionClosed(NettySession session) {
            reconnector();
        }

        @Override
        public void notifyExceptionCaught(Throwable cause) {
        }

        @Override
        public void notifySessionIdle(NettySession session, int idleType, int idleTimes) {
        }

        private void reconnector() {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    if (!connector()) {
                        System.out.println("链接失败，1分钟后重试");
                        AsyncScheduledService.submitCommonTask(this, 10, TimeUnit.SECONDS);
                    }
                }
            };
            AsyncScheduledService.submitImmediateCommonTask(runnable);
        }
    }
}
