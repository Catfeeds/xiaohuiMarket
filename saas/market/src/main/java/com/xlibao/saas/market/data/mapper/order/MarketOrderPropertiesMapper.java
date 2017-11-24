package com.xlibao.saas.market.data.mapper.order;

import com.xlibao.saas.market.data.model.MarketOrderProperties;
import org.apache.ibatis.annotations.Param;

public interface MarketOrderPropertiesMapper {

    int createOrderProperties(MarketOrderProperties orderProperties);

    MarketOrderProperties getOrderProperties(@Param("orderId") long orderId, @Param("propertiesType") int propertiesType, @Param("propertiesKey") String propertiesKey);

    int updateOrderProperties(@Param("id") long id, @Param("propertiesValue") String propertiesValue);
}