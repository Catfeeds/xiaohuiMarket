package com.xlibao.metadata.order;

import java.util.Date;
import java.util.List;

public class OrderEntry {

    private Long id;
    private String sequenceNumber;
    private String orderSequenceNumber;
    private String partnerId;
    private String partnerUserId;
    private Integer type;
    private Integer status;
    private Integer deliverType;
    private Integer deliverStatus;
    private Integer refundStatus;
    private String paymentType;
    private String transType;
    private Integer userSource;
    private Integer daySortNumber;
    private Integer pushType;
    private Long shippingPassportId;
    private String shippingNickName;
    private String shippingProvince;
    private String shippingCity;
    private String shippingDistrict;
    private String shippingAddress;
    private String shippingLocation;
    private String shippingPhone;
    private String receiptUserId;
    private String receiptNickName;
    private String receiptProvince;
    private String receiptCity;
    private String receiptDistrict;
    private String receiptAddress;
    private String receiptPhone;
    private String receiptLocation;
    private Long courierPassportId = 0L;
    private String courierNickName;
    private String courierPhone;
    private Long totalDistance;
    private String currentLocation;
    private Byte collectingFees;
    private String remark;
    private Long actualPrice;
    private Long totalPrice;
    private Long discountPrice;
    private Long distributionFee;
    private String priceLogger;
    private String cancelLogger;
    private Date createTime;
    private Date paymentTime = new Date(0);
    private Date confirmTime = new Date(0);
    private String body;
    private String detail;

    private List<OrderItemSnapshot> itemSnapshots;
    private List<OrderStateLogger> stateLoggers;

    /**
     * 订单ID
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 预下单序号
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber == null ? null : sequenceNumber.trim();
    }

    /**
     * 订单序列号
     */
    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    /**
     * 归属商户号ID
     */
    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * 商户方用户ID
     */
    public String getPartnerUserId() {
        return partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId;
    }

    /**
     * 订单类型
     */
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 订单状态
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(Integer deliverType) {
        this.deliverType = deliverType;
    }

    /**
     * 配送状态
     */
    public Integer getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Integer deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    /**
     * 退款状态
     */
    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * 支付类型
     */
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * 交易类型
     */
    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }

    /**
     * 用户来源
     */
    public Integer getUserSource() {
        return userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    /**
     * 当天排号
     */
    public Integer getDaySortNumber() {
        return daySortNumber;
    }

    public void setDaySortNumber(Integer daySortNumber) {
        this.daySortNumber = daySortNumber;
    }

    /**
     * 推送类型
     */
    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    /**
     * 发货人ID
     */
    public Long getShippingPassportId() {
        return shippingPassportId;
    }

    public void setShippingPassportId(Long shippingPassportId) {
        this.shippingPassportId = shippingPassportId;
    }

    /**
     * 发货人昵称
     */
    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName == null ? null : shippingNickName.trim();
    }

    /**
     * 发货省份
     */
    public String getShippingProvince() {
        return shippingProvince == null ? "" : shippingProvince.trim();
    }

    public void setShippingProvince(String shippingProvince) {
        this.shippingProvince = shippingProvince == null ? null : shippingProvince.trim();
    }

    /**
     * 发货城市
     */
    public String getShippingCity() {
        return shippingCity == null ? "" : shippingCity.trim();
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity == null ? null : shippingCity.trim();
    }

    /**
     * 发货区域
     */
    public String getShippingDistrict() {
        return shippingDistrict == null ? "" : shippingDistrict.trim();
    }

    public void setShippingDistrict(String shippingDistrict) {
        this.shippingDistrict = shippingDistrict == null ? null : shippingDistrict.trim();
    }

    /**
     * 发货地址
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress == null ? null : shippingAddress.trim();
    }

    /**
     * 发货地址经纬度
     */
    public String getShippingLocation() {
        return shippingLocation;
    }

    public void setShippingLocation(String shippingLocation) {
        this.shippingLocation = shippingLocation == null ? null : shippingLocation.trim();
    }

    /**
     * 发货人电话
     */
    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone == null ? null : shippingPhone.trim();
    }

    public String getReceiptUserId() {
        return receiptUserId;
    }

    public void setReceiptUserId(String receiptUserId) {
        this.receiptUserId = receiptUserId;
    }

    /**
     * 收货人昵称
     */
    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName == null ? null : receiptNickName.trim();
    }

    /**
     * 收货省份
     */
    public String getReceiptProvince() {
        return receiptProvince == null ? "" : receiptProvince.trim();
    }

    public void setReceiptProvince(String receiptProvince) {
        this.receiptProvince = receiptProvince == null ? null : receiptProvince.trim();
    }

    /**
     * 收货城市
     */
    public String getReceiptCity() {
        return receiptCity == null ? "" : receiptCity.trim();
    }

    public void setReceiptCity(String receiptCity) {
        this.receiptCity = receiptCity == null ? null : receiptCity.trim();
    }

    /**
     * 收货区域
     */
    public String getReceiptDistrict() {
        return receiptDistrict == null ? "" : receiptDistrict.trim();
    }

    public void setReceiptDistrict(String receiptDistrict) {
        this.receiptDistrict = receiptDistrict == null ? null : receiptDistrict.trim();
    }

    /**
     * 收货地址
     */
    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress == null ? null : receiptAddress.trim();
    }

    /**
     * 收货人联系电话
     */
    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone == null ? null : receiptPhone.trim();
    }

    /**
     * 收货地址经纬度
     */
    public String getReceiptLocation() {
        return receiptLocation;
    }

    public void setReceiptLocation(String receiptLocation) {
        this.receiptLocation = receiptLocation == null ? null : receiptLocation.trim();
    }

    /**
     * 快递员ID
     */
    public Long getCourierPassportId() {
        return courierPassportId;
    }

    public void setCourierPassportId(Long courierPassportId) {
        this.courierPassportId = courierPassportId;
    }

    /**
     * 快递员昵称
     */
    public String getCourierNickName() {
        return courierNickName;
    }

    public void setCourierNickName(String courierNickName) {
        this.courierNickName = courierNickName == null ? null : courierNickName.trim();
    }

    /**
     * 快递员联系号码
     */
    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone == null ? null : courierPhone.trim();
    }

    /**
     * 总配送距离
     */
    public Long getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Long totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * 下单时所在地址经纬度
     */
    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation == null ? null : currentLocation.trim();
    }

    /**
     * 代收费用标志
     */
    public Byte getCollectingFees() {
        return collectingFees;
    }

    public void setCollectingFees(Byte collectingFees) {
        this.collectingFees = collectingFees;
    }

    /**
     * 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 实收费用
     */
    public Long getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Long actualPrice) {
        this.actualPrice = actualPrice;
    }

    /**
     * 商品总费用
     */
    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 优惠价
     */
    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    /**
     * 配送费
     */
    public Long getDistributionFee() {
        return distributionFee;
    }

    public void setDistributionFee(Long distributionFee) {
        this.distributionFee = distributionFee;
    }

    /**
     * 价格描述
     */
    public String getPriceLogger() {
        return priceLogger;
    }

    public void setPriceLogger(String priceLogger) {
        this.priceLogger = priceLogger == null ? null : priceLogger.trim();
    }

    /**
     * 取消日志
     */
    public String getCancelLogger() {
        return cancelLogger;
    }

    public void setCancelLogger(String cancelLogger) {
        this.cancelLogger = cancelLogger == null ? null : cancelLogger.trim();
    }

    /**
     * 建立时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    /**
     * 内容
     */
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    /**
     * 商品详情(体系外)
     */
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    /**
     * 商品快照列表(体系内)
     */
    public List<OrderItemSnapshot> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<OrderItemSnapshot> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }

    public List<OrderStateLogger> getStateLoggers() {
        return stateLoggers;
    }

    public void setStateLoggers(List<OrderStateLogger> stateLoggers) {
        this.stateLoggers = stateLoggers;
    }

    public boolean isPriceMatch(long actualPrice, long totalPrice, long discountPrice, long distributionFee, int deliverType) {
        return actualPrice == getActualPrice() && totalPrice == getTotalPrice() && discountPrice == getDiscountPrice() && distributionFee == getDistributionFee() && deliverType == getDeliverType();
    }

    /**
     * 格式化发货地址
     */
    public String formatShippingAddress() {
        return getShippingProvince() + getShippingCity() + getShippingDistrict() + getShippingAddress();
    }

    /**
     * 格式化收货地址
     */
    public String formatReceiptAddress() {
        return getReceiptProvince() + getReceiptCity() + getReceiptDistrict() + getReceiptAddress();
    }
}