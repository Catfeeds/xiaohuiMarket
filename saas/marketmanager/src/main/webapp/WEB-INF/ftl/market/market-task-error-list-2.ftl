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
                            <li class="active"><a href="#">店铺异常任务列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>店铺异常任务列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <button id="backBtn" type="button"
                                class="btn waves-effect waves-light btn-primary" >返回
                            </button>

                            <div class="form-group">
                                <label>店铺 ${marketName} 的任务情况</label>
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
                            <th>弹夹编码</th>
                            <th>商品模版ID</th>
                            <th>商品名称</th>
                            <th>商品单位</th>
                            <th>商品数量</th>
                            <th>商品条码</th>
                            <th>任务类型</th>
                            <th>任务状态</th>
                            <th>期望执行的日期</th>
                            <th>实际完成日期</th>
                        </tr>
                        </thead>
                        <tbody id="taskListTable">

                        <#if tasks?exists && (tasks?size > 0)>
                            <#list tasks as task>
                            <tr>
                                <td>${task.locationCode}</td>
                                <td>${task.itemTemplateId?c}</td>
                                <td>${task.itemName}</td>
                                <td>${task.unitName}</td>
                                <td>${task.itemQuantity}</td>
                                <td>${task.barcode}</td>
                                <td><#if task.type == 1><span class="label label-primary">上架任务</span></#if>
                                    <#if task.type == 2><span class="label label-success">下架任务</span></#if></td>
                                <td>
                                    <#if task.status == 0><span class="label label-inverse">失效</span></#if>
                                    <#if task.status == 1><span class="label label-inverse">未执行</span></#if>
                                    <#if task.status == 2><span class="label label-success">完成</span></#if>
                                    <#if task.status == 3><span class="label label-danger">异常(执行一半)</span></#if>
                                    <#if task.status == 4><span class="label label-success">已执行</span></#if>
                                </td>
                                <td>${task.hopeExecutorDate}</td>
                                <td>${task.completeTime}</td>
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
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/market/marketErrorTasks.do?marketId=${marketId?c}&happenDate=${happenDate}" />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>

        <script type="text/javascript">

            $(document).ready(function () {

                $("#backBtn").on('click', function () {
                    open({url: "${base}/market/marketErrorTasks.do?marketId=${marketId?c}"});
                });

            });

        </script>