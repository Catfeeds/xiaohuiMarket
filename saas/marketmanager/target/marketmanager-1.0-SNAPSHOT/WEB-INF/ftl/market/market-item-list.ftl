<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>


<div class="modal fade" id="statusDialog" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="upItemTitle">商品上下架状态修改</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-10">
                        <p id="statusItemName"></p>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-10">
                        <select class="form-control" id="updateStatusSelect">
                            <option data_id="1">上架</option>
                            <option data_id="3">下架</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-12">
                        <button id="statusOkBtn" type="button" class="btn btn-primary"
                                data-dismiss="modal">确定
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


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
                            <li class="active"><a href="#">店铺商品列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>店铺商品列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <button id="addBtn" type="button" class="btn waves-effect waves-light btn-primary "><i
                                    class="fa fa-pencil"></i> 添加店铺商品
                            </button>

                            <#--<button id="pBtn" type="button"-->
                                    <#--class="btn btn-default waves-effect waves-light btn-primary ">批量上下架</button>-->

                            <div class="form-group">
                                <label>选择店铺：</label>
                                <select id="sMarket" style="width:200px;">
                                    <option data_id="0" selected>选择店铺</option>
                                <#if markets?exists && (markets?size > 0)>
                                    <#list markets as market>
                                        <option data_id="${market.id?c}" <#if marketId == market.id>
                                                selected </#if>>
                                        ${market.name}
                                        </option>
                                    </#list>
                                </#if>
                                </select>
                            </div>

                            <div class="input-group m-l-15">
                                <div class="input-group-btn">
                                    <button id="searchMenu" type="button"
                                            class="btn btn-default waves-effect waves-light dropdown-toggle"
                                            data-toggle="dropdown">按商品模板名称搜索 <span class="caret"></span></button>
                                    <ul class="dropdown-menu" id="searchType">
                                        <li><a href="javascript:void(0)" search_name="商品模板名称" search_type="itemTemplateName">按商品模板名称搜索</a>
                                        </li>
                                        <li><a href="javascript:void(0)" search_name="商品模板ID" search_type="itemTemplateId">按商品模板ID搜索</a>
                                        </li>
                                    </ul>
                                </div>
                                <input type="text" id="searchKeyTxt"
                                       class="form-control" placeholder="按商品模板ID搜索">
                                <span class="input-group-btn">
                                    <button id="searchBtn" type="button"
                                            class="btn waves-effect waves-light btn-primary"><i
                                            class="fa fa-search"></i></button>
                                </span>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>商品模板ID</th>
                            <th>商品名称</th>
                            <#if marketId == 0>
                                <th>所在店铺ID</th>
                            </#if>
                            <th>成本价(元)</th>
                            <th>一般销售价(元)</th>
                            <th>一般市场售价(元)</th>
                            <th>促销价(元)</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="itemsTable">

                        <#if items?exists && (items?size > 0)>
                            <#list items as item >
                            <tr>
                                <td>${item.id?c}</td>
                                <td>${item.itemTemplateId?c}</td>
                                <td>${item.defineName}</td>
                                <#if marketId == 0>
                                    <td><a href="${base}/market/marketItems.do?marketId=${item.ownerId?c}" alt="点击查看该店铺的商品列表">${item.ownerId?c}</a></td>
                                </#if>
                                <td>${item.costPrice}</td>
                                <td>${item.sellPrice}</td>
                                <td>${item.marketPrice}</td>
                                <td>${item.discountPrice}</td>
                                <td>
                                    <#if item.status == 2>
                                        <button id="statusBtn" type="button"
                                                class="btn waves-effect waves-light btn-danger btn-xs"
                                                data_id="${item.id?c}" data_v="${item.defineName}">失效
                                        </button>
                                    </#if>
                                    <#if item.status == 1>
                                        <button id="statusBtn" type="button"
                                                class="btn waves-effect waves-light btn-success btn-xs"
                                                data_id="${item.id?c}" data_v="${item.defineName}">可售
                                        </button>
                                    </#if>
                                    <#if item.status == 0>
                                        <button id="statusBtn" type="button"
                                                class="btn waves-effect waves-light btn-inverse btn-xs"
                                                data_id="${item.id?c}" data_v="${item.defineName}">未上架
                                        </button>
                                    </#if>
                                </td>
                                <td>
                                    <button id="editBtn" type="button" data_id="${item.id?c}"
                                            class="btn waves-effect waves-light btn-warning btn-sm">编辑
                                    </button>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr id="emptyTr">
                            <td colSpan="<#if marketId == 0>10<#else>9</#if>" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>

                        </tbody>
                    </table>


                </div>
            </div>

        <#if items?exists && (items?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/market/marketItems.do?marketId=${marketId?c}&searchType=${searchType}&searchKey=${searchKey}" />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>


        <script type="text/javascript">

            $(document).ready(function () {

                var s_marketId = ${marketId?c};
                var s_searchType = "${searchType}";
                //默认按名称
                var _searchMenu = $("#searchMenu");
                var _searchKeyTxt = $("#searchKeyTxt");

                var _sMarket = $("#sMarket");

                //根据搜索类型，设置选中项
                if (s_searchType == null || s_searchType == "") {
                    s_searchType = "name";
                }

                if (s_searchType == "itemTemplateName") {
                    _searchMenu.html("按商品模板名称搜索<span class=\"caret\"></span>");
                    _searchKeyTxt.attr("placeholder", "按商品模板名称搜索");
                } else {
                    s_searchType = "itemTemplateId";
                    _searchMenu.html("按商品模板ID搜索<span class=\"caret\"></span>");
                    _searchKeyTxt.attr("placeholder", "按商品模板ID搜索");
                }

                //设置搜索框内容
                _searchKeyTxt.val("${searchKey}");

                //搜索选择
                $("#searchType li a").on('click', function () {
                    var sName = $(this).attr("search_name");
                    var txt = "按" + sName + "搜索";
                    s_searchType = $(this).attr("search_type");
                    _searchMenu.html(txt + "<span class=\"caret\"></span>");
                    _searchMenu.attr("search_name", sName);
                    _searchKeyTxt.attr("placeholder", txt);
                });

                _sMarket.select2();
                _sMarket.change(function () {
                    var s_searchKey = _searchKeyTxt.val();
                    var select_obj = _sMarket.find("option:selected");
                    s_marketId = select_obj.attr("data_id");
                    s_searchType = _searchMenu.attr("search_type");

                    open({url:"${base}/market/marketItems.do?marketId=" + s_marketId+"&searchType="+s_searchType+"&searchKey="+s_searchKey});
                    //location.href = "${base}/market/marketItems.do?marketId=" + s_marketId+"&searchType="+s_searchType+"&searchKey="+s_searchKey;
                });

                //添加新品
                $("#addBtn").on('click', function () {

                    var select_obj = _sMarket.find("option:selected");
                    s_marketId = select_obj.attr("data_id");

                    open({url:"${base}/market/marketItemAdd.do?marketId="+s_marketId});
                    //location.href = "${base}/market/marketItemAdd.do?marketId="+s_marketId;
                });

                //搜索
                $("#searchBtn").on('click', function () {
                    var s_searchKey = _searchKeyTxt.val();
                    if (containSpecial.test(s_searchKey)) {
                        swal("包括了特殊符号，无法搜索!");
                        return;
                    }

                    var select_obj = _sMarket.find("option:selected");
                    s_marketId = select_obj.attr("data_id");

                    open({url:"${base}/market/marketItems.do?marketId=" + s_marketId+"&searchType="+s_searchType+"&searchKey="+s_searchKey});
                });

                //状态修改确定
                $("#statusOkBtn").on('click', function () {

                    //获得当前想修改的值
                    var _status_obj = $("#updateStatusSelect").find("option:selected");
                    var _status = _status_obj.attr("data_id");

                    tokenPost("${base}/market/marketItemUpdateStatus.do?itemId=" + $(this).attr("data_id") + "&status=" + _status, function (json) {

                        $("#statusDialog").modal('hide');

                        if (json.code != 0) {
                            swal(json.msg);
                        } else {
                            //改变
                            var s_searchKey_a = _searchKeyTxt.val();
                            var select_obj_a = _sMarket.find("option:selected");
                            s_marketId = select_obj_a.attr("data_id");

                            open({url:"${base}/market/marketItems.do?marketId=" + s_marketId+"&searchType="+s_searchType+"&searchKey="+s_searchKey_a+"&pageSize=${pageSize}&pageIndex=${pageIndex}"});
                        }
                    }, "json");
                });

                <#if items?exists && (items?size > 0) >
                    //单项编辑
                    $("#itemsTable").find('button[id=editBtn]').each(function () {
                        $(this).on('click', function () {
                            var s_searchKey = _searchKeyTxt.val();
                            open({url:"${base}/market/marketItemEdit.do?id=" + $(this).attr("data_id")
                                +"&searchType="+s_searchType+"&searchKey="+s_searchKey
                                +"&pageSize=${pageSize}&pageIndex=${pageIndex}"
                            });
                            //location.href = "${base}/market/marketItemEdit.do?marketId=" + s_marketId + "&id=" + $(this).attr("data_id");
                        });
                    });

                    //单项状态改变
                    $("#itemsTable").find('button[id=statusBtn]').each(function () {
                        $(this).on('click', function () {
                            $("#statusOkBtn").attr("data_s", $(this).attr("data_s"));
                            $("#statusOkBtn").attr("data_id", $(this).attr("data_id"));
                            $("#statusItemName").html($(this).attr("data_v"));
                            $("#statusItemName").attr("data_id", $(this).attr("data_id"));
                            $("#statusDialog").modal('show');
                        });
                    });
                </#if>
            });
        </script>