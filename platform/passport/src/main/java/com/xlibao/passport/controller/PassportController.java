package com.xlibao.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.passport.service.passport.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/2/7.
 */
@Controller
@RequestMapping(value = "/passport")
public class PassportController extends BasicWebService {

    @Autowired
    private PassportService passportService;

    /**
     * <pre>
     *     <b>注册通行证</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/registerPassport
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>访问参数：</b>
     *          <b>name</b> - String 登录用户名，必填参数。
     *          <b>password</b> - String 登录密码，必填参数。
     *          <b>showName</b> - String 用于展示的名字，非必填参数；默认与name一致。
     *          <b>realName</b> - String 真实姓名，非必填参数；实名验证时必填。
     *          <b>idNumber</b> - String 身份证号，非必填参数；实名验证时必填。
     *          <b>phoneNumber</b> - String 联系号码，必填参数；可作为另一个登录帐号。
     *          <b>smsCode</b> - String 短信验证码，必填参数。
     *          <b>sex</b> - byte 性别，非必填参数，具体参考：{@linkplain com.xlibao.common.GlobalAppointmentOptEnum#MALE} 男性、
     *                              {@linkplain com.xlibao.common.GlobalAppointmentOptEnum#FEMALE} 女性；
     *                              默认为{@linkplain com.xlibao.common.GlobalAppointmentOptEnum#FEMALE} 女性。
     *          <b>type</b> - int 用户类型，非必填参数；参考{@linkplain com.xlibao.common.constant.passport.PassportTypeEnum}；
     *                              默认为{@linkplain com.xlibao.common.constant.passport.PassportTypeEnum#DEFAULT}。
     *          <b>fromChannel</b> - long 来源渠道，必填参数；具体由数据库分配，默认为官方渠道，参考：Channel
     *          <b>deviceType</b> - int 设备类型，非必填参数；具体参考：{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum}
     *                              默认为{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum#DEVICE_TYPE_ANDROID} 安卓。
     *          <b>deviceName</b> - String 设备名字，非必填参数；默认为：Android。
     *          <b>versionIndex</b> - int 当前使用的版本号，必填参数；<b>注意：</b>版本号区分两种情况，1、用于展示的版本号 2、内部使用的版本号；本处指内部使用的版本号
     *
     *     <b>返回结果：</b>
     *          <b>passportId</b> - long 通行证ID
     *          <b>showName</b> - String 可用于展示的名字
     *          <b>phoneNumber</b> - String 手机号（已隐藏部分字符，前端直接展示即可）
     *          <b>headImage</b> - String 头像地址
     *          <b>roleValue</b> - int 角色类型值，参考：{@link com.xlibao.common.constant.passport.PassportRoleTypeEnum}
     *          <b>accessToken</b> - String 访问令牌(暂时未使用)
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "registerPassport")
    public JSONObject registerPassport() {
        return passportService.registerPassport();
    }

    /**
     * <pre>
     *     <b>登录通行证</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/loginPassport
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>访问参数：</b>
     *
     *     <b>username</b> - String 登录用户名(自定义用户名或手机号或其他已绑定的帐号)，必填参数。
     *     <b>password</b> - String 登录密码，必填参数。
     *     <b>deviceType</b> - int 设备类型，必填参数；参考：{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum}。
     *     <b>clientType</b> - int 客户端类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.passport.ClientTypeEnum}，
     *                      默认值：{@link com.xlibao.common.constant.passport.ClientTypeEnum#CONSUMER}。
     *     <b>versionIndex</b> - int 当前的版本标志，必填参数；(内部版本号，一般递增，初始为1，此部分的信息主要由前端决定)。
     *
     *     <b>返回结果：</b>
     *          <b>passportId</b> - long 通行证ID
     *          <b>showName</b> - String 可用于展示的名字
     *          <b>phoneNumber</b> - String 手机号（已隐藏部分字符，前端直接展示即可）
     *          <b>headImage</b> - String 头像地址
     *          <b>roleValue</b> - int 角色类型值，参考：{@link com.xlibao.common.constant.passport.PassportRoleTypeEnum}
     *          <b>accessToken</b> - String 访问令牌(暂时未使用)
     *
     *          <b>versionMessage</b> - JSONObject 版本信息
     *              <b>upgradeType</b> - int 更新的类型，参考：{@linkplain com.xlibao.common.constant.version.VersionTypeEnum}
     *              <b>title</b> - String 更新标题
     *              <b>showVersion</b> - String 用于展示前端的版本号，如：V1.0
     *
     *              当upgradeType为<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#UN_FORCE_UPGRADE}</b>或<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#FORCE_UPGRADE}</b>
     *                  <b>versionIndex</b> - int 最新内部版本号
     *                  <b>introduce</b> - String 版本更新提示内容
     *                  <b>versionUrl</b> - String 新版本的下载地址
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "loginPassport")
    public JSONObject loginPassport() {
        return passportService.loginPassport();
    }

    /**
     * <pre>
     *     <b>通过验证码登录</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/loginForVerificationCode
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>访问参数：</b>
     *
     *     <b>phone</b> - String 登录手机号，必填参数。
     *     <b>smsCode</b> - String 登录验证码，必填参数；验证码通过 {@link SmsController#requestVerificationCode()}进行获取
     *     <b>deviceType</b> - int 设备类型，必填参数；参考：{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum}。
     *     <b>clientType</b> - int 客户端类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.passport.ClientTypeEnum}，
     *                      默认值：{@link com.xlibao.common.constant.passport.ClientTypeEnum#CONSUMER}。
     *     <b>versionIndex</b> - int 当前的版本标志，必填参数；(内部版本号，一般递增，初始为1，此部分的信息主要由前端决定)。
     *
     *     <b>返回结果：</b>
     *          <b>passportId</b> - long 通行证ID
     *          <b>showName</b> - String 可用于展示的名字
     *          <b>phoneNumber</b> - String 手机号（已隐藏部分字符，前端直接展示即可）
     *          <b>headImage</b> - String 头像地址
     *          <b>roleValue</b> - int 角色类型值，参考：{@link com.xlibao.common.constant.passport.PassportRoleTypeEnum}
     *          <b>accessToken</b> - String 访问令牌(暂时未使用)
     *
     *          <b>versionMessage</b> - JSONObject 版本信息
     *              <b>upgradeType</b> - int 更新的类型，参考：{@linkplain com.xlibao.common.constant.version.VersionTypeEnum}
     *              <b>title</b> - String 更新标题
     *              <b>showVersion</b> - String 用于展示前端的版本号，如：V1.0
     *
     *              当upgradeType为<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#UN_FORCE_UPGRADE}</b>或<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#FORCE_UPGRADE}</b>
     *                  <b>versionIndex</b> - int 最新内部版本号
     *                  <b>introduce</b> - String 版本更新提示内容
     *                  <b>versionUrl</b> - String 新版本的下载地址
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "loginForVerificationCode")
    public JSONObject loginForVerificationCode() {
        return passportService.loginForVerificationCode();
    }

    /**
     * <pre>
     *     <b>退出登录</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/logoutPassport
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>访问参数：</b>
     *          <b>passportId</b> - long 用户通行证ID，必填参数。
     *          <b>accessToken</b> - String 访问令牌，必填参数。
     *
     *     <b>返回：</b>仅返回退出成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "logoutPassport")
    public JSONObject logoutPassport() {
        return passportService.logoutPassport();
    }

    /**
     * <pre>
     *     <b>修改密码</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/modifyPassword
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *
     *     <b>username</b> - String 用户名，必填参数。
     *     <b>oldPassword</b> - String 原密码，必填参数。
     *     <b>newPassword</b> - String 新密码，必填参数。
     *     <b>confirmPassword</b> - String 确认密码，必填参数。
     *
     *     <b>返回结果：</b>仅提示成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "modifyPassword")
    public JSONObject modifyPassword() {
        return passportService.modifyPassword();
    }

    @ResponseBody
    @RequestMapping(value = "modifyNickname")
    public JSONObject modifyNickname() {
        return passportService.modifyNickname();
    }

    /**
     * <pre>
     *     <b>忘记密码</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/forgetPassword
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>phoneNumber</b> - String 电话号码，必填参数。
     *          <b>smsCode</b> - String 验证码，必填参数。
     *          <b>password</b> - String 新密码，必填参数。
     *          <b>confirmPassword</b> - String 确认棉麻，必填参数。
     *
     *     <b>返回结果：</b>仅提示成功或失败
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "forgetPassword")
    public JSONObject forgetPassword() {
        return passportService.forgetPassword();
    }

    /**
     * <pre>
     *     <b>重置手机</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/resetMobileNumber
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 通行证ID，必填参数。
     *          <b>currentMobileNumber</b> - String 当前使用的手机号，必填参数；必须要求用户填写。
     *          <b>expectMobileNumber</b> - String 期望使用的手机号，必填参数；和获取验证为同一个号码。
     *          <b>smsCode</b> - String 短信验证码，必填参数；通过获取验证码接口请求后下发至手机短信。
     *
     *     <b>返回：</b>重置结果
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "resetMobileNumber")
    public JSONObject resetMobileNumber() {
        return passportService.resetMobileNumber();
    }

    /**
     * <pre>
     *     <b>实名认证</b>
     * </pre>
     *
     * @deprecated 预留方法 暂时不用
     */
    @ResponseBody
    @RequestMapping(value = "authentication")
    public JSONObject authentication() {
        return passportService.authentication();
    }

    /**
     * <pre>
     *     <b>版本更新</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/versionUpgrade
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>deviceType</b> - int 设备类型，必填参数；参考：
     *                  {@linkplain com.xlibao.common.constant.device.DeviceTypeEnum#DEVICE_TYPE_ANDROID}
     *                  {@linkplain com.xlibao.common.constant.device.DeviceTypeEnum#DEVICE_TYPE_IOS}。
     *          <b>clientType</b> - int 客户端类型，必填参数；参数：{@link com.xlibao.common.constant.passport.ClientTypeEnum}。
     *          <b>versionIndex</b> - int 当前的版本标志，必填参数；(内部版本号，一般递增，初始为1，此部分的信息主要由前端决定)。
     *
     *     <b>返回结果：</b>
     *          <b>upgradeType</b> - int 更新的类型，参考：{@linkplain com.xlibao.common.constant.version.VersionTypeEnum}
     *          <b>title</b> - String 更新标题
     *          <b>showVersion</b> - String 用于展示前端的版本号，如：V1.0
     *
     *          当upgradeType为<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#UN_FORCE_UPGRADE}</b>或<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#FORCE_UPGRADE}</b>
     *              <b>versionIndex</b> - int 最新内部版本号
     *              <b>introduce</b> - String 版本更新提示内容
     *              <b>versionUrl</b> - String 新版本的下载地址
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "versionUpgrade")
    public JSONObject versionUpgrade() {
        return passportService.versionUpgrade();
    }

    /**
     * <pre>
     *     <b>确认手机号是否已被使用</b>
     *
     *     <b>访问地址：</b>http://domainName/passport/phoneBeUsed
     *     <b>访问方式：</b>GET/POST，推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>phoneNumber</b> - String 需要验证的手机号
     *
     *     <b>返回：</b>仅返回是否被使用的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "phoneBeUsed")
    public JSONObject phoneBeUsed() {
        return passportService.phoneBeUsed();
    }

    @ResponseBody
    @RequestMapping(value = "changeAccessToken")
    public JSONObject changeAccessToken() {
        return passportService.changeAccessToken();
    }

    @ResponseBody
    @RequestMapping(value = "extendAccessToken")
    public JSONObject extendAccessToken() {
        return passportService.extendAccessToken();
    }
}