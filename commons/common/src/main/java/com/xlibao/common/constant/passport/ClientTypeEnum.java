package com.xlibao.common.constant.passport;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     <b>结构：</b>key,clientVersionType(版本控制表中对应client_type),code,name
 * </pre>
 * @author chinahuangxc on 2017/4/7.
 */
public enum ClientTypeEnum {

    /** 1、快递员 */
    COURIER(1, 1, "courier", "快递员"),
    /** 2、POS */
    POS(2, 2, "pos", "POS"),
    /** 3、消费者(原来的商家) */
    CONSUMER(3, 3, "consumer", "消费者"),
    /** 4、商家(暂时不使用) */
    MERCHANT(4, 4, "merchant", "商家"),
    /** 5、仓管 */
    WAREHOUSE(5, 5, "warehouse", "仓管"),
    /** 6、采购 */
    PURCHASE(6, 5, "purchase", "采购"),
    /** 7、供应商 */
    SUPPLIER(7, 3, "supplier", "供应商"),
    /** 8、后台 */
    BACKSTAGE(8, 8, "backstage", "后台"),
    ;

    private int key;
    private int clientVersionType;
    private String code;
    private String name;

    ClientTypeEnum(int key, int clientVersionType, String code, String name) {
        this.key = key;
        this.clientVersionType = clientVersionType;
        this.code = code;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public int getClientVersionType() {
        return clientVersionType;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, ClientTypeEnum> CLIENT_TYPE_ENUM_MAP = new HashMap<>();

    static {
        ClientTypeEnum[] clientTypeEnums = ClientTypeEnum.values();
        for (ClientTypeEnum clientTypeEnum : clientTypeEnums) {
            CLIENT_TYPE_ENUM_MAP.put(clientTypeEnum.getKey(), clientTypeEnum);
        }
    }

    public static ClientTypeEnum getClientTypeEnum(int key) {
        return CLIENT_TYPE_ENUM_MAP.get(key);
    }
}