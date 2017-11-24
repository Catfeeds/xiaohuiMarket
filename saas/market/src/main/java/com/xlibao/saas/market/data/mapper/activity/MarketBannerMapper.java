package com.xlibao.saas.market.data.mapper.activity;

import com.xlibao.market.data.model.MarketBanner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketBannerMapper {

    List<MarketBanner> getBannerByMarket(@Param("marketId") long marketId);

    List<MarketBanner> getAdcodeDefaultBanners(@Param("adcode") String adcode);

    List<MarketBanner> getDefaultBanners();
}