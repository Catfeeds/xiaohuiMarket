package com.xlibao.io.service.mina.filter;

import com.xlibao.io.entry.MessageOutputStream;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

class MessageEncoder extends ProtocolEncoderAdapter {

    MessageEncoder() {
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput encoder) throws Exception {
        if (!(message instanceof MessageOutputStream)) {
            return;
        }
        MessageOutputStream mos = (MessageOutputStream) message;

        byte[] data = mos.toBytes();

        IoBuffer buffer = IoBuffer.wrap(data);
        buffer.position(0);
        encoder.write(buffer);
    }
}