package com.xlibao.passport.data.mapper.location;

import com.xlibao.metadata.passport.PassportArea;
import com.xlibao.metadata.passport.PassportCity;
import com.xlibao.metadata.passport.PassportProvince;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassportAreaMapper {

    List<PassportArea> loaderDistricts(@Param("cityId") long cityId);

    PassportArea searchAreaByName(@Param("name") String name);

    PassportArea getAreaById(@Param("id") long id);
}