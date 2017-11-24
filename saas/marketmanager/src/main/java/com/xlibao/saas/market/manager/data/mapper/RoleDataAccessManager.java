package com.xlibao.saas.market.manager.data.mapper;

import com.xlibao.saas.market.manager.data.model.RoleEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDataAccessManager {

    @Autowired
    private RoleEntryMapper roleEntryMapper;

    public List<RoleEntry> searchRoles(RoleEntry searchModel, int pageSize, int pageStartIndex){
        return roleEntryMapper.searchRoles(searchModel,pageSize,pageStartIndex);
    }

    public int roleCount(){
        return roleEntryMapper.roleCount();
    }

    public int createRole(RoleEntry entry) {
        return  roleEntryMapper.createRole(entry);
    }

    public int updateRole(RoleEntry entry){
        return  roleEntryMapper.updateRole(entry);
    }

    public RoleEntry searchRoleById(long id){
        return roleEntryMapper.searchRoleEntryById(id);
    }

    public int roleDel(RoleEntry entry){
        return roleEntryMapper.roleDel(entry);
    }
}