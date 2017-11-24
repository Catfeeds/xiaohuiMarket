package com.xlibao.passport.service.address;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/3/29.
 */
public interface AddressService {

    JSONObject newAddress();

    JSONObject removeAddress();

    JSONObject setDefaultAddress();

    JSONObject modifyAddress();

    JSONObject getAddress();

    JSONObject getDefaultAddress();

    JSONObject getAddresses();
}
