package com.xlibao.io.service.netty.filter;

import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.impl.MessageInputStreamImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author chinahuangxc on 2017/8/8.
 */
public class DefaultMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        if (length < 4) { // 小于4位时无法成为一个完整的消息
            return;
        }
        int index = in.readerIndex();
        int size = in.readInt();
        if (length < size) {
            in.readerIndex(index);
            return;
        }
        try {
            // 具体应用逻辑上的内容 表示消息类型
            byte msgType = in.readByte();
            // 具体应用逻辑上的内容 表示消息ID
            short msgId = in.readShort();
            // 具体应用逻辑上的内容 表示消息序列
            int msgSequence = in.readInt();
            // 消息大小
            int dataSize = size - 7;

            byte[] data = new byte[dataSize];
            // 通过buffer填充数据
            in.readBytes(data);

            MessageInputStream message = new MessageInputStreamImpl(msgType, msgId, msgSequence, data);
            out.add(message);
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
    }
}