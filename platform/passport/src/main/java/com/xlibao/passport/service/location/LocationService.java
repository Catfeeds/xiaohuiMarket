package com.xlibao.passport.service.location;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;

/**
 * @author chinahuangxc on 2017/7/21.
 */
public interface LocationService {

    JSONObject loaderLocations();

    JSONObject loaderProvinces();

    JSONObject loaderCitys();

    JSONObject loaderDistricts();

    JSONObject loaderStreets();

    JSONObject getStreet();

    JSONObject searchProvinceByName();

    JSONObject searchCityByName();

    JSONObject searchAreaByName();

    JSONObject searchStreetByName();

    JSONObject streetEditSave();

    JSONObject getProvinceById();

    JSONObject getCityById();

    JSONObject getAreaById();
}
