package com.xlibao.passport.data.mapper.location;

import com.xlibao.metadata.passport.PassportProvince;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassportProvinceMapper {

    List<PassportProvince> loaderProvinces();

    PassportProvince searchProvinceByName(@Param("name") String name);

    PassportProvince getProvinceById(@Param("id") long id);
}