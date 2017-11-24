package com.xlibao.io.service.mina;

import com.xlibao.io.entry.MessageInputStream;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理器
 *
 * @author huangxc
 */
public abstract class AbstractMessageHandler extends IoHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        logger.info(session.toString());
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        logger.info(session.toString());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        sessionClose(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
        if (status != IdleStatus.BOTH_IDLE) {
            return;
        }
        // 消息空闲 表示完全无读写操作 为了避免误杀 一般需要启动心跳协议
        int idleCount = session.getBothIdleCount();
        sessionIdle(session, idleCount);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);

        if (!(message instanceof MessageInputStream)) {
            // TODO 错误的消息体
            return;
        }
        MessageInputStream mis = (MessageInputStream) message;

        messageReceived(session, mis);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        notifyExceptionCaught(session, cause);
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
    }
    
    public abstract void notifyExceptionCaught(IoSession session, Throwable cause) ;

    public abstract void messageReceived(IoSession session, MessageInputStream message) throws Exception;

    public abstract void sessionIdle(IoSession session, int idleCount) throws Exception;

    public abstract void sessionClose(IoSession session) throws Exception;
}