package com.xlibao.purchase.data.model;

import java.util.Date;

public class PurchaseWarehouse {
    private Long id;

    private String warehouseCode;

    private String warehouseName;

    private String address="";

    private Integer status;

    private String stopRemark="";

    private String remark="";

    private  String wmsKEY="";

    private String updateTime;

    private String createTime;

    private Integer isDelete;

    private Integer userCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode == null ? null : warehouseCode.trim();
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName == null ? null : warehouseName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStopRemark() {
        return stopRemark;
    }

    public void setStopRemark(String stopRemark) {
        this.stopRemark = stopRemark == null ? null : stopRemark.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getWmsKEY() {
        return wmsKEY;
    }

    public void setWmsKEY(String wmsKEY) {
        this.wmsKEY = wmsKEY;
    }
}