package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentCurrencyOffsetLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PaymentCurrencyOffsetLoggerMapper {

    int createCurrencyOffsetLogger(PaymentCurrencyOffsetLogger currencyOffsetLogger);

    PaymentCurrencyOffsetLogger getCurrencyOffsetLogger(@Param("id") long id);

    List<PaymentCurrencyOffsetLogger> getCurrencyOffsetLoggers(@Param("passportId") long passportId, @Param("currencyType") int currencyType, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    List<PaymentCurrencyOffsetLogger> getCurrencyOffsetLoggersForTransType(@Param("passportId") long passportId, @Param("currencyType") int currencyType, @Param("transType") String transType,
                                                                           @Param("balanceType") int balanceType, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    List<PaymentCurrencyOffsetLogger> getCurrencyOffsetLoggersForDate(@Param("passportId") long passportId, @Param("currencyType") int currencyType, @Param("dateTime") String dateTime, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    long incomeForDate(@Param("passportId") long passportId, @Param("currencyType") int currencyType, @Param("dateTime") String dateTime);

    List<Map<String, Object>> dailyAmountStatistics(@Param("passportId") long passportId, @Param("currencyType") int currencyType, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
}