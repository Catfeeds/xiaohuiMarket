package com.xlibao.passport.service.partner;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/2/15.
 */
public interface PartnerService {

    JSONObject signatureSecurity();

    JSONObject signatureParameters();
}