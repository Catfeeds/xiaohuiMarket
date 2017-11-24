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
                            <li><a href="#">订单管理</a></li>
                            <li class="active"><a href="#">订单列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>订单列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <h4 class="header-title m-t-0"><b>搜索订单</b></h4>
                <form class="form-inline" role="form">

                    <div class="form-group m-l-15">
                        <label>店铺：</label>
                        <select class="form-control" id="marketSelect" style="width:150px">
                            <option data_id="0">全部店铺</option>
                        <#if markets?exists && (markets?size > 0)>
                            <#list markets as market>
                                <option data_id="${market.id?c}" <#if market.id == marketId>selected</#if> >${market.name}</option>
                            </#list>
                        </#if>
                        </select>
                    </div>

                    <div class="form-group m-l-15">
                        <label>订单状态：</label>
                        <select class="form-control" id="statusSelect" style="width:150px">
                            <#if backOrder?exists >
                                <option data_id="2048" <#if orderState=2048>selected</#if> >申请退款中</option>
                                <option data_id="4096" <#if orderState=4096>selected</#if> >退款中</option>
                                <option data_id="4098" <#if orderState=4098>selected</#if> >拒绝退款</option>
                                <option data_id="4352" <#if orderState=4352>selected</#if> >已退款</option>
                            <#else>
                                <option data_id="0" <#if orderState=0>selected</#if> >全部订单</option>
                                <option data_id="1" <#if orderState=1>selected</#if> >待支付</option>
                                <option data_id="2" <#if orderState=2>selected</#if> >已取消</option>
                                <option data_id="4" <#if orderState=4>selected</#if> >已失效</option>
                                <option data_id="8" <#if orderState=8>selected</#if> >已支付</option>
                                <option data_id="16" <#if orderState=16>selected</#if> >已接单</option>
                                <option data_id="32" <#if orderState=32>selected</#if> >发货中</option>
                                <option data_id="64" <#if orderState=64>selected</#if> >配送中</option>
                                <option data_id="128" <#if orderState=128>selected</#if> >送达</option>
                                <option data_id="256" <#if orderState=256>selected</#if> >已完成</option>
                            </#if>
                        </select>
                    </div>

                    <div class="form-group m-l-15">
                        <label>日期：</label>
                        <div class="input-group">
                            <input id="startTime" type="text" class="form-control" value="${sTime}">
                            <span class="input-group-addon bg-default"
                                  onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})"><i
                                    class="fa fa-calendar"></i></span>
                        </div>

                        <label> 至 </label>

                        <div class="input-group">
                            <input id="endTime" type="text" class="form-control" value="${eTime}" readonly>
                            <span class="input-group-addon bg-default"
                                  onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})"><i
                                    class="fa fa-calendar"></i></span>
                        </div>
                    </div>

                    <div class="input-group m-l-15">
                        <div class="input-group-btn">
                            <button id="searchMenu" type="button" search_name="订单号"
                                    class="btn btn-default waves-effect waves-light dropdown-toggle"
                                    data-toggle="dropdown">按订单号搜索 <span class="caret"></span></button>
                            <ul class="dropdown-menu" id="searchType">
                                <li><a href="javascript:void(0)" search_name="订单号" search_type="orderNum">按订单号搜索</a>
                                </li>
                                <li><a href="javascript:void(0)" search_name="用户名" search_type="uName">按用户名搜索</a></li>
                                <li><a href="javascript:void(0)" search_name="用户电话" search_type="uPhone">按用户电话搜索</a>
                                </li>
                                <li><a href="javascript:void(0)" search_name="配送员" search_type="cName">按配送员搜索</a></li>
                                <li><a href="javascript:void(0)" search_name="配送员电话" search_type="cPhone">按配送员电话搜索</a>
                                </li>
                            </ul>
                        </div>
                        <input type="text" id="searchKeyTxt"
                               class="form-control" placeholder="按订单号搜索">
                            <span class="input-group-btn">
                                    <button id="searchBtn" type="button"
                                            class="btn waves-effect waves-light btn-primary"><i
                                            class="fa fa-search"></i></button>
                            </span>
                    </div>

                </form>
            </div>

            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>ID</th>
                            <th>订单号</th>
                            <th>店铺信息</th>
                            <th>收货信息</th>
                            <th>配送信息</th>
                            <th>订单信息</th>
                            <th>订单状态</th>
                            <th>下单时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="orderListTable">

                        <#if (orders?size > 0)>
                            <#list orders as order>
                            <tr>
                                <td>${order.id?c}</td>
                                <td>${order.orderSequenceNumber}</td>
                                <td>店铺名：${order.shippingNickName}<br>电话：${order.shippingPhone}</td>
                                <td>用户：${order.receiptNickName}<br>电话：${order.receiptPhone}<br><#if order.deliverType != 1>地址：</#if></td>
                                <td><#if order.deliverType == 1><span class="label label-success">自提</span><#else>
                                    <#if order.status < 16>
                                        <span class="label label-danger">未有快递员接单</span>
                                    <#else>
                                        配送员：<#if order.courierNickName??>${order.courierNickName}</#if><br>电话：<#if order.courierPhone??>${order.courierPhone}</#if>
                                    </#if>
                                </#if></td>
                                <td>
                                    订单总费用：${order.totalPrice}<br>
                                    优惠的费用：${order.discountPrice}<br>
                                    <#if order.deliverType != 1>
                                        配送费用：${order.distributionFee}<br>
                                    </#if>
                                    实收费用：${order.actualPrice}
                                </td>
                                <td>
                                    <span class="label label-success">${order.stateName}</span>
                                </td>
                                <td>
                                    下单时间：${order.createTime}<br>支付时间：${order.paymentTime}<br><#if order.status == 256>
                                    订单完成时间：${order.confirmTime}</#if></td>
                                <td>
                                    <#if orderState == 2048 >
                                        <button id="agreedBtn" type="button"
                                                class="btn waves-effect waves-light btn-warning btn-sm"
                                                data_id="${order.id?c}">同意退款
                                        </button>
                                        <button id="rejectBtn" type="button"
                                                class="btn waves-effect waves-light btn-warning btn-sm"
                                                data_id="${order.id?c}">拒绝退款
                                        </button>
                                    </#if>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${order.id?c}">查看订单详情
                                    </button>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="9" height="200px">
                                <p class="text-center">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>

                </div>
            </div>

        <#if (orders?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/order/orders.do?searchType=${searchType}&searchKey=${searchKey}&marketId=${marketId}&orderState=${orderState}&sTime=${sTime}&eTime=${eTime}" />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>


        <script type="text/javascript" src="${res}/assets/plugins/jedate/jedate.min.js"></script>
        <script type="text/javascript">

            $(document).ready(function () {

                //默认按名称
                var marketId = "${marketId}";

                var searchTypeValue = "${searchType}";

                var _searchMenu = $("#searchMenu");
                var _searchKeyTxt = $("#searchKeyTxt");
                var _orderListTable = $("#orderListTable");
                var _startTime = $("#startTime");
                var _endTime = $("#endTime");

                //根据搜索类型，设置选中项
                if (searchTypeValue == "uName") {
                    _searchMenu.html("按用户名搜索<span class=\"caret\"></span>");
                } else if (searchTypeValue == "uPhone") {
                    _searchMenu.html("按用户电话搜索<span class=\"caret\"></span>");
                } else if (searchTypeValue == "cName") {
                    _searchMenu.html("按配送员搜索<span class=\"caret\"></span>");
                } else if (searchTypeValue == "cPhone") {
                    _searchMenu.html("按配送员电话搜索<span class=\"caret\"></span>");
                } else {
                    searchTypeValue = "orderNum";
                    _searchMenu.html("按订单号搜索<span class=\"caret\"></span>");
                }

                //设置搜索框内容
                _searchKeyTxt.val("${searchKey}");

                //搜索选择
                $("#searchType li a").on('click', function () {
                    var sName = $(this).attr("search_name");
                    var txt = "按" + sName + "搜索";
                    searchTypeValue = $(this).attr("search_type");
                    _searchMenu.html(txt + "<span class=\"caret\"></span>");
                    _searchMenu.attr("search_name", sName)
                    _searchKeyTxt.attr("placeholder", txt);
                });

                //店铺选择
                $("#marketSelect").on("change", function () {
                    var obj = $(this).children('option:selected');
                    marketId = obj.attr("data_id");
                });

                //订单状态选择
                $("#stateSelect").on("change", function () {
                    var obj = $(this).children('option:selected');
                    orderState = obj.attr("data_id");
                });

                $("#searchBtn").on('click', function () {
                    //搜索文本
                    var sValue = _searchKeyTxt.val();
                    if (containSpecial.test(sValue)) {
                        swal("包括了特殊符号，无法搜索!");
                        return;
                    }

                    var sType = searchTypeValue;
                    if(sValue == "") {
                        sType = "";
                    }

                    var sTime = _startTime.val();
                    var eTime = _endTime.val();

                    var url = "${base}/order/orders.do?searchType="+sType
                            +"&searchKey="+sValue
                            +"&marketId="+marketId
                            +"&orderState="+orderState
                            +"&sTime="+sTime+"&eTime="+eTime;

                    open({url:url});
                });

            <#if (orders?size > 0)>

                //同意退款
                _orderListTable.find('button[id=agreedBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/item/itemEdit.do?id=" + $(this).attr("data_id")});
                        //location.href = "${base}/item/itemEdit.do?id=" + $(this).attr("data_id");
                    });
                });

                //拒绝退款
                _orderListTable.find('button[id=rejectBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/market/marketItems.do?searchType=itemTemplateId&searchKey=" + $(this).attr("data_id")});
                        //location.href = "${base}/market/marketItems.do?searchType=itemTemplateId&searchKey=" + $(this).attr("data_id");
                    });
                });

                //详情
                _orderListTable.find('button[id=rejectBtn]').each(function () {
                    $(this).on('click', function () {
                        open({url:"${base}/market/marketItems.do?searchType=itemTemplateId&searchKey=" + $(this).attr("data_id")});
                        //location.href = "${base}/market/marketItems.do?searchType=itemTemplateId&searchKey=" + $(this).attr("data_id");
                    });
                });

            </#if>
            });
        </script>