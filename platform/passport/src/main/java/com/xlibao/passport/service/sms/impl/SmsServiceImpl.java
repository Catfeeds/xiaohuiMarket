package com.xlibao.passport.service.sms.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.*;
import com.xlibao.common.constant.sms.SmsCodeTypeEnum;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.passport.config.ConfigFactory;
import com.xlibao.passport.data.mapper.sms.SmsDataManager;
import com.xlibao.passport.data.model.PassportSmsLogger;
import com.xlibao.passport.service.sms.SmsService;
import com.xlibao.passport.service.sms.channel.RBPSMSMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chinahuangxc on 2017/2/7.
 */
@Transactional
@Service("smsService")
public class SmsServiceImpl extends BasicWebService implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Autowired
    private SmsDataManager smsDataManager;

    @Override
    public JSONObject sendSmsCode() {
        String phoneNumber = getUTF("phoneNumber");

        boolean isMobileNum = CommonUtils.isMobileNum(phoneNumber);
        if (!isMobileNum) {
            return fail("错误的手机号");
        }
        int type = getIntParameter("type", SmsCodeTypeEnum.REGISTER.getKey());
        SmsCodeTypeEnum smsTypeEnum = SmsCodeTypeEnum.getSmsCodeTypeEnum(type);
        if (smsTypeEnum == null) {
            return fail("无效的短信类型");
        }
        PassportSmsLogger smsLogger = smsDataManager.getSmsLogger(phoneNumber, type, GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        if (smsLogger != null) {
            if ((smsLogger.getCreateTime().getTime() + GlobalConstantConfig.SMS_CODE_DUPLICATE_PERIOD) > System.currentTimeMillis()) {
                return fail("1分钟内只能获取一次验证码");
            }
            // 状态 -- 失效
            smsDataManager.modifySmsStatus(smsLogger.getId(), (byte) (GlobalAppointmentOptEnum.LOGIC_TRUE.getKey() | GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()));
        }
        String smsCode = ConfigFactory.getPartner().isSmsOpen() ? DefineRandom.randomNumber(4) : "1111";
        sendSmsVerifyCode(phoneNumber, type, smsTypeEnum.getValue(), smsCode);

        logger.info(phoneNumber + "获取的验证码为：" + smsCode + "；验证码类型：" + type);
        return success(ConfigFactory.getPartner().isSmsOpen() ? "获取验证码成功" : "测试环境，验证码为：1111");
    }

    @Override
    public JSONObject verifySmsCode(String phone, String smsCode, int smsType) {
        PassportSmsLogger smsLogger = smsDataManager.getSmsLogger(phone, smsType, GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        if (smsLogger == null) {
            throw new XlibaoIllegalArgumentException("请先获取验证码");
        }
        if ((smsLogger.getCreateTime().getTime() + GlobalConstantConfig.SMS_CODE_TERM_OF_VALIDITY) < System.currentTimeMillis()) {
            // 状态 -- 失效
            smsDataManager.modifySmsStatus(smsLogger.getId(), (byte) (GlobalAppointmentOptEnum.LOGIC_TRUE.getKey() | GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()));
            throw new XlibaoIllegalArgumentException("验证码已过期，请重新获取！");
        }
        if (!smsCode.equals(smsLogger.getCode())) {
            throw new XlibaoIllegalArgumentException("验证码错误！");
        }
        // 状态 -- 已使用
        smsDataManager.modifySmsStatus(smsLogger.getId(), GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());

        return success();
    }

    @Override
    public void sendSmsVerifyCode(String phone, int type, String smsContent, String smsCode) {
        int result = smsDataManager.createSmsLogger(phone, type, GlobalAppointmentOptEnum.LOGIC_FALSE.getKey(), smsContent, CommonUtils.nullToEmpty(smsCode));
        if (result <= 0) {
            throw new XlibaoRuntimeException("获取验证码失败，请稍后重试！");
        }
        if (!ConfigFactory.getPartner().isSmsOpen()) {
            logger.info("没有开启短信验证码功能，不执行发送流程");
            return;
        }
        // 拼接短信内容
        smsContent = smsCode + smsContent;
        // 接入短信渠道
        try {
            RBPSMSMessageService.sendMessage(phone, smsContent);
        } catch (Exception ex) {
            logger.error("发送短信验证码失败发生异常", ex);
            throw new XlibaoRuntimeException("获取验证码失败，请稍后重试！");
        }
    }
}