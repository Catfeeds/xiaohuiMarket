package com.xlibao.order.data.mapper.order;

import com.xlibao.metadata.order.OrderStatusListener;
import org.apache.ibatis.annotations.Param;

public interface OrderStatusListenerMapper {

    OrderStatusListener getOrderStatusListener(@Param("partnerId") String partnerId, @Param("eventType") int eventType);
}