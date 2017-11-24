package com.xlibao.saas.market.data.mapper.question;

import com.xlibao.market.data.model.MarketLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketLabelMapper {

    List<MarketLabel> loaderLabels(@Param("type") int type);
}