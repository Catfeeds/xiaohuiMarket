package com.xiaohui.courier.response;

/**
 * Created by zhumg on 8/29.
 */
public class Market implements java.io.Serializable {


    //////////////////////////
    private String adminName;
    private String formatDistance;
    private String formatAddress;
    private String address;
    private String coveringDistance;
    private int deliveryMode;
    private String showPhoneNumber;
    private String name;
    private long id;
    private String hidePhoneNumber;
    private int type;
    private long deliveryCost;
    private int status;
    //////////////////////////

    //选中标志
    private boolean selected;

    public Market() {
    }

    public String getAdminName() {
        return adminName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getFormatDistance() {
        return formatDistance;
    }

    public void setFormatDistance(String formatDistance) {
        this.formatDistance = formatDistance;
    }

    public String getFormatAddress() {
        return formatAddress;
    }

    public void setFormatAddress(String formatAddress) {
        this.formatAddress = formatAddress;
    }

    public String getCoveringDistance() {
        return coveringDistance;
    }

    public void setCoveringDistance(String coveringDistance) {
        this.coveringDistance = coveringDistance;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getShowPhoneNumber() {
        return showPhoneNumber;
    }

    public void setShowPhoneNumber(String showPhoneNumber) {
        this.showPhoneNumber = showPhoneNumber;
    }

    public String getHidePhoneNumber() {
        return hidePhoneNumber;
    }

    public void setHidePhoneNumber(String hidePhoneNumber) {
        this.hidePhoneNumber = hidePhoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(long deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    //根据新状态，修改为新状态，因为原来 可能包括了 断连状态
    public void changeStatus(int targetStatus) {
//        if((status & 16) == 16) {
//            status = targetStatus | 16;
//        } else {
            status = targetStatus;
//        }
    }

    //是否在维护中
    public boolean isEdit() {
        //如果与硬件断连
        int newStatus = status;
//        if ((status & 16) == 16) {
//            newStatus = (status ^ 16);
//        } else {
//            newStatus = status;
//        }
        return newStatus == 4;//4表示维护中
    }
}
