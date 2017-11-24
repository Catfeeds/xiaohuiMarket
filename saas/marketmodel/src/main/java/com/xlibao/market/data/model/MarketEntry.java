package com.xlibao.market.data.model;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.lbs.SimpleLocationUtils;

import java.util.Date;

public class MarketEntry {

    private Long id;
    private Long passportId;
    private String name;
    private String image;
    private Integer type;
    private Integer status;
    private Integer onlineStatus;
    private String adminName;
    private String phoneNumber;
    private Long streetId;
    private String province;
    private String city;
    private String district;
    private String street;
    private String streetNumber;
    private String address;
    private String location;
    private Long coveringDistance;
    private Integer deliveryMode;
    private Long deliveryCost;
    private Long freeDeliveryFee;
    private Date createTime;

    private int distance = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber == null ? null : streetNumber.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public Long getCoveringDistance() {
        return coveringDistance;
    }

    public void setCoveringDistance(Long coveringDistance) {
        this.coveringDistance = coveringDistance;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(Integer deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public Long getFreeDeliveryFee() {
        return freeDeliveryFee;
    }

    public void setFreeDeliveryFee(Long freeDeliveryFee) {
        this.freeDeliveryFee = freeDeliveryFee;
    }

    public Long getDeliveryCost() {
        return deliveryCost == null ? 0 : deliveryCost;
    }

    public void setDeliveryCost(Long deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public MarketEntry clone() {
        MarketEntry marketEntry = new MarketEntry();
        marketEntry.setId(getId());
        marketEntry.setPassportId(getPassportId());
        marketEntry.setName(getName());
        marketEntry.setType(getType());
        marketEntry.setStatus(getStatus());
        marketEntry.setAdminName(getAdminName());
        marketEntry.setPhoneNumber(getPhoneNumber());
        marketEntry.setStreetId(getStreetId());
        marketEntry.setProvince(getProvince());
        marketEntry.setCity(getCity());
        marketEntry.setDistrict(getDistrict());
        marketEntry.setStreet(getStreet());
        marketEntry.setStreetNumber(getStreetNumber());
        marketEntry.setAddress(getAddress());
        marketEntry.setLocation(getLocation());
        marketEntry.setCoveringDistance(getCoveringDistance());
        marketEntry.setDeliveryMode(getDeliveryMode());
        marketEntry.setDeliveryCost(getDeliveryCost());
        marketEntry.setFreeDeliveryFee(getFreeDeliveryFee());
        marketEntry.setCreateTime(getCreateTime());

        return marketEntry;
    }

    public JSONObject message(double latitude, double longitude) {
        JSONObject response = new JSONObject();

        response.put("id", getId());
        response.put("name", getName());
        response.put("type", getType());
        response.put("status", getStatus());
        response.put("onlineStatus", getOnlineStatus());
        response.put("adminName", CommonUtils.nullToEmpty(getAdminName()));
        response.put("showPhoneNumber", CommonUtils.hideChar(getPhoneNumber(), 4, 7));
        response.put("hidePhoneNumber", getPhoneNumber());
        response.put("formatAddress", formatAddress());
        response.put("address", CommonUtils.nullToEmpty(getAddress()));
        response.put("coveringDistance", CommonUtils.formatDistance(getCoveringDistance()));
        response.put("deliveryMode", getDeliveryMode());
        response.put("deliveryCost", getDeliveryCost());
        response.put("freeDeliveryFee", getFreeDeliveryFee());
        response.put("formatDistance", CommonUtils.formatDistance(SimpleLocationUtils.simpleDistance(latitude, longitude, Double.parseDouble(getLocation().split(CommonUtils.SPLIT_COMMA)[1]), Double.parseDouble(getLocation().split(CommonUtils.SPLIT_COMMA)[0]))));

        return response;
    }

    public String formatAddress() {
        return CommonUtils.nullToEmpty(getProvince()) + CommonUtils.nullToEmpty(getCity()) + CommonUtils.nullToEmpty(getDistrict()) + CommonUtils.nullToEmpty(getStreet()) + CommonUtils.nullToEmpty(getStreetNumber());
    }
}