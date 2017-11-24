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
                        <h4 class="page-title "><b>添加供应商</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <div>
                                <h5 class="page-title" style="padding-top: 20px"><b>基本信息</b></h5>
                                <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                                <div class="table-responsive advert_detail_table">
                                    <table class="table table-bordered">
                                        <tbody>
                                        <tr>
                                            <td style="background-color: #f9f9f9">供应商名称</td>
                                            <td><input type="text" class="form-control" id="addSupplierName" placeholder="输入供应商名称"/></td>
                                            <td style="background-color: #f9f9f9">供应商地址</td>
                                            <td><input type="text" class="form-control" id="addSupplierAddress" placeholder="输入供应商地址"/></td>
                                        </tr>
                                        </tbody>
                                        <tbody>
                                        <tr>
                                            <td style="background-color: #f9f9f9">供应商属性</td>
                                            <td>
                                                <select id="supplierSelect" class="form-control">
                                                    <option value="1">一级供应商</option>
                                                    <option value="2">二级供应商</option>
                                                    <option value="3">品牌供应商</option>
                                                    <option value="4">超市供应商</option>
                                                </select>
                                            </td>
                                            <td style="background-color: #f9f9f9">出货周期</td>
                                            <td><input type="text" class="form-control" id="addSupplierTime" placeholder="输入出货周期（天）"/></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="advert_container">
                            <div>
                                <h5 class="page-title" style="padding-top: 20px"><b>联系信息</b></h5>
                                <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                                <div class="table-responsive">
                                    <table class="table table-bordered">
                                        <thead>
                                        <tr>
                                            <th style="background-color: #f9f9f9">对接业务员</th>
                                            <th style="background-color: #f9f9f9">联系电话</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td><input type="text" class="form-control" id="addSaleName" placeholder="输入业务员姓名"/></td>
                                            <td><input type="text" class="form-control" id="addPhone" placeholder="输入业务员电话"/></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="m-t-40">
                                    <button id="addBtn" type="button" class="btn waves-effect waves-light btn-primary col-md-2">确定</button>
                                    <button id="backBtn" type="button" class="btn waves-effect waves-light btn-default col-md-2" onclick="history.go(-1);">返回</button>
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
    function checkInput (obj){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {return false;} else {return true;}
    }
    $(document).ready(function () {
        $("#addBtn").on('click',function () {
            $(this).button('loading').delay(500).queue(function() {
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var arr = {
                    "name": $("#addSupplierName").val(),
                    "address": $("#addSupplierAddress").val(),
                    "select": $("#supplierSelect").val(),
                    "time": $("#addSupplierTime").val(),
                    "sname": $("#addSaleName").val(),
                    "phone": $("#addPhone").val()
                };
                var input1 = checkInput(arr.name);
                var input2 = checkInput(arr.address);
                var input3 = checkInput(arr.select);
                var input4 = checkInput(arr.time);
                var input5 = checkInput(arr.sname);
                var input6 = checkInput(arr.phone);
                if (input1 && input2 && input3 && input4 && input5 && input6) {
                    var url = "${base}/purchase/saveSupplier.do?supplierName="
                            + arr.name + "&address=" + arr.address + "&supplierType=" + arr.select + "&deliverPeriod="
                            + arr.time + "&salesmanName=" + arr.sname + "&phone=" + arr.phone;
                    $.post(url, function (data) {
                        //重新刷新
                        console.log(data);
                        if (data.code == "0") {
                            swal("提示", "添加成功", "success");
                            setTimeout(function () {
                                location.href = "${base}/purchase/supplierPage.do";
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