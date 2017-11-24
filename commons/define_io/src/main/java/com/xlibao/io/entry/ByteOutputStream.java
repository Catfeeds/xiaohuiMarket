package com.xlibao.io.entry;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface ByteOutputStream {

    /**
     * 设置写数据时是否在控制台打印对应数据的内容
     *
     * @param println true为打印内容 false为不打印结果
     */
    void setPrintln(boolean println);

    /**
     * <pre>
     * 填充一个byte数据；同时更新消息长度为原长度+1，并将指针后移一位
     * <b>注意：</b>这里为了使用者不需要进行类型转换，接受了一个int类型的数值；实际上实现中已将int强制转换为byte来进行填充；所以使用者必须注意取值范围
     * </pre>
     *
     * @param v 进行填充的数据
     */
    void writeByte(int v);

    /**
     * <pre>
     * 填充一个short数据；同时更新消息长度为原长度+2，并将指针后移两位
     * <b>注意：</b>这里为了使用者不需要进行类型转换，接受了一个int类型的数值；实际上实现中已将int强制转换为short来进行填充；所以使用者必须注意取值范围
     * </pre>
     *
     * @param v 进行填充的数据
     */
    void writeShort(int v);

    /**
     * 填充一个int数据；同时更新消息长度为原长度+4，并将指针后移四位
     *
     * @param v 进行填充的数据
     */
    void writeInt(int v);

    /**
     * 填充一个long数据；同时更新消息长度为原长度+8，并将指针后移八位
     *
     * @param v 进行填充的数据
     */
    void writeLong(long v);

    /**
     * 填充一个float数据；同时更新消息长度为原长度+4，并将指针后移四位
     *
     * @param v 进行填充的数据
     */
    void writeFloat(float v);

    /**
     * 填充一个double数据；同时更新消息长度为原长度+8，并将指针后移八位
     *
     * @param v 进行填充的数据
     */
    void writeDouble(double v);

    /**
     * 填充一个boolean数据；同时更新消息长度为原长度+1，并将指针后移一位；底层以byte表示
     *
     * @param v 进行填充的数据
     */
    void writeBoolean(boolean v);

    /**
     * <pre>
     * 填充一个char数据；同时更新消息长度为原长度+2，并将指针后移两位
     * <b>注意：</b>char在java中是2个字节。java采用unicode，2个字节（16位）来表示一个字符。
     * </pre>
     *
     * @param v 进行填充的数据
     */
    void writeChar(char v);

    /**
     * <pre>
     * 填充一个字符串
     * <b>注意：</b>这里实现的编码为UTF-8，填充后位置后移的位数为：String转byte[]的length+4；这里为什么加四位，主要是因为字符串必须有一个表示长度的数据，我们以int来填充
     * </pre>
     *
     * @param v 进行填充的数据
     */
    void writeUTF(String v);

    /**
     * 填充一个byte数组内容
     *
     * @param v 进行填充的数据
     */
    void writeBytes(byte[] v);

    /**
     * 填充一个byte数组内容，由使用者指定填充的起点和长度
     *
     * @param v      进行填充的数据
     * @param source 填充的起点
     * @param length 填充的长度
     */
    void writeBytes(byte[] v, int source, int length);

    /**
     * <pre>
     * 将数据以byte数组的形式返回
     * 该方法一般在发送数据进行编码时使用(即将对象转成byte数组)
     * </pre>
     *
     * @return 已填充的数据
     */
    byte[] toBytes();

    /**
     * 将数据以byte数组的形式返回(指定起点)
     *
     * @param startIndex 指定的起点
     * @return 指定起点的数据
     */
    byte[] toBytes(int startIndex);

    /**
     * <pre>
     * 重置缓存区
     * 指针位置归零；填充数据清空
     * </pre>
     */
    void reset();

    /**
     * <pre>
     * 释放内存资源
     * 缓冲区设置为null
     * </pre>
     */
    void release();

    /**
     * 当前缓冲区的数据大小(字节)
     *
     * @return 当前数据大小
     */
    int getBodyDataSize();

    /**
     * 当前缓冲区指针位置
     *
     * @return 当前指针位置
     */
    int getIndex();

    /**
     * 设置缓冲区指针
     *
     * @param index 使用者提供的指针位置
     */
    void setIndex(int index);
}