package com.xiaohui.replenishment.response;

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


    public static final String OPEN_API = "openapi/";
//    public static final String OPEN_API = "";

    //登陆
    public static final String LOGIN = PASSPORT_URL + "passport/loginPassport";//登陆

    //版本检测更新
    public static final String VERSION_UPGRADE = PASSPORT_URL + "passport/versionUpgrade";

    //商店列表
    public static final String MARKET_LIST = MARKET_URL + "market/" + OPEN_API + "myFocusMarkets.do";

    //任务列表
    public static final String TASK_LIST = MARKET_URL + "market/" + OPEN_API + "showShelvesTask.do";

    //扫商品条码，检测任务
    public static final String SCAN_TASK = MARKET_URL + "market/" + OPEN_API + "scanShelvesTask.do";

    //扫货架，提交任务,上架
    public static final String SCAN_OK_UP = MARKET_URL + "market/item/" + OPEN_API + "onShelves.do";

    //扫货架，提交任务,下架
    public static final String SCAN_OK_DOWN = MARKET_URL + "market/item/" + OPEN_API + "offShelves.do";

    //检测店铺今日任务完成情况
    public static final String FIND_VALID_TASKS = MARKET_URL + "market/" + OPEN_API + "findValidTasks.do";

    //提交店铺今日所有任务
    public static final String FINISH_DAY_TASK = MARKET_URL + "market/" + OPEN_API + "finishTodayPrepareActions.do";

    //修改店铺状态
    public static final String UPDATE_MARKET_STATUS = MARKET_URL + "market/manager/marketUpdateStatus.do";

}
