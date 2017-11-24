package com.market.courier.response;

/**
 * Created by zhumg on 9/5.
 */
public class Passport {


    private long passportId;
    private String accessToken;
    private String showName;
    private String phoneNumber;//隐藏phone，明文，不用显示
    private String showPhoneNumber;//加密的phone，显示用
    private VersionInfo versionMessage;
    private int roleValue;
    private String loginName;//登陆后，客户端自己注入

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getShowPhoneNumber() {
        if(showPhoneNumber == null) {
            return "";
        }
        return showPhoneNumber;
    }

    public void setShowPhoneNumber(String showPhoneNumber) {
        this.showPhoneNumber = showPhoneNumber;
    }

    public int getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(int roleValue) {
        this.roleValue = roleValue;
    }

    public VersionInfo getVersionMessage() {
        return versionMessage;
    }

    public void setVersionMessage(VersionInfo versionMessage) {
        this.versionMessage = versionMessage;
    }

    public String getPhoneNumber() {
        if(phoneNumber == null) {
            return "";
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportIdStr() {
        return String.valueOf(passportId);
    }

    public long getPassportId() {
        return passportId;
    }

    public void setPassportId(long passportId) {
        this.passportId = passportId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

}
