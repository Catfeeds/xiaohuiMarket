package com.xlibao.passport.data.mapper.sms;

import com.xlibao.passport.data.model.PassportSmsLogger;
import org.apache.ibatis.annotations.Param;

public interface PassportSmsLoggerMapper {

    int createSmsLogger(PassportSmsLogger smsLogger);

    PassportSmsLogger getSmsLogger(@Param("phoneNumber") String phoneNumber, @Param("type") int type, @Param("status") byte status);

    int modifySmsStatus(@Param("id") long id, @Param("status") byte status);
}