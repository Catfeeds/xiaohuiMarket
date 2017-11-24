<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">优惠券管理</a></li>
                            <li class="active"><a href="#">优惠券列表</a></li>
                        </ol>
                        <h4 class="page-title "><b>编辑优惠券</b></h4>
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
                                        <td style="background-color: #f9f9f9">* 优惠券类型</td>
                                        <td>
                                            <select id="couponSelect" class="form-control">
                                                <option value="1">注册赠券</option>
                                            <#--<option value="2">注册赠券</option>-->
                                            <#--<option value="3">注册赠券</option>-->
                                            <#--<option value="4">注册赠券</option>-->
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 优惠券名称</td>
                                        <td><input type="text" class="form-control" id="couponName" placeholder="输入优惠券名称" value="${rule.activeRuleName}"/></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 优惠券标题</td>
                                        <td><input type="text" class="form-control" id="couponTitle" placeholder="输入优惠券标题" value="${rule.activeRuleTitle}"/></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 优惠券总张数</td>
                                        <td>
                                            <#if rule.activeDistributeCount=-1>
                                                <form class="form-inline col-sm-12" id="form1">
                                                    <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="numberRadio" id="inlineRadio1" value="-1" checked>不限量
                                                        </label>
                                                    </div>
                                                    <div class="form-group col-sm-10 pull-right" style="margin-bottom: 0px">
                                                        <label class="radio-menu-item">
                                                            <input type="radio" name="numberRadio" id="inlineRadio2" value="">
                                                        </label>
                                                        <div class="input-group col-sm-10">
                                                            <input type="text" class="form-control" id="amountInput" placeholder="输入优惠券总发行数量（单位：张）"/>
                                                        </div>
                                                    </div>
                                                </form>
                                            <#else>
                                                <form class="form-inline col-sm-12" id="form1">
                                                    <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="numberRadio" id="inlineRadio1" value="-1">不限量
                                                        </label>
                                                    </div>
                                                    <div class="form-group col-sm-10 pull-right" style="margin-bottom: 0px">
                                                        <label class="radio-menu-item">
                                                            <input type="radio" name="numberRadio" id="inlineRadio2" value="" checked>
                                                        </label>
                                                        <div class="input-group col-sm-10">
                                                            <input type="text" class="form-control" id="amountInput" placeholder="输入优惠券总发行数量（单位：张）" value="${rule.activeDistributeCount}"/>
                                                        </div>
                                                    </div>
                                                </form>
                                            </#if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 优惠金额</td>
                                        <td><input type="text" class="form-control" id="couponSum" placeholder="输入优惠券金额（单位：元）" value="${rule.activeMonery}"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>使用须知</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <ul>
                                <li>1.带 * 的一栏为必填项，没有可以不填</li>
                                <li>2.单选栏必须先选中单选按钮，再输入相应数据，不可直接输入数据</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>联系信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 使用门槛</td>
                                        <td>
                                            <#if rule.activeRuleOrderMoney=-1>
                                                <form class="form-inline col-sm-10" id="form2">
                                                    <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="sumRadio" id="inlineRadio3" value="-1" checked>不限金额
                                                        </label>
                                                    </div>
                                                    <div class="form-group col-sm-8 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-menu-item">
                                                            <input type="radio" name="sumRadio" id="inlineRadio4" value="">
                                                        </label>
                                                        <div class="input-group col-sm-6">
                                                            <input type="text" class="form-control" id="sumInput" placeholder="购满多少可以使用（单位：元）"/>
                                                        </div>
                                                    </div>
                                                </form>
                                            <#else>
                                                <form class="form-inline col-sm-10" id="form2">
                                                    <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="sumRadio" id="inlineRadio3" value="-1">不限金额
                                                        </label>
                                                    </div>
                                                    <div class="form-group col-sm-8 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-menu-item">
                                                            <input type="radio" name="sumRadio" id="inlineRadio4" value="" checked>
                                                        </label>
                                                        <div class="input-group col-sm-6">
                                                            <input type="text" class="form-control" id="sumInput" placeholder="购满多少可以使用（单位：元）" value="${rule.activeRuleOrderMoney}"/>
                                                        </div>
                                                    </div>
                                                </form>
                                            </#if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 每人限领</td>
                                        <td><div class="form-inline"><text class="pull-left" style="margin-left:55px;color: #999">1&nbsp;张</text></div></td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 可使用商品</td>
                                        <td>
                                            <form class="form-inline col-sm-10" id="form3">
                                                <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                    <label class="radio-inline" style="color: #999">
                                                        <input type="radio" name="goodsRadio" id="inlineRadio5" value="1" checked>全部商品
                                                    </label>
                                                </div>
                                            </form>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">* 有效时间</td>
                                        <td>
                                            <#if rule.effectiveType=1>
                                                <form class="form-inline col-sm-12" id="form2">
                                                    <div class="form-group col-sm-6 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="timeRadio" id="inlineRadio6" value="2">起止时间：
                                                        </label>
                                                        <div class="input-group" id="stimediv" style="display: inline-table">
                                                            <input id="startTime" type="text" class="form-control" name="beginTime" readonly>
                                                            <span class="input-group-addon bg-default" id="stimespan"
                                                                  onClick="addStime()"><i class="fa fa-calendar" style="display: block"></i></span>
                                                        </div>
                                                        <label style="color: #999">-</label>
                                                        <div class="input-group" id="etimediv" style="display: inline-table">
                                                            <input id="endTime" type="text" class="form-control" name="endTime" readonly>
                                                            <span class="input-group-addon bg-default" id="etimespan"
                                                                  onClick="addEtime()"><i class="fa fa-calendar" style="display: block"></i></span>
                                                        </div>
                                                    </div>
                                                    <#if rule.activeRuleEffective=-1>
                                                        <div class="form-group col-sm-4 pull-left" style="margin-bottom: 0px">
                                                            <label class="radio-menu-item">
                                                                <input type="radio" name="timeRadio" id="inlineRadio7" value="1"/>
                                                            </label>
                                                            <div class="input-group">
                                                                <input type="text" class="form-control" id="dayInput" placeholder="输入有效天数(单位：天)" value=""/>
                                                            </div>
                                                        </div>
                                                        <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                            <label class="radio-inline" style="color: #999">
                                                                <input type="radio" name="timeRadio" id="inlineRadio8" value="-1" checked>不限时
                                                            </label>
                                                        </div>
                                                    <#else>
                                                        <div class="form-group col-sm-4 pull-left" style="margin-bottom: 0px">
                                                            <label class="radio-menu-item">
                                                                <input type="radio" name="timeRadio" id="inlineRadio7" value="1" checked/>
                                                            </label>
                                                            <div class="input-group">
                                                                <input type="text" class="form-control" id="dayInput" placeholder="输入有效天数(单位：天)" value="${rule.activeRuleEffective}"/>
                                                            </div>
                                                        </div>
                                                        <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                            <label class="radio-inline" style="color: #999">
                                                                <input type="radio" name="timeRadio" id="inlineRadio8" value="-1">不限时
                                                            </label>
                                                        </div>
                                                    </#if>
                                                </form>
                                            <#elseif rule.effectiveType=2>
                                                <form class="form-inline col-sm-12" id="form2">
                                                    <div class="form-group col-sm-6 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="timeRadio" id="inlineRadio6" value="2" checked>起止时间：
                                                        </label>
                                                        <div class="input-group" id="stimediv" style="display: inline-table">
                                                            <input id="startTime" type="text" class="form-control" name="beginTime" value="${rule.activeBeginTime}" readonly>
                                                            <span class="input-group-addon bg-default" id="stimespan"
                                                                  onClick="addStime()"><i class="fa fa-calendar" style="display: block"></i></span>
                                                        </div>
                                                        <label style="color: #999">-</label>
                                                        <div class="input-group" id="etimediv" style="display: inline-table">
                                                            <input id="endTime" type="text" class="form-control" name="endTime" value="${rule.activeEndTime}" readonly>
                                                            <span class="input-group-addon bg-default" id="etimespan"
                                                                  onClick="addEtime()"><i class="fa fa-calendar" style="display: block"></i></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-4 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-menu-item">
                                                            <input type="radio" name="timeRadio" id="inlineRadio7" value="1"/>
                                                        </label>
                                                        <div class="input-group">
                                                            <input type="text" class="form-control" id="dayInput" placeholder="输入有效天数(单位：天)" value=""/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="timeRadio" id="inlineRadio8" value="-1">不限时
                                                        </label>
                                                    </div>
                                                </form>
                                            <#elseif rule.effectiveType=-1>
                                                <form class="form-inline col-sm-12" id="form2">
                                                    <div class="form-group col-sm-6 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="timeRadio" id="inlineRadio6" value="2">起止时间：
                                                        </label>
                                                        <div class="input-group" id="stimediv" style="display: inline-table">
                                                            <input id="startTime" type="text" class="form-control" name="beginTime" readonly>
                                                            <span class="input-group-addon bg-default" id="stimespan"
                                                                  onClick="addStime()"><i class="fa fa-calendar" style="display: block"></i></span>
                                                        </div>
                                                        <label style="color: #999">-</label>
                                                        <div class="input-group" id="etimediv" style="display: inline-table">
                                                            <input id="endTime" type="text" class="form-control" name="endTime" readonly>
                                                            <span class="input-group-addon bg-default" id="etimespan"
                                                                  onClick="addEtime()"><i class="fa fa-calendar" style="display: block"></i></span>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-sm-4 pull-left" style="margin-bottom: 0px">
                                                        <label class="radio-menu-item">
                                                            <input type="radio" name="timeRadio" id="inlineRadio7" value=""/>
                                                        </label>
                                                        <div class="input-group">
                                                            <input type="text" class="form-control" id="dayInput" placeholder="输入有效天数(单位：天)"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group pull-left col-sm-2" style="margin-bottom: 0px">
                                                        <label class="radio-inline" style="color: #999">
                                                            <input type="radio" name="timeRadio" id="inlineRadio8" value="-1" checked>不限时
                                                        </label>
                                                    </div>
                                                </form>
                                            </#if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">使用规则</td>
                                        <td>
                                            <#if rule.activeRuleExplain=''>
                                                <input type="text" class="form-control" id="couponRule" placeholder="输入使用优惠券的规则"/>
                                            <#else>
                                                <input type="text" class="form-control" id="couponRule" placeholder="输入使用优惠券的规则" value="${rule.activeRuleExplain}"/>
                                            </#if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="background-color: #f9f9f9">备注</td>
                                        <td>
                                            <#if rule.bak=''>
                                                <input type="text" class="form-control" id="couponRemark" placeholder="输入备注信息"/>
                                            <#else>
                                                <input type="text" class="form-control" id="couponRemark" placeholder="输入备注信息" value="${rule.bak}"/>
                                            </#if>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="m-t-40 pull-right">
                                <button id="backBtn" type="button" class="btn waves-effect waves-light btn-default pull-left" onclick="history.go(-1);">返回</button>
                                <button id="saveBtn" type="button" class="btn waves-effect waves-light btn-primary pull-right">确定</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${res}/assets/plugins/jedate/jedate.min.js"></script>
<script type="text/javascript">
    function checkInput (obj){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {
            return false;
        } else {
            return true;
        }
    }
    function addStime() {
        $("#dayInput").val('');
        $("#dayInput").attr("disabled","disabled");
        $("#inlineRadio7").attr("checked",false);
        $("#inlineRadio6").attr("checked",true);
        jeDate({dateCell:'#startTime',isTime:true,format:'YYYY-MM-DD'});
    }
    function addEtime() {
        $("#dayInput").val('');
        $("#dayInput").attr("disabled","disabled");
        $("#inlineRadio7").attr("checked",false);
        $("#inlineRadio6").attr("checked",true);
        jeDate({dateCell:'#endTime',isTime:true,format:'YYYY-MM-DD'});
    }
    $(document).ready(function () {
        $("#inlineRadio1").click(function(){
            $("#amountInput").prop("disabled","disabled");
            $("#amountInput").val('');
        });
        $("#inlineRadio2").click(function(){
            $("#amountInput").prop("disabled","");
            $("#amountInput").focus();
        });
        $("#inlineRadio3").click(function(){
            $("#sumInput").prop("disabled","disabled");
            $("#sumInput").val('');
        });
        $("#inlineRadio4").click(function(){
            $("#sumInput").prop("disabled","");
            $("#sumInput").focus();
        });
        $("#inlineRadio6").click(function(){
//            $("#stimediv").css("display","inline-table");
//            $("#etimediv").css("display","inline-table");
            $(".dis-fa").css("display","block");
            $("#startTime").prop("disabled","");
            $("#endTime").prop("disabled","");
            $("#dayInput").prop("disabled","disabled");
            $("#dayInput").val('');
        });
        $("#inlineRadio7").click(function(){
            $("#startTime").val('');
            $("#endTime").val('');
            $("#dayInput").prop("disabled","");
            $("#dayInput").focus();
        });
        $("#inlineRadio8").click(function(){
            $("#startTime").val('');
            $("#endTime").val('');
            $(".dis-fa").css("display","none");
            $("#startTime").prop("disabled","disabled");
            $("#endTime").prop("disabled","disabled");
            $("#dayInput").val('');
            $("#dayInput").prop("disabled","disabled");
        });
        $("#amountInput").focus(function () {
            $("#inlineRadio2").attr("checked",true);
        });
        $("#sumInput").focus(function () {
            $("#inlineRadio4").attr("checked",true);
        });
        $("#dayInput").focus(function () {
            $("#inlineRadio7").attr("checked",true);
            $(".dis-fa").css("display","none");
            $("#startTime").prop("disabled","disabled");
            $("#endTime").prop("disabled","disabled");
//            $("#stimediv").css("display","none");
//            $("#etimediv").css("display","none");
            $("#startTime").val('');
            $("#endTime").val('');
        });
        $("#saveBtn").on('click',function () {
            $(this).button('loading').delay(500).queue(function() {
                $(this).button('reset'); //重置按钮
                $(this).dequeue();
                var cn = '';//优惠券总发行数
                var cm = '';//优惠券使用门槛金额
                var tt = '';//时间类型
                var st = '';//开始时间
                var et = '';//结束时间
                var ed = ''//有效天数
                var arr = {
                    "v1": $("#couponSelect").val(),//优惠券类型
                    "v2": $("#couponName").val(),//优惠券名称
                    "v3": $("#couponTitle").val(),//优惠券标题
                    "v4": $("#couponSum").val(),//优惠券金额
                    "v5": "1",//每人限领，默认1
                    "v6": $("#inlineRadio5").val(),//可使用商品，默认全部商品
                    "v7": $("#couponRule").val(),//使用规则
                    "v8": $("#couponRemark").val()//备注
                };
                $('input[name="numberRadio"]:checked').each(function () {
                    var id= $(this).attr("id");
                    if (id == "inlineRadio1") {
                        cn = $("#" + id).val();
                    } else if (id == "inlineRadio2") {
                        cn = $("#amountInput").val();
                    } else{
                        cn ='';
                    }
                });
                $('input[name="sumRadio"]:checked').each(function () {
                    var id= $(this).attr("id");
                    if (id == "inlineRadio3") {
                        cm = $("#" + id).val();
                    } else if (id == "inlineRadio4") {
                        cm = $("#sumInput").val();
                    } else{
                        cm ='';
                    }
                });
                $('input[name="timeRadio"]:checked').each(function () {
                    var id= $(this).attr("id");
                    if (id == "inlineRadio6") {
                        tt = $("#" + id).val();
                        st = $("#startTime").val();
                        et = $("#endTime").val();
                    } else if (id == "inlineRadio7") {
                        tt = $("#" + id).val();
                        ed = $("#dayInput").val();
                    } else if (id == "inlineRadio8") {
                        tt = $("#" + id).val();
                        ed = -1;
                    } else{
                        tt ='';
                    }
                });
//                if($('input[name="timeRadio"]:checked').attr("id") == "inlineRadio6"){
//                    tt = 2;
//                    st = $("#startTime").val();
//                    et = $("#endTime").val();
//                }
                alert(tt);
                var input1 = checkInput(arr.v1);
                var input2 = checkInput(arr.v2);
                var input3 = checkInput(arr.v3);
                var input4 = checkInput(arr.v4);
                var input5 = checkInput(arr.v5);
                var input6 = checkInput(arr.v6);
                if ((input1 && input2 && input3 && input4 && input5 && input6 == true) && cn!='' && cm!='' && tt!='') {
                    if(tt == 2){
                        if(st && et !=''){
                            var url = "${base}/coupon/updateCoupon.do?activeRuleName=" + arr.v2 + "&activeRuleTitle=" + arr.v3 + "&activeRuleType=" + arr.v1 + "&activeMonery="
                                    + arr.v4 + "&activeRuleNum=" + arr.v5 + "&activeScene=" + arr.v6 + "&activeRuleExplain=" + arr.v7 + "&bak=" + arr.v8 + "&activeDistributeCount="
                                    + cn + "&activeRuleOrderMoney=" + cm + "&effectiveType=" + tt + "&activeBeginTime=" + st + "&activeEndTime=" + et + "&activeRuleID=" + ${rule.id};
                            $.post(url, function (data) {
                                //重新刷新
                                console.log(data);
                                if (data.code == "0") {
                                    swal("提示", "添加成功", "success");
                                    setTimeout(function () {
                                        location.href = "${base}/coupon/couponPage.do";
                                    }, 1000);
                                } else {
                                    swal("提示", data.msg, "error");
                                }
                            }, "json");
                        }else{
                            swal("提示", "起止时间为空！", "info");
                        }
                    }else if(tt == 1){
                        var url = "${base}/coupon/updateCoupon.do?activeRuleName="
                                + arr.v2 + "&activeRuleTitle=" + arr.v3 + "&activeRuleType=" + arr.v1 + "&activeMonery=" + arr.v4 + "&activeRuleNum="
                                + arr.v5 + "&activeScene=" + arr.v6 + "&activeRuleExplain=" + arr.v7 + "&bak=" + arr.v8 + "&activeDistributeCount="
                                + cn + "&activeRuleOrderMoney=" + cm + "&effectiveType=" + tt + "&activeRuleEffective=" + ed + "&activeRuleID=" + ${rule.id};
                        $.post(url, function (data) {
                            //重新刷新
                            console.log(data);
                            if (data.code == "0") {
                                swal("提示", "添加成功", "success");
                                setTimeout(function () {
                                    location.href = "${base}/coupon/couponPage.do";
                                }, 1000);
                            } else {
                                swal("提示", data.msg, "error");
                            }
                        }, "json");
                    }
                } else {
                    swal("提示", "请检查表单是否有漏填项！", "info");
                }
            });
        });
    });
</script>
