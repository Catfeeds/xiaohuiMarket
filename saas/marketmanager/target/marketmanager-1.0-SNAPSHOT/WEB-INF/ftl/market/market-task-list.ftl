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
                            <li class="active"><a href="#">店铺任务列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>店铺任务列表</b></h4>
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

                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>任务ID</th>
                            <th>弹夹编码</th>
                            <th>商品模版ID</th>
                            <th>商品名称</th>
                            <th>商品数量</th>
                            <th>商品条码</th>
                            <th>商品单位</th>
                            <th>任务状态</th>
                            <th>期望执行的日期</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="taskListTable">

                        <#if tasks?exists && (tasks?size > 0)>
                            <#list tasks as task>
                            <tr>
                                <td>${task.taskId?c}</td>
                                <td>${task.locationCode}</td>
                                <td>${task.itemTemplateId?c}</td>
                                <td>${task.itemName}</td>
                                <td>${task.itemQuantity}</td>
                                <td>${task.barcode}</td>
                                <td>${task.unitName}</td>
                                <td><#if task.status == 1><span class="label label-primary">有效(未完成)</span></#if>
                                    <#if task.status == 2><span class="label label-success">已执行</span></#if></td>
                                <td>${task.hopeExecutorDate}</td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-danger btn-sm"
                                            data_id="${task.taskId?c}">取消
                                    </button>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="10" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>

        <#if tasks?exists && (tasks?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/market/marketTasks.do" />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>


        <script type="text/javascript">


            $(document).ready(function () {

                var _sMarket = $("#sMarket");

                _sMarket.select2();
                _sMarket.change(function () {
                    var select_obj = _sMarket.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    open({url:"${base}/market/marketTasks.do?marketId=" + data_id});
                    //location.href = "${base}/market/marketTasks.do?marketId=" + data_id;
                });

                //删除任务列表里的某项
                $("#taskListTable").on("click", "button[id=editBtn]", function () {
                    var task_id = $(this).attr("data_id");
                    //2次确定
                    showWarning("确定要取消这个任务吗？", function (isConfirm) {
                        if (!isConfirm) {
                            return;
                        }
                        //去取消
                        tokenPost("${base}/market/cancelPrepareActionTask.do?taskId=" + task_id, function (json) {
                            if (json.code != 0) {
                                swal(json.msg);
                            } else {
                                var select_obj = _sMarket.find("option:selected");
                                var data_id = select_obj.attr("data_id");
                                open({url:"${base}/market/marketTasks.do?marketId=" + data_id});
                            }
                        }, "json");
                    });
                });
            });
        </script>