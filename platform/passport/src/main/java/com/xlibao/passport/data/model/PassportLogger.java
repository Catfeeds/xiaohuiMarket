package com.xlibao.passport.data.model;

import java.util.Date;

public class PassportLogger {

    private Long id;
    private Long passportId;
    private Date createTime;
    private Integer type;
    private String content;
    private String mark;
    private Long optPassportId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

    public Long getOptPassportId() {
        return optPassportId;
    }

    public void setOptPassportId(Long optPassportId) {
        this.optPassportId = optPassportId;
    }
}