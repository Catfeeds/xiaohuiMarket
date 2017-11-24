package com.xlibao.metadata.item;

import java.util.List;

public class ItemType {

    private Long id;
    private String title;
    private Long parentId;
    private Byte status;
    private Integer sort;
    private Byte top;
    private String icon;
    private String image;
    private Integer requestSource;

    private List<ItemType> subTypes;

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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Byte getTop() {
        return top;
    }

    public void setTop(Byte top) {
        this.top = top;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(Integer requestSource) {
        this.requestSource = requestSource;
    }

    public void putSubType(ItemType itemType) {
        subTypes.add(itemType);
    }

    public void setSubTypes(List<ItemType> subTypes) {
        this.subTypes = subTypes;
    }

    public List<ItemType> getSubTypes() {
        return subTypes;
    }
}