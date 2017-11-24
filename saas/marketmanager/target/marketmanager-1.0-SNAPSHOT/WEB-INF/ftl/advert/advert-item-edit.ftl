<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">广告端管理</a></li>
                            <li class="active"><a href="#">广告管理</a></li>
                        </ol>
                        <h4 class="page-title"><b>广告列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/advert/adverts.do">
                    <div class="form-group m-l-15">
                        <label>广告标题：</label>
                        <input type="text" class="form-control" id="advertNavTitle" placeholder="输入广告标题关键字" name="title">
                    </div>
                    <div class="form-group m-l-15">
                        <label>广告时长：</label>
                        <select id="advertNavTime" class="form-control" name="timeType">
                            <option class="optionTime" value="-1">选择广告时长</option>
                            <option class="optionTime" value="0">15s以内</option>
                            <option class="optionTime" value="1">16s-30s</option>
                            <option class="optionTime" value="2">31s-60s</option>
                            <option class="optionTime" value="3">61s-90s</option>
                            <option class="optionTime" value="4">91s-120s</option>
                            <option class="optionTime" value="5">121s以上</option>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchButton"><i class="fa fa-search"></i> 搜索</button>
                    </div>
                </form>
            </div>

            <button type="button" class="btn btn-primary pull-right" data-toggle="modal" data-target="#uploadModel" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-upload"></i> 上传广告</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>序号</th>
                            <th>广告标题</th>
                            <th>广告时长</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="advertInfoTable">
                        <#if (advertList?size > 0)>
                            <#list advertList as advert>
                            <tr id="tr_${advert_index}">
                                <td>${advert_index +1}</td>
                                <td>${advert.title}</td>
                                <td>${advert.timeSize}</td>
                                <td>${advert.createTime}</td>
                                <td>
                                    <button id="lookBtn" type="button" class="btn btn-primary btn-sm"
                                            data_title="${advert.title}" data_remark="${advert.remark}"
                                             data_time="${advert.timeSize}" data_id="${advert.advertID?c}">查看
                                    </button>
                                    <button id="editBtn" type="button" data-target="#editModel" class="btn btn-primary btn-sm"
                                            data_title="${advert.title}" data_remark="${advert.remark}"
                                            data_time="${advert.timeSize}" data_id="${advert.advertID?c}">编辑
                                    </button>
                                    <button id="deleBtn" type="button" data-target="#deleModel" class="btn btn-danger btn-sm"
                                            data_title="${advert.title}" data_remark="${advert.remark}"
                                            data_time="${advert.timeSize}" data_id="${advert.advertID?c}">删除
                                    </button>
                                </td>
                            </tr>
                            </#list>
                        <#else>
                        <tr>
                            <td colSpan="11" height="200px">
                                <p class="text-center" style="line-height: 200px">暂无任何数据</p>
                            </td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!--分页-->
            <div class="row small_page">
                <div class="col-sm-12">
                <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/advert/adverts.do?title=${title}&timeType=${timeType}"/>
                </div>
            </div>
            <!--/分页-->
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        function checkInput (obj){//检查表单是否有空项，空格验证方法待加
            var val = obj.val();
            if(val == "") {
                return false;
            } else {
                return true;
            }
        }
         //取url参数给表单赋值
        function GetQueryString(name) {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }
        var add_title = GetQueryString("title");

        //鼠标经过效果
        $("tr[id^='tr_']").hover(
            function(){ // onmouseover
                $(this).css("background-color", "#FFFFBF"); // 设置背景颜色
            },
            function(){ // onmouseout
                // 代表当前行对应的checkbox没有选中
                if (!$(this.id.replace("tr_", "")).attr("checked")){
                    $(this).css("background-color", "#FFFFFF"); // 还原背景颜色
                }
            }
        );
        //搜索
        //上传广告
        $("#uploadAdvertButton").on('click', function () {
            $(this).button('loading').delay(500).queue(function() {//阻止二次提交
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var form = new FormData($('#uploadForm')[0]);//表单数据，序列化
                console.log(form.get("file"));
                var input1 = checkInput($("#modalAdvertTitle"));
                var input2 = checkInput($("#modalAdvertTime"));
                var input3 = checkInput($("#modalAdvertRemark"));
                var input4 = checkInput($("#modalAdvertFile"));
                if (input1 && input2 && input3 && input4) {
                    swal({
                        title: "文件正在上传中…",
                        text: "文件越大,用时稍长,请耐心等待!",
                        imageUrl: "${res}/assets/images/gif/timg1.gif",
                        animation: "slide-from-top",
                        showConfirmButton: false,
                    });
                    $.ajax({
                        type: "POST",
                        url: "${base}/advert/addAdvert.do",
                        data: form,
                        // 告诉jQuery不要去处理发送的数据
                        processData: false,
                        // 告诉jQuery不要去设置Content-Type请求头
                        contentType: false,
                        success: function (result) {
                            if ('yes' == result) {
                                $("#uploadModel").modal('hide');
                                swal("提示", "上传成功", "success");
                                setTimeout(function () {
                                    location.reload();
                                }, 1000);
                            } else {
                                swal("提示", "上传失败！请检查重试", "error");
                            }
                        },
                        error: function (result) {
                            swal("提示", "请求失败", "info");
                        }
                    });
                } else {
                    swal("提示", "请检查表单是否有漏填项！", "info");
                }
            });
        });

        //删除
    <#if (advertList?size > 0)>
        $("#advertInfoTable").find('button[id=deleBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                console.log($(that).attr("data_id"));
                $("#deleModel").modal('show');
                $("#deleOkBtn").on('click',function () {
                    $.post("${base}/advert/delAdvert.do?advertID=" + $(that).attr("data_id"), function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            $("#deleModel").modal('hide');
                            swal("提示", "删除成功", "success");
                            setTimeout(function(){location.reload();},1000);
                        } else {
                            swal("提示", "删除失败", "error");
                        }
                    }, "json");
                });
            });
        });
    </#if>
        //编辑
    <#if (advertList?size > 0)>
        $("#advertInfoTable").find('button[id=editBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                $("#editModel").modal('show');
                $("#editTitle").val($(that).attr("data_title"));
                $("#editTime").val($(that).attr("data_time"));
                $("#editRemark").val($(that).attr("data_remark"));
                $("#noButton").on('click',function () {
                    $("#editModel").modal('hide');
                });
                $("#yesButton").on('click',function () {
                    $(this).button('loading').delay(500).queue(function() {
                        $(this).button('reset'); //重置按钮
                        $(this).dequeue();
                        var e_title = $("#editTitle").val();
                        var e_time = $("#editTime").val();
                        var e_remark = $("#editRemark").val();
                        var input1 = checkInput($("#editTitle"));
                        var input2 = checkInput($("#editTime"));
                        var input3 = checkInput($("#editRemark"));
                        if (input1 && input2 && input3) {
                            $.post("${base}/advert/updateAdvert.do?title=" + e_title + "&timeSize=" + e_time + "&remark=" + e_remark + "&advertID=" + $(that).attr("data_id"), function (data) {
                                //重新刷新
                                if (data.code == "0") {
                                    $("#editModel").modal('hide');
                                    swal("提示", "编辑成功", "success");
                                    setTimeout(function () {
                                        location.reload();
                                    }, 1000);
                                } else {
                                    swal("提示", "编辑失败", "error");
                                }
                            }, "json");
                        } else {
                            swal("提示", "请检查表单是否有漏填项！", "info");
                        }
                    });
                });
            });
        });
    </#if>
        //查看
    <#if (advertList?size > 0)>
        $("#advertInfoTable").find('button[id=lookBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/advert/goAdvert.do?advertID="+$(that).attr("data_id")});
            });
        });
    </#if>
    });
</script>

<!--上传弹窗-->
<div class="modal fade" id="uploadModel" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">上传广告</h4>
            </div>
            <div class="modal-body" role="form">
                <div class="modalAdvertStyle">
                    <form class="form-horizontal" id="uploadForm" enctype="multipart/form-data">
                        <div class="form-group">
                            <label class="col-md-4 control-label">广告标题：</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" id="modalAdvertTitle" placeholder="输入广告标题" name="title"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">广告时长：</label>
                            <div class="col-md-6">
                                <input type="text" class="form-control" id="modalAdvertTime" placeholder="输入广告时长(单位/秒)" name="timeSize"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">广告备注：</label>
                            <div class="col-md-6">
                                <input type="text" style="width: 80%" class="form-control" id="modalAdvertRemark" placeholder="输入广告备注" name="remark"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件：</label>
                            <div class="col-md-6">
                                <input class="form-control" id="modalAdvertFile" type="file" multiple="multiple" name="file"/>
                            </div>
                        </div>
                        <div class="modal-footer" style="text-align: center">
                            <button type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary" style="padding:10px 80px " id="uploadAdvertButton">确定</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!--编辑弹窗-->
<div class="modal fade" id="editModel" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">编辑广告</h4>
            </div>
            <div class="modal-body">
                <div class="modalAdvertStyle">
                    <form class="form-inline">
                        <div class="form-group" style="width: 100%">
                            <label>广告标题：</label>
                            <input type="text" style="width: 80%" class="form-control" id="editTitle" placeholder="输入广告标题"/>
                        </div>
                    </form>
                    <form class="form-inline">
                        <div class="form-group">
                            <label>广告时长：</label>
                            <input type="text" class="form-control" id="editTime" placeholder="输入广告时长"/>&nbsp;&nbsp;&nbsp;s (以秒为计算单位)
                        </div>
                    </form>
                    <form class="form-inline">
                        <div class="form-group" style="width: 100%">
                            <label>广告备注：</label>
                            <input type="text" style="width: 80%" class="form-control" id="editRemark" placeholder="输入广告备注"/>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="noButton" type="Button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="yesButton" type="Button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>
<!--删除弹窗-->
<div class="modal fade" id="deleModel" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">删除广告</h4>
            </div>
            <div class="modal-body">
                <p style="text-align: center">确定要删除该广告吗？</p>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="deleNoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="deleOkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>
