<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">仓库列表</a></li>
                        </ol>
                        <h4 class="page-title "><b>添加仓管员</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <div>
                                <ol class="breadcrumb pull-right">
                                    <#--<li><a href="#">添加仓管员</a></li>-->
                                </ol>
                                <h5 class="page-title" style="padding-top: 20px"><b>基本信息</b></h5>
                                <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                                <div class="table-responsive advert_detail_table">
                                    <table class="table table-bordered">
                                        <tbody>
                                        <tr>
                                            <td style="background-color: #f9f9f9">账号</td>
                                            <td><input id="userName" type="text" class="form-control" placeholder="请输入账号"></td>
                                            <td style="background-color: #f9f9f9">备注</td>
                                            <td><input id="remark" type="text" class="form-control" placeholder="请输入相关说明"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="form-group m-t-20">
                                    <button id="saveBtn" type="button"
                                            class="btn waves-effect waves-light btn-primary col-md-2">确定
                                    </button>
                                    <button id="backBtn" type="button" onclick="history.go(-1);"
                                            class="btn waves-effect waves-light btn-default col-md-2">返回
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        //取url参数给表单赋值
        function GetQueryString(name) {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }
        $("#saveBtn").on('click',function () {
            $(this).button('loading').delay(500).queue(function() {
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var _id = GetQueryString('id');
                var name = $("#userName").val();
                var remark = $("#remark").val();
                var url = "${base}/purchase/saveWarehouseUser.do?warehouseID=" + _id + "&userName=" + name + "&remark=" + remark;
                $.post(url, function (data) {
                    //重新刷新
                    console.log(data);
                    if (data.code == "0") {
                        swal("提示", "添加成功", "success");
                        setTimeout(function () {
                            location.href = "${base}/purchase/warehousePage.do";
                        }, 1000);
                    } else {
                        swal("提示", data.msg, "error");
                    }
                }, "json");
            });
        });
    });
</script>
