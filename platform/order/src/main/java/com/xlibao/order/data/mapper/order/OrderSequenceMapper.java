package com.xlibao.order.data.mapper.order;

import com.xlibao.metadata.order.OrderSequence;
import org.apache.ibatis.annotations.Param;

public interface OrderSequenceMapper {

    int createOrderSequence(OrderSequence orderSequence);

    OrderSequence lastOrderSequence(@Param("partnerId") String partnerId, @Param("partnerUserId") String partnerUserId, @Param("orderType") int orderType);

    OrderSequence findOrderSequence(@Param("sequenceNumber") String sequenceNumber);

    int updateOrderSequence(@Param("sequenceNumber") String sequenceNumber, @Param("status") int status);
}