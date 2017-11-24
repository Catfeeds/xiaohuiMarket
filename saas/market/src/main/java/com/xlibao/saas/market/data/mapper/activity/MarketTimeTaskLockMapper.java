package com.xlibao.saas.market.data.mapper.activity;

import com.xlibao.saas.market.data.model.MarketTimeTaskLock;
import org.apache.ibatis.annotations.Param;

public interface MarketTimeTaskLockMapper {

    int createTimeTaskLock(MarketTimeTaskLock timeTaskLock);

    int hasExecutor(@Param("type") int type, @Param("happenTime") String happenTime);
}