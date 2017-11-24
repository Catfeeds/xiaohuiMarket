package com.xlibao.saas.market.manager.service.rolemanager.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xlibao.common.BasicWebService;
import com.xlibao.saas.market.manager.data.mapper.RoleDataAccessManager;
import com.xlibao.saas.market.manager.data.model.OperationEntry;
import com.xlibao.saas.market.manager.data.model.RoleEntry;
import com.xlibao.saas.market.manager.service.rolemanager.RoleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service("roleManagerService")
public class RoleManagerServiceImpl extends BasicWebService implements RoleManagerService {

    @Autowired
    private RoleDataAccessManager roleDataAccessManager;

    @Override
    public JSONObject searchRoles(RoleEntry entry, int pageSize, int pageIndex) {
        List<RoleEntry> operations = roleDataAccessManager.searchRoles(entry , pageSize,pageIndex);
        int count = roleDataAccessManager.roleCount();

        JSONObject response = new JSONObject();
        response.put("data", operations);
        response.put("count", count);

        return success(response);
    }

    @Override
    public JSONObject roleEditSave() {
        long roleId = getLongParameter("roleId", 0);

        String roleTitle = getUTF("roleTitle");
        String roleName = getUTF("roleName");
        int status = getIntParameter("status");
        String explain = getUTF("explain");

        RoleEntry entry = new RoleEntry();
        entry.setRoleName(roleName);
        entry.setRoleTitle(roleTitle);
        entry.setStatus(status);
        entry.setIsDelete(1);
        entry.setExplain(explain);
        entry.setCreateTime(new Date());
        entry.setUpdateTime(new Date());

        if(roleId > 0) {
            // 编辑
            entry.setId(roleId);
            if(roleDataAccessManager.updateRole(entry) > 0)
                return success("编辑成功");

            return fail("编辑失败");
        } else {
            // 新增
            if(roleDataAccessManager.createRole(entry) > 0)
                return success("添加成功");

            return fail("添加失败");
        }
    }

    @Override
    public JSONObject searchRoleById(long id) {
        RoleEntry entry = roleDataAccessManager.searchRoleById(id);
        if(entry == null)
            return fail("未找到权限信息");

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(entry ,SerializerFeature.WriteMapNullValue));

        return response;
    }

    @Override
    public JSONObject roleDel() {
        long roleId = getLongParameter("roleId", 0);

        RoleEntry entry = new RoleEntry();
        entry.setId(roleId);
//        entry.setIsDelete(0);
        entry.setUpdateTime(new Date());

        if(roleDataAccessManager.roleDel(entry) > 0)
            return success("删除成功");

        return fail("删除失败");
    }
}