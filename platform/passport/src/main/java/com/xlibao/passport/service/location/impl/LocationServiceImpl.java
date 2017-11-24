package com.xlibao.passport.service.location.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.metadata.passport.PassportStreet;
import com.xlibao.passport.data.mapper.location.LocationDataAccessManager;
import com.xlibao.passport.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/7/21.
 */
@Service("locationService")
public class LocationServiceImpl extends BasicWebService implements LocationService {

    @Autowired
    private LocationDataAccessManager locationDataAccessManager;

    @Override
    public JSONObject loaderLocations() {
        List<PassportProvince> provinces = locationDataAccessManager.loaderProvinces();
        List<PassportCity> citys = locationDataAccessManager.loaderCitys(0);
        List<PassportArea> districts = locationDataAccessManager.loaderDistricts(0);
        List<PassportStreet> streets = locationDataAccessManager.loaderStreets(0);

        Map<Long, List<PassportStreet>> streetMap = new HashMap<>();
        for (PassportStreet street : streets) {
            List<PassportStreet> streetList = streetMap.get(street.getAreaId());
            if (streetList == null) {
                streetList = new ArrayList<>();
                streetMap.put(street.getAreaId(), streetList);
            }
            streetList.add(street);
        }

        Map<Long, List<PassportArea>> districtMap = new HashMap<>();
        for (PassportArea district : districts) {
            List<PassportArea> districtList = districtMap.get(district.getCityId());
            if (districtList == null) {
                districtList = new ArrayList<>();
                districtMap.put(district.getCityId(), districtList);
            }
            districtList.add(district);
        }

        Map<Long, List<PassportCity>> cityMap = new HashMap<>();
        for (PassportCity city : citys) {
            List<PassportCity> cityList = cityMap.get(city.getProvinceId());
            if (cityList == null) {
                cityList = new ArrayList<>();
                cityMap.put(city.getProvinceId(), cityList);
            }
            cityList.add(city);
        }

        JSONArray response = new JSONArray();
        for (PassportProvince province : provinces) {
            JSONObject provinceData = JSONObject.parseObject(JSONObject.toJSONString(province));

            List<PassportCity> cityList = cityMap.get(province.getId());
            JSONArray cityArray = new JSONArray();
            if (!CommonUtils.isEmpty(cityList)) {
                for (PassportCity city : cityList) {
                    JSONObject cityData = JSONObject.parseObject(JSONObject.toJSONString(city));

                    List<PassportArea> districtList = districtMap.get(city.getId());
                    JSONArray districtArray = new JSONArray();
                    if (!CommonUtils.isEmpty(districtList)) {
                        for (PassportArea district : districtList) {
                            JSONObject districtData = JSONObject.parseObject(JSONObject.toJSONString(district));

                            List<PassportStreet> streetList = streetMap.get(district.getId());
                            JSONArray streetArray = new JSONArray();
                            if (!CommonUtils.isEmpty(streetList)) {
                                for (PassportStreet street : streetList) {
                                    JSONObject streetData = JSONObject.parseObject(JSONObject.toJSONString(street));

                                    streetArray.add(streetData);
                                }
                            }
                            districtData.put("streetData", streetArray);
                            districtArray.add(districtData);
                        }
                    }
                    cityData.put("districts", districtArray);
                    cityArray.add(cityData);
                }
            }
            provinceData.put("citys", cityArray);
            response.add(provinceData);
        }
        return success(response);
    }

    @Override
    public JSONObject loaderProvinces() {
        List<PassportProvince> provinces = locationDataAccessManager.loaderProvinces();

        JSONArray response = provinces.stream().map(province -> JSONObject.parseObject(JSONObject.toJSONString(province))).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    @Override
    public JSONObject loaderCitys() {
        long provinceId = getLongParameter("provinceId", 0);
        List<PassportCity> citys = locationDataAccessManager.loaderCitys(provinceId);

        JSONArray response = citys.stream().map(city -> JSONObject.parseObject(JSONObject.toJSONString(city))).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    @Override
    public JSONObject loaderDistricts() {
        long cityId = getLongParameter("cityId", 0);
        List<PassportArea> districts = locationDataAccessManager.loaderDistricts(cityId);

        JSONArray response = districts.stream().map(district -> JSONObject.parseObject(JSONObject.toJSONString(district))).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    @Override
    public JSONObject loaderStreets() {
        long districtId = getLongParameter("districtId", 0);
        List<PassportStreet> streets = locationDataAccessManager.loaderStreets(districtId);

        JSONArray response = streets.stream().map(street -> JSONObject.parseObject(JSONObject.toJSONString(street))).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    @Override
    public JSONObject getStreet() {
        long streetId = getLongParameter("streetId", 0);
        PassportStreet street = locationDataAccessManager.getStreet(streetId);

        return result(street != null ? street : "没有该街道信息");
    }

    @Override
    public JSONObject searchProvinceByName() {
        String name = getUTF("name");
        PassportProvince province = locationDataAccessManager.searchProvinceByName(name);

        return result(province != null ? province : "没有该省份");
    }

    @Override
    public JSONObject searchCityByName() {
        String name = getUTF("name");
        PassportCity city = locationDataAccessManager.searchCityByName(name);

        return result(city != null ? city : "没有该城市");
    }

    @Override
    public JSONObject searchAreaByName() {
        String name = getUTF("name");
        PassportArea area = locationDataAccessManager.searchAreaByName(name);

        return result(area != null ? area : "没有该区域");
    }

    public JSONObject searchStreetByName() {
        long areaId = getLongParameter("areaId");
        String name = getUTF("name");
        PassportStreet street = locationDataAccessManager.searchStreetByName(areaId, name);
        return result(street != null ? street : "没有该街道");
    }

    public JSONObject streetEditSave() {
        long id = getLongParameter("id", 0);
        long areaId = getLongParameter("areaId");
        String name = getUTF("name");

        //检查是否重复，同一区域下，不允许出现相同名称的街道
        PassportStreet street = locationDataAccessManager.searchStreetByName(areaId, name);
        if(street != null) {
            return fail(-1, "已存在相同名称的街道");
        }

        if(id == 0) {
            //新增
            PassportStreet ps = new PassportStreet();
            ps.setAreaId(areaId);
            ps.setName(name);
            if(locationDataAccessManager.createStreet(ps) > 0) {
                return success("添加成功");
            }
            return fail(-1, "添加失败");
        } else {
            //修改
            locationDataAccessManager.updateStreet(id, name);
            return success("修改成功");
        }
    }

    public JSONObject getProvinceById() {
        long provinceId = getLongParameter("id");
        PassportProvince province = locationDataAccessManager.getProvinceById(provinceId);
        return result(province != null ? province : "没有该省份");
    }

    public JSONObject getCityById() {
        long cityId = getLongParameter("id");
        PassportCity city = locationDataAccessManager.getCityById(cityId);
        return result(city != null ? city : "没有该城市");
    }

    public JSONObject getAreaById() {
        long areaId = getLongParameter("id");
        PassportArea area = locationDataAccessManager.getAreaById(areaId);
        return result(area != null ? area : "没有该区域");
    }

    private JSONObject result(Object obj) {
        if (obj instanceof String) {
            return fail((String) obj);
        } else {
            JSONObject json = new JSONObject();
            json.put("data", obj);
            return success(json);
        }
    }
}