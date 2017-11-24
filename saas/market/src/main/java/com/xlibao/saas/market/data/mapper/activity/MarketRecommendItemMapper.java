package com.xlibao.saas.market.data.mapper.activity;

import com.xlibao.market.data.model.MarketRecommendItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketRecommendItemMapper {

    List<MarketRecommendItem> getRecommendItemsByMarket(@Param("marketId") long marketId, @Param("type") int type);

    List<MarketRecommendItem> getAdcodeDefaultRecommendItems(@Param("adcode") String adcode, @Param("type") int type);

    List<MarketRecommendItem> getDefaultRecommendItems(@Param("type") int type);
}