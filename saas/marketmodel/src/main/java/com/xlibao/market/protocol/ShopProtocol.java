package com.xlibao.market.protocol;

/**
 * @author chinahuangxc on 2017/8/9.
 */
public interface ShopProtocol {

    /**
     * <pre>
     *     <b>商店安全验证</b>
     *
     *     <b>C --> S</b>
     *          String username = message.readUTF();  // 用户名，必填参数。
     *          String password = message.readUTF();  // 密码，必填参数。
     *          int deviceType = message.readInt();   // 设备类型，参考：{@link com.xlibao.common.constant.device.DeviceTypeEnum}，必填参数。
     *          int versionIndex = message.readInt(); // 当前的版本标志，必填参数；(内部版本号，一般递增，初始为1，此部分的信息主要由前端决定)。
     *
     *     <b>S --> C</b>
     *          byte result = message.readByte();   // 结果标志位，参考：{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_TRUE}、{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_FALSE}
     *          当<b>result</b>为{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_FALSE}时
     *              int code = message.readInt();   // 错误码
     *              String msg = message.readUTF(); // 错误内容
     *
     *          当<b>result</b>为{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_TRUE}时
     *              String msg = message.readUTF(); // 返回的消息内容，结构为：JSONObject
     *                  <b>passportId</b> - long 通行证ID
     *                  <b>showName</b> - String 可用于展示的名字
     *                  <b>phoneNumber</b> - String 手机号（已隐藏部分字符，前端直接展示即可）
     *                  <b>headImage</b> - String 头像地址
     *                  <b>roleValue</b> - int 角色类型值，参考：{@link com.xlibao.common.constant.passport.PassportRoleTypeEnum}
     *                  <b>accessToken</b> - String 访问令牌(暂时未使用)
     *
     *                  <b>versionMessage</b> - JSONObject 版本信息
     *                      <b>upgradeType</b> - int 更新的类型，参考：{@linkplain com.xlibao.common.constant.version.VersionTypeEnum}
     *                      <b>title</b> - String 更新标题
     *                      <b>showVersion</b> - String 用于展示前端的版本号，如：V1.0
     *
     *                      当upgradeType为<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#UN_FORCE_UPGRADE}</b>或<b>{@linkplain com.xlibao.common.constant.version.VersionTypeEnum#FORCE_UPGRADE}</b>
     *                          <b>versionIndex</b> - int 最新内部版本号
     *                          <b>introduce</b> - String 版本更新提示内容
     *                          <b>versionUrl</b> - String 新版本的下载地址
     * </pre>
     */
    short CS_SECURITY_VERIFICATION = 10000;

    /**
     * <pre>
     *
     * </pre>
     */
    short CS_HARDWARE = 10001;
}