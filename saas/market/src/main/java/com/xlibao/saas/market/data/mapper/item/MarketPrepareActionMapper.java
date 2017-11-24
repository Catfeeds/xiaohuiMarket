package com.xlibao.saas.market.data.mapper.item;

import com.xlibao.market.data.model.MarketPrepareAction;
import com.xlibao.market.data.model.MarketShelvesDailyTaskLogger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketPrepareActionMapper {

    int createPrepareAction(MarketPrepareAction prepareAction);

    MarketPrepareAction getPrepareAction(@Param("marketId") long marketId, @Param("itemLocation") String itemLocation, @Param("type") int type, @Param("statusSet") String statusSet);

    List<MarketPrepareAction> getPrepareActions(@Param("executorPassportId") long executorPassportId, @Param("marketId") long marketId, @Param("statusSet") String statusSet,
                                                @Param("happenDate") String happenDate, @Param("pageStartIndex") int pageStartIndex, @Param("pageSize") int pageSize);

    int getPrepareActionSize(@Param("executorPassportId") long executorPassportId, @Param("marketId") long marketId, @Param("statusSet") String statusSet,
                             @Param("happenDate") String happenDate);

    List<MarketPrepareAction> getPrepareActionsForLocationSet(@Param("marketId") long marketId, @Param("locationSet") String locationSet, @Param("statusSet") String  statusSet);

    List<MarketPrepareAction> getPrepareActionForBarcode(@Param("marketId") long marketId, @Param("barcode") String barcode, @Param("statusSet") String statusSet);

    int getRemainActionRows(@Param("marketId") long marketId, @Param("type") int type, @Param("statusSet") String statusSet);

    int modifyPrepareActionStatus(@Param("executorPassportId") long executorPassportId, @Param("marketId") long marketId, @Param("type") int type, @Param("itemLocation") String itemLocation, @Param("incrementQuantity") int incrementQuantity, @Param("hopeExecutorQuantity") int hopeExecutorQuantity,@Param("matchStatusSet") String matchStatusSet, @Param("status") int status, @Param("time") String time);

    MarketPrepareAction getPrepareActionForId(@Param("taskId") long taskId);

    int distinctPrepareItemBarcode(@Param("marketId") long marketId, @Param("matchStatusSet") String matchStatusSet, @Param("passportId") long passportId, @Param("hopeExecutorDate") String hopeExecutorDate);

    List<MarketShelvesDailyTaskLogger> preparedSummaryData(@Param("passportId") long passportId, @Param("marketId") long marketId, @Param("type") int type, @Param("statusSet") String statusSet);

    int batchModifyPrepareActionStatus(@Param("executorPassportId") long executorPassportId, @Param("marketId") long marketId, @Param("targetStatus") int targetStatus, @Param("matchStatusSet") String matchStatusSet);
}