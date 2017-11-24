package com.xlibao.passport.data.mapper.location;

import com.xlibao.metadata.item.ItemUnit;
import com.xlibao.metadata.passport.PassportStreet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PassportStreetMapper {

    List<PassportStreet> loaderStreets(@Param("districtId") long districtId);

    PassportStreet getStreet(@Param("streetId") long streetId);

    PassportStreet searchStreetByName(@Param("areaId") long areaId, @Param("name") String name);

    void updateStreet(@Param("streetId") long streetId, @Param("name") String name);

    int createStreet(PassportStreet street);
}