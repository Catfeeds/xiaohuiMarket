package com.xlibao.metadata.passport;

import java.util.Date;

public class PassportPartnerApplicationProperties {

    private Long id;
    private Long appId;
    private Integer usePlatform;
    private String packages;
    private Byte scene;
    private Integer status;
    private String devCertificate;
    private String prodCertificate;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getUsePlatform() {
        return usePlatform;
    }

    public void setUsePlatform(Integer usePlatform) {
        this.usePlatform = usePlatform;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages == null ? null : packages.trim();
    }

    public Byte getScene() {
        return scene;
    }

    public void setScene(Byte scene) {
        this.scene = scene;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDevCertificate() {
        return devCertificate;
    }

    public void setDevCertificate(String devCertificate) {
        this.devCertificate = devCertificate == null ? null : devCertificate.trim();
    }

    public String getProdCertificate() {
        return prodCertificate;
    }

    public void setProdCertificate(String prodCertificate) {
        this.prodCertificate = prodCertificate == null ? null : prodCertificate.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}