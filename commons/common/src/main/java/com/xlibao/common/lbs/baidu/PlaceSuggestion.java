package com.xlibao.common.lbs.baidu;

/**
 * @author chinahuangxc
 */
public class PlaceSuggestion {

    private String name;
    private String latitude;
    private String longitude;
    private String uid;
    private String city;
    private String district;
    private String business;
    private String cityId;

    PlaceSuggestion(String name, String latitude, String longitude, String uid, String city, String district, String business, String cityId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid = uid;
        this.city = city;
        this.district = district;
        this.business = business;
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUid() {
        return uid;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getBusiness() {
        return business;
    }

    public String getCityId() {
        return cityId;
    }

    @Override
    public String toString() {
        return city + "，" + district + "，" + name + "(" + latitude + ", " + longitude + ")";
    }
}