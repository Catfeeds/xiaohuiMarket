<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">广告端管理</a></li>
                            <li class="active"><a href="#">广告管理</a></li>
                        </ol>
                        <h4 class="page-title"><b>分组管理</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/advert/groupPage.do">
                    <div class="form-group m-l-15">
                        <label>分组名称：</label>
                        <input type="text" class="form-control" id="groupsName" placeholder="输入广告分组名称" name="groupName">
                    </div>
                    <div class="form-group m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchButton"><i class="fa fa-search"></i> 搜索</button>
                    </div>
                </form>
            </div>

            <button type="button" class="btn btn-primary pull-right" data-toggle="modal" data-target="#addModal" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-plus"></i> 新增分组</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>分组名称</th>
                            <th>创建时间</th>
                        <#--<th>备注</th>-->
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="groupInfoTable">
                        <#if (groups?size > 0)>
                            <#list groups as group>
                            <tr id="tr_${group_index}">
                                <td>${group.id}</td>
                                <td>${group.groupName}</td>
                                <td>${group.createTime}</td>
                            <#--<td>${group.remark}</td>-->
                                <td>
                                    <button id="lookBtn" type="button" class="btn btn-primary btn-sm"
                                            data_id="${group.id}">查看
                                    </button>
                                    <button id="editBtn" type="button" class="btn btn-primary btn-sm"
                                            data_id="${group.id}">编辑
                                    </button>
                                    <button id="deleBtn" type="button" data-target="#deleModel" class="btn btn-danger btn-sm"
                                            data_id="${group.id}">删除
                                    </button>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="11" height="200px">
                                <p class="text-center" style="line-height: 200px">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!--分页-->
            <div class="row small_page">
                <div class="col-sm-12">
                <#include "../common/paginate.ftl">
                    <#--<@paginate nowPage=pageIndex itemCount=count action="${base}/advert/groupPage.do?groupName=${groupName}"/>-->
                </div>
            </div>
            <!--/分页-->
        </div>
    </div>
</div>
<script>
    function checkInput (obj){//检查表单是否有空项，空格验证方法待加
        var val = obj.val();
        if(val == "") {
            return false;
        } else {
            return true;
        }
    }
    //取url参数给表单赋值
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    $(document).ready(function () {
        //鼠标经过效果
        $("tr[id^='tr_']").hover(
                function(){ // onmouseover
                    $(this).css("background-color", "#FFFFBF"); // 设置背景颜色
                },
                function(){ // onmouseout
                    // 代表当前行对应的checkbox没有选中
                    if (!$(this.id.replace("tr_", "")).attr("checked")){
                        $(this).css("background-color", "#FFFFFF"); // 还原背景颜色
                    }
                }
        );
        //新增分组
        $("#sumitBtn").on('click', function () {
            $(this).button('loading').delay(500).queue(function() {//阻止二次提交
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var v1 = $("#modalAdvertTitle").val();
                var v2 = $("#modalAdvertRemark").val();
                var input1 = checkInput($("#modalAdvertTitle"));
                if (input1 == true) {
                    $.post("${base}/advert/saveGroup.do?groupName=" + v1 + "&remark=" + v2, function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            $("#addModal").modal('hide');
                            swal("提示", "添加成功", "success");
                            setTimeout(function(){location.reload();},1000);
                        } else {
                            swal("提示", "添加失败", "error");
                        }
                    }, "json");
                } else {
                    swal("提示", "请检查表单是否有漏填项！", "info");
                }
            });
        });
        //删除
    <#if (groups?size > 0)>
        $("#groupInfoTable").find('button[id=deleBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                console.log($(that).attr("data_id"));
                $("#deleModel").modal('show');
                $("#deleOkBtn").on('click',function () {
                    $.post("${base}/advert/delGroup.do?groupID=" + $(that).attr("data_id"), function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            $("#deleModel").modal('hide');
                            swal("提示", "删除成功", "success");
                            setTimeout(function(){location.reload();},1000);
                        } else {
                            swal("提示", "删除失败", "error");
                        }
                    }, "json");
                });
            });
        });
    </#if>
        //编辑
    <#if (groups?size > 0)>
        $("#groupInfoTable").find('button[id=editBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/advert/goEditgroup.do?groupID="+$(that).attr("data_id")});
            });
        });
    </#if>
        //查看
    <#if (groups?size > 0)>
        $("#groupInfoTable").find('button[id=lookBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/advert/groupDetail.do?groupID="+$(that).attr("data_id")});
            });
        });
    </#if>
    });
</script>
<!--新增弹窗-->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">新增分组</h4>
            </div>
            <div class="modal-body" role="form">
                <div class="modalAdvertStyle">
                    <form class="form-horizontal" id="addGroupForm" enctype="multipart/form-data">
                        <div class="form-group">
                            <label class="col-md-4 control-label">分组名称：</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" id="modalAdvertTitle" placeholder="输入分组名称" name="groupName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">分组备注：</label>
                            <div class="col-md-6">
                                <textarea type="text" class="form-control" rows="3" id="modalAdvertRemark" placeholder="输入分组备注，默认空" name="remark"></textarea>
                            </div>
                        </div>
                        <div class="modal-footer" style="text-align: center">
                            <button type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" style="padding:10px 80px " id="sumitBtn">确定</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!--删除弹窗-->
<div class="modal fade" id="deleModel" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">删除分组</h4>
            </div>
            <div class="modal-body">
                <p style="text-align: center">确定要删除该分组吗？</p>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="deleNoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="deleOkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>