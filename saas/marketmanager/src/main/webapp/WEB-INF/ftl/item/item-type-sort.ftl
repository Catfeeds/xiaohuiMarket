<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<!-- Plugins css -->
<link href="${res}/assets/plugins/nestable/jquery.nestable.css" rel="stylesheet">

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
                            <li><a href="#">商品分类</a></li>
                            <li class="active"><a href="#">分类排序</a></li>
                        </ol>
                        <h4 class="page-title"><b>分类排序</b></h4>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <div class="card-box">
                        <div class="row">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-3">
                                <select class="form-control" id="sortlist">
                                    <option data_id="0">排序所有一级分类</option>
                                <#if itemTypes?exists && (itemTypes?size > 0)>
                                    <#list itemTypes as type>
                                        <option data_id="${type.id?c}" <#if selectId == type.id>
                                                selected
                                        </#if>>排序 ${type.title} 的子类
                                        </option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                            <div class="col-sm-3">
                                <button id="saveBtn" type="button" class="btn waves-effect waves-light btn-primary "><i
                                        class="fa fa-pencil"></i> 保存调整后的排序
                                </button>
                            </div>
                            <div class="col-sm-3"></div>
                        </div>

                        <div class="row m-t-30">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-6">
                                <div class="dd" id="item_type_sort">
                                    <ol class="dd-list">
                                    <#if cItemTypes?exists && (cItemTypes?size > 0)>
                                        <#list cItemTypes as type>
                                            <li class="dd-item" data_sort="${type.sort}" data_id="${type.id?c}">
                                                <div class="dd-handle">${type.title}   ${type.sort}   </div>
                                            </li>
                                        </#list>
                                    </#if>
                                    </ol>
                                </div>
                            </div>
                            <div class="col-sm-3"></div>
                        </div>

                    </div>
                </div>
            </div>
            <!-- End of row -->
        </div>

        <!-- 拖动排序js -->
        <script src="${res}/assets/plugins/nestable/jquery.nestable.js"></script>

        <script type="text/javascript">

            $(document).ready(function () {

                $("#item_type_sort").nestable();

                $("#sortlist").on("change", function () {
                    var obj = $(this).children('option:selected');
                    open({url:"${base}/item/itemTypeSort.do?id=" + obj.attr("data_id")});
                    //location.href = "${base}/item/itemTypeSort.do?id=" + obj.attr("data_id");
                });

                $("#saveBtn").on("click", function () {

                    var dIndex = 0;
                    var ids = "";

                    $("#item_type_sort").find('li').each(function () {
                        var v = $(this).attr("data_sort");
                        if (dIndex != v) {
                            ids = ids + $(this).attr("data_id") + "," + dIndex + ",";
                        }
                        dIndex = dIndex + 1;
                    });

                    if(ids == "") {
                        swal("没有任何改变");
                        return;
                    }

                    //保存
                    tokenPresPost("${base}/item/itemTypeSortEditSave.do", {ids:ids}, function(data) {

                        if(data.code == 0) {
                            //成功
                            swal({
                                title: "保存成功",
                                text: "商品分类排序已变更",
                                type: "success",
                                confirmButtonText: "确定"
                            }, function (ok) {
                                location.reload(true);
                            });
                        } else {
                            //失败
                            swal({
                                title: "保存失败",
                                text: "未知原因，商品分类排序保存失败",
                                type: "error",
                                confirmButtonText: "确定"
                            }, function (ok) {
                                location.reload(true);
                            });
                        }
                    }, "json");

                });
            });

        </script>

