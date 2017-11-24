<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>

<div class="modal" tabindex="-1" role="dialog" id="upImgDialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="mySmallModalLabel">Small modal</h4>
            </div>
            <div class="modal-body">
                <div class="progress">
                    <div id="upImgBar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0"
                         aria-valuemax="100">
                        <span class="sr-only"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">系统管理</a></li>
                            <li class="active">角色帐号列表</li>
                        </ol>
                        <h4 class="page-title"><b>角色帐号列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <div class="form-group">
                                <label>选择管理员类型：</label>
                                <select id="sUser" style="width:200px;">
                                    <option data_id="0">请选择</option>
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
                <div class="col-sm-8">

                    <table id="pTable" class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>呢称</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>绑定的店铺</th>
                            <#--<th>操作</th>-->
                        </tr>
                        </thead>
                        <tbody>
                        <#assign jumpCount=3 >
                        <#if passports?exists && (passports?size > 0)>
                            <#list passports as passport >
                            <tr>
                                <td>${passport.passportId?c}</td>
                                <td><#if passport.showName?exists>${passport.showName}</#if></td>
                                <td><#if passport.realName?exists>${passport.realName}</#if></td>
                                <td><#if passport.phoneNumber?exists>${passport.phoneNumber}</#if></td>
                                <td>
                                    <#if passport.relationship?exists >
                                        <#assign relationships=passport.relationship >
                                        <#list relationships as relationship>
                                            <#if relationship_index == jumpCount>
                                                <#break>
                                            </#if>
                                        ${relationship.marketName},
                                        </#list>
                                        <#if (relationships?size > jumpCount) >
                                            <button id="seeBtn" type="button"
                                                    class="btn waves-effect waves-light btn-danger btn-sm"
                                                    data_id="${passport.passportId?c}">查看更多
                                            </button>
                                        </#if>
                                    </#if>
                                </td>
                                <#--<td><button id="unBindBtn" type="button"-->
                                            <#--class="btn waves-effect waves-light btn-danger btn-sm"-->
                                            <#--data_id="${passport.passportId?c}">解绑-->
                                <#--</button></td>-->
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
            </div>

        <#if passports?exists && (passports?size > 0)>
            <div class="row small_page">
                <div class="col-sm-8">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/passport/passportList.do?roleType=${roleType}" />
                </div>
            </div>
        </#if>

            <!-- end container -->
        </div>


        <script type="text/javascript">
            $(document).ready(function () {

                //当前选中的角色类型
                var roleType = ${roleType};

                var _relationship = {};
            <#if passports?exists && (passports?size > 0)>
                <#list passports as passport >
                    <#if passport.relationship?exists>
                        <#assign relationships =passport.relationship >
                        <#if (relationships?size > jumpCount) >
                            var ms_${passport_index} = new Array();
                            <#list relationships as relationship>
                                ms_${passport_index}[${relationship_index}] = "${relationship.marketName}";
                            </#list>
                            _relationship[${passport.passportId?c}] = ms_${passport_index};
                        </#if>
                    </#if>
                </#list>

                //查看所有绑定的店铺
                $("#pTable").find('button[id=seeBtn]').each(function () {
                    $(this).on('click', function () {
                        var data_id = $(this).attr("data_id");
                        var rs = _relationship[data_id];
                        if (rs) {
                            var infos = "";
                            var ind = 0;
                            for (ind in rs) {
                                infos = infos + rs[ind] + ", ";
                                if (ind % ${jumpCount} == 0) {
                                    infos = infos + "\n";
                                }
                            }
                            swal(infos);
                        }
                    });
                });

                <#--//解绑-->
                <#--$("#pTable").find('button[id=seeBtn]').each(function () {-->
                    <#--$(this).on('click', function () {-->
                        <#--var data_id = $(this).attr("data_id");-->
                        <#--var rs = _relationship[data_id];-->
                        <#--if (rs) {-->
                            <#--var infos = "";-->
                            <#--var ind = 0;-->
                            <#--for (ind in rs) {-->
                                <#--infos = infos + rs[ind] + ", ";-->
                                <#--if (ind % ${jumpCount} == 0) {-->
                                    <#--infos = infos + "\n";-->
                                <#--}-->
                            <#--}-->
                            <#--swal(infos);-->
                        <#--}-->
                    <#--});-->
                <#--});-->
            </#if>

                function getBindType() {
                    if (roleType == 21) {
                        return 1;
                    } else {
                        return 2;
                    }
                }

                var _sUser = $("#sUser");

                _sUser.select2();
                _sUser.change(function () {
                    var select_obj = _sUser.find("option:selected");
                    roleType = select_obj.attr("data_id");

                    open({url: "${base}/passport/passportList.do?roleType=" + roleType});
                });

            });
        </script>