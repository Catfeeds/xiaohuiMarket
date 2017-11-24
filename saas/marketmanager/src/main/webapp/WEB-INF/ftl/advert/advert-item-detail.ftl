<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">广告端管理</a></li>
                            <li class="active"><a href="#">广告详情</a></li>
                        </ol>
                        <h4 class="page-title "><b>广告详情</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" id="advertReturnBtn" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <div>
                                <ol class="breadcrumb pull-right">
                                    <#--<li><a href="#" data-toggle="modal" data-target="#editButton">编辑</a></li>-->
                                    <#--<li><a href="#" data-toggle="modal" data-target="#deleteButton">删除</a></li>-->
                                </ol>
                                <h5 class="page-title" style="padding-top: 20px"><b>广告信息</b></h5>
                                <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                                <div class="table-responsive advert_detail_table">
                                    <table class="table table-bordered">
                                        <#if advert?exists>
                                            <tbody>
                                            <tr>
                                                <td style="background-color: #f9f9f9;width: 20%">广告标题</td>
                                                <td>${advert.title}</td>
                                                <#--<td style="background-color: #f9f9f9">广告状态</td>-->
                                                <#--<td><#if advert.isDelete=0>未删除<#else>已删除</#if></td>-->
                                                <td style="background-color: #f9f9f9;width: 20%">广告时长</td>
                                                <td>${advert.timeSize}</td>
                                            </tr>
                                            <tr>
                                                <td style="background-color: #f9f9f9">创建时间</td>
                                                <td>${advert.createTime}</td>
                                                <td style="background-color: #f9f9f9;width: 20%">广告备注</td>
                                                <td style="width: 40%"><#if advert.remark!=''>${advert.remark}<#else>无</#if></td>
                                            </tr>
                                        </#if>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <div>
                                <h5 class="page-title" style="padding-top: 20px"><b>门店信息</b></h5>
                                <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                                <div class="table-responsive advert_detail_table">
                                    <table class="table table-bordered">
                                        <thead>
                                        <tr>
                                            <th style="background-color: #f9f9f9">门店</th>
                                            <th style="background-color: #f9f9f9">屏幕编码</th>
                                            <th style="background-color: #f9f9f9">屏幕分辨率</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <#if (screens?size > 0)>
                                            <#list screens as screen>
                                            <tr>
                                                <td>${screen.marketName}</td>
                                                <td>${screen.code}</td>
                                                <td>${screen.screenSize}</td>
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
</div>

<div class="modal fade" id="editButton" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">编辑广告</h4>
            </div>
            <div class="modal-body">
                <div class="modalAdvertStyle">
                    <form class="form-inline">
                        <div class="form-group" style="width: 100%">
                            <label for="modalAdvertTitle">广告标题：</label>
                            <input type="text" style="width: 80%" class="form-control" id="modalAdvertTitle" placeholder="输入广告标题"/>
                        </div>
                    </form>
                    <form class="form-inline">
                        <div class="form-group">
                            <label for="modalAdvertTitle">广告时长：</label>
                            <input type="text" class="form-control" id="modalAdvertTitle" placeholder="输入广告时长"/>&nbsp;&nbsp;&nbsp;S
                        </div>
                    </form>
                    <form class="form-inline">
                        <div class="form-group" style="width: 100%">
                            <label for="modalAdvertTitle">广告备注：</label>
                            <input type="text" style="width: 80%" class="form-control" id="modalAdvertTitle" placeholder="输入广告备注"/>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteButton" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">删除广告</h4>
            </div>
            <div class="modal-body">
                <p style="text-align: center">确定要删除该广告吗？</p>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>