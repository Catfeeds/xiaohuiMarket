package com.xlibao.io.service.mina.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageProtocolCodecFactory implements ProtocolCodecFactory {

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return new MessageDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return new MessageEncoder();
    }
}