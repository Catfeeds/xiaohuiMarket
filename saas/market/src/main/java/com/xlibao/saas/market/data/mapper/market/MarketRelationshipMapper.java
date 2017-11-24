package com.xlibao.saas.market.data.mapper.market;

import com.xlibao.market.data.model.MarketRelationship;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketRelationshipMapper {

    List<MarketRelationship> myFocusMarkets(@Param("k") String k, @Param("type") int type);

    MarketRelationship getRelationship(@Param("k") String k, @Param("marketId") long marketId, @Param("type") int type);

    List<MarketRelationship> getRelationships(@Param("marketId") long marketId, @Param("type") int type);
}