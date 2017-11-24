package com.xlibao.common.lbs.baidu;

import com.xlibao.common.CommonUtils;

public class Navigation {

    private int status;
    private String statusDescribe;
    private String origin;
    private String destination;
    private int distance;
    private String distanceValue;
    private long timeConsuming;

    Navigation(int status, String statusDescribe, String origin, String destination, int distance, String distanceValue, long timeConsuming) {
        this.status = status;
        this.statusDescribe = statusDescribe;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.distanceValue = distanceValue;
        this.timeConsuming = timeConsuming;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusDescribe() {
        return statusDescribe;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getDistance() {
        return distance;
    }

    public String getDistanceValue() {
        return distanceValue;
    }

    public long getTimeConsuming() {
        return timeConsuming;
    }

    @Override
    public String toString() {
        return "从 [ " + origin + " ] 到 [ " + destination + " ] 距离为：" + distanceValue + "(" + (distance / 1000f) + "公里)，预计需要耗时：" + CommonUtils.secondToTimeValue(timeConsuming);
    }
}