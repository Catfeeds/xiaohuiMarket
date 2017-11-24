package com.xlibao.passport.data.mapper.location;

import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassportCityMapper {

    List<PassportCity> loaderCitys(@Param("provinceId") long provinceId);

    PassportCity searchCityByName(@Param("name") String name);

    PassportCity getCityById(@Param("id") long id);
}