package com.xiaohui.courier.response;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class OrderData {

    private String orderId;
    private String time;
    private String address;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
