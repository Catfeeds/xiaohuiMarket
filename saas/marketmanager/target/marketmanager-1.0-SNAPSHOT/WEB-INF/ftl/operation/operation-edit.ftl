<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/address/address.js"></script>

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>

<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=lbVUzs9wHA7D61mfQjvrr7CxV97fbML7"></script>


<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">权限管理</a></li>
                            <li class="active"><#if operation?exists>编辑<#else>添加</#if>店铺</li>
                        </ol>
                        <h4 class="page-title"><b><#if operation?exists>编辑<#else>添加</#if>店铺</b></h4>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <div class="row m-t-30">
                        <div class="col-md-8">
                            <form class="form-horizontal" role="form" onkeypress="if(event.keyCode==13) {$('#saveBtn').click();return false;}" method="POST" action="${base}/operation/" id="formDefault">
                                <div class="form-group">
                                    <label class="col-md-4 control-label">权限标题：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="operationKey" class="form-control" <#if operation?exists> value="${operation.operationKey}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">权限名称：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="operationValue" class="form-control" <#if operation?exists> value="${operation.operationValue}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">上级权限：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="fatherId" class="form-control" <#if operation?exists> value="${operation.fatherId?c}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">请求地址：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="url" class="form-control" <#if operation?exists> value="${operation.url}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">描述：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="explain" class="form-control" <#if operation?exists> value="${operation.explain!""}" </#if>>
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
                </div>
            </div>

            <!-- end container -->
        </div>


        <script type="text/javascript">
            $(document).ready(function () {

                //临时内容
                var _operationId = <#if operation?exists> ${operation.id?c};<#else> 0;</#if>


                $("#backBtn").on('click', function () {
                    history.back(-1);
                });

//                function getVs(ui_name, info) {
//                    var obj = $("#"+ui_name).find("option:selected");
//                    var id = obj.attr("data_id");
//                    if(id == "0") {
//                        swal(info);
//                        return null;
//                    }
//                    return {id:id,name:obj.attr("data_v")};
//                }

                $("#saveBtn").on('click', function () {

                    var _operationKey = checkVal("operationKey", "请输入权限标题");
                    if(_operationKey == null) return;

                    var _operationValue = checkVal("operationValue", "请输入权限名称");
                    if(_operationValue == null) return;

//                    var _fatherId = checkVal("fatherId", "请输入上级权限");
//                    if(_fatherId == null) return;
                    var _fatherId = $("#fatherId").val();

                    var _url = $("#url").val();
//                    var _url = checkVal("url", "请输入请求地址");
//                    if(_url == null) return;
                    var _explain = $("#explain").val();
//                    var _explain = checkVal("explain", "请输入描述信息");
//                    if(_explain == null) return;


                    var post_data = {
                        operationId:_operationId,
                        operationKey: _operationKey,
                        operationValue:_operationValue,
                        fatherId:_fatherId,
                        url:_url,
                        explain : _explain,
                    };

                    var btn_ = $(this);
                    btn_.attr("disabled", true); // 限制网络卡，多次提交

                    tokenPresPost("${base}/operation/operationEditSave.do", post_data, function (data) {
                        //重新刷新
                        if(data.code == "0") {
                            showSuccess(data.msg, function () {
                                var url = "${base}/operation/operations.do?";
                                open({ url: url });
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