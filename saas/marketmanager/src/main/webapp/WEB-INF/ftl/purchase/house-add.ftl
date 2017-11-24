<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">供应商列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>新建仓库</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>基本信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">仓库编码:</td>
                                        <td><input type="text" class="form-control" id="addHouseCode" placeholder="输入仓库编码"/></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">仓库名称</td>
                                        <td><input type="text" class="form-control" id="addHouseName" placeholder="输入仓库名称"/></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">仓库地址</td>
                                        <td><input type="text" class="form-control" id="addHouseAddress" placeholder="输入仓库地址"/></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">备注</td>
                                        <td><input type="text" class="form-control" id="remark" placeholder="请输入相关说明"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-6">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>对接信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">Key:</td>
                                        <td><input type="text" class="form-control" id="keyInput" placeholder="请输入与WMS对接Key"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="m-t-40">
                                <button id="saveBtn" type="button" class="btn waves-effect waves-light btn-primary col-md-2">确定</button>
                                <button id="backBtn" type="button" class="btn waves-effect waves-light btn-default col-md-2" onclick="history.go(-1);">返回</button>
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
        function checkInput (obj){//检查表单是否有空项，空格验证方法待加
            if(obj == "") {return false;} else {return true;}
        }
        $("#saveBtn").on('click', function () {//确定
            $(this).button('loading').delay(500).queue(function() {
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var arr = {
                    "code": $("#addHouseCode").val(),
                    "name": $("#addHouseName").val(),
                    "address": $("#addHouseAddress").val(),
                    "remark": $("#remark").val(),
                    "key": $("#keyInput").val(),
                };
                var input1 = checkInput(arr.code);
                var input2 = checkInput(arr.name);
                var input3 = checkInput(arr.address);
//                var input4 = checkInput(arr.key);//wmsKEY可为空，省略检测
                if (input1 && input2 && input3) {
                    var url = "${base}/purchase/saveWarehouse.do?warehouseCode="
                            + arr.code + "&warehouseName=" + arr.name + "&address=" + arr.address + "&remark="
                            + arr.remark + "&wmsKEY=" + arr.key;
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
                } else {
                    swal("提示", "请检查表单是否有漏填项！", "info");
                }
            });
        });
    });
</script>
