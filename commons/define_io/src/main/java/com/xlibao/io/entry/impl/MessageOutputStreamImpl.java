package com.xlibao.io.entry.impl;

import com.xlibao.io.entry.ByteOutputStream;
import com.xlibao.io.entry.ByteUtil;
import com.xlibao.io.entry.MessageOutputStream;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public class MessageOutputStreamImpl implements MessageOutputStream {

    private static final int DEFAULT_MSG_SIZE = 1024;

    private ByteOutputStream bos = null;

    private int msgType;
    private short msgId;
    private int msgSequence;

    /**
     * 初始化消息输出流
     *
     * @param msgType 消息类型
     * @param msgId 消息ID
     */
    public MessageOutputStreamImpl(int msgType, short msgId) {
        this(msgType, msgId, 0, DEFAULT_MSG_SIZE);
    }

    /**
     * 初始化消息输出流
     *
     * @param msgType 消息类型
     * @param msgId 消息ID
     * @param msgSequence 消息序号
     */
    public MessageOutputStreamImpl(int msgType, short msgId, int msgSequence) {
        this(msgType, msgId, msgSequence, DEFAULT_MSG_SIZE);
    }

    /**
     * 初始化消息输出流
     *
     * @param msgType 消息类型
     * @param msgId 消息ID
     * @param msgSequence 消息序号
     * @param msgSize 消息大小(流量)
     */
    public MessageOutputStreamImpl(int msgType, short msgId, int msgSequence, int msgSize) {
        this.msgType = msgType;
        this.msgId = msgId;
        this.msgSequence = msgSequence;

        bos = new ByteOutputStreamImpl(msgSize);

        // 预留的空间 用于表示消息的长度 toBytes方法中将重设该空间的数据
        bos.writeInt(0);
        bos.writeByte(msgType);
        bos.writeShort(msgId);
        bos.writeInt(msgSequence);
    }

    @Override
    public byte getMsgType() {
        return (byte) msgType;
    }

    @Override
    public short getMsgId() {
        return msgId;
    }

    @Override
    public int getMsgSequence() {
        return msgSequence;
    }

    @Override
    public void setPrintln(boolean println) {
        bos.setPrintln(println);
    }

    @Override
    public void writeByte(int v) {
        bos.writeByte(v);
    }

    @Override
    public void writeShort(int v) {
        bos.writeShort(v);
    }

    @Override
    public void writeInt(int v) {
        bos.writeInt(v);
    }

    @Override
    public void writeLong(long v) {
        bos.writeLong(v);
    }

    @Override
    public void writeFloat(float v) {
        bos.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) {
        bos.writeDouble(v);
    }

    @Override
    public void writeBoolean(boolean v) {
        bos.writeBoolean(v);
    }

    @Override
    public void writeChar(char v) {
        bos.writeChar(v);
    }

    @Override
    public void writeUTF(String v) {
        bos.writeUTF(v);
    }

    @Override
    public void writeBytes(byte[] v) {
        bos.writeBytes(v);
    }

    @Override
    public void writeBytes(byte[] v, int source, int length) {
        bos.writeBytes(v, source, length);
    }

    @Override
    public byte[] toBytes() {
        byte[] data = this.bos.toBytes();
        // 表示长度的消息内容不需要进行消息大小计算 消息长度的空间用int来存放(即4个字节空间)
        int totalMsgSize = data.length - 4;
        // ///////////////////////// -- 重设消息长度 -- 开始 ///////////////////////////
        // data[0] = ((byte) (totalMsgSize >>> 24 & 0xFF));
        // data[1] = ((byte) (totalMsgSize >>> 16 & 0xFF));
        // data[2] = ((byte) (totalMsgSize >>> 8 & 0xFF));
        // data[3] = ((byte) (totalMsgSize >>> 0 & 0xFF));
        ByteUtil.writeInt(data, 0, totalMsgSize);
        // ///////////////////////// -- 重设消息长度 -- 结束 ///////////////////////////
        return data;
    }

    @Override
    public byte[] toBytes(int v) {
        return bos.toBytes(v);
    }

    @Override
    public void reset() {
        bos.reset();
    }

    @Override
    public void release() {
        bos.release();
        bos = null;
    }

    @Override
    public int getBodyDataSize() {
        return bos.getBodyDataSize();
    }

    @Override
    public int getIndex() {
        return bos.getIndex();
    }

    @Override
    public void setIndex(int index) {
        bos.setIndex(index);
    }
}