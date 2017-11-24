const config = require('../../config')
const icons = require('../../icons')

var app = getApp();
var user = app.User.get();
var _this = null;

var winAjax = {
    cartData : function(){
      _this.setData(app.Cart.getCheckData());
    },
    prepareCreateOrder : function(){
      var option = config.prepareCreateOrder;
      // option.data.
      option.success = function(data){
        console.log("预下单号："+data.data.response.sequenceNumber);
        _this.setData({sequenceNumber : data.data.response.sequenceNumber});
      };
      app.ajax(_this,option);
    },
    generateOrder : function(){
      var option = config.generateOrder;
      if(_this.data.sequenceNumber != null){
        option.data.sequenceNumber = _this.data.sequenceNumber;
      }else{
        wx.showToast({
          title: '正在创建订单..',
          icon: 'loading',
          duration: 2000
        })

        _this.data.status.isTap = true;
        _this.setData({status : _this.data.status});
        return false;
      }
      wx.showLoading({
        title: '加载中',
      })

      option.data.marketId = _this.data.market.id;
      option.data.deliverType = _this.data.status.tabActive;
      option.data.currentLocation = user.location.lat+","+user.location.lot;
        
      var itemSet = {};
      for(var key in _this.data.cartData){
        if(itemSet[key] == null) itemSet[key] = {};
        itemSet[key] = _this.data.cartData[key].num;
      }
      option.data.itemTemplateSet = itemSet;

      if(option.data.deliverType == 2){
        // 配送
        option.data.receiptProvince = "";
        option.data.receiptCity = "";
        option.data.receiptDistrict = "";
        option.data.receiptAddress = "";
        option.data.receiptNickName = "";
        option.data.receiptPhone = "";
        option.data.receiptLocation = "";
      }

      option.success = function(data){
        if(data.data.code == 0){
            winAjax.paymentOrder(data.data.response.orderSequenceNumber);
        }else{
          wx.showToast({
            title: data.data.msg,
            icon: 'loading',
            duration: 2000
          })
        }
      };
      app.ajax(_this,option);
    },
    paymentOrder : function(orderSequenceNumber){
      var option = config.paymentOrder;
      console.log(orderSequenceNumber);
      option.data.orderSequenceNumber = orderSequenceNumber;
      option.data.paymentType = 'BALANCE';
      option.success = function(data){
        if(data.data.code == 0){
          winAjax.BALANCE(data.data.response);
          wx.hideLoading();
        }else{
          wx.showToast({
            title: data.data.msg,
            icon: 'loading',
            duration: 2000
          })
        }
      };
      app.ajax(_this,option);
    },
    BALANCE : function(successData){
      var option = config.pay.BALANCE;
      option.data.paymentParameter = successData
      option.success = function(data){
        if(data.data.code == 0){
          wx.showToast({
            title: '支付成功',
            icon: 'success',
            duration: 2000
          })
          setTimeout(function(){
            wx.reLaunch({'url' : '../index/index'});
          },1000)
        }else{
          wx.showToast({
            title: data.data.msg,
            icon: 'loading',
            duration: 2000
          })
        }
      };
      app.ajax(_this,option);
    }
}



Page({
  data: {
    sequenceNumber : null, // 预下单号
    market : {},
    cartData : [],
    cartTotal :{
      totalNum : 0,
      totalMoney : 0.0
    },
    checkTotal :{
      totalNum : 0,
      totalMoney : 0.0
    },
    status : {tabActive : 1,isTap: true},
    icons : icons()
  },
  onLoad: function () {
    // 初始化信息
    _this = this

    // 加载店铺信息
    this.setData({
      market : user.market
    });
    
    // 加载购物车数据
    this.ajax.cartData();

    //预下单
    this.ajax.prepareCreateOrder()


    console.log(this.data);
  },
  onPay: function(e){
    if(e.target.dataset.istap){
      this.data.status.isTap = false;
      this.setData({status : this.data.status});
      this.ajax.generateOrder()
    }
  },
  onStatus: function(e){
    var data = {};
    this.data.status[e.target.dataset.statusKey] = e.target.dataset.statusVal ? e.target.dataset.statusVal : (this.data.status[e.target.dataset.statusKey] ? false : true);
    data.status = this.data.status;
 
    this.setData(data);
  },
  ajax : winAjax
})