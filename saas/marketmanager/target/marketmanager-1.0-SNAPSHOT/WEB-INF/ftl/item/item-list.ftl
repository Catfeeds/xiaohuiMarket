<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<#--<script src="${base}/assets/pages/item-list.js" />-->

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
                            <li class="active"><a href="#">商品模板库</a></li>
                        </ol>
                        <h4 class="page-title"><b>商品模板库</b></h4>
                    </div>
                </div>
            </div>


                <div class="row small_page">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">
                            <button id="addBtn" type="button" class="btn waves-effect waves-light btn-primary "><i
                                    class="fa fa-pencil"></i> 添加商品模板
                            </button>
                            <div class="input-group">
                                <div class="input-group-btn">
                                    <button id="searchMenu" type="button" search_name="名称"
                                            class="btn btn-default waves-effect waves-light dropdown-toggle"
                                            data-toggle="dropdown">按名称搜索 <span class="caret"></span></button>
                                    <ul class="dropdown-menu" id="searchType">
                                        <li><a href="javascript:void(0)" search_name="名称" search_type="name">按名称搜索</a>
                                        </li>
                                        <li><a href="javascript:void(0)" search_name="自定义编码" search_type="define_code">按自定义编码搜索</a>
                                        </li>
                                        <li><a href="javascript:void(0)" search_name="条形码"
                                               search_type="barcode">按条形码搜索</a>
                                        </li>
                                    </ul>
                                </div>
                                <input type="text" id="searchKeyTxt"
                                       class="form-control" placeholder="按名称查">
                                <span class="input-group-btn">
                                    <button id="searchBtn" type="button"
                                            class="btn waves-effect waves-light btn-primary"><i
                                            class="fa fa-search"></i></button>
                                </span>
                            </div>
                            <#--<div class="form-group pull-right">-->
                                <#--<div class="btn-group">-->
                                    <#--<button type="button"-->
                                            <#--class="btn btn-default waves-effect waves-light dropdown-toggle"-->
                                            <#--data-toggle="dropdown" aria-expanded="false">导出 商品模板库 <span-->
                                            <#--class="caret"></span></button>-->
                                    <#--<ul class="dropdown-menu">-->
                                        <#--<li><a href="#" id="outExcelBtn">To Excel</a></li>-->
                                        <#--<li><a href="#" id="outPdfBtn">To PDF</a></li>-->
                                        <#--<li class="divider"></li>-->
                                        <#--<li><a href="#" id="outCopyBtn">To 剪贴板</a></li>-->
                                    <#--</ul>-->
                                <#--</div>-->
                            <#--</div>-->
                        </form>
                    </div>
            </div>

            <div class="row m-t-15 small_page">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>商品ID</th>
                            <th>商品名称</th>
                            <th>商品自定义编码</th>
                            <th>唯一条形码</th>
                            <th>商品类型</th>
                            <th>单位</th>
                            <th>成本价(元)</th>
                            <th>零售价(元)</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="itemListTable">

                        <#if (itemlist?size > 0)>
                            <#list itemlist as item>
                            <tr>
                                <td>${item.id?c}</td>
                                <td>${item.name}</td>
                                <td>${(item.defineCode)!}</td>
                                <td>${item.barcode}</td>
                                <td>${item.typeName}</td>
                                <td>${item.unitName}</td>
                                <td>${item.costPrice}</td>
                                <td>${item.defaultPrice}</td>
                                <td>${item.uploadTime}</td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${item.id?c}">编辑
                                    </button>
                                    <button id="seeBtn" type="button"
                                            class="btn waves-effect waves-light btn-info btn-sm"
                                            data_id="${item.id?c}">商品是否在店铺上架
                                    </button>
                                </td>
                            </tr>

                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="11" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>


            <div class="row small_page">
                <div class="col-sm-12">
                <#include "../common/paginate.ftl">
                    <@paginate pageItemCount=pageSize nowPage=pageIndex itemCount=count action="${base}/item/itemList.do?searchType=${searchType}&searchKey=${searchKey}" />
                </div>
            </div>

            <!-- end container -->
        </div>

        <script type="text/javascript">

            $(document).ready(function () {

                //默认按名称
                var searchTypeValue = "${searchType}";

                var _searchMenu = $("#searchMenu");
                var _searchKeyTxt = $("#searchKeyTxt");
                var _itemListTable = $("#itemListTable");

                //根据搜索类型，设置选中项
                if (searchTypeValue == null || searchTypeValue == "") {
                    searchTypeValue = "name";
                }

                if (searchTypeValue == "define_code") {
                    _searchMenu.html("按自定义编码搜索<span class=\"caret\"></span>");
                    _searchKeyTxt.attr("placeholder", "按自定义编码搜索");
                } else if (searchTypeValue == "barcode") {
                    _searchMenu.html("按条形码搜索<span class=\"caret\"></span>");
                    _searchKeyTxt.attr("placeholder", "按条形码搜索");
                } else {
                    searchTypeValue = "name";
                    _searchMenu.html("按名称搜索<span class=\"caret\"></span>");
                    _searchKeyTxt.attr("placeholder", "按名称搜索");
                }

                //设置搜索框内容
                _searchKeyTxt.val("${searchKey}");

                //搜索选择
                $("#searchType li a").on('click', function () {
                    var sName = $(this).attr("search_name");
                    var txt = "按" + sName + "搜索";
                    searchTypeValue = $(this).attr("search_type")
                    _searchMenu.html(txt + "<span class=\"caret\"></span>");
                    _searchMenu.attr("search_name", sName)
                    _searchKeyTxt.attr("placeholder", txt);
                });

                $("#searchBtn").on('click', function () {
                    //搜索
                    var sValue = _searchKeyTxt.val();
                    if (sValue == "") {
                        swal("请输入商品" + _searchMenu.attr("search_name") + "进行搜索!");
                        return;
                    }
                    if (containSpecial.test(sValue)) {
                        swal("包括了特殊符号，无法搜索!");
                        return;
                    }
                    open({url:"${base}/item/itemList.do?searchType=" + searchTypeValue + "&searchKey=" + sValue});
                    //location.href = "${base}/item/itemList.do?searchType=" + searchTypeValue + "&searchKey=" + sValue;
                });

                //添加新品
                $("#addBtn").on('click', function () {
                    location.href = "${base}/item/itemEdit.do";
                });

            <#if (itemlist?size > 0)>

                //单项编辑
                _itemListTable.find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/item/itemEdit.do?id=" + $(this).attr("data_id")});
                        //location.href = "${base}/item/itemEdit.do?id=" + $(this).attr("data_id");
                    });
                });

                //单项查看店铺分布
                _itemListTable.find('button[id=seeBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/market/marketItems.do?searchType=itemTemplateId&searchKey=" + $(this).attr("data_id")});
                        //location.href = "${base}/market/marketItems.do?searchType=itemTemplateId&searchKey=" + $(this).attr("data_id");
                    });
                });

            </#if>

            });
        </script>