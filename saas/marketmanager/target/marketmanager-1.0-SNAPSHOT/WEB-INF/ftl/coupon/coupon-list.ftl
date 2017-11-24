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
                        <h4 class="page-title"><b>优惠券列表</b></h4>
                    </div>
                </div>
            </div>

            <div class="card-box">
                <form class="form-inline" role="form" action="${base}/coupon/couponPage.do">
                    <div class="form-group m-l-15">
                        <label>优惠券名称：</label>
                        <input type="text" class="form-control" placeholder="输入优惠券名称" name="activeRuleName">
                    </div>
                    <div class="form-group m-l-15">
                        <label>优惠券类型：</label>
                        <select id="houseSelect" class="form-control" name="activeRuleType">
                            <option value="1">注册赠券</option>
                            <#--<#if coupons?exists >-->
                                <#--<#list coupons as coupon>-->
                                    <#--<option value="${coupon.id}">注册优惠</option>-->
                                <#--</#list>-->
                            <#--</#if>-->
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <label>是否过期：</label>
                        <select id="status" class="form-control" name="activeStatus">
                            <option value="-1">全部</option>
                            <option value="0">未过期</option>
                            <option value="1">已过期</option>
                        </select>
                    </div>
                    <div class="form-group m-l-15">
                        <button type="submit" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i> 搜索</button>
                    </div>
                </form>
            </div>

            <button type="button" id="addCouponBtn" class="btn btn-primary pull-right" style="padding-left: 30px;padding-right: 30px;margin-right: 30px;margin-bottom: 20px"><i class="fa fa-plus"></i>添加优惠券</button>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-striped table-bordered">
                        <thead class="table_head">
                        <tr>
                            <th>编号</th>
                            <th>优惠券名称</th>
                            <th>优惠券类型</th>
                            <th>可使用商品</th>
                            <th>使用门槛</th>
                            <th>优惠金额</th>
                            <th>总发行量</th>
                            <th>有效期(范围/天)</th>
                            <th>是否过期</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="couponInfoTable">
                        <#if (coupons?size>0)>
                            <#list coupons as coupon>
                            <tr id="tr_${coupon_index}">
                                <td>${coupon.id}</td>
                                <td>${coupon.activeRuleName}</td>
                                <td><#if coupon.activeRuleType=1>注册赠券</#if></td>
                                <td><#if coupon.activeScene=1>全部商品</#if></td>
                                <td><#if coupon.activeRuleOrderMoney=-1>不限金额<#else>购满${coupon.activeRuleOrderMoney}元</#if></td>
                                <td>${coupon.activeMonery?c}</td>
                                <td><#if coupon.activeDistributeCount=-1>没有上限<#else>${coupon.activeDistributeCount}</#if></td>
                                <td>
                                    <#if coupon.effectiveType=1>
                                        <#if coupon.activeRuleEffective=-1>
                                            不限时
                                        <#else>
                                            ${coupon.activeRuleEffective}&nbsp;天
                                        </#if>
                                    <#else>
                                        ${coupon.activeBeginTime}<br/>${coupon.activeEndTime}
                                    </#if>
                                </td>
                                <#if coupon.activeStatus=0>
                                    <td class="text-success"><b>未过期</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${coupon.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${coupon.id}">编辑</button>
                                        <button id="deleBtn" type="button" class="btn btn-danger btn-sm" data_id="${coupon.id}" data_status="${coupon.id}">删除</button>
                                    </td>
                                <#else>
                                    <td class="text-danger"><b>已过期</b></td>
                                    <td>
                                        <button id="lookBtn" type="button" class="btn btn-primary btn-sm" data_id="${coupon.id}">查看</button>
                                        <button id="editBtn" type="button" class="btn btn-primary btn-sm" data_id="${coupon.id}">编辑</button>
                                        <button id="deleBtn" type="button" class="btn btn-danger btn-sm" data_id="${coupon.id}" data_status="${coupon.id}">删除</button>
                                    </td>
                                </#if>
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
                    <@paginate nowPage=pageIndex itemCount=count action="${base}/coupon/couponPage.do?activeRuleName=${activeRuleName}&activeRuleType=${activeRuleType}&activeStatus=${activeStatus}"/>
                </div>
            </div>
            <!--/分页-->
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
         //取url参数给表单赋值
        function GetQueryString(name) {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }

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
        //添加优惠券
        $("#addCouponBtn").on('click',function () {
            location.href = "${base}/coupon/couponAdd.do";
        });

        //操作
    <#if (coupons?size > 0)>
        $("#couponInfoTable").find('button[id=lookBtn]').each(function () {//查看
            var that = this;
            $(this).on('click', function () {
                <#--open({url:"${base}/coupon/couponDetail.do?id="+$(that).attr("data_id")});-->
                open({url:"${base}/coupon/couponDetail.do?activeRuleID="+$(that).attr("data_id")});
            });
        });
        $("#couponInfoTable").find('button[id=editBtn]').each(function () {//编辑
            var that = this;
            $(this).on('click', function () {
                open({url:"${base}/coupon/couponEdit.do?activeRuleID="+$(that).attr("data_id")});
            });
        });
        $("#couponInfoTable").find('button[id=deleBtn]').each(function () {//删除
            var that = this;
            $(this).on('click', function () {
                $("#stopModal").modal('show');
                $("#stopOkBtn").on('click',function () {
//                    var txt = $("#stopRemark").val();
                    $.post("${base}/coupon/delCoupon.do?activeRuleID=" +$(that).attr("data_id"), function(data) {
                        //重新刷新
                        if(data.code == "0") {
                            $("#stopModal").modal('hide');
                            swal("提示", "删除成功", "success");
                            setTimeout(function(){location.reload();},1000);
                        } else {
                            swal("提示", data.msg, "error");
                        }
                    }, "json");
                })
            });
        });
    </#if>
    });
</script>

<!--删除弹窗-->
<div class="modal fade" id="stopModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">
                <#--<form class="form-horizontal" role="form">-->
                	<#--<div class="form-group">-->
                        <#--<label class="col-md-4 control-label">请说明删除优惠券的原因：</label>-->
                    <#--</div>-->
                    <#--<div class="form-group">-->
                        <#--<div class="col-md-12">-->
                            <#--<textarea class="form-control" rows="6" id="stopRemark"></textarea>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</form>-->
                    <p style="text-align: center">确定要删除该优惠券吗？</p>
            </div>
            <div class="modal-footer" style="text-align: center">
                <button id="stopNoBtn" type="button" class="btn btn-primary" style="padding:10px 80px" data-dismiss="modal">取消</button>
                <button id="stopOkBtn" type="button" class="btn btn-primary" style="padding:10px 80px">确定</button>
            </div>
        </div>
    </div>
</div>