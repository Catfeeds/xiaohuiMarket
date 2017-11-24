package com.xlibao.saas.market.data.mapper.order;

import com.xlibao.market.data.model.MarketOrderUnacceptLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketOrderUnacceptLoggerMapper {

    int createUnAcceptLogger(MarketOrderUnacceptLogger orderUnacceptLogger);

    MarketOrderUnacceptLogger getOrderUnacceptLogger(@Param("passportId") long passportId, @Param("orderSequenceNumber") String orderSequenceNumber);

    List<String> getAppointMarketOrderSequences(@Param("passportId") long passportId, @Param("marketId") long marketId, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int removeUnAcceptLoggers(@Param("orderSequenceNumber") String orderSequenceNumber);
}