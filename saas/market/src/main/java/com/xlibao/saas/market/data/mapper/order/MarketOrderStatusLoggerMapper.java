package com.xlibao.saas.market.data.mapper.order;

import com.xlibao.saas.market.data.model.MarketOrderStatusLogger;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface MarketOrderStatusLoggerMapper {

    int createOrderStatusLogger(MarketOrderStatusLogger orderStatusLogger);

    MarketOrderStatusLogger getOrderStatusLogger(@Param("orderSequenceNumber") String orderSequenceNumber, @Param("notifyType") int notifyType, @Param("localStatus") int localStatus);

    int modifyOrderRemoteStatusLogger(@Param("orderSequenceNumber") String orderSequenceNumber, @Param("notifyType") int notifyType, @Param("localStatus") int localStatus, @Param("remoteStatus") int remoteStatus, @Param("remoteCompleteTime") Date remoteCompleteTime);
}