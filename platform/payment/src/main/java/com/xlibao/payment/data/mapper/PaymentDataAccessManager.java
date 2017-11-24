package com.xlibao.payment.data.mapper;

import com.xlibao.common.CommonUtils;
import com.xlibao.common.constant.payment.CurrencyTypeEnum;
import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import com.xlibao.payment.data.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Component
public class PaymentDataAccessManager {

    @Autowired
    private PaymentCurrencyAccountMapper currencyAccountMapper;
    @Autowired
    private PaymentCurrencyOffsetLoggerMapper currencyOffsetLoggerMapper;
    @Autowired
    private PaymentCurrencyPropertiesMapper currencyPropertiesMapper;
    @Autowired
    private PaymentPartnerPropertiesMapper partnerPropertiesMapper;
    @Autowired
    private PaymentTransactionLoggerMapper transactionLoggerMapper;
    @Autowired
    private PaymentTransactionPropertiesMapper transactionPropertiesMapper;
    @Autowired
    private PaymentTakeRuleMapper takeRuleMapper;
    @Autowired
    private PaymentRechargePresentMapper rechargePresentMapper;
    @Autowired
    private PaymentBankMapper bankMapper;
    @Autowired
    private PaymentBankTemplateMapper bankTemplateMapper;

    public PaymentPartnerProperties getPartnerProperties(long pid, String appId) {
        return partnerPropertiesMapper.getPartnerProperties(pid, appId);
    }

    public int createPaymentTransactionLogger(PaymentTransactionLogger transactionLogger) {
        return transactionLoggerMapper.createPaymentTransactionLogger(transactionLogger);
    }

    public int getTransactionLoggerSize(long passportId, int transType, String dateTime) {
        return transactionLoggerMapper.getTransactionLoggerSize(passportId, transType, dateTime);
    }

    public List<PaymentTransactionLogger> getTransactionLoggers(long passportId, int transType, int pageStartIndex, int pageSize) {
        return transactionLoggerMapper.getTransactionLoggers(passportId, transType, pageStartIndex, pageSize);
    }

    public List<PaymentTransactionLogger> getRechargeTransactionLoggers(long passportId, String currencyType, int pageStartIndex, int pageSize) {
        return transactionLoggerMapper.getRechargeTransactionLoggers(passportId, TransTypeEnum.RECHARGE.getKey(), currencyType, TransStatusEnum.TRADE_SUCCESS_SERVER.getKey(), pageStartIndex, pageSize);
    }

    public int updateRefundParameter(PaymentTransactionLogger transactionLogger) {
        return transactionLoggerMapper.updateRefundParameter(transactionLogger);
    }

    public int createTransactionProperties(String transSequenceNumber, int type, String key, String value) {
        PaymentTransactionProperties transactionProperties = new PaymentTransactionProperties();
        transactionProperties.setTransSequenceNumber(transSequenceNumber);
        transactionProperties.setType(type);
        transactionProperties.setK(key);
        transactionProperties.setV(value);

        return transactionPropertiesMapper.createTransactionProperties(transactionProperties);
    }

    public PaymentCurrencyAccount createCurrencyAccount(long passportId, long channelId, CurrencyTypeEnum currencyType) {
        PaymentCurrencyAccount currencyAccount = new PaymentCurrencyAccount();

        currencyAccount.setPassportId(passportId);
        currencyAccount.setChannelId(channelId);
        currencyAccount.setCurrencyType(currencyType.getKey());
        currencyAccount.setName(currencyType.getValue());

        currencyAccountMapper.createCurrencyAccount(currencyAccount);

        return currencyAccount;
    }

    public PaymentCurrencyAccount getCurrencyAccount(long passportId, long channelId, CurrencyTypeEnum currencyType) {
        PaymentCurrencyAccount currencyAccount = currencyAccountMapper.getCurrencyAccount(passportId, channelId, currencyType.getKey());

        if (currencyAccount == null) {
            currencyAccount = createCurrencyAccount(passportId, channelId, currencyType);
        }
        return currencyAccount;
    }

    public List<PaymentCurrencyAccount> getCurrencyAccounts(long passportId, long channelId) {
        List<PaymentCurrencyAccount> currencyAccounts = currencyAccountMapper.getCurrencyAccounts(passportId, channelId);

        CurrencyTypeEnum[] currencyTypeEnums = CurrencyTypeEnum.values();

        if (CommonUtils.isEmpty(currencyAccounts)) {
            currencyAccounts = new ArrayList<>();
        }

        Map<Integer, PaymentCurrencyAccount> currencyAccountMap = new HashMap<>();
        for (PaymentCurrencyAccount currencyAccount : currencyAccounts) {
            currencyAccountMap.put(currencyAccount.getCurrencyType(), currencyAccount);
        }
        for (CurrencyTypeEnum currencyType : currencyTypeEnums) {
            PaymentCurrencyAccount currencyAccount = currencyAccountMap.get(currencyType.getKey());
            if (currencyAccount == null) {
                currencyAccount = createCurrencyAccount(passportId, channelId, currencyType);
                currencyAccounts.add(currencyAccount);
            }
        }
        return currencyAccounts;
    }

    public int offsetCurrencyAmount(long passportId, long channelId, int currencyType, long offsetAmount) {
        return currencyAccountMapper.offsetCurrencyAmount(passportId, channelId, currencyType, offsetAmount);
    }

    public int createCurrencyProperties(long passportId, int type, String k, String v) {
        PaymentCurrencyProperties currencyProperties = new PaymentCurrencyProperties();
        currencyProperties.setPassportId(passportId);
        currencyProperties.setType(type);
        currencyProperties.setK(k);
        currencyProperties.setV(v);

        return currencyPropertiesMapper.createCurrencyProperties(currencyProperties);
    }

    public PaymentCurrencyProperties getCurrencyProperties(long passportId, int type, String key) {
        return currencyPropertiesMapper.getCurrencyProperties(passportId, type, key);
    }

    public int modifyCurrencyProperties(long passportId, int type, String key, String value) {
        return currencyPropertiesMapper.modifyCurrencyProperties(passportId, type, key, value);
    }

    public int createCurrencyOffsetLogger(PaymentCurrencyOffsetLogger currencyOffsetLogger) {
        return currencyOffsetLoggerMapper.createCurrencyOffsetLogger(currencyOffsetLogger);
    }

    public PaymentCurrencyOffsetLogger getCurrencyOffsetLogger(long id) {
        return currencyOffsetLoggerMapper.getCurrencyOffsetLogger(id);
    }

    public List<PaymentCurrencyOffsetLogger> getCurrencyOffsetLoggers(long passportId, int currencyType, int pageStartIndex, int pageSize) {
        return currencyOffsetLoggerMapper.getCurrencyOffsetLoggers(passportId, currencyType, pageStartIndex, pageSize);
    }

    public List<PaymentCurrencyOffsetLogger> getCurrencyOffsetLoggersForTransType(long passportId, int currencyType, String transType, int balanceType, int pageStartIndex, int pageSize) {
        return currencyOffsetLoggerMapper.getCurrencyOffsetLoggersForTransType(passportId, currencyType, transType, balanceType, pageStartIndex, pageSize);
    }

    public List<PaymentCurrencyOffsetLogger> getCurrencyOffsetLoggersForDate(long passportId, int currencyType, String dateTime, int pageStartIndex, int pageSize) {
        return currencyOffsetLoggerMapper.getCurrencyOffsetLoggersForDate(passportId, currencyType, dateTime, pageStartIndex, pageSize);
    }

    public long incomeForDate(long passportId, int currencyType, String date) {
        return currencyOffsetLoggerMapper.incomeForDate(passportId, currencyType, date);
    }

    public List<Map<String, Object>> dailyAmountStatistics(long passportId, int currencyType, String beginTime, String endTime) {
        return currencyOffsetLoggerMapper.dailyAmountStatistics(passportId, currencyType, beginTime, endTime);
    }

    public PaymentTransactionLogger getTransactionLogger(String transSequenceNumber) {
        return transactionLoggerMapper.getTransactionLogger(transSequenceNumber);
    }

    public PaymentTransactionLogger getTransactionLoggerForPartnerTradeNumber(String partnerTradeNumber) {
        return transactionLoggerMapper.getTransactionLoggerForPartnerTradeNumber(partnerTradeNumber);
    }

    public PaymentTransactionLogger getTransactionLoggerById(long tid) {
        return transactionLoggerMapper.getTransactionLoggerById(tid);
    }

    public int modifyTransactionStatus(PaymentTransactionLogger transactionLogger) {
        return transactionLoggerMapper.modifyTransactionStatus(transactionLogger);
    }

    public PaymentBankTemplate getBankTemplate(long bankTemplateId) {
        return bankTemplateMapper.getBankTemplate(bankTemplateId);
    }

    public List<PaymentBankTemplate> showBankTemplates() {
        return bankTemplateMapper.showBankTemplates();
    }

    public List<PaymentBankTemplate> getBankTemplates(String bankTemplateSet) {
        return bankTemplateMapper.getBankTemplates(bankTemplateSet);
    }

    public PaymentTakeRule getTakeRule(int mode, int targetType) {
        return takeRuleMapper.getTakeRule(mode, targetType);
    }

    public List<PaymentTakeRule> getTakeRules(int targetType) {
        return takeRuleMapper.getTakeRules(targetType);
    }

    public PaymentRechargePresent getRechargePresent(long matchAmount) {
        return rechargePresentMapper.getRechargePresent(matchAmount);
    }

    public List<PaymentRechargePresent> getRechargePresents(int currencyType) {
        return rechargePresentMapper.getRechargePresents(currencyType);
    }

    public int createPaymentBank(PaymentBank paymentBank) {
        return bankMapper.createPaymentBank(paymentBank);
    }

    public PaymentBank getBank(long bankId) {
        return bankMapper.getBankForKey(bankId);
    }

    public PaymentBank getBank(String bankAccount) {
        return bankMapper.getBank(bankAccount);
    }

    public List<PaymentBank> getBanks(long passportId) {
        return bankMapper.getBanks(passportId);
    }

    public int updateBanksStatus(long passportId, int status, int matchStatus) {
        return bankMapper.updateBanksStatus(passportId, status, matchStatus);
    }

    public int setDefault(long bankId, int status) {
        return bankMapper.setDefault(bankId, status);
    }

    public int updateBankData(long bankId, long passportId, long bankTemplateId, String bankAccount, String accountName, String reservePhone, String branchName, int status) {
        return bankMapper.updateBankData(bankId, passportId, bankTemplateId, bankAccount, accountName, reservePhone, branchName, status);
    }
}