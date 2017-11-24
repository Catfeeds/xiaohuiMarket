package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentTransactionLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentTransactionLoggerMapper {

    int createPaymentTransactionLogger(PaymentTransactionLogger transactionLogger);

    int getTransactionLoggerSize(@Param("passportId") long passportId, @Param("transType") int transType, @Param("dateTime") String dateTime);

    PaymentTransactionLogger getTransactionLogger(@Param("transSequenceNumber") String transSequenceNumber);

    PaymentTransactionLogger getTransactionLoggerForPartnerTradeNumber(@Param("partnerTradeNumber") String partnerTradeNumber);

    PaymentTransactionLogger getTransactionLoggerById(@Param("tid") long tid);

    List<PaymentTransactionLogger> getTransactionLoggers(@Param("passportId") long passportId, @Param("transType") int transType, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    List<PaymentTransactionLogger> getRechargeTransactionLoggers(@Param("passportId") long passportId, @Param("transType") int transType, @Param("currencyType") String currencyType, @Param("transStatus") int transStatus,
                                                                 @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int modifyTransactionStatus(PaymentTransactionLogger transactionLogger);

    int updateRefundParameter(PaymentTransactionLogger transactionLogger);
}