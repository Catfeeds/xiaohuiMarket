package com.xlibao.io.entry;

import com.xlibao.io.entry.impl.MessageOutputStreamImpl;

public class MessageFactory {

    /** 内部消息类型 */
    public static final byte MSG_TYPE_INTERNAL = 0;
    /** 平台消息类型 */
    public static final byte MSG_TYPE_PLATFORM = 2;
    /** 逻辑消息类型 */
    public static final byte MSG_TYPE_LOGIC = 4;

    public static final short MSG_ID_HEARTBEAT = 88;

    public static MessageOutputStream createLogicMessage(short msgId) {
        return new MessageOutputStreamImpl(MSG_TYPE_LOGIC, msgId, 0);
    }

    public static MessageOutputStream createInternalMessage(short msgId) {
        return new MessageOutputStreamImpl(MSG_TYPE_INTERNAL, msgId, 0);
    }

    public static MessageOutputStream createPlatformMessage(short msgId) {
        return new MessageOutputStreamImpl(MSG_TYPE_PLATFORM, msgId, 0);
    }

    public static MessageOutputStream createMessageOutputStream(byte msgType, short msgId) {
        return new MessageOutputStreamImpl(msgType, msgId, 0);
    }

    public static MessageOutputStream createMessageOutputStream(byte msgType, short msgId, int msgSequence) {
        return new MessageOutputStreamImpl(msgType, msgId, msgSequence);
    }

    public static MessageOutputStream createResponseMessage(MessageInputStream requestMessage) {
        return createMessageOutputStream(requestMessage.getMsgType(), requestMessage.getMsgId(), requestMessage.getMsgSequence());
    }
}