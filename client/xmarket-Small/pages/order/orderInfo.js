const config = require('../../config')

var app = getApp();
var _this = null;

var winAjax = {
  orderDetail : function(orderId){

      var option = config.orderDetail;
      option.data.orderId = orderId;

      option.success = function(data){
          // data = {"data":{"msg":"请求成功","code":0,"response":{"sequenceNumber":"90820170815173208513526183","targetName":"小惠便利店恒大山水城店","address":"广东省广州市增城市御峰四街3号","addressTitle":"取货地址：","orderId":100018,"actualPrice":71,"statusValue":"待支付","targetTitle":"取货点：","distributionFee":0,"discountPrice":0,"orderStatus":1,"deliverType":1,"deliverValue":"到店自提","createTime":"2017-08-15 17:32:09","deliverTitle":"配送进度","deliverResult":"已取货","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},"accessToken":""},"header":{"Server":"Apache-Coyote/1.1","Set-Cookie":"JSESSIONID=127D18C5A1AF1717E73E1AE510469EA9; Path=/market; HttpOnly","Content-Type":"application/json;charset=UTF-8","Transfer-Encoding":"chunked","Date":"Tue, 15 Aug 2017 10:43:01 GMT"},"statusCode":200,"errMsg":"request:ok"};
          data = data.data ? data.data.response : {};
          _this.setData({
              order: data || {}
          });
      };
      app.ajax(_this,option);
  }
}


Page({
  data: {
    order : {},
    status : {tabActive : 0}
  },
  onLoad: function (option){

    // 初始化信息
    _this = this

    
    this.ajax.orderDetail(option.orderId);

  },
  ajax : winAjax
})