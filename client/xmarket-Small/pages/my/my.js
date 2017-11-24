const icons = require('../../icons')

var app = getApp()
var _this = null
Page({
  data: {
    userInfo: {},
    status:{islogin : true},
    icons : icons()
  },
  onLoad: function () {
    console.log('onLoad')
    var _this = this
    

    app.getUserInfo(function(userInfo){
      //更新数据
      _this.setData({
        userInfo:userInfo
      })
    });


  },
  onGo: function(e){
    wx.navigateTo({url: e.target.dataset.url})
  }
})