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
                            <li class="active"><#if role?exists>编辑<#else>添加</#if>店铺</li>
                        </ol>
                        <h4 class="page-title"><b><#if role?exists>编辑<#else>添加</#if>店铺</b></h4>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <div class="row m-t-30">
                        <div class="col-md-8">
                            <form class="form-horizontal" role="form" onkeypress="if(event.keyCode==13) {$('#saveBtn').click();return false;}" method="POST" action="${base}/role/" id="formDefault">
                                <div class="form-group">
                                    <label class="col-md-4 control-label">角色标题：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="roleTitle" class="form-control" <#if role?exists> value="${role.roleTitle}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">角色名称：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="roleName" class="form-control" <#if role?exists> value="${role.roleName}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">禁用状态：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="status" class="form-control" <#if role?exists> value="${role.status?c}" </#if>>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label">描述：</label>
                                    <div class="col-md-8">
                                        <input type="text" id="explain" class="form-control" <#if role?exists> value="${role.explain!""}" </#if>>
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
                var _roleId = <#if role?exists> ${role.id?c};<#else> 0;</#if>


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

                    var _roleTitle = checkVal("roleTitle", "请输入角色标题");
                    if(_roleTitle == null) return;

                    var _roleName = checkVal("roleName", "请输入角色名称");
                    if(_roleName == null) return;

                    var _status = checkVal("status", "请输入禁用状态");
                    if(_status == null) return;

                    var _explain = checkVal("explain", "请输入描述信息");
                    if(_explain == null) return;


                    var post_data = {
                        roleId:_roleId,
                        roleTitle:_roleTitle,
                        roleName: _roleName,
                        status:_status,
                        explain : _explain,
                    };

                    var btn_ = $(this);
                    btn_.attr("disabled", true); // 限制网络卡，多次提交

                    tokenPresPost("${base}/role/roleEditSave.do", post_data, function (data) {
                        //重新刷新
                        if(data.code == "0") {
                            showSuccess(data.msg, function () {
                                var url = "${base}/role/roles.do?";
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