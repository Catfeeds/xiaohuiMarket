package com.xlibao.saas.market.shop.service.support.remote;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.support.BasicRemoteService;
import com.xlibao.saas.market.shop.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/8/13.
 */
public class MarketRemoteService extends BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(MarketRemoteService.class);

    public static JSONObject marketResponse(long passportId, byte responseStatus) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("responseStatus", String.valueOf(responseStatus));

        String url = ConfigFactory.getDomainNameConfig().marketRemoteURL + "/market/openapi/marketResponse.do";
        JSONObject response = executor(url, parameters);

        logger.info(passportId + "[" + responseStatus + "]修改商店状态结果：" + response);
        return response;
    }
}