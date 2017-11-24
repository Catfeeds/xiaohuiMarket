package com.xlibao.passport.service.passport;

import com.xlibao.common.constant.passport.PassportRoleTypeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.passport.listener.PassportEventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/7.
 */
@Component
public class PassportEventListenerManager {

    private Map<String, PassportEventListener> passportEventListenerMap = new HashMap<>();

    public void registerPassportEventListener(String name, PassportEventListener passportEventListener) {
        PassportEventListener listener = passportEventListenerMap.get(name);
        if (listener != null) { // 存在同名的监听器
            throw new XlibaoRuntimeException("注册通行证监听器失败，已存在同名监听器(" + name + ")");
        }
        passportEventListenerMap.put(name, passportEventListener);
    }

    public void notifyCreatedPassport(Passport passport, PassportRoleTypeEnum passportRoleTypeEnum) {
        for (PassportEventListener passportEventListener : passportEventListenerMap.values()) {
            passportEventListener.notifyCreatedPassport(passport, passportRoleTypeEnum);
        }
    }

    public void notifyLoginPassport(Passport passport) {
        for (PassportEventListener passportEventListener : passportEventListenerMap.values()) {
            passportEventListener.notifyLoginPassport(passport);
        }
    }
}