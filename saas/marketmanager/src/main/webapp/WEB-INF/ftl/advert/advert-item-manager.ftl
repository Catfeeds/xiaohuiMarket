<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">
<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>
<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12 ">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">广告端管理</a></li>
                            <li class="active"><a href="#">投放广告</a></li>
                        </ol>
                        <h4 class="page-title"><b>投放管理</b></h4>
                    </div>
                </div>
            </div>

            <div class="" style="padding:0px 20px 20px 20px">
                <button id="adPlayBtn" type="button" class="btn btn-primary btn-lg"><i class="fa fa-play-circle"></i> 广告播放</button>
                <button id="adScreenEditBtn" type="button" class="btn btn-default btn-lg"><i class="fa fa-edit"></i> 屏幕配置</button>
            </div>
            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/advert/advertScreens.do">
                    <div class="form-group m-l-15">
                        <label>门店信息:</label>
                        <select class="form-control" id="marketSelect" name="marketID">
                            <option value="-1">全部店铺</option>
                        <#if markets?exists >
                            <#list markets as market>
                                <option value="${market.id?c}" <#if marketID?exists && marketID == market.id>selected</#if>>
                                    ${market.name}
                                </option>
                            </#list>
                        </#if>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <label>屏幕编号:</label>
                        <input type="text" class="form-control" id="screenNum" placeholder="输入编号" name="code" value="${code}">
                    </div>
                    <div class="form-group m-l-15">
                        <label>广告分组:</label>
                        <input type="text" class="form-control" id="advertNavTitle" placeholder="输入广告分组" name="title" value="${title}">
                    </div>
                    <div class="form-group m-l-15">
                        <label>日期:</label>
                        <div class="input-group">
                            <input id="startTime" type="text" class="form-control" name="beginTime" value="${beginTime}" readonly>
                            <span class="input-group-addon bg-default"
                                  onClick="jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD 00:00'})"><i
                                    class="fa fa-calendar"></i></span>
                        </div>
                        <#--<label> &nbsp;至&nbsp; </label>-->
                        <#--<div class="input-group">-->
                            <#--<input id="endTime" type="text" class="form-control" name="endTime" readonly>-->
                            <#--<span class="input-group-addon bg-default"-->
                                  <#--onClick="jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD 00:00'})"><i-->
                                    <#--class="fa fa-calendar"></i></span>-->
                        <#--</div>-->
                    </div>
                    <#--<div class="form-group m-l-15">-->
                        <#--<label>是否下载:</label>-->
                        <#--<select class="form-control" id="isdownSelect" name="isDown">-->
                            <#--<option value="-1"<#if isDown == -1>selected</#if>>全部</option>-->
                            <#--<option value="0"<#if isDown == 0>selected</#if>>否</option>-->
                            <#--<option value="1"<#if isDown == 1>selected</#if>>是</option>-->
                        <#--</select>-->
                    <#--</div>-->
                    <#--<div class="form-group m-l-15">-->
                        <#--<label>播放状态:</label>-->
                        <#--<select class="form-control" id="statusSelect" name="playStatus">-->
                            <#--<option value="-1"<#if playStatus == -1>selected</#if>>全部</option>-->
                            <#--<option value="0"<#if playStatus == 0>selected</#if>>待播放</option>-->
                            <#--<option value="1"<#if playStatus == 1>selected</#if>>播放中</option>-->
                            <#--<option value="2"<#if playStatus == 2>selected</#if>>已停止</option>-->
                            <#--<option value="3"<#if playStatus == 3>selected</#if>>已移除</option>-->
                        <#--</select>-->
                    <#--</div>-->
                    <div class="form-group  m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i> 搜索 </button>
                    </div>
                </form>
            </div>

            <button type="button" class="btn btn-primary pull-right m-b-15" data-toggle="modal" id="addPlayButton" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-plus"></i> 添加广告播放</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>序号</th>
                            <th>门店信息</th>
                            <th>屏幕编号</th>
                            <th>广告分组</th>
                            <th>播放类型</th>
                            <th>开始时间</th>
                        <#--<th>结束时间</th>-->
                            <th>是否为默认广告</th>
                            <th>播放状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="storeInfoListTable">
                        <#if (advertScreens?size > 0)>
                            <#list advertScreens as advert>
                            <tr id="tr_${advert_index}">
                                <td>${advert_index +1}</td>
                                <td>${advert.marketName}</td>
                                <td>${advert.sCode}</td>
                                <td>${advert.groupName}</td>
                                <#if advert.style=1>
                                    <td>顺序播放</td>
                                    <td>--</td>
                                <#--<td>--</td>-->
                                <#elseif advert.style=2>
                                    <td>插播</td>
                                    <td>${advert.beginTime}</td>
                                <#--<td>${advert.endTime}</td>-->
                                </#if>
                                <#if advert.playStatus=0>
                                    <td>否</td>
                                <#elseif advert.playStatus=1>
                                    <td class="text-danger">是</td>
                                </#if>
                                <td>
                                    <#if advert.style=1><span class="label label-primary">播放中</span>
                                    <#elseif advert.style=2>
                                        <#if advert.beginTime?datetime lte .now?datetime><#--大于 gt, 大于或等于 gte ,小于 lt , 小于或等于 lte-->
                                            <span class="label label-success">已停止</span>
                                        <#else>
                                            <span class="label label-default">待播放</span>
                                        </#if>
                                    </#if>
                                </td>
                                <td>
                                    <#if advert.playStatus=0>
                                        <button id="lookBtn" type="button"
                                                class="btn btn-primary btn-sm" data_id="${advert.id}"
                                                a_id="${advert.advertID?c}" m_id="${advert.marketID}" s_id="${advert.screenID?c}">查看
                                        </button>
                                        <button id="editBtn" type="button" data-target="#editModel"
                                                class="btn btn-primary btn-sm" data-toggle="model" data_id="${advert.id}"
                                                a_id="${advert.advertID?c}" m_id="${advert.marketID}" s_id="${advert.screenID?c}">编辑
                                        </button>
                                        <button id="deleBtn" type="button" data-target="#deleModel"
                                                class="btn btn-danger btn-sm" data-toggle="model" data_id="${advert.id}"
                                                a_id="${advert.advertID?c}" m_id="${advert.marketID}" s_id="${advert.screenID?c}">删除
                                        </button>
                                    <#elseif advert.playStatus=1>
                                        <button id="lookBtn" type="button"
                                                class="btn btn-primary btn-sm" data_id="${advert.id}"
                                                a_id="${advert.advertID?c}" m_id="${advert.marketID}" s_id="${advert.screenID?c}">查看
                                        </button>
                                        <button id="editBtn" type="button" data-target="#editModel"
                                                class="btn btn-primary btn-sm" data-toggle="model" data_id="${advert.id}"
                                                a_id="${advert.advertID?c}" m_id="${advert.marketID}" s_id="${advert.screenID?c}">编辑
                                        </button>
                                    </#if>
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
                <@paginate nowPage=pageIndex itemCount=count action="${base}/advert/advertScreens.do?marketID=${marketID?c}&code=${code}&title=${title}&beginTime=${beginTime}"/>
                </div>
            </div>
            <!--/分页-->

        </div>
    </div>
</div>
<script type="text/javascript" src="${res}/assets/plugins/jedate/jedate.js"></script>
<script type="text/javascript">
    function checkInput (obj){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {return false;} else {return true;}
    }
    //取url参数给表单赋值
    function GetQueryString(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    $(document).ready(function () {
        $("#marketSelect").select2();
        $("#marketSelect").select2();
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
        //单选按钮组处理
//        $("#isdownBtnGroup").find('button').each(function () {
//            $(this).on('click',function () {
//                console.log(this);
//                if(!$(this).hasClass('btn-primary')){
//                    $("#isdownBtnGroup button").removeClass("btn-primary focus");
//                    $(this).addClass("btn-primary focus");
//                }
//            })
//        });
//        $("#isplayBtnGroup").find('button').each(function () {
//            $(this).on('click',function () {
//                if(!$(this).hasClass('btn-primary')){
//                    $("#isplayBtnGroup button").removeClass("btn-primary action");
//                    $(this).addClass("btn-primary action");
//                }
//            });
//        });
        //编辑
    <#if (advertScreens?size > 0)>
        $("#storeInfoListTable").find('button[id=editBtn]').each(function () {
            var that = this;
            var keyID = $(this).attr("data_id");
            var _id = $(this).attr("a_id");
            $(this).on('click', function () {
                $("#editModel").modal('show');
                $.get("${base}/advert/getAdvertScreen.do?advertID=" +_id+ "&screenID=" +$(that).attr("s_id")+ "&marketID=" +$(that).attr("m_id")+ "&id=" +$(that).attr("data_id"),function(object){//取该行列表全部信息
                    console.log(object);
                    $("#editStoreInfo option:selected").val(object.marketID);
                    $("#editStoreInfo option:selected").text(object.marketName);
                    $("#editScreenNum option:selected").val(object.screenID);
                    $("#editScreenNum option:selected").text(object.sCode);
                    $("#editTitle").find("option[value='"+_id+"']").attr("selected",true);
                    console.log(object.playStatus);
                    if(object.playStatus==0){
                        $("#inlineRadio0").attr("checked",true);
                    }else if(object.playStatus==1){
                        $("#inlineRadio1").attr("checked",true);
                    }
                    if(object.style==1){
                        $("#stdiv").css("display","none");
                        $("#editType").find("option[value=1]").attr("selected",true);
                    }else if(object.style==2){
                        $("#stdiv").css("display","block");
                        $("#editType").find("option[value=2]").attr("selected",true);
                    }
                    $("#editStartTime").val(object.beginTime);
//                        $("#editEndTime").val(object.endTime);
                    $("#editPlaySort").val(object.playOrder);
                    $("#editPlayRemark").val(object.remark);
                    $("#editYesBtn").on('click',function () {
                        var adverts = {
                            "id": keyID,//id
                            "marketID": $("#editStoreInfo").val(),//门店
                            "screenID": $("#editScreenNum").val(),//屏幕编号
                            "advertID": $("#editTitle").val(),//广告分组
                            "style":$("#editType").val(),//播放类型
                            "beginTime": $("#editStartTime").val(),//开始时间
                            "playStatus": $('input:radio[name="isDefaultRadio"]:checked').val(),//是否设为默认广告
                            "playOrder": $("#editPlaySort").val(),//播放顺序
                            "remark": $("#editPlayRemark").val(),//播放备注
                            "sCode": $("#editScreenNum option:selected").text()//屏幕sCode
                        };
                        console.log(adverts.screenID);
                        var input1 = checkInput(adverts.marketID);
                        var input2 = checkInput(adverts.screenID);
                        var input3 = checkInput(adverts.advertID);
                        var input4 = checkInput(adverts.beginTime);
                        var input5 = checkInput(adverts.endTime);
                        var input6 = checkInput(adverts.playOrder);
                        var input7 = checkInput(adverts.style);
                        var input8 = checkInput(adverts.sCode);
                        if(adverts.style=="2"){
                            if (input4 && input6 == true) {
                                $.ajax({
                                    type: "POST",
                                    url: "${base}/advert/updateScreenAdvert.do",
                                    data: adverts,
                                    success: function (data) {
                                        console.log(data);
                                        if (data.code == '0') {
                                            swal("提示", "更新成功", "success");
                                            setTimeout(function () {
                                                location.reload();
                                            }, 1000);
                                        } else {
                                            swal("提示", "更新失败", "error");
                                        }
                                    },
                                    error: function (data) {
                                        swal("提示", data.msg, "error");
                                    }
                                });
                            } else {
                                swal("提示", "请检查表单是否有漏填项！", "info");
                            }
                        }else if(adverts.style=="1"){
                            if (input6 == true) {
                                $.ajax({
                                    type: "POST",
                                    url: "${base}/advert/updateScreenAdvert.do",
                                    data: adverts,
                                    success: function (data) {
                                        console.log(data);
                                        if (data.code == '0') {
                                            swal("提示", "更新成功", "success");
                                            setTimeout(function () {
                                                location.reload();
                                            }, 1000);
                                        } else {
                                            swal("提示", "更新失败", "error");
                                        }
                                    },
                                    error: function (data) {
                                        swal("提示", data.msg, "error");
                                    }
                                });
                            } else {
                                swal("提示", "请检查表单是否有漏填项！", "info");
                            }
                        }
                    });
                },"json");
            });
        });
    </#if>
        //删除
    <#if (advertScreens?size > 0)>
        $("#storeInfoListTable").find('button[id=deleBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                console.log($(that).attr("a_id"));
                $("#deleModel").modal('show');
                $("#deleNoBtn").on('click',function () {
                    $("#deleModel").modal('hide');
                });
                $("#deleYesBtn").on('click',function () {
                    $.post("${base}/advert/delScreenAdvert.do?advertID=" +$(that).attr("a_id")+ "&screenID=" +$(that).attr("s_id")+ "&marketID=" +$(that).attr("m_id")+"&id="+$(that).attr("data_id"), function(data) {
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
        //查看
    <#if (advertScreens?size > 0)>
        $("#storeInfoListTable").find('button[id=lookBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/advert/goAdvertScreen.do?id="+$(that).attr("data_id")+"&screenID="+$(that).attr("s_id")+"&marketID="+$(that).attr("m_id")+"&groupID="+$(that).attr("a_id")});
            });
        });
    </#if>
        //更新
    <#if (advertScreens?size > 0)>
        $("#storeInfoListTable").find('button[id=updateBtn]').each(function () {
            var that = this;
            $(this).on('click', function () {
                $.post("${base}/advert/updateIsdown.do?advertID=" +$(that).attr("a_id")+ "&screenID=" +$(that).attr("s_id")+ "&marketID=" +$(that).attr("m_id"), function(data) {
                    //重新刷新
                    if(data.code == "0") {
                        $("#deleModel").modal('hide');
                        swal("提示", "更新成功", "success");
                        setTimeout(function(){location.reload();},1000);
                    } else {
                        swal("提示", "更新失败", "error");
                    }
                }, "json");
            });
        });
    </#if>
        $("#adScreenEditBtn").on('click', function () {//屏幕配置
            location.href = "${base}/advert/screens.do";
        });
        $("#addPlayButton").on('click', function () {//添加广告播放
            location.href = "${base}/advert/goAddAdvertScreen.do";
        });
    });
</script>
<!--编辑弹窗-->
<div class="modal fade" id="editModel" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">编辑广告播放</h4>
            </div>
            <div class="modal-body">
                <div class="modalAdvertStyle">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label class="col-md-4 control-label">门店信息：</label>
                            <div class="input-group col-md-6">
                                <select class="form-control" id="editStoreInfo" disabled="disabled">
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">屏幕编号：</label>
                            <div class="input-group col-md-6">
                                <select id="editScreenNum" class="form-control" disabled="disabled">
                                    <option value=""></option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <fieldset disabled>
                                <label class="col-md-4 control-label">是否设为默认广告：</label>
                                <label class="radio-inline">
                                    <input type="radio" name="isDefaultRadio" id="inlineRadio0" value="0"> 否
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="isDefaultRadio" id="inlineRadio1" value="1"> 是
                                </label>
                            </fieldset>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">广告分组：</label>
                            <div class="input-group col-md-6">
                                <select id="editTitle" class="form-control">
                                <#if groups?exists >
                                    <#list groups as group>
                                        <option value="${group.id}">${group.groupName}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">播放类型：</label>
                            <div class="input-group col-md-6">
                                <select id="editType" class="form-control">
                                    <option value="1">顺序播放</option>
                                    <option value="2">插播</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" id="stdiv">
                            <label class="col-md-4 control-label">开始时间：</label>
                            <div class="input-group col-md-6">
                                <input type="text" class="form-control" id="editStartTime">
                                <span class="input-group-addon bg-default"
                                      onClick="jeDate({dateCell:'#editStartTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})">
                                    <i class="fa fa-calendar"></i>
                                </span>
                            </div>
                        </div>
                        <div class="form-group" style="display: none">
                            <label class="col-md-4 control-label">结束时间：</label>
                            <div class="input-group col-md-6">
                                <input type="text" class="form-control" id="editEndTime">
                                <span class="input-group-addon bg-default"
                                      onClick="jeDate({dateCell:'#editEndTime',isTime:true,format:'YYYY-MM-DD hh:mm:ss'})">
                                    <i class="fa fa-calendar"></i>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">播放排序：</label>
                            <div class="input-group col-md-2">
                                <input type="text" class="form-control" id="editPlaySort"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">播放备注：</label>
                            <div class="input-group col-md-6">
                                <textarea class="form-control" rows="4" id="editPlayRemark"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="editNoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="editYesBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
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
                <button id="deleYesBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>