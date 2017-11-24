
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta name="description" content="智能超市管理系统-小惠科技公司版权所有">
    <meta name="author" content="Coderthemes">

    <link rel="shortcut icon" href="${res}/assets/images/favicon_1.ico">

    <title>智能超市后台管理系统</title>

    <link href="${res}/assets/plugins/sweetalert/dist/sweetalert.css" rel="stylesheet" type="text/css">

    <link href="${res}/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/core.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/icons.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/components.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/pages.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/menu.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/responsive.css" rel="stylesheet" type="text/css">

    <script src="${res}/assets/js/modernizr.min.js"></script>

    <!-- jQuery  -->
    <script src="${res}/assets/js/jquery.min.js"></script>
    <script src="${res}/assets/js/jquery.cookie.js"></script>
    <script src="${res}/assets/js/bootstrap.min.js"></script>
</head>

    <body style="height: auto;width: auto;background-image: url('${res}/assets/images/bg.jpg')">


        <div class="wrapper-page">

            <div class="text-center">
                <a href="#" class="logo logo-lg"><i class="fa fa-windows"></i> <span>智能超市后台管理系统</span> </a>
            </div>

            <form class="form-horizontal m-t-20">

                <div class="form-group">
                    <div class="col-xs-12">
                        <input id="ipUserName" class="form-control" type="text" placeholder="用户名">
                        <i class="fa fa-user form-control-feedback l-h-34"></i>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-12">
                        <input id="ipPassWord" class="form-control" type="password" placeholder="密码">
                        <i class="fa fa-key form-control-feedback l-h-34"></i>
                    </div>
                </div>

                <div class="form-group m-t-20">

                    <div class="col-xs-6">
                        <div class="checkbox checkbox-primary">
                            <input id="checkbox-signup" type="checkbox">
                            <label for="checkbox-signup" class="text-a">
                                记住用户名
                            </label>
                        </div>
                    </div>

                    <div class="col-xs-6 text-right">
                        <button class="btn btn-primary btn-custom w-md waves-effect waves-light" id="loginBtn">登录
                        </button>
                    </div>
                </div>

                <div class="form-group m-t-30">
                    <div class="col-sm-7">
                        <a href="pages-recoverpw.html" class="text-a"><i class="fa fa-lock m-r-5"></i> 忘记密码?</a>
                    </div>
                    <div class="col-sm-5 text-right">
                        <a href="pages-register.html" class="text-a"><i class="fa fa-user-plus m-r-5"></i> 注册</a>
                    </div>
                </div>
            </form>
        </div>



        <script>
            var resizefunc = [];
        </script>


        <!-- 导航相关的效果js -->
        <script src="${res}/assets/js/detect.js"></script>
        <script src="${res}/assets/js/fastclick.js"></script>
        <script src="${res}/assets/js/jquery.slimscroll.js"></script>
        <script src="${res}/assets/js/jquery.core.js"></script>
        <script src="${res}/assets/js/jquery.app.js"></script>

        <!-- Sweet-Alert  -->
        <script src="${res}/assets/plugins/sweetalert/dist/sweetalert.min.js"></script>
        <script src="${res}/assets/pages/jquery.sweet-alert.init.js"></script>


        <script type="text/javascript">

            $(document).ready(function () {

                var loginName = $.cookie("loginName");
                if(loginName != null) {
                    $("#checkbox-signup").attr("checked", true);
                    $("#ipUserName").val(loginName);
                    $("#ipPassWord").focus();
                } else {
                    $("#ipUserName").focus();
                }

                <#if error?? >
                    swal("${error}");
                </#if>

                //保存商品单位
                $("#loginBtn").on('click', function () {

                    //检测
                    var name = $("#ipUserName").val();
                    if (name == "") {
                        swal("请输入用户名!");
                        return;
                    }
                    //如果格式错误
                    if(containSpecial.test(name)) {
                        swal("用户名包含了特殊符号!");
                        return;
                    }

                    var pass = $("#ipPassWord").val();
                    if (pass == "") {
                        swal("请输入密码!");
                        return;
                    }
                    //如果格式错误
                    if(containSpecial.test(pass)) {
                        swal("密码包含了特殊符号!");
                        return;
                    }

                    //如果需要记住用户名，将信息存储到cookie
                    if ($("#checkbox-signup").is(':checked')) {
                        $.cookie("loginName", name, { expires: 365 });
                    } else {
                        $.cookie("loginName", '', { expires: -1 });
                    }

                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    $.post("${base}/passportopen/login.do?userName=" + name + "&passWord=" + pass, function (data) {

                        //$(this).button("reset");
                        //重新刷新
                        if (data.code == "0") {
                            //登录成功，记录登录的用户ID，显示名称
                            var passport = data.response;
                            //记录这个passportId
                            $.cookie("passportId", passport.passportId, { path: "/", expires: 365 });
                            $.cookie("showName", passport.showName, { path: "/", expires: 365 });
                            $.cookie("accessToken", passport.accessToken, { path: "/", expires: 365 });

                            open({url:"${base}/passport/main.do?passportId="+passport.passportId})
                            //location.href = "${base}/passport/main.do?passportId="+passport.id;
                        } else {
                            //$("#loginBtn").button("reset");
                            btn_.removeAttr("disabled");
                            swal(data.msg);
                        }
                    }, "json");
                });
            });
        </script>
    </body>

</html>