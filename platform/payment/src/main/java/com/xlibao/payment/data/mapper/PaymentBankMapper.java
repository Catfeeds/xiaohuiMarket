package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentBank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentBankMapper {

    int createPaymentBank(PaymentBank paymentBank);

    PaymentBank getBankForKey(@Param("bankId") long bankId);

    PaymentBank getBank(@Param("bankAccount") String bankAccount);

    List<PaymentBank> getBanks(@Param("passportId") long passportId);

    int updateBanksStatus(@Param("passportId") long passportId, @Param("status") int status, @Param("matchStatus") int matchStatus);

    int setDefault(@Param("bankId") long bankId, @Param("status") int status);

    int updateBankData(@Param("bankId") long bankId, @Param("passportId") long passportId, @Param("bankTemplateId") long bankTemplateId, @Param("bankAccount") String bankAccount, @Param("accountName") String accountName,
                       @Param("reservePhone") String reservePhone, @Param("branchName") String branchName, @Param("status") int status);
}