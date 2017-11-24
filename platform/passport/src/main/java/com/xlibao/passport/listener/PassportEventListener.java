package com.xlibao.passport.listener;

import com.xlibao.common.constant.passport.PassportRoleTypeEnum;
import com.xlibao.metadata.passport.Passport;

/**
 * @author chinahuangxc on 2017/2/7.
 */
public interface PassportEventListener {

    void notifyCreatedPassport(Passport passport, PassportRoleTypeEnum passportRoleTypeEnum);

    void notifyLoginPassport(Passport passport);
}