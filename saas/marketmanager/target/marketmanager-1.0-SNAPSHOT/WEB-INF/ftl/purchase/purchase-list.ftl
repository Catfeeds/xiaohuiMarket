<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">采购列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>采购列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/purchase/purchasePage.do">
                    <div class="form-group m-l-15">
                        <label>仓库名称：</label>
                        <select id="houseSelect" class="form-control" name="warehouseCode">
                            <option value="">所有仓库</option>
                            <#if warehouseItem?exists >
                                <#list warehouseItem as warehouse>
                                    <option value="${warehouse.warehouseCode}">${warehouse.warehouseName}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <label>供应商名称：</label>
                        <input type="text" class="form-control" id="supplierName" placeholder="输入供应商名称" name="supplierName">
                    </div>
                    <div class="form-group m-l-15">
                        <label>入库状态：</label>
                        <select id="status" class="form-control" name="status">
                            <option value="-1">全部</option>
                            <option value="0">未提交</option>
                            <option value="1">未入库</option>
                            <option value="2">入库异常</option>
                            <option value="3">完成入库</option>
                            <option value="4">强制完成入库</option>
                            <option value="3">部分入库</option>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i> 搜索</button>
                    </div>
                </form>
            </div>

            <button type="button" id="addPurchaseBtn" class="btn btn-primary pull-right" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-plus"></i> 新建采购单</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>仓库名称</th>
                            <th>供应商名称</th>
                            <th>创建时间</th>
                            <th>入库状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="purchaseInfoTable">
                        <#if (purchases?size > 0)>
                            <#list purchases as purchase>
                            <tr id="tr_${purchase_index}">
                                <td>${purchase.id}</td>
                                <td>${purchase.warehouse_name}</td>
                                <td>${purchase.supplier_name}</td>
                                <td>${purchase.create_time}</td>
                                <#if purchase.status=0>
                                    <td class="text-muted"><b>未提交</b></td>
                                    <td>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">编辑</button>
                                        <button id="deleBtn" type="button" class="btn btn-danger btn-sm" data_id="${purchase.id}">删除</button>
                                    </td>
                                <#elseif purchase.status=1>
                                    <td class="text-default"><b>未入库</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">编辑</button>
                                    </td>
                                <#elseif purchase.status=2>
                                    <td class="text-danger"><b>入库异常</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">编辑</button>
                                    </td>
                                <#elseif purchase.status=3>
                                    <td class="text-success"><b>完成入库</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">查看</button>
                                    </td>
                                <#elseif purchase.status=4>
                                    <td class="text-warning"><b>强制完成入库</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">查看</button>
                                    </td>
                                <#elseif purchase.status=5>
                                    <td class="text-info"><b>部分入库</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${purchase.id}">编辑</button>
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
            <#if (purchases?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/purchase/purchasePage.do?supplierName=${supplierName}&warehouseCode=${warehouseCode}&status=${status}"/>
                </div>
            </div>
            </#if>
            <!--/分页-->
        </div>
    </div>
</div>
<script type="text/javascript">
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

        //新建采购单
        $("#addPurchaseBtn").on('click',function () {
            location.href = "${base}/purchase/purchaseAdd.do";
        });

        //查看,编辑
    <#if (purchases?size > 0)>
        $("#purchaseInfoTable").find('button[id=lookBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/purchase/purchaseDetail.do?id="+$(that).attr("data_id")+"&purchaseID="+$(that).attr("data_id")});
            });
        });
        $("#purchaseInfoTable").find('button[id=editBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/purchase/purchaseEdit.do?id="+$(that).attr("data_id")+"&purchaseID="+$(that).attr("data_id")});
            });
        });
        $("#purchaseInfoTable").find('button[id=deleBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                $("#deleModal").modal('show');
                $("#deleOkBtn").on('click',function () {
                    $.post("${base}/purchase/deletePurchase.do?id=" +$(that).attr("data_id"), function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            $("#deleModal").modal('hide');
                            swal("提示", "删除成功", "success");
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

<!--删除弹窗-->
<div class="modal fade" id="deleModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <p style="text-align: center">确定要删除该条记录吗？</p>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="deleNoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="deleOkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>