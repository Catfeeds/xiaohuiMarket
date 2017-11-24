package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentPartnerProperties;
import org.apache.ibatis.annotations.Param;

public interface PaymentPartnerPropertiesMapper {

    PaymentPartnerProperties getPartnerProperties(@Param("pid") long pid, @Param("appId") String appId);
}