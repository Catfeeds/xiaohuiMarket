package com.xlibao.saas.market.manager.service.passportmanager.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.common.http.HttpUtils;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.metadata.passport.PassportStreet;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.service.passportmanager.PassportManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("passportManagerService")
public class PassportManagerServiceImpl extends BasicWebService implements PassportManagerService {

    @Override
    public boolean passportLogin(String userName, String passWord) {
        //String result = HttpUtils.get()
        return false;
    }

    public JSONObject getStreets(long districtId) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/loaderStreets.do?districtId=" + districtId);
        JSONObject response = JSONObject.parseObject(json);
        return response;
    }

    public JSONObject getStreetsToMap(long districtId) {
        JSONObject response = getStreets(districtId);
        if (response.getIntValue("code") == 0) {
            JSONObject result = new JSONObject();
            result.put("code", response.getIntValue("code"));
            result.put("msg", response.getString("msg"));
            JSONArray array = response.getJSONObject("response").getJSONArray("datas");
            Map map = new HashMap();
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                map.put(json.getInteger("id"), json.getString("name"));
            }
            result.put("response", map);
            return result;
        }
        return response;
    }


    public PassportProvince getProvinceById(long provinceId) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/getProvinceById.do?id=" + provinceId);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportProvince.class);
        }
        return null;
    }

    public PassportCity getCityById(long cityId) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/getCityById.do?id=" + cityId);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportCity.class);
        }
        return null;
    }

    public PassportArea getAreaById(long areaId) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/getAreaById.do?id=" + areaId);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportArea.class);
        }
        return null;
    }


    public JSONObject getStreetJson(long streetId) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/getStreet.do?streetId=" + streetId);
        JSONObject response = JSONObject.parseObject(json);
        return response;
    }


    public PassportProvince searchProvinceByName(String name) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/searchProvinceByName.do?name=" + name);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportProvince.class);
        }
        return null;
    }

    public PassportCity searchCityByName(String name) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/searchCityByName.do?name=" + name);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportCity.class);
        }
        return null;
    }

    public PassportArea searchAreaByName(String name) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/searchAreaByName.do?name=" + name);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportArea.class);
        }
        return null;
    }

    public PassportStreet searchStreetByName(long districtId, String name) {
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/searchStreetByName.do?name=" + name + "&districtId=" + districtId);
        JSONObject response = JSONObject.parseObject(json);
        if (response.getInteger("code") == 0) {
            return JSONObject.parseObject(response.getJSONObject("response").getString("data"), PassportStreet.class);
        }
        return null;
    }

    public PassportStreet getStreet(long streetId) {
        JSONObject json = getStreetJson(streetId);
        if (json.getInteger("code") == 0) {
            return JSONObject.parseObject(json.getJSONObject("response").getString("data"), PassportStreet.class);
        }
        return null;
    }

    public JSONObject streetEditSave(long id, long areaId, String name) {
        Map map = new HashMap();
        map.put("id", String.valueOf(id));
        map.put("name", name);
        map.put("areaId", String.valueOf(areaId));
        String json = HttpRequest.post(ConfigFactory.getDomainNameConfig().passportRemoteURL + "/passport/location/streetEditSave.do", map);
        return JSONObject.parseObject(json);
    }
}