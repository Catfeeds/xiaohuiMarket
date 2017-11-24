package com.xlibao.common.constant.passport;

/**
 * <pre>
 *     <b>对应通行证的角色类型</b>
 *     <b>注意：</b>
 *     1、一个通行证可能存在多个角色，但对同一体系下的，只能存在一种角色；如：普通商家、供应商只能存在一个
 *     2、类型值1--10 为保留值，请不要使用
 * </pre>
 *
 * @author chinahuangxc on 2017/4/24.
 */
public enum PassportRoleTypeEnum {

    /** 11 -- 普通用户 */
    DEFAULT(11, ClientTypeEnum.CONSUMER, "普通用户", true),
    /** 12 -- 平台供应商 */
    PLATFORM_SUPPLIER(12, ClientTypeEnum.CONSUMER, "平台供应商", false),
    /** 13 -- 门店供应商 */
    SUPPLIER(13, ClientTypeEnum.CONSUMER, "门店供应商", false),

    /** 21 -- 仓库 */
    WAREHOUSE(21, ClientTypeEnum.WAREHOUSE, "仓库", false),
    /** 22 -- 主仓 */
    MAIN_WAREHOUSE(22, ClientTypeEnum.WAREHOUSE, "主仓", false),
    ;

    private int key;
    private ClientTypeEnum clientTypeEnum;
    private String value;
    private boolean isDefault;

    PassportRoleTypeEnum(int key, ClientTypeEnum clientTypeEnum, String value, boolean isDefault) {
        this.key = key;
        this.clientTypeEnum = clientTypeEnum;
        this.value = value;
        this.isDefault = isDefault;
    }

    public int getKey() {
        return key;
    }

    public ClientTypeEnum getClientTypeEnum() {
        return clientTypeEnum;
    }

    public String getValue() {
        return value;
    }

    public boolean isDefault() {
        return isDefault;
    }
}