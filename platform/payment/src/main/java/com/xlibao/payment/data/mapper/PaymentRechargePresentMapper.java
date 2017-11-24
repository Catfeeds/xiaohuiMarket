package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentRechargePresent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentRechargePresentMapper {

    PaymentRechargePresent getRechargePresent(@Param("matchAmount") long matchAmount);

    List<PaymentRechargePresent> getRechargePresents(@Param("currencyType") int currencyType);
}