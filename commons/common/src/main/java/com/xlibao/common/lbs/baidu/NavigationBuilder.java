package com.xlibao.common.lbs.baidu;

public class NavigationBuilder {

    private int status = 0;
    private String statusDescribe;
    private String origin;
    private String destination;
    private int distance;
    private String distanceValue;
    private long timeConsuming;

    public NavigationBuilder setStatus(int status) {
        this.status = status;
        return this;
    }

    public NavigationBuilder setStatusDescribe(String statusDescribe) {
        this.statusDescribe = statusDescribe;
        return this;
    }

    public NavigationBuilder setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public NavigationBuilder setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public NavigationBuilder setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public NavigationBuilder setDistanceValue(String distanceValue) {
        this.distanceValue = distanceValue;
        return this;
    }

    public NavigationBuilder setTimeConsuming(long timeConsuming) {
        this.timeConsuming = timeConsuming;
        return this;
    }

    public Navigation builder() {
        Navigation navigation = new Navigation(status, statusDescribe, origin, destination, distance, distanceValue, timeConsuming);
        return navigation;
    }
}