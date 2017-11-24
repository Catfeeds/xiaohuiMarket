<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta name="description" content="智能超市管理系统-小惠科技公司版权所有">
    <meta name="author" content="Coderthemes">

    <link rel="shortcut icon" href="${res}/assets/images/favicon_1.ico">

    <title>智能超市后台管理系统</title>

    <link href="${res}/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/core.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/icons.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/components.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/pages.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/menu.css" rel="stylesheet" type="text/css">
    <link href="${res}/assets/css/responsive.css" rel="stylesheet" type="text/css">

    <!-- jQuery  -->
    <script src="${res}/assets/js/jquery.min.js"></script>
    <script src="${res}/assets/js/bootstrap.min.js"></script>

</head>

<body class="fixed-left">

<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                ${error}
                    <br>
                    <button id="backBtn" type="button"
                            class="btn waves-effect waves-light btn-default col-md-2">返回
                    </button>
                </div>
            </div>
        </div>

    </div>
</div>


<script type="text/javascript">
    $(document).ready(function () {

    <#--<#if accessToken?? >-->
        <#--$.cookie("accessToken", ${accessToken}, {expires: 365});-->
    <#--</#if>-->

        $("#backBtn").on('click', function () {
            history.back(-1);
        });
    });
</script>

</body>