<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

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
                            <li class="active">店铺绑定硬件MAC</li>
                        </ol>
                        <h4 class="page-title"><b>店铺绑定硬件MAC</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

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

                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label col-md-2">新增绑定：</label>
                        <div class="col-md-6">
                            <input type="text" id="macInput" class="form-control">
                        </div>
                        <button id="saveBtn" type="button"
                                class="btn waves-effect waves-light btn-primary col-md-2">增加
                        </button>
                    </div>
                </div>
            </div>

            <div class="row m-t-15">
                <div class="col-sm-6">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>绑定的硬件MAC</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="macListTable">

                        <#if macs?exists && (macs?size > 0)>
                            <#list macs as mac>
                            <tr>
                                <td>${mac}</td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-danger btn-sm"
                                            data_id="${marketId?c}" data_k="${mac}">解绑
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

            <!-- end container -->
        </div>


        <script type="text/javascript">
            $(document).ready(function () {

                var _sMarket = $("#sMarket");

                _sMarket.select2();
                _sMarket.change(function () {
                    var select_obj = _sMarket.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    open({url: "${base}/market/marketBindMacList.do?marketId=" + data_id});
                });

                //解绑
            <#if macs?exists && (macs?size > 0)>

                //单项编辑
                $("#macListTable").find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {
                        var btn_ = $(this);
                        var data_id = $(this).attr("data_id");
                        btn_.attr("disabled", true);
                        //type=3是mac
                        tokenPost("${base}/market/marketUnBind.do?marketId=" + data_id + "&k=" + $(this).attr("data_k") + "&type=3", function (json) {
                            if (json.code == 0) {
                                //成功了，跳转去目标
                                open({url: "${base}/market/marketBindMacList.do?marketId=" + data_id});
                            } else {
                                btn_.removeAttr("disabled");
                                swal(json.msg);
                            }
                        });
                    });
                });
            </#if>

                //新增
                $("#saveBtn").on('click', function () {

                    var mac = $("#macInput").val();

                    var select_obj = _sMarket.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    tokenPost("${base}/market/marketBind.do?marketId=" + data_id + "&k=" + mac + "&type=3", function (json) {
                        if (json.code == 0) {
                            //成功了，跳转去目标
                            open({url: "${base}/market/marketBindMacList.do?marketId=" + data_id});
                        } else {
                            btn_.removeAttr("disabled");
                            swal(json.msg);
                        }
                    });
                });
            });
        </script>