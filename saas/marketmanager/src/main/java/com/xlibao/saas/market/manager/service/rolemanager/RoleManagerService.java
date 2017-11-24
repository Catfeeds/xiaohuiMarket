package com.xlibao.saas.market.manager.service.rolemanager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.manager.data.model.RoleEntry;

public interface RoleManagerService {

    /**
     * 分页查询
     * @param entry
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public JSONObject searchRoles(RoleEntry entry, int pageSize, int pageIndex);

    JSONObject roleEditSave();

    JSONObject searchRoleById(long id);

    JSONObject roleDel();
}