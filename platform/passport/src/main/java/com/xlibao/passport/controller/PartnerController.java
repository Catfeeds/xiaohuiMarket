package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.passport.service.partner.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/12.
 */
@Controller
@RequestMapping(value = "/passport/partner")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    /**
     * <pre>
     *     <b>验证合作商户的签名</b>
     *
     *     <b>请求地址：</b> http://domainName/partner/signatureSecurity
     *     <b>请求方式：</b> get/post 推荐使用<b>post</b>
     *
     *     <b>请求参数：</b>
     *          <b>partnerId</b> -- String 我方分配给合作商户的商户ID，以xlb908开头
     *          <b>appId</b> -- String 我方分配给合作商户的对应的应用ID，以908开头
     *          <b>signature</b> -- String 合作方加密后的内容，具体参考加密规则{@linkplain com.xlibao.common.GlobalConstantConfig#signatureRule}
     *          <b>signatureParameters</b> -- String 进行签名的参数内容，<b>注意：</b>参与签名的参数中不要包含signature、appkey等敏感信息
     *
     *     <b>返回结果：</b> 仅返回结果描述 标志码为0时表示成功 其他值表示失败；可直接读取msg内容
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "signatureSecurity")
    public JSONObject signatureSecurity() {
        return partnerService.signatureSecurity();
    }

    /**
     * <pre>
     *     <b>通用方法，提供给使用者生成加密字符串</b>
     *
     *     <b>请求地址：</b> http://domainName/partner/signatureParameters
     *     <b>请求方式：</b> get/post 推荐使用<b>post</b>
     *
     *     <b>data</b> -- JSONObject 准备进行加密的数据，必须包含固定参数：partnerId、appId
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "signatureParameters")
    public JSONObject signatureParameters() {
        return partnerService.signatureParameters();
    }
}