package com.xlibao.saas.market.core.message;

import com.xlibao.common.thread.AsyncScheduledService;
import com.xlibao.saas.market.core.scan.VGFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/8/19.
 */
@Component
public class MessageApplicationContextNotify {

    @Autowired
    private VGFrame vgFrame;

    public void loaderNotify() {
        Runnable runnable = () -> vgFrame.setVisible(true);
        AsyncScheduledService.submitImmediateCommonTask(runnable);
    }
}