package com.xlibao.common.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chinahuangxc on 2016/10/23.
 */
public class ApplicationSessionListener implements HttpSessionListener {

    private static AtomicLong sessionSize = new AtomicLong(0);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        sessionSize.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        sessionSize.decrementAndGet();
    }

    static long getSessionSize() {
        return sessionSize.get();
    }
}