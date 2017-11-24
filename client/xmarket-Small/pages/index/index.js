const config = require('../../config')
const icons = require('../../icons')

var app = getApp()
var user = app.User.get();
var _this = null

var winAjax = {
    marketData : function(){
      var marketData = {} ,currMarket = user.market;
      var option = config.showMarkets;

      option.success = function(data){
        // wx.showModal({
        //   title: '提示',
        //   content: JSON.stringify(data),
        //   success: function(res) {
        //   }
        // })
          // var data = {data : {"msg":"请求成功","code":0,"response":{"datas":[{"adminName":"","formatDistance":"0米","formatAddress":"广东省广州市增城市中新镇团结村","address":"御峰四街3号","coveringDistance":"1公里","deliveryMode":1,"showPhoneNumber":"1868***3352","name":"恒大山水城店","id":100000,"hidePhoneNumber":"18680233352","type":1,"deliveryCost":0}]},"accessToken":""}};
          
          data = data.data.response ? data.data.response.datas : {};
          for(var i = 0; i < data.length; i++){
            if(currMarket != null && currMarket.id == data[i].id){

            }else{
              marketData[data[i].id] = data[i];
            }
          }
          _this.setData({
              marketData: marketData
          });
      };
      app.ajax(_this,option);
    },
    itemTypes : function(){
        var option = config.itemTypes;
        option.success = function(data){
            _this.setData({
                typeList: data.data.response ? data.data.response.datas : {}
            })
        };
        app.ajax(_this,option);
    },
    goodData : function(itemTypeId){
      var goodData = {}, cart = app.Cart.getCart();
      var option = config.pageItems;
      if(_this.data.currMarket.id == null){
        _this.data.status.canvasShow = true;
        _this.data.status.temp_canvas = "temp_offcanvas_market";
        _this.setData({
            status: _this.data.status
        })
        return false;
      }
      option.data.marketId = _this.data.currMarket.id;
      option.data.itemTypeId = itemTypeId || 0;
      option.success = function(data){
        if(data.data.code == 100){
          wx.showToast({
            title: data.data.msg,
            icon: 'success',
            duration: 2000
          });
          _this.setData({goodData: {}})
          return true;
        }
        // var data = {"data":{"msg":"请求成功","code":0,"response":{"pageItemMsg":[{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味原味原味原味原味原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜洽瓜洽瓜洽瓜洽瓜洽瓜洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1}],"buyMessage":{"100000":{"beyondControl":0,"showHasBuy":"您已购买0份","hasBuy":0,"showDiscount":"暂无优惠"}}},"accessToken":""},"header":{"Set-Cookie":"JSESSIONID=6B9C2AD161F0527029DAF98BAAC26937;path=/;HttpOnly","Content-Type":"application/json;charset=UTF-8","Transfer-Encoding":"chunked","Date":"Mon, 21 Aug 2017 08:14:40 GMT"},"statusCode":200,"errMsg":"request:ok"};
        
        // data = [{"unitName":"部","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://oss.0085.com/supplyerp/1490696078398.jpg","name":"iPhone7","batchesCode":"","discountType":0,"maxPrice":10,"stock":100,"barcode":"885909842612","actualSales":0,"deliveryDelay":0,"status":1,"num":0,"idx":0},{"unitName":"部","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100001,"imageUrl":"http://oss.0085.com/supplyerp/1490696078398.jpg","name":"iPhone7","batchesCode":"","discountType":0,"maxPrice":10,"stock":100,"barcode":"885909842612","actualSales":0,"deliveryDelay":0,"status":1,"num":0,"idx":1}];
        // var data = {"data":{"msg":"请求成功","code":0,"response":{"pageItemMsg":[{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味原味原味原味原味原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100001,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100002,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜洽瓜洽瓜洽瓜洽瓜洽瓜洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100003,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100004,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100005,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100006,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100007,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100008,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100009,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100010,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1},{"unitName":"份","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://ksupplychain.oss-cn-shenzhen.aliyuncs.com/supplychain/items/i_100000.jpg","name":"洽洽瓜子原味150g","batchesCode":"","discountType":0,"maxPrice":10,"stock":86,"barcode":"6924187829428","actualSales":0,"deliveryDelay":0,"status":1}],"buyMessage":{"100000":{"beyondControl":0,"showHasBuy":"您已购买0份","hasBuy":0,"showDiscount":"暂无优惠"}}},"accessToken":""},"header":{"Set-Cookie":"JSESSIONID=6B9C2AD161F0527029DAF98BAAC26937;path=/;HttpOnly","Content-Type":"application/json;charset=UTF-8","Transfer-Encoding":"chunked","Date":"Mon, 21 Aug 2017 08:14:40 GMT"},"statusCode":200,"errMsg":"request:ok"};
        data = data.data.response.pageItemMsg;
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
      // var data = [{"unitName":"部","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100000,"imageUrl":"http://oss.0085.com/supplyerp/1490696078398.jpg","name":"iPhone7","batchesCode":"","discountType":0,"maxPrice":10,"stock":100,"barcode":"885909842612","actualSales":0,"deliveryDelay":0,"status":1,"num":0,"idx":0},{"unitName":"部","maximumSellCount":100,"minimumSellCount":1,"discountPrice":0,"sellPrice":10,"restrictionQuantity":-1,"itemTemplateId":100000,"itemId":100001,"imageUrl":"http://oss.0085.com/supplyerp/1490696078398.jpg","name":"iPhone7","batchesCode":"","discountType":0,"maxPrice":10,"stock":100,"barcode":"885909842612","actualSales":0,"deliveryDelay":0,"status":1,"num":0,"idx":1}];
    },
    orderData : function(){
      var option = config.showOrders;
      option.data.statusEnter = 11;  // 待取货
      option.data.ageIndex = 1;
      option.data.ageSize = 100;

      option.success = function(data){
          var data = {"data":{"msg":"请求成功","code":0,"response":{"datas":[{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":8,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100131},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100132},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100133},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100134}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":8,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100127},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100128},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100129},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100130}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":8,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100123},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100124},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100125},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100126}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":8,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100119},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100120},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100121},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100122}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":8,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100115},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100116},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100117},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100118}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":8,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100107},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100108},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100109},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100110}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":0,"orderStatus":1,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"洽洽瓜子原味150g","quantity":10,"price":10,"itemSnapshotId":100006}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":0,"orderStatus":1,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"洽洽瓜子原味150g","quantity":10,"price":10,"itemSnapshotId":100005}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":1,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100111},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100112},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100113},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100114}],"totalItemQuantity":10,"marketId":100000},{"marketName":"恒大山水城店","actualPrice":116,"orderStatus":1,"marketFormatAddress":"广东省广州市增城市中新镇团结村","deliverStatus":0,"items":[{"itemTemplateId":100000,"itemId":100000,"image":"http://oss.0085.com/supplyerp/1490696078398.jpg","itemName":"iPhone7","quantity":2,"price":10,"itemSnapshotId":100103},{"itemTemplateId":100001,"itemId":100001,"image":"http://oss.0085.com/supplyerp/1490696237981.jpg","itemName":"iPhone7 Plus","quantity":2,"price":11,"itemSnapshotId":100104},{"itemTemplateId":100002,"itemId":100002,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6","quantity":4,"price":12,"itemSnapshotId":100105},{"itemTemplateId":100003,"itemId":100003,"image":"http://oss.0085.com/2016/1207/supplychaint/14811263610893.jpeg","itemName":"iPhone6 Plus","quantity":2,"price":13,"itemSnapshotId":100106}],"totalItemQuantity":10,"marketId":100000}]},"accessToken":""},"header":{"Server":"Apache-Coyote/1.1","Set-Cookie":"JSESSIONID=7071EE0FD4EB85E6A851084870DC4C4C; Path=/; HttpOnly","Content-Type":"application/json;charset=UTF-8","Transfer-Encoding":"chunked","Date":"Tue, 15 Aug 2017 02:11:39 GMT"},"statusCode":200,"errMsg":"request:ok"};
          
          data = data.data.response ? data.data.response.datas : {};
          for(var i in data){
            data[i].itemsSize = data[i].items.length;
          }
          _this.setData({
              orderData: data
          });
      };
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
    location : {hasLocation : false},
    marketData : [],
    currMarket : false,
    typeList : [{"image":"http://oss.0085.com/supplyerp/1490943599954.jpg","name":"电子/数码","icon":"http://oss.0085.com/2017/0112/supplychaint/14841891670002.jpeg","id":100000,"fillImage":1,"subItemTypes":[{"image":"http://oss.0085.com/supplyerp/1490943792006.jpg","name":"手机","icon":"http://oss.0085.com/supplyerp/1490943850667.jpg","id":100001,"fillImage":0},{"image":"http://oss.0085.com/2017/0112/supplychaint/14841891670002.jpeg","name":"数据线","icon":"http://oss.0085.com/2017/0112/supplychaint/14841891670002.jpeg","id":100002,"fillImage":0},{"image":"http://oss.0085.com/2017/0112/supplychaint/14841891190230.jpeg","name":"数码配件","icon":"http://oss.0085.com/2017/0112/supplychaint/14841891190230.jpeg","id":100003,"fillImage":0},{"image":"","name":"影音娱乐","icon":"","id":100018,"fillImage":0}]},{"image":"http://supplymc.0085.com/static/img/homepage/type5.png","name":"特价酒水","icon":"http://supplymc.0085.com/static/img/homepage/type5.png","id":100019,"fillImage":1,"subItemTypes":[{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90044.jpg","name":"啤酒","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90044.jpg","id":100020,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90045.jpg","name":"劲酒","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90045.jpg","id":100021,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90050.jpg","name":"红酒","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90050.jpg","id":100022,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90051.jpg","name":"洋酒","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90051.jpg","id":100023,"fillImage":0}]},{"image":"http://supplymc.0085.com/static/img/homepage/type1.png","name":"零食糖果","icon":"http://supplymc.0085.com/static/img/homepage/type1.png","id":100004,"fillImage":1,"subItemTypes":[{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT11013.jpg","name":"口香糖","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT11013.jpg","id":100005,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT11015.jpg","name":"花生瓜子","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT11015.jpg","id":100006,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90055.jpg","name":"糖果","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90055.jpg","id":100007,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90056.jpg","name":"饼干膨化","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90056.jpg","id":100008,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90062.jpg","name":"进口产品","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90062.jpg","id":100009,"fillImage":0}]},{"image":"http://supplymc.0085.com/static/img/homepage/type3.png","name":"日用洗护","icon":"http://supplymc.0085.com/static/img/homepage/type3.png","id":100013,"fillImage":1,"subItemTypes":[{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT16003.jpg","name":"计生日用","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT16003.jpg","id":100014,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT16006.jpg","name":"洗护类","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT16006.jpg","id":100015,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90027.jpg","name":"纸类","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90027.jpg","id":100016,"fillImage":0},{"image":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90046.jpg","name":"牙膏牙刷","icon":"http://1life.oss-cn-shenzhen.aliyuncs.com/supplychain/item_template/GT90046.jpg","id":100017,"fillImage":0}]}],
    goodData : [],
    orderData : [],
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
      typeShow : 0,
      typeSubShow : 0,
      checkAll : false,
      canvasShow : false
    },
    temps : {
      temp_main : "temp_hot",
      temp_canvas : ""
    },
    icons : icons()
  },
  onLoad: function (option) {
    console.log(option);

    // 初始化信息
    _this = this
    // 获取位置
    this.getLocation()
    // 检测店铺
    if(user.market == null){
      this.ajax.marketData();
      this.data.temps.temp_canvas = 'temp_offcanvas_market';
      this.data.status.canvasShow = true;
    }else{
      this.data.currMarket = user.market;
    }
    this.data.cartTotal = app.Cart.getCart().cartTotal;
    this.data.checkTotal = app.Cart.getCheckTotal(); // 加载选中商品total
    this.setData(this.data);

    // 加载分类
    this.ajax.itemTypes()


    // 加载 热卖
    this.getHot() 

  },
  getHot: function(){
    // 记载 待取货订单
    this.ajax.orderData()
    // this.ajax.hotData()
  },
  getLocation: function () {
    wx.showLoading({
      title: '定位中'
    })
    wx.getLocation({
      type: 'wgs84',
      success: function (res) {

      wx.showLoading({
        title: '解析中'
      })
        user.location = {
          hasLocation: true,
          lot: res.longitude,
          lat : res.latitude
        };

        wx.request({  //百度地图经纬度反查路径   ak=btsVVWf0TM1zUBEbzFz6QqWF
          url: 'http://api.map.baidu.com/geocoder/v2/?ak=926joUfDFejSEZYXlH1atEFiGSaGiDkm&location=' + res.latitude + ',' + res.longitude + '&output=json&pois=0',
          
          success: function (ops) {

            wx.showLoading({
              title: '解析中成功'
            })
            user.location.country = ops.data.result.addressComponent.country;
            user.location.formatted_address = ops.data.result.formatted_address;
            _this.setData({location : user.location});
            app.User.set(user);
            _this.setData({loading: false});
            wx.hideLoading();
          }
        })
        setTimeout(function(){
          wx.hideLoading();
        },3000)
      },
      fail: function({errMsg}) {
        _this.setData({loading: false});
      }
    })
  },
  onStatus: function(e){
    var data = {};
    this.data.status[e.target.dataset.statusKey] = e.target.dataset.statusVal ? e.target.dataset.statusVal : (this.data.status[e.target.dataset.statusKey] ? false : true);
    data.status = this.data.status;

    if(e.target.dataset.tempKey && e.target.dataset.tempVal){
      this.data.temps[e.target.dataset.tempKey] = e.target.dataset.tempVal;
      data.temps = this.data.temps;
    }

    // 特殊处理 分类
    if(e.target.dataset.statusKey == 'typeShow'){
      this.data.status.typeSubShow = 0;
    }


    // 特殊处理 购物车 
    if(e.target.dataset.statusKey == 'cartShow' && this.data.status.cartShow){
      data.cartData = this.ajax.cartData();       // 加载购物车数据
      data.checkTotal = app.Cart.getCheckTotal(); // 加载购物车数据
    }

    // 特殊处理 选择店铺
    if(e.target.dataset.tempVal == 'temp_offcanvas_market'){
      this.ajax.marketData(); // 加载店铺数据
    }

    // 特殊处理 选择分类 data-temp-val
    if(e.target.dataset.tempVal == 'temp_hot'){
      this.getHot()
    }else if(e.target.dataset.tempVal == 'temp_goods'){
      this.ajax.goodData(e.target.dataset.statusVal);
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
  onMarket : function(e){
    user.market = this.data.marketData[e.target.dataset.id];
    delete this.data.marketData[e.target.dataset.id];
    app.User.set(user);
    this.data.status.canvasShow = false;
    this.setData({
      marketData : this.data.marketData,
      currMarket : user.market,
      status: this.data.status
    })
  },
  ajax : winAjax
})



