package com.xlibao.common.lbs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chinahuangxc on 2017/3/22.
 */
public class LBSConstant {

    public static final String LOGIC_AND = "&";

    private static final ConcurrentHashMap<Integer, String> BAIDU_APP_KEY_MAP = new ConcurrentHashMap<>();

    static {
        int i = 0;
        BAIDU_APP_KEY_MAP.put(i++, "sHkOOXK0nrsfV1HYcaa4G3yk"); // 黄晓春
        BAIDU_APP_KEY_MAP.put(i++, "3GwHQ0tNgrCFIkOvrMCGH1Zm"); // 梁志强
        BAIDU_APP_KEY_MAP.put(i++, "dcZDBxdRfEtLpE5ejZxi9448"); // 公司163帐号
        BAIDU_APP_KEY_MAP.put(i++, "NGcdiTIjpGeg3WaewE0xBd2c"); // 彭延福
        BAIDU_APP_KEY_MAP.put(i++, "BWGbXOMOcbRBlSX7cnhNGFZw"); // 梁彦韬
        BAIDU_APP_KEY_MAP.put(i++, "doe14G32L8ZMoh1mfVPna7Kp"); // 魏洪流
        BAIDU_APP_KEY_MAP.put(i++, "VmdTsGit2ItlcWv2maUObzYc"); // 赵镇洲
        BAIDU_APP_KEY_MAP.put(i++, "owbbNcyrG0S8oGrTjiWd7uTC"); // 周正军
        BAIDU_APP_KEY_MAP.put(i++, "KkCiQFqT90bFNhjDlwx9hYwy"); // 黄雪娟
        BAIDU_APP_KEY_MAP.put(i++, "t2lwQexNzXNPWgKSASliD8OG"); // 罗炳赞
        BAIDU_APP_KEY_MAP.put(i++, "ON7rNF1c3sZgBjMA1xFiBvnM"); // 陈伟健
        BAIDU_APP_KEY_MAP.put(i++, "IFcu597n5X6qxbV2Mp87E6El"); // 曾广贞
        BAIDU_APP_KEY_MAP.put(i++, "GG6eS9Q2enqfMU2uVd6Syf3M"); // 徐星
        BAIDU_APP_KEY_MAP.put(i++, "sUWHex1eXAbdQ2u8Wom5nza0"); // 陈雅洁
    }

    // 取模的值
    private static final int MODULAR = BAIDU_APP_KEY_MAP.size();
    /**
     * <pre>
     * coord_type 坐标类型，可选参数，默认为bd09ll。允许的值为：bd09ll（百度经纬度坐标）、bd09mc（百度摩卡托坐标）、gcj02（国测局加密坐标）、wgs84（gps设备获取的坐标）。
     * 为了和高德地图兼容，这里选取gcj02
     * tactics 导航策略。导航路线类型：10，不走高速；11、最少时间；12、最短路径。
     * </pre>
     */
    public static final String BAIDU_MAP_ROUTE_MATRIX_API_URI = "http://api.map.baidu.com/direction/v1/routematrix?output=json&tactics=10&ak=";

    /** 通过经纬度获取地址信息 */
    public static final String BAIDU_MAP_GEOCODING_API_URI = "http://api.map.baidu.com/geocoder/v2/?output=json&pois=0&ak=";

    /** 通过地址获取经纬度信息 */
    public static final String BAIDU_MAP_GEOCODING_ADDRESS_URI = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=";

    /** 模糊查询地址信息 */
    public static final String BAIDU_PLACE_SUGGESTION_URL = "http://api.map.baidu.com/place/v2/suggestion?output=json&ak=";

    public static final String BAIDU_LOCATION_IMAGE_URL = "http://api.map.baidu.com/staticimage/v2?width=1024&height=576&zoom=11&pathStyles=0xff0000,5,1&ak="; // sHkOOXK0nrsfV1HYcaa4G3yk&paths=116.288891,40.004261;116.487812,40.017524;116.525756,39.967111;116.536105,39.872373

    public static StringBuilder getBaiduMapRouteMatrixApiURI(long sequence) {
        StringBuilder builder = new StringBuilder(BAIDU_MAP_ROUTE_MATRIX_API_URI);
        builder.append(getBaiduKey(sequence));
        return builder;
    }

    public static StringBuilder getBaiduMapGeocodingApiURI(long sequence) {
        StringBuilder builder = new StringBuilder(BAIDU_MAP_GEOCODING_API_URI);
        builder.append(getBaiduKey(sequence));
        return builder;
    }

    public static StringBuilder getBaiduMapGeocodingAddressURI(long sequence) {
        StringBuilder builder = new StringBuilder(BAIDU_MAP_GEOCODING_ADDRESS_URI);
        builder.append(getBaiduKey(sequence));
        return builder;
    }

    public static StringBuilder getBaiduPlaceSuggestionURI(long sequence) {
        StringBuilder builder = new StringBuilder(BAIDU_PLACE_SUGGESTION_URL);
        builder.append(getBaiduKey(sequence));
        return builder;
    }

    public static StringBuilder getBaiduLocationImageURI(long sequence) {
        StringBuilder builder = new StringBuilder(BAIDU_LOCATION_IMAGE_URL);
        builder.append(getBaiduKey(sequence));
        return builder;
    }

    public static String getBaiduKey(long sequence) {
        int index = (int) (sequence % MODULAR);
        return BAIDU_APP_KEY_MAP.get(index);
    }
}