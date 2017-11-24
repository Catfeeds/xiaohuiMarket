package com.xlibao.saas.market.manager.service.ordermanager.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.service.ordermanager.OrderManagerService;
import com.xlibao.saas.market.manager.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Transactional
@Service("orderManagerService")
public class OrderManagerServiceImpl implements OrderManagerService {

    @Override
    public JSONObject searchPageOrders(long marketId, int orderState, String startTime, String endTime, String searchType, String searchKey, int pageSize, int pageIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConfigFactory.getDomainNameConfig().orderRemoteURL).append("/order/searchPageOrders?");
        try {
            //由于日期有空格，用HttpRequest.get时，需要将带空格的字符串，转为 UTF-8
            Utils.appendStr(sb, "startTime", URLEncoder.encode(startTime, "UTF-8"));
            Utils.appendStr(sb, "endTime", URLEncoder.encode(endTime, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Utils.appendLong(sb, "marketId", marketId);
        Utils.appendInt(sb, "orderState", orderState);
        Utils.appendInt(sb, "pageSize", pageSize);
        Utils.appendInt(sb, "pageIndex", pageIndex);

        Utils.appendStr(sb, "searchType", searchType);
        Utils.appendStr(sb, "searchKey", searchKey);

        Utils.appendInt(sb, "pageSize", pageSize);
        Utils.appendInt(sb, "pageIndex", pageIndex);

        String json = HttpRequest.get(sb.toString());
        return JSONObject.parseObject(json);
    }
}