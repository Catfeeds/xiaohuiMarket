<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">供应商列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>供应商列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/purchase/supplierPage.do">
                    <div class="form-group m-l-15">
                        <label>供应商名称：</label>
                        <input type="text" class="form-control" id="supplierName" placeholder="输入供应商名称" name="supplierName">
                    </div>
                    <div class="form-group m-l-15">
                        <label>供应商属性：</label>
                        <select id="supplierSelect" class="form-control" name="supplierType">
                            <option value="-1">所有供应商</option>
                            <option value="1">一级供应商</option>
                            <option value="2">二级供应商</option>
                            <option value="3">品牌供应商</option>
                            <option value="4">超市供应商</option>                      
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

            <button type="button" id="addSupplierBtn" class="btn btn-primary pull-right" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-plus"></i> 添加供应商</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>名称</th>
                            <th>属性</th>
                            <th>地址</th>
                            <th>创建时间</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="supplierInfoTable">
                        <#if (suppliers?size > 0)>
                            <#list suppliers as supplier>
                            <tr id="tr_${supplier_index}">
                                <td>${supplier.id}</td>
                                <td>${supplier.supplierName}</td>
                                <td>
                                    <#if supplier.supplierType=1>一级供应商
                                    <#elseif supplier.supplierType=2>二级供应商
                                    <#elseif supplier.supplierType=3>品牌供应商
                                    <#elseif supplier.supplierType=4>超市供应商
                                    </#if>
                                </td>
                                <td>${supplier.address}</td>
                                <td>${supplier.createTime}</td>
                                <#if supplier.status=1>
                                    <td><b>正常</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${supplier.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${supplier.id}">编辑</button>
                                        <button id="disableBtn" type="button" class="btn btn-danger btn-sm" data_id="${supplier.id}">停用</button>
                                    </td>
                                <#else>
                                    <td class="text-danger"><b>停用</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${supplier.id}">查看</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <button id="enableBtn" type="button" class="btn btn-primary btn-sm" data_id="${supplier.id}">启用</button>
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
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/purchase/supplierPage.do?supplierName=${supplierName}&supplierType=${supplierType}&status=${status}"/>
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
        //添加供应商
        $("#addSupplierBtn").on('click',function () {
            location.href = "${base}/purchase/supplierAdd.do";
        });

        //查看,编辑,启用,停用
        <#if (suppliers?size > 0)>
            $("#supplierInfoTable").find('button[id=lookBtn]').each(function () {
                var that = this;
                $(this).on('click', function () {
                    open({url:"${base}/purchase/supplierDetail.do?id="+$(that).attr("data_id")});
                });
            });
            $("#supplierInfoTable").find('button[id=editBtn]').each(function () {
                var that = this;
                $(this).on('click', function () {
                    open({url:"${base}/purchase/supplierEdit.do?id="+$(that).attr("data_id")});
                });
            });
            $("#supplierInfoTable").find('button[id=enableBtn]').each(function () {
                var that = this;
                $(this).on('click', function () {
                    $.post("${base}/purchase/updateSupplierStatus.do?id=" +$(that).attr("data_id")+ "&status=1", function(data) {
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
            $("#supplierInfoTable").find('button[id=disableBtn]').each(function () {
                var that = this;
                $(this).on('click', function () {
                    $("#disableModal").modal('show');
                    $("#OkBtn").on('click',function () {
                        var txt = $("#textRemark").val();
                        $.post("${base}/purchase/updateSupplierStatus.do?id=" +$(that).attr("data_id")+ "&status=0&stopRemark=" +txt, function(data) {
                            //重新刷新
                            if(data.code == "0") {
                                $("#disableModal").modal('hide');
                                swal("提示", "更新成功", "success");
                                setTimeout(function(){location.reload();},1000);
                            } else {
                                swal("提示", data.msg, "error");
                            }
                        }, "json");
                    });
                });
            });
        </#if>
    });
</script>

<!--停用弹窗-->
<div class="modal fade" id="disableModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                	<div class="form-group">
                        <label class="col-md-4 control-label">请说明停用供应商的原因：</label>                    
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <textarea class="form-control" rows="6" id="textRemark"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="NoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="OkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>
