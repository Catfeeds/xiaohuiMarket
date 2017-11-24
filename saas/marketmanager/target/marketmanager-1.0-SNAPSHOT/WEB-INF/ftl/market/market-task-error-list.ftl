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

                            <div class="form-group">
                                <label>选择店铺：</label>
                                <select id="sMarket" style="width:200px;">
                                    <option data_id="0" selected>选择店铺</option>
                                <#if markets?exists && (markets?size > 0)>
                                    <#list markets as market>
                                        <option data_v="${market.name}" data_id="${market.id?c}" <#if marketId == market.id>
                                                selected </#if>>
                                        ${market.name}
                                        </option>
                                    </#list>
                                </#if>
                                </select>
                            </div>

                            <#--<div class="form-group m-l-15">-->
                                <#--<label>选择日期：</label>-->
                                <#--<div class="input-group">-->
                                    <#--<input id="startTime" type="text" class="form-control" readonly>-->
                                    <#--<span class="input-group-addon bg-default"-->
                                          <#--onClick="jeDate({dateCell:'#startTime',isTime:false,format:'YYYY-MM-DD'})"><i-->
                                            <#--class="fa fa-calendar"></i></span>-->
                                <#--</div>-->
                            <#--</div>-->

                            <#--<button id="searchBtn" type="button"-->
                                    <#--class="btn waves-effect waves-light btn-primary" >搜索-->
                            <#--</button>-->
                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>执行者</th>
                            <th>期望上架数量</th>
                            <th>实际执行上架数量</th>
                            <th>期望上架(SKU数)</th>
                            <th>实际执行上架(SKU数)</th>
                            <th>期望执行日期</th>
                            <th>提交的时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="taskListTable">

                        <#if tasks?exists && (tasks?size > 0)>
                            <#list tasks as task>
                            <tr>
                                <td>${task.executorPassportName}</td>
                                <td>${task.hopeTotalItemQuantity?c}</td>
                                <td>${task.actualItemQuantity?c}</td>
                                <td>${task.hopeTotalBarcodeQuantity?c}</td>
                                <td>${task.actualBarcodeQuantity?c}</td>
                                <td>${task.happenDate}</td>
                                <td>${task.submitTime}</td>
                                <td>
                                    <button id="seeBtn" type="button"
                                            class="btn waves-effect waves-light btn-danger btn-sm"
                                            data_id="${task.happenDate}">查看任务
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

        <#if tasks?exists && (tasks?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/market/marketErrorTasks.do?marketId=${marketId?c}" />
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

                    open({url:"${base}/market/marketErrorTasks.do?marketId=" + data_id});
                    //location.href = "${base}/market/marketTasks.do?marketId=" + data_id;
                });

        <#if tasks?exists && (tasks?size > 0) >
            //单项编辑
            $("#taskListTable").find('button[id=seeBtn]').each(function () {
                $(this).on('click', function () {
                    var select_obj = _sMarket.find("option:selected");
                    var data_id = select_obj.attr("data_id");
                    var data_v = select_obj.attr("data_v");

                    var happenDate = $(this).attr("data_id");
                    var target_url = "${base}/market/marketErrorTasks.do?marketId=" + data_id + "&happenDate=" + happenDate + "&marketName=" + data_v;
                    open({url: target_url});
                });
            });
        </#if>
            });
        </script>