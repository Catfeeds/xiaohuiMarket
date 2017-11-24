package com.xlibao.saas.market.manager.service.ordermanager;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface OrderManagerService {
    public JSONObject searchPageOrders(long marketId, int orderState, String startTime, String endTime, String searchType, String searchKey, int pageSize, int pageIndex);
}
