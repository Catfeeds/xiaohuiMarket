package com.xlibao.io.service.netty;

import com.xlibao.io.entry.MessageInputStream;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author chinahuangxc on 2017/8/8.
 */
public interface MessageEventListener {

    ByteToMessageDecoder newDecoder();

    MessageToMessageEncoder newEncoder();

    void notifyChannelActive(NettySession session) throws Exception;

    void notifyMessageReceived(NettySession session, MessageInputStream message) throws Exception;

    void notifySessionClosed(NettySession session);

    void notifyExceptionCaught(Throwable cause);

    void notifySessionIdle(NettySession session, int idleType, int idleTimes);
}