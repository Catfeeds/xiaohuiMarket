package com.xlibao.passport.service.passport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/16.
 */
@Component
public class PassportApplicationContextNotify {

    private static final String MODULE_NAME = "passport";

    @Autowired
    private PassportEventListenerManager passportEventListenerManager;
    @Autowired
    private InternalPassportEventListenerImpl passportEventListener;

    public void loaderNotify() {
        passportEventListenerManager.registerPassportEventListener(MODULE_NAME, passportEventListener);
    }
}
