<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>

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
                            <li><a href="#">店铺管理</a></li>
                            <li><a href="#">店铺商品管理</a></li>
                            <li class="active">编辑店铺商品</li>
                        </ol>
                        <h4 class="page-title"><b>编辑店铺商品</b></h4>
                    </div>
                </div>
            </div>

            <div class="row m-t-30">
                <div class="col-md-6">
                    <form class="form-horizontal" role="form" id="formDefault">
                        <div class="form-group">
                            <label class="col-md-4 control-label">店铺：</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" value="${market.name}" disabled>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">商品名称：</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" value="${itemTemplate.name}" disabled>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">成本价(元)：</label>
                            <div class="col-md-8">
                                <input id="ipCostPrice" type="text" class="form-control" value="${marketItem.costPrice}" onkeyup="clearNoNum(this)">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">销售价(元)：</label>
                            <div class="col-md-8">
                                <input id="ipSellPrice" type="text" class="form-control" value="${marketItem.sellPrice}" onkeyup="clearNoNum(this)">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">市场售价(元)：</label>
                            <div class="col-md-8">
                                <input id="ipMarketPrice" type="text" class="form-control" value="${marketItem.marketPrice}" onkeyup="clearNoNum(this)">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">促销价(元)：</label>
                            <div class="col-md-8">
                                <input id="ipDiscountPrice" type="text" class="form-control" value="${marketItem.discountPrice}" onkeyup="clearNoNum(this)">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">商品描述：</label>
                            <div class="col-md-8">
                                <textarea class="form-control" id="itemInfo" rows="5" value="${marketItem.description}" ></textarea>
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


                <div class="col-md-6">

                </div>
            </div>

            <!-- end container -->
        </div>


        <script type="text/javascript">

            $(document).ready(function () {

                //该商品归属的商店ID
                var s_itemId = ${marketItem.id?c};

                $("#backBtn").on('click', function () {
                    history.back(-1);
                });

                //保存商品单位
                $("#saveBtn").on('click', function () {

                    //$(this).button("loading");

                    var _costPrice = $("#ipCostPrice").val();
                    var _sellPrice = $("#ipSellPrice").val();
                    var _marketPrice = $("#ipMarketPrice").val();
                    var _discountPrice = $("#ipDiscountPrice").val();
                    var des = $("#itemInfo").val();

                    var post_data = {
                        marketId:${market.id?c},
                        itemId:s_itemId,
                        costPrice:_costPrice,
                        sellPrice:_sellPrice,
                        marketPrice:_marketPrice,
                        discountPrice:_discountPrice,
                        description:des,
                    };

                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    tokenPresPost("${base}/market/marketItemEditSave.do", post_data, function (data) {

                        //重新刷新
                        if(data.code == "0") {
                            showSuccess(data.msg, function () {
                                open({url:"${base}/market/marketItems.do?marketId=${market.id?c}&searchType=${searchType}&searchKey=${searchKey}&pageSize=${pageSize}&pageIndex=${pageIndex}"
                                });
                            });
                        } else {
                            //$(this).button("reset");
                            btn_.removeAttr("disabled");
                            swal(data.msg);
                        }

                    }, "json");
                });
            });
        </script>