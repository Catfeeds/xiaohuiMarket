package com.xlibao.metadata.order;

import java.util.Date;
import java.util.Objects;

public class OrderItemSnapshot {

    private Long id;
    private Long orderId;
    private String userMark;
    private Long itemId;
    private Long itemTemplateId;
    private String itemName;
    private Long itemTypeId;
    private String itemTypeName;
    private Long itemUnitId;
    private String itemUnitName;
    private String itemBarcode;
    private String itemCode;
    private Integer itemBatches;
    private String introductionPage;
    private Date createTime;
    private Integer normalQuantity = 0;
    private Integer discountQuantity = 0;
    private Integer shipmentQuantity = 0;
    private Integer distributionQuantity = 0;
    private Integer receiptQuantity = 0;
    private Integer returnQuantity = 0;
    private Long normalPrice = 0L;
    private Long discountPrice = 0L;
    private Long costPrice = 0L;
    private Long marketPrice = 0L;
    private Long totalPrice = 0L;
    private Long returnPrice = 0L;

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

    public String getUserMark() {
        return userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark == null ? null : userMark.trim();
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(Long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName == null ? null : itemTypeName.trim();
    }

    public Long getItemUnitId() {
        return itemUnitId;
    }

    public void setItemUnitId(Long itemUnitId) {
        this.itemUnitId = itemUnitId;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName == null ? null : itemUnitName.trim();
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode == null ? null : itemBarcode.trim();
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }

    public Integer getItemBatches() {
        return itemBatches;
    }

    public void setItemBatches(Integer itemBatches) {
        this.itemBatches = itemBatches;
    }

    public String getIntroductionPage() {
        return introductionPage;
    }

    public void setIntroductionPage(String introductionPage) {
        this.introductionPage = introductionPage == null ? null : introductionPage.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNormalQuantity() {
        return normalQuantity;
    }

    public void setNormalQuantity(Integer normalQuantity) {
        this.normalQuantity = normalQuantity;
    }

    public Integer getDiscountQuantity() {
        return discountQuantity;
    }

    public void setDiscountQuantity(Integer discountQuantity) {
        this.discountQuantity = discountQuantity;
    }

    public Integer getShipmentQuantity() {
        return shipmentQuantity;
    }

    public void setShipmentQuantity(Integer shipmentQuantity) {
        this.shipmentQuantity = shipmentQuantity;
    }

    public Integer getDistributionQuantity() {
        return distributionQuantity;
    }

    public void setDistributionQuantity(Integer distributionQuantity) {
        this.distributionQuantity = distributionQuantity;
    }

    public Integer getReceiptQuantity() {
        return receiptQuantity;
    }

    public void setReceiptQuantity(Integer receiptQuantity) {
        this.receiptQuantity = receiptQuantity;
    }

    public Integer getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(Integer returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public Long getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(Long normalPrice) {
        this.normalPrice = normalPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long costPrice) {
        this.costPrice = costPrice;
    }

    public Long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(Long returnPrice) {
        this.returnPrice = returnPrice;
    }

    public int totalShippingQuantity() {
        return shipmentQuantity + distributionQuantity + receiptQuantity + returnQuantity;
    }

    public int totalQuantity() {
        return normalQuantity + discountQuantity;
    }

    public int unDeliveryQuantity() {
        return totalQuantity() - totalShippingQuantity();
    }

    public boolean isMatch(OrderItemSnapshot itemSnapshot) {
        return Objects.equals(itemSnapshot.getNormalQuantity(), getNormalQuantity()) && Objects.equals(itemSnapshot.getNormalPrice(), getNormalPrice())
                && Objects.equals(itemSnapshot.getDiscountQuantity(), getDiscountQuantity()) && Objects.equals(itemSnapshot.getDiscountPrice(), getDiscountPrice());
    }

    public int unReceiptQuantity() {
        return normalQuantity + discountQuantity - receiptQuantity;
    }
}