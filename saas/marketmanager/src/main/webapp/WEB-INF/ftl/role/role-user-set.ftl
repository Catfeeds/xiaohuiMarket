<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="content-page">
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
                        <h4 class="page-title "><b>分配用户</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" id="returnBtn" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
            <div class="card-box">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>角色信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                <#if role?exists>
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">角色名称</td>
                                        <td><#if role?exists> ${role.roleTitle} </#if></td>
                                        <td style="background-color: #f9f9f9">角色类型</td>
                                        <td><#if role?exists> ${role.roleName} </#if></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">角色状态</td>
                                        <td>
                                         <#if role?exists>
                                            <#if role.status=0>正常
                                            <#elseif role.status=-1>禁用
                                            </#if>
                                        </#if>
                                        </td>
                                        <td style="background-color: #f9f9f9">描述</td>
                                        <td><#if role?exists> ${role.explain!""} </#if></td>
                                    </tr>
                                    </tbody>
                                </#if>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <ol class="breadcrumb pull-right">
                                <li><button class="btn-primary" id="setUserBtn">分配用户</button></li>
                            </ol>
                            <h5 class="page-title" style="padding-top: 20px"><b>用户信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th style="background-color: #f9f9f9">用户ID</th>
                                        <th style="background-color: #f9f9f9">用户名</th>
                                        <th style="background-color: #f9f9f9">用户昵称</th>
                                        <th style="background-color: #f9f9f9">手机号</th>
                                        <th style="background-color: #f9f9f9">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="tab">
                                    <#if passports?exists>
                                        <#list passports as passport>
                                        <tr>
                                            <td>${passport.id?c}</td>
                                            <td>${(passport.realName)!}</td>
                                            <td>${passport.showName}</td>
                                            <td>${passport.phoneNumber}</td>
                                            <td><button type="button" class="btn-danger" onclick="deleTr(this);">删除</button></td>
                                        </tr>
                                        </#list>
                                    </#if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="m-t-40">
                                <button id="saveBtn" type="button" class="btn btn-primary col-md-1 statusBtn pull-right">提交</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${res}/assets/plugins/sweetalert/center-loader.js"></script>
<script type="text/javascript" src="${res}/assets/plugins/tableCheckbox/jquery.tableCheckbox.js"></script>
<script src="${res}/assets/pages/bootstrap-paginator.min.js"></script>
<script type="text/javascript">
    function deleTr(nowTr) {
        var trlen = $("#tab").find("tr").length;
        if(trlen > 0){
            $(nowTr).parent().parent().remove();
        }
    }
    $(document).ready(function () {
        //编辑广告
        $("#setUserBtn").on('click',function () {
            $("#checkModal").modal('show');
            var val = $("#userSelect").val();
            var $userTab = $("#userInfoTable");
            $userTab.loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
            var url = "${base}/role/passportWithAppointPower.do?pageIndex=1&pageSize=10&roleType=";//接口地址
            $.get(url + val,function(data) {
                var _data = data.response.datas;
                if(data.code==0) {//返回的数据不为空
                    var html = '';
                    for(var i in _data){
                        html +='<tr id="tr_'+ i + '"' +'>' +
                                '<td><input type="checkbox" value="0"></td>' +
                                '<td>' + _data[i].id + '</td>' +
                                '<td>' + _data[i].realName + '</td>' +
                                '<td>' + _data[i].showName + '</td>' +
                                '<td>' + _data[i].phoneNumber + '</td>' +
                                '</tr>';
                    }
                    $userTab.html(html);
                    $("#checkTable").tableCheckbox({ /* options */ });//全选与反选
                    $('#pageLimit').bootstrapPaginator({
                        currentPage: 1,//当前所在页
                        totalPages: data.response.totalPage,//总页数
                        size: "normal",//页码按钮大小
                        bootstrapMajorVersion: 3,//bootstrap的版本要求
                        alignment: "right",//向右排序
                        numberOfPages: 10,//每页显示条数
                        itemTexts: function (type, page, current) {
                            switch (type) {
                                case "first": return "首页";
                                case "prev": return "上一页";
                                case "next": return "下一页";
                                case "last": return "末页";
                                case "page": return page;
                            }//默认显示的是第一页
                        },
                        onPageClicked: function (event, originalEvent, type, page){//page为当前点击的页码值
                            $("#checkAll").prop("checked",false);
                            $userTab.loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
                            $.ajax({
                                url:url + val,
                                type:'GET',
                                data:{'pageIndex':page,'pageSize':10},
                                dataType:'JSON',
                                success:function (res) {
                                    var _res = res.response.datas;
                                    var _html = '';
                                    for(var i in _res){
                                        _html +='<tr id="tr_'+ i + '"' +'>' +
                                                '<td><input type="checkbox" value="0"></td>' +
                                                '<td>' + _res[i].id + '</td>' +
                                                '<td>' + _res[i].realName + '</td>' +
                                                '<td>' + _res[i].showName + '</td>' +
                                                '<td>' + _res[i].phoneNumber + '</td>' +
                                                '</tr>';
                                    }
                                    $userTab.html(_html);
                                    $("#checkTable").tableCheckbox({ /* options */ });//全选与反选
                                },
                                error:function(res){
                                    swal("提示", res.msg, "error");
                                    return;
                                }
                            })
                        }
                    });
                } else {//返回的数据为空
                    swal("提示", data.msg, "error");
                    return;
                }
            }, "json");
        });
        $("#userSelect").change(function(){
            var _val = $(this).val();
            var $userTab = $("#userInfoTable");
            var url = "${base}/role/passportWithAppointPower.do?roleType=";
            $userTab.loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
            $.get(url+ _val,function(data) {
                console.log(data);
                var _data = data.response.datas;
                if(data.code==0) {//返回的数据不为空
                    var html = '';
                    $userTab.empty();
                    for(var i in _data){
                        html +='<tr id="tr_'+ i + '"' +'>' +
                                '<td><input type="checkbox" value="0"></td>' +
                                '<td>' + _data[i].id + '</td>' +
                                '<td>' + _data[i].realName + '</td>' +
                                '<td>' + _data[i].showName + '</td>' +
                                '<td>' + _data[i].phoneNumber + '</td>' +
                                '</tr>';
                    }
                    $userTab.html(html);
                    $("#checkTable").tableCheckbox({ /* options */ });//全选与反选
                    $('#pageLimit').bootstrapPaginator({
                        currentPage: 1,
                        totalPages: data.response.totalPage,
                        size: "normal",
                        bootstrapMajorVersion: 3,
                        alignment: "right",
                        numberOfPages: 10,
                        itemTexts: function (type, page, current) {
                            switch (type) {
                                case "first": return "首页";
                                case "prev": return "上一页";
                                case "next": return "下一页";
                                case "last": return "末页";
                                case "page": return page;
                            }//默认显示的是第一页
                        },
                        onPageClicked: function (event, originalEvent, type, page){//page为当前点击的页码值
                            $("#checkAll").prop("checked",false);
                            $userTab.loader('show','<i class="fa fa-2x fa-spinner fa-spin"></i>');
                            $.ajax({
                                url:'${base}/role/passportWithAppointPower.do?propertiesType=1&roleType=' + _val,
                                type:'GET',
                                data:{'pageIndex':page,'pageSize':10},
                                dataType:'JSON',
                                success:function (res) {
                                    var _res = res.response.datas;
                                    var _html = '';
                                    for(var i in _res){
                                        _html +='<tr id="tr_'+ i + '"' +'>' +
                                                '<td><input type="checkbox" value="0"></td>' +
                                                '<td>' + _res[i].id + '</td>' +
                                                '<td>' + _res[i].realName + '</td>' +
                                                '<td>' + _res[i].showName + '</td>' +
                                                '<td>' + _res[i].phoneNumber + '</td>' +
                                                '</tr>';
                                    }
                                    $userTab.html(_html);
                                    $("#checkTable").tableCheckbox({ /* options */ });//全选与反选
                                },
                                error:function(res){
                                    swal("提示", res.msg, "error");
                                    return;
                                }
                            })
                        }
                    });
                } else {//返回的数据为空
                    swal("提示", data.msg, "error");
                    return;
                }
            }, "json");
        });
        $("#checkYesBtn").on('click',function() {//选择确定
            var data = [];//声明一个数组
            var $tab = $("#tab");
            var t = $("#checkTable tbody tr").length;
            $("#checkTable tbody tr:lt(" + t + ")").each(function () { //获取行号，从第t行开始遍历
                if ($(this).find("td:eq(0) input").prop("checked") == true) { //只遍历checkbox选中的
                    var param = {};//声明一个json
                    var id = $(this).find("td:eq(1)").text();
                    id = $.trim(id);//去除首尾空格符
                    param.id = id;
                    var name = $(this).find("td:eq(2)").text();
                    name = $.trim(name);
                    param.name = name;
                    var time = $(this).find("td:eq(3)").text();
                    time = $.trim(time);
                    param.time = time;
                    var remark = $(this).find("td:eq(4)").text();
                    remark = $.trim(remark);
                    param.remark = remark;
                    data.push(param);//将封装好的json添加到数组
                }
            });
            console.log(data);
            var error = checkElement(data);
            if (error){return;}
            if(data.length != 0) {//数据不为空
                var trHtml = "";
                console.log(data.length);
                for(var i = 0; i < data.length; i++) {
                    trHtml += "<tr><td>" + data[i].id + "</td><td>" + data[i].name + "</td><td>" + data[i].time + "</td><td>" + data[i].remark
                            + "</td><td><button type='button' class='btn-danger' onclick='deleTr(this)'>删除</button></td></tr>";
                }
                $tab.last().append(trHtml);
                $("#checkModal").modal('hide');
            }
            function checkElement(data) {
                if (data.length < 1) {
                    swal("提示", "至少选择一项", "error");
                    return;
                }
            }
        });
        $("#saveBtn").on('click',function () {
            var itemID = "";//列表id
            $("#tab tr").each(function() {
                var _txt = $(this).children("td:eq(0)").text();
                itemID += _txt + ",";
            });
            itemID = itemID.length > 0 ? itemID.substring(0, itemID.length - 1) : "";
            $.post("${base}/role/userRoleSetAdd.do?roleId=${role.id?c}&passportIds=" + itemID, function (data) {//提交
                //重新刷新
                console.log(data);
                if (data.code == "0") {
                    swal("提示", "添加成功", "success");
                    setTimeout(function () {
                        history.back();
                    }, 1000);
                } else {
                    swal("提示", data.msg, "error");
                }
            }, "json");
        });
    });
</script>
<!--分配用户弹窗-->
<div class="modal fade" id="checkModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">选择用户</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" style="margin-bottom: 20px">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">用户类型：</label>
                        <div class="col-sm-7">
                            <select class="form-control" id="userSelect">
                                <option value="21">补货员</option>
                                <option value="31">快递员</option>
                                <option value="41">后台管理员</option>
                            </select>
                        </div>
                    </div>
                </form>
                <div class="modalAdvertStyle">
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-striped table-bordered" id="checkTable">
                                <thead class="table_head">
                                <tr>
                                    <th><input id="checkAll" type="checkbox" value=""/>全选</th>
                                    <th>用户ID</th>
                                    <th>用户名</th>
                                    <th>用户昵称</th>
                                    <th>手机号</th>
                                </tr>
                                </thead>
                                <tbody id="userInfoTable">

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <form class="form-horizontal pull-right">
                    <ul id="pageLimit"></ul>
                </form>
            </div>
            <div class="modal-footer" style="text-align: center;margin-top: 50px">
                <button type="button" id="checkNoBtn" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button type="button" id="checkYesBtn" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>