<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<link href="${res}/assets/plugins/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
<link href="${res}/assets/plugins/zTree/css/demo.css" rel="stylesheet" type="text/css">
<script src="${res}/assets/plugins/zTree/js/jquery.ztree.core.js"></script>
<script src="${res}/assets/plugins/zTree/js/jquery.ztree.excheck.js"></script>
<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">角色管理</a></li>
                            <li class="active"><a href="#">角色列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>角色列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <button id="addBtn" type="button"
                                    class="btn waves-effect waves-light btn-primary"><i class="fa fa-plus"> 添加角色</i>
                            </button>
                            <div class="input-group m-l-15">
                                <input type="text" id="searchKeyTxt"
                                       class="form-control" placeholder="输入权限名称搜索">
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
                            <th>角色标题</th>
                            <th>角色名称</th>
                            <th>禁用状态</th>
                            <th>说明</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="roleListTable">

                        <#if (datas?size > 0) >
                            <#list datas as d>
                            <tr>
                                <td>${d.id?c}</td>
                                <td>${(d.roleTitle)!}</td>
                                <td>${(d.roleName)!}</td>
                                <td><#if d.status=-1>禁用<#else>开启</#if></td>
                                <td>${(d.explain)!}</td>
                                <td>${d.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                                <td>
                                    <button id="addUserBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${d.id?c}">分配用户
                                    </button>
                                    <button id="setBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${d.id?c}">设置权限
                                    </button>
                                    <button id="editBtn" type="button"
                                            class="btn waves-effect waves-light btn-warning btn-sm"
                                            data_id="${d.id?c}">编辑
                                    </button>
                                    <button id="delBtn" type="button"
                                            class="btn waves-effect waves-light btn-pink btn-sm"
                                            data_id="${d.id?c}">删除
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

        <#if (datas?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/role/roles.do?"/>
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>
    </div>
</div>

<script type="text/javascript" src="${res}/assets/plugins/sweetalert/center-loader.js"></script>
<script type="text/javascript">
    var setting = {
        check: {
            enable: true
        },
        async: {
            enable: true,
            url: "${base}/role/getAllOperation.do", //异步加载时的请求地址
            autoParam: ["id"], //提交参数
            type: 'get',
            dataType: 'json'
        },
        checkable: true,
        showIcon: true,
        showLine: true,
        data: {
            simpleData: {
                enable: true
            }
        },
        expandSpeed: "",
        callback: {
            onClick: onCheck
        }
    };
    function onCheck(e,treeId,treeNode){}
    function checkInput (obj){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {return false;} else {return true;}
    }
    $(document).ready(function () {
        var _roleListTable = $("#roleListTable");
        $("#addBtn").on('click', function () {//添加
            //直接跳转
            open({url: "${base}/role/roleEdit.do" });
        });
    <#if (datas?size > 0)>
        _roleListTable.find('button[id=addUserBtn]').each(function () {//分配用户
            $(this).on('click', function () {
                open({url: "${base}/role/userRoleSet.do?roleId=" + $(this).attr("data_id")});
            });
        });
    </#if>
    <#if (datas?size > 0)>
        _roleListTable.find('button[id=setBtn]').each(function () {//设置权限
            $(this).on('click', function () {
                <#--open({url: "${base}/role/roleOperaSet.do?roleId=" + $(this).attr("data_id")});-->
                var _roleId = $(this).attr("data_id");
                $("#assignModal").modal('show');
                $("#treeDemo").loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
                $.ajax({
                    url: '${base}/role/getAllOperation.do?roleId='+_roleId,
                    type: 'get',
                    dataType: 'json',
                    success: function (data) {
                        var str = "[";
                        $.each(data.response,function(i,opera){
                            //var openV= (opera.fatherId<=0?"open:true":"open:false");
                            var checked = (opera.checked>0?"checked:true":"checked:false");
                            str = str + '{ id:\''+opera.id+'\', pId:\''+opera.fatherId+ '\', name:\'' +opera.operationKey+ '\',open:true,'+checked+'},';
                        });
                        str=str.substring(0,str.length-1);
                        str = str + "]"; //此时str是JSON字符串
                        var msg = eval('(' + str + ')');//将字符串转换为json对象
                        $.fn.zTree.init($("#treeDemo"), setting, msg);
                    }
                });
                $("#okBtn").on('click',function () {
                    $(this).button('loading').delay(500).queue(function() {//阻止二次提交
                        $(this).button('reset'); //重置按钮
                        $(this).dequeue();
                        var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
                                nodes=treeObj.getCheckedNodes(true),
                                operationId="";
                        for(var i=0;i<nodes.length;i++){
                            operationId+=nodes[i].id + ",";//获取选中节点的值
                        }
                        operationId=operationId.substring(0,operationId.length-1);

                        var arr = {
                            "operationIds": operationId,
                            "roleId": _roleId,
                        };
                        var input1 = checkInput(arr.roleId);
                        if (input1) {
                            var url = "${base}/role/roleOperaSetAdd.do?roleId="
                                    + arr.roleId + "&operationIds=" + arr.operationIds ;
                            $.post(url, function (data) {
                                //重新刷新
                                console.log(data);
                                if (data.code == "0") {
                                    swal("提示", "设置成功", "success");
                                    setTimeout(function () {
                                        location.href = "${base}/role/roles.do";
                                    }, 1000);
                                } else {
                                    swal("提示", data.msg, "error");
                                }
                            }, "json");
                        } else {
                            swal("提示", "请至少选择分配一种权限！", "info");
                        }
                    });
                });
            });
        });
    </#if>

        <#if (datas?size > 0)>
            _roleListTable.find('button[id=editBtn]').each(function () {//编辑
                $(this).on('click', function () {
                    open({url: "${base}/role/roleEdit.do?roleId=" + $(this).attr("data_id")});
                });
            });
        </#if>

        _roleListTable.find('button[id=delBtn]').each(function () {//删除
            $(this).on('click', function () {
                var post_data = {
                    roleId:$(this).attr("data_id"),
                };
                tokenPresPost("${base}/role/roleDel.do", post_data, function (data) {
                    //重新刷新
                    if(data.code == "0") {
                        swal("提示", "删除成功", "success");
                        setTimeout(function () {
                            location.reload()
                        }, 1000);
                    } else {
                        swal("提示", data.msg, "error");
                    }
                }, "json");
            });
        });
    });
</script>
<!--权限设置弹窗-->
<div class="modal fade" id="assignModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">权限设置</h4>
            </div>
            <div class="modal-body">
                <div class="zTreeDemoBackground right">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="noBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="okBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>