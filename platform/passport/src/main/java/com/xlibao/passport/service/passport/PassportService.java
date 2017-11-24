package com.xlibao.passport.service.passport;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.passport.Passport;

/**
 * @author chinahuangxc on 2017/2/6.
 */
public interface PassportService {

    JSONObject registerPassport();

    Passport channelAuthorize(String channelUserId, String password, int channelType, int deviceType, String deviceName);

    JSONObject loginPassport();

    JSONObject loginForVerificationCode();

    JSONObject logoutPassport();

    JSONObject modifyPassword();

    JSONObject forgetPassword();

    JSONObject resetMobileNumber();

    int bindingMobileNumber(long passportId, String mobileNumber);

    JSONObject authentication();

    JSONObject versionUpgrade();

    JSONObject changeAccessToken();

    JSONObject extendAccessToken();

    void changeAccessToken(Passport passport);

    Passport getPassport(long passportId);

    Passport getPassport(String loginName);

    JSONObject phoneBeUsed();

    JSONObject modifyNickname();
}