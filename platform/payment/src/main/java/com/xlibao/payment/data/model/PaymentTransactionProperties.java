package com.xlibao.payment.data.model;

public class PaymentTransactionProperties {

    private Long id;
    private String transSequenceNumber;
    private Integer type;
    private String k;
    private String v;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransSequenceNumber() {
        return transSequenceNumber;
    }

    public void setTransSequenceNumber(String transSequenceNumber) {
        this.transSequenceNumber = transSequenceNumber == null ? null : transSequenceNumber.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k == null ? null : k.trim();
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v == null ? null : v.trim();
    }
}