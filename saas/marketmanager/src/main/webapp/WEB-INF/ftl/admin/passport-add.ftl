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
                            <li><a href="#">系统管理</a></li>
                            <li class="active">添加帐号</li>
                        </ol>
                        <h4 class="page-title"><b>添加帐号</b></h4>
                    </div>
                </div>
            </div>

            <div class="row m-t-30">
                <div class="col-md-8">
                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label class="col-md-4 control-label">帐号类型：</label>
                            <div class="col-md-8">
                                <select class="form-control" id="pType" style="width:150px">
                                    <option data_id="21">补货员</option>
                                    <option data_id="31">快递员</option>
                                    <option data_id="41">后台管理员</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">登录用户名：</label>
                            <div class="col-md-8">
                                <input type="text" id="loginName" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label" for="example-email">登录密码：</label>
                            <div class="col-md-8">
                                <input type="text" id="loginPass" class="form-control" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">呢称：</label>
                            <div class="col-md-8">
                                <input type="text" id="showName" class="form-control" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">姓名：</label>
                            <div class="col-md-8">
                                <input type="text" id="zName" class="form-control" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">身份证号：</label>
                            <div class="col-md-8">
                                <input type="text" id="zNo" class="form-control" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">联系号码：</label>
                            <div class="col-md-8">
                                <input type="text" id="zPhone" class="form-control" >
                            </div>
                        </div>


                        <div class="form-group m-t-20">
                            <div class="col-md-4">
                            </div>
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

                $("#backBtn").on('click', function () {
                    history.back(-1);
                });

                //添加/修改 商品
                $("#saveBtn").on('click', function () {

                    //店铺状态
                    var _status_obj = $("#pType").find("option:selected");
                    var _type = _status_obj.attr("data_id");

                    var post_data = {
                        name: $("#loginName").val(),
                        password: $("#loginPass").val(),
                        showName: $("#showName").val(),
                        realName: $("#zName").val(),
                        idNumber: $("#zNo").val(),
                        phoneNumber: $("#zPhone").val(),
                        roleType:_type,
                    };

                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    tokenPresPost("${base}/passport/distributionAccount.do", post_data, function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            //跳转去passport列表
                            open({url:"${base}/passport/passportList.do"});
                        } else {
                            btn_.removeAttr("disabled");
                            swal(data.msg);
                        }
                    });
                });
            });
        </script>