package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.market.data.model.MarketShelvesDailyTaskLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketShelvesDailyTaskLoggerMapper {

    int createShelvesDailyTaskLogger(MarketShelvesDailyTaskLogger shelvesDailyTaskLogger);

    List<MarketShelvesDailyTaskLogger> getShelvesDailyTaskLoggers(@Param("marketId") long marketId, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int getShelvesDailyTaskLoggerSize(@Param("marketId") long marketId);
}