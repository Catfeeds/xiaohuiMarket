package com.xlibao.passport.service.partner.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.metadata.passport.PassportPartner;
import com.xlibao.metadata.passport.PassportPartnerApplication;
import com.xlibao.passport.data.mapper.partner.PartnerDataManager;
import com.xlibao.passport.service.partner.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/15.
 */
@Service("partnerService")
public class PartnerServiceImpl extends BasicWebService implements PartnerService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerDataManager partnerDataManager;

    @Override
    public JSONObject signatureSecurity() {
        String partnerId = getUTF("partnerId");
        String appId = getUTF("appId");
        String signature = getUTF("signature");
        String signatureParameters = getUTF("signatureParameters");
        try {
            signatureParameters = URLDecoder.decode(signatureParameters, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PassportPartnerApplication app = getPartnerApp(partnerId, appId);
        // 添加签名key
        String localSign = signatureParameters + "key=" + app.getAccessKeySecret();
        boolean matchResult = CommonUtils.simpleSignature(localSign, signature);
        logger.info("验签结果：" + matchResult);
        return matchResult ? success("验签通过") : fail("验证签名失败");
    }

    @Override
    public JSONObject signatureParameters() {
        String data = getUTF("data");

        JSONObject p = JSONObject.parseObject(data);

        Map<String, String> parameters = new HashMap<>();
        for (String k : p.keySet()) {
            parameters.put(k, p.getString(k));
        }
        String partnerId = parameters.get("partnerId");
        String appId = parameters.get("appId");

        PassportPartnerApplication app = getPartnerApp(partnerId, appId);

        JSONObject response = new JSONObject();
        response.put("signature", CommonUtils.signature(parameters, app.getAccessKeySecret()));
        return success(response);
    }

    private PassportPartnerApplication getPartnerApp(String partnerId, String appId) {
        if (!partnerId.startsWith(GlobalConstantConfig.PARTNER_ID_PREFIX)) {
            logger.error("错误的商户号，partner id " + partnerId);
            throw new XlibaoIllegalArgumentException("商户号错误，商户号以“" + GlobalConstantConfig.PARTNER_ID_PREFIX + "开头”");
        }
        if (!appId.startsWith(GlobalConstantConfig.UNIQUE_PRIMARY_KEY_PREFIX)) {
            logger.error("错误的appId，app id " + appId);
            throw new XlibaoIllegalArgumentException("AppId错误，appId以“" + GlobalConstantConfig.UNIQUE_PRIMARY_KEY_PREFIX + "开头”");
        }
        long pid = Long.parseLong(partnerId.replaceFirst(GlobalConstantConfig.PARTNER_ID_PREFIX, ""));

        PassportPartner partner = partnerDataManager.getPartner(pid);
        if (partner == null) {
            logger.error("合作商户不存在，partner id(" + partnerId + ")");
            throw new XlibaoIllegalArgumentException("合作商户不存在，partnerId(" + partnerId + ")");
        }
        long aid = Long.parseLong(appId.replaceFirst(GlobalConstantConfig.UNIQUE_PRIMARY_KEY_PREFIX, ""));
        PassportPartnerApplication app = partnerDataManager.getPartnerApp(aid);
        if (app == null) {
            throw new XlibaoIllegalArgumentException("AppId错误，对应的应用信息不存在");
        }
        if (app.getPartnerId() != pid) {
            logger.error("商户信息不匹配，partner id(" + partnerId + ")，app id(" + appId + ")");
            throw new XlibaoIllegalArgumentException("您的商户信息不匹配，请联系服务人员！");
        }
        return app;
    }
}