package com.xlibao.saas.market.data.model;

import java.util.Date;

public class MarketSearchHistory {

    private Long id;
    private Long marketId;
    private String k;
    private Integer totalSearchTimes = 1;
    private Date lastSearchTime = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k == null ? null : k.trim();
    }

    public Integer getTotalSearchTimes() {
        return totalSearchTimes;
    }

    public void setTotalSearchTimes(Integer totalSearchTimes) {
        this.totalSearchTimes = totalSearchTimes;
    }

    public Date getLastSearchTime() {
        return lastSearchTime;
    }

    public void setLastSearchTime(Date lastSearchTime) {
        this.lastSearchTime = lastSearchTime;
    }
}