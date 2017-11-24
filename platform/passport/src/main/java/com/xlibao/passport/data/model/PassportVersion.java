package com.xlibao.passport.data.model;

import java.util.Date;

public class PassportVersion {

    private Long id;
    private Integer deviceType;
    private Integer clientType;
    private Integer versionIndex;
    private String showVersion;
    private Integer minVersionIndex;
    private String versionUrl;
    private String versionIntroduce;
    private Date openTime;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(Integer versionIndex) {
        this.versionIndex = versionIndex;
    }

    public String getShowVersion() {
        return showVersion;
    }

    public void setShowVersion(String showVersion) {
        this.showVersion = showVersion == null ? null : showVersion.trim();
    }

    public Integer getMinVersionIndex() {
        return minVersionIndex;
    }

    public void setMinVersionIndex(Integer minVersionIndex) {
        this.minVersionIndex = minVersionIndex;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl == null ? null : versionUrl.trim();
    }

    public String getVersionIntroduce() {
        return versionIntroduce;
    }

    public void setVersionIntroduce(String versionIntroduce) {
        this.versionIntroduce = versionIntroduce == null ? null : versionIntroduce.trim();
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}