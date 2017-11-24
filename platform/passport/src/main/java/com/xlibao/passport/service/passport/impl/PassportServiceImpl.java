package com.xlibao.passport.service.passport.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.passport.*;
import com.xlibao.common.constant.sms.SmsCodeTypeEnum;
import com.xlibao.common.constant.version.VersionTypeEnum;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.common.exception.code.PassportErrorCodeEnum;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.metadata.passport.PassportChannel;
import com.xlibao.metadata.passport.PassportProperties;
import com.xlibao.passport.data.mapper.channel.ChannelDataManager;
import com.xlibao.passport.data.mapper.passport.PassportDataManager;
import com.xlibao.passport.data.mapper.version.VersionDataManager;
import com.xlibao.passport.data.model.PassportAlias;
import com.xlibao.passport.data.model.PassportVersion;
import com.xlibao.passport.service.passport.PassportEventListenerManager;
import com.xlibao.passport.service.passport.PassportService;
import com.xlibao.passport.service.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author chinahuangxc on 2017/2/6.
 */
@Transactional
@Service("passportService")
public class PassportServiceImpl extends BasicWebService implements PassportService {

    private static final Logger logger = LoggerFactory.getLogger(PassportServiceImpl.class);

    @Autowired
    private PassportDataManager passportDataManager;
    @Autowired
    private VersionDataManager versionDataManager;
    @Autowired
    private ChannelDataManager channelDataManager;

    @Autowired
    private PassportEventListenerManager passportEventListenerManager;
    @Autowired
    private SmsService smsService;

    @Override
    public JSONObject registerPassport() {
        String name = getUTF("name");
        String password = getUTF("password");
        String showName = getUTF("showName", name);
        String realName = getUTF("realName", "");
        String idNumber = getUTF("idNumber", "");
        String phoneNumber = getUTF("phoneNumber");
        String smsCode = getUTF("smsCode");
        byte sex = getByteParameter("sex", GlobalAppointmentOptEnum.FEMALE.getKey());
        // int clientType = getIntParameter("clientType", ClientTypeEnum.CONSUMER.getKey());
        int type = getIntParameter("type", PassportTypeEnum.DEFAULT.getKey());
        long fromChannel = getLongParameter("fromChannel");
        int deviceType = getIntParameter("deviceType", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey());
        String deviceName = getUTF("deviceName", "Android");

        int versionIndex = getIntParameter("versionIndex");

        if (!CommonUtils.isLegalChar(name, 6, 32)) {
            return fail("用户名只能为6到32位字母、数字和下划线组成");
        }
        if (password.length() < 8) {
            return fail("密码长度不能低于8位");
        }
        if (!CommonUtils.isMobileNum(phoneNumber)) {
            return fail("[" + phoneNumber + "]不是有效的手机号！");
        }
        // 短信验证码验证 错误时抛 XlibaoIllegalArgumentException异常
        smsService.verifySmsCode(phoneNumber, smsCode, SmsCodeTypeEnum.REGISTER.getKey());
        Passport passport = passportDataManager.getPassport(name);
        if (passport != null) {
            throw new XlibaoRuntimeException((CommonUtils.isMobileNum(name) ? "手机号：" : "帐号名：") + name + "已被使用，请重新设置！");
        }
        passport = passportDataManager.getPassport(phoneNumber);
        if (passport != null) {
            throw new XlibaoRuntimeException("手机号：" + phoneNumber + "已被使用，请重新设置！");
        }
        PassportChannel passportChannel = channelDataManager.getChannel(fromChannel);
        if (passportChannel == null) {
            throw new XlibaoRuntimeException("您所使用的客户端不合法，请到官网下载最新的安装包！");
        }
        // 密码前缀 + 密码原文 + 密码后缀
        String encryptionPassword = encryptionPassword(password);

        passport = new Passport();
        passport.setDefaultName(name);
        passport.setPassword(encryptionPassword);
        passport.setShowName(showName);
        passport.setRealName(realName);
        passport.setIdNumber(idNumber);
        passport.setType(type);
        passport.setStatus(PassportStatusEnum.NORMAL.getKey());
        passport.setPhoneNumber(phoneNumber);
        passport.setSex(sex);
        passport.setFromChannel(fromChannel);
        passport.setDeviceType(deviceType);
        passport.setDeviceName(deviceName);
        passport.setVersionIndex(versionIndex);

        int result = passportDataManager.createPassport(passport);
        if (result <= 0) {
            throw new XlibaoRuntimeException("注册通行证失败，请稍后再试！");
        }
        changeAccessToken(passport);

        // 通知所有监听者 建立通行证成功
        passportEventListenerManager.notifyCreatedPassport(passport, PassportRoleTypeEnum.DEFAULT);
        // 到这步可理解为用户已正常登录系统
        return success(fillLoginMessage(passport, String.valueOf(PassportRoleTypeEnum.DEFAULT.getKey())));
    }

    @Override
    public Passport channelAuthorize(String channelUserId, String password, int channelType, int deviceType, String deviceName) {
        String encryptionPassword = encryptionPassword(password);

        Passport passport = new Passport();
        passport.setDefaultName(channelUserId);
        passport.setPassword(encryptionPassword);
        passport.setType(channelType);
        passport.setStatus(PassportStatusEnum.UN_PERFECT_INFORMATION.getKey());
        passport.setFromChannel((long) channelType);
        passport.setDeviceType(deviceType);
        passport.setDeviceName(deviceName);
        passport.setVersionIndex(1);

        try {
            int result = passportDataManager.createPassport(passport);
            if (result <= 0) {
                throw new XlibaoRuntimeException("注册通行证失败，请稍后再试！");
            }
            changeAccessToken(passport);

            // 通知所有监听者 建立通行证成功
            passportEventListenerManager.notifyCreatedPassport(passport, PassportRoleTypeEnum.DEFAULT);
        } catch (Exception ex) {
            passport = getPassport(channelUserId);
            if (passport == null) {
                throw new XlibaoRuntimeException("注册通行证失败，请稍后再试！");
            }
        }
        return passport;
    }

    @Override
    public JSONObject loginPassport() {
        String username = getUTF("username");
        String password = getUTF("password");
        int deviceType = getIntParameter("deviceType", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey());
        int clientType = getIntParameter("clientType", ClientTypeEnum.CONSUMER.getKey());
        // 当前版本号
        int versionIndex = getIntParameter("versionIndex");

        password = encryptionPassword(password);

        Passport passport = passportDataManager.getPassport(username);
        if (passport == null) {
            logger.error("登录失败，错误的用户名 -- " + username);
            throw new XlibaoIllegalArgumentException("帐号或密码错误");
        }
        if (!password.equals(passport.getPassword())) {
            logger.error("登录失败，登录帐号(" + username + ")、登录密码(" + password + ")错误，用户 -- " + passport);
            throw new XlibaoIllegalArgumentException("帐号或密码错误");
        }
        return afterLogin(passport, deviceType, clientType, versionIndex);
    }

    @Override
    public JSONObject loginForVerificationCode() {
        String phone = getUTF("phone");
        String smsCode = getUTF("smsCode");

        int deviceType = getIntParameter("deviceType", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey());
        int clientType = getIntParameter("clientType", ClientTypeEnum.CONSUMER.getKey());
        // 当前版本号
        int versionIndex = getIntParameter("versionIndex");

        if (!CommonUtils.isMobileNum(phone)) {
            return PlatformErrorCodeEnum.PHONE_NUMBER_ERROR.response("手机号[" + phone + "]无效");
        }
        // 短信验证码验证 错误时抛 XlibaoIllegalArgumentException异常
        smsService.verifySmsCode(phone, smsCode, SmsCodeTypeEnum.LOGIN.getKey());

        Passport passport = passportDataManager.getPassport(phone);
        if (passport == null) {
            logger.error("登录失败，手机号不存在 -- " + phone);
            throw new XlibaoIllegalArgumentException("手机号：" + phone + "未注册！");
        }
        return afterLogin(passport, deviceType, clientType, versionIndex);
    }

    @Override
    public JSONObject logoutPassport() {
        long passportId = getLongParameter("passportId");
        String accessToken = getUTF("accessToken");
        Passport passport = getPassport(passportId);
        if (!accessToken.equals(passport.getAccessToken())) {
            throw PlatformErrorCodeEnum.INVALID_ACCESS.throwException();
        }
        int result = passportDataManager.modifyAccessToken(passport.getId(), "", CommonUtils.nowFormat());
        if (result >= 1) {
            passport.setAccessToken(accessToken);
            passport.setTokenInvalidTime(new Date());
        }
        setAccessToken(passport.getAccessToken());
        return null;
    }

    @Override
    public JSONObject modifyPassword() {
        String username = getUTF("username");
        String oldPassword = getUTF("oldPassword");
        String newPassword = getUTF("newPassword");
        String confirmPassword = getUTF("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            return fail("两次输入的密码不同");
        }
        if (newPassword.length() < 8) {
            return fail("新密码长度不能低于8位");
        }
        Passport passport = passportDataManager.getPassport(username);
        if (passport == null) {
            logger.error("修改密码失败，错误的用户名 -- " + username);
            throw new XlibaoIllegalArgumentException("帐号或者原密码错误");
        }
        oldPassword = encryptionPassword(oldPassword);
        if (!oldPassword.equals(passport.getPassword())) {
            logger.error("原密码错误，帐号(" + username + ")、密码(" + oldPassword + ")，用户 -- " + passport);
            throw new XlibaoIllegalArgumentException("帐号或者原密码错误");
        }
        int result = passportDataManager.modifyPassportPassword(passport.getId(), encryptionPassword(newPassword));
        return result <= 0 ? fail("修改密码失败，请稍后重试！") : success("修改密码成功");
    }

    @Override
    public JSONObject forgetPassword() {
        String phoneNumber = getUTF("phoneNumber");
        // 短信验证码
        String smsCode = getUTF("smsCode");
        String password = getUTF("password");
        String confirmPassword = getUTF("confirmPassword");

        if (!password.equals(confirmPassword)) {
            return fail("两次输入的密码不同");
        }
        if (password.length() < 8) {
            return fail("新密码长度不能低于8位");
        }
        // 短信验证码验证 错误时抛 XlibaoIllegalArgumentException异常
        smsService.verifySmsCode(phoneNumber, smsCode, SmsCodeTypeEnum.MODIFY_PASSWORD.getKey());

        Passport passport = passportDataManager.getPassport(phoneNumber);
        if (passport == null) {
            return fail("手机号(" + phoneNumber + ")未被注册");
        }
        int result = passportDataManager.modifyPassportPassword(passport.getId(), encryptionPassword(password));
        return result <= 0 ? fail("重置密码失败，请稍后重试！") : success("重置密码成功");
    }

    @Override
    public JSONObject resetMobileNumber() {
        long passportId = getLongParameter("passportId");
        String currentMobileNumber = getUTF("currentMobileNumber");
        String expectMobileNumber = getUTF("expectMobileNumber");
        String smsCode = getUTF("smsCode");

        if (!CommonUtils.isMobileNum(currentMobileNumber)) {
            return fail("当前使用的手机号格式错误，请检查！");
        }
        if (!CommonUtils.isMobileNum(expectMobileNumber)) {
            return fail("新输入的手机号格式错误，请检查！");
        }
        Passport passport = getPassport(passportId);
        if (!currentMobileNumber.equals(passport.getPhoneNumber())) {
            return fail("当前使用的手机号错误，请重新输入！");
        }
        passport = passportDataManager.getPassport(expectMobileNumber);
        if (passport != null) {
            throw new XlibaoRuntimeException("手机号：" + expectMobileNumber + "已被使用，请重新设置！");
        }
        // 短信验证码验证 错误时抛 XlibaoIllegalArgumentException异常
        smsService.verifySmsCode(expectMobileNumber, smsCode, SmsCodeTypeEnum.RESET_MOBILE_NUMBER.getKey());
        // TODO 数据库操作
        return success("重置手机号码成功，您可以使用手机号码[" + expectMobileNumber + "]进行登录");
    }

    @Override
    public int bindingMobileNumber(long passportId, String mobileNumber) {
        Passport passport = passportDataManager.getPassport(mobileNumber);
        if (passport != null) {
            throw new XlibaoRuntimeException("手机号：" + mobileNumber + "已被使用，请重新设置！");
        }
        int result = passportDataManager.modifyPhoneNumber(passportId, mobileNumber);
        if (result <= 0) {
            throw new XlibaoRuntimeException("绑定手机号出错，请稍后重试");
        }
        PassportAlias passportAlias = new PassportAlias();
        passportAlias.setPassportId(passportId);
        passportAlias.setType(PassportAliasTypeEnum.PHONE.getKey());
        passportAlias.setAliasName(mobileNumber);
        passportAlias.setStatus((byte) PassportStatusEnum.NORMAL.getKey());

        result = passportDataManager.createPassportAlias(passportAlias);
        if (result <= 0) {
            throw new XlibaoRuntimeException("绑定手机号出错，请稍后重试");
        }
        return result;
    }

    @Override
    public JSONObject authentication() {
        long passportId = getLongParameter("passportId");
        String fullName = getUTF("fullName");
        String IDNumber = getUTF("IDNumber");
        String backIDImage = getUTF("backIDImage");
        String frontIDImage = getUTF("frontIDImage");

        Passport passport = getPassport(passportId);

        PassportProperties properties = passportDataManager.getPassportProperties(passport.getId(), PropertiesTypeEnum.PERSONAL.getKey(), PersonalKeyEnum.ID_AUTHENTICATION.getKey());
        if (properties != null) {
        }
        passport.setIdNumber(IDNumber);
        // passport.getRealName()
        return null;
    }

    @Override
    public JSONObject versionUpgrade() {
        int deviceType = getIntParameter("deviceType");
        int clientType = getIntParameter("clientType");
        int versionIndex = getIntParameter("versionIndex");

        return success(versionControl(deviceType, clientType, versionIndex));
    }

    @Override
    public JSONObject changeAccessToken() {
        // TODO 2017-09-28 23:43 修改accessToken的使用方式 将原来每次访问生成全新的token业务去除 修改延长当前token的有效期
        // long passportId = getLongParameter("passportId");
        // String accessToken = getUTF("accessToken");

        // changeAccessToken(passportId, accessToken);
        // return success();
        return extendAccessToken();
    }

    @Override
    public JSONObject extendAccessToken() {
        long passportId = getLongParameter("passportId");
        String accessToken = getUTF("accessToken");

        Passport passport = getPassport(passportId);
        if (!accessToken.equals(passport.getAccessToken())) {
            throw PlatformErrorCodeEnum.INVALID_ACCESS.throwException();
        }
        if (passport.getTokenInvalidTime() == null || passport.getTokenInvalidTime().getTime() <= System.currentTimeMillis()) {
            throw PlatformErrorCodeEnum.INVALID_ACCESS.throwException();
        }
        int result = passportDataManager.extendAccessToken(passportId, accessToken, CommonUtils.dateFormat(System.currentTimeMillis() + GlobalConstantConfig.ACCESS_TOKEN_TIME_OUT));
        if (result <= 0) {
            logger.error("其实此时的Token已经更新失败，但为了避免多线程时造成的误会，因为该请求依然放行；但如果并不是因为多线程造成的问题，那么可能在下次访问时进行拒绝；用户为："
                    + passport.getShowName() + "(" + passportId + ")，本次的accessToken为：" + accessToken);
        }
        return success();
    }

    @Override
    public void changeAccessToken(Passport passport) {
        String accessToken = CommonUtils.generateAccessToken(GlobalConstantConfig.PARTNER_ID_PREFIX + String.valueOf(passport.getId()));
        long accessTokenTimeout = System.currentTimeMillis() + GlobalConstantConfig.ACCESS_TOKEN_TIME_OUT;
        int result = passportDataManager.modifyAccessToken(passport.getId(), accessToken, CommonUtils.dateFormat(accessTokenTimeout));
        if (result >= 1) {
            passport.setAccessToken(accessToken);
            passport.setTokenInvalidTime(new Date(accessTokenTimeout));
        }
        setAccessToken(passport.getAccessToken());
    }

    @Override
    public Passport getPassport(long passportId) {
        Passport passport = passportDataManager.getPassport(passportId);
        if (passport == null) {
            throw PassportErrorCodeEnum.NOT_FOUND_PASSPORT.throwException("无法获取通行证信息，通行证ID错误：" + passportId);
        }
        return passport;
    }

    @Override
    public Passport getPassport(String loginName) {
        return passportDataManager.getPassport(loginName);
    }

    @Override
    public JSONObject phoneBeUsed() {
        String phoneNumber = getUTF("phoneNumber");
        if (!CommonUtils.isMobileNum(phoneNumber)) {
            return PlatformErrorCodeEnum.PHONE_NUMBER_ERROR.response("手机号格式错误，号码：" + phoneNumber);
        }
        Passport passport = getPassport(phoneNumber);
        return passport == null ? success() : fail("手机号[" + phoneNumber + "]已被使用");
    }

    @Override
    public JSONObject modifyNickname() {
        changeAccessToken();
        long passportId = getLongParameter("passportId");
        String nickname = getUTF("nickname");

        int result = passportDataManager.modifyNickname(passportId, nickname);
        return result <= 0 ? fail() : success();
    }

    private JSONObject afterLogin(Passport passport, int deviceType, int clientType, int versionIndex) {
        if (passport.getStatus() == PassportStatusEnum.FORBID_LOGIN.getKey()) {
            logger.error("登录失败，账户(" + passport + ")的状态类型不正常，目前状态：" + passport.getStatus());
            throw new XlibaoRuntimeException("您的帐号已被禁止登录系统，如有疑问，请联系我司客服");
        }
        if (passport.getStatus() == PassportStatusEnum.BACK_LIST.getKey()) {
            logger.error("登录失败，账户(" + passport + ")的状态类型不正常，目前状态：" + passport.getStatus());
            throw new XlibaoRuntimeException("您的帐号目前处于黑名单状态，暂时不能登录");
        }
        // 客户端的权限控制
        String roleValue = clientPowerControl(passport, clientType);
        // 设置新的访问密令
        changeAccessToken(passport);

        passport.setVersionIndex(versionIndex);
        // 版本检测
        JSONObject versionMessage = versionControl(deviceType, clientType, versionIndex);
        JSONObject response = fillLoginMessage(passport, roleValue);
        response.put("versionMessage", versionMessage);

        // 通知监听系统登录成功
        passportEventListenerManager.notifyLoginPassport(passport);
        return success(response);
    }

    private JSONObject fillLoginMessage(Passport passport, String roleValue) {
        PassportProperties properties = passportDataManager.getPassportProperties(passport.getId(), PropertiesTypeEnum.PERSONAL.getKey(), PersonalKeyEnum.HEAD_IMAGE.getKey());
        // 填充登录消息
        JSONObject response = new JSONObject();
        response.put("passportId", passport.getId());
        response.put("showName", passport.getShowName());
        response.put("phoneNumber", passport.getPhoneNumber());
        response.put("showPhoneNumber", CommonUtils.hideChar(passport.getPhoneNumber(), 3, 7));
        response.put("headImage", properties == null ? "" : CommonUtils.nullToEmpty(properties.getV()));
        response.put("roleValue", roleValue);
        response.put("accessToken", passport.getAccessToken());
        response.put("lastRefreshTime", CommonUtils.dateFormat(passport.getTokenInvalidTime()));

        return response;
    }

    private String clientPowerControl(Passport passport, int clientType) {
        ClientTypeEnum clientTypeEnum = ClientTypeEnum.getClientTypeEnum(clientType);
        // if (clientTypeEnum.isCheck()) {
        PassportProperties properties = passportDataManager.getPassportProperties(passport.getId(), PropertiesTypeEnum.ROLE.getKey(), clientTypeEnum.getCode());
        if (properties == null) {
            logger.error(passport.getDefaultName() + "正在登录客户端：" + clientTypeEnum.getName() + "，但管理员未为用户开通使用权限");
            throw new XlibaoRuntimeException("您没有权限使用该客户端，请联系管理员为您设置权限");
        }
        if (properties.getV().equals(String.valueOf(PassportStatusEnum.FORBID_LOGIN.getKey()))) {
            logger.error(passport.getDefaultName() + "正在登录客户端：" + clientTypeEnum.getName() + "，但权限已为不可使用的状态，值为：" + properties.getV());
            throw new XlibaoRuntimeException("您已被限制使用该客户端，请联系管理员");
        }
        if (properties.getV().equals(String.valueOf(PassportStatusEnum.BACK_LIST.getKey()))) {
            logger.error(passport.getDefaultName() + "正在登录客户端：" + clientTypeEnum.getName() + "，但权限已为不可使用的状态，值为：" + properties.getV());
            throw new XlibaoRuntimeException("您已被限制使用该客户端，请联系管理员");
        }
        return properties.getV();
    }

    private JSONObject versionControl(int deviceType, int clientType, int versionIndex) {
        DeviceTypeEnum deviceTypeEnum = DeviceTypeEnum.getDeviceTypeEnum(deviceType);
        if (deviceTypeEnum == null) {
            throw new XlibaoIllegalArgumentException("错误的设备类型，类型值：" + deviceType);
        }
        ClientTypeEnum clientTypeEnum = ClientTypeEnum.getClientTypeEnum(clientType);
        if (clientTypeEnum == null) {
            throw new XlibaoIllegalArgumentException("错误的客户端类型，类型值：" + clientType);
        }
        PassportVersion version = versionDataManager.getNewestVersion(deviceType, clientTypeEnum.getClientVersionType());
        if (version == null) {
            throw new XlibaoIllegalArgumentException("数据异常，没有对应的版本记录");
        }
        JSONObject response = new JSONObject();
        int upgradeType = VersionTypeEnum.UN_UPGRADE.getKey();
        if (version.getVersionIndex() > versionIndex) {
            if (versionIndex >= version.getMinVersionIndex()) {
                // 兼容的版本 仅提示更新 不强制更新
                upgradeType = VersionTypeEnum.UN_FORCE_UPGRADE.getKey();
            } else {
                // 非兼容版本 强制更新
                upgradeType = VersionTypeEnum.FORCE_UPGRADE.getKey();
            }
            response.put("versionIndex", version.getVersionIndex());
            response.put("introduce", version.getVersionIntroduce());
            response.put("versionUrl", version.getVersionUrl());
        }
        VersionTypeEnum versionTypeEnum = VersionTypeEnum.getVersionTypeEnum(upgradeType);

        response.put("upgradeType", upgradeType);
        response.put("title", versionTypeEnum.getTitle());
        response.put("showVersion", version.getShowVersion());

        return response;
    }
}