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
                            <li><a href="#">街道信息</a></li>
                            <li class="active"><#if street?exists>编辑<#else>添加</#if>街道信息</li>
                        </ol>
                        <h4 class="page-title"><b><#if street?exists>编辑<#else>添加</#if>街道信息</b></h4>
                    </div>
                </div>
            </div>

            <div class="row m-t-30">
                <div class="col-md-8">
                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label class="col-md-4 control-label" for="example-email">所在地区：</label>
                            <div class="col-md-8">
                                <select id="loc_province" style="width:120px;"></select>
                                <select id="loc_city" style="width:120px; margin-left: 10px"></select>
                                <select id="loc_district" style="width:120px;margin-left: 10px"></select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">街道名称：</label>
                            <div class="col-md-8">
                                <input id="streetTitle" type="text" class="form-control" <#if street?exists>
                                       value="${street.name}" </#if>>
                            </div>
                        </div>

                        <br><br>

                        <div class="form-group">
                            <div class="col-md-4"></div>
                            <button id="saveBtn" type="button"
                                    class="btn waves-effect waves-light btn-primary col-md-2">确定
                            </button>
                            <button id="backBtn" type="button"
                                    class="btn waves-effect waves-light btn-default col-md-2">返回
                            </button>
                        </div>

                        <br><br><br><br><br>
                    </form>
                </div>
            </div>

            <!-- end container -->
        </div>


        <script type="text/javascript">

            $(document).ready(function () {

                var _now_areaId = 0;
                var _streetTitle = $("#streetTitle");
                var _streetId = <#if street?exists>${street.id?c};<#else>0;</#if>

                showLocation(${provinceId?c}, ${cityId?c}, ${areaId?c}, 0, null, function (id) {
                    _now_areaId = id;
                });

                moveEnd(_streetTitle.get(0));

                $("#backBtn").on('click', function () {
                    history.back(-1);
                });

                //保存商品单位
                $("#saveBtn").on('click', function () {

                    //检测
                    var title = _streetTitle.val();
                    if (title == "") {
                        swal("请输入街道名称!");
                        return;
                    }

                    //$(this).button("loading");

                    //直接跳转
                    var provinceId = $('#loc_province').val();
                    var cityId = $('#loc_city').val();
                    var areaId = $('#loc_district').val();


                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    tokenPost("${base}/market/streetEditSave.do?id=" + _streetId + "&title=" + title + "&areaId=" + areaId, function (data) {
                        //重新刷新
                        if (data.code == "0") {
                            showSuccess(data.msg, function () {
                                open({url:"${base}/market/streets.do?provinceId=" + provinceId + "&cityId=" + cityId + "&areaId=" + areaId });
                                //location.href = "${base}/market/marketItems.do?id=" + s_MarketId + "&groupCode=" + s_Group + "&unitCode=" + s_Unit + "&floorCode=" + s_Layer;
                            })
                        } else {
                            //$(this).button("reset");
                            btn_.removeAttr("disabled");
                            swal(data.msg);
                        }
                    }, "json");
                });
            });
        </script>