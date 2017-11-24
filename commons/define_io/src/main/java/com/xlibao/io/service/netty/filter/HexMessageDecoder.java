package com.xlibao.io.service.netty.filter;

import com.xlibao.common.CommonUtils;
import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.impl.MessageInputStreamImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author chinahuangxc on 2017/8/12.
 */
public class HexMessageDecoder extends ByteToMessageDecoder {

    // 结束符
    private String END_CHARACTER = "\r\n";

    public HexMessageDecoder(String endCharacter) {
        END_CHARACTER = endCharacter;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> messages) throws Exception {
        int length = byteBuf.readableBytes();
        // 如果大于4位
        int position = byteBuf.readerIndex();
        byte[] bytes = new byte[length];

        byteBuf.readBytes(bytes);

        String parameter = CommonUtils.decodeHexBytes(bytes);

        if (!parameter.contains(END_CHARACTER)) { // 不包含结束符
            byteBuf.readerIndex(position);
            return;
        }
        int index = parameter.lastIndexOf(END_CHARACTER);

        parameter = parameter.substring(0, index + 4);
        int currentIndex = CommonUtils.encodeHexString(parameter).length;
        if (currentIndex < bytes.length) {
            // 设置位置
            byteBuf.readerIndex(currentIndex);
        }
        String[] values = parameter.split(END_CHARACTER);
        for (String value : values) {
            MessageInputStream messageInputStream = new MessageInputStreamImpl(value.getBytes());
            messages.add(messageInputStream);
        }
    }
}