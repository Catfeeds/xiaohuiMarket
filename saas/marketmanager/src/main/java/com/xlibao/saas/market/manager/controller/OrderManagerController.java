package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.LogicConfig;
import com.xlibao.saas.market.manager.service.ordermanager.OrderManagerService;
import com.xlibao.saas.market.manager.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/marketmanager/order")
public class OrderManagerController extends BaseController {

    @Autowired
    private OrderManagerService orderManagerService;

    @RequestMapping("/orders")
    public String searchPageOrders(ModelMap map) {

        //店铺ID
        long marketId = getLongParameter("marketId", -1);
        int orderState = getIntParameter("orderState", -1);
        String startTime = getUTF("sTime", "");
        String endTime = getUTF("eTime", "");
        String searchType = getUTF("searchType", "");
        String searchKey = getUTF("searchKey", "");

        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject orderJson = orderManagerService.searchPageOrders(marketId, orderState, startTime, endTime, searchType, searchKey, pageSize, pageIndex);

        if (orderJson.getIntValue("code") != 0) {
            return remoteFail(map, orderJson, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_UNIT);
        }

        JSONObject response = orderJson.getJSONObject("response");
        JSONArray array = response.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            //重置日期
            Utils.changeData(jsonObject, "createTime");
            Utils.changeData(jsonObject, "paymentTime");
            Utils.changeData(jsonObject, "confirmTime");

            jsonObject.put("stateName", OrderStatusEnum.getOrderStatusEnum(jsonObject.getInteger("status")).getValue());
        }

        map.put("marketId", marketId);
        map.put("orderState", orderState);
        map.put("searchType", searchType);
        map.put("searchKey", searchKey);
        map.put("pageIndex", pageIndex);
        map.put("sTime",startTime);
        map.put("eTime",endTime);
        map.put("orders", response.getJSONArray("data"));
        map.put("count", response.getIntValue("count"));

        return jumpPage(map, LogicConfig.FTL_ORDER_LIST, LogicConfig.TAB_ORDER, LogicConfig.TAB_ORDER_LIST);
    }
}
