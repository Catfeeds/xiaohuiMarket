package com.xlibao.common.constant.passport;

/**
 * <pre>
 *      <b>通行证别名类型枚举</b>
 *      可通过不同通行证名登录同一个账户
 * </pre>
 *
 * @author chinahuangxc on 2017/2/12.
 */
public enum PassportAliasTypeEnum {

    /** 1 -- 官网注册帐号 */
    DEFAULT(1, "官网注册帐号"),
    /** 2 -- 手机号 */
    PHONE(2, "手机号"),
    /** 3 -- 微信帐号(unionId) */
    WEIXIN(3, "微信帐号(unionId)"),
    /** 4 -- QQ帐号 */
    QQ(4, "QQ帐号"),
    /** 5 -- 邮箱帐号 */
    EMAIL(5, "邮箱帐号"),
    /** 6 -- 支付宝帐号 */
    ALIBABA(6, "支付宝帐号"),
    /** 7 -- 淘宝帐号 */
    TAOBAO(7, "淘宝帐号"),
    /** 8 -- 微博帐号 */
    WEIBO(8, "微博帐号"),
    ;

    private int key;
    private String value;

    PassportAliasTypeEnum(int key, String value){
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
