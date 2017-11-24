package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.data.model.RoleEntry;
import com.xlibao.saas.market.manager.service.rolemanager.RoleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/marketmanager/role")
public class RoleManagerController extends BaseController {

    @Autowired
    private RoleManagerService roleManagerService;

    /**
     * 权限列表
     * @param map
     * @return
     */
    @RequestMapping("/roles")
    public String roles(ModelMap map) {

        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject roleJSON = roleManagerService.searchRoles(new RoleEntry() , pageSize ,(pageIndex - 1) * pageSize);

        JSONObject response = roleJSON.getJSONObject("response");

        map.put("datas" , response.getJSONArray("data"));
        map.put("count" , response.getIntValue("count"));
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);

//        return jumpPage(map, LogicConfig.FTL_MARKET_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_LIST);
        return jumpPage(map, "role/role-list", "role", "roles");
    }

    //商店 编辑 页面
    @RequestMapping("/roleEdit")
    public String marketEdit(ModelMap map) {
        long id = getLongParameter("roleId", 0); // 权限ID
        if(id > 0) {
            // 编辑
            JSONObject entry = roleManagerService.searchRoleById(id);
            if (entry == null) {
                return fail(map, "无法找到权限信息,权限id=" + id, "role", "roles");
            }
            map.put("role",entry);
        } else {
            // 新增
        }

        return jumpPage(map, "role/role-edit", "role", "roles");
    }

    @ResponseBody
    @RequestMapping("/roleEditSave")
    public JSONObject marketEditSave() {
        return roleManagerService.roleEditSave();
    }

    @ResponseBody
    @RequestMapping("/roleDel")
    public JSONObject roleDel() {
        return roleManagerService.roleDel();
    }
}