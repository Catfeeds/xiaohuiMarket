package com.xlibao.io.entry;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface HeartbeatExtension {

    String heartbeatParameters();

    void notifyHeartbeatResult(long userKey, String callbackValue);
}