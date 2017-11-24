package com.xlibao.saas.market.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.saas.market.manager.config.LogicConfig;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhumg on 8/10.
 */
public class BaseController extends BasicWebService {

    /**
     * 跳转页面
     * @param map
     * @param targetPage    目标页面，ftl文件夹里面的文件名
     * @param menuTab       选中的主菜单项
     * @param menuTabChild  选中的主菜单下的子菜单项
     * @return
     */
    public String jumpPage(ModelMap map, String targetPage, String menuTab, String menuTabChild) {
        map.put(LogicConfig.RIGHT_FTL_PAGE_URL, targetPage);
        map.put(LogicConfig.TAB_NAME, menuTab);
        map.put(LogicConfig.TAB_CHILD_NAME, menuTabChild);
        return LogicConfig.FTL_PAGE;
    }

    public String remoteFail(ModelMap map, JSONObject json, String tab, String tab_child) {
        map.put(LogicConfig.ERROR_NAME, "远程调用异常\n" + json.getString("msg"));
        return jumpPage(map, LogicConfig.FTL_PAGE, tab, tab_child);
    }

    public String fail(ModelMap map, String msg, String tab, String tab_child) {
        map.put(LogicConfig.ERROR_NAME, msg);
        return jumpPage(map, LogicConfig.FTL_PAGE, tab, tab_child);
    }

    protected int getPageSize() {
        return getIntParameter("pageSize", 12);
    }


    protected Map<String, String> getMapParameter() {
        Map<String, String[]> requestParams = getHttpServletRequest().getParameterMap();
        Map<String, String> parame = new HashMap<>();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();


            if (values.length == 1) {
                parame.put(name, values[0]);
            } else {
                parame.put(name, String.valueOf(values));
            }
        }
        return parame;
    }
}
