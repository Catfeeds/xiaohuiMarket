package com.xlibao.payment.data.mapper;

import com.xlibao.payment.data.model.PaymentCurrencyProperties;
import org.apache.ibatis.annotations.Param;

public interface PaymentCurrencyPropertiesMapper {

    int createCurrencyProperties(PaymentCurrencyProperties currencyProperties);

    PaymentCurrencyProperties getCurrencyProperties(@Param("passportId") long passportId, @Param("type") int type, @Param("key") String key) ;

    int modifyCurrencyProperties(@Param("passportId") long passportId, @Param("type") int type, @Param("key") String key, @Param("value") String value);
}