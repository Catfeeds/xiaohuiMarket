package com.xlibao.common.lbs.baidu;

public class AddressComponent {

    private String country = "中国";
    private String province = "广东省";
    private String city = "广州市";
    private String district = "天河区";
    private String adcode = "440100";
    private String street = "东圃镇天海创意园";
    private String streetNumber = "555";

    private double longitude;
    private double latitude;

    private String formattedAddress = "";
    private String business = "";

    AddressComponent(String country, String province, String city, String district, String adcode, String street, String streetNumber, String formattedAddress, String business, double longitude, double latitude) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.adcode = adcode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.formattedAddress = formattedAddress;
        this.business = business;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * 国家名称
     */
    public String getCountry() {
        return country;
    }

    /**
     * 所属省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 行政区域
     */
    public String getDistrict() {
        return district;
    }

    public String getAdcode() {
        return adcode;
    }

    /**
     * 街道名字
     */
    public String getStreet() {
        return street;
    }

    /**
     * 街道门牌号
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * 格式化地址
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * 所属商圈
     */
    public String getBusiness() {
        return business;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "详细地址 [ " + country + ", " + province + ", " + city + ", " + district + "(" + adcode + "), " + street + ", " + streetNumber + " ]\r\n格式化地址 [ " + formattedAddress + " ]\r\n所属商圈 [ " + business + " ]\r\n经纬度[" + longitude + ", " + latitude + "]";
    }
}
