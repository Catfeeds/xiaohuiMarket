package com.xlibao.saas.market.service.order.properties;

/**
 * @author chinahuangxc on 2017/9/3.
 */
public enum PropertiesKeyEnum {

    /** 订单扩展属性 -- 商品存放的货柜信息 */
    CONTAINER_SET(PropertiesTypeEnum.CONTAINER, "containerSet", "订单商品存放的货柜信息"),
    /** 订单扩展属性 -- 取货的货柜信息 */
    PICK_UP_CONTAINER_SET(PropertiesTypeEnum.CONTAINER, "pickUpContainerSet", "取货的货柜信息"),
    /** 订单扩展属性 -- 货架数据 */
    CONTAINER_DATA(PropertiesTypeEnum.CONTAINER, "containerData", "货架数据"),
    /** 订单扩展属性 -- 询问货架信息 */
    ASK_CONTAINER_SET(PropertiesTypeEnum.ASK, "askContainerSet", "询问货架信息"),
    ;

    private PropertiesTypeEnum typeEnum;
    private String key;
    private String value;

    PropertiesKeyEnum(PropertiesTypeEnum typeEnum, String key, String value) {
        this.typeEnum = typeEnum;
        this.key = key;
        this.value = value;
    }

    public PropertiesTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
