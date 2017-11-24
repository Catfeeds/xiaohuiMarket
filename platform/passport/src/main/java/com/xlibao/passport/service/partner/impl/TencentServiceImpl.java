package com.xlibao.passport.service.partner.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalAppointmentOptEnum;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import com.xlibao.common.constant.passport.ChannelTypeEnum;
import com.xlibao.common.constant.passport.PassportStatusEnum;
import com.xlibao.common.constant.sms.SmsCodeTypeEnum;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.exception.code.PassportErrorCodeEnum;
import com.xlibao.common.http.HttpUtils;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.passport.config.ConfigFactory;
import com.xlibao.passport.data.mapper.passport.PassportDataManager;
import com.xlibao.passport.service.partner.TencentService;
import com.xlibao.passport.service.partner.WeixinAuthorTypeEnum;
import com.xlibao.passport.service.passport.PassportService;
import com.xlibao.passport.service.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/6/9.
 */
@Transactional
@Service("tencentServiceImpl")
public class TencentServiceImpl extends BasicWebService implements TencentService {

    private final static Logger logger = LoggerFactory.getLogger(TencentServiceImpl.class);

    @Autowired
    private PassportService passportService;
    @Autowired
    private PassportDataManager passportDataManager;
    @Autowired
    private SmsService smsService;

    @Override
    public JSONObject weixinAuthorization() {
        int weixinAuthorType = getIntParameter("weixinAuthorType", WeixinAuthorTypeEnum.OAUTH2.getKey());
        JSONObject response = new JSONObject();

        DeviceTypeEnum deviceType = DeviceTypeEnum.DEVICE_TYPE_H5;
        if (weixinAuthorType == WeixinAuthorTypeEnum.OAUTH2.getKey()) {
            response = weixinOauth2();
        } else if (weixinAuthorType == WeixinAuthorTypeEnum.JS_CODE_SESSION.getKey()) {
            response = weixinJSAuth();
            deviceType = DeviceTypeEnum.DEVICE_TYPE_APPLET;
        }
        String openId = response.getString("openId");
        String sessionKey = response.getString("session_key");
        Passport passport = passportService.getPassport(openId);
        if (passport == null) {
            // 执行注册
            response = autoRegister(openId, deviceType);
        } else {
            passportService.changeAccessToken(passport);
            response = fillPassportMsg(passport);
        }
        response.put("openId", openId);
        response.put("sessionKey", sessionKey);
        return success(response);
    }

    @Override
    public JSONObject perfectWeixinInformation() {
        long passportId = getLongParameter("passportId");
        String nickName = getUTF("nickName");
        String headImageUrl = getUTF("headImageUrl", "");
        byte sex = getByteParameter("sex", GlobalAppointmentOptEnum.FEMALE.getKey());

        int result = passportDataManager.perfectPassportInformation(passportId, nickName, headImageUrl, sex, PassportStatusEnum.NORMAL.getKey());
        return result <= 0 ? fail() : success();
    }

    @Override
    public JSONObject weixinBindingCellNumber() {
        String openId = getUTF("openId");
        String phoneNumber = getUTF("phoneNumber");
        String smsCode = getUTF("smsCode");

        if (!CommonUtils.isMobileNum(phoneNumber)) {
            return PlatformErrorCodeEnum.PHONE_NUMBER_ERROR.response("手机号格式错误，请检查！");
        }
        // 短信验证码验证 错误时抛 XlibaoIllegalArgumentException异常
        smsService.verifySmsCode(phoneNumber, smsCode, SmsCodeTypeEnum.BINDING_MOBILE_NUMBER.getKey());

        Passport passport = passportService.getPassport(openId);
        if (passport == null) {
            throw PassportErrorCodeEnum.NOT_FOUND_PASSPORT.throwException("绑定手机号前请先授权获取您的微信基本数据(仅昵称与头像)");
        }
        int result = passportService.bindingMobileNumber(passport.getId(), phoneNumber);
        return result >= 1 ? success("绑定手机号成功") : fail("绑定手机号失败");
    }

    private JSONObject weixinJSAuth() {
        String code = getUTF("code");
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + ConfigFactory.getPartner().getWeixinAppletAppId() + "&secret=" + ConfigFactory.getPartner().getWeixinAppletAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";

        return oauth2(url);
    }

    private JSONObject weixinOauth2() {
        String code = getUTF("code");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ConfigFactory.getPartner().getWeixinMpAppId() + "&secret=" + ConfigFactory.getPartner().getWeixinMpAppSecret() + "&code=" + code + "&grant_type=authorization_code";

        return oauth2(url);
    }

    private JSONObject oauth2(String url) {
        logger.info("微信授权登录请求链接：" + url);
        String result = HttpUtils.get(url);

        JSONObject response = JSONObject.parseObject(result);
        logger.info("微信授权登录，获取微信用户授权信息：" + response);
        int errorCode = response.getIntValue("errcode");
        if (errorCode != 0) {
            throw new XlibaoRuntimeException(errorCode, response.getString("errmsg"));
        }
        // 本次的访问Token
        String accessToken = response.getString("access_token");
        // 用于刷新的Token
        // String refreshToken = response.getString("refresh_token");
        // 微信在此公众号下的唯一ID
        String openId = response.getString("openid");
        String unionId = response.getString("unionid");
        String sessionKey = response.getString("session_key");

        response.clear();

        response.put("openId", openId);
        response.put("unionId", CommonUtils.nullToEmpty(unionId));
        response.put("sessionKey", CommonUtils.nullToEmpty(sessionKey));
        response.put("accessToken", CommonUtils.nullToEmpty(accessToken));
        return response;
    }

    private JSONObject autoRegister(String channelUserId, DeviceTypeEnum deviceType) {
        try {
            // 帐号不存在 执行自动注册
            Passport passport = passportService.channelAuthorize(channelUserId, channelUserId, ChannelTypeEnum.WEIXIN.getKey(), deviceType.getKey(), deviceType.getValue());
            return fillPassportMsg(passport);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fail();
    }

    private JSONObject fillPassportMsg(Passport passport) {
        JSONObject response = new JSONObject();
        response.put("passportId", passport.getId());
        response.put("nickName", CommonUtils.nullToEmpty(passport.getShowName()));
        response.put("headImageUrl", CommonUtils.nullToEmpty(passport.getHeadImage()));
        response.put("sex", passport.getSex() == null ? GlobalAppointmentOptEnum.FEMALE.getKey() : passport.getSex());
        response.put("status", passport.getStatus());
        response.put("hasBinding", CommonUtils.isNotNullString(passport.getPhoneNumber()) ? GlobalAppointmentOptEnum.LOGIC_TRUE.getKey() : GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        response.put("bindingPhoneNumber", CommonUtils.hideChar(passport.getPhoneNumber(), 3, 8));
        response.put("accessToken", passport.getAccessToken());
        response.put("lastRefreshTime", CommonUtils.dateFormat(passport.getTokenInvalidTime()));

        return response;
    }

    private JSONObject getWeixinUserInfo(String openId, String accessToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";

        JSONObject response = JSONObject.parseObject(HttpUtils.get(url));
        logger.info("微信用户个人信息：" + response);
        int errorCode = response.getIntValue("errcode");
        if (errorCode != 0) {
            throw new XlibaoRuntimeException(errorCode, response.getString("errmsg"));
        }
        return response;
    }
}