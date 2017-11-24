package com.xlibao.passport.service.partner;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/6/9.
 */
public interface TencentService {

    JSONObject weixinAuthorization();

    JSONObject perfectWeixinInformation();

    JSONObject weixinBindingCellNumber();
}