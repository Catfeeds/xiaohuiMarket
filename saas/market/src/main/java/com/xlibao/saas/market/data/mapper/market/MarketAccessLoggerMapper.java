package com.xlibao.saas.market.data.mapper.market;

import com.xlibao.saas.market.data.model.MarketAccessLogger;
import org.apache.ibatis.annotations.Param;

public interface MarketAccessLoggerMapper {

    MarketAccessLogger getLastAccessLogger(@Param("passportId") long passportId);
}