
var app = getApp()

Page({
  data: {
  	check : true
  },
  onLoad: function () {

  },
  onGo: function(e){
    wx.navigateTo({url: e.target.dataset.url})
  }
})