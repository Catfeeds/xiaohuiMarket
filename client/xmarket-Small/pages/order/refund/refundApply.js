
var app = getApp();
var _this = null;

var winAjax = {

}


Page({
  data: {
    orderInfo : {},
    status : {}
  },
  onLoad: function () {
    // 初始化信息
    _this = this


  },
  onGo: function(e){
    wx.navigateTo({url: 'refundWhy'})
  },
  ajax : winAjax
})