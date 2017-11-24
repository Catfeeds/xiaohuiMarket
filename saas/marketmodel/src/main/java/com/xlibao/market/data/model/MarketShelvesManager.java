package com.xlibao.market.data.model;

import java.util.Date;

public class MarketShelvesManager {

    private Long id;
    private Long marketId;
    private String groupCode;
    private String unitCode;
    private String floorCode;
    private String clipCode;
    private Date lastModifyTime;
    private Long modifyPassportId;
    private Date createTime;

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

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode == null ? null : unitCode.trim();
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode == null ? null : floorCode.trim();
    }

    public String getClipCode() {
        return clipCode;
    }

    public void setClipCode(String clipCode) {
        this.clipCode = clipCode == null ? null : clipCode.trim();
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Long getModifyPassportId() {
        return modifyPassportId;
    }

    public void setModifyPassportId(Long modifyPassportId) {
        this.modifyPassportId = modifyPassportId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}