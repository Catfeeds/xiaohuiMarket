package com.xlibao.common.lbs.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.lbs.LBSConstant;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author chinahuangxc on 2017/8/17
 */
public class BaiduWebAPI {

    // 主要用于获取本次的AppKey 通过取模形式获取
    private static final AtomicLong KEY_SEQUENCE = new AtomicLong(0);

    /**
     * @param origin 可以是经纬度数据也可以是省市区具体文字内容
     */
    public static Navigation rountMatrix(String origin, String destination) {
        NavigationBuilder navigationBuilder = new NavigationBuilder();
        try {
            navigationBuilder.setOrigin(origin).setDestination(destination);

            Map<String, String> parameter = new HashMap<>();
            parameter.put("origins", origin);
            parameter.put("destinations", destination);

            // 拼接请求地址
            StringBuilder requestURI = LBSConstant.getBaiduMapRouteMatrixApiURI(KEY_SEQUENCE.incrementAndGet());
            requestURI.append(LBSConstant.LOGIC_AND).append(HttpRequest.httpBuildQuery(parameter));
            // 请求百度返回结果
            String result = HttpRequest.get(requestURI.toString());
            // 编码转换
            result = StringEscapeUtils.unescapeJava(result);
            // 返回的数据
            JSONObject data = JSONObject.parseObject(result);
            // 结果状态标识
            int status = data.getIntValue("status");
            // 是否搜索成功
            if (status != 0) {
                navigationBuilder.setStatus(status).setStatusDescribe("无法定位[" + origin + "]到[" + destination + "]的导航信息");
                return navigationBuilder.builder();
            }
            // 搜索结果数据
            JSONObject routeResult = data.getJSONObject("result");
            // 节点数 (两点之间只有一个元素)
            JSONArray elements = routeResult.getJSONArray("elements");
            // 获取第一个节点
            JSONObject element = elements.getJSONObject(0);
            int elementStatus = element.getIntValue("status");
            if (elementStatus == 11) {
                // 起终点信息模糊
                navigationBuilder.setStatus(11).setStatusDescribe("起点[" + origin + "]或[" + destination + "]的信息模糊");
                return navigationBuilder.builder();
            }
            // 距离 获取value值 精确到米
            JSONObject distanceData = element.getJSONObject("distance");
            // 预计耗时 获取text 百度已转换
            JSONObject durationData = element.getJSONObject("duration");

            navigationBuilder.setDistance(distanceData.getIntValue("value")).setDistanceValue(distanceData.getString("text")).setTimeConsuming(durationData.getIntValue("value"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return navigationBuilder.builder();
    }

    public static AddressComponent geocoding(double latitude, double longitude) {
        AddressComponent addressComponent = null;
        try {
            // 拼接请求地址
            StringBuilder requestURI = LBSConstant.getBaiduMapGeocodingApiURI(KEY_SEQUENCE.incrementAndGet());
            requestURI.append(LBSConstant.LOGIC_AND).append("location=").append(latitude).append(",").append(longitude);
            // 请求百度返回结果
            String result = HttpRequest.get(requestURI.toString());
            // 编码转换
            result = StringEscapeUtils.unescapeJava(result);
            // 返回的数据
            JSONObject geoCodingResult = JSONObject.parseObject(result);
            // 结果状态标识
            int status = geoCodingResult.getIntValue("status");
            if (status != 0) {
                // 发生错误
                return null;
            }
            JSONObject locationData = geoCodingResult.getJSONObject("result");
            // 地址格式化
            String formattedAddress = locationData.getString("formatted_address");
            // 商圈
            String business = locationData.getString("business");
            // 地址组建
            JSONObject addressComponentData = locationData.getJSONObject("addressComponent");
            // 国家
            String country = addressComponentData.getString("country");
            // 省份
            String province = addressComponentData.getString("province");
            // 城市
            String city = addressComponentData.getString("city");
            // 区域
            String district = addressComponentData.getString("district");
            // 区域编码
            String adcode = addressComponentData.getString("adcode");
            // 街道
            String street = addressComponentData.getString("street");
            // 门牌号
            String streetNumber = addressComponentData.getString("street_number");

            JSONObject locationParameter = locationData.getJSONObject("location");

            addressComponent = new AddressComponent(country, province, city, district, adcode, street, streetNumber, formattedAddress, business, Double.parseDouble(locationParameter.getString("lng")), Double.parseDouble(locationParameter.getString("lat")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addressComponent;
    }

    public static LocationComponent geocoding(String address) {
        // 拼接请求地址
        StringBuilder requestURI = LBSConstant.getBaiduMapGeocodingAddressURI(KEY_SEQUENCE.incrementAndGet());
        requestURI.append(LBSConstant.LOGIC_AND).append("address=").append(address);

        // 请求百度返回结果
        String result = HttpRequest.get(requestURI.toString());
        // 编码转换
        result = StringEscapeUtils.unescapeJava(result);
        // 返回的数据
        JSONObject geoCodingResult = JSONObject.parseObject(result);
        // 结果状态标识
        int status = geoCodingResult.getIntValue("status");
        if (status != 0) {
            // 发生错误
            return null;
        }
        JSONObject locationData = geoCodingResult.getJSONObject("result").getJSONObject("location");

        byte precise = (byte) geoCodingResult.getJSONObject("result").getIntValue("precise");
        int confidence = geoCodingResult.getJSONObject("result").getIntValue("confidence");

        return new LocationComponent(address, Double.parseDouble(locationData.getString("lng")), Double.parseDouble(locationData.getString("lat")), precise, confidence);
    }

    public static List<PlaceSuggestion> placeSuggestion(String value, String region) {
        // 拼接请求地址
        StringBuilder requestURI = LBSConstant.getBaiduPlaceSuggestionURI(KEY_SEQUENCE.incrementAndGet());
        requestURI.append(LBSConstant.LOGIC_AND).append("q=").append(value);
        requestURI.append(LBSConstant.LOGIC_AND).append("region=").append(region);

        String result = HttpRequest.get(requestURI.toString());
        JSONObject callbackJSON = JSONObject.parseObject(result);
        int status = callbackJSON.getIntValue("status");
        if (status != 0) {
            return Collections.emptyList();
        }
        List<PlaceSuggestion> placeSuggestions = new ArrayList<>();

        JSONArray resultArray = callbackJSON.getJSONArray("result");
        for (int i = 0; i < resultArray.size(); i++) {
            JSONObject placeData = resultArray.getJSONObject(i);

            String name = placeData.getString("name");
            JSONObject location = placeData.getJSONObject("location");
            String latitude = "0.0";
            String longtitude = "0.0";
            if (location != null) {
                latitude = location.getString("lat");
                longtitude = location.getString("lng");
            }
            String uid = placeData.getString("uid");
            String city = placeData.getString("city");
            String district = placeData.getString("district");
            String business = placeData.getString("business");
            String cityId = placeData.getString("cityid");

            if (!cityId.equals(region)) {
                continue;
            }
            placeSuggestions.add(new PlaceSuggestion(name, latitude, longtitude, uid, city, district, business, cityId));
        }
        return placeSuggestions;
    }

    /**
     * 通过多点经纬度生成行车轨迹图片
     *
     * @param locationValue lng,lat;lng,lat
     */
    public static byte[] generateLocationImageDatas(String locationValue) {
        StringBuilder requestURI = LBSConstant.getBaiduLocationImageURI(KEY_SEQUENCE.incrementAndGet());
        requestURI.append(LBSConstant.LOGIC_AND).append("paths=").append(locationValue);

        return HttpRequest.readBytes(requestURI.toString());
    }

    public static void main(String[] args) {
        System.out.println(geocoding("广州市越秀区淘金南路110号"));
    }
}
