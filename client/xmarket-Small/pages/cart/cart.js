const icons = require('../../icons')

var app = getApp()
var _this = null

var winAjax = {
    cartData : function(){
      var cart = app.Cart.getCart(), data = {};
      for(var key in cart){
        if(key == 'cartTotal') continue;
        data[key] = cart[key];
      }
      
      _this.setData({
          cartData: data,
          cartTotal : cart.cartTotal
      })
    }
}



Page({
  data: {
    cartData : [],
    cartTotal :{
      totalNum : 0,
      totalMoney : 0.0
    },
    checkTotal :{
      totalNum : 0,
      totalMoney : 0.0
    },
    status : {checkAll : false},
    icons : icons()
  },
  onLoad: function () {
    // 初始化信息
    _this = this

    // 加载购物车数据
    this.ajax.cartData();
    // 加载选中商品total
    _this.setData({checkTotal : app.Cart.getCheckTotal()});

    
  },
  onCartCheckAll : function(e){
    _this.setData(app.Cart.CHECKALL(this.data, e.target.dataset.check));
  },
  onCart : function(e){
    var data = {};
    data.status = this.data.status;
    data.cartData = this.data.cartData;
    data.cartTotal = this.data.cartTotal;
    data.checkTotal = this.data.checkTotal;
    _this.setData(app.Cart[e.target.dataset.type](data, e.target.dataset.id));
  },
  ajax : winAjax
})