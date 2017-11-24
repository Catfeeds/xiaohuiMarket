package com.xlibao.saas.market.data.mapper.market;

import com.xlibao.market.data.model.MarketTaskLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketTaskLoggerMapper {

    List<MarketTaskLogger> getTaskLoggers(@Param("passportId") long passportId, @Param("marketId") long marketId, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int createTaskLogger(MarketTaskLogger taskLogger);
}