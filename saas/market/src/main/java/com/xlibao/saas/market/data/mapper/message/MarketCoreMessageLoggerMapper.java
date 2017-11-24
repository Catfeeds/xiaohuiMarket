package com.xlibao.saas.market.data.mapper.message;

import com.xlibao.saas.market.data.model.MarketCoreMessageLogger;
import org.apache.ibatis.annotations.Param;

public interface MarketCoreMessageLoggerMapper {

    int createMessageLogger(MarketCoreMessageLogger coreMessageLogger);

    MarketCoreMessageLogger getMessageLogger(@Param("marketId") long marketId, @Param("keyword") String keyword);
}