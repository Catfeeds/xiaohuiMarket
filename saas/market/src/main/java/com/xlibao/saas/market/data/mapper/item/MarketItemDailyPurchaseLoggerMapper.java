package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.market.data.model.MarketItemDailyPurchaseLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketItemDailyPurchaseLoggerMapper {

    List<MarketItemDailyPurchaseLogger> passportDailyBuyLoggers(@Param("passportId") long passportId, @Param("itemSet") String itemSet, @Param("date") String date);
}