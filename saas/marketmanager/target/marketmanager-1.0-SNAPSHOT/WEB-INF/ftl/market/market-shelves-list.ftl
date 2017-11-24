<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->

<link href="${res}/assets/plugins/bootstrap-select2/select2.min.css" rel="stylesheet" type="text/css">

<script src="${res}/assets/plugins/bootstrap-select2/select2.min.js"></script>
<script src="${res}/assets/plugins/bootstrap-select2/zh-CN.js"></script>

<!-- 商品更改库存界面 -->
<div class="modal fade" id="upItemDialog" tabindex="-1" role="dialog" >
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="lItemTitle">上架商品</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="col-md-4 control-label">弹夹编码：</label>
                                <div class="col-md-4">
                                    <input type="text" id="ipItemCode" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group" id="dItemName">
                                <label class="col-md-4 control-label" id="lItemName">商品名称：</label>
                                <div class="col-md-4">
                                    <input type="text" id="ipItemName" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group" id="dItemStock">
                                <label class="col-md-4 control-label" id="lItemStock">库存：</label>
                                <div class="col-md-4">
                                    <input type="text" id="ipItemStock" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group" id="dItemTypes">
                                <label class="col-md-4 control-label" id="lItemTypes">选择替换的商品：</label>
                                <div class="col-md-4">
                                    <select id="sItemTypes" style="width:200px">
                                    <#if itemTypes?exists && (itemTypes?size > 0)>
                                        <option data_id="0">选择分类</option>
                                        <#list itemTypes as iType>
                                            <option data_id="${iType.id?c}" data_v="${iType.title}">
                                                <#if iType.parentId == 0>
                                                ${iType.title}
                                                <#else>
                                                    &nbsp;&nbsp;&nbsp;&nbsp;${iType.title}
                                                </#if>
                                            </option>
                                        </#list>
                                    <#else>
                                        <option data_id="0">选择分类</option>
                                    </#if>
                                    </select>
                                    <select id="sItem" style="width:200px"></select>
                                </div>
                            </div>
                            <div class="form-group" id="dNewItemStock">
                                <label class="col-md-4 control-label" id="lNewItemStock">新增库存：</label>
                                <div class="col-md-4">
                                    <input type="number" id="ipNewItemStock" class="form-control">
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-6">
                        <button type="button" class="btn btn-default"
                                data-dismiss="modal">取消
                        </button>
                        <button id="upItemOkBtn" type="button"
                                class="btn btn-primary">确定
                        </button>
                    </div>
                    <div class="col-sm-2"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- 任务详情 -->
<div class="modal fade" id="taskDialog" tabindex="-1" role="dialog" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="upItemTitle">任务详情</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-10">
                        <form class="form-horizontal" role="form">
                            <div class="form-group">
                                <label class="col-md-4 control-label">任务ID：</label>
                                <div class="col-md-6">
                                    <input type="text" id="ipTaskId" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">商品名：</label>
                                <div class="col-md-6">
                                    <input type="text" id="ipTaskItemName" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">商品数量：</label>
                                <div class="col-md-6">
                                    <input type="text" id="ipTaskItemStock" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">期望执行的日期：</label>
                                <div class="col-md-6">
                                    <input type="text" id="ipTaskDate" class="form-control" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">任务状态：</label>
                                <div class="col-md-6">
                                    <input type="text" id="ipTaskStatus" class="form-control" disabled value="未执行">
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-3"></div>
                    <div class="col-sm-6">
                        <button type="button" class="btn btn-success"
                                data-dismiss="modal">继续任务
                        </button>
                        <button id="cancelTaskBtn" type="button"
                                class="btn btn-danger">取消任务
                        </button>
                    </div>
                    <div class="col-sm-3"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- 任务列表 -->
<div class="modal fade" id="taskListDialog" tabindex="-1" role="dialog" >
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="upItemTitle">任务列表</h4>
            </div>
            <div class="modal-body">
                <table class="table table-striped table-bordered">
                    <thead class="table_head">
                    <tr>
                        <th>弹夹编码</th>
                        <th>任务描述</th>
                        <th>商品名称</th>
                        <th>原库存</th>
                        <th>切换商品名称</th>
                        <th>新库存</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="taskListTable">
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <div class="row">
                    <div class="col-sm-3"></div>
                    <div class="col-sm-6">
                        <button id="submitTaskBtn" type="button"
                                class="btn btn-primary">提交所有任务
                        </button>
                    </div>
                    <div class="col-sm-3"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">店铺管理</a></li>
                            <li class="active"><a href="#">店铺列表</a></li>
                        </ol>
                        <h4 class="page-title"><b>店铺列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <div class="row">
                    <div class="col-sm-12">
                        <form class="form-inline" role="form">

                            <div class="form-group">
                                <label>选择店铺：</label>
                                <select id="sMarket" style="width:200px;">
                                <#if markets?exists && (markets?size > 0)>
                                    <#list markets as market>
                                        <option data_id="${market.id?c}" <#if marketId == market.id>
                                                selected </#if>>
                                        ${market.name}
                                        </option>
                                    </#list>
                                <#else>
                                    <option data_id="0" selected>选择店铺</option>
                                </#if>
                                </select>
                            </div>

                            <div class="form-group m-l-15">
                                <label>组：</label>
                                <select id="m_group" style="width:120px;">
                                    <option data_id="0">选择组</option>
                                <#if groups?exists && (groups?size > 0)>
                                    <#list groups as group>
                                        <option value="${group}">${group}</option>
                                    </#list>
                                </#if>
                                </select>
                            </div>

                            <div class="form-group m-l-15">
                                <label>单元：</label>
                                <select id="m_unit" style="width:120px;"></select>
                            </div>

                            <div class="form-group m-l-15">
                                <label>层：</label>
                                <select id="m_layer" style="width:120px;"></select>
                            </div>

                            <div class="form-group pull-right">
                                <button id="taskListBtn" type="button"
                                        class="btn waves-effect waves-light btn-default">任务列表
                                </button>
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
                            <th>弹夹编码</th>
                            <th>商品条码</th>
                            <th>商品名称</th>
                            <th>商品单位</th>
                            <th>商品库存</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="clipListTable">
                        <tr id="emptyTr">
                            <td colSpan="7" height="200px">
                                <p class="text-center" id="errorInfo">暂无任何数据</p>
                            </td>
                        </tr>
                        </tbody>
                    </table>


                </div>
            </div>

        <#if clips?exists && (clips?size > 0)>
            <div class="row small_page">
                <div class="col-sm-12">
                    <#include "../common/paginate.ftl">
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/market/markets.do" />
                </div>
            </div>
        </#if>
            <!-- end container -->
        </div>


        <script type="text/javascript">


            $(document).ready(function () {

                //---------------------------------------上架库存相关内容

                var _lItemTitle = $("#lItemTitle");
                var _ipItemCode = $("#ipItemCode");

                var _divItemName = $("#dItemName");
                var _labelItemName = $("#lItemName");
                var _ipItemName = $("#ipItemName");

                var _divItemStock = $("#dItemStock");
                var _labelItemStock = $("#lItemStock");
                var _ipItemStock = $("#ipItemStock");

                var _sItem = $("#sItem");

                var _divItemTypes = $("#dItemTypes");
                var _labelItemTypes = $("#lItemTypes");
                var _sItemTypes = $("#sItemTypes");

                var _divNewItemStock = $("#dNewItemStock");
                var _labelNewItemStock = $("#lNewItemStock");
                var _ipNewItemStock = $("#ipNewItemStock");

                var _clipListTable = $("#clipListTable");//单元表
                var _upItemOkBtn = $("#upItemOkBtn");
                var _upItemDialog = $("#upItemDialog");
                var _taskDialog = $("#taskDialog");
                var _taskListDialog = $("#taskListDialog");
                var _taskListTable = $("#taskListTable");

                //---任务
                var _ipTaskId = $("#ipTaskId");
                var _ipTaskItemName = $("#ipTaskItemName");
                var _ipTaskItemStock = $("#ipTaskItemStock");
                var _ipTaskStatus = $("#ipTaskStatus");
                var _ipTaskDate = $("#ipTaskDate");
                var _cancelTaskBtn = $("#cancelTaskBtn");

                var _taskListBtn = $("#taskListBtn");

                //商品内容
                var items = {};//以分类ID储存

                //以弹夹为key的任务列表
                var task_list = {};
                var task_count = 0;
                var task_size = 50;

                var unit_list = {};
                var level_list = {};

                //选中的商铺ID
                var s_MarketId = ${marketId?c};
                var s_Group = "";
                var s_Unit = "";
                var s_Layer = "";

                var title = ['选择单元', '选择楼层'];
                $.each(title, function (k, v) {
                    title[k] = '<option data_id=\"0\">' + v + '</option>';
                });

                //处理类型
                var _handlerType = 0;

                _sItemTypes.select2();
                _sItemTypes.change(function () {
                    //获取商品
                    var select_obj = _sItemTypes.find("option:selected");
                    var data_id = select_obj.attr("data_id");
                    _sItem.empty();
                    _sItem.append("<option data_id=\"0\" >选择商品</option>");

                    //如果有商品，则不理会，否则，弹出选择
                    if (items[data_id] != null) {
                        //添加
                        $.each(items[data_id], function (k, v) {
                            var option = '<option data_id="' + k + '" data_v="' + v + '">' + v + '</option>';
                            _sItem.append(option);
                        });
                    } else {
                        //从网络获取
                        tokenPost("${base}/item/idNameItems.do?itemTypeId=" + data_id, function (json) {
                            if (json.code != 0) {
                                swal(json.msg);
                            } else {
                                items[data_id] = json.response.datas;
                                $.each(items[data_id], function (k, v) {
                                    var option = '<option data_id="' + k + '" data_v="' + v + '">' + v + '</option>';
                                    _sItem.append(option);
                                });
                            }
                        }, "json");
                    }
                });

                _sItem.append("<option data_id=\"0\" >选择商品</option>");
                _sItem.select2();

                //上架，提交任务
                _upItemOkBtn.on('click', function () {
                    var data_id = $(this).attr("data_id");
                    var code = _ipItemCode.val();
                    var task = null;
                    var stock = _ipNewItemStock.val();

                    if(_handlerType == 1) {
                        //增加
                        task = {
                            "handlerType": _handlerType,
                            "code": code,
                            "itemId": data_id,
                            "itemName": _ipItemName.val(),
                            "itemStock": stock,
                            "itemSrcStock": _ipItemStock.val(),
                        };
                    } else {
                        var select_obj = _sItem.find("option:selected");
                        var data_id = select_obj.attr("data_id");
                        if (data_id == "0") {
                            swal("你还没有选择商品");
                            return;
                        }
                        if(_handlerType == 0) {
                            //新增
                            task = {
                                "handlerType": _handlerType,
                                "code": code,
                                "itemId": data_id,
                                "itemName": select_obj.attr("data_v"),
                                "itemStock": stock
                            };
                        } else {
                            //替换
                            task = {
                                "handlerType": _handlerType,
                                "code": code,
                                "itemId": data_id,
                                "itemName": select_obj.attr("data_v"),
                                "itemStock": stock,
                                "itemSrcStock": _ipItemStock.val(),
                                "itemSrcName": _ipItemName.val(),
                            };
                        }
                    }

                    task_list[_ipItemCode.val()] = task;
                    task_count = task_count + 1;
                    _taskListBtn.text("任务列表( " + task_count + " )");

                    var td = $("#h" + code);
                    td.empty();
                    td.append("<span class=\"label label-warning\">任务待提交</span>");
                    _upItemDialog.modal('hide');
                });

                //显示任务列表
                function showTaskDailog() {
                    if (task_count < 1) {
                        swal("当前没有需要提交的任务");
                        return;
                    }
                    //删除所有行
                    _taskListTable.empty();
                    //重新生成
                    $.each(task_list, function (k, value) {
                        var txt = "<tr id=\"t" + k + "\"><td>" + k + "</td>";
                        var hType = value.handlerType;
                        if (hType == 0) {
                            txt = txt + "<td><span class=\"label label-purple\">上架商品</span></td><td>" + value.itemName + "</td><td>0</td><td></td><td>" + value.itemStock + "</td>";
                        } else if (hType == 1) {
                            txt = txt + "<td><span class=\"label label-success\">上架库存</span></td><td>" + value.itemName + "</td><td>" + value.itemSrcStock + "</td><td></td><td>" + value.itemStock + "</td>";
                        } else {
                            txt = txt + "<td><span class=\"label label-pink\">切换商品</span></td><td>" + value.itemSrcName + "</td><td>" + value.itemSrcStock + "</td><td>" + value.itemName + "</td><td>" + value.itemStock + "</td>";
                        }
                        txt = txt + "<td><button id=\"delTaskBtn\" data_code=\"" + k + "\" type=\"button\" class=\"btn waves-effect waves-light btn-danger\">删除</button></td>";
                        _taskListTable.append(txt);
                    });
                    _taskListDialog.modal('show');
                }

                //任务列表
                _taskListBtn.on("click", function () {
                    showTaskDailog();
                });

                //删除任务列表里的某项
                _taskListTable.on("click", "button[id=delTaskBtn]", function () {
                    var obj = $(this);
                    var code = obj.attr("data_code");
                    showWarning("确定要删除该任务吗?", function (isConfirm) {
                        if (!isConfirm) {
                            return;
                        }
                        $("#t" + code).remove();//删除ui
                        delete task_list[code];
                        //回复按钮
                        var td = $("#h" + code);
                        if (td != null) {
                            td.empty();
                            td.append(addBtns(td.attr("task_id"), td.attr("data_id"), td.attr("data_code")));
                        }
                        //删除一项任务
                        task_count = task_count - 1;
                        if(task_count < 1) {
                            _taskListDialog.modal('hide');
                        }
                        if(task_count > 0) {
                            _taskListBtn.text("任务列表( " + task_count + " )");
                        } else {
                            _taskListBtn.text("任务列表");
                        }
                    });
                });

                //查看 任务详情
                _clipListTable.on("click", "button[id=seeTaskBtn]", function () {
                    var td = $(this).parent();//找到父 td
                    var task_id = td.attr("task_id");
                    var data_id = td.attr("data_id");
                    var data_code = td.attr("data_code");
                    //取得任务数据
                    tokenPost("${base}/market/checkPrepareActionTask.do?taskId=" + task_id, function (json) {
                        if (json.code != 0) {
                            swal(json.msg);
                        } else {
                            var data = json.response;
                            _ipTaskId.val(data.taskId);
                            _ipTaskItemName.val(data.itemName);
                            _ipTaskItemStock.val(data.itemQuantity);
                            _ipTaskDate.val(data.hopeExecutorDate);

                            //将当前数据注入按钮
                            _cancelTaskBtn.attr("task_id", task_id);
                            _cancelTaskBtn.attr("data_id", data_id);
                            _cancelTaskBtn.attr("data_code", data_code);

                            _taskDialog.modal('show');
                        }
                    }, "json");
                });

                //上架商品
                _clipListTable.on("click", "button[id=upItemBtn]", function () {

                    //如果原来有商品
                    var td = $(this).parent();//找到父 td
                    var task_id = td.attr("task_id");
                    var data_id = td.attr("data_id");
                    var data_code = td.attr("data_code");

                    _upItemOkBtn.attr("data_id", data_id);
                    _upItemOkBtn.attr("data_code", data_code);

                    var tr_ui = td.parent();

                    _lItemTitle.text("上架商品");
                    _ipItemCode.val(tr_ui.find("td[id=iCode]").text());//编码

                    if (data_id == 0) {
                        _handlerType = 0;
                        //原来没有，上架新商品
                        _divItemName.hide();
                        _divItemStock.hide();
                        _divItemTypes.show();
                        _labelItemTypes.text("选择上架商品：");
                        _labelNewItemStock.text("上架库存：");

                        _upItemOkBtn.attr("data_id", "0");
                    } else {
                        _handlerType = 1;
                        _divItemName.show();
                        _divItemStock.show();
                        _divItemTypes.hide();
                        _labelNewItemStock.text("新增库存：");
                        _ipItemName.val(tr_ui.find("td[id=iName]").text());//名称
                        _ipItemStock.val(tr_ui.find("td[id=iStock]").text());//原库存

                    }
                    _upItemDialog.modal('show');
                });

                //原来有商品，现在替换
                _clipListTable.on("click", "button[id=changeItemBtn]", function () {

                    var td = $(this).parent();//找到父 td
                    var task_id = td.attr("task_id");
                    var data_id = td.attr("data_id");
                    var data_code = td.attr("data_code");

                    //切换商品
                    var tr_ui = td.parent();

                    _handlerType = 2;

                    _lItemTitle.text("切换商品");
                    _ipItemCode.val(tr_ui.find("td[id=iCode]").text());//编码

                    _divItemName.show();
                    _divItemStock.show();
                    _divItemTypes.show();

                    _labelItemTypes.text("选择切换商品：");
                    _labelNewItemStock.text("切换库存：");
                    _ipItemName.val(tr_ui.find("td[id=iName]").text());//名称
                    _ipItemStock.val(tr_ui.find("td[id=iStock]").text());//原库存

                    _upItemOkBtn.attr("data_id", data_id);
                    _upItemOkBtn.attr("data_code", data_code);

                    _upItemDialog.modal('show');
                });

                //取消某个任务
                _cancelTaskBtn.on("click", function () {
                    var task_id = $(this).attr("task_id");
                    var data_id = $(this).attr("data_id");
                    var data_code = $(this).attr("data_code");
                    //2次确定
                    showWarning("确定要取消这个任务吗？", function (isConfirm) {
                        if(!isConfirm) {
                            return;
                        }
                        //去取消
                        tokenPost("${base}/market/cancelPrepareActionTask.do?taskId=" + task_id, function (json) {
                            if (json.code != 0) {
                                swal(json.msg);
                            } else {
                                //取消完，关闭详情
                                _taskDialog.modal('hide');
                                //改变按钮样式
                                var td = $("#h" + data_code);
                                if(td != null) {
                                    td.empty();
                                    td.attr("task_id", task_id);
                                    td.attr("data_id", data_id);
                                    td.attr("data_code", data_code);
                                    if (data_id == 0) {
                                        td.append(addBtns(2, 0, data_code));
                                    } else {
                                        td.append(addBtns(1, data_id, data_code));
                                    }
                                }
                            }
                        }, "json");
                    })
                });

                //提交所有任务
                $("#submitTaskBtn").on("click", function () {
                    if(task_count < 1) {
                        swal("当前没有任务可提交");
                        return;
                    }
                    //$(this).button("loading");
                    //将 task 拼成 map;
                    var txt = "";
                    var hDate = "";
                    $.each(task_list, function (k, value) {
                        txt = txt + k + "-" + value.itemId + "-" + value.itemStock + ",";
                    });

                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    tokenPost("${base}/market/prepareAction.do?marketId=" + s_MarketId + "&actionDatas=" + txt + "&hopeExecutorDate=" + hDate, function (data) {
                        //重新刷新
                        if (data.code == 0) {
                            showSuccess(data.msg, function () {
                                open({url:"${base}/market/marketTasks.do?marketId=" + s_MarketId});
                                //location.href = "${base}/market/marketItems.do?id=" + s_MarketId + "&groupCode=" + s_Group + "&unitCode=" + s_Unit + "&floorCode=" + s_Layer;
                            })
                        } else {
                            //$(this).button("reset");
                            btn_.removeAttr("disabled");
                            swal(data.msg);
                        }
                    }, "json");
                });

                //-------------------------------------------------------

                var _emptyTr = $("#emptyTr");
                var _errorInfo = $("#errorInfo");

                function setSelectUi(ui_id, ui_title) {
                    var v = $("#" + ui_id + "");
                    v.append(ui_title);
                    v.select2();
                    return v;
                }

                function setDatas(ui, list) {
                    $.each(list, function (k, v) {
                        var option = '<option value="' + v + '" >' + v + '</option>';
                        ui.append(option);
                    });
                }

                function hibeDatas(msg) {
                    //删除所有tr
                    _clipListTable.find('tr[id=dataTr]').each(function () {
                        $(this).remove();
                    });

                    _emptyTr.show();
                    _errorInfo.text(msg);
                }

                function addBtns(addType, id, locationCode) {
                    if (addType == 0) {
                        return "<button id=\"seeTaskBtn\" type=\"button\""
                                + "class=\"btn waves-effect waves-light btn-danger btn-sm\">查看任务</button>";
                    } else if (addType == 1) {
                        return "<button id=\"upItemBtn\" type=\"button\""
                                + "class=\"btn waves-effect waves-light btn-success btn-sm\">上架库存</button>"
                                + "<button id=\"changeItemBtn\" type=\"button\""
                                + "class=\"m-l-10 btn waves-effect waves-light btn-pink btn-sm\">切换商品</button>";
                    } else {
                        return "<button id=\"upItemBtn\" type=\"button\""
                                + "class=\"btn waves-effect waves-light btn-purple btn-sm\">上架商品</button>";
                    }
                }

                function showDatas(json) {
                    //先删除
                    _clipListTable.find('tr[id=dataTr]').each(function () {
                        $(this).remove();
                    });
                    //隐藏
                    _emptyTr.hide();
                    //生成 tr
                    $.each(json, function (n, value) {
                        var txt = "<tr id=\"dataTr\"><td id=\"iCode\">" + value.locationCode + "</td>"
                                + "<td>" + value.barcode + "</td>"
                                + "<td id=\"iName\">" + value.itemName + "</td>"
                                + "<td>" + value.unitName + "</td>"
                                + "<td id=\"iStock\">" + value.itemQuantity + "</td>";
                        //如果没有任务，则什么也不做
                        if (!task_list[value.locationCode]) {
                            if (value.taskId > 0) {
                                txt = txt + "<td id=\"h" + value.locationCode + "\" task_id=\"" + value.taskId + "\" data_id=\"" + value.itemTemplateId + "\" data_code=\"" + value.locationCode + "\">" + addBtns(0, value.taskId, value.locationCode) + "</td>";
                            } else if (value.itemTemplateId != 0) {
                                txt = txt + "<td id=\"h" + value.locationCode + "\" task_id=\"1\" data_id=\"" + value.itemTemplateId + "\" data_code=\"" + value.locationCode + "\">" + addBtns(1, value.itemTemplateId, value.locationCode) + "</td>";
                            } else {
                                txt = txt + "<td id=\"h" + value.locationCode + "\" task_id=\"2\" data_id=\"0\" data_code=\"" + value.locationCode + "\">" + addBtns(2, 0, value.locationCode) + "</td>";
                            }
                        } else {
                            if (value.taskId > 0) {
                                txt = txt + "<td id=\"h" + value.locationCode + "\" task_id=\"0\" data_id=\"" + value.itemTemplateId + "\" data_code=\"" + value.locationCode + "\"><span class=\"label label-warning\">任务待提交</span></td>";
                            } else if (value.itemTemplateId != 0) {
                                txt = txt + "<td id=\"h" + value.locationCode + "\" task_id=\"1\" data_id=\"" + value.itemTemplateId + "\" data_code=\"" + value.locationCode + "\"><span class=\"label label-warning\">任务待提交</span></td>";
                            } else {
                                txt = txt + "<td id=\"h" + value.locationCode + "\" task_id=\"2\" data_id=\"0\" data_code=\"" + value.locationCode + "\"><span class=\"label label-warning\">任务待提交</span></td>";
                            }
                        }
                        _clipListTable.append(txt);
                    });
                }

                var _sMarket = $("#sMarket");
                _sMarket.select2();

                var _m_group_ui = $("#m_group");
                _m_group_ui.select2();

                var _m_unit_ui = setSelectUi("m_unit", title[0]);
                var _m_layer_ui = setSelectUi("m_layer", title[1]);

                _sMarket.change(function () {

                    var select_obj = _sMarket.find("option:selected");
                    s_MarketId = select_obj.attr("data_id");
                    if (s_MarketId == "0") {
                        return;
                    }

                    //如果有任务，则提醒，否则再切换，否则 直接切换
                    if (task_count > 0) {
                        swal({
                            title: "提醒",
                            text: "当前店铺有上架任务未提交，确定丢弃切换 店铺？",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "丢弃并切换店铺",
                            cancelButtonText: "查看任务列表",
                            closeOnConfirm: false,
                            closeOnCancel: false
                        }, function (isConfirm) {
                            if (isConfirm) {
                                //直接切换
                                open({url:"${base}/market/marketShelves.do?marketId=" + s_MarketId});
                                //location.href = "${base}/market/marketShelves.do?id=" + s_MarketId;
                            } else {
                                //弹出任务列表
                                showTaskDailog();
                            }
                        });
                    } else {
                        //直接切换
                        open({url:"${base}/market/marketShelves.do?marketId=" + s_MarketId});
                        //location.href = "${base}/market/marketShelves.do?id=" + s_MarketId;
                    }
                });

                _m_group_ui.change(function () {
                    _m_unit_ui.empty();
                    _m_unit_ui.append(title[0]);

                    var select_obj = _m_group_ui.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    if (data_id == "0") {
                        return;
                    }

                    s_Group = select_obj.val();

                    if (unit_list[s_Group] == null) {
                        tokenPost("${base}/market/getShelvesMarks.do?marketId=" + s_MarketId + "&shelvesType=2&groupCode=" + s_Group, function (json) {
                            if (json.code != 0) {
                                swal("无法找到该店铺的 单元 信息");
                            } else {
                                unit_list[s_Group] = json.response.datas;
                                setDatas(_m_unit_ui, unit_list[s_Group]);
                                _m_unit_ui.change();
                            }
                        }, "json");
                    } else {
                        //更改
                        setDatas(_m_unit_ui, unit_list[s_Group]);
                        _m_unit_ui.change();
                    }
                });

                _m_unit_ui.change(function () {
                    _m_layer_ui.empty();
                    _m_layer_ui.append(title[1]);

                    var select_obj = _m_unit_ui.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    if (data_id == "0") {
                        return;
                    }

                    s_Unit = select_obj.val();

                    if (level_list[s_Unit] == null) {
                        tokenPost("${base}/market/getShelvesMarks.do?marketId=" + s_MarketId + "&shelvesType=3&groupCode=" + s_Group + "&unitCode=" + s_Unit, function (json) {
                            if (json.code != 0) {
                                swal("无法找到该店铺的 层 信息");
                            } else {
                                level_list[s_Unit] = json.response.datas;
                                setDatas(_m_layer_ui, level_list[s_Unit]);
                                _m_layer_ui.change();
                            }
                        }, "json");
                    } else {
                        //更改
                        setDatas(_m_layer_ui, level_list[s_Unit]);
                        _m_layer_ui.change();
                    }

                });

                _m_layer_ui.change(function () {
                    var select_obj = _m_layer_ui.find("option:selected");
                    var data_id = select_obj.attr("data_id");

                    if (data_id == "0") {
                        hibeDatas("暂无任何数据");
                        return;
                    }
                    s_Layer = select_obj.val();

                    tokenPost("${base}/market/loaderClipDatas.do?marketId=" + s_MarketId + "&groupCode=" + s_Group + "&unitCode=" + s_Unit + "&floorCode=" + s_Layer, function (json) {

                        if (json.code != 0) {
                            hibeDatas(json.msg);
                        } else {
                            var data = json.response.datas;
                            showDatas(data);
                        }
                    }, "json");

                });

            });
        </script>