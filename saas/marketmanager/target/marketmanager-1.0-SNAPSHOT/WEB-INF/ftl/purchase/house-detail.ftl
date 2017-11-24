<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">仓库列表</a></li>
                        </ol>
                        <h4 class="page-title "><b>仓库详情</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" id="returnBtn" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>基本信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <#if warehouse?exists>
                                        <tbody>
                                        <tr>
                                            <td style="background-color: #f9f9f9">仓库编码</td>
                                            <td>${warehouse.warehouseCode}</td>
                                            <td style="background-color: #f9f9f9">创建时间</td>
                                            <td>${warehouse.createTime}</td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">仓库名称</td>
                                            <td>${warehouse.warehouseName}</td>
                                            <td style="background-color: #f9f9f9">仓库地址</td>
                                            <td>${warehouse.address}</td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">状态</td>
                                            <td>
                                                <#if warehouse.status=1>正常<#else>停用</#if>
                                            </td>
                                            <#if warehouse.status=0>
                                                <td style="background-color: #f9f9f9">停用原因</td>
                                                <td><#if warehouse.stopRemark!=''>${warehouse.stopRemark}<#else>无</#if></td>
                                            </#if>
                                        </tr>
                                        </tbody>
                                    </#if>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-10">
                        <div class="advert_container">
                            <div  style="width: 50%">
                                <h5 class="page-title" style="padding-top: 20px"><b>对接信息</b></h5>
                                <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                                <div class="table-responsive advert_detail_table">
                                    <table class="table table-bordered">
                                        <thead>
                                        <tr>
                                            <td style="background-color: #f9f9f9;width: 30%">key</td>
                                            <td style="background-color: #f9f9f9;"><#if warehouse.wmsKEY!=''>${warehouse.wmsKEY}<#else>暂无对接信息</#if></td>
                                        </tr>
                                        </thead>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <#--<div class="row">-->
                    <#--<div class="col-sm-12">-->
                        <#--<div class="advert_container">-->
                            <#--<div>-->
                                <#--<h5 class="page-title" style="padding-top: 20px"><b>仓管员信息</b></h5>-->
                                <#--<hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>-->
                                <#--<div class="table-responsive advert_detail_table">-->
                                    <#--<table class="table table-bordered">-->
                                        <#--<thead>-->
                                        <#--<tr>-->
                                            <#--<th style="background-color: #f9f9f9">创建时间</th>-->
                                            <#--<th style="background-color: #f9f9f9">账号</th>-->
                                            <#--<th style="background-color: #f9f9f9">备注</th>-->
                                            <#--<th style="background-color: #f9f9f9">操作</th>-->
                                        <#--</tr>-->
                                        <#--</thead>-->
                                        <#--<tbody>-->
                                        <#--<#if (users?size > 0)>-->
                                            <#--<#list users as user>-->
                                            <#--<tr>-->
                                                <#--<td>${user.createTime}</td>-->
                                                <#--<td>${user.username}</td>-->
                                                <#--<td><#if user.remark!=''>${user.remark}<#else>无</#if></td>-->
                                                <#--<td><button id="resetBtn">重置密码</button>&nbsp;&nbsp;<button id="deleBtn">删除</button></td>-->
                                            <#--</tr>-->
                                            <#--</#list>-->
                                        <#--<#else>-->
                                            <#--<tr>-->
                                                <#--<td colSpan="11" height="100px">-->
                                                    <#--<p class="text-center" style="line-height: 100px">该仓库没有分配仓管员</p>-->
                                                <#--</td>-->
                                            <#--</tr>-->
                                        <#--</#if>-->
                                        <#--</tbody>-->
                                    <#--</table>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</div>-->
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    <#--$(document).ready(function () {-->
        <#--<#if (warehouse?exists)>-->
            <#--$("#resetBtn").on('click',function () {-->
                <#--$.post("${base}/purchase/resetWarehouseUserPwd.do?id=${warehouse.id}&password=", function (data) {//提交-->
                    <#--//重新刷新-->
                    <#--console.log(data);-->
                    <#--if (data.code == "0") {-->
                        <#--swal("提示", "更新成功", "success");-->
                        <#--setTimeout(function () {-->
                            <#--location.reload()-->
                        <#--}, 1000);-->
                    <#--} else {-->
                        <#--swal("提示", data.msg, "error");-->
                    <#--}-->
                <#--}, "json");-->
            <#--});-->
            <#--$("#deleBtn").on('click',function () {-->
                <#--$.post("${base}/purchase/deleteWarehouseUser.do?id=${warehouse.id}", function (data) {//提交-->
                    <#--//重新刷新-->
                    <#--console.log(data);-->
                    <#--if (data.code == "0") {-->
                        <#--swal("提示", "删除成功", "success");-->
                        <#--setTimeout(function () {-->
                            <#--location.reload()-->
                        <#--}, 1000);-->
                    <#--} else {-->
                        <#--swal("提示", data.msg, "error");-->
                    <#--}-->
                <#--}, "json");-->
            <#--});-->
        <#--</#if>-->
    <#--});-->
</script>
