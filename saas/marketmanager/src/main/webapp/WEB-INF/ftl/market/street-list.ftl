<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->


<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/address/address.js"></script>

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>

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
                            <li class="active"><a href="#">街道列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>街道列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form">

                    <button id="addBtn" type="button"
                         class="btn waves-effect waves-light btn-primary">添加街道
                    </button>

                    <div class="form-group m-l-15">
                        <label for="exampleInputName2">地区选择：</label>
                        <select id="loc_province" style="width:120px;"></select>
                        <select id="loc_city" style="width:120px; margin-left: 10px"></select>
                        <select id="loc_district" style="width:120px;margin-left: 10px"></select>
                    </div>
                </form>
            </div>


            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>街道名称</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="streetListTable">

                        <#if (streets?size > 0)>
                            <#list streets as street>
                            <tr>
                                <td>${street.id?c}</td>
                                <td>${street.name}</td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${street.id?c}">编辑
                                    </button>
                                </td>
                            </tr>

                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="3" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>


                </div>
            </div>

            <!-- end container -->
        </div>


        <script type="text/javascript">

            $(document).ready(function () {

                var old_areaId = ${areaId?c};

                showLocation(${provinceId?c}, ${cityId?c}, ${areaId?c}, 0, null, function (id) {

                    if(old_areaId != id) {
                        //直接跳转
                        var provinceId = $('#loc_province').val();
                        var cityId = $('#loc_city').val();
                        var areaId = $('#loc_district').val();

                        open({url:"${base}/market/streets.do?provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId});
                        //location.href = "${base}/market/streets.do?provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId;
                    }
                });

                $("#addBtn").on('click', function () {

                    //直接跳转
                    var provinceId = $('#loc_province').val();
                    var cityId = $('#loc_city').val();
                    var areaId = $('#loc_district').val();

                    open({url:"${base}/market/streetEdit.do?provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId});
                    //location.href = "${base}/market/streetEdit.do";
                });

            <#if (streets?size > 0)>

                //单项编辑
                $("#streetListTable").find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {

                        //直接跳转
                        var provinceId = $('#loc_province').val();
                        var cityId = $('#loc_city').val();
                        var areaId = $('#loc_district').val();

                        open({url:"${base}/market/streetEdit.do?id=" + $(this).attr("data_id") +"&provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId});
                        //location.href = "${base}/market/streetEdit.do?id=" + $(this).attr("data_id");
                    });
                });

            </#if>
            });


        </script>