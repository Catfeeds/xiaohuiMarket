package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentTakeRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentTakeRuleMapper {

    PaymentTakeRule getTakeRule(@Param("mode") int mode, @Param("targetType") int targetType);

    List<PaymentTakeRule> getTakeRules(@Param("targetType") int targetType);
}