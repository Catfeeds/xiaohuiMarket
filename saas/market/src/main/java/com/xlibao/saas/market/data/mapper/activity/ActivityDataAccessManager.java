package com.xlibao.saas.market.data.mapper.activity;

import com.xlibao.market.data.model.MarketBanner;
import com.xlibao.market.data.model.MarketRecommendItem;
import com.xlibao.saas.market.data.model.MarketTimeTaskLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chinahuangxc on 2017/7/16.
 */
@Component
public class ActivityDataAccessManager {

    @Autowired
    private MarketBannerMapper bannerMapper;
    @Autowired
    private MarketRecommendItemMapper recommendItemMapper;
    @Autowired
    private MarketTimeTaskLockMapper timeTaskLockMapper;

    public List<MarketBanner> getBannerByMarket(long marketId) {
        return bannerMapper.getBannerByMarket(marketId);
    }

    public List<MarketBanner> getAdcodeDefaultBanners(String adcode) {
        return bannerMapper.getAdcodeDefaultBanners(adcode);
    }

    public List<MarketBanner> getDefaultBanners() {
        return bannerMapper.getDefaultBanners();
    }

    public List<MarketRecommendItem> getRecommendItemsByMarket(long marketId, int type) {
        return recommendItemMapper.getRecommendItemsByMarket(marketId, type);
    }

    public List<MarketRecommendItem> getAdcodeDefaultRecommendItems(String adcode, int type) {
        return recommendItemMapper.getAdcodeDefaultRecommendItems(adcode, type);
    }

    public List<MarketRecommendItem> getDefaultRecommendItems(int type) {
        return recommendItemMapper.getDefaultRecommendItems(type);
    }

    public int hasExecutor(int type, String happenTime) {
        return timeTaskLockMapper.hasExecutor(type, happenTime);
    }

    public int createTimeTaskLock(MarketTimeTaskLock timeTaskLock) {
        return timeTaskLockMapper.createTimeTaskLock(timeTaskLock);
    }
}