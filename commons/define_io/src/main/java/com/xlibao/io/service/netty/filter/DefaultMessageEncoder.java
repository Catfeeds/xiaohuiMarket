package com.xlibao.io.service.netty.filter;

import com.xlibao.io.entry.MessageOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author chinahuangxc on 2017/8/8.
 */
public class DefaultMessageEncoder extends MessageToMessageEncoder<MessageOutputStream> {

    @Override
    protected void encode(ChannelHandlerContext context, MessageOutputStream message, List<Object> out) throws Exception {
        ByteBuf byteBuf = context.alloc().ioBuffer();

        byteBuf.writeBytes(message.toBytes());
        // 添加进去
        out.add(byteBuf);
    }
}