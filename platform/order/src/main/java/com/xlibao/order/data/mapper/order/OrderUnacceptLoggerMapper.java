package com.xlibao.order.data.mapper.order;

import com.xlibao.metadata.order.OrderUnacceptLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderUnacceptLoggerMapper {

    int createUnacceptLogger(OrderUnacceptLogger unacceptLogger);

    OrderUnacceptLogger getUnacceptLogger(@Param("orderId") long orderId, @Param("passportId") long passportId);

    OrderUnacceptLogger getNewestUnacceptLogger(@Param("passportId") long passportId) ;

    int unAcceptOrderSize(@Param("passportId") long passportId);

    int repushOrders(@Param("orderId") long orderId);

    int removeUnacceptLoggers(@Param("orderId") long orderId);

    List<OrderUnacceptLogger> getUnacceptLoggers(long passportId, int pageStartIndex, int pageSize);
}