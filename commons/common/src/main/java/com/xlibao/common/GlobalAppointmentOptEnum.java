package com.xlibao.common;

/**
 * <pre>
 *     部分全局通用的操作枚举
 * </pre>
 *
 * @author chinahuangxc on 2016/11/11.
 */
public enum GlobalAppointmentOptEnum {

    /** 0 逻辑假 */
    LOGIC_FALSE(0, "逻辑假"),
    /** 1 逻辑真 */
    LOGIC_TRUE(1, "逻辑真"),

    /** 0 结果升序 */
    RESULT_ASCENDING(0, "结果升序"),
    /** 1 结果降序 */
    RESULT_DESCENDING(1, "结果降序"),

    /** 0 性别 -- 女性 */
    FEMALE(0, "女性"),
    /** 1 性别 -- 男性 */
    MALE(1, "男性"),
    ;

    private byte key;
    private String value;

    GlobalAppointmentOptEnum(int key, String value) {
        this.key = (byte) key;
        this.value = value;
    }

    public byte getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
