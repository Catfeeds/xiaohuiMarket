package com.xlibao.saas.market.manager.controller;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.xlibao.common.file.oss.AliyunConfig;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.LogicConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhumg on 9/5.
 */
@Controller
@RequestMapping(value = "/marketmanager/oss")
public class OssController extends BaseController {

    @RequestMapping("/index")
    public String index(ModelMap map) {
        return jumpPage(map, LogicConfig.FTL_SYS_OSS, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TYPE);
    }

    @ResponseBody
    @RequestMapping("/uploadImg")
    public JSONObject uploadImg() {

        //店铺market, 商品item, 分类图标itemtype
        String targetDir = getUTF("targetDir");

        String accessId = "LTAIzupnl78sRpBm";//AliyunConfig.getAccessKeyId();
        String accessKey = "4zbTwU9ghdfpmr2XPE8GHQsDeIv3eP";//AliyunConfig.getAccessKeySecret();
        String bucket = "xmarket";
        String dir = targetDir + "/";
        String endpoint = AliyunConfig.getOssPublicEndPoint();
        String host = "http://" + bucket + "." + endpoint;

        OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            JSONObject result = new JSONObject();

            result.put("accessid", accessId);
            result.put("policy", encodedPolicy);
            result.put("signature", postSignature);
            //respMap.put("expire", formatISO8601Date(expiration));
            result.put("dir", dir);
            result.put("host", host);
            result.put("expire", String.valueOf(expireEndTime / 1000));

            getHttpServletResponse().setHeader("Access-Control-Allow-Origin", "*");
            getHttpServletResponse().setHeader("Access-Control-Allow-Methods", "GET, POST");

            return result;

        } catch (Exception e) {
            //Assert.fail(e.getMessage());
            fail("OSS验签失败！");
        }

        return null;
    }

}
