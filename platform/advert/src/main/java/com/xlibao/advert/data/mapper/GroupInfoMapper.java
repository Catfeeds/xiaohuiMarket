package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.GroupInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GroupInfo record);

    int insertSelective(GroupInfo record);

    GroupInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GroupInfo record);

    int updateByPrimaryKey(GroupInfo record);

    List<GroupInfo> searchGroupPage(@Param("groupName")String groupName,@Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int searchGroupPageCount(@Param("groupName")String groupName);

    List<GroupInfo> getAllGroup();

}