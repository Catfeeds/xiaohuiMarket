package com.xlibao.saas.market.data.mapper.order;

import com.xlibao.market.data.model.MarketSplitOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketSplitOrderMapper {

    int createSplitOrder(MarketSplitOrder splitOrder);

    List<MarketSplitOrder> getSplitOrders(@Param("orderId") long orderId);
}