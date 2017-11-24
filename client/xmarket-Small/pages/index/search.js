const config = require('../../config')
const icons = require('../../icons')

var app = getApp()
var user = app.User.get();
var _this = null

var winAjax = {
    goodData : function(itemTypeId){
      var goodData = {}, cart = app.Cart.getCart();
      var option = config.pageItems;
      option.success = function(data){

        if(data.data.code == 100){
          // wx.showToast({
          //   title: data.data.msg,
          //   icon: 'success',
          //   duration: 2000
          // });

          _this.setData({goodData: {}})
          return true;
        }

        // data = data.data.response.pageItemMsg;
        data = [{"unitName":"部","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://oss.0085.com/supplyerp/1490696078398.jpg","name":"iPhone7","batchesCode":"","discountType":0,"maxPrice":10,"stock":100,"barcode":"885909842612","actualSales":0,"deliveryDelay":0,"status":1,"num":0,"idx":0},{"unitName":"部","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100001,"imageUrl":"http://oss.0085.com/supplyerp/1490696078398.jpg","name":"iPhone7","batchesCode":"","discountType":0,"maxPrice":10,"stock":100,"barcode":"885909842612","actualSales":0,"deliveryDelay":0,"status":1,"num":0,"idx":1}];
        for(var i = 0; i < data.length; i++){
          if(cart[data[i].itemId]){
            data[i].num = cart[data[i].itemId].num;
            data[i].check = cart[data[i].itemId].check;
          }else{
            data[i].num = 0;
            data[i].check = false;
          }
          goodData[data[i].itemId] = data[i];
        }
    
        _this.setData({
            goodData: goodData,
            cartTotal : cart.cartTotal
        })
      }
      app.ajax(_this,option);
      
    },
    cartData : function(){
      var cartData = app.Cart.getCart();
      delete cartData.cartTotal;
      return cartData;
    }
}

Page({
  data: {
    goodData : {},
    cartData : {},
    cartTotal :{
      totalNum : 0,
      totalMoney : 0.0
    },
    checkTotal :{
      speciesNum : 0,
      totalNum : 0,
      totalMoney : 0.0
    },
    status : {
      cartShow : false,
      checkAll : false,
      showSearch : 0
    },
    icons : icons()
  },
  onLoad: function () {

    // 初始化信息
    _this = this

    this.ajax.goodData()

  },
  onStatus: function(e){
    var data = {};
    this.data.status[e.target.dataset.statusKey] = e.target.dataset.statusVal ? e.target.dataset.statusVal : (this.data.status[e.target.dataset.statusKey] ? false : true);
    data.status = this.data.status;

    if(e.target.dataset.tempKey && e.target.dataset.tempVal){
      this.data.temps[e.target.dataset.tempKey] = e.target.dataset.tempVal;
      data.temps = this.data.temps;
    }

    // 特殊处理 购物车 
    if(e.target.dataset.statusKey == 'cartShow' && this.data.status.cartShow){
      data.cartData = this.ajax.cartData();       // 加载购物车数据
      data.checkTotal = app.Cart.getCheckTotal(); // 加载购物车数据
    }

    _this.setData(data);
  },
  onCartCheckAll : function(e){
    _this.setData(app.Cart.CHECKALL(this.data, e.target.dataset.check));
  },
  onCart : function(e){
    var data = {};
    data.status = this.data.status;
    data.goodData = this.data.goodData;
    data.cartData = this.data.cartData;
    data.cartTotal = this.data.cartTotal;
    data.checkTotal = this.data.checkTotal;
    _this.setData(app.Cart[e.target.dataset.type](data, e.target.dataset.id));
  },
  onSearch : function(e){
    console.log(e.detail.value);
    this.data.status.showSearch = 1;
    this.setData({status : this.data.status});
    config.pageItems.searchKeyValue = e.detail.value;
    this.ajax.goodData();
  },
  onSearchGood : function(e){
    this.data.status.showSearch = 2;
    this.setData({status : this.data.status});
  },
  onBack : function(){
    wx.navigateBack({
      delta: 1
    })
  },
  ajax : winAjax
})



