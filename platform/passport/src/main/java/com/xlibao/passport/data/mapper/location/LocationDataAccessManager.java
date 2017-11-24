package com.xlibao.passport.data.mapper.location;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.metadata.passport.PassportStreet;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chinahuangxc on 2017/7/20.
 */
@Component
public class LocationDataAccessManager {

    @Autowired
    private PassportProvinceMapper provinceMapper;
    @Autowired
    private PassportCityMapper cityMapper;
    @Autowired
    private PassportAreaMapper areaMapper;
    @Autowired
    private PassportStreetMapper streetMapper;

    public List<PassportProvince> loaderProvinces() {
        return provinceMapper.loaderProvinces();
    }

    public List<PassportCity> loaderCitys(long provinceId) {
        return cityMapper.loaderCitys(provinceId);
    }

    public List<PassportArea> loaderDistricts(long cityId) {
        return areaMapper.loaderDistricts(cityId);
    }

    public List<PassportStreet> loaderStreets(long districtId) {
        return streetMapper.loaderStreets(districtId);
    }

    public PassportStreet getStreet(long streetId) {
        return streetMapper.getStreet(streetId);
    }

    public PassportProvince searchProvinceByName(String name) {
        return provinceMapper.searchProvinceByName(name);
    }

    public PassportCity searchCityByName(String name) {
        return cityMapper.searchCityByName(name);
    }

    public PassportArea searchAreaByName(String name) {
        return areaMapper.searchAreaByName(name);
    }

    public PassportStreet searchStreetByName(long areaId, String name) {
        return streetMapper.searchStreetByName(areaId, name);
    }

    public void updateStreet(long streetId, String name) {
        streetMapper.updateStreet(streetId, name);
    }

    public int createStreet(PassportStreet street) {
        return streetMapper.createStreet(street);
    }

    public PassportProvince getProvinceById(long provinceId) {
        return provinceMapper.getProvinceById(provinceId);
    }

    public PassportCity getCityById(long cityId) {
        return cityMapper.getCityById(cityId);
    }

    public PassportArea getAreaById(long areaId) {
        return areaMapper.getAreaById(areaId);
    }
}