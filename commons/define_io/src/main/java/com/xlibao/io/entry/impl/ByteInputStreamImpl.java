package com.xlibao.io.entry.impl;


import com.xlibao.io.entry.ByteInputStream;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public class ByteInputStreamImpl implements ByteInputStream {

    private byte[] data = null;
    private int index = 0;
    private boolean println = false;
    private int msgSize;

    public ByteInputStreamImpl(byte[] data) {
        this.data = data;
        msgSize = this.data.length;
        index = 0;
    }

    @Override
    public void setPrintln(boolean open) {
        println = open;
    }

    @Override
    public byte readByte() {
        byte b = data[(index++)];
        if (this.println) {
            System.err.println("readByte=" + b);
        }
        return b;
    }

    @Override
    public short readShort() {
        short a = (short) data[(index++)];
        short b = (short) data[(index++)];
        short s = (short) (((a & 0xFF) << 8) + ((b & 0xFF)));
        if (this.println) {
            System.err.println("readShort=" + s);
        }
        return s;
    }

    @Override
    public int readInt() {
        int a = data[(index++)];
        int b = data[(index++)];
        int c = data[(index++)];
        int d = data[(index++)];
        int k = ((a & 0xFF) << 24) + ((b & 0xFF) << 16) + ((c & 0xFF) << 8) + ((d & 0xFF));
        if (this.println) {
            System.err.println("readInt=" + k);
        }
        return k;
    }

    @Override
    public long readLong() {
        long a = data[(index++)];
        long b = data[(index++)];
        long c = data[(index++)];
        long d = data[(index++)];
        long e = data[(index++)];
        long f = data[(index++)];
        long g = data[(index++)];
        long h = data[(index++)];
        long re = (a << 56) + ((b & 0xFF) << 48) + ((c & 0xFF) << 40) + ((d & 0xFF) << 32) + ((e & 0xFF) << 24) + ((f & 0xFF) << 16) + ((g & 0xFF) << 8) + ((h & 0xFF));
        if (this.println) {
            System.err.println("readLong=" + re);
        }
        return re;
    }

    @Override
    public float readFloat() {
        int i = readInt();
        return Float.intBitsToFloat(i);
    }

    @Override
    public double readDouble() {
        long l = readLong();
        return Double.longBitsToDouble(l);
    }

    @Override
    public boolean readBoolean() {
        boolean b = readByte() == 1;
        if (this.println) {
            System.err.println("readBoolean=" + b);
        }
        return b;
    }

    @Override
    public char readChar() {
        int a = data[(index++)];
        int b = data[(index++)];
        char c = (char) (((a & 0xFF) << 8) + ((b & 0xFF)));
        if (this.println) {
            System.err.println("readChar=" + c);
        }
        return c;
    }

    @Override
    public String readUTF() {
        // 注意：该方法本身 index不需要移位 因为在读取长度和填充内容时已移位
        int size = readInt();
        byte[] datas = new byte[size];
        readBytes(datas);
        try {
            String value = new String(datas, "UTF-8");
            if (println) {
                System.err.println("readUTF=" + value);
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(getClass().getName() + " read utf error ... data size" + size);
        }
        return "";
    }

    @Override
    public void readBytes(byte[] target) {
        System.arraycopy(data, index, target, 0, target.length);
        index += target.length;
    }

    @Override
    public void readBytes(byte[] target, int startIndex, int length) {
        System.arraycopy(data, index, target, startIndex, length);
        index += length;
    }

    @Override
    public void reset() {
        index = 0;
    }

    @Override
    public void release() {
        data = null;
    }

    @Override
    public int getBodyDataSize() {
        return msgSize;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }
}