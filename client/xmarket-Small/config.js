/**
 * 小程序配置文件
 */


// itemTypes: {
//     host : "marketItem",     域名
//     islogin : true,          是否存在 开放接口
//     url : "/itemTypes.do",   
//     data : { marketId : 100000 }
// },

// huangxc 
// #### 通行证中心域名地址 ####
// passportRemoteURL=http://huangxc:8080/passport/
// #### 支付中心域名地址 ####
// paymentRemoteURL=http://huangxc:8080/payment/
// #### 订单中心域名地址 ####
// orderRemoteURL=http://huangxc:8080/order/
// #### 商品中心域名地址 ####
// itemRemoteURL=http://huangxc:8080/item/
// #### 物流域名地址 ####
// logisticsRemoteURL=http://huangxc:8080/logistics/
// #### 无人超市域名地址 ####
// marketRemoteURL=http://huangxc:8080/market/
// #### 商店服务端 ####
// marketShopRemoteURL=http://huangxc:8080/marketshop/

// 服务器
// #### 通行证中心域名地址 ####
// passportRemoteURL=http://112.74.185.56:8080/
// #### 支付中心域名地址 ####
// paymentRemoteURL=http://119.23.104.118:8082/
// #### 无人超市域名地址 ####
// marketRemoteURL=http://119.23.104.118:8083/


var host = {
        // market      : "http://huangxc:8080/market",
        // passport    : "http://huangxc:8080/passport", 
        // payment    : "http://huangxc:8080/payment",

        payment     : "http://119.23.104.118:8082",
        market      : "http://119.23.104.118:8083",
        passport    : "http://112.74.185.56:8080",
    }

var ServerHost = {
        market      : host.market + "/market",
        marketItem  : host.market + "/market/item",
        marketOrder : host.market + "/market/order",
        passport    : host.passport + "",
        payment     : host.payment + ""
    }

var ServerHostOpenApi = {  
        market      : ServerHost.market + '/openapi',
        marketItem  : ServerHost.marketItem + '/openapi',
        marketOrder : ServerHost.marketOrder + "/openapi"
    }

var config = {
    //商品类型接口
    itemTypes: {
        host : "marketItem",
        islogin : true,
        url : "/itemTypes.do",
        data : { marketId : 100000 }
    },
    // 找到商店
    findMarket: {
        host : "market",
        server : "",
        islogin : true,
        url : "/findMarket.do",
        data : { passportId : 100000, marketId : 0 }
    },
    // 找到可用的商店
    showMarkets: {
        host : "market",
        islogin : true,
        url : "/showMarkets.do",
        data : { passportId : 100000 , longitude : 0, latitude : 0}
    },
    // 找商品
    pageItems: {
        host : "marketItem",
        islogin : true,
        url : "/pageItems.do",
        data : { passportId : 100000 , marketId : 0 , itemTypeId : 0 , sortType : 0 , sortValue : 0 , searchKeyValue : '' , pageSize : 10 , pageIndex : 1}
    },
    // 订单数据
    showOrders: {
        host : "marketOrder",
        islogin : true, // false
        url : "/showOrders.do",
        // passportId  - long 通行证ID，必填参数。
        // roleType    - int 角色类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderRoleTypeEnum}。
        // orderType   - int 订单类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderTypeEnum}。
        // statusEnter - int 状态入口，必填参数；具体参考：{@linkplain com.xlibao.saas.market.service.order.StatusEnterEnum}。 11:全部  12:待付款  13:待收货 14:退款
        // ageIndex    - int 页码，非必填参数；默认为：{@linkplain com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_INDEX}。
        // ageSize     - int 显示数量，非必填参数；默认为：{@linkplain com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_SIZE}。
        data : { passportId : 100000 ,roleType : 1,orderType : 1,statusEnter : 11,ageIndex : 1,ageSize : 10}
    },
    orderDetail: {
        host : "marketOrder",
        islogin : true, // false
        url : "/orderDetail.do",
        // passportId  - long 通行证ID，必填参数。
        // roleType    - int 角色类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderRoleTypeEnum}。
        // orderType   - int 订单类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderTypeEnum}。
        // orderId     - long 订单ID，必填参数。
        data : { passportId : 100000 ,roleType : 1,orderId : 0}
    },


    // 预下单
    prepareCreateOrder : {
        host : "marketOrder",
        islogin : true, // false
        url : "/prepareCreateOrder.do",
        // passportId - long 下单通行证ID，必填参数；未登录时，提示用户登录。
        // orderType - int 订单类型，必填参数；具体参考：{@linkplain com.xlibao.common.constant.order.OrderTypeEnum}。 销售订单：1
        data : { passportId : 100000 , orderType : 1}
    },
    // 生成订单
    generateOrder : {
        host : "marketOrder",
        islogin : true, // false
        url : "/generateOrder.do",
        //  passportId - long 下单通行证ID，必填参数。
        //  marketId - long 商店ID，必填参数。
        //  deviceType - int 设备类型，非必填参数；默认为{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum#DEVICE_TYPE_ANDROID}， 具体参考：{@linkplain com.xlibao.common.constant.device.DeviceTypeEnum}。
        //  deliverType - int 配送类型，非必填参数；参看：{@link com.xlibao.saas.market.service.order.DeliverTypeEnum} 1: 自提 2:配送
        //  sequenceNumber - String 预下单序号，必填参数。
        //  currentLocation - String 当前位置信息，非必填参数；尽量提供，方便后期跟踪；格式为：latitude,longitude。
        //  collectingFees - byte 代收费用，非必填参数；默认为{@linkplain com.xlibao.common.constant.order.CollectingFeesEnum#UN_COLLECTION} 不代收。
        //  receiptProvince - String 收货省份，必填参数。
        //  receiptCity - String 收货城市，必填参数。
        //  receiptDistrict - String 收货区域，必填参数
        //  receiptAddress - String 具体收货地址，必填参数。
        //  receiptNickName - String 收货人昵称，必填参数。
        //  receiptPhone - String 收货人联系号码，必填参数。
        //  receiptLocation - String 收货地址经纬度，非必填参数；请尽量提供，方便距离跟踪；格式为：latitude,longitude。
        //  remark - String 描述内容(备注)。
        //  itemTemplateSet - String 商品集合，格式为：JSONObject -- {"10000":"2"} ID:数量。
        data : { passportId : 100000 , marketId : 0 , deviceType : 5 , deliverType: 1, sequenceNumber : 0 , currentLocation : "0,0" , collectingFees : 0 , receiptProvince : "" , receiptCity : "" , receiptDistrict : "" , receiptAddress : "" , receiptNickName : "" , receiptPhone : "" , receiptLocation : "" , remark : "" , itemTemplateSet : ""}
    },
    // 创建支付订单
    paymentOrder: {
        host : "marketOrder",
        islogin : true, // false
        url : "/paymentOrder.do",
        // passportId - long 通行证ID，必填参数。
        // orderSequenceNumber - String 订单序列号, 创建订单返回。
        // deliverType - int 配送类型，非必填参数；参看：{@link com.xlibao.saas.market.service.order.DeliverTypeEnum} 1: 自提 2:配送
        // paymentType - String 支付类型，必填参数；参考：{@linkplain com.xlibao.common.constant.payment.PaymentTypeEnum} 31000:WEIXIN_JS/ ,  30000:WEIXIN_NATIVE/微信 ,  10000:BALANCE/余额支付
        data : { passportId : 100000 , orderSequenceNumber : 0 , deliverType : 1 , paymentType : 'WEIXIN_JS' }
    },
    // 支付
    pay : {
        BALANCE : {
            host : "payment",
            islogin : false,
            url : "/payment/balancePayment",
            data : { passportId : 100001 , paymentPassword : '123456'}
        },
        WEIXIN_JS : {

        }
    }
};


console.log("config....................");
module.exports = function(){
    config.host = ServerHost;
    config.hostOpenApi = ServerHostOpenApi;
    return config;
}();

