package com.xlibao.saas.market.manager.data.mapper;

import com.xlibao.saas.market.manager.data.model.RoleEntry;
import com.xlibao.saas.market.manager.data.model.RoleEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleEntryMapper {

    List<RoleEntry> searchRoles(@Param("searchModel") RoleEntry searchModel, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int roleCount();

    int createRole(RoleEntry entry);

    int updateRole(RoleEntry entry);

    RoleEntry searchRoleEntryById(@Param("id") long id);

    int roleDel(RoleEntry entry);
}