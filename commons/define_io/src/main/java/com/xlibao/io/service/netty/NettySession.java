package com.xlibao.io.service.netty;

import com.xlibao.common.CommonUtils;
import com.xlibao.io.entry.MessageOutputStream;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NettySession {

    private Channel channel;
    private String id;

    private Map<String, Object> attributes = new HashMap<>();

    public NettySession() {
    }

    public void init(Channel channel) {
        this.channel = channel;
        this.id = channel.id().asShortText();
    }

    public String getId() {
        return id;
    }

    public String getIP() {
        return ((InetSocketAddress) channel.remoteAddress()).getHostName();
    }

    public int getPort() {
        return ((InetSocketAddress) channel.remoteAddress()).getPort();
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public String netTrack() {
        return "会话ID：" + getId() + " 地址：" + getIP() + " 链接端口：" + getPort();
    }

    public boolean send(MessageOutputStream message) {
        if (message != null && isActive()) {
            try {
                channel.writeAndFlush(message).sync();
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isActive() {
        return channel != null && channel.isActive();
    }

    public void send(String message) {
        if (CommonUtils.isNotNullString(message)) {
            channel.writeAndFlush(message);
        }
    }

    public void close() throws Exception {
        channel.close().sync();
    }
}