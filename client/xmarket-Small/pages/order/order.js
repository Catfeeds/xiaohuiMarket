const config = require('../../config')

var app = getApp()
var _this = null
var _load = false

var winAjax = {
  ordersData : function(){

      var option = config.showOrders;
      console.log(option.data.ageIndex)
      option.data.statusEnter = _this.data.status.tabActive || 11;  // 11:全部

      option.success = function(data){
          // var data = {"data":{"msg":"请求成功","code":0,"response":{"datas":[{"sequenceNumber":"90820170815173208513526183","orderId":100018,"actualPrice":71,"orderStatus":1,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待支付","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},{"sequenceNumber":"90820170815173208513526183","orderId":100018,"actualPrice":71,"orderStatus":1,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待支付","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},{"sequenceNumber":"90820170815173208513526183","orderId":100018,"actualPrice":71,"orderStatus":1,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待支付","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},{"sequenceNumber":"90820170815173208513526183","orderId":100018,"actualPrice":71,"orderStatus":1,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待支付","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},{"sequenceNumber":"90820170815173208513526183","orderId":100018,"actualPrice":71,"orderStatus":1,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待支付","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},{"sequenceNumber":"90820170815173208513526183","orderId":100018,"actualPrice":71,"orderStatus":1,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待支付","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100037},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100038},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100039}],"orderSequenceNumber":"190820170815173209893575711","totalItemQuantity":6},{"sequenceNumber":"90820170815171719832474689","orderId":100017,"actualPrice":71,"orderStatus":64,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待取货","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":3,"totalPrice":33,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100034},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100035},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100036}],"orderSequenceNumber":"190820170815171721641061349","totalItemQuantity":6},{"sequenceNumber":"90820170815171653604695029","orderId":100016,"actualPrice":38,"orderStatus":2048,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"退款","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100032},{"itemTemplateId":100003,"itemId":100003,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100058.jpg","quantity":2,"totalPrice":26,"price":13,"name":"哈尔滨（冰纯）600ml","itemSnapshotId":100033}],"orderSequenceNumber":"190820170815171655600527648","totalItemQuantity":3},{"sequenceNumber":"90820170815163500500490316","orderId":100015,"actualPrice":34,"orderStatus":8,"marketId":100000,"deliverType":1,"marketName":"恒大山水城店","deliverTitle":"到店自提","orderStatusTitle":"待出货","marketFormatAddress":"广东省广州市增城市中新镇团结村","items":[{"itemTemplateId":100001,"itemId":100001,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100056.jpg","quantity":2,"totalPrice":22,"price":11,"name":"哈尔滨（冰纯）330ml","itemSnapshotId":100030},{"itemTemplateId":100002,"itemId":100002,"image":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100057.jpg","quantity":1,"totalPrice":12,"price":12,"name":"哈尔滨（冰纯）500ml","itemSnapshotId":100031}],"orderSequenceNumber":"190820170815163502095444322","totalItemQuantity":3}]},"accessToken":""},"header":{"Server":"Apache-Coyote/1.1","Set-Cookie":"JSESSIONID=FB2D22D9908D60CA81B28574F4E3CD77; Path=/market; HttpOnly","Content-Type":"application/json;charset=UTF-8","Transfer-Encoding":"chunked","Date":"Tue, 15 Aug 2017 10:42:25 GMT"},"statusCode":200,"errMsg":"request:ok"};
          data = data.data.response ? data.data.response.datas : {};

          wx.hideLoading();


          if(data == null || data.length == 0){
            _this.data.status.scrollUpStop = true; // 没有数据
            _this.setData({
              status : _this.data.status
            });
          }

          for(var i in data){
            data[i].itemsSize = data[i].items.length;
          }

          if(_load){
            _load = false;
            data = _this.data.ordersData.concat(data);
          }

          _this.setData({
              ordersData: data || {}
          });
      };
      app.ajax(_this,option);
  }
}


Page({
  data: {
    ordersData : {},
    status : {
      tabActive : 11,
      scrollUpStop : false
    }
  },
  onLoad: function () {
    // 初始化信息
    _this = this

    // 获取订单数据
    this.ajax.ordersData()



  },
  onScrollTop: function(e){
    // wx.showLoading({
    //   title: '加载中',
    //   mask : true
    // })
    // loadd = false;
    // this.ajax.ordersData();
  },
  onScrollBottom: function(e){
    console.log("----...."+e.target.offsetTop)
    if(this.data.status.scrollUpStop){
      return false;
    }
    wx.showLoading({
      title: '加载中',
      mask : true
    })
    _load = true;
    config.showOrders.data.ageIndex += 1
    
    this.ajax.ordersData();

  },
  onStatus: function(e){ 
    this.data.status[e.target.dataset.statusKey] = e.target.dataset.statusVal ? e.target.dataset.statusVal : (this.data.status[e.target.dataset.statusKey] ? false : true);


    // 特殊处理 订单状态数据
    if(e.target.dataset.statusKey == 'tabActive'){
      this.setData({ordersData: {}});
      this.data.status.scrollUpStop = false;  // 重置下拉
      config.showOrders.data.ageIndex = 1;    // 重置分页
      this.ajax.ordersData();                 // 拉取数据
    }

    this.setData({status : this.data.status});
  },
  onGo: function(e){
    wx.navigateTo({url: e.target.dataset.url})
  },
  ajax : winAjax
})