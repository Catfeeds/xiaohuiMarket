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
                        <h4 class="page-title"><b>仓库列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/purchase/warehousePage.do">
                    <div class="form-group m-l-15">
                        <label>仓库名称：</label>
                        <select id="houseSelect" class="form-control" name="warehouseName">
                            <option value="">所有仓库</option>
                            <#if warehouseItem?exists >
                                <#list warehouseItem as house>
                                    <option value="${house.warehouseName}">${house.warehouseName}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <label>状态：</label>
                        <select id="status" class="form-control" name="status">
                            <option value="-1">全部</option>
                            <option value="0">停用</option>
                            <option value="1">正常</option>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i> 搜索</button>
                    </div>
                </form>
            </div>

            <button type="button" id="addHouseBtn" class="btn btn-primary pull-right" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-plus"></i> 新建仓库</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>仓库编码</th>
                            <th>名称</th>
                            <th>地址</th>
                            <#--<th>仓管员/位</th>-->
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="houseInfoTable">
                        <#if (warehouses?size > 0)>
                            <#list warehouses as house>
                            <tr id="tr_${house_index}">
                                <td>${house.warehouseCode}</td>
                                <td>${house.warehouseName}</td>
                                <td>${house.address}</td>
                                <#--<td>${house.userCount}</td>-->
                                <#if house.status=1>
                                    <td><b>正常</b></td>
                                    <td>
                                        <#--<button id="addBtn" type="button" class="btn btn-primary btn-sm" data_id="${house.id}">添加仓管</button>-->
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${house.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${house.id}">编辑</button>
                                        <button id="stopBtn" type="button" class="btn btn-danger btn-sm" data_id="${house.id}" status="${house.status}">停用</button>
                                    </td>
                                <#else>
                                    <td class="text-danger"><b>停用</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${house.id}">查看</button>
                                        <button id="startBtn" type="button" class="btn btn-primary btn-sm" data_id="${house.id}" status="${house.status}">启用</button>
                                    </td>
                                </#if>
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
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/purchase/warehousePage.do?warehouseName=${warehouseName}&status=${status}"/>
                </div>
            </div>
            <!--/分页-->
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
         //取url参数给表单赋值
        function GetQueryString(name) {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }

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
        //添加仓库
        $("#addHouseBtn").on('click',function () {
            location.href = "${base}/purchase/warehouseAdd.do";
        });

        //操作
    <#if (warehouses?size > 0)>
        $("#houseInfoTable").find('button[id=addBtn]').each(function () {//添加仓管员
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/purchase/warehouseUserAdd.do?id="+$(that).attr("data_id")});
            });
        });
        $("#houseInfoTable").find('button[id=lookBtn]').each(function () {//查看
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/purchase/warehouseDetail.do?id="+$(that).attr("data_id")});
            });
        });
        $("#houseInfoTable").find('button[id=editBtn]').each(function () {//编辑
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/purchase/warehouseEdit.do?id="+$(that).attr("data_id")});
            });
        });
        $("#houseInfoTable").find('button[id=stopBtn]').each(function () {//停用
            var that = this;
            $(this).on('click', function () {
                $("#stopModal").modal('show');
                $("#stopOkBtn").on('click',function () {
                    var txt = $("#stopRemark").val();
                    $.post("${base}/purchase/updateWarehouseStatus.do?id=" +$(that).attr("data_id")+ "&status=0&stopRemark=" +txt, function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            $("#disableModal").modal('hide');
                            swal("提示", "更新成功", "success");
                            setTimeout(function(){location.reload();},1000);
                        } else {
                            swal("提示", data.msg, "error");
                        }
                    }, "json");
                })
            });
        });
        $("#houseInfoTable").find('button[id=startBtn]').each(function () {//启用
            var that = this;
            $(this).on('click', function () {
                $.post("${base}/purchase/updateWarehouseStatus.do?id=" +$(that).attr("data_id")+ "&status=1", function(data) {
                    //重新刷新
                    if(data.code == "0") {
                        swal("提示", "更新成功", "success");
                        setTimeout(function(){location.reload();},1000);
                    } else {
                        swal("提示", data.msg, "error");
                    }
                }, "json");
            });
        });
    </#if>
    });
</script>

<!--停用弹窗-->
<div class="modal fade" id="stopModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                	<div class="form-group">
                        <label class="col-md-4 control-label">请说明停用仓库的原因：</label>                    
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <textarea class="form-control" rows="6" id="stopRemark"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="stopNoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="stopOkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>