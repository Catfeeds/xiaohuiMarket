package com.xlibao.io.entry;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface MessageInputStream extends ByteInputStream {

    /**
     * <pre>
     * <b>消息类型</b>
     * 可用于区分该消息的来源，如：内部管理系统消息、用户使用的产品消息、登录前的消息(未与用户建立关系)、登录后的消息(可与用户建立关系)
     * </pre>
     *
     * @return byte
     */
    byte getMsgType();

    /**
     * <pre>
     * <b>消息ID</b>
     * 主要用于处理消息分发，即：每条消息可独立编写处理逻辑
     * </pre>
     *
     * @return short
     */
    short getMsgId();

    /**
     * <pre>
     * <b>消息序列</b>
     * 这里主要由客户端生成，服务器处理时对此值不做任何修改，返回消息时将该值原样发送回客户端(可用于处理消息等待的情况，如：关闭对应的消息等待窗口)
     * </pre>
     *
     * @return int
     */
    int getMsgSequence();
}
