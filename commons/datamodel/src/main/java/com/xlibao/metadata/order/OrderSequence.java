package com.xlibao.metadata.order;

import java.util.Date;

public class OrderSequence {

    private Long id;
    private String partnerId;
    private String partnerUserId;
    private String sequenceNumber;
    private Integer type;
    private Byte status;
    private Date createTime;
    private Integer validityTermSecond;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber == null ? null : sequenceNumber.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getValidityTermSecond() {
        return validityTermSecond;
    }

    public void setValidityTermSecond(Integer validityTermSecond) {
        this.validityTermSecond = validityTermSecond;
    }
}