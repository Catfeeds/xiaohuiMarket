<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">商品管理</a></li>
                            <li class="active"><a href="#">商品单位</a></li>
                        </ol>
                        <h4 class="page-title"><b>商品单位</b></h4>
                    </div>
                </div>
            </div>


            <div class="row small_page">
                <div class="col-sm-8">
                    <form class="form-inline">
                        <button id="addBtn" type="button" class="btn waves-effect waves-light btn-primary "><i
                                class="fa fa-pencil"></i>
                            添加商品单位
                        </button>
                        <div class="input-group">
                            <input type="text" id="searchKeyTxt"
                                   class="form-control" placeholder="输入名称搜索">
                            <span class="input-group-btn">
                                                <button id="searchBtn" type="button"
                                                        class="btn waves-effect waves-light btn-default"> <i
                                                        class="fa fa-search"></i> 搜索 </button>
                                            </span>
                        </div>
                        <div class="form-group pull-right">
                            <div class="btn-group">
                                <button type="button"
                                        class="btn btn-primary waves-effect waves-light dropdown-toggle"
                                        data-toggle="dropdown" aria-expanded="false">导出 商品单位 <span
                                        class="caret"></span></button>
                                <ul class="dropdown-menu">
                                    <li><a href="#" id="outExcelBtn">To Excel</a></li>
                                    <li><a href="#" id="outPdfBtn">To PDF</a></li>
                                    <li class="divider"></li>
                                    <li><a href="#" id="outCopyBtn">To 剪贴板</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-sm-4"></div>
            </div>

            <div class="row small_page">
                <div class="col-sm-8">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>商品单位名称</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="itemUnitTable">
                        <#if (itemUnits?size > 0)>
                            <#list itemUnits as unit>
                            <tr>
                                <td>${unit.id?c}</td>
                                <td>${unit.title}</td>
                                <td>
                                    <#if unit.status == 1>
                                        <span class="label label-success">启用</span>
                                    <#else>
                                        <span class="label label-default">不启用</span>
                                    </#if>
                                </td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${unit.id?c}">编辑
                                    </button>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="4" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>

        <#if (itemUnits?size > 0)>
            <div class="row small_page">
                <div class="col-sm-8">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/item/itemUnits.do?searchKey=${searchKey}" />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>

        <script type="text/javascript">

            $(document).ready(function () {

                //默认按名称
                var _searchKeyTxt = $("#searchKeyTxt");

                //设置搜索框内容
                _searchKeyTxt.val("${searchKey}");

                $("#searchBtn").on('click', function () {
                    //搜索
                    var sValue = _searchKeyTxt.val();
                    if (containSpecial.test(sValue)) {
                        swal("包括了特殊符号，无法搜索!");
                        return;
                    }
                    open({url:"${base}/item/itemUnits.do?searchKey=" + sValue});
                    //location.href = "${base}/item/itemUnits.do?searchKey=" + sValue;
                });

                //添加新品
                $("#addBtn").on('click', function () {
                    open({url:"${base}/item/itemUnitEdit.do"});
                    //location.href = "${base}/item/itemUnitEdit.do";
                });

            <#if (itemUnits?size > 0)>

                //单项编辑
                $("#itemUnitTable").find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/item/itemUnitEdit.do?id=" + $(this).attr("data_id")});
                        //location.href = "${base}/item/itemUnitEdit.do?id=" + $(this).attr("data_id");
                    });
                });

            </#if>

            });
        </script>