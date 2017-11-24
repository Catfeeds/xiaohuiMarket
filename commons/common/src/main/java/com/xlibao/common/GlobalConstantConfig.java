package com.xlibao.common;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author chinahuangxc on 2016/11/9.
 */
public class GlobalConstantConfig {

    /** 项目名称 */
    public static final String PROJECT_NAME = "xmarket";

    /** 序列号前缀 */
    public static final String UNIQUE_PRIMARY_KEY_PREFIX = "908";

    /** 序列号有效时长 */
    public static final long SEQUENCE_NUMBER_VALIDITY_TERM_SECOND = TimeUnit.MINUTES.toSeconds(30);

    /** 合作商户号前缀 */
    public static final String PARTNER_ID_PREFIX = "xlb908";

    /** 密码前缀 */
    public static final String PASSWORD_PREFIX = "xmarket";
    /** 密码后缀 */
    public static final String PASSWORD_SUFFIX = "@8#=+&X$%@";

    /** 1 - 默认页码 */
    public static final int DEFAULT_PAGE_INDEX = 1;
    /** 10 - 默认每页内容数量 */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /** 短信验证码有效期 */
    public static final long SMS_CODE_TERM_OF_VALIDITY = TimeUnit.MINUTES.toMillis(5);
    /** 两次获取验证码的间隔时间 */
    public static final long SMS_CODE_DUPLICATE_PERIOD = TimeUnit.MINUTES.toMillis(1);

    /** 无效经纬度地址 */
    public static final String INVALID_LOCATION = "0.0,0.0";

    /** 系统健康监控周期 */
    public static final long HEALTHY_MONITOR_DELAY = 10;
    /** 系统健康监控时间单位 */
    public static final TimeUnit HEALTHY_MONITOR_TIME_UNIT = TimeUnit.MINUTES;

    /** 访问令牌有效时长(可续约)，单位：分钟 */
    public static final long ACCESS_TOKEN_TIME_OUT = TimeUnit.MINUTES.toMillis(60);

    /** 本地IP地址 */
    public static String LOCAL_IP_ADDRESS = "";

    /**
     * <pre>
     *     <b>该参数仅用于描述加密算法的规则，并不参与具体的业务逻辑</b>
     *
     *     <b>签名算法：</b>签名生成的通用步骤如下：
     *          "第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。"
     *              特别注意以下重要规则：
     *                  ◆ 生成一个 32 位的随机字符串randomParameter
     *                  ◆ 参数名ASCII码从小到大排序（字典序），其中包括固定参数partnerId、appId
     *                  ◆ 如果参数的值为空不参与签名；
     *                  ◆ 参数名区分大小写；
     *                  ◆ 验证调用返回或主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
     *                  ◆ 接口可能增加字段，验证签名时必须支持增加的扩展字段
     *          第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。
     *
     *     <b>2、签名算法过程示例</b>
     *          请求接口参数(所有参数为字符串)：
     *              partnerId = xlb908100000
     *              appId = 908100000
     *              randomParameter = RUWbgF0Xs78G2nXXTmsEYBfVLU9DWLp6 (32位)
     *              orderStatusSet = 8
     *              pageIndex = 1
     *              pageSize = 10
     *              roleType = 2
     *              targetUserId = 100000
     *              type = -1
     *          字典排序后的参数为："appId=908100001&orderStatusSet=8&pageIndex=1&pageSize=10&partnerId=xlb908100000&roleType=2&targetUserId=100000&type=-1"
     *          拼接key后字符串为："appId=908100001&orderStatusSet=8&pageIndex=1&pageSize=10&partnerId=xlb908100000&roleType=2&targetUserId=100000&type=-1&key=JGW4bPKIIF7tf0tk"
     *          计算MD5得到（大写）：5972321665ECA455973894E756C12E1A
     *          该字串就是最终生成的签名（必须为大写）。
     * </pre>
     */
    public static final Object signatureRule = CommonUtils.STATIC_FINAl;

    static {
        try {
            LOCAL_IP_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}