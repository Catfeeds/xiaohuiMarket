package com.xlibao.datacache.location;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.http.HttpUtils;
import com.xlibao.datacache.DataCacheApplicationContextLoaderNotify;
import com.xlibao.metadata.passport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chinahuangxc on 2017/7/21.
 */
class LocationRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(LocationRemoteService.class);

    static List<PassportProvince> loaderProvinces() {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderProvinces";
        return getLocations(url, PassportProvince.class);
    }

    static List<PassportCity> loaderCitys() {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderCitys";
        return getLocations(url, PassportCity.class);
    }

    static List<PassportArea> loaderDistricts() {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderDistricts";
        return getLocations(url, PassportArea.class);
    }

    static List<PassportStreet> loaderStreets() {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderStreets";
        return getLocations(url, PassportStreet.class);
    }

    static List<PassportCity> getCitys(long provinceId) {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderCitys?provinceId=" + provinceId;
        return getLocations(url, PassportCity.class);
    }

    static List<PassportArea> getDistricts(long cityId) {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderDistricts?cityId=" + cityId;
        return getLocations(url, PassportArea.class);
    }

    static List<PassportStreet> getStreets(long districtId) {
        String url = DataCacheApplicationContextLoaderNotify.getLocationRemoteServiceURL() + "passport/location/loaderStreets?districtId=" + districtId;
        return getLocations(url, PassportStreet.class);
    }

    private static <T> List<T> getLocations(String url, Class<T> t) {
        String result = HttpUtils.get(url);
        JSONObject response = JSONObject.parseObject(result);

        int code = response.getIntValue("code");
        if (code != 0) {
            throw new XlibaoRuntimeException(code, response.getString("msg"));
        }
        response = response.getJSONObject("response");

        JSONArray provinceArray = response.getJSONArray("datas");

        List<T> provinces = new ArrayList<>();

        for (int i = 0; i < provinceArray.size(); i++) {
            provinces.add(JSONObject.parseObject(provinceArray.getString(i), t));
        }
        return provinces;
    }
}