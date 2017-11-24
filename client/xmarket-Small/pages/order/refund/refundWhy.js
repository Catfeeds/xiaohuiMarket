
const icons = require('../../../icons')

var app = getApp();
var _this = null;

var winAjax = {

}


Page({
  data: {
    refundData : [
      {id : 1,name : '下单错误，重新购买'},
      {id : 2,name : '这是原因二'},
      {id : 3,name : '这是原因三'},
    ],
    status : {check : 0},
    icons : icons()
  },
  onLoad: function () {
    // 初始化信息
    _this = this
 

  },
  onSubmit: function(){

  },
  onStatus: function(e){
    var data = {};
    this.data.status[e.target.dataset.statusKey] = e.target.dataset.statusVal ? e.target.dataset.statusVal : (this.data.status[e.target.dataset.statusKey] ? false : true);
    data.status = this.data.status;
 
    _this.setData(data);
  },
  ajax : winAjax
})