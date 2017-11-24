<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/address/address.js"></script>

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>


<div class="modal fade" id="statusDialog" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="upItemTitle">店铺状态修改</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-10">
                        <p id="statusMarketName"></p>
                    </div>
                </div>

                <div class="row">
                <div class="col-sm-10">
                    <select class="form-control" id="updateStatusSelect">
                        <option data_id="1">正常</option>
                        <option data_id="4">维护</option>
                        <option data_id="2">关店</option>
                        <option data_id="0">无效</option>
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
                            <li class="active"><a href="#">店铺列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>店铺列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <button id="addBtn" type="button"
                                    class="btn waves-effect waves-light btn-primary">添加店铺
                            </button>

                            <div class="form-group">
                                <label for="exampleInputName2">地区选择：</label>
                                <select id="loc_province" style="width:120px;"></select>
                                <select id="loc_city" style="width:120px; margin-left: 10px"></select>
                                <select id="loc_district" style="width:120px;margin-left: 10px"></select>
                                <select id="loc_street" style="width:120px;margin-left: 10px"></select>
                            </div>

                            <div class="form-group m-l-15">
                                <label for="exampleInputName2">店铺状态：</label>
                                <select class="form-control" id="statusSelect" style="width:150px">
                                    <option data_id="-1" <#if status?exists && status == -1>selected</#if>>全部</option>
                                    <option data_id="1" <#if status?exists && status == 1>selected</#if>>正常</option>
                                    <option data_id="4" <#if status?exists && status == 4>selected</#if>>维护</option>
                                    <option data_id="2" <#if status?exists && status == 2>selected</#if>>关店</option>
                                    <option data_id="0" <#if status?exists && status == 0>selected</#if>>无效</option>
                                </select>
                            </div>

                            <div class="form-group m-l-15">
                                <label for="exampleInputName2">店铺配送方式：</label>
                                <select class="form-control" id="deliveryModeSelect" style="width:150px">
                                    <option data_id="-1" <#if deliveryMode?exists && deliveryMode == -1>selected</#if>>
                                        全部
                                    </option>
                                    <option data_id="1" <#if deliveryMode?exists && deliveryMode == 1>selected</#if>>
                                        仅自提
                                    </option>
                                    <option data_id="2" <#if deliveryMode?exists && deliveryMode == 2>selected</#if>>
                                        仅送货
                                    </option>
                                    <option data_id="3" <#if deliveryMode?exists && deliveryMode == 3>selected</#if>>
                                        可自提也可送货
                                    </option>
                                    <option data_id="4" <#if deliveryMode?exists && deliveryMode == 4>selected</#if>>
                                        AI(智能)
                                    </option>
                                    <option data_id="5" <#if deliveryMode?exists && deliveryMode == 5>selected</#if>>
                                        可自提也可AI配送
                                    </option>
                                </select>
                            </div>

                            <div class="input-group m-l-15">
                                <input type="text" id="searchKeyTxt"
                                       class="form-control" placeholder="输入店铺名称搜索">
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
                            <th>商店名字</th>
                            <th>商店类型</th>
                            <th>店铺状态</th>
                            <th>所在地区</th>
                            <th>具体地址</th>
                            <th>配送方式</th>
                            <th>配送距离(米)</th>
                            <th>配送费(元)</th>
                            <th>建店时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="marketListTable">

                        <#if (markets?size > 0) >
                            <#list markets as market>
                            <tr>
                                <td>${market.id?c}</td>
                                <td>${(market.name)!}</td>
                                <td>自营</td>
                                <td>
                                    <#if market.status == 0>
                                        <button id="statusBtn" type="button" data_s="${market.statusOffline}" data_status="${market.nowStatus}"
                                                class="btn waves-effect waves-light btn-inverse btn-xs"
                                                data_id="${market.id?c}" data_v="${market.name}">无效
                                        </button>
                                    </#if>
                                    <#if market.status == 1>
                                        <button id="statusBtn" type="button" data_s="${market.statusOffline}" data_status="${market.nowStatus}"
                                                class="btn waves-effect waves-light btn-success btn-xs"
                                                data_id="${market.id?c}" data_v="${market.name}">正常
                                        </button>
                                    </#if>
                                    <#if market.status == 2>
                                        <button id="statusBtn" type="button" data_s="${market.statusOffline}" data_status="${market.nowStatus}"
                                                class="btn waves-effect waves-light btn-danger btn-xs"
                                                data_id="${market.id?c}" data_v="${market.name}">关店
                                        </button>
                                    </#if>
                                    <#if market.status == 4>
                                        <button id="statusBtn" type="button" data_s="${market.statusOffline}" data_status="${market.nowStatus}"
                                                class="btn waves-effect waves-light btn-inverse btn-xs"
                                                data_id="${market.id?c}" data_v="${market.name}">维护
                                        </button>
                                    </#if>
                                    <!-- 断连标志 -->
                                    <#if market.statusOffline == 1>
                                        <button id="statusBtn" type="button" data_s="${market.statusOffline}" data_status="${market.nowStatus}"
                                                class="btn waves-effect waves-light btn-danger btn-xs"
                                                data_id="${market.id?c}" data_v="${market.name}">与硬件断连
                                        </button>
                                    </#if>
                                </td>
                                <td>
                                    <#if market.province??>
                                    ${market.province}&nbsp;
                                    </#if>
                                    <#if market.city??>
                                    ${market.city}&nbsp;
                                    </#if>
                                    <#if market.district??>
                                    ${market.district}&nbsp;
                                    </#if>
                                    <#if market.street??>
                                    ${market.street}&nbsp;
                                    </#if>
                                    <#if market.streetName?? >
                                    ${market.streetName}&nbsp;
                                    </#if>
                                    <#if market.streetNumber?? >
                                    ${market.streetNumber}&nbsp;
                                    </#if>
                                </td>
                                <td><#if market.address??>
                                ${market.address}&nbsp;&nbsp;
                                </#if></td>
                                <td>
                                    <#if market.deliveryMode == 1>
                                        仅自提
                                    </#if>
                                    <#if market.deliveryMode == 2>
                                        仅送货
                                    </#if>
                                    <#if market.deliveryMode == 3>
                                        可自提也可送货
                                    </#if>
                                    <#if market.deliveryMode == 4>
                                        AI(智能)
                                    </#if>
                                    <#if market.deliveryMode == 5>
                                        可自提也可AI配送
                                    </#if>
                                </td>
                                <td>
                                    <#if market.coveringDistance == -1>
                                        无限制(全国)
                                    </#if>
                                    <#if market.coveringDistance == 0>
                                        不对外
                                    </#if>
                                    <#if (market.coveringDistance > 0)>
                                    ${market.coveringDistance?c}
                                    </#if>
                                </td>
                                <td>
                                    <#if (market.deliveryCost > 0)>
                                        <#assign dCost = market.deliveryCost/100 >
                                    ${dCost}
                                    <#else>
                                        0
                                    </#if>
                                </td>
                                <td>${market.createTime}</td>
                                <td>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${market.id?c}">编辑
                                    </button>
                                    <button id="seeBtn" type="button"
                                            class="btn waves-effect waves-light btn-pink btn-sm"
                                            data_id="${market.id?c}">查看商品
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

        <#if (markets?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/market/markets.do?"
                + "province=${province}&provinceId=${provinceId}"
                + "&city=${city}&cityId=${cityId}"
                + "&district=${district}&districtId=${districtId}"
                + "&street=${street}&streetId=${streetId}"
                + "&status=${status}&deliveryMode=${deliveryMode}"
                />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>


        <script type="text/javascript">

            $(document).ready(function () {

                var _statusOkBtn = $("#statusOkBtn");

                var _marketListTable = $("#marketListTable");

                var _streets = {};
            <#if streets?exists && (streets?size > 0) >
                <#list streets as street>
                    _streets[${street.id?c}] = "${street.name}";
                </#list>
            </#if>

                showLocation(
                        <#if provinceId?exists >${provinceId?c}<#else>0</#if>,
                        <#if cityId?exists >${cityId?c}<#else>0</#if> ,
                        <#if districtId?exists >${districtId?c}<#else>0</#if> ,
                        <#if streetId?exists >${streetId?c}<#else>0</#if> ,
                        _streets, function (id, func) {
                            tokenPost("${base}/market/getStreets.do?districtId=" + id, function (data) {
                                func(id, data);
                            }, "json");
                        });

                function getVs(ui_name, info) {
                    var obj = $("#" + ui_name).find("option:selected");
                    var id = obj.attr("data_id");
                    if (id == "0" && info != null) {
                        swal(info);
                        return null;
                    }
                    if (id == "0") {
                        return {id: id, name: ""};
                    } else {
                        return {id: id, name: obj.attr("data_v")};
                    }
                }

                //添加
                $("#addBtn").on('click', function () {

                    //直接跳转
                    var provinceId = $('#loc_province').val();
                    var cityId = $('#loc_city').val();
                    var districtId = $('#loc_district').val();
                    var streetId = $('#loc_street').val();

                    open({url: "${base}/market/marketEdit.do?provinceId=" + provinceId + "&cityId=" + cityId + "&districtId=" + districtId + "&streetId=" + streetId});
                });

                function searchHandler() {

                    //直接跳转
                    var province = getVs("loc_province", null);
                    var city = getVs("loc_city", null);
                    var area = getVs("loc_district", null);
                    var street = getVs("loc_street", null);

                    //店铺状态
                    var _status_obj = $("#statusSelect").find("option:selected");
                    var _status = _status_obj.attr("data_id");

                    //配送方式
                    var _deliveryMode_obj = $("#deliveryModeSelect").find("option:selected");
                    var _deliveryMode = _deliveryMode_obj.attr("data_id");

                    var url = "${base}/market/markets.do?province=" + province.name + "&provinceId=" + province.id
                            + "&city=" + city.name + "&cityId=" + city.id
                            + "&district=" + area.name + "&districtId=" + area.id
                            + "&street=" + street.name + "&streetId=" + street.id
                            + "&status=" + _status + "&deliveryMode=" + _deliveryMode;

                    open({url: url});
                }

                //搜索
                $("#searchBtn").on('click', function () {
                    searchHandler();
                });

                //状态修改确定
                _statusOkBtn.on('click', function () {

                    //获得当前想修改的值
                    var _status_obj = $("#updateStatusSelect").find("option:selected");
                    var _status = _status_obj.attr("data_id");

                    //提交到服务器
                    var btn = $(this);
                    var beforeStatus = btn.attr("data_status");

                    //硬件有断连，并且又改为正常，则不允许修改
                    if(btn.attr("data_s") == "1" && _status == "1") {
                        swal("硬件断连中，不能将店铺状态改为 正常");
                        return;
                    }

                    tokenPost("${base}/market/marketUpdateStatus.do?marketId=" + btn.attr("data_id") + "&status=" + _status + "&beforeStatus=" + beforeStatus, function (json) {

                        $("#statusDialog").modal('hide');

                        if (json.code != 0) {
                            swal(json.msg);
                        } else {
                            //改变
                            searchHandler();
                        }
                    }, "json");
                });

            <#if (markets?size > 0)>

                //单项编辑
                _marketListTable.find('button[id=editBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url: "${base}/market/marketEdit.do?marketId=" + $(this).attr("data_id")});
                        //location.href = "${base}
                        //market/marketEdit.do?marketId=" + $(this).attr("data_id");
                    });
                });

                //单项查看商品
                _marketListTable.find('button[id=seeBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url: "${base}/market/marketItems.do?marketId=" + $(this).attr("data_id")});
                        //location.href = "${base}
                        //market/marketItems.do?marketId=" + $(this).attr("data_id");
                    });
                });

                //单项状态改变
                _marketListTable.find('button[id=statusBtn]').each(function () {
                    $(this).on('click', function () {

                        _statusOkBtn.attr("data_s", $(this).attr("data_s"));
                        _statusOkBtn.attr("data_status", $(this).attr("data_status"));
                        _statusOkBtn.attr("data_id", $(this).attr("data_id"));


                        $("#statusMarketName").html($(this).attr("data_v"));
                        $("#statusMarketName").attr("data_id", $(this).attr("data_id"));
                        $("#statusDialog").modal('show');
                    });
                });

            </#if>
            });
        </script>