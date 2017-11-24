package com.xlibao.common.lbs;

import java.util.List;

/**
 * @author chinahuangxc on 2016/10/10.
 */
public class SimpleLocationUtils {

    public static final double DEGREES_TO_RADIANS = Math.PI / 180.0;
    // public static final double RADIANS_TO_DEGREES = 180.0 / Math.PI;
    // 地球半径
    public static final double EARTH_MEAN_RADIUS_KM = 6371.009;
    // 地球直径
    private static final double EARTH_MEAN_DIAMETER = EARTH_MEAN_RADIUS_KM * 2;

    public static int routeCalculation(double originLatitude, double originLongitude, double terminalLatitude, double terminalLongitude) {
        // 计算经纬度
        double latRad = terminalLatitude * DEGREES_TO_RADIANS;
        double lonRad = terminalLongitude * DEGREES_TO_RADIANS;

        // 计算经纬度的差
        double diffX = originLatitude * DEGREES_TO_RADIANS - latRad;
        double diffY = originLongitude * DEGREES_TO_RADIANS - lonRad;
        // 计算正弦和余弦
        double hsinX = Math.sin(diffX * 0.5);
        double hsinY = Math.sin(diffY * 0.5);
        double latCenterRad_cos = Math.cos(originLatitude * DEGREES_TO_RADIANS);
        double h = hsinX * hsinX + (latCenterRad_cos * Math.cos(latRad) * hsinY * hsinY);

        return (int) ((EARTH_MEAN_DIAMETER * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h))) * 1000);
    }

    public static int consumeCalculate(double[] startLocation, List<double[]> pointLocation, double[] endLocation) {
        int distance = 0;
        if (pointLocation == null || pointLocation.isEmpty()) {
            // 计算起点到终点的距离
            distance = routeCalculation(startLocation[0], startLocation[1], endLocation[0], endLocation[1]);
            return distance;
        }
        // 起点到第一个途经点的距离
        distance = routeCalculation(startLocation[0], startLocation[1], pointLocation.get(0)[0], pointLocation.get(0)[1]);
        for (int i = 1; i < pointLocation.size(); i++) {
            // 每两个途经点的距离
            distance += routeCalculation(pointLocation.get(i - 1)[0], pointLocation.get(i - 1)[1], pointLocation.get(i)[0], pointLocation.get(i)[1]);
        }
        // 最后一个途经点到终点的距离
        distance += routeCalculation(pointLocation.get(pointLocation.size() - 1)[0], pointLocation.get(pointLocation.size() - 1)[1], endLocation[0], endLocation[1]);
        return distance;
    }

    public static int amapRouteCalculation(double originLatitude, double originLongitude, double terminalLatitude, double terminalLongitude) {
        originLongitude *= 0.01745329251994329D;
        originLatitude *= 0.01745329251994329D;
        terminalLongitude *= 0.01745329251994329D;
        terminalLatitude *= 0.01745329251994329D;
        double d6 = Math.sin(originLongitude);
        double d7 = Math.sin(originLatitude);
        double d8 = Math.cos(originLongitude);
        double d9 = Math.cos(originLatitude);
        double d10 = Math.sin(terminalLongitude);
        double d11 = Math.sin(terminalLatitude);
        double d12 = Math.cos(terminalLongitude);
        double d13 = Math.cos(terminalLatitude);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0]) + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1]) + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (int) (Math.asin(d14 / 2.0D) * 12742001.579854401D);
    }

    public static int simpleDistance(double originLatitude, double originLongitude, double terminalLatitude, double terminalLongitude) {
        // 经度差值
        double dx = originLongitude - terminalLongitude;
        // 纬度差值
        double dy = originLatitude - terminalLatitude;
        // 平均纬度
        double b = (originLatitude + terminalLatitude) / 2.0;
        // 东西距离
        double Lx = Math.toRadians(dx) * 6367000.0 * Math.cos(Math.toRadians(b));
        // 南北距离
        double Ly = 6367000.0 * Math.toRadians(dy);
        // 用平面的矩形对角距离公式计算总距离
        return (int) Math.sqrt(Lx * Lx + Ly * Ly);
    }
}