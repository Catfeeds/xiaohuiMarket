package com.market.courier.response;

/**
 * Created by zhumg on 9/26.
 */
public class OrderItemData implements java.io.Serializable {

    private long itemSnapshotId;//</b> - long 商品快照ID
    private long itemId;//</b> - long 商品ID
    private long itemTemplateId;//</b> - long 商品模版ID
    private String name;//</b> - String 商品名
    private String image;//</b> - String 商品图片
    private long price;//</b> - long 商品单价(原价)，单位：分
    private int quantity;//</b> - int 商品数量
    private long totalPrice;//</b> - long 商品总价(已计算了优惠)

    public long getItemSnapshotId() {
        return itemSnapshotId;
    }

    public void setItemSnapshotId(long itemSnapshotId) {
        this.itemSnapshotId = itemSnapshotId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
