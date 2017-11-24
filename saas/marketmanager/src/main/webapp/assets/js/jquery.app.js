/**
 * Theme: Minton Admin Template
 * Author: Coderthemes
 * Module/App: Main Js
 */


// Roar////////......callback


!function ($) {
    "use strict";

    var Sidemenu = function () {
        this.$body = $("body"),
            this.$openLeftBtn = $(".open-left"),
            this.$menuItem = $("#sidebar-menu a")
    };
    Sidemenu.prototype.openLeftBar = function () {
        $("#wrapper").toggleClass("enlarged");
        $("#wrapper").addClass("forced");

        if ($("#wrapper").hasClass("enlarged") && $("body").hasClass("fixed-left")) {
            $("body").removeClass("fixed-left").addClass("fixed-left-void");
        } else if (!$("#wrapper").hasClass("enlarged") && $("body").hasClass("fixed-left-void")) {
            $("body").removeClass("fixed-left-void").addClass("fixed-left");
        }

        if ($("#wrapper").hasClass("enlarged")) {
            $(".left ul").removeAttr("style");
        } else {
            $(".subdrop").siblings("ul:first").show();
        }

        toggle_slimscroll(".slimscrollleft");
        $("body").trigger("resize");
    },
        //menu item click
        Sidemenu.prototype.menuItemClick = function (e) {
            if (!$("#wrapper").hasClass("enlarged")) {
                if ($(this).parent().hasClass("has_sub")) {

                }
                if (!$(this).hasClass("subdrop")) {
                    // hide any open menus and remove all other classes
                    $("ul", $(this).parents("ul:first")).slideUp(350);
                    $("a", $(this).parents("ul:first")).removeClass("subdrop");
                    $("#sidebar-menu .pull-right i").removeClass("md-remove").addClass("md-add");

                    // open our new menu and add the open class
                    $(this).next("ul").slideDown(350);
                    $(this).addClass("subdrop");
                    $(".pull-right i", $(this).parents(".has_sub:last")).removeClass("md-add").addClass("md-remove");
                    $(".pull-right i", $(this).siblings("ul")).removeClass("md-remove").addClass("md-add");
                } else if ($(this).hasClass("subdrop")) {
                    $(this).removeClass("subdrop");
                    $(this).next("ul").slideUp(350);
                    $(".pull-right i", $(this).parent()).removeClass("md-remove").addClass("md-add");
                }
            }
        },

        //init sidemenu
        Sidemenu.prototype.init = function () {
            var $this = this;

            var ua = navigator.userAgent,
                event = (ua.match(/iP/i)) ? "touchstart" : "click";

            //bind on click
            this.$openLeftBtn.on(event, function (e) {
                e.stopPropagation();
                $this.openLeftBar();
            });

            // LEFT SIDE MAIN NAVIGATION
            $this.$menuItem.on(event, $this.menuItemClick);

            // NAVIGATION HIGHLIGHT & OPEN PARENT
            $("#sidebar-menu ul li.has_sub a.active").parents("li:last").children("a:first").addClass("active").trigger("click");
        },

        //init Sidemenu
        $.Sidemenu = new Sidemenu, $.Sidemenu.Constructor = Sidemenu

}(window.jQuery),


    function ($) {
        "use strict";

        var FullScreen = function () {
            this.$body = $("body"),
                this.$fullscreenBtn = $("#btn-fullscreen")
        };

        //turn on full screen
        // Thanks to http://davidwalsh.name/fullscreen
        FullScreen.prototype.launchFullscreen = function (element) {
            if (element.requestFullscreen) {
                element.requestFullscreen();
            } else if (element.mozRequestFullScreen) {
                element.mozRequestFullScreen();
            } else if (element.webkitRequestFullscreen) {
                element.webkitRequestFullscreen();
            } else if (element.msRequestFullscreen) {
                element.msRequestFullscreen();
            }
        },
            FullScreen.prototype.exitFullscreen = function () {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.webkitExitFullscreen) {
                    document.webkitExitFullscreen();
                }
            },
            //toggle screen
            FullScreen.prototype.toggle_fullscreen = function () {
                var $this = this;
                var fullscreenEnabled = document.fullscreenEnabled || document.mozFullScreenEnabled || document.webkitFullscreenEnabled;
                if (fullscreenEnabled) {
                    if (!document.fullscreenElement && !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement) {
                        $this.launchFullscreen(document.documentElement);
                    } else {
                        $this.exitFullscreen();
                    }
                }
            },
            //init sidemenu
            FullScreen.prototype.init = function () {
                var $this = this;
                //bind
                $this.$fullscreenBtn.on('click', function () {
                    $this.toggle_fullscreen();
                });
            },
            //init FullScreen
            $.FullScreen = new FullScreen, $.FullScreen.Constructor = FullScreen

    }(window.jQuery),


//main app module
    function ($) {
        "use strict";

        var App = function () {
            this.VERSION = "1.0.0",
                this.AUTHOR = "Coderthemes",
                this.SUPPORT = "coderthemes@gmail.com",
                this.pageScrollElement = "html, body",
                this.$body = $("body")
        };

        //on doc load
        App.prototype.onDocReady = function (e) {
            FastClick.attach(document.body);
            resizefunc.push("initscrolls");
            resizefunc.push("changeptype");

            $('.animate-number').each(function () {
                $(this).animateNumbers($(this).attr("data-value"), true, parseInt($(this).attr("data-duration")));
            });

            //RUN RESIZE ITEMS
            $(window).resize(debounce(resizeitems, 100));
            $("body").trigger("resize");

            // right side-bar toggle
            $('.right-bar-toggle').on('click', function (e) {

                $('#wrapper').toggleClass('right-bar-enabled');
            });


        },
            //initilizing
            App.prototype.init = function () {
                var $this = this;
                //document load initialization
                $(document).ready($this.onDocReady);
                //init side bar - left
                $.Sidemenu.init();
                //init fullscreen
                $.FullScreen.init();
            },

            $.App = new App, $.App.Constructor = App

    }(window.jQuery),

//initializing main application module
    function ($) {
        "use strict";
        $.App.init();
    }(window.jQuery);


/* ------------ some utility functions ----------------------- */
//this full screen
// var toggle_fullscreen = function () {
//
// }
//
// function executeFunctionByName(functionName, context /*, args */) {
//     var args = [].slice.call(arguments).splice(2);
//     var namespaces = functionName.split(".");
//     var func = namespaces.pop();
//     for(var i = 0; i < namespaces.length; i++) {
//         context = context[namespaces[i]];
//     }
//     return context[func].apply(this, args);
// }
var w, h, dw, dh;
var changeptype = function () {
    w = $(window).width();
    h = $(window).height();
    dw = $(document).width();
    dh = $(document).height();

    if (jQuery.browser.mobile === true) {
        $("body").addClass("mobile").removeClass("fixed-left");
    }

    if (!$("#wrapper").hasClass("forced")) {
        if (w > 990) {
            $("body").removeClass("smallscreen").addClass("widescreen");
            $("#wrapper").removeClass("enlarged");
        } else {
            $("body").removeClass("widescreen").addClass("smallscreen");
            $("#wrapper").addClass("enlarged");
            $(".left ul").removeAttr("style");
        }
        if ($("#wrapper").hasClass("enlarged") && $("body").hasClass("fixed-left")) {
            $("body").removeClass("fixed-left").addClass("fixed-left-void");
        } else if (!$("#wrapper").hasClass("enlarged") && $("body").hasClass("fixed-left-void")) {
            $("body").removeClass("fixed-left-void").addClass("fixed-left");
        }

    }
    toggle_slimscroll(".slimscrollleft");
}


var debounce = function (func, wait, immediate) {
    var timeout, result;
    return function () {
        var context = this, args = arguments;
        var later = function () {
            timeout = null;
            if (!immediate) result = func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) result = func.apply(context, args);
        return result;
    };
}

function resizeitems() {
    if ($.isArray(resizefunc)) {
        for (i = 0; i < resizefunc.length; i++) {
            window[resizefunc[i]]();
        }
    }
}

function initscrolls() {
    if (jQuery.browser.mobile !== true) {
        //SLIM SCROLL
        $('.slimscroller').slimscroll({
            height: 'auto',
            size: "5px"
        });

        $('.slimscrollleft').slimScroll({
            height: 'auto',
            position: 'right',
            size: "5px",
            color: '#dcdcdc',
            wheelStep: 5
        });
    }
}
function toggle_slimscroll(item) {
    if ($("#wrapper").hasClass("enlarged")) {
        $(item).css("overflow", "inherit").parent().css("overflow", "inherit");
        $(item).siblings(".slimScrollBar").css("visibility", "hidden");
    } else {
        $(item).css("overflow", "hidden").parent().css("overflow", "hidden");
        $(item).siblings(".slimScrollBar").css("visibility", "visible");
    }
}

// var wow = new WOW(
//     {
//         boxClass: 'wow', // animated element css class (default is wow)
//         animateClass: 'animated', // animation css class (default is animated)
//         offset: 50, // distance to the element when triggering the animation (default is 0)
//         mobile: false        // trigger animations on mobile devices (true is default)
//     }
// );
// wow.init();

!function ($) {
    "use strict";

    var SweetAlert = function () {
    };

    //init
    $.SweetAlert = new SweetAlert, $.SweetAlert.Constructor = SweetAlert
}(window.jQuery);

var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\.)(\/)(\<)(\>)(\?)(\)]+/);

//containSpecial.test(s)

//
// //initializing
//     function($) {
//         "use strict";
//         //Basic
//         $('#sa-basic').click(function(){
//             swal("Here's a message!");
//         });
//     }(window.jQuery);


function showWarning(msg, callback) {
    swal({
        title: "系统提示?",
        text: msg,
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定!",
        cancelButtonText: "取消",
        closeOnConfirm: true
    }, callback);
}

function showSuccess(msg, callback) {
    swal({
        title: "系统提示",
        text: msg,
        type: "success",
        showCancelButton: false,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "确定",
        closeOnConfirm: true,
    }, callback);
}

function moveEnd(obj) {
    obj.focus();
    var len = obj.value.length;
    if (document.selection) {
        var sel = obj.createTextRange();
        sel.moveStart('character', len); //设置开头的位置
        sel.collapse();
        sel.select();
    } else if (typeof obj.selectionStart == 'number' && typeof obj.selectionEnd == 'number') {
        obj.selectionStart = obj.selectionEnd = len;
    }
}

function getUrl(href) {
    if (href == null || href == '') return {url: '', data: {}}
    var url = {}
    url.href = href.split("?")[0];
    url.data = {};
    if (href.split("?")[1]) {
        url.d = href.split("?")[1].split("&"); // asd=sdasd&asd=asd

        for (var key in url.d) {
            var item = url.d[key].split("=");
            url.data[item[0]] = item[1] || "";
        }
    }
    return url;
}

function checkVal(ui_name, info) {
    var v = $("#" + ui_name).val();
    if (v == null || v == "") {
        swal(info);
        return null;
    }
    //去掉左右空格
    v = v.replace(/(^\s*)|(\s*$)/g, "");
    if (v == "") {
        swal(info);
        return null;
    }
    return v;
}

function tokenPost(url, callback) {
    // var accessToken = $.cookie("accessToken") || "";
    var passportId = $.cookie("passportId") || "";
    $.post(url + "&passportId=" + passportId, function (data) {
        $.cookie("accessToken", data.accessToken, { path: "/", expires: 365 });
        callback(data);
    }, "json");
    // $.post(url, callback, "json");
}

function tokenPresPost(url, pres, callback) {
    // var accessToken = $.cookie("accessToken") || "";
    var passportId = $.cookie("passportId") || "";
    $.post(url + "?passportId=" + passportId, pres, function (data) {
        $.cookie("accessToken", data.accessToken, { path: "/", expires: 365 });
        callback(data);
    }, "json");
    // $.post(url, pres, callback, "json");
}

function open(option) {
    option = $.extend({
        url: '',
        method: 'post',
        target: '_self' //_blank ,_self
    }, option);

    var u = getUrl(option.url);

    var form = $('<form style="display: none"></form>');   // 创建Form
    // 设置属性
    form.attr('action', u.href);
    form.attr('method', option.method);
    form.attr('target', option.target);

    for (var Key in u.data) {
        form.append($('<input type="text" name="' + Key + '" value="' + u.data[Key] + '" />'));
    }

    // var accessToken = $.cookie("accessToken") || "";
    // var passportId = $.cookie("passportId") || "";
    //
    // form.append($('<input type="text" name="accessToken" value="' + accessToken + '" />'));
    // form.append($('<input type="text" name="passportId" value="' + passportId + '" />'));

    // 提交表单
    form.appendTo("body");
    form.submit();

    // 注意return false取消链接的默认动作
    return false;
}

//字符长度，区别中英文
function strlen(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        //单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            len++;
        }
        else {
            len += 2;
        }
    }
    return len;
}

//policy 要经过base64编码， signature 还要进一步处理，可以查阅官方文档
// function OssUpload(param, file, fileName, callBack) {
//     var policyBase64 = Base64.encode(param.policy);
//     var signature = param.signature.split(':')[1];
//     var filePathName = param.filePath + "/" + param.fileName;
//     var fileFullName = param.contentHostName + "/" + filePathName;
//     var request = new FormData();
//     request.append('OSSAccessKeyId', param.accessKeyId);
//     request.append('policy', policyBase64);
//     request.append('Signature', signature);
//     request.append('key', filePathName);
//     for (var i in param.metaDatas) {
//         request.append(i, param.metaDatas[i]);
//     }
//     request.append('file', file);
//     request.append('submit', "Upload to OSS");
//     $.ajax({
//         url: param.contentHostName,
//         data: request,
//         processData: false,
//         cache: false,
//         async: false,
//         contentType: false,
//         //关键是要设置contentType 为false，不然发出的请求头 没有boundary
//         //该参数是让jQuery去判断contentType
//         type: "POST",
//         success: function (data, textStatus, request) {
//             if (textStatus === "nocontent") {
//                 callBack(fileFullName);
//                 alert("success!");
//             } else {
//                 alert(textStatus);
//             }
//         }
//     });
// }

//
// var glo_image_data = {
//         OSSAccessKeyId:'',//需要根据自己的bucket填写 详情请见oss api
//         policy:'',
//         signature:'',
//         success_action_status:'201',
//         key:'img/111/${filename}'
//     },
//     glo_image_upload_url = 'http://xxx.oss-cn-beijing.aliyuncs.com';
//
// function doUploadImage(url, data){
//     var oMyForm = new FormData();
//
//     for(var field_name in data){
//         oMyForm.append(field_name, data[field_name]);
//     }
//
//     oMyForm.append("file", document.getElementByIdx_x('file').files[0]);
//
//     var oReq = new XMLHttpRequest();
//     //上传进度监听
//     oReq.upload.onprogress = function (e) {
//         if(e.type=='progress'){
//             var percent = Math.round(e.loaded/e.total*100,2)+'%';
//             $('#progress').html(percent);//显示进度的容器 自行修改
//         }
//     };
//     //上传结果
//     oReq.onreadystatechange = function(e){
//         if(oReq.readyState == 4){
//             if(oReq.status==201)//这里如果成功返回的是 success_action_status设置的值
//                 alert('成功');
//             else
//                 alert('失败');
//         }
//     };
//     oReq.open("POST", url);
//     oReq.send(oMyForm);
// }
//
// doUploadImage(glo_image_upload_url, glo_image_data);