package com.xiaohui.replenishment.response;

import com.zhumg.anlib.widget.AdapterModel;

/**
 * Created by zhumg on 8/29.
 */
public class Task implements AdapterModel {

    private long itemTemplateId;
    private String itemName;
    private int itemQuantity;
    private int hasCompleteQuantity;
    private String unitName;
    private String dateTitle;
    private String locationCode;
    private String barcode;
    private long taskId;
    private int status;
    private String hopeExecutorDate;
    private int uiType;
    private String completeTime;

    ///任务类型
    private int type;


    public Task() {
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public int getHasCompleteQuantity() {
        return hasCompleteQuantity;
    }

    public void setHasCompleteQuantity(int hasCompleteQuantity) {
        this.hasCompleteQuantity = hasCompleteQuantity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getDateTitle() {
        return dateTitle;
    }

    public void setDateTitle(String dateTitle) {
        this.dateTitle = dateTitle;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHopeExecutorDate() {
        return hopeExecutorDate;
    }

    public void setHopeExecutorDate(String hopeExecutorDate) {
        this.hopeExecutorDate = hopeExecutorDate;
    }

    @Override
    public int getUiType() {
        return uiType;
    }

    public void setUiType(int uiType) {
        this.uiType = uiType;
    }

    //是否是上架任务
    public boolean isUpTask() {
        return type == 1;
    }

    //已完成标志
    public boolean isPass() {
        return status == 2 || status == 4;
    }

    public boolean isError() {
        return hasCompleteQuantity != itemQuantity;
    }
}
