package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.market.data.model.MarketItemStockLockLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketItemStockLockLoggerMapper {

    int createItemStockLogger(MarketItemStockLockLogger itemStockLockLogger);

    List<MarketItemStockLockLogger> getItemStockLockLoggers(@Param("orderSequenceNumber") String orderSequenceNumber, @Param("key") int key, @Param("status") int status);

    List<MarketItemStockLockLogger> findInvalidItemStockLockLoggers(@Param("status") int status, @Param("timeout") String timeout);

    int modifyStockLockStatus(@Param("id") long id, @Param("status") int status);

    int releaseTimeoutItemLockStock(@Param("status") int status, @Param("timeout") String timeout, @Param("matchStatus") int matchStatus);
}