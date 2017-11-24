package com.xlibao.io.service.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2017/8/12.
 */
public class AbstractChannelInitializer extends ChannelInitializer<Channel> {

    private MessageEventListener messageEventListener;
    private NettyConfig config;

    AbstractChannelInitializer(MessageEventListener messageEventListener, NettyConfig config) {
        this.messageEventListener = messageEventListener;
        this.config = config;
    }

    @Override
    protected void initChannel(Channel socketChannel) throws Exception {
        AbstractChannelInboundHandlerAdapter channelInboundHandlerAdapter = new AbstractChannelInboundHandlerAdapter();
        channelInboundHandlerAdapter.registerMessageEventListener(messageEventListener);

        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // 设置解码器、编码器
        channelPipeline.addLast(messageEventListener.newDecoder()).addLast(messageEventListener.newEncoder());
        // 设置超时处理(空闲)
        channelPipeline.addLast(new IdleStateHandler(config.getReadTimeout(), config.getWriteTimeout(), config.getBothTimeout(), TimeUnit.SECONDS));
        // 设置适配器
        channelPipeline.addLast(channelInboundHandlerAdapter);
    }
}