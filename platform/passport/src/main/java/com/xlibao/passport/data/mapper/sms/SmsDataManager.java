package com.xlibao.passport.data.mapper.sms;

import com.xlibao.passport.data.model.PassportSmsLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chinahuangxc on 2017/2/12.
 */
@Component
public class SmsDataManager {

    @Autowired
    private PassportSmsLoggerMapper passportSmsLoggerMapper;

    public int createSmsLogger(String phoneNumber, int type, byte status, String content, String code) {
        PassportSmsLogger smsLogger = new PassportSmsLogger();
        smsLogger.setPhone(phoneNumber);
        smsLogger.setType(type);
        smsLogger.setStatus(status);
        smsLogger.setContent(content);
        smsLogger.setCode(code);
        return passportSmsLoggerMapper.createSmsLogger(smsLogger);
    }

    public PassportSmsLogger getSmsLogger(String phoneNumber, int type, byte status) {
        return passportSmsLoggerMapper.getSmsLogger(phoneNumber, type, status);
    }

    public int modifySmsStatus(long id, byte status) {
        return passportSmsLoggerMapper.modifySmsStatus(id, status);
    }
}