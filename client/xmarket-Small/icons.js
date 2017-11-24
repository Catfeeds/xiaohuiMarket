

var icons = {
  toUrl : function(icon){
    return this.host + this[icon].u + this[icon] + this.size + this.format;
  },
  host : "http://xmarket.oss-cn-shenzhen.aliyuncs.com/market/app/icon/",
  size : "@3x",
  format : ".png",
  account:{
    "w" : "9.5px",
    "h" : "14px",
    "u" : "accountIcon"
  },
  B_location:{
    "w" : "9.5px",
    "h" : "14px",
    "u" : "B-location"
  },
  O_location:{
    "w" : "9.5px",
    "h" : "14px",
    "u" : "O-location"
  },
  W_location:{
    "w" : "9px",
    "h" : "13px",
    "u" : "W-locationIcon"
  },
  cart:{
    "w" : "",
    "h" : "",
    "u" : "CarIcon"
  },
  check:{
    "w" : "13px",
    "h" : "9px",
    "u" : "checkIcon"
  },
  close:{
    "w" : "15px",
    "h" : "15px",
    "u" : "closeIcon"
  },
  focus:{
    "w" : "",
    "h" : "",
    "u" : "FocusIcon"
  },
  loading:{
    "w" : "",
    "h" : "",
    "u" : "loading"
  },
  minus:{
    "w" : "",
    "h" : "",
    "u" : "minusIcon"
  },
  navigate:{
    "w" : "15px",
    "h" : "16px",
    "u" : "navigateiconicon"
  },
  order:{
    "w" : "33px",
    "h" : "40px",
    "u" : "OrderIcon"
  },
  payment:{
    "w" : "33px",
    "h" : "40px",
    "u" : "paymentIcon"
  },
  receipt:{
    "w" : "49px",
    "h" : "40px",
    "u" : "ReceiptIcon"
  },
  refunds:{
    "w" : "33px",
    "h" : "40px",
    "u" : "RefundsIcon"
  },
  search:{
    "w" : "16px",
    "h" : "16px",
    "u" : "SearchIcon"
  },
  selected:{
    "w" : "",
    "h" : "",
    "u" : "SelectedIcon"
  },
  success:{
    "w" : "93px",
    "h" : "93px",
    "u" : "successIcon"
  },
  search:{
    "w" : "13px",
    "h" : "13px",
    "u" : "SearchIcon"
  },
  trash:{
    "w" : "10px",
    "h" : "12px",
    "u" : "trashIcon"
  }
}




module.exports = function(){
    var imgs = {};
    for(var key in icons){
        if(key == "host" || key == "size" || key == "format") continue;
        imgs[key] = {};
        imgs[key].url = icons.host + icons[key].u + icons.size + icons.format;
        imgs[key].w = icons[key].w;
        imgs[key].h = icons[key].h;
    }
    return imgs;
}