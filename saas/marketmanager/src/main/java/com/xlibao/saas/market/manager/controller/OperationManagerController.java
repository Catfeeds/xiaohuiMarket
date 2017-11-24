package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.data.model.OperationEntry;
import com.xlibao.saas.market.manager.service.operationmanager.OperationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/marketmanager/operation")
public class OperationManagerController extends BaseController {

    @Autowired
    private OperationManagerService operationManagerService;

    /**
     * 权限列表
     * @param map
     * @return
     */
    @RequestMapping("/operations")
    public String operations(ModelMap map) {

        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject operationJSON = operationManagerService.searchOperations(new OperationEntry() , pageSize ,(pageIndex - 1) * pageSize);

        JSONObject response = operationJSON.getJSONObject("response");

        map.put("datas" , response.getJSONArray("data"));
        map.put("count" , response.getIntValue("count"));
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);

//        return jumpPage(map, LogicConfig.FTL_MARKET_LIST, LogicConfig.TAB_MARKET, LogicConfig.TAB_MARKET_LIST);
        return jumpPage(map, "operation/operation-list", "operation", "operations");
    }

    //商店 编辑 页面
    @RequestMapping("/operationEdit")
    public String marketEdit(ModelMap map) {
        long id = getLongParameter("operationId", 0); // 权限ID
        if(id > 0) {
            // 编辑
            JSONObject entry = operationManagerService.searchOperationById(id);
            if (entry == null) {
                return fail(map, "无法找到权限信息,权限id=" + id, "operation", "operations");
            }
            map.put("operation",entry);
        } else {
            // 新增
        }

        return jumpPage(map, "operation/operation-edit", "operation", "operations");
    }

    @ResponseBody
    @RequestMapping("/operationEditSave")
    public JSONObject marketEditSave() {
        return operationManagerService.operationEditSave();
    }

    @ResponseBody
    @RequestMapping("/operationDel")
    public JSONObject operationDel() {
        return operationManagerService.operationDel();
    }
}