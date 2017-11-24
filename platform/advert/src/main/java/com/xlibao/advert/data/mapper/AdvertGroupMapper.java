package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.AdvertGroup;

public interface AdvertGroupMapper {
    int deleteByPrimaryKey(Long advertGroupId);

    int insert(AdvertGroup record);

    int insertSelective(AdvertGroup record);

    AdvertGroup selectByPrimaryKey(Long advertGroupId);

    int updateByPrimaryKeySelective(AdvertGroup record);

    int updateByPrimaryKey(AdvertGroup record);

    int delAdvertGroup(AdvertGroup advertGroup);
}