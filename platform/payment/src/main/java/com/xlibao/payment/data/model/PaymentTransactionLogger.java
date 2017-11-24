package com.xlibao.payment.data.model;

import java.util.Date;

public class PaymentTransactionLogger {

    private Long id;
    private Long passportId;
    private String transSequenceNumber;
    private Integer transStatus;
    private String paymentType;
    private Integer transType;
    private String partnerId;
    private String appId;
    private String partnerUserId;
    private String partnerTradeNumber;
    private Integer channelId;
    private String channelTradeNumber;
    private String channelUserId;
    private String channelUserName;
    private String channelRemark;
    private String accountNumber;
    private String accountName;
    private Integer accountType;
    private String currencyType;
    private String bankName;
    private String bankBranchCode;
    private String bankSimpleName;
    private Long transUnitAmount;
    private Integer transNumber;
    private Long transTotalAmount;
    private String transTitle;
    private String remark;
    private Date createTime;
    private Date transCreateTime;
    private Date paymentTime;
    private Byte useCoupon;
    private Long discountAmount;
    private Byte refundStatus;
    private Date refundTime;
    private String channelRefundParameters = "";
    private String notifyFrontUrl;
    private String notifyUrl;
    private String extendParameter;

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

    public String getTransSequenceNumber() {
        return transSequenceNumber;
    }

    public void setTransSequenceNumber(String transSequenceNumber) {
        this.transSequenceNumber = transSequenceNumber == null ? null : transSequenceNumber.trim();
    }

    public Integer getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(Integer transStatus) {
        this.transStatus = transStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId == null ? null : partnerId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId == null ? null : partnerUserId.trim();
    }

    public String getPartnerTradeNumber() {
        return partnerTradeNumber;
    }

    public void setPartnerTradeNumber(String partnerTradeNumber) {
        this.partnerTradeNumber = partnerTradeNumber;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelTradeNumber() {
        return channelTradeNumber;
    }

    public void setChannelTradeNumber(String channelTradeNumber) {
        this.channelTradeNumber = channelTradeNumber == null ? null : channelTradeNumber.trim();
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId == null ? null : channelUserId.trim();
    }

    public String getChannelUserName() {
        return channelUserName;
    }

    public void setChannelUserName(String channelUserName) {
        this.channelUserName = channelUserName == null ? null : channelUserName.trim();
    }

    public String getChannelRemark() {
        return channelRemark;
    }

    public void setChannelRemark(String channelRemark) {
        this.channelRemark = channelRemark == null ? null : channelRemark.trim();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? null : accountNumber.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode == null ? null : bankBranchCode.trim();
    }

    public String getBankSimpleName() {
        return bankSimpleName;
    }

    public void setBankSimpleName(String bankSimpleName) {
        this.bankSimpleName = bankSimpleName == null ? null : bankSimpleName.trim();
    }

    public Long getTransUnitAmount() {
        return transUnitAmount;
    }

    public void setTransUnitAmount(Long transUnitAmount) {
        this.transUnitAmount = transUnitAmount;
    }

    public Integer getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(Integer transNumber) {
        this.transNumber = transNumber;
    }

    public Long getTransTotalAmount() {
        return transTotalAmount;
    }

    public void setTransTotalAmount(Long transTotalAmount) {
        this.transTotalAmount = transTotalAmount;
    }

    public String getTransTitle() {
        return transTitle;
    }

    public void setTransTitle(String transTitle) {
        this.transTitle = transTitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTransCreateTime() {
        return transCreateTime;
    }

    public void setTransCreateTime(Date transCreateTime) {
        this.transCreateTime = transCreateTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Byte getUseCoupon() {
        return useCoupon;
    }

    public void setUseCoupon(Byte useCoupon) {
        this.useCoupon = useCoupon;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Byte getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Byte refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getChannelRefundParameters() {
        return channelRefundParameters;
    }

    public void setChannelRefundParameters(String channelRefundParameters) {
        this.channelRefundParameters = channelRefundParameters;
    }

    public String getNotifyFrontUrl() {
        return notifyFrontUrl;
    }

    public void setNotifyFrontUrl(String notifyFrontUrl) {
        this.notifyFrontUrl = notifyFrontUrl == null ? null : notifyFrontUrl.trim();
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
    }

    public String getExtendParameter() {
        return extendParameter;
    }

    public void setExtendParameter(String extendParameter) {
        this.extendParameter = extendParameter == null ? null : extendParameter.trim();
    }
}