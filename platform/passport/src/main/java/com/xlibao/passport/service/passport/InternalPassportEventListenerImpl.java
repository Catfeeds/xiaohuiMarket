package com.xlibao.passport.service.passport;

import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.passport.*;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.passport.data.mapper.passport.PassportDataManager;
import com.xlibao.passport.data.model.PassportAlias;
import com.xlibao.passport.data.model.PassportLogger;
import com.xlibao.passport.listener.PassportEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/7.
 */
@Component
public class InternalPassportEventListenerImpl extends BasicWebService implements PassportEventListener {

    @Autowired
    private PassportDataManager passportDataManager;

    @Override
    public void notifyCreatedPassport(Passport passport, PassportRoleTypeEnum passportRoleTypeEnum) {
        ClientTypeEnum clientTypeEnum = passportRoleTypeEnum.getClientTypeEnum();
        int result = passportDataManager.createPassportProperties(passport.getId(), PropertiesTypeEnum.ROLE.getKey(), clientTypeEnum.getCode(), String.valueOf(passportRoleTypeEnum.getKey()));
        if (result <= 0) {
            throw new XlibaoRuntimeException("建立角色失败");
        }
        if (!CommonUtils.nullToEmpty(passport.getDefaultName()).equals(CommonUtils.nullToEmpty(passport.getPhoneNumber()))) {
            createPassportAlias(passport.getId(), passport.getDefaultName(), PassportAliasTypeEnum.DEFAULT.getKey());
        }
        if (CommonUtils.isNotNullString(passport.getPhoneNumber())) {
            createPassportAlias(passport.getId(), passport.getPhoneNumber(), PassportAliasTypeEnum.PHONE.getKey());
        }
        // 新增帐号操作日志
        createPassportLogger(passport.getId(), PassportLoggerTypeEnum.REGISTER, String.valueOf(passport.getVersionIndex()), passport.getAccessToken(), passport.getId());
    }

    @Override
    public void notifyLoginPassport(Passport passport) {
        // 新增帐号操作日志
        createPassportLogger(passport.getId(), PassportLoggerTypeEnum.LOGIIN, String.valueOf(passport.getVersionIndex()), passport.getAccessToken(), passport.getId());
    }

    private int createPassportAlias(long passportId, String aliasName, int type) {
        PassportAlias passportAlias = new PassportAlias();
        passportAlias.setPassportId(passportId);
        passportAlias.setType(type);
        passportAlias.setStatus((byte) PassportStatusEnum.NORMAL.getKey());
        passportAlias.setAliasName(aliasName);

        return passportDataManager.createPassportAlias(passportAlias);
    }

    private int createPassportLogger(long passportId, PassportLoggerTypeEnum loggerTypeEnum, String content, String mark, long optPassportId) {
        PassportLogger passportLogger = new PassportLogger();
        passportLogger.setPassportId(passportId);
        passportLogger.setType(loggerTypeEnum.getKey());
        passportLogger.setContent(content);
        passportLogger.setMark(mark);
        passportLogger.setOptPassportId(optPassportId);

        return passportDataManager.createPassportLogger(passportLogger);
    }
}