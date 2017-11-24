package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.passport.service.partner.TencentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/8.
 */
@Controller
@RequestMapping(value = "/passport/channel")
public class ChannelController {

    @Autowired
    private TencentService tencentService;

    /**
     * <pre>
     *     <b>微信授权</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/channel/weixinAuthorization
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>weixinAuthorType</b> - int 授权方式，非必填参数；参考：{@link com.xlibao.passport.service.partner.WeixinAuthorTypeEnum}
     *          <b>code</b> - String 前端请求微信授权后获得的code，必填参数
     *
     *     <b>返回：</b>
     *          <b>passportId</b> - long 通行证ID
     *          <b>nickName</b> - String 用户昵称
     *          <b>headImageUrl</b> - String 头像地址
     *          <b>sex</b> - byte 性别，参考：{@link com.xlibao.common.GlobalAppointmentOptEnum#FEMALE}
     *          <b>status</b> - int 通行证状态，参考：{@link com.xlibao.common.constant.passport.PassportStatusEnum}，
     *                  当<b>status</b>值为{@link com.xlibao.common.constant.passport.PassportStatusEnum#UN_PERFECT_INFORMATION}时，必须执行完善资料的接口
     *          <b>openId</b> - String 微信授权后返回的用户在该公众号下的openId(同一个项目下唯一)
     *          <b>sessionKey</b> - String 微信返回的会话密钥，小程序授权时有效
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "weixinAuthorization")
    public JSONObject weixinAuthorization() {
        return tencentService.weixinAuthorization();
    }

    /**
     * <pre>
     *     <b>完善微信授权的用户资料</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/channel/perfectWeixinInformation
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>nickName</b> - String 昵称，用于显示，必填参数。
     *          <b>headImageUrl</b> - String 头像，用于显示，非必填参数。
     *          <b>sex</b> - byte 用户性别，参考：{@link com.xlibao.common.GlobalAppointmentOptEnum}；默认值为：{@link com.xlibao.common.GlobalAppointmentOptEnum#FEMALE}
     *
     *     <b>返回：</b>仅返回操作成功或失败的提示
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "perfectWeixinInformation")
    public JSONObject perfectWeixinInformation() {
        return tencentService.perfectWeixinInformation();
    }

    /**
     * <pre>
     *     <b>微信账户绑定手机号</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/channel/weixinBindingCellNumber
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>openId</b> - String 微信授权后获取的openId
     *          <b>phoneNumber</b> - String 期望绑定的手机号
     *          <b>smsCode</b> - String 收到的验证码
     *
     *     <b>返回：</b>仅返回成功或失败的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "weixinBindingCellNumber")
    public JSONObject weixinBindingCellNumber() {
        return tencentService.weixinBindingCellNumber();
    }
}