package com.market.courier.response;

import java.util.List;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class OrderData implements java.io.Serializable {

    private String sequenceNumber;
    private long orderId;
    private String orderSequenceNumber;

    private String formatReceiptAddress;
    private String deliverDistance;

    private int orderStatus;
    private String orderStatusTitle;

    private String paymentTime;

    private String receiptPhone;

    private List<OrderItemData> items;

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getFormatReceiptAddress() {
        return formatReceiptAddress;
    }

    public void setFormatReceiptAddress(String formatReceiptAddress) {
        this.formatReceiptAddress = formatReceiptAddress;
    }

    public String getDeliverDistance() {
        return deliverDistance;
    }

    public void setDeliverDistance(String deliverDistance) {
        this.deliverDistance = deliverDistance;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusTitle() {
        return orderStatusTitle;
    }

    public void setOrderStatusTitle(String orderStatusTitle) {
        this.orderStatusTitle = orderStatusTitle;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public List<OrderItemData> getItems() {
        return items;
    }

    public void setItems(List<OrderItemData> items) {
        this.items = items;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }
}
