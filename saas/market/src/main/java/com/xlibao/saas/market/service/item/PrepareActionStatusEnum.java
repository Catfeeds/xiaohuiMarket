package com.xlibao.saas.market.service.item;

/**
 * @author chinahuangxc on 2017/8/20.
 */
public enum PrepareActionStatusEnum {

    /** 0 -- 失效 */
    INVALID(0, "失效"),
    /** 1 -- 未执行 */
    UN_EXECUTOR(1, "未执行"),
    /** 2 -- 完成 */
    COMPLETE(2, "完成"),
    /** 3 -- 异常(执行一半) */
    EXCEPTION(3, "异常(执行一半)"),
    /** 4 -- 提交 */
    COMMIT(4, "提交"),
    ;

    private int key;
    private String value;

    PrepareActionStatusEnum(int key, String value) {
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