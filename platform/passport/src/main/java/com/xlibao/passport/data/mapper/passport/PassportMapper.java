package com.xlibao.passport.data.mapper.passport;

import com.xlibao.metadata.passport.Passport;
import org.apache.ibatis.annotations.Param;

public interface PassportMapper {

    int createPassport(Passport passport);

    Passport getPassport(@Param("id") long id);

    int modifyPassportPassword(@Param("id") long id, @Param("password") String password);

    int modifyAccessToken(@Param("id") long id, @Param("accessToken") String accessToken, @Param("appointTime") String appointTime);

    int extendAccessToken(@Param("passportId") long passportId, @Param("accessToken") String accessToken, @Param("appointTime") String appointTime);

    int modifyPhoneNumber(@Param("passportId") long passportId, @Param("mobileNumber") String mobileNumber);

    int modifyNickname(@Param("id") long id, @Param("nickname") String nickname);

    int perfectPassportInformation(@Param("id") long id, @Param("nickName") String nickName, @Param("headImageUrl") String headImageUrl, @Param("sex") byte sex, @Param("status") int status);
}