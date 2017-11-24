package com.xlibao.saas.market.data.mapper.market;

import com.xlibao.market.data.model.MarketEntry;
import com.xlibao.market.data.model.MarketRelationship;
import com.xlibao.market.data.model.MarketShelvesManager;
import com.xlibao.market.data.model.MarketTaskLogger;
import com.xlibao.saas.market.data.model.MarketAccessLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chinahuangxc on 2017/7/15.
 */
@Component
public class MarketDataAccessManager {

    @Autowired
    private MarketEntryMapper entryMapper;
    @Autowired
    private MarketRelationshipMapper relationshipMapper;
    @Autowired
    private MarketAccessLoggerMapper accessLoggerMapper;
    @Autowired
    private MarketShelvesManagerMapper shelvesManagerMapper;
    @Autowired
    private MarketTaskLoggerMapper taskLoggerMapper;

    public List<MarketEntry> loaderMarkets() {
        return entryMapper.loaderMarkets();
    }

    public MarketEntry getMarket(long marketId) {
        return entryMapper.getMarket(marketId);
    }

    public List<MarketEntry> getMarkets(long streetId) {
        return entryMapper.getMarkets(streetId);
    }

    public MarketEntry getMarketForPassport(long passportId) {
        return entryMapper.getMarketForPassport(passportId);
    }

    public int marketResponse(long marketId, int status, int matchStatus) {
        return entryMapper.marketResponse(marketId, status, matchStatus);
    }

    public int changeOnlineStatus(long marketId, int targetStatus, int matchStatus) {
        return entryMapper.changeOnlineStatus(marketId, targetStatus, matchStatus);
    }

    public List<MarketEntry> searchMarkets(MarketEntry searchModel, int pageSize, int pageStartIndex) {
        return entryMapper.searchMarkets(searchModel, pageSize, pageStartIndex);
    }

    public int searchMarketsCount(MarketEntry searchModel) {
        return entryMapper.searchMarketsCount(searchModel);
    }

    public int createMarket(MarketEntry entry) {
        return entryMapper.createMarket(entry);
    }

    public int updateMarket(MarketEntry entry) {
        return entryMapper.updateMarket(entry);
    }

    public MarketAccessLogger getLastAccessLogger(long passportId) {
        return accessLoggerMapper.getLastAccessLogger(passportId);
    }

    public int createShelves(MarketShelvesManager shelvesManager) {
        return shelvesManagerMapper.createShelves(shelvesManager);
    }

    public List<MarketShelvesManager> getShelvesDatas(long marketId) {
        return shelvesManagerMapper.getShelvesDatas(marketId);
    }

    public List<String> getShelvesMarks(long marketId, String groupCode, String unitCode, int shelvesType) {
        return shelvesManagerMapper.getShelvesMarks(marketId, groupCode, unitCode, shelvesType);
    }

    public List<MarketShelvesManager> getClipDatas(long marketId, String groupCode, String unitCode, String floorCode, int pageStartIndex, int pageSize) {
        return shelvesManagerMapper.getClipDatas(marketId, groupCode, unitCode, floorCode, pageStartIndex, pageSize);
    }

    public List<MarketRelationship> myFocusMarkets(String k, int type) {
        return relationshipMapper.myFocusMarkets(k, type);
    }

    public List<MarketRelationship> getRelationships(long marketId, int type) {
        return relationshipMapper.getRelationships(marketId, type);
    }

    public MarketRelationship getRelationship(String k, long marketId, int type) {
        return relationshipMapper.getRelationship(k, marketId, type);
    }

    public List<MarketTaskLogger> getTaskLoggers(long passportId, long marketId, int pageStartIndex, int pageSize) {
        return taskLoggerMapper.getTaskLoggers(passportId, marketId, pageStartIndex, pageSize);
    }

    public int createTaskLogger(MarketTaskLogger taskLogger) {
        return taskLoggerMapper.createTaskLogger(taskLogger);
    }

    public int clearShelves(long marketId) {
        return shelvesManagerMapper.clearShelves(marketId);
    }

}