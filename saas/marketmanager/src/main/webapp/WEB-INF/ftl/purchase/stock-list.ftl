<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">
<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>
<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">商品库存列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>商品库存列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/purchase/commodityStoresPage.do">
                    <div class="form-group m-l-15">
                        <label>仓库名称：</label>
                        <select id="houseSelect" class="form-control" name="warehouseCode">
                            <option value="">所有仓库</option>
                        <#if warehouseItem?exists >
                            <#list warehouseItem as warehouse>
                                <option value="${warehouse.warehouseCode}" <#if warehouseCode?exists && warehouseCode == warehouse.warehouseCode>selected</#if>>
                                    ${warehouse.warehouseName}
                                </option>
                            </#list>
                        </#if>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <label>商品名称：</label>
                        <input type="text" class="form-control" id="goodsName" placeholder="输入商品名称" name="itemName" value="${itemName}">
                    </div>
                    <div class="form-group m-l-15">
                        <label>条形码：</label>
                        <input type="text" class="form-control" id="goodsCode" placeholder="输入商品条形码" name="barcode" value="${barcode}">
                    </div>
                    <div class="form-group m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i> 搜索</button>
                    </div>
                </form>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>仓库名称</th>
                            <th>商品分类</th>
                            <th>商品名称</th>
                            <th>条形码</th>
                            <th>库存数量</th>
                            <th>库存预警</th>
                            <th>更新时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="stockInfoTable">
                        <#if (stocks?size > 0)>
                            <#list stocks as stock>
                            <tr id="tr_${stock_index}">
                                <td>${stock.id}</td>
                                <td>${stock.warehouseName}</td>
                                <td>${stock.itemTypeName}</td>
                                <td>${stock.itemName}</td>
                                <td>${stock.barcode}</td>
                                <#if (stock.warnNumber > stock.storesNumber)>
                                    <td class="text-danger"><b>${stock.storesNumber?c}</b></td>
                                <#else>
                                    <td>${stock.storesNumber?c}</td>
                                </#if>
                                <td>${stock.warnNumber?c}</td>
                                <td><#if stock.updateTime??>${stock.updateTime}<#else>--</#if></td>
                                <td>
                                    <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${stock.id}" data_no="${stock.warnNumber}" onclick="update(this)">编辑</button>
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
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/purchase/commodityStoresPage.do?warehouseCode=${warehouseCode}&itemName=${itemName}&barcode=${barcode}"/>
                </div>
            </div>
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
    function update(obj){
        console.log(obj);
        var xg=$(obj).html();
        var $tr = $(obj).parent().parent();
        console.log($tr);
        var $td = $tr.find("td:eq(6)");
        var tdTxt = $td.text();
        if(xg=='编辑'){
            $($td).html("<input type='text' style='border: 1px solid #e69900' name='editname' class='form-control' value="+tdTxt+" >");
            var result=$td.children().val();//对input取值
            $td.children().val("").focus().val(result);//清空值获焦再赋值
            $(obj).addClass('btn-warning');
            $(obj).removeClass('btn-primary');
            $(obj).html('保存');
        }else if(xg=='保存'){
            var inputTxt  = $td.find('input').val();
            $.post("${base}/purchase/updateCommodityStores.do?id="+$(obj).attr("data_id")+"&warnNumber="+inputTxt , function (data) {
                //重新刷新
                if (data.code == "0") {
                    swal("提示", "保存成功", "success");
                    setTimeout(function () {location.reload()}, 1000);
                } else {
                    swal("提示", data.msg, "error");
                }
            }, "json");
            $(obj).addClass('btn-primary');
            $(obj).removeClass('btn-warning');
            $(obj).html('编辑');
        }
    }
    $(document).ready(function () {
        $("select").select2();
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
        //弹窗保存
    <#--<#if (stocks?size > 0)>-->
    <#--$("#stockInfoTable").find('button[id=editBtn]').each(function () {-->
    <#--var that = this;-->
    <#--$(this).on('click', function () {-->
    <#--var txt = $(this).attr("data_no");-->
    <#--$("#editModal").modal('show');-->
    <#--$("#editCount").val(txt);-->
    <#--$("#yesBtn").on('click',function () {-->
    <#--var count = $("#editCount").val();-->
    <#--$.post("${base}/purchase/updateCommodityStores.do?id="+$(that).attr("data_id")+"&warnNumber="+count , function (data) {-->
    <#--//重新刷新-->
    <#--console.log(data);-->
    <#--if (data.code == "0") {-->
    <#--swal("提示", "保存成功", "success");-->
    <#--setTimeout(function () {location.reload()}, 1000);-->
    <#--} else {-->
    <#--swal("提示", data.msg, "error");-->
    <#--}-->
    <#--}, "json");-->
    <#--});-->
    <#--});-->
    <#--});-->
    <#--</#if>-->
    });
</script>
<!--编辑弹窗-->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">编辑库存预警</h4>
            </div>
            <div class="modal-body">
                <div class="modalAdvertStyle">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-md-4 control-label">库存预警：</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" id="editCount"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="noBtn" type="Button" class="btn btn-primary" style="padding:10px 30px" data-dismiss="modal">取消</button>
                <button id="yesBtn" type="Button" class="btn btn-primary" style="padding:10px 30px">确定</button>
            </div>
        </div>
    </div>
</div>