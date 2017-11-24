package com.xlibao.purchase.data.model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xlibao.purchase.utils.JsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PurchaseSupplier {
    private Long id;

    private String supplierName;

    private String abbreviation;

    private String address;

    private Integer supplierType;

    private String deliverPeriod;

    private String salesmanName;

    private String phone;

    private Integer status;

    private String stopRemark="";

    private String remark="";

    private String updateTime;

    private String createTime;

    private Integer isDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation == null ? null : abbreviation.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(Integer supplierType) {
        this.supplierType = supplierType;
    }

    public String getDeliverPeriod() {
        return deliverPeriod;
    }

    public void setDeliverPeriod(String deliverPeriod) {
        this.deliverPeriod = deliverPeriod == null ? null : deliverPeriod.trim();
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName == null ? null : salesmanName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
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
}