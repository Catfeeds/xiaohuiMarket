<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">
                <div class="col-sm-12">
                    <div class="page-title-box">
                        <ol class="breadcrumb pull-right">
                            <li><a href="#">首页</a></li>
                            <li><a href="#">商品管理</a></li>
                            <li><a href="#">商品分类</a></li>
                            <li class="active"><#if itemType?exists>编辑<#else>添加</#if>商品分类</li>
                        </ol>
                        <h4 class="page-title"><b><#if itemType?exists>编辑<#else>添加</#if>商品分类</b></h4>
                    </div>
                </div>
            </div>

            <div class="row m-t-30">
                <div class="col-md-8">
                    <form class="form-horizontal" role="form">
                        <div id="sBtn" class="form-group">
                            <div class="col-md-4"></div>
                            <div class="col-md-8">
                                <div class="radio radio-primary radio-inline">
                                    <input id="addBtnA" type="radio" name="radio3" value="option3">
                                    <label for="addBtnA">
                                        添加一级分类
                                    </label>
                                </div>
                                <div class="radio radio-primary radio-inline">
                                    <input id="addBtnB" type="radio" name="radio3" value="option3" <#if pItemType?exists>checked</#if>>
                                    <label for="addBtnB">
                                        添加二级分类
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">商品分类名称：</label>
                            <div class="col-md-8">
                                <input id="itName" type="text" class="form-control" <#if itemType?exists>value="${itemType.title}"</#if>>
                            </div>
                        </div>

                        <div id="typesAlist" class="form-group">
                            <label class="col-md-4 control-label">绑定一级分类：</label>
                            <div class="col-md-8">
                                <select class="form-control" id="itSelect">
                                <#if itemTypes?exists && (itemTypes?size > 0)>
                                    <#list itemTypes as type>
                                        <option data_id="${type.id?c}"
                                                <#if pItemType?exists && pItemType.id == type.id>
                                                selected
                                                <#else>
                                                <#if itemType?exists && itemType.parentId == type.id>
                                                selected </#if>
                                                </#if>
                                                >${type.title}
                                        </option>
                                    </#list>
                                <#else>
                                    <option data_id="0">无</option>
                                </#if>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">分类图标：</label>
                            <div class="col-md-4">
                                <div id="itemMainImgDiv" style="background: white; width:150px; height:150px;">
                                    <img id="upImg" <#if itemType?exists && itemType.icon??>
                                         src="${itemType.icon}" </#if>/>
                                </div>
                                <p class="m-t-10">
                                    <button id="selectFileBtn" type="button"
                                            class="btn waves-effect waves-light btn-default">
                                        选择图标
                                    </button>
                                    <button id="removeFileBtn" type="button"
                                            class="btn waves-effect waves-light btn-default">
                                        删除图标
                                    </button>
                                </p>
                            </div>
                        </div>

                        <br><br>

                        <div class="form-group">
                            <div class="col-md-4"></div>
                            <button id="saveBtn" type="button" class="btn waves-effect waves-light btn-primary col-md-2">确定</button>
                            <button id="backBtn" type="button" class="btn waves-effect waves-light btn-default col-md-2">返回</button>
                        </div>

                        <br><br><br><br><br>
                    </form>
                </div>
            </div>

            <!-- end container -->
        </div>

        <script src="${res}/assets/plugins/alioss/plupload-2.1.2/js/plupload.full.min.js"></script>
        <script src="${res}/assets/plugins/alioss/plupload-2.1.2/my_upload.js"></script>

        <script type="text/javascript">

            $(document).ready(function () {

                var itemTypeId = 0;
                var _typesAlist = $("#typesAlist");
                var addType = 1;//添加类型，1=一级，2=二级
                var _imgUrl = <#if itemType?exists >"${itemType.icon}";<#else>"";</#if>
                //如果原来有图片
                if(_imgUrl != "") {
                    srcImgBool = true;
                }
                function postSuccess() {
                    var msg = "添加成功";
                    if(itemTypeId != 0) {
                        msg = "修改成功";
                    }
                    showSuccess(msg, function () {
                        open({url:"${base}/item/itemTypes.do"});
                    });
                }
                //上传图片
                function updateImgUrl(itemTypeId, itemImgUrl) {
                    tokenPost("${base}/item/itemTypeUpdateIconUrl.do?itemTypeId="+itemTypeId+"&itemTypeIconUrl="+itemImgUrl, function(data) {
                        //重新刷新
                        postSuccess();
                    }, "json");
                }

                $("#removeFileBtn").on('click', function () {
                    _imgUrl = "";
                    clear_img();
                });

                upImgName = "upImg";
                serverUrl = "${base}/oss/uploadImg.do?targetDir=itemtype";

                function upImgFunc(itemTypeId, itemImgKey) {
                    upCallback = function (hType, obj) {
                        if(hType == 0) {
                            //没有图片提交
                            alert("没有图片需要提交");
                        } else if(hType == 1) {
                            //开始提交
                        } else if(hType == 2) {
                            //进度监听
                        } else if(hType == 100) {
                            //完成，更新回对应的图片
                            updateImgUrl(itemTypeId, obj);
                        } else {
                            //有错误
                        }
                    };
                    set_upload_param(uploader, itemImgKey);
                }

            <#if itemType?exists>
                    itemTypeId = ${itemType.id?c};
                    //隐藏
                    $("#sBtn").hide();

                    //如果是修改一级，则隐藏2级选项
                    <#if itemType.parentId == 0>
                        _typesAlist.hide();
                    </#if>

                </#if>

                <#if !(itemType?exists) && !(pItemType?exists) >
                    //默认选中一级
                    $("#addBtnA").attr("checked", true);
                    //隐藏二级
                    _typesAlist.hide();
                    //添加点击切换
                    $("#addBtnA").on('click', function () {
                        if($(this).is(':checked')) {
                            //隐藏二级
                            addType = 1;
                            _typesAlist.hide();
                        }
                    });
                    //添加点击切换
                    $("#addBtnB").on('click', function () {
                        if($(this).is(':checked')) {
                            //显示二级
                            addType = 2;
                            _typesAlist.show();
                        }
                    });
                </#if>

                <#if pItemType?exists>
                    $("#sBtn").hide();
                </#if>

                moveEnd($("#itName").get(0));

                $("#backBtn").on('click', function () {
                    history.back(-1);
                });

                //保存商品单位
                $("#saveBtn").on('click', function () {

                    //检测
                    var title = $("#itName").val();
                    if (title == "") {
                        swal("请输入分类名称!");
                        return;
                    }

                    //检测长度
                    if(strlen(title) > 8) {
                        swal("分类名称不能超过4个中文或者8个字符!");
                        return;
                    }

                    var _typeId = 0;
                    if(addType == 2) {
                        _typeId = $("#itSelect").find("option:selected").attr("data_id");
                    }

                    var post_data = {
                        itemTypeId: itemTypeId,
                        parentId: _typeId,
                        title: title,
                        icon: _imgUrl,
                    }

                    var btn_ = $(this);
                    btn_.attr("disabled", true);

                    //保存
                    tokenPresPost("${base}/item/itemTypeEditSave.do", post_data, function(data) {

                        //重新刷新
                        if(data.code == "0") {
                            var itemType = data.response.datas;
                            //如果需要更新图片路径
                            if(upImgBool) {
                                upImgFunc(itemType.id, itemType.id);
                            } else {
                                postSuccess();
                            }
                        } else {
                            //$(this).button("reset");
                            btn_.removeAttr("disabled");
                            swal(data.msg);
                        }
                    }, "json");

                });
            });
        </script>