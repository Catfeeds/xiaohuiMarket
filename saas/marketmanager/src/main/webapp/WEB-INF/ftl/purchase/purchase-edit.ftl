<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">
<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>
<div class="content-page">
    <div class="content">
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">采购端管理</a></li>
                            <li class="active"><a href="#">采购单列表</a></li>
                        </ol>
                        <h4 class="page-title "><b>编辑采购单</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
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
                                        <td style="background-color: #f9f9f9">仓库名称</td>
                                        <td>
                                        <#if purchase.status=1 || purchase.status=2 || purchase.status=5>
                                            <fieldset disabled>
                                                <select class="form-control" id="houseSelect">
                                                    <#if warehouseItem?exists >
                                                        <#list warehouseItem as warehouse>
                                                            <option value="${warehouse.warehouseCode}" <#if warehouseItem?exists && purchase?exists && warehouse.warehouseName == purchase.warehouse_name>selected</#if>>${warehouse.warehouseName}</option>
                                                        </#list>
                                                    </#if>
                                                </select>
                                            </fieldset>
                                        <#else>
                                            <select class="form-control" id="houseSelect">
                                                <#if warehouseItem?exists >
                                                    <#list warehouseItem as warehouse>
                                                        <option value="${warehouse.warehouseCode}" <#if warehouseItem?exists && purchase?exists && warehouse.warehouseName == purchase.warehouse_name>selected</#if>>${warehouse.warehouseName}</option>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </#if>
                                        </td>
                                        <td style="background-color: #f9f9f9">供应商名称</td>
                                        <td>
                                        <#if purchase.status=1 || purchase.status=2 || purchase.status=5>
                                            <fieldset disabled>
                                                <select class="form-control" id="supplierSelect">
                                                    <#if supperlierItem?exists >
                                                        <#list supperlierItem as supplier>
                                                            <option value="${supplier.id?c}" <#if supperlierItem?exists && purchase?exists && supplier.supplierName == purchase.supplier_name>selected</#if>>${supplier.supplierName}</option>
                                                        </#list>
                                                    </#if>
                                                </select>
                                            </fieldset>
                                        <#else>
                                            <select class="form-control" id="supplierSelect">
                                                <#if supperlierItem?exists >
                                                    <#list supperlierItem as supplier>
                                                        <option value="${supplier.id?c}" <#if supperlierItem?exists && purchase?exists && supplier.supplierName == purchase.supplier_name>selected</#if>>${supplier.supplierName}</option>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </#if>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                <#if purchase.status=2 || purchase.status=5>
                    <div class="col-sm-4">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>温馨提示</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <ul class="list-group" style="color: red">
                                <li>1. 此页面是强制入库操作页面</li>
                                <li>2. 只能进行提交动作</li>
                            </ul>
                        </div>
                    </div>
                </#if>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="advert_container">
                            <ol class="breadcrumb pull-right">
                            <#if purchase.status=2 || purchase.status=5>
                            <#else>
                                <li><button id="addRow" class='btn-primary' onclick="addTr2('tab',-1);">添加商品</button></li>
                            </#if>
                            </ol>
                            <h5 class="page-title" style="padding-top: 20px"><b>采购信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <thead>
                                    <th style="background-color: #f9f9f9">商品分类</th>
                                    <th style="background-color: #f9f9f9">商品名称</th>
                                    <th style="background-color: #f9f9f9">条形码</th>
                                    <th style="background-color: #f9f9f9">采购日期</th>
                                    <th style="background-color: #f9f9f9">采购数量</th>
                                    <th style="background-color: #f9f9f9">采购单价</th>
                                    <th style="background-color: #f9f9f9"><#if purchase.status=2 || purchase.status=5>入库数量<#else>操作</#if></th>
                                    </thead>
                                    <tbody id="tab">
                                    <#if (commoditys?size > 0)>
                                        <#list commoditys as commodity>
                                            <#if purchase.status=2 || purchase.status=5>
                                            <tr>
                                                <td>
                                                    <select class="form-control" disabled="disabled">
                                                    <#--<option value="${commodity.purchaseId}">${commodity.itemTypeTitle}</option>-->
                                                        <#if (itemTypes?size > 0)>
                                                            <#list itemTypes as itemType>
                                                                <option value=${itemType.id?c} <#if commodity?exists && itemType.id == commodity.itemTypeId>selected </#if>><#if itemType.parentId == 0>${itemType.title}
                                                                <#else>&nbsp;&nbsp;&nbsp;&nbsp;${itemType.title}</#if></option>
                                                            </#list>
                                                        <#else><option value='0'>找不到商品</option>
                                                        </#if>
                                                    </select>
                                                </td>
                                                <td>
                                                    <select class="form-control" disabled="disabled">
                                                        <option value="${commodity.itemId?c}">${commodity.itemName}</option>
                                                    </select>
                                                </td>
                                                <td>
                                                    <fieldset disabled>
                                                        <input type="text" class="form-control" value="${commodity.barcode}" disabled="disabled">
                                                    </fieldset>
                                                </td>
                                                <td>
                                                    <input id="time${commodity_index}" type="text" class="form-control" disabled="disabled" value="${commodity.purchaseTime}" placeholder="请选择时间">
                                                </td>
                                                <td>
                                                    <input type="text" class="form-control" value="${commodity.purchaseNumber?c}" disabled="disabled">
                                                </td>
                                                <td>
                                                    <input type="text" class="form-control" value="${(commodity.costPrice)!}" disabled="disabled">
                                                </td>
                                                <td>
                                                    <#if commodity.depositNumber??>${commodity.depositNumber}<#else>--</#if>
                                                </td>
                                            </tr>
                                            <#else>
                                            <tr>
                                                <td>
                                                    <select class="form-control" onchange="changeSelect(this)" onblur="clearSelectErr(this);">
                                                    <#--<option value="${commodity.purchaseId}">${commodity.itemTypeTitle}</option>-->
                                                        <#if (itemTypes?size > 0)>
                                                            <#list itemTypes as itemType>
                                                                <option value=${itemType.id?c} <#if commodity?exists && itemType.id == commodity.itemTypeId>selected </#if>><#if itemType.parentId == 0>${itemType.title}
                                                                <#else>&nbsp;&nbsp;&nbsp;&nbsp;${itemType.title}</#if></option>
                                                            </#list>
                                                        <#else><option value='0'>找不到商品</option>
                                                        </#if>
                                                    </select>
                                                </td>
                                                <td>
                                                    <select class="form-control" onchange="changeCode(this)" onblur="clearSelectErr(this);">
                                                        <option value="${commodity.itemId?c}">${commodity.itemName}</option>
                                                    </select>
                                                </td>
                                                <td>
                                                    <fieldset disabled>
                                                        <input type="text" class="form-control" value="${commodity.barcode}">
                                                    </fieldset>
                                                </td>
                                                <td>
                                                    <div class="input-group">
                                                        <input id="time${commodity_index}" type="text" class="form-control" disabled="disabled" value="${commodity.purchaseTime}" placeholder="请选择时间">
                                                        <span class="input-group-addon bg-default" onClick="findInput(this)" onblur="clearTimeErr(this)">
                                                                 <i class="fa fa-calendar"></i></span>
                                                    </div>
                                                </td>
                                                <td>
                                                    <input type="text" class="form-control" value="${commodity.purchaseNumber?c}" onblur="clearInputErr(this);">
                                                </td>
                                                <td>
                                                    <input type="text" class="form-control" value="${(commodity.costPrice)!}" onblur="clearInputErr(this);">
                                                </td>
                                                <td>
                                                    <button type="button" class="btn-danger" onclick="deleTr(this);">删除</button>
                                                </td>
                                            </tr>
                                            </#if>
                                        </#list>
                                    </#if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="m-t-40">
                            <#if purchase.status=2 || purchase.status=5>
                                <button id="saveBtn" type="button" class="btn btn-danger col-md-1 statusBtn pull-right" data_status="${purchase.status}">强制入库</button>
                            <#elseif purchase.status=1>
                                <div class="pull-right">
                                    <button id="saveBtn" type="button" class="btn btn-primary statusBtn pull-right" data_status="${purchase.status}">提交</button>
                                </div>
                            <#else>
                                <div class="pull-right">
                                    <button id="unSaveBtn" type="button" class="btn btn-warning statusBtn pull-left" data_status="0">保存草稿</button>
                                    <button id="saveBtn" type="button" class="btn btn-primary statusBtn pull-right" data_status="${purchase.status}">提交</button>
                                </div>
                            </#if>
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
    function clearSelectErr(obj) { //下拉列表框清除错误
        var _val = $(obj).find('option:selected').val();
        if(_val !=''){
            $(obj).css('border','1px solid #eeeeee');
        }else{
            $(obj).css('border','1px solid red');
        }
    }
    function clearTimeErr(obj) { //时间控件清除错误
        console.log(obj);
        var _val = $(obj).parent().parent().find('input').val();
        if(_val ==''){
            $(obj).parent().parent().find('input').css('border','1px solid #eeeeee');
        }
    }
    function clearInputErr(obj) { //输入框清除错误
        var _val = $(obj).val();
        console.log(_val);
        if(_val !=''){
            $(obj).css('border','1px solid #eeeeee');
        }else{
            $(obj).css('border','1px solid red');
        }
    }
    function checkTypeSelect (obj,num,that){//检查表单是否有空项
        if(obj == "") {
            $(that).children("td:eq(0)").find('select').css("border","1px solid red");
            return false;
        } else {
            return true;
        }
    }
    function checkTitleSelect (obj,num,that){//检查表单是否有空项
        if(obj == "") {
            $(that).children("td:eq(1)").find('select').css("border","1px solid red");
            return false;
        } else {
            return true;
        }
    }
    function checkTimeInput (obj,num,that){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {
            $(that).children("td:eq(3)").find('input').css("border","1px solid red");
            return false;
        } else {
            return true;
        }
    }
    function checkNumInput (obj,num,that){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {
            $(that).children("td:eq(4)").find('input').css("border","1px solid red");
            return false;
        } else {
            return true;
        }
    }
    function checkPriceInput (obj,num,that){//检查表单是否有空项，空格验证方法待加
        if(obj == "") {
            $(that).children("td:eq(5)").find('input').css("border","1px solid red");
            return false;
        } else {
            return true;
        }
    }
    function addTr2(tab, row){
        var trHtml="<tr><td>" +
                "<select class='form-control' onchange='changeSelect(this)' onblur='clearSelectErr(this)'>" +
                "<option value=''>请选择商品分类</option>" +
                        "<#if (itemTypes?size > 0)><#list itemTypes as iType> <option value='${iType.id?c}' <#if item?exists && iType.id == item.typeId>selected </#if>>" +
                        "<#if iType.parentId == 0>${iType.title}<#else>&nbsp;&nbsp;&nbsp;&nbsp;${iType.title}</#if></option></#list><#else><option value='0'>无</option></#if>" +
                "</select>" +
                "</td>" +
                "<td><select class='form-control' onchange='changeCode(this)' onblur='clearSelectErr(this)'><option value=''>请选择商品名称</option></select></td>" +
                "<td><fieldset disabled><input type='text' class='form-control'></fieldset></td>" +
                "<td>" +
                "<div class='input-group'><input type='text' class='form-control' disabled='disabled' placeholder='请选择时间' value='${.now?date}'>" +
//                "<span class='input-group-addon bg-default' onClick=\"jeDate({dateCell:\'#endTime\',isTime:true,format:\'YYYY-MM-DD\'})\"><i class='fa fa-calendar'></i></span>" +
                "<span class='input-group-addon bg-default' onClick='findInput(this)' onblur='clearTimeErr(this)'><i class='fa fa-calendar'></i></span>" +
                "</div>" +
                "</td>" +
                "<td><input type='text' class='form-control' placeholder='输入采购数量' onblur='clearInputErr(this)'></td>" +
                "<td><input type='text' class='form-control' placeholder='输入采购单价' onblur='clearInputErr(this)'></td>" +
                "<td><button class='btn-danger' onclick='deleTr(this);'>删除</button></td>" +
                "</tr>";

        setTimeout(function(){//给时间框添加id标签
            var len = $('#tab tr').length-1;
            console.log($("#tab").children().last().children().eq(3).children());
            $("#tab").children().last().children().eq(3).children().find('input').attr("id",'time' + len);
        },100);
        addTr(tab, row, trHtml);
    }
    function addTr(tab, row, trHtml){
        //获取table最后一行 $("#tab tr:last")
        //获取table第一行 $("#tab tr").eq(0)
        //获取table倒数第二行 $("#tab tr").eq(-2)
//        var $tr=$("#"+tab+" tr").eq(row);
        var $tr=$("#tab");
        if($tr.size()==0){
            alert("指定的table id或行数不存在！");
            return;
        }
//        $tr.after(trHtml);
        $tr.append(trHtml);
    }
    function deleTr(nowTr) {
        var trlen = $("#tab").find("tr").length;
        if(trlen > 1){
            $(nowTr).parent().parent().remove();
        }else {
            swal("提示", "不可删除至少要保留一行信息！", "error");
        }
    }
    function changeSelect($s1) {
        var _id = $($s1).val();
        var $tr = $($s1).parent().parent();
        var $td = $tr.find("td:eq(1)");
        var $s2 = $td.find('select');
        var $in = $tr.find("td:eq(2)").find('input');
        if(_id !=""){//当商品分类不为空时
            console.log($s1);
//            if(!$s1.data(_id)){//不在缓冲区中,需要向服务器请求
//            var _url = "http://192.168.1.166:17777/item/item/getItemTemplateIdAndNames.do?itemTypeId=";
            var url = "${base}/purchase/itemsByitemTypeId.do?itemTypeId=";
            $.get(url+_id,function(data) {
                console.log(data);
                var _obj = data.response.datas;
                if(data.code==0) {//返回的数据不为空
                    $s2.empty();//清空第二个下拉列表
                    $s2.html("");
                    $in.val("");//清空条形码输入框
                    $("<option value=''>请选择商品名称</option>").appendTo($s2);
                    for(var i = 0; i < _obj.length; i++) {
                        $("<option value ='" + _obj[i].id + "' data-id ='" + _obj[i].barcode + "'> " + _obj[i].name + "</option>").appendTo($s2);
                    }
                } else {//返回的数据为空
                    $s2.empty();
                    $s2.html("");
                    $("<option value=''>该分类下没有商品</option>").appendTo($s2);
                    $in.val("");
                }
//                    $s2.data(_id, data);
            }, "json");
//            }else{//在缓冲区
//                var data = $s1.data(_id);
//                if(data.length != 0) {//返回的数据不为空
//                    $s2.html("");
//                    $("<option value=''>请选择商品分类</option>").appendTo($s2);
//                    for(var i = 0; i < data.length; i++) {
//                        $("<option value =' " + data[i].id + " '> " + data[i].name + "</option>").appendTo($s2);
//                    }
//                    $s2.parent().show();
//                    $s1.next().show();
//                } else {//返回的数据为空
//                    $s2.parent().hide();
//                    $s1.next().hide();
//                }
//            }
        }else{//商品分类为空的情况，隐藏第二个下拉框
            $s2.empty();
            $in.val("");
        }
    }
    function changeCode($se) {
        var _code = $($se).find('option:selected').attr('data-id');
        $($se).parent().parent().find("td:eq(2)").find('input').val(_code);
    }
    function findInput(ele) {
        var $input = $(ele).parent().parent().find('input');
        var id = $input.attr("id");
        jeDate({
            dateCell:'#' + id,
            isTime:true,
            format:'YYYY-MM-DD'
        });
        clearTimeErr(ele);
    }
    $(document).ready(function () {
        $("#tab tr td").find("select").select2();
        $(".statusBtn").click(function () {
            var warehouseID = $("#houseSelect").val();//仓库id
            var supplierID = $("#supplierSelect").val();//供应商id
            var itemIDs = "";//商品id
            var itemNames = "";//商品名称
            var itemTypeIDs = "";//商品类型id
            var itemTypeTitles = "";//商品类型名称
            var barcodes = "";//条形码
            var purchaseDates = "";//商品采购时间
            var purchaseNumbers = "";//商品采购数量
            var costPrices = "";//商品采购单价
            var index = 0;//行数
            var c1 = false;
            var c2 = false;
            var c3 = false;
            var c4 = false;
            var c5 = false;
            if((warehouseID && supplierID) ==''){
                swal("提示","仓库名称和供应商名称不能为空", "error");
            }else {
                $("#tab tr").each(function () {
                    index += 1;
                    var that = this;
                    var sTxt1 = $.trim($(this).children("td:eq(0)").find('option:selected').text());
                    var sVal1 = $(this).children("td:eq(0)").find('select').val();
                    var sTxt2 = $.trim($(this).children("td:eq(1)").find('option:selected').text());
                    var sVal2 = $(this).children("td:eq(1)").find('select').val();
                    var code = $(this).children("td:eq(2)").find('input').val();
                    var inputTxt1 = $(this).children("td:eq(3)").find('input').val();
                    var inputTxt2 = $(this).children("td:eq(4)").find('input').val();
                    var inputTxt3 = $(this).children("td:eq(5)").find('input').val();
                    c1 = checkTypeSelect(sVal1, index, that);
                    c2 = checkTitleSelect(sVal2, index, that);
                    c3 = checkTimeInput(inputTxt1, index, that);
                    c4 = checkNumInput(inputTxt2, index, that);
                    c5 = checkPriceInput(inputTxt3, index, that);
                    itemIDs += sVal2 + ",";
                    itemNames += sTxt2 + ",";
                    itemTypeIDs += sVal1 + ",";
                    itemTypeTitles += sTxt1 + ",";
                    barcodes += code + ",";
                    purchaseDates += inputTxt1 + ",";
                    purchaseNumbers += inputTxt2 + ",";
                    costPrices += inputTxt3 + ",";
                });
                if((c1 && c2 && c3 && c4 && c5) == true){
                    itemIDs = itemIDs.length > 0 ? itemIDs.substring(0, itemIDs.length - 1) : "";
                    itemNames = itemNames.length > 0 ? itemNames.substring(0, itemNames.length - 1) : "";
                    itemTypeIDs = itemTypeIDs.length > 0 ? itemTypeIDs.substring(0, itemTypeIDs.length - 1) : "";
                    itemTypeTitles = itemTypeTitles.length > 0 ? itemTypeTitles.substring(0, itemTypeTitles.length - 1) : "";
                    barcodes = barcodes.length > 0 ? barcodes.substring(0, barcodes.length - 1) : "";
                    purchaseDates = purchaseDates.length > 0 ? purchaseDates.substring(0, purchaseDates.length - 1) : "";
                    purchaseNumbers = purchaseNumbers.length > 0 ? purchaseNumbers.substring(0, purchaseNumbers.length - 1) : "";
                    costPrices = costPrices.length > 0 ? costPrices.substring(0, costPrices.length - 1) : "";
                    var para0 = "&warehouseCode=" + warehouseID + "&supplierID=" + supplierID + "&itemIDs=" + itemIDs + "&itemNames=" + itemNames + "&itemTypeIDs=" + itemTypeIDs
                            + "&itemTypeTitles=" + itemTypeTitles + "&purchaseTimes=" + purchaseDates + "&purchaseNumbers=" + purchaseNumbers + "&status=0&barcodes=" + barcodes+"&costPrices="+costPrices;
                    var para1 = "&warehouseCode=" + warehouseID + "&supplierID=" + supplierID + "&itemIDs=" + itemIDs + "&itemNames=" + itemNames + "&itemTypeIDs=" + itemTypeIDs
                            + "&itemTypeTitles=" + itemTypeTitles + "&purchaseTimes=" + purchaseDates + "&purchaseNumbers=" + purchaseNumbers + "&status=1&barcodes=" + barcodes+"&costPrices="+costPrices;
                    var para4 = "&warehouseCode=" + warehouseID + "&supplierID=" + supplierID + "&itemIDs=" + itemIDs + "&itemNames=" + itemNames + "&itemTypeIDs=" + itemTypeIDs
                            + "&itemTypeTitles=" + itemTypeTitles + "&purchaseTimes=" + purchaseDates + "&purchaseNumbers=" + purchaseNumbers + "&status=4&barcodes=" + barcodes+"&costPrices="+costPrices;
                    var eleId = $(this).attr('id');
                    var eleStatus = $(this).attr('data_status');
                    if ((eleStatus == 1 || eleStatus == 0) && eleId == 'saveBtn') {
                        $.post("${base}/purchase/updatePurchase.do?id=${purchase.id}" + para1, function (data) {//提交
                            //重新刷新
                            console.log(data);
                            if (data.code == "0") {
                                swal("提示", "更新成功", "success");
                                setTimeout(function () {
                                    history.back();
                                }, 1000);
                            } else {
                                swal("提示", data.msg, "error");
                            }
                        }, "json");
                    } else if ((eleStatus == 2 || eleStatus == 5) && eleId == 'saveBtn'){
                        $("#disableModal").modal('show');
                        $("#OkBtn").on('click',function () {
                            var txt = $("#textRemark").val();
                            $.post("${base}/purchase/updatePurchase.do?id=${purchase.id}" + para4 + "&coerceRemark=" + txt, function (data) {//强制入库提交
                                //重新刷新
                                console.log(data);
                                if (data.code == "0") {
                                    swal("提示", "更新成功", "success");
                                    setTimeout(function () {
                                        history.back();
                                    }, 1000);
                                } else {
                                    swal("提示", data.msg, "error");
                                }
                            }, "json");
                        });
                    } else {
                        $.post("${base}/purchase/updatePurchase.do?id=${purchase.id}" + para0, function (data) {//保存草稿
                            //重新刷新
                            console.log(data);
                            if (data.code == "0") {
                                swal("提示", "保存成功", "success");
                                setTimeout(function () {
                                    history.back();
                                }, 1000);
                            } else {
                                swal("提示", data.msg, "error");
                            }
                        }, "json");
                    }
                }else{
                    swal("提示", "请填写完整信息", "error");
                }
            }
        });
    });
</script>
<!--强制入库弹窗-->
<div class="modal fade" id="disableModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-md-4 control-label">请说明强制入库的原因：</label>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <textarea class="form-control" rows="6" id="textRemark"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="NoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="OkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>