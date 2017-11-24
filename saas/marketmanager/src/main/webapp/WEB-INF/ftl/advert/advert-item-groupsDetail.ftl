<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">广告管理</a></li>
                            <li class="active"><a href="#">分组管理</a></li>
                        </ol>
                        <h4 class="page-title "><b>分组详情</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" id="returnBtn" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
            <div class="card-box">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>分组信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                <#if group?exists>
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">分组名称</td>
                                        <td>${group.groupName}</td>
                                        <td style="background-color: #f9f9f9">创建时间</td>
                                        <td>${group.createTime}</td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">备注</td>
                                        <td colspan="3"><#if group.remark!=''>${group.remark}<#else>无</#if></td>
                                    </tr>
                                    </tbody>
                                </#if>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>广告信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th style="background-color: #f9f9f9">广告ID</th>
                                        <th style="background-color: #f9f9f9">广告名称</th>
                                        <th style="background-color: #f9f9f9">创建时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#if (adverts?size > 0)>
                                        <#list adverts as advert>
                                        <tr>
                                            <td>${advert.id}</td>
                                            <td>${advert.title}</td>
                                            <td>${advert.createTime}</td>
                                        </tr>
                                        </#list>
                                    <#else>
                                    <tr>
                                        <td colSpan="11" height="100px">
                                            <p class="text-center" style="line-height: 100px">暂无任何数据</p>
                                        </td>
                                    </tr>
                                    </#if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
