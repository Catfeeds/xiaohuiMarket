package com.xlibao.io.service.netty;

public class NettyConfig {

    /** 空闲类型 -- 读 */
    public static final int TIME_OUT_READER = 0;
    /** 空闲类型 -- 写 */
    public static final int TIME_OUT_WRITER = 1;
    /** 空闲类型 -- 读、写 */
    public static final int TIME_OUT_BOTH = 2;

    // 写超时
    private int writeTimeout = 10;
    // 读超时
    private int readTimeout = 10;
    // 读写同时超时
    private int bothTimeout = 15;

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public void setBothTimeout(int bothTimeout) {
        this.bothTimeout = bothTimeout;
    }

    int getReadTimeout() {
        return readTimeout;
    }

    int getWriteTimeout() {
        return writeTimeout;
    }

    int getBothTimeout() {
        return bothTimeout;
    }
}