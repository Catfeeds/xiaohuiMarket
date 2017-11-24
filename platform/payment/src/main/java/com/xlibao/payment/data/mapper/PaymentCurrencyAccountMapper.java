package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentCurrencyAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentCurrencyAccountMapper {

    int createCurrencyAccount(PaymentCurrencyAccount currencyAccount);

    PaymentCurrencyAccount getCurrencyAccount(@Param("passportId") long passportId, @Param("channelId") long channelId, @Param("currencyType") int currencyType);

    List<PaymentCurrencyAccount> getCurrencyAccounts(@Param("passportId") long passportId, @Param("channelId") long channelId);

    int offsetCurrencyAmount(@Param("passportId") long passportId, @Param("channelId") long channelId, @Param("currencyType") int currencyType, @Param("offsetAmount") long offsetAmount);
}