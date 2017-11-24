package com.xlibao.saas.market.data.mapper.market;

import com.xlibao.market.data.model.MarketShelvesManager;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketShelvesManagerMapper {

    int createShelves(MarketShelvesManager marketShelvesManager);

    List<MarketShelvesManager> getShelvesDatas(@Param("marketId") long marketId);

    List<String> getShelvesMarks(@Param("marketId") long marketId, @Param("groupCode") String groupCode, @Param("unitCode") String unitCode, @Param("shelvesType") int shelvesType);

    List<MarketShelvesManager> getClipDatas(@Param("marketId") long marketId, @Param("groupCode") String groupCode, @Param("unitCode") String unitCode, @Param("floorCode") String floorCode,
                                            @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int clearShelves(@Param("marketId") long marketId);
}