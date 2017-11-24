<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/address/address.js"></script>

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
                            <li class="active"><a href="#">店铺列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>权限列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <button id="addBtn" type="button"
                                    class="btn waves-effect waves-light btn-primary">添加权限
                            </button>
                            <div class="input-group m-l-15">
                                <input type="text" id="searchKeyTxt"
                                       class="form-control" placeholder="输入权限名称搜索">
                                <span class="input-group-btn">
                                        <button id="searchBtn" type="button"
                                                class="btn waves-effect waves-light btn-primary"><i
                                                class="fa fa-search"></i></button>
                                </span>
                            </div>

                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>上级权限</th>
                            <th>权限名字</th>
                            <th>权限值</th>
                            <th>请求地址</th>
                            <th>说明</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="operationListTable">

                        <#if (datas?size > 0) >
                            <#list datas as d>
                            <tr>
                                <td>${d.id?c}</td>
                                <td>${(d.fatherId?c)!}</td>
                                <td>${(d.operationKey)!}</td>
                                <td>${(d.operationValue)!}</td>
                                <td>${(d.url)!}</td>
                                <td>${(d.explain)!}</td>
                                <td>${d.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${d.id?c}">编辑
                                    </button>
                                    <button id="delBtn" type="button"
                                            class="btn waves-effect waves-light btn-pink btn-sm"
                                            data_id="${d.id?c}">删除
                                    </button>
                                </td>
                            </tr>

                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="11" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>


                </div>
            </div>

        <#if (datas?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/operation/operations.do?"/>
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>
    </div>
</div>


        <script type="text/javascript">

            $(document).ready(function () {
                var _operationListTable = $("#operationListTable");
                //添加
                $("#addBtn").on('click', function () {

                    //直接跳转
                    //var provinceId = $('#loc_province').val();
                    //var cityId = $('#loc_city').val();
                    //var districtId = $('#loc_district').val();
                    //var streetId = $('#loc_street').val();

                    open({url: "${base}/operation/operationEdit.do" });
                });

                <#if (datas?size > 0)>
                    _operationListTable.find('button[id=editBtn]').each(function () {
                        $(this).on('click', function () {
                            open({url: "${base}/operation/operationEdit.do?operationId=" + $(this).attr("data_id")});
                        });
                    });
                </#if>

                _operationListTable.find('button[id=delBtn]').each(function () {
                    $(this).on('click', function () {
                        var post_data = {
                            operationId:$(this).attr("data_id"),
                        };
                        tokenPresPost("${base}/operation/operationDel.do", post_data, function (data) {
                            //重新刷新
                            if(data.code == "0") {
                                showSuccess(data.msg, function () {
                                    var url = "${base}/operation/operations.do?";
                                    open({ url: url });
                                });
                            } else {
                                //$(this).button("reset");
                                btn_.removeAttr("disabled");
                                swal(data.msg);
                            }

                        }, "json");
                    });
                });

                //搜索
                $("#searchBtn").on('click', function () {
                    //searchHandler();
                });

            });
        </script>