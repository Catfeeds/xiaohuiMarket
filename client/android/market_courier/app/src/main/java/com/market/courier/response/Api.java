package com.market.courier.response;

/**
 * Created by zhumg on 8/29.
 */
public class Api {

    public static Passport passport;

    public static final String PASSPORT_URL = "https://market.xiaohuistore.com/";
    public static final String MARKET_URL = "https://market.xiaohuistore.com/";
//    public static final String PASSPORT_URL = "http://192.168.1.102:8080/";
//    public static final String MARKET_URL = "http://192.168.1.102:8080/";
//    public static final String PASSPORT_URL = "http://10.0.2.2:8080/";
//    public static final String MARKET_URL = "http://10.0.2.2:8080/";

    //0未完成
//    http://localhost:8080/market/market/openapi/showShelvesTask.do?marketId=100000&passportId=100001&status=0&pageIndex=1&pageSize=10

    //1已完成
//    http://localhost:8080/market/market/openapi/showShelvesTask.do?marketId=100000&passportId=100001&status=1&pageIndex=1&pageSize=10

    //商店列表
//    http://localhost:8080/market/market/openapi/myFocusMarkets.do?passportId=100001


//    public static final String OPEN_API = "openapi/";
    public static final String OPEN_API = "";

    //登陆
    public static final String LOGIN = PASSPORT_URL + "passport/loginForVerificationCode";//登陆

    //版本检测更新
    public static final String VERSION_UPGRADE = PASSPORT_URL + "passport/versionUpgrade";

    //商店列表
    public static final String MARKET_LIST = MARKET_URL + "market/" + OPEN_API + "myFocusMarkets.do";

    //订单列表
    public static final String SHOW_ORDERS = MARKET_URL + "market/order/" + OPEN_API + "showOrders.do";

    //接单
    public static final String ACCEPT_ORDER = MARKET_URL + "market/order/" + OPEN_API + "acceptOrder.do";

    //开始配送
    public static final String DELIVER_ORDER = MARKET_URL + "market/order/" + OPEN_API + "deliverOrder.do";

    //确认送达
    public static final String ARRIVE_ORDER = MARKET_URL + "market/order/" + OPEN_API + "arriveOrder.do";

    //测试，扫码配送
    public static final String FIND_CONTAINER_DATA = MARKET_URL + "market/order/" + OPEN_API + "findContainerData.do";

    //获取验证码
    public static final String GET_SMS = PASSPORT_URL + "passport/sms/requestVerificationCode";//SMS

    //客服电话
    public static final String KF_PHONE = "4004004000";
    public static final String KF_PHONE_TXT = "400-400-4000";

    public static final int PAGE_SIZE = 10;
}
