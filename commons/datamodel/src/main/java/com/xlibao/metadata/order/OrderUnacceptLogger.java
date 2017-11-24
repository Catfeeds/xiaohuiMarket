package com.xlibao.metadata.order;

import java.util.Date;

public class OrderUnacceptLogger {

    private Long id;
    private Long orderId;
    private Long targetPassportId;
    private Integer pushedCount;
    private Date pushedTime;
    private Date lastModifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTargetPassportId() {
        return targetPassportId;
    }

    public void setTargetPassportId(Long targetPassportId) {
        this.targetPassportId = targetPassportId;
    }

    public Integer getPushedCount() {
        return pushedCount;
    }

    public void setPushedCount(Integer pushedCount) {
        this.pushedCount = pushedCount;
    }

    public Date getPushedTime() {
        return pushedTime;
    }

    public void setPushedTime(Date pushedTime) {
        this.pushedTime = pushedTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}