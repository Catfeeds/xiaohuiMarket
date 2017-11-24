package com.xlibao.advert.data.model;

import java.util.Date;

public class AdvertGroup {
    private Long advertGroupId;

    private Long advertId;

    private Long groupId;

    private String updateTime;

    private String createTime;

    private Integer isDelete;

    public Long getAdvertGroupId() {
        return advertGroupId;
    }

    public void setAdvertGroupId(Long advertGroupId) {
        this.advertGroupId = advertGroupId;
    }

    public Long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}