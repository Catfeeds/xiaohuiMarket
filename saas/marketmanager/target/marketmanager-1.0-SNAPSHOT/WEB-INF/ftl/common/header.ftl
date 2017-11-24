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

    <script type="text/javascript">
        function clearNoNum(obj) {
//先把非数字的都替换掉，除了数字和.   
            obj.value = obj.value.replace(/[^\d.]/g, "");
//必须保证第一个为数字而不是.   
            obj.value = obj.value.replace(/^\./g, "");
//保证只有出现一个.而没有多个.   
            obj.value = obj.value.replace(/\.{2,}/g, ".");
//保证.只出现一次，而不能出现两次以上   
            obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");

            //最后保证是正确的数字
            var str = obj.value;
            if (str.length > 1) {
                //第一个是否为0
                if (str.charAt(0) == '0' && str.charAt(1) != '.') {
                    obj.value = "0";
                }
            }
        }
    </script>

    <#--<#if accessToken??>-->
        <#--<script type="text/javascript">-->
            <#--$.cookie("accessToken", "${accessToken}", { path: "/", expires: 365 });-->
        <#--</script>-->
    <#--</#if>-->