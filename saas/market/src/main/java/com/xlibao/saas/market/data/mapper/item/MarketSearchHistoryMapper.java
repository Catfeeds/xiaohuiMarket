package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.saas.market.data.model.MarketSearchHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketSearchHistoryMapper {

    void createHistorySearch(MarketSearchHistory searchHistory);

    List<String> loaderHotSearch(@Param("marketId") long marketId, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int incrementSearchTimes(@Param("marketId") long marketId, @Param("searchKey") String searchKey, @Param("lastSearchTime") String lastSearchTime);
}