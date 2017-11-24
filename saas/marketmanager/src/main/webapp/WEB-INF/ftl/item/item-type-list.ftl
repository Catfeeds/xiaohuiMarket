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
                            <li class="active"><a href="#">商品分类</a></li>
                        </ol>
                        <h4 class="page-title"><b>商品分类</b></h4>
                    </div>
                </div>
            </div>

            <div class="row small_page">
                <div class="col-sm-8">
                    <form class="form-inline">
                        <button id="addBtn" type="button" class="btn waves-effect waves-light btn-primary "><i
                                class="fa fa-pencil"></i> 添加分类
                        </button>
                        <button id="sortBtn" type="button" class="btn waves-effect waves-light btn-primary "><i
                                class="fa fa-pencil">分类排序</i>
                        </button>

                        <div class="input-group">
                            <input type="text" id="searchKeyTxt"
                                   class="form-control" placeholder="输入分类名称搜索">
                            <span class="input-group-btn">
                            <button id="searchBtn" type="button" class="btn waves-effect waves-light btn-primary">
                                <i class="fa fa-search"></i> 搜索 </button>
                            </span>
                        </div>
                    </form>
                </div>
            </div>

            <div class="row small_page">
                <div class="col-sm-8">
                    <table id="itemTypeTable" class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>商品分类名称</th>
                            <th>分类级别</th>
                            <th>排序值</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        <!-- 这里显示的是当前所有二级的一级，应需求不需要显示 -->
                        <#if itemType?exists >
                        <tr>
                            <td colspan="4">${itemType.title}  的所有二级</td>
                            <td>
                                <button id="pEditBtn" type="button" data_id="${itemType.id?c}"
                                        class="btn waves-effect waves-light btn-warning btn-sm">编辑
                                </button>
                                <button id="pAddChildBtn" type="button" data_id="${itemType.id?c}"
                                        class="btn waves-effect waves-light btn-warning btn-sm">添加子类
                                </button>
                            </td>
                        </tr>
                        </#if>

                        <#if itemTypes?exists && (itemTypes?size > 0)>
                            <#list itemTypes as type >
                            <tr>
                                <td>${type.id?c}</td>
                                <td>${type.title}</td>
                                <td><#if type.parentId == 0><span class="label label-primary">一级分类</span><#else>
                                    <span class="label label-success">二级分类</span></#if></td>
                                <td>${type.sort}</td>
                                <td>
                                    <#if type.parentId == 0>
                                        <button id="editBtn" type="button" data_id="${type.id?c}"
                                                class="btn waves-effect waves-light btn-warning btn-sm">编辑
                                        </button>
                                        <button id="seeChildBtn" type="button" data_id="${type.id?c}"
                                                class="btn waves-effect waves-light btn-warning btn-sm">查看子类
                                        </button>
                                        <button id="addChildBtn" type="button" data_id="${type.id?c}"
                                                class="btn waves-effect waves-light btn-warning btn-sm">添加子类
                                        </button>
                                    <#else>
                                        <button id="editBtn" type="button" data_id="${type.id?c}"
                                                class="btn waves-effect waves-light btn-warning btn-sm">编辑
                                        </button>
                                    </#if>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="5" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>

        <#if (itemTypes?size > 0)>
            <div class="row small_page">
                <div class="col-sm-8">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/item/itemTypes.do?searchKey=${searchKey}" />
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
                    open({url:"${base}/item/itemTypes.do?searchKey=" + sValue});
                    //location.href = "${base}/item/itemTypes.do?searchKey=" + sValue;
                });

                //添加分类
                $("#addBtn").on('click', function () {
                    open({url:"${base}/item/itemTypeEdit.do"});
                    //location.href = "${base}/item/itemTypeEdit.do";
                });

                //添加分类
                $("#sortBtn").on('click', function () {
                    open({url:"${base}/item/itemTypeSort.do"});
                    //location.href = "${base}/item/itemTypeSort.do";
                });

            <#if itemType?exists >
                $("#pEditBtn").on('click', function () {
                    open({url:"${base}/item/itemTypeEdit.do?id=" + $(this).attr("data_id")});
                    //location.href = "${base}/item/itemTypeEdit.do?id=" + $(this).attr("data_id");
                });
                $("#pAddChildBtn").on('click', function () {
                    open({url:"${base}/item/itemTypeEdit.do?parentId=" + $(this).attr("data_id")});
                    //location.href = "${base}/item/itemTypeEdit.do?parentId=" + $(this).attr("data_id");
                });
            </#if>

            <#if (itemTypes?size > 0)>

                //单项编辑
                $("#itemTypeTable").find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/item/itemTypeEdit.do?id=" + $(this).attr("data_id")});
                        //location.href = "${base}/item/itemTypeEdit.do?id=" + $(this).attr("data_id");
                    });
                });

                //单项添加子类
                $("#itemTypeTable").find('button[id=addChildBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/item/itemTypeEdit.do?parentId=" + $(this).attr("data_id")});
                        //location.href = "${base}/item/itemTypeEdit.do?parentId=" + $(this).attr("data_id");
                    });
                });

                //单项查看子类
                $("#itemTypeTable").find('button[id=seeChildBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/item/itemTypes.do?id=" + $(this).attr("data_id")});
                        //location.href = "${base}/item/itemTypes.do?id=" + $(this).attr("data_id");
                    });
                });

            </#if>

            });
        </script>