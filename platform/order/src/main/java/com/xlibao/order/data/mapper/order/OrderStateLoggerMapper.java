package com.xlibao.order.data.mapper.order;


import com.xlibao.metadata.order.OrderStateLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderStateLoggerMapper {

    int createOrderStateLogger(OrderStateLogger orderStateLogger);

    OrderStateLogger getOrderStateLogger(@Param("orderId") long orderId, @Param("status") int status);

    List<OrderStateLogger> getOrderStateLoggers(@Param("orderId") long orderId);

    int getRowSortStatistics(@Param("shippingPassportId") long shippingPassportId, @Param("orderType") int orderType, @Param("status") int status, @Param("matchTime") String matchTime, @Param("id") long id);
}