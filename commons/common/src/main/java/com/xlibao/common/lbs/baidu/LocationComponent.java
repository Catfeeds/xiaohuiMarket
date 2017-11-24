package com.xlibao.common.lbs.baidu;

public class LocationComponent {

    private String address;
    private double longitude;
    private double latitude;
    // 位置的附加信息，是否精确查找。1为精确查找，0为不精确。
    private byte precise;
    // 查询结果可信度(一般可信度较低的是因为位置信息不够精确，所以前端提供数据时请尽量给出精确位置信息)
    private int confidence;

    public LocationComponent(String address, double longitude, double latitude, byte precise, int confidence) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.precise = precise;
        this.confidence = confidence;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public byte getPrecise() {
        return precise;
    }

    public int getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return "地址 [ " + address + " ] \r\n经纬度 [ " + longitude + "，" + latitude + " ] \r\n是否精确查找 [ " + (precise == 1) + " ] \r\n结果可信度 [ " + confidence + " ] ";
    }
}