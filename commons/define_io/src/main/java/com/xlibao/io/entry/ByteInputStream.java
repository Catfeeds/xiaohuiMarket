package com.xlibao.io.entry;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface ByteInputStream {

    /**
     * 设置读取数据时是否在控制台打印对应数据的内容
     *
     * @param println true为打印内容 false为不打印结果
     */
    void setPrintln(boolean println);

    /**
     * 读取一个byte字节，同时位置向后移一位
     *
     * @return 读取到的byte
     */
    byte readByte();

    /**
     * 读取一个short数值，同时位置向后移两位
     *
     * @return 读取到的short
     */
    short readShort();

    /**
     * 读取一个int数值，同时位置向后移四位
     *
     * @return 读取到的int
     */
    int readInt();

    /**
     * 读取一个long数值，同时位置向后移八位
     *
     * @return 读取到的long
     */
    long readLong();

    /**
     * 读取一个float数值，同时位置向后移四位
     *
     * @return 读取到的float
     */
    float readFloat();

    /**
     * 读取一个double数值，同时位置向后移八位
     *
     * @return 读取到的double
     */
    double readDouble();

    /**
     * 读取一个boolean值，同时位置向后移一位(底层以byte表示)
     *
     * @return 读取到的boolean
     */
    boolean readBoolean();

    /**
     * 读取一个char值，同时位置向后移两位
     *
     * @return 读取到的char
     */
    char readChar();

    /**
     * <pre>
     * 读取一个字符串
     * <b>注意：</b>这里实现的编码为UTF-8，读取后位置后移的位数为：String转byte[]的length+4；这里为什么加四位，主要是因为字符串必须有一个表示长度的数据，我们以int来填充
     * </pre>
     *
     * @return 读取到的字符串
     */
    String readUTF();

    /**
     * <pre>
     * 填充数据到使用者指定的byte数组中
     * <b>注意：</b>使用者提供的byte数组必须指定长度
     * </pre>
     *
     * @param target 使用者提供已指定长度且元素为空的byte数组
     */
    void readBytes(byte[] target);

    /**
     * <pre>
     * 填充数据到使用者指定的byte数组中，由使用者指定填充的起点和长度
     * <b>注意：</b>使用者提供的byte数组必须指定长度且长度不能小于参数length+startIndex，否则将会数组溢出
     * </pre>
     *
     * @param target     使用者提供已指定长度且元素为空的byte数组
     * @param startIndex 使用者指定的填充起点
     * @param length     使用者指定的填充长度
     */
    void readBytes(byte[] target, int startIndex, int length);

    /**
     * 将位置归零(可重头开始读取数据)
     */
    void reset();

    /**
     * 释放内存(将消息内容清空，无法再进行消息读取)
     */
    void release();

    /**
     * 获取消息内容的大小
     *
     * @return 消息总大小
     */
    int getBodyDataSize();

    /**
     * 使用者强行设置消息的指针位置
     *
     * @param index 消息位置
     */
    void setIndex(int index);

    /**
     * 获取当前消息指针位置
     *
     * @return 消息位置
     */
    int getIndex();
}