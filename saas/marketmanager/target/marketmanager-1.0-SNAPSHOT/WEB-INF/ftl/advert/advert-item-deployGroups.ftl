<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">广告管理</a></li>
                            <li class="active"><a href="#">分组管理</a></li>
                        </ol>
                        <h4 class="page-title "><b>配置广告</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
            <div class="card-box">
                <div class="row">
                    <div class="col-sm-4">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>分组信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">分组名称</td>
                                        <td>
                                            <input type="text" class="form-control" id="groupName" value="${group.groupName}" disabled="disabled"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <ol class="breadcrumb pull-right">
                                <li><button class="btn-primary" id="deployBtn" data-toggle="modal" data-target="#checkModal">分配广告</button></li>
                            </ol>
                            <h5 class="page-title" style="padding-top: 20px"><b>广告信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th style="background-color: #f9f9f9">广告ID</th>
                                        <th style="background-color: #f9f9f9">广告名称</th>
                                        <th style="background-color: #f9f9f9">广告时长</th>
                                        <th style="background-color: #f9f9f9">备注</th>
                                        <th style="background-color: #f9f9f9">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="tab">
                                    </tbody>
                                </table>
                            </div>
                            <div class="m-t-40">
                                <div class="pull-right">
                                    <button id="saveBtn" type="button" class="btn btn-primary statusBtn pull-right">提交</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${res}/assets/plugins/tableCheckbox/jquery.tableCheckbox.js"></script>
<script type="text/javascript">
    function deleTr(nowTr) {
        var trlen = $("#tab").find("tr").length;
        if(trlen > 0){
            $(nowTr).parent().parent().remove();
        }
    }
    $(document).ready(function () {
        //配置广告
        $("#checkTable").tableCheckbox({ /* options */ });//全选与反选
        $("#checkYesBtn").on('click',function() {//选择确定
            var data = [];//声明一个数组
            var $tab = $("#tab");
            var t = $("#checkTable tbody tr").length;
            $("#checkTable tbody tr:lt(" + t + ")").each(function () { //获取行号，从第t行开始遍历
                if ($(this).find("td:eq(0) input").prop("checked") == true) { //只遍历checkbox选中的
                    var param = {};//声明一个json
                    param.id = $(this).find("td:eq(1)").text();//开始json的键值对赋值
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
            $.post("${base}/advert/saveAdverGroup.do?groupID=${group.id}&advertIDs=" + itemID, function (data) {//提交
                //重新刷新
                console.log(data);
                if (data.code == "0") {
                    swal("提示", "添加成功", "success");
                    setTimeout(function () {
                        location.href="${base}/advert/groupPage.do"
                    }, 1000);
                } else {
                    swal("提示", data.msg, "error");
                }
            }, "json");
        });
    });
</script>
<!--配置广告弹窗-->
<div class="modal fade" id="checkModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">选择广告</h4>
            </div>
            <div class="modal-body" style="height: 680px;overflow:hidden;overflow-y:auto;">
                <div class="modalAdvertStyle">
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-striped table-bordered" id="checkTable">
                                <thead class="table_head">
                                <tr>
                                    <th><input id="checkAll" type="checkbox" value=""/>全选</th>
                                    <th>ID</th>
                                    <th>广告标题</th>
                                    <th>广告时长</th>
                                    <th>备注</th>
                                </tr>
                                </thead>
                                <tbody id="advertInfoTable">
                                <#if (adverts?size > 0)>
                                    <#list adverts as advert>
                                    <tr id="tr_${advert_index}">
                                        <td style="text-align: center"><input type="checkbox" value="${advert_index}"/></td>
                                        <td style="text-align: center">${advert.id}</td>
                                        <td style="text-align: center">${advert.title}</td>
                                        <td style="text-align: center">${advert.timeSize} s</td>
                                        <td style="text-align: center">${advert.remark}</td>
                                    </tr>
                                    </#list>
                                <#else>
                                    <tr>
                                        <td colSpan="11" height="200px">
                                            <p class="text-center" style="line-height: 200px">暂无任何广告</p>
                                        </td>
                                    </tr>
                                </#if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button type="button" id="checkNoBtn" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button type="button" id="checkYesBtn" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>