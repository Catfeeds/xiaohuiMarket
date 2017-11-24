package com.xlibao.metadata.passport;

import java.util.Date;

public class Passport {

    /** 通行证ID 全体系内唯一 */
    private Long id;
    /** 默认登录用户名 */
    private String defaultName;
    /** 登录密码(同账户唯一，特殊时由具体的逻辑验证) */
    private String password;
    /** 用户期望展示的名字(视应用而定) */
    private String showName;
    /** 用户的真实姓名(主要是实名认证使用，实名认证通过后不可修改) */
    private String realName;
    /** 头像 */
    private String headImage;
    /** 身份证号(主要是实名认证使用，实名认证通过后不可修改) */
    private String idNumber;
    /** 用户联系号码，必选且必须有效(丢失时可修改) */
    private String phoneNumber;
    /** 用户性别，实名认证通过后不可修改 */
    private Byte sex;
    /** 用户类型(普通用户，VIP用户等，由逻辑设定) */
    private Integer type;
    /** 当前用户状态(禁言、禁登、黑名单等) */
    private Integer status;
    /** 建立时间 */
    private Date createTime;
    /** 渠道来源(如：官网、第三方渠道等；用于分析推广方式) */
    private Long fromChannel;
    /** 设备类型(分析用户群，可针对不同用户开展不同的活动 */
    private Integer deviceType;
    /** 设备名 */
    private String deviceName;
    /** 系统分配的令牌(登录后分配，120分钟(或其他配置的时长)有效期) */
    private String accessToken;
    /** token的失效时间(非建立时间) */
    private Date tokenInvalidTime;
    /** 最后登录使用的版本号 */
    private int versionIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName == null ? null : defaultName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName == null ? null : showName.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(Long fromChannel) {
        this.fromChannel = fromChannel;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getTokenInvalidTime() {
        return tokenInvalidTime;
    }

    public void setTokenInvalidTime(Date tokenInvalidTime) {
        this.tokenInvalidTime = tokenInvalidTime;
    }

    public int getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(int versionIndex) {
        this.versionIndex = versionIndex;
    }

    @Override
    public String toString() {
        return "id " + id + " name " + defaultName;
    }
}