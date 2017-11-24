package com.xlibao.saas.market.data.mapper.market;

import com.xlibao.market.data.model.MarketEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketEntryMapper {

    List<MarketEntry> loaderMarkets();

    MarketEntry getMarket(@Param("marketId") long marketId);

    List<MarketEntry> getMarkets(@Param("streetId") long streetId);

    MarketEntry getMarketForPassport(@Param("passportId") long passportId);

    List<MarketEntry> searchMarkets(@Param("searchModel")MarketEntry searchModel, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int searchMarketsCount(@Param("searchModel")MarketEntry searchModel);

    int marketResponse(@Param("marketId") long marketId, @Param("status") int status, @Param("matchStatus") int matchStatus);

    int changeOnlineStatus(@Param("marketId") long marketId, @Param("targetStatus") int targetStatus, @Param("matchStatus") int matchStatus);

    int createMarket(MarketEntry entry);

    int updateMarket(MarketEntry entry);
}