package com.xlibao.metadata.item;

import java.util.Date;

public class ItemRelationship {

    private Long id;
    private String title;
    private String sourceBarcode;
    private String targetBarcode;
    private Integer relationCoefficient;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSourceBarcode() {
        return sourceBarcode;
    }

    public void setSourceBarcode(String sourceBarcode) {
        this.sourceBarcode = sourceBarcode;
    }

    public String getTargetBarcode() {
        return targetBarcode;
    }

    public void setTargetBarcode(String targetBarcode) {
        this.targetBarcode = targetBarcode;
    }

    public Integer getRelationCoefficient() {
        return relationCoefficient;
    }

    public void setRelationCoefficient(Integer relationCoefficient) {
        this.relationCoefficient = relationCoefficient;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}