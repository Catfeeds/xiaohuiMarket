package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import com.xlibao.passport.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/7/21.
 */
@Controller
@RequestMapping(value = "/passport/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @ResponseBody
    @RequestMapping("loaderLocations")
    public JSONObject loaderLocations() {
        return locationService.loaderLocations();
    }

    @ResponseBody
    @RequestMapping("loaderProvinces")
    public JSONObject loaderProvinces() {
        return locationService.loaderProvinces();
    }

    @ResponseBody
    @RequestMapping("loaderCitys")
    public JSONObject loaderCitys() {
        return locationService.loaderCitys();
    }

    @ResponseBody
    @RequestMapping("loaderDistricts")
    public JSONObject loaderDistricts() {
        return locationService.loaderDistricts();
    }

    @ResponseBody
    @RequestMapping("loaderStreets")
    public JSONObject loaderStreets() {
        return locationService.loaderStreets();
    }

    @ResponseBody
    @RequestMapping("getStreet")
    public JSONObject getStreet() {
        return locationService.getStreet();
    }


    @ResponseBody
    @RequestMapping("searchProvinceByName")
    public JSONObject searchProvinceByName() {
        return locationService.searchProvinceByName();
    }

    @ResponseBody
    @RequestMapping("searchCityByName")
    public JSONObject searchCityByName() {
        return locationService.searchCityByName();
    }

    @ResponseBody
    @RequestMapping("searchAreaByName")
    public JSONObject searchAreaByName() {
        return locationService.searchAreaByName();
    }

    @ResponseBody
    @RequestMapping("searchStreetByName")
    public JSONObject searchStreetByName() {
        return locationService.searchStreetByName();
    }

    @ResponseBody
    @RequestMapping(value = "/streetEditSave", method = RequestMethod.POST)
    public JSONObject streetEditSave() {
        return locationService.streetEditSave();
    }

    @ResponseBody
    @RequestMapping("getProvinceById")
    public JSONObject getProvinceById() {
        return locationService.getProvinceById();
    }

    @ResponseBody
    @RequestMapping("getCityById")
    public JSONObject getCityById() {
        return locationService.getCityById();
    }

    @ResponseBody
    @RequestMapping("getAreaById")
    public JSONObject getAreaById() {
        return locationService.getAreaById();
    }
}