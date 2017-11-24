package com.xlibao.io.service.mina.filter;

import com.xlibao.io.entry.MessageInputStream;
import com.xlibao.io.entry.impl.MessageInputStreamImpl;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.util.concurrent.atomic.AtomicLong;

class MessageDecoder extends CumulativeProtocolDecoder {

    private static final String FAIL_REQUEST_COUNT = "failRequestCount";
    private static final int FAIL_REQUEST_MAX_TIMES = 10000;

    MessageDecoder() {
    }

    protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput decoder) throws Exception {
        AtomicLong value = (AtomicLong) session.getAttribute(FAIL_REQUEST_COUNT);
        // 注意：这里不需要考虑多线程的情况 每个会话都是单线程模式
        if (value == null) {
            value = new AtomicLong();
            session.setAttribute(FAIL_REQUEST_COUNT, value);
        }
        // 自增 表示获取该条消息的次数
        value.incrementAndGet();
        // 消息剩余内容的长度
        int remaining = buffer.remaining();
        // 小于4时无法表示消息的长度
        if (remaining < 4) {
            // 这里可能是数据未读取完整 返回false由Mina继续对消息进行填充 但一般不会发生 这里其实可以认为是保护机制
            // 这里可能会出现死循环 所以必须加限制 当超过指定次数仍无法获取到完整消息 直接丢弃该消息
            if (value.get() > FAIL_REQUEST_MAX_TIMES) {
                buffer.position(buffer.position() + remaining);
                session.close(true);
                return true;
            }
            return false;
        }
        // 当前buffer中的指针所在位置，其实就是消息流中byte数组的下标位置；
        // 这里的作用是：当消息未填充完整时必须返回给mina继续进行数据填充 而位置因为读取了size后发生了变化 所以必须在返回之前将位置还原
        int pos = buffer.position();
        // 获取消息长度(包括消息头)
        int size = buffer.getInt();
        // 当消息长度小于7时 该消息无法构建(消息头长度为7) 继续返回读取数据
        if (size < 7) {
            if (value.get() > FAIL_REQUEST_MAX_TIMES) {
                buffer.position(buffer.position() + remaining);
                session.close(true);
                return true;
            }
            buffer.position(pos);
            return false;
        }
        // 消息的剩余数据是否足够构建一条消息 一条消息至少的容量为：消息实体内容(可能只是消息头)+表示消息长度的空间，即size的占用空间(int -- 4位)
        if (remaining < size + 4) {
            if (value.get() > FAIL_REQUEST_MAX_TIMES) {
                buffer.position(pos + remaining);
                session.close(true);
                return true;
            }
            buffer.position(pos);
            return false;
        }
        try {
            // 具体应用逻辑上的内容 表示消息类型
            byte msgType = buffer.get();
            // 具体应用逻辑上的内容 表示消息ID
            short msgId = buffer.getShort();
            // 具体应用逻辑上的内容 表示消息序列
            int msgSequence = buffer.getInt();
            int dataSize = size - 7;

            byte[] data = new byte[dataSize];
            // 通过buffer填充数据
            buffer.get(data);

            MessageInputStream message = new MessageInputStreamImpl(msgType, msgId, msgSequence, data);
            decoder.write(message);
            // 消息读取成功 将读取次数设置为0
            value.set(0L);
        } catch (Throwable cause) {
            cause.printStackTrace();
            session.close(true);
        }
        return true;
    }
}