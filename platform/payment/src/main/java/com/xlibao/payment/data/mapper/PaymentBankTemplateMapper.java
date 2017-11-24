package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentBankTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentBankTemplateMapper {

    List<PaymentBankTemplate> showBankTemplates();

    List<PaymentBankTemplate> getBankTemplates(@Param("bankTemplateSet") String bankTemplateSet);

    PaymentBankTemplate getBankTemplate(@Param("bankTemplateId") long bankTemplateId);
}