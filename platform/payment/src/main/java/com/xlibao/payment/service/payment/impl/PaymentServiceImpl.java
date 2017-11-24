package com.xlibao.payment.service.payment.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.*;
import com.xlibao.common.constant.passport.ClientTypeEnum;
import com.xlibao.common.constant.payment.*;
import com.xlibao.common.constant.sms.SmsCodeTypeEnum;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.support.PassportRemoteService;
import com.xlibao.common.support.SharePaymentRemoteService;
import com.xlibao.metadata.passport.Passport;
import com.xlibao.payment.config.ConfigFactory;
import com.xlibao.payment.data.mapper.PaymentDataAccessManager;
import com.xlibao.payment.data.model.*;
import com.xlibao.payment.service.channel.alibaba.AlipayPayment;
import com.xlibao.payment.service.channel.tencent.TencentPayment;
import com.xlibao.payment.service.channel.xlibao.BalancePayment;
import com.xlibao.payment.service.currency.CurrencyEventListenerManager;
import com.xlibao.payment.service.currency.CurrencyPropertiesTypeKeyEnum;
import com.xlibao.payment.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Transactional
@Service("paymentService")
public class PaymentServiceImpl extends BasicWebService implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentDataAccessManager paymentDataAccessManager;
    @Autowired
    private TencentPayment tencentPayment;
    @Autowired
    private AlipayPayment alipayPayment;
    @Autowired
    private BalancePayment balancePayment;

    @Autowired
    private CurrencyEventListenerManager currencyEventListenerManager;

    @Override
    public JSONObject unifiedOrder() {
        PassportRemoteService.signatureSecurity(getHttpServletRequest());

        String partnerId = getUTF("partnerId");
        String appId = getUTF("appId");
        long passportId = getLongParameter("passportId");
        String paymentType = getUTF("paymentType", PaymentTypeEnum.BALANCE.getKey());
        int transType = getIntParameter("transType");
        String partnerUserId = getUTF("partnerUserId", "");
        String partnerTradeNumber = getUTF("partnerTradeNumber", "");
        long transUnitAmount = getLongParameter("transUnitAmount");
        int transNumber = getIntParameter("transNumber");
        long transTotalAmount = getLongParameter("transTotalAmount");
        String transTitle = getUTF("transTitle");
        String remark = getUTF("remark", "");
        byte useCoupon = getByteParameter("useCoupon", GlobalAppointmentOptEnum.LOGIC_FALSE.getKey());
        long discountAmount = getLongParameter("discountAmount", 0);
        String notifyFrontUrl = getUTF("notifyFrontUrl", "");
        String notifyUrl = getUTF("notifyUrl");
        String randomParameter = getUTF("randomParameter");
        String extendParameter = getUTF("extendParameter", "");

        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentType);
        if (paymentTypeEnum == null) {
            throw new XlibaoIllegalArgumentException("该支付类型暂未开通，请选择其他支付渠道！错误码：" + paymentType);
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("appId", appId);
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("paymentType", paymentType);
        parameters.put("transType", String.valueOf(transType));
        parameters.put("partnerId", partnerId);
        parameters.put("partnerUserId", partnerUserId);
        parameters.put("partnerTradeNumber", partnerTradeNumber);
        parameters.put("transUnitAmount", String.valueOf(transUnitAmount));
        parameters.put("transNumber", String.valueOf(transNumber));
        parameters.put("transTotalAmount", String.valueOf(transTotalAmount));
        parameters.put("transTitle", transTitle);
        parameters.put("remark", remark);
        parameters.put("notifyFrontUrl", notifyFrontUrl);
        parameters.put("notifyUrl", notifyUrl);
        parameters.put("randomParameter", randomParameter);
        parameters.put("extendParameter", extendParameter);
        parameters.put("useCoupon", String.valueOf(useCoupon));
        parameters.put("discountAmount", String.valueOf(discountAmount));

        String transSequenceNumber = uniquePrimaryKey();
        parameters.put("channelId", String.valueOf(paymentTypeEnum.getChannelId()));
        parameters.put("transSequenceNumber", "P" + transSequenceNumber);

        PaymentTransactionLogger transactionLogger = createPaymentTransactionLogger(parameters);
        logger.info(transSequenceNumber + "生成交易记录成功，交易ID：" + transactionLogger.getId());

        JSONObject response = beforePayment(paymentTypeEnum, transactionLogger, parameters);
        logger.info(transSequenceNumber + "生成支付参数，支付类型：" + paymentType + "，执行后内容：" + response.toString());
        return success(response);
    }

    @Override
    public JSONObject balancePayment() {
        /*
         * 支付参数 一般由应用服务器生成 用于验证该请求的合法性
         * 结构为：JSONObject -- {"partnerId" : "partnerId", "appId" : "appId", "prePaymentId" : "prePaymentId", "randomParameter" : "randomParameter", "timeStamp" : "timeStamp", "sign" : "sign"}
         */
        String paymentParameter = getUTF("paymentParameter");
        // 前端提供的参数 -- 通行证ID
        long passportId = getLongParameter("passportId");
        // 前端提供的参数 -- 支付密码
        String paymentPassword = getUTF("paymentPassword");

        JSONObject paymentData = JSONObject.parseObject(paymentParameter);

        String prePaymentId = paymentData.getString("prePaymentId");

        if (prePaymentId.length() <= 8) {
            return fail("错误的预支付ID，错误码：" + prePaymentId);
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("partnerId", paymentData.getString("partnerId"));
        parameters.put("appId", paymentData.getString("appId"));
        parameters.put("prePaymentId", prePaymentId);
        parameters.put("randomParameter", paymentData.getString("randomParameter"));
        parameters.put("timeStamp", paymentData.getString("timeStamp"));

        String signatureParameters = CommonUtils.parameterSort(parameters, new ArrayList<>());

        PassportRemoteService.signatureSecurity(paymentData.getString("partnerId"), paymentData.getString("appId"), paymentData.getString("sign"), signatureParameters);
        // 验证支付密码
        validatePassword(passportId, paymentPassword);
        // 扣取余额额度
        balancePayment.decreaseBalanceAmount(passportId, prePaymentId);
        return success("支付成功");
    }

    @Override
    public JSONObject offsetBalance() {
        PassportRemoteService.signatureSecurity(getHttpServletRequest());

        long passportId = getLongParameter("passportId");
        long offsetAmount = getLongParameter("offsetAmount");
        String transTitle = getUTF("transTitle");
        int transType = getIntParameter("transType");
        String transSequenceNumber = getUTF("transSequenceNumber");

        Passport passport = PassportRemoteService.getPassport(passportId);

        PaymentCurrencyAccount currencyAccount = paymentDataAccessManager.getCurrencyAccount(passportId, passport.getFromChannel(), CurrencyTypeEnum.BALANCE);
        int result = paymentDataAccessManager.offsetCurrencyAmount(passportId, currencyAccount.getChannelId(), currencyAccount.getCurrencyType(), offsetAmount);
        // 记录流水
        currencyEventListenerManager.notifyOffsetCurrencyAmount(passportId, currencyAccount.getChannelId().intValue(), CurrencyTypeEnum.BALANCE.getKey(), currencyAccount.getCurrentAmount(), offsetAmount,
                (currencyAccount.getCurrentAmount() + offsetAmount), PaymentTypeEnum.BALANCE.getKey(), transTitle, transType, transSequenceNumber);
        return result > 0 ? success() : fail();
    }

    @Override
    public JSONObject isSetPaymentPassword() {
        long passportId = getLongParameter("passportId");
        PaymentCurrencyProperties currencyProperties = paymentDataAccessManager.getCurrencyProperties(passportId, CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getType(), CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getKey());

        JSONObject response = new JSONObject();
        response.put("isSet", currencyProperties == null ? GlobalAppointmentOptEnum.LOGIC_FALSE.getKey() : GlobalAppointmentOptEnum.LOGIC_TRUE.getKey());

        return success(response);
    }

    @Override
    public JSONObject firstSetPaymentPassword() {
        long passportId = getLongParameter("passportId");

        String password = getUTF("password");
        String confirmPassword = getUTF("confirmPassword");

        if (!password.equals(confirmPassword)) {
            return fail("两次输入密码不一致");
        }
        PaymentCurrencyProperties currencyProperties = paymentDataAccessManager.getCurrencyProperties(passportId, CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getType(), CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getKey());
        if (currencyProperties != null) {
            return fail("您已设置了支付密码，若您忘记支付密码，请使用重置密码进行设置！");
        }
        password = encryptionPassword(password);
        try {
            int result = paymentDataAccessManager.createCurrencyProperties(passportId, CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getType(), CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getKey(), password);
            return result <= 0 ? fail("设置支付密码失败") : success("支付密码设置成功");
        } catch (Exception ex) {
            return fail("设置失败，请检查是否已设置了支付密码");
        }
    }

    @Override
    public JSONObject setPaymentPassword() {
        long passportId = getLongParameter("passportId");
        // String phone = getUTF("phone");
        String smsCode = getUTF("smsCode");
        // int smsType = getIntParameter("smsType");
        String paymentPassword = getUTF("paymentPassword");
        // 检查用户是否存在
        Passport passport = PassportRemoteService.getPassport(passportId);
        // 检查验证码是否正确
        PassportRemoteService.verifySmsCode(passport.getPhoneNumber(), smsCode, SmsCodeTypeEnum.MODIFY_PASSWORD.getKey());

        paymentPassword = encryptionPassword(paymentPassword);
        int result;
        try {
            result = paymentDataAccessManager.createCurrencyProperties(passportId, CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getType(), CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getKey(), paymentPassword);
        } catch (Exception ex) {
            result = paymentDataAccessManager.modifyCurrencyProperties(passportId, CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getType(), CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getKey(), paymentPassword);
        }
        return result <= 0 ? fail("设置支付密码失败") : success("支付密码设置成功");
    }

    @Override
    public JSONObject passportCurrency() {
        long passportId = getLongParameter("passportId");
        Passport passport = PassportRemoteService.getPassport(passportId);

        List<PaymentCurrencyAccount> currencyAccounts = paymentDataAccessManager.getCurrencyAccounts(passportId, passport.getFromChannel());

        JSONArray response = new JSONArray();

        for (PaymentCurrencyAccount currencyAccount : currencyAccounts) {
            JSONObject currencyMsg = new JSONObject();

            currencyMsg.put("id", currencyAccount.getId());
            currencyMsg.put("name", currencyAccount.getName());
            currencyMsg.put("channelId", currencyAccount.getChannelId());
            currencyMsg.put("type", currencyAccount.getCurrencyType());
            currencyMsg.put("currentAmount", currencyAccount.getCurrentAmount());
            currencyMsg.put("freezeAmount", currencyAccount.getFreezeAmount());

            response.add(currencyMsg);
        }
        return success(response);
    }

    @Override
    public JSONObject rechargeActivityTemplates() {
        long passportId = getLongParameter("passportId");
        int currencyType = getIntParameter("currencyType");

        List<PaymentRechargePresent> rechargePresents = paymentDataAccessManager.getRechargePresents(currencyType);

        Passport passport = PassportRemoteService.getPassport(passportId);

        PaymentCurrencyAccount currencyAccount = paymentDataAccessManager.getCurrencyAccount(passportId, passport.getFromChannel(), CurrencyTypeEnum.VIP_BALANCE);

        JSONObject response = new JSONObject();

        JSONArray rechargeArray = new JSONArray();
        for (PaymentRechargePresent rechargePresent : rechargePresents) {
            JSONObject data = new JSONObject();

            long minimumRechargeAmount = rechargePresent.getMinimumRechargeAmount();
            long presentAmount = rechargePresent.getPresentAmount();

            data.put("minimumRechargeAmount", minimumRechargeAmount);
            data.put("presentAmount", presentAmount);
            data.put("totalAmount", minimumRechargeAmount + presentAmount);

            rechargeArray.add(data);
        }
        response.put("rechargeArray", rechargeArray);
        response.put("vipBalance", currencyAccount.getCurrentAmount());

        return success(response);
    }

    @Override
    public JSONObject rechargeBalance() {
        long passportId = getLongParameter("passportId");
        String paymentType = getUTF("paymentType");
        int rechargeCurrencyType = getIntParameter("rechargeCurrencyType");
        long rechargeAmount = getLongParameter("rechargeAmount");

        Passport passport = PassportRemoteService.getPassport(passportId);

        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(paymentType);
        if (paymentTypeEnum == null) {
            return fail("该支付类型暂未开通，请选择其他支付渠道！错误码：" + paymentType);
        }
        if (paymentTypeEnum == PaymentTypeEnum.BALANCE && rechargeCurrencyType == CurrencyTypeEnum.BALANCE.getKey()) {
            return fail("对不起，不能使用相同类型的货币类型进行充值");
        }
        if (paymentTypeEnum == PaymentTypeEnum.VIP_BALANCE && rechargeCurrencyType == CurrencyTypeEnum.VIP_BALANCE.getKey()) {
            return fail("对不起，不能使用相同类型的货币类型进行充值");
        }
        String transSequenceNumber = uniquePrimaryKey();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("partnerId", ConfigFactory.getXlibaoConfig().getPartnerId());
        parameters.put("appId", ConfigFactory.getXlibaoConfig().getAppId());
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("paymentType", paymentType);
        parameters.put("transType", String.valueOf(TransTypeEnum.RECHARGE.getKey()));
        parameters.put("partnerName", passport.getDefaultName());
        parameters.put("partnerUserId", String.valueOf(passportId));
        parameters.put("partnerTradeNumber", transSequenceNumber);
        parameters.put("transUnitAmount", String.valueOf(rechargeAmount));
        parameters.put("transNumber", String.valueOf(1));
        parameters.put("transTotalAmount", String.valueOf(rechargeAmount));
        parameters.put("transTitle", PaymentTypeEnum.getPaymentTypeEnum(paymentType).getValue() + "充值");
        parameters.put("remark", "");
        parameters.put("extendParameter", String.valueOf(rechargeCurrencyType));
        parameters.put("useConpon", String.valueOf(GlobalAppointmentOptEnum.LOGIC_FALSE.getKey()));
        parameters.put("discountAmount", String.valueOf(0));

        parameters.put("channelId", String.valueOf(paymentTypeEnum.getChannelId()));
        parameters.put("transSequenceNumber", "T" + transSequenceNumber);

        PaymentTransactionLogger transactionLogger = createPaymentTransactionLogger(parameters);
        logger.info(transSequenceNumber + "生成充值记录成功，交易ID：" + transactionLogger.getId());

        JSONObject response = beforePayment(paymentTypeEnum, transactionLogger, parameters);

        if (PaymentTypeEnum.BALANCE.getKey().equals(paymentType) || PaymentTypeEnum.VIP_BALANCE.getKey().equals(paymentType)) {
            response = fillBalanceParameter(response.getString("prePaymentId"), DefineRandom.randomChar(32));
        }
        logger.info(transSequenceNumber + "生成充值参数，支付类型：" + paymentType + "，执行后内容：" + response.toString());
        return success(response);
    }

    @Override
    public JSONObject rechargeFlows() {
        long passportId = getLongParameter("passportId");
        int currencyType = getIntParameter("currencyType", 0);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        List<PaymentTransactionLogger> transactionLoggers = paymentDataAccessManager.getRechargeTransactionLoggers(passportId, String.valueOf(currencyType), pageStartIndex, pageSize);
        if (CommonUtils.isEmpty(transactionLoggers)) {
            return fail(2, "没有充值记录");
        }
        JSONArray response = new JSONArray();
        for (PaymentTransactionLogger transactionLogger : transactionLoggers) {
            JSONObject data = new JSONObject();
            data.put("transSequenceNumber", transactionLogger.getTransSequenceNumber());
            data.put("transType", transactionLogger.getPaymentType());
            data.put("title", transactionLogger.getTransTitle());
            data.put("time", CommonUtils.dateFormat(transactionLogger.getTransCreateTime().getTime()));
            data.put("transAmount", CommonUtils.formatAmount(transactionLogger.getTransUnitAmount()) + "元");
            data.put("presentAmount", CommonUtils.formatAmount(transactionLogger.getTransTotalAmount() - transactionLogger.getTransUnitAmount()) + "元");

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject rechargeDetail() {
        long passportId = getLongParameter("passportId");
        String transSequenceNumber = getUTF("transSequenceNumber");

        PaymentTransactionLogger transactionLogger = paymentDataAccessManager.getTransactionLogger(transSequenceNumber);
        if (transactionLogger == null) {
            return fail("流水记录不存在，错误序列码：" + transSequenceNumber);
        }
        if (transactionLogger.getPassportId() != passportId) {
            return fail("您没有权限查看该充值记录");
        }
        JSONObject response = new JSONObject();
        response.put("transAmount", transactionLogger.getTransUnitAmount());
        response.put("transSequenceNumber", transSequenceNumber);
        response.put("title", transactionLogger.getTransTitle());
        response.put("transType", PaymentTypeEnum.getPaymentTypeEnum(transactionLogger.getPaymentType()).getValue());
        response.put("transTime", CommonUtils.dateFormat(transactionLogger.getTransCreateTime().getTime()));
        return success(response);
    }

    @Override
    public JSONObject showAmountBalanceOfPayments() {
        long passportId = getLongParameter("passportId");
        int currencyType = getIntParameter("currencyType", CurrencyTypeEnum.BALANCE.getKey());
        int enterType = getIntParameter("enterType", BalanceOfPaymentsEnterTypeEnum.ALL.getKey());
        String date = getUTF("date", enterType == BalanceOfPaymentsEnterTypeEnum.DAY.getKey() ? CommonUtils.defineDateFormat(System.currentTimeMillis(), CommonUtils.Y_M_D) : CommonUtils.defineDateFormat(System.currentTimeMillis(), CommonUtils.Y_M));
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        List<PaymentCurrencyOffsetLogger> currencyOffsetLoggers = new ArrayList<>();
        if (enterType == BalanceOfPaymentsEnterTypeEnum.ALL.getKey()) {
            currencyOffsetLoggers = paymentDataAccessManager.getCurrencyOffsetLoggers(passportId, currencyType, pageStartIndex, pageSize);
        } else if (enterType == BalanceOfPaymentsEnterTypeEnum.DAY.getKey() || enterType == BalanceOfPaymentsEnterTypeEnum.MONTH.getKey()) {
            currencyOffsetLoggers = paymentDataAccessManager.getCurrencyOffsetLoggersForDate(passportId, currencyType, date, pageStartIndex, pageSize);
        }
        if (CommonUtils.isEmpty(currencyOffsetLoggers)) {
            return fail("已展示全部流水记录");
        }
        JSONObject response = new JSONObject();

        long maxTime = 0;
        long minTime = CommonUtils.dateFormatToLong("9999-12-31 23:59:59");

        JSONArray currencyArray = new JSONArray();
        for (PaymentCurrencyOffsetLogger currencyOffsetLogger : currencyOffsetLoggers) {
            if (currencyOffsetLogger.getCreateTime().getTime() > maxTime) {
                maxTime = CommonUtils.dateFormatToLong(CommonUtils.defineDateFormat(currencyOffsetLogger.getCreateTime().getTime(), CommonUtils.Y_M_D) + " 23:59:59");
            }
            if (currencyOffsetLogger.getCreateTime().getTime() < minTime) {
                minTime = CommonUtils.dateFormatToLong(CommonUtils.defineDateFormat(currencyOffsetLogger.getCreateTime().getTime(), CommonUtils.Y_M_D) + " 00:00:00");
            }
            JSONObject currencyData = fillCurrencyOffsetLoggerMsg(currencyOffsetLogger);

            currencyArray.add(currencyData);
        }
        List<Map<String, Object>> maps = paymentDataAccessManager.dailyAmountStatistics(passportId, currencyType, CommonUtils.dateFormat(minTime), CommonUtils.dateFormat(maxTime));

        JSONArray statisticsArray = new JSONArray();
        for (Map<String, Object> map : maps) {
            JSONObject data = new JSONObject();

            String time = (String) map.get("ymd");
            if (CommonUtils.isToday(CommonUtils.dateFormatToLong(time + " 00:00:00"))) {
                data.put("title", "今日");
            } else if (CommonUtils.isSameDay(CommonUtils.dateFormatToLong(time + " 00:00:00"), System.currentTimeMillis() - CommonUtils.DAY_MILLISECOND_TIME)) {
                data.put("title", "昨日");
            } else {
                data.put("title", time + " " + CommonUtils.dayOfWeekForTime(CommonUtils.dateFormatToLong(time + " 00:00:00")));
            }
            data.put("time", time);
            data.put("count", map.get("c"));
            data.put("totalAmount", map.get("s"));

            statisticsArray.add(data);
        }
        response.put("currencyArray", currencyArray);
        response.put("statisticsArray", statisticsArray);
        return success(response);
    }

    @Override
    public JSONObject balanceFlows() {
        long passportId = getLongParameter("passportId");
        int balanceType = getIntParameter("balanceType", BalanceTypeEnum.ALL.getKey());
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        BalanceTypeEnum balanceTypeEnum = BalanceTypeEnum.getBalanceTypeEnum(balanceType);
        if (balanceTypeEnum == null) {
            balanceType = BalanceTypeEnum.ALL.getKey();
        }
        List<PaymentCurrencyOffsetLogger> currencyOffsetLoggers = paymentDataAccessManager.getCurrencyOffsetLoggersForTransType(passportId, CurrencyTypeEnum.BALANCE.getKey(), PaymentTypeEnum.BALANCE.getKey(), balanceType, pageStartIndex, pageSize);
        if (CommonUtils.isEmpty(currencyOffsetLoggers)) {
            return fail(2, "已展示全部记录");
        }
        JSONArray currencyArray = new JSONArray();
        for (PaymentCurrencyOffsetLogger currencyOffsetLogger : currencyOffsetLoggers) {
            JSONObject currencyData = fillCurrencyOffsetLoggerMsg(currencyOffsetLogger);

            currencyArray.add(currencyData);
        }
        return success(currencyArray);
    }

    @Override
    public JSONObject drawCashFlows() {
        long passportId = getLongParameter("passportId");
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex(pageSize);

        List<PaymentTransactionLogger> transactionLoggers = paymentDataAccessManager.getTransactionLoggers(passportId, TransTypeEnum.DRAW_CASH.getKey(), pageStartIndex, pageSize);

        JSONArray response = new JSONArray();
        for (PaymentTransactionLogger transactionLogger : transactionLoggers) {
            JSONObject data = new JSONObject();

            data.put("id", transactionLogger.getId());
            data.put("transStatus", transactionLogger.getTransStatus());
            data.put("bankName", transactionLogger.getBankName());
            data.put("bankSimpleName", transactionLogger.getBankSimpleName());
            data.put("transAmount", transactionLogger.getTransTotalAmount());
            data.put("transTime", CommonUtils.dateFormat(transactionLogger.getCreateTime().getTime()));
            data.put("bankAccount", transactionLogger.getAccountNumber().substring(transactionLogger.getAccountNumber().length() - 4));

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject paymentDetail() {
        long passportId = getLongParameter("passportId");
        long currencyOffsetId = getLongParameter("currencyOffsetId");

        PaymentCurrencyOffsetLogger currencyOffsetLogger = paymentDataAccessManager.getCurrencyOffsetLogger(currencyOffsetId);
        if (currencyOffsetLogger == null) {
            return fail("流水记录存在，错误码：" + currencyOffsetId);
        }
        if (currencyOffsetLogger.getPassportId() != passportId) {
            return fail("您没有权限查看该流水记录");
        }
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(currencyOffsetLogger.getTransType());

        JSONObject response = new JSONObject();
        response.put("transAmount", currencyOffsetLogger.getOffsetAmount());
        response.put("transSequenceNumber", currencyOffsetLogger.getRelationTransSequence());
        response.put("title", currencyOffsetLogger.getTransTitle());
        response.put("transType", paymentTypeEnum.getValue());
        response.put("transTime", CommonUtils.dateFormat(currencyOffsetLogger.getCreateTime().getTime()));
        return success(response);
    }

    @Override
    public JSONObject incomeStatistics() {
        long passportId = getLongParameter("passportId");
        String date = CommonUtils.defineDateFormat(System.currentTimeMillis(), CommonUtils.Y_M_D);

        long todayTotalAmount = paymentDataAccessManager.incomeForDate(passportId, CurrencyTypeEnum.BALANCE.getKey(), date);

        date = CommonUtils.defineDateFormat(System.currentTimeMillis(), CommonUtils.Y_M);

        long monthTotalAmount = paymentDataAccessManager.incomeForDate(passportId, CurrencyTypeEnum.BALANCE.getKey(), date);

        JSONObject response = new JSONObject();
        response.put("todayTotalAmount", todayTotalAmount);
        response.put("monthTotalAmount", monthTotalAmount);
        return success(response);
    }

    @Override
    public JSONObject showBankTemplates() {
        List<PaymentBankTemplate> bankTemplates = paymentDataAccessManager.showBankTemplates();

        JSONArray response = new JSONArray();
        for (PaymentBankTemplate bankTemplate : bankTemplates) {
            JSONObject data = new JSONObject();

            data.put("id", bankTemplate.getId());
            data.put("name", bankTemplate.getName());
            data.put("logo", bankTemplate.getLogo());
            data.put("simpleCode", bankTemplate.getSimpleCode());
            data.put("bankCode", bankTemplate.getBankCode());

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject bindBankAccount() {
        long passportId = getLongParameter("passportId");
        long bankTemplateId = getLongParameter("bankTemplateId");

        String bankAccount = getUTF("bankAccount");
        String accountName = getUTF("accountName");
        String reservePhone = getUTF("reservePhone");
        String branchName = getUTF("branchName", "");

        if (!CommonUtils.isMobileNum(reservePhone)) {
            return fail("手机号[" + reservePhone + "]不正确");
        }
        bankAccount = bankAccount.replaceAll(CommonUtils.SPACE, "");
        if (!CommonUtils.luhn(bankAccount)) {
            return fail(2, "您填写的银行卡号不正确");
        }
        PaymentBank paymentBank = paymentDataAccessManager.getBank(bankAccount);
        if (paymentBank != null) {
            if (paymentBank.getStatus() != BankStatusEnum.INVALID.getKey()) {
                return fail("您提供的卡号已被使用(" + bankAccount + ")");
            }
            // 更新信息
            paymentDataAccessManager.updateBankData(paymentBank.getId(), passportId, bankTemplateId, bankAccount, accountName, reservePhone, branchName, BankStatusEnum.EXAMINE.getKey());
            return success("绑定银行卡成功，请等待审核！");
        }
        paymentBank = new PaymentBank();
        paymentBank.setPassportId(passportId);
        paymentBank.setBankTemplateId(bankTemplateId);
        paymentBank.setBankAccount(bankAccount);
        paymentBank.setAccountName(accountName);
        paymentBank.setReservePhone(reservePhone);
        paymentBank.setBranchName(branchName);
        paymentBank.setStatus(BankStatusEnum.EXAMINE.getKey());

        paymentDataAccessManager.createPaymentBank(paymentBank);
        return success("绑定银行卡成功，请等待审核！");
    }

    @Override
    public JSONObject showDrawCashMode() {
        int roleType = getIntParameter("roleType");
        List<PaymentTakeRule> paymentTakeRules = paymentDataAccessManager.getTakeRules(roleType);

        JSONArray response = new JSONArray();
        for (PaymentTakeRule takeRule : paymentTakeRules) {
            JSONObject data = new JSONObject();
            data.put("mode", takeRule.getTakeMode());
            data.put("name", takeRule.getTakeDesc());
            data.put("lowCost", takeRule.getLowCost());
            data.put("highCost", takeRule.getHighCost());
            data.put("rate", takeRule.getRate());
            data.put("showImage", takeRule.getShowImage());
            data.put("defaultOption", takeRule.getDefaultOption());

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject beforeTakeCash() {
        long passportId = getLongParameter("passportId");
        int mode = getIntParameter("mode");
        int roleType = getIntParameter("roleType", ClientTypeEnum.CONSUMER.getKey());

        Passport passport = PassportRemoteService.getPassport(passportId);
        PaymentTakeRule takeCashRule = paymentDataAccessManager.getTakeRule(mode, roleType);
        if (takeCashRule == null) {
            return fail("非法的提现方式，错误码：" + mode);
        }
        PaymentCurrencyAccount currencyAccount = paymentDataAccessManager.getCurrencyAccount(passportId, passport.getFromChannel(), CurrencyTypeEnum.BALANCE);
        // price[0] -- 为今次可提现的最大额度  price[1] -- 为今次要扣取的服务费  price[2] -- 费率
        double[] price = calculationCommissionAmount(currencyAccount.getCurrentAmount(), takeCashRule);
        if (currencyAccount.getCurrentAmount() < price[1]) {
            return fail("您的余额不足支付本次提现的服务费，请选择其他到账方式");
        }
        if (price[0] == currencyAccount.getCurrentAmount()) {
            // 如果可提现的最大额度为用户余额 那么重设可提现的最大额度
            price[0] = currencyAccount.getCurrentAmount() - price[1];
        }
        // 当天提现次数
        int size = paymentDataAccessManager.getTransactionLoggerSize(passportId, TransTypeEnum.DRAW_CASH.getKey(), CommonUtils.todayFormat());

        JSONObject response = new JSONObject();
        response.put("rateValue", "提现服务费率" + CommonUtils.unRounding((price[2] * 100)) + "%");
        response.put("maxTakeCash", "本次最多可提现：" + CommonUtils.formatNumber(price[0] / 100f, "#.##") + "元");
        response.put("maxTakeCashValue", (long) price[0]);
        response.put("remainTakeCashCount", size > 0 ? 0 : 1);
        if (size > 0) {
            response.put("tip", "今天剩余提现次数为0次");
            response.put("tipMode", 1);
        } else {
            response.put("tip", "今天剩余提现次数为" + 1 + "次，是否确认提现？");
            response.put("tipMode", 2);
        }
        return success(response);
    }

    @Override
    public JSONObject drawCash() {
        long passportId = getLongParameter("passportId");
        String paymentPassword = getUTF("paymentPassword");
        String smsCode = getUTF("smsCode");
        long bankId = getLongParameter("bankId");
        long amount = getLongParameter("amount");
        int mode = getIntParameter("mode", DrawCashModeEnum.TWO_HOURS.getKey());
        int roleType = getIntParameter("roleType", ClientTypeEnum.CONSUMER.getKey());

        Passport passport = PassportRemoteService.getPassport(passportId);
        // 验证支付密码
        validatePassword(passportId, paymentPassword);

        int size = paymentDataAccessManager.getTransactionLoggerSize(passportId, TransTypeEnum.DRAW_CASH.getKey(), CommonUtils.todayFormat());
        if (size > 0) {
            return fail("每天只能提现一次");
        }
        PaymentTakeRule takeCashRule = paymentDataAccessManager.getTakeRule(mode, roleType);
        if (takeCashRule == null) {
            return fail("非法的提现方式，错误码：" + mode);
        }
        double[] prices = calculationCommissionAmount(amount, takeCashRule);

        amount += prices[1];
        PaymentCurrencyAccount currencyAccount = paymentDataAccessManager.getCurrencyAccount(passportId, passport.getFromChannel(), CurrencyTypeEnum.BALANCE);
        if (currencyAccount.getCurrentAmount() < amount) {
            return fail("您的余额不足");
        }
        if ((currencyAccount.getTotalIntoAmount() < (currencyAccount.getTotalOutputAmount() + amount))) {
            logger.error("资金安全保护，出现该异常时，请及时检查用户数据；总收入：" + currencyAccount.getTotalIntoAmount() + "，总支出：" + currencyAccount.getTotalOutputAmount() + "，本次提现：" + amount);
            return fail("您的账户资金数据异常，请联系服务人员");
        }
        PaymentBank bank = paymentDataAccessManager.getBank(bankId);
        if (bank == null) {
            logger.error("银行卡安全保护，出现该异常时，请及时检查用户数据；银行卡不存在，ID：" + bankId);
            return fail("银行卡不存在，请检查！");
        }
        if (bank.getStatus() == BankStatusEnum.INVALID.getKey()) {
            logger.error("银行卡安全保护，出现该异常时，请及时检查用户数据；银行卡失效：" + bankId);
            return fail("银行卡已失效，请选择其他银行卡！");
        }
        if (bank.getStatus() == BankStatusEnum.EXAMINE.getKey()) {
            logger.error("银行卡安全保护，出现该异常时，请及时检查用户数据；银行卡未通过审核：" + bankId);
            return fail("该卡未通过审核，暂时不能使用，请选择其他银行卡！");
        }
        if (bank.getPassportId() != passportId) {
            logger.error("【严重】银行卡安全保护，出现该异常时，请及时检查用户数据；银行卡归属出错，提现用户：" + passportId + "，卡绑定用户：" + bank.getPassportId());
            return fail("您没有权限提款到该银行卡");
        }
        // 检查验证码是否正确
        PassportRemoteService.verifySmsCode(passport.getPhoneNumber(), smsCode, SmsCodeTypeEnum.DRAW_CASH.getKey());

        PaymentBankTemplate bankTemplate = paymentDataAccessManager.getBankTemplate(bank.getBankTemplateId());
        // 生成交易流水
        PaymentTransactionLogger transactionLogger = createDrawCashTransactionLogger(passport, bankTemplate, bank, amount, mode);

        amount = -Math.abs(amount);
        int result = paymentDataAccessManager.offsetCurrencyAmount(passportId, passport.getFromChannel(), CurrencyTypeEnum.BALANCE.getKey(), amount);
        if (result <= 0) {
            throw new XlibaoRuntimeException("扣除余额失败，请检查是否余额不足！");
        }
        currencyEventListenerManager.notifyOffsetCurrencyAmount(transactionLogger.getPassportId(), transactionLogger.getChannelId(), CurrencyTypeEnum.BALANCE.getKey(), currencyAccount.getCurrentAmount(),
                amount, (currencyAccount.getCurrentAmount() + amount), transactionLogger.getPaymentType(), transactionLogger.getTransTitle(), transactionLogger.getTransType(), transactionLogger.getTransSequenceNumber());

        return success("提现成功");
    }

    @Override
    public JSONObject showBanks() {
        long passportId = getLongParameter("passportId");

        List<PaymentBank> banks = paymentDataAccessManager.getBanks(passportId);
        if (CommonUtils.isEmpty(banks)) {
            return fail(2, "您未绑定银行卡");
        }
        StringBuilder bankTemplateSet = new StringBuilder();
        for (PaymentBank bank : banks) {
            bankTemplateSet.append(bank.getBankTemplateId()).append(CommonUtils.SPLIT_COMMA);
        }
        bankTemplateSet.deleteCharAt(bankTemplateSet.length() - 1);
        List<PaymentBankTemplate> bankTemplates = paymentDataAccessManager.getBankTemplates(bankTemplateSet.toString());
        if (CommonUtils.isEmpty(bankTemplates)) {
            return fail(3, "您未绑定银行卡");
        }
        Map<Long, PaymentBankTemplate> bankTemplateMap = new HashMap<>();
        for (PaymentBankTemplate bankTemplate : bankTemplates) {
            bankTemplateMap.put(bankTemplate.getId(), bankTemplate);
        }
        JSONArray response = new JSONArray();
        for (PaymentBank bank : banks) {
            PaymentBankTemplate bankTemplate = bankTemplateMap.get(bank.getBankTemplateId());
            if (bankTemplate == null) {
                continue;
            }
            JSONObject data = new JSONObject();
            data.put("bankId", bank.getId());
            data.put("bankTemplateId", bank.getBankTemplateId());
            data.put("bankName", bankTemplate.getName());
            data.put("accountType", bank.getAccountType());
            data.put("bankAccount", CommonUtils.hideChar(bank.getBankAccount(), 4, 8));
            data.put("accountName", bank.getAccountName());
            data.put("logo", bankTemplate.getLogo());
            data.put("status", bank.getStatus());
            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject setDefaultBank() {
        long passportId = getLongParameter("passportId");
        long bankId = getLongParameter("bankId");

        PaymentBank bank = paymentDataAccessManager.getBank(bankId);
        if (bank == null || bank.getStatus() == BankStatusEnum.INVALID.getKey()) {
            return fail("该绑定关系已失效");
        }
        if (bank.getPassportId() != passportId) {
            return fail("该银行卡不归属于您的帐号");
        }
        if (bank.getStatus() == BankStatusEnum.EXAMINE.getKey()) {
            return fail("该卡未通过审核，请耐心等待或联系我司服务人员");
        }
        // 不论是否存在其他绑定关系，设置默认的银行卡为普通状态
        paymentDataAccessManager.updateBanksStatus(passportId, BankStatusEnum.NORMAL.getKey(), BankStatusEnum.DEFAULT.getKey());
        paymentDataAccessManager.setDefault(bankId, BankStatusEnum.DEFAULT.getKey());
        return success();
    }

    @Override
    public JSONObject refund() {
        // 签名验证
        PassportRemoteService.signatureSecurity(getHttpServletRequest());

        String partnerSequenceNumber = getUTF("partnerSequenceNumber");

        PaymentTransactionLogger transactionLogger = paymentDataAccessManager.getTransactionLoggerForPartnerTradeNumber(partnerSequenceNumber);
        if (transactionLogger == null) {
            return fail("未存在交易记录，错误码：" + partnerSequenceNumber);
        }
        if ((transactionLogger.getTransStatus() & TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) != TransStatusEnum.TRADE_SUCCESS_SERVER.getKey()) {
            logger.error("异常操作，请求退款的订单并未成功支付，合作伙伴单号：" + partnerSequenceNumber + "；交易订单号：" + transactionLogger.getTransSequenceNumber());
            return fail("未完成支付的订单不能进行退款操作");
        }
        if (transactionLogger.getRefundStatus() == GlobalAppointmentOptEnum.LOGIC_TRUE.getKey()) {
            return success("该交易已退款成功，请留意发起渠道(微信或支付宝等)的退款进度");
        }
        if (transactionLogger.getPaymentType().equals(PaymentTypeEnum.WEIXIN_APP.getKey()) || transactionLogger.getPaymentType().equals(PaymentTypeEnum.WEIXIN_NATIVE.getKey())
                || transactionLogger.getPaymentType().equals(PaymentTypeEnum.WEIXIN_JS.getKey()) || transactionLogger.getPaymentType().equals(PaymentTypeEnum.WEIXIN_APPLET.getKey())) {
            return tencentPayment.refund(transactionLogger);
        }
        return success();
    }

    private double[] calculationCommissionAmount(long amount, PaymentTakeRule takeCashRule) {
        // 配置表中的费率为万份比
        double rate = takeCashRule.getRate() / 10000f;
        // 本次提现额度可能需要支付的手续费
        long commissionAmount = (long) (amount * rate);
        if (commissionAmount < takeCashRule.getLowCost()) {
            // 最低限制
            commissionAmount = takeCashRule.getLowCost();
        }
        if (commissionAmount > takeCashRule.getHighCost()) {
            // 最高限制
            commissionAmount = takeCashRule.getHighCost();
        }
        // 只取小数点后两位 非四舍五入
        long maxTakeCash = amount - commissionAmount;
        if (maxTakeCash > takeCashRule.getSingleAmountLimit()) {
            // 最多只能提现的额度
            maxTakeCash = takeCashRule.getSingleAmountLimit();
        }
        return new double[]{maxTakeCash, commissionAmount, rate};
    }

    private JSONObject fillCurrencyOffsetLoggerMsg(PaymentCurrencyOffsetLogger currencyOffsetLogger) {
        JSONObject response = new JSONObject();

        response.put("id", currencyOffsetLogger.getId());
        response.put("title", currencyOffsetLogger.getTransTitle());
        response.put("time", CommonUtils.dateFormat(currencyOffsetLogger.getCreateTime().getTime()));
        response.put("amount", currencyOffsetLogger.getOffsetAmount());
        response.put("transType", currencyOffsetLogger.getTransType());
        response.put("transSequence", currencyOffsetLogger.getRelationTransSequence());

        return response;
    }

    private JSONObject beforePayment(PaymentTypeEnum paymentTypeEnum, PaymentTransactionLogger transactionLogger, Map<String, String> parameters) {
        String function = "支付";
        switch (paymentTypeEnum) {
            case ALIPAY: {
                String paymentURL = alipayPayment.generationEncryptionPaymentURL(parameters.get("transSequenceNumber"), function, parameters.get("partnerName"), CommonUtils.formatNumber(Math.abs(Long.parseLong(parameters.get("transTotalAmount"))) / 100d, "#.##"));
                JSONObject response = new JSONObject();
                response.put("paymentURL", paymentURL);
                return response;
            }
            case WEIXIN_NATIVE: {
                return tencentPayment.weixinNativePaymentParameters(parameters.get("transSequenceNumber"), Long.parseLong(parameters.get("transTotalAmount")), function, parameters.get("partnerName"), parameters.get("extendParameter"), GlobalConstantConfig.LOCAL_IP_ADDRESS);
            }
            case WEIXIN_APP: {
                return tencentPayment.weixinAppPaymentParameters(parameters.get("transSequenceNumber"), Long.parseLong(parameters.get("transTotalAmount")), function, parameters.get("partnerName"), parameters.get("extendParameter"), GlobalConstantConfig.LOCAL_IP_ADDRESS);
            }
            case WEIXIN_JS: {
                return tencentPayment.weixinJSPaymentParameters(transactionLogger.getPartnerUserId(), parameters.get("transSequenceNumber"), Long.parseLong(parameters.get("transTotalAmount")), function, parameters.get("partnerName"), parameters.get("extendParameter"), GlobalConstantConfig.LOCAL_IP_ADDRESS);
            }
            case WEIXIN_APPLET: {
                return tencentPayment.weixinAppletPaymentParameters(transactionLogger.getPartnerUserId(), parameters.get("transSequenceNumber"), Long.parseLong(parameters.get("transTotalAmount")), function, parameters.get("partnerName"), parameters.get("extendParameter"), GlobalConstantConfig.LOCAL_IP_ADDRESS);
            }
            case BALANCE: {
                return balancePayment.prePayment(transactionLogger);
            }
            default:
        }
        throw new XlibaoIllegalArgumentException("错误的支付类型");
    }

    private PaymentTransactionLogger createDrawCashTransactionLogger(Passport passport, PaymentBankTemplate bankTemplate, PaymentBank bank, long amount, int mode) {
        PaymentTransactionLogger transactionLogger = new PaymentTransactionLogger();

        String transSequenceNumber = "D" + uniquePrimaryKey();
        transactionLogger.setTransSequenceNumber(transSequenceNumber);
        transactionLogger.setPassportId(passport.getId());
        transactionLogger.setPaymentType(PaymentTypeEnum.DRAW_CASH.getKey());
        transactionLogger.setTransType(TransTypeEnum.DRAW_CASH.getKey());
        transactionLogger.setPartnerId(ConfigFactory.getXlibaoConfig().getPartnerId());
        transactionLogger.setAppId(ConfigFactory.getXlibaoConfig().getAppId());
        transactionLogger.setPartnerUserId(String.valueOf(passport.getId()));
        transactionLogger.setPartnerTradeNumber(transSequenceNumber);
        transactionLogger.setChannelId(PaymentTypeEnum.DRAW_CASH.getChannelId());
        transactionLogger.setTransUnitAmount(amount);
        transactionLogger.setTransNumber(1);
        transactionLogger.setTransTotalAmount(amount);
        transactionLogger.setTransTitle(PaymentTypeEnum.DRAW_CASH.getValue());
        transactionLogger.setAccountNumber(bank.getBankAccount());
        transactionLogger.setAccountName(bank.getAccountName());
        transactionLogger.setAccountType(bank.getAccountType());
        transactionLogger.setCurrencyType("RMB");
        transactionLogger.setBankName(bankTemplate.getName());
        transactionLogger.setBankBranchCode(bankTemplate.getBankCode());
        transactionLogger.setBankSimpleName(bankTemplate.getSimpleCode());
        transactionLogger.setRemark(String.valueOf(mode));

        // 数据库操作
        paymentDataAccessManager.createPaymentTransactionLogger(transactionLogger);
        return transactionLogger;
    }

    private PaymentTransactionLogger createPaymentTransactionLogger(Map<String, String> parameters) {
        PaymentTransactionLogger transactionLogger = new PaymentTransactionLogger();

        transactionLogger.setTransSequenceNumber(parameters.get("transSequenceNumber"));
        transactionLogger.setPassportId(Long.parseLong(parameters.get("passportId")));
        transactionLogger.setPaymentType(parameters.get("paymentType"));
        transactionLogger.setTransType(Integer.parseInt(parameters.get("transType")));
        transactionLogger.setCurrencyType(parameters.get("extendParameter"));
        transactionLogger.setPartnerId(parameters.get("partnerId"));
        transactionLogger.setAppId(parameters.get("appId"));
        transactionLogger.setPartnerUserId(parameters.get("partnerUserId"));
        transactionLogger.setPartnerTradeNumber(parameters.get("partnerTradeNumber"));
        transactionLogger.setChannelId(Integer.parseInt(parameters.get("channelId")));
        transactionLogger.setTransUnitAmount(Long.parseLong(parameters.get("transUnitAmount")));
        transactionLogger.setTransNumber(Integer.parseInt(parameters.get("transNumber")));
        transactionLogger.setTransTotalAmount(Long.parseLong(parameters.get("transTotalAmount")));
        transactionLogger.setTransTitle(parameters.get("transTitle"));
        transactionLogger.setRemark(parameters.get("remark"));
        transactionLogger.setUseCoupon(Byte.parseByte(parameters.get("useCoupon")));
        transactionLogger.setDiscountAmount(Long.parseLong(parameters.get("discountAmount")));
        transactionLogger.setNotifyFrontUrl(parameters.get("notifyFrontUrl"));
        transactionLogger.setNotifyUrl(parameters.get("notifyUrl"));
        transactionLogger.setExtendParameter(parameters.get("extendParameter"));

        // 数据库操作
        paymentDataAccessManager.createPaymentTransactionLogger(transactionLogger);
        return transactionLogger;
    }

    private void validatePassword(long passportId, String password) {
        PaymentCurrencyProperties currencyProperties = paymentDataAccessManager.getCurrencyProperties(passportId, CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getType(), CurrencyPropertiesTypeKeyEnum.PAYMENT_PASSWORD.getKey());
        if (currencyProperties == null) {
            throw new XlibaoRuntimeException(100, "请先设置支付密码！");
        }

        String encryptionPassword = encryptionPassword(password);
        if (!encryptionPassword.equals(currencyProperties.getV())) {
            throw new XlibaoIllegalArgumentException("支付密码错误，请重试");
        }
    }

    private static JSONObject fillBalanceParameter(String prePaymentId, String randomParameter) {
        return SharePaymentRemoteService.fillBalanceParameter(ConfigFactory.getXlibaoConfig().getPartnerId(), ConfigFactory.getXlibaoConfig().getAppId(), ConfigFactory.getXlibaoConfig().getAppKey(), prePaymentId, randomParameter);
    }
}