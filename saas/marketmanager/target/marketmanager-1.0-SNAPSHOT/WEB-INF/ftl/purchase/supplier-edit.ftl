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
                        <h4 class="page-title "><b>编辑供应商</b></h4>
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
                                <#if supplier?exists>
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">供应商名称</td>
                                        <td><input type="text" class="form-control" id="editSupplierName" placeholder="输入供应商名称" value="${supplier.supplierName}"/></td>
                                        <td style="background-color: #f9f9f9">供应商地址</td>
                                        <td><input type="text" class="form-control" id="editSupplierAddress" placeholder="输入供应商地址" value="${supplier.address}"/></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">供应商属性</td>
                                        <td>
                                            <select id="supplierSelect" class="form-control">
                                                <option value="1" <#if supplier.supplierType=1>selected</#if>>一级供应商</option>
                                                <option value="2" <#if supplier.supplierType=2>selected</#if>>二级供应商</option>
                                                <option value="3" <#if supplier.supplierType=3>selected</#if>>品牌供应商</option>
                                                <option value="4" <#if supplier.supplierType=4>selected</#if>>超市供应商</option>
                                            </select>
                                        </td>
                                        <td style="background-color: #f9f9f9">出货周期</td>
                                        <td><input type="text" class="form-control" id="editSupplierTime" placeholder="输入出货周期" value="${supplier.deliverPeriod}"/></td>
                                    </tr>
                                    </tbody>
                                </#if>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <div class="advert_container">
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
                                    <#if supplier?exists>
                                        <tr>
                                            <td><input type="text" class="form-control" id="saleName" placeholder="输入业务员姓名" value="${supplier.salesmanName}"/></td>
                                            <td><input type="text" class="form-control" id="salePhone" placeholder="输入业务员电话" value="${supplier.phone}"/></td>
                                        </tr>
                                    <#else>
                                        <tr>
                                            <td colSpan="11" height="50px">
                                                <p class="text-center" style="line-height: 50px">暂无任何数据</p>
                                            </td>
                                        </tr>
                                    </#if>
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
            if(obj == "") {
                return false;
            } else {
                return true;
            }
        }
        $("#saveBtn").on('click',function () {
            $(this).button('loading').delay(500).queue(function() {
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var arr = {
                    "name": $("#editSupplierName").val(),
                    "address": $("#editSupplierAddress").val(),
                    "select": $("#supplierSelect").val(),
                    "time": $("#editSupplierTime").val(),
                    "sname": $("#saleName").val(),
                    "phone": $("#salePhone").val()
                };
                var input1 = checkInput(arr.name);
                var input2 = checkInput(arr.address);
                var input3 = checkInput(arr.select);
                var input4 = checkInput(arr.time);
                var input5 = checkInput(arr.sname);
                var input6 = checkInput(arr.phone);
                if (input1 && input2 && input3 && input4 && input5 && input6) {
                    var url = "${base}/purchase/updateSupplier.do?id=${supplier.id}&supplierName="
                            + arr.name + "&address=" + arr.address + "&supplierType=" + arr.select + "&deliverPeriod="
                            + arr.time + "&salesmanName=" + arr.sname + "&phone=" + arr.phone;
                    $.post(url, function (data) {
                        //重新刷新
                        console.log(data);
                        if (data.code == "0") {
                            swal("提示", "更新成功", "success");
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
