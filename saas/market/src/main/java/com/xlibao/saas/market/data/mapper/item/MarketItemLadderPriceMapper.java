package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.market.data.model.MarketItemLadderPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketItemLadderPriceMapper {

    List<MarketItemLadderPrice> loadItemLadderPrices(@Param("itemSet") String itemSet);

    int createItemLadderPrice(MarketItemLadderPrice itemLadderPrice);

    int removeItemLadderPrice(@Param("itemId") long itemId, @Param("itemLadderId") long itemLadderId);
}