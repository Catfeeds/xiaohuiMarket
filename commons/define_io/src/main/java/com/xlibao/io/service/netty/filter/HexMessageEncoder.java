package com.xlibao.io.service.netty.filter;

import com.xlibao.common.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author chinahuangxc on 2017/8/12.
 */
public class HexMessageEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext context, String message, List<Object> out) throws Exception {
        ByteBuf byteBuf = context.alloc().ioBuffer();

        byteBuf.writeBytes(CommonUtils.encodeHexString(message));
        // 添加进去
        out.add(byteBuf);
    }
}
