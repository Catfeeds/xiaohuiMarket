package com.xlibao.io.entry.impl;

import com.xlibao.io.entry.ByteOutputStream;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public class ByteOutputStreamImpl implements ByteOutputStream {

    private byte[] data = null;
    private int index = 0;
    private boolean println = false;

    public ByteOutputStreamImpl(int size) {
        this.data = new byte[size];
    }

    public ByteOutputStreamImpl(byte[] data, int index) {
        this.data = data;
        this.index = index;
    }

    @Override
    public void setPrintln(boolean open) {
        this.println = open;
    }

    @Override
    public void writeByte(int v) {
        updateSize(1);
        data[(index++)] = ((byte) v);
        if (println) {
            System.err.println("writeByte=" + v);
        }
    }

    @Override
    public void writeShort(int v) {
        updateSize(2);
        data[(index++)] = ((byte) (v >>> 8 & 0xFF));
        data[(index++)] = ((byte) (v >>> 0 & 0xFF));
        if (println) {
            System.err.println("writeShort=" + v);
        }
    }

    @Override
    public void writeInt(int v) {
        updateSize(4);
        data[(index++)] = ((byte) (v >>> 24 & 0xFF));
        data[(index++)] = ((byte) (v >>> 16 & 0xFF));
        data[(index++)] = ((byte) (v >>> 8 & 0xFF));
        data[(index++)] = ((byte) (v >>> 0 & 0xFF));
        if (println) {
            System.err.println("writeInt=" + v);
        }
    }

    @Override
    public void writeLong(long v) {
        updateSize(8);
        data[(index++)] = ((byte) (int) (v >>> 56));
        data[(index++)] = ((byte) (int) (v >>> 48));
        data[(index++)] = ((byte) (int) (v >>> 40));
        data[(index++)] = ((byte) (int) (v >>> 32));
        data[(index++)] = ((byte) (int) (v >>> 24));
        data[(index++)] = ((byte) (int) (v >>> 16));
        data[(index++)] = ((byte) (int) (v >>> 8));
        data[(index++)] = ((byte) (int) (v >>> 0));
        if (println) {
            System.err.println("writeLong=" + v);
        }
    }

    @Override
    public void writeFloat(float v) {
        int i = Float.floatToIntBits(v);
        writeInt(i);
    }

    @Override
    public void writeDouble(double v) {
        long l = Double.doubleToLongBits(v);
        writeLong(l);
    }

    @Override
    public void writeChar(char c) {
        updateSize(2);
        data[(index++)] = ((byte) (c >>> '\b' & 0xFF));
        data[(index++)] = ((byte) (c >>> '\000' & 0xFF));
        if (println) {
            System.err.println("writeChar=" + c);
        }
    }

    @Override
    public void writeBoolean(boolean b) {
        if (println) {
            System.err.println("writeBoolean=" + b);
        }
        writeByte(b ? 1 : 0);
    }

    @Override
    public void writeUTF(String v) {
        if (println) {
            System.err.println("writeUTF=" + v);
        }
        try {
            byte[] datas = v.getBytes("UTF-8");
            writeInt(datas.length);
            writeBytes(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeBytes(byte[] bs) {
        updateSize(bs.length);
        System.arraycopy(bs, 0, data, index, bs.length);
        this.index += bs.length;
    }

    @Override
    public void writeBytes(byte[] bs, int startIndex, int length) {
        updateSize(length);
        System.arraycopy(bs, startIndex, data, index, length);
        this.index += length;
    }

    @Override
    public byte[] toBytes(int startIndex) {
        byte[] b = new byte[index - startIndex];
        System.arraycopy(data, startIndex, b, 0, index);
        return b;
    }

    @Override
    public byte[] toBytes() {
        byte[] b = new byte[index];
        System.arraycopy(data, 0, b, 0, index);
        return b;
    }

    @Override
    public void reset() {
        data = new byte[1024];
        index = 0;
    }

    @Override
    public void release() {
        data = null;
        index = 0;
    }

    @Override
    public int getBodyDataSize() {
        return index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    private void updateSize(int newIndex) {
        int newcount = index + newIndex;
        if (newcount > data.length) {
            byte[] newdata = new byte[newcount + 341];
            System.arraycopy(data, 0, newdata, 0, data.length);
            data = newdata;
        }
    }
}
