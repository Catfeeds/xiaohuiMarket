package com.xlibao.io.entry.impl;

import com.xlibao.io.entry.ByteInputStream;
import com.xlibao.io.entry.MessageInputStream;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public class MessageInputStreamImpl implements MessageInputStream {

    private int type;
    private short id;
    private int msgSequence;
    private ByteInputStream bis = null;

    public MessageInputStreamImpl(byte[] data) {
        bis = new ByteInputStreamImpl(data);
    }

    /**
     * 由通讯解码器填充的消息输入流
     *
     * @param type        消息类型
     * @param id          消息ID
     * @param msgSequence 消息序号
     * @param data        消息内容
     */
    public MessageInputStreamImpl(int type, int id, int msgSequence, byte[] data) {
        this.type = type;
        this.id = ((short) id);
        this.msgSequence = msgSequence;
        bis = new ByteInputStreamImpl(data);
    }

    @Override
    public byte getMsgType() {
        return (byte) type;
    }

    @Override
    public short getMsgId() {
        return id;
    }

    @Override
    public int getMsgSequence() {
        return msgSequence;
    }

    @Override
    public byte readByte() {
        return bis.readByte();
    }

    @Override
    public short readShort() {
        return bis.readShort();
    }

    @Override
    public int readInt() {
        return bis.readInt();
    }

    @Override
    public long readLong() {
        return bis.readLong();
    }

    @Override
    public boolean readBoolean() {
        return bis.readBoolean();
    }

    @Override
    public char readChar() {
        return bis.readChar();
    }

    @Override
    public String readUTF() {
        return bis.readUTF();
    }

    @Override
    public void readBytes(byte[] target) {
        bis.readBytes(target);
    }

    public void readBytes(byte[] bytes, int startIndex, int length) {
        bis.readBytes(bytes, startIndex, length);
    }

    public void reset() {
        bis.reset();
    }

    public void release() {
        bis.release();
        bis = null;
    }

    public int getBodyDataSize() {
        return bis.getBodyDataSize();
    }

    public void setIndex(int i) {
        bis.setIndex(i);
    }

    public int getIndex() {
        return bis.getIndex();
    }

    public void setPrintln(boolean open) {
        bis.setPrintln(open);
    }

    @Override
    public float readFloat() {
        return bis.readFloat();
    }

    @Override
    public double readDouble() {
        return bis.readDouble();
    }
}