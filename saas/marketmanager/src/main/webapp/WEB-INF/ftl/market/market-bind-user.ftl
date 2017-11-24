<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>

<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">店铺管理</a></li>
                            <li class="active">店铺绑定管理员</li>
                        </ol>
                        <h4 class="page-title"><b>店铺绑定管理员</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <div class="form-group">
                                <label>选择店铺：</label>
                                <select id="sMarket" style="width:200px;">
                                    <option data_id="0" selected>选择店铺</option>
                                <#if markets?exists && (markets?size > 0)>
                                    <#list markets as market>
                                        <option data_id="${market.id?c}" <#if marketId == market.id>
                                                selected </#if>>
                                        ${market.name}
                                        </option>
                                    </#list>
                                </#if>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>选择管理员类型：</label>
                                <select id="sUser" style="width:200px;">
                                    <option data_id="21" <#if roleType == 21>
                                            selected </#if>>补货员
                                    </option>
                                    <option data_id="31" <#if roleType == 31>
                                            selected </#if>>快递员
                                    </option>
                                </select>
                            </div>

                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-6">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>绑定的管理员ID</th>
                            <th>呢称</th>
                            <th>姓名</th>
                            <th>类型</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="bindTable">

                        <#if binds?exists && (binds?size > 0)>
                            <#list binds as bind>
                            <tr>
                                <td>${bind.passportId?c}</td>
                                <td><#if bind.showName?exists>${bind.showName}</#if></td>
                                <td><#if bind.realName?exists>${bind.realName}</#if></td>
                                <td><#if roleType == 21>补货员<#else>快递员</#if></td>
                                <td><button id="editBtn" type="button"
                                        class="btn waves-effect waves-light btn-danger btn-sm"
                                        data_id="${marketId?c}" data_k="${bind.passportId?c}">解绑
                                </button></td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="5" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>


                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label col-md-4">手机号搜索帐号：</label>
                        <div class="col-md-6">
                            <input type="text" id="phoneInput" class="form-control"
                                   <#if phone?exists>value="${phone}"</#if>>
                        </div>
                        <button id="searchBtn" type="button"
                                class="btn waves-effect waves-light btn-primary col-md-2">搜索
                        </button>
                    </div>

                    <#if passports?exists && (passports?size>0) >
                        <table class="table table-striped table-bordered">
                            <thead class="table_head">
                            <tr>
                                <th>ID</th>
                                <th>手机号</th>
                                <th>呢称</th>
                                <th>真实姓名</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="userListTable">
                                <#list passports as passport>
                                <tr>
                                    <td>${passport.id?c}</td>
                                    <td><#if passport.phoneNumber?exists>${passport.phoneNumber}</#if></td>
                                    <td><#if passport.showName?exists>${passport.showName}</#if></td>
                                    <td><#if passport.realName?exists>${passport.realName}</#if></td>
                                    <td>
                                        <button id="userBindBtn" type="button"
                                                class="btn waves-effect waves-light btn-warning btn-sm"
                                                data_id="${marketId?c}" data_k="${passport.id?c}">绑定
                                        </button>
                                    </td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                    </#if>

                </div>

            </div>

            <!-- end container -->
        </div>


        <script type="text/javascript">
            $(document).ready(function () {

                //当前选中的角色类型
                var roleType = ${roleType};
                var marketId = ${marketId?c};

                function getBindType() {
                    if (roleType == 21) {
                        return 1;
                    } else {
                        return 2;
                    }
                }

                <#if phone?exists>
                    <#if passports?exists && (passports?size>0) >

                    </#if>
                </#if>

                var _sMarket = $("#sMarket");

                _sMarket.select2();
                _sMarket.change(function () {
                    var select_obj = _sMarket.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    var phone = $("#phoneInput").val();
                    open({url: "${base}/market/marketBindUserList.do?marketId=" + data_id + "&roleType=" + roleType + "&phone=" + phone});
                });


                var _sUser = $("#sUser");

                _sUser.select2();
                _sUser.change(function () {
                    var select_obj = _sUser.find("option:selected");
                    roleType = select_obj.attr("data_id");

                    var phone = $("#phoneInput").val();
                    open({url: "${base}/market/marketBindUserList.do?marketId=" + marketId + "&roleType=" + roleType + "&phone=" + phone});
                });

                //搜索帐号
                $("#searchBtn").on('click', function () {

                    var phone = $("#phoneInput").val();
                    if (phone == "") {
                        swal("请输入手机号搜索");
                        return;
                    }

                    open({url: "${base}/market/marketBindUserList.do?marketId=" + marketId + "&roleType=" + roleType + "&phone=" + phone});
                });

                //解绑
            <#if binds?exists && (binds?size > 0)>

                //单项解绑
                $("#bindTable").find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {
                        var btn_ = $(this);
                        var data_id = $(this).attr("data_id");

                        btn_.attr("disabled", true);

                        tokenPost("${base}/market/marketUnBind.do?marketId=" + data_id + "&k=" + $(this).attr("data_k") + "&type=" + getBindType(), function (json) {
                            if (json.code == 0) {
                                //成功了，跳转去目标
                                var phone = $("#phoneInput").val();
                                open({url: "${base}/market/marketBindUserList.do?marketId=" + marketId + "&roleType=" + roleType + "&phone=" + phone});
                            } else {
                                btn_.removeAttr("disabled");
                                swal(json.msg);
                            }
                        });
                    });
                });

            </#if>

              //绑定
            <#if passports?exists && (passports?size > 0)>

                //单项绑定
                $("#userListTable").find('button[id=userBindBtn]').each(function () {
                    $(this).on('click', function () {
                        var btn_ = $(this);
                        var data_id = $(this).attr("data_id");
                        if(data_id == 0) {
                            swal("请先选择店铺");
                            return;
                        }

                        btn_.attr("disabled", true);

                        tokenPost("${base}/market/marketBind.do?marketId=" + data_id + "&k=" + $(this).attr("data_k") + "&type=" + getBindType(), function (json) {
                            if (json.code == 0) {
                                //成功了，跳转去目标
                                var phone = $("#phoneInput").val();
                                open({url: "${base}/market/marketBindUserList.do?marketId=" + marketId + "&roleType=" + roleType + "&phone=" + phone});
                            } else {
                                btn_.removeAttr("disabled");
                                swal(json.msg);
                            }
                        });
                    });
                });

            </#if>

            });
        </script>