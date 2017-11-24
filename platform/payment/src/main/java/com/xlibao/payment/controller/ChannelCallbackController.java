package com.xlibao.payment.controller;

import com.xlibao.common.BasicWebService;
import com.xlibao.payment.service.channel.alibaba.AlipayPayment;
import com.xlibao.payment.service.channel.tencent.TencentPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/2.
 */
@Controller
@RequestMapping(value = "/payment/channelCallback")
public class ChannelCallbackController extends BasicWebService {

    @Autowired
    private TencentPayment tencentPayment;
    @Autowired
    private AlipayPayment alipayPayment;

    /**
     * <pre>
     *     <b>微信支付回调接口</b>
     * </pre>
     *
     * @deprecated - <b>特别说明</b>：仅供回调使用，内部系统不可调用
     */
    @ResponseBody
    @RequestMapping(value = "notifyWeixinNativePayment")
    public void notifyWeixinNativePayment() {
        tencentPayment.notifyWeixinNativePayment();
    }

    @ResponseBody
    @RequestMapping(value = "notifyWeixinRefund")
    public void notifyWeixinRefund() {
        tencentPayment.notifyWeixinRefund();
    }

    /**
     * <pre>
     *     <b>支付宝支付回调接口</b>
     * </pre>
     *
     * @deprecated - <b>特别说明</b>：仅供回调使用，内部系统不可调用
     */
    @ResponseBody
    @RequestMapping(value = "notifyAlipayNativePayment")
    public void notifyAlipayNativePayment() {
        alipayPayment.notifyAlipayNativePaymented();
    }
}