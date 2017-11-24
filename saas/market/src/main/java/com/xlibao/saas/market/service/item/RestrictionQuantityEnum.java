package com.xlibao.saas.market.service.item;

/**
 * <pre>
 *     <b>限购数量</b> -1-不限购 0-不出售 其他正数表示限购数量
 * </pre>
 * @author chinahuangxc on 2017/7/25.
 */
public enum RestrictionQuantityEnum {

    /** -1 -- 不限购 */
    UN_LIMIT(-1, "不限购"),
    /** 0 -- 正常出售 */
    UN_SELL(0, "不出售"),;

    private int key;
    private String value;

    RestrictionQuantityEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}