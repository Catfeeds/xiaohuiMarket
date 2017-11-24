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
                        <h4 class="page-title "><b>优惠券详情</b></h4>
                    </div>
                </div>
            </div>
            <button type="button" id="returnBtn" class="btn btn-primary" style="padding-right:0px 20px;margin-bottom: 22px;" onclick="javascript:history.go(-1);"><i class="fa fa-backward"></i> 返回列表</button>
            <div class="card-box">
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>基本信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <#if rule?exists>
                                        <tbody>
                                        <tr>
                                            <td style="background-color: #f9f9f9">优惠券类型</td>
                                            <td><#if rule.activeRuleType=1>注册赠券</#if></td>
                                            <td style="background-color: #f9f9f9">优惠券名称</td>
                                            <td>${rule.activeRuleName}</td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">优惠券标题</td>
                                            <td>${rule.activeRuleTitle}</td>
                                            <td style="background-color: #f9f9f9">优惠券总张数</td>
                                            <td><#if rule.activeDistributeCount=-1>没有上限<#else>${rule.activeDistributeCount}张</#if></td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">优惠券金额</td>
                                            <td>${rule.activeMonery}元</td>
                                            <td style="background-color: #f9f9f9">是否过期</td>
                                            <td>
                                                <#if rule.activeStatus=0><text class="text-success">未过期</text>
                                                <#else><text class="text-danger">已过期</text>
                                                </#if>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </#if>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-8">
                        <div class="advert_container">
                            <h5 class="page-title" style="padding-top: 20px"><b>使用规则</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                <#if rule?exists>
                                    <tbody>
                                        <tr>
                                            <td style="background-color: #f9f9f9">使用门槛</td>
                                            <td><#if rule.activeRuleOrderMoney=-1>不限金额<#else>购满${rule.activeRuleOrderMoney}元</#if></td>
                                            <td style="background-color: #f9f9f9">每人限领</td>
                                            <td>1张</td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">可使用商品</td>
                                            <td><#if rule.activeScene=1>全部商品</#if></td>
                                            <td style="background-color: #f9f9f9">有效时间</td>
                                            <td><#if rule.effectiveType=-1>不限时<#elseif rule.effectiveType=1>${rule.activeRuleEffective}<#else>${rule.activeBeginTime}<br/>${rule.activeEndTime}</#if></td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">使用规则</td>
                                            <td colspan="3"><#if rule.activeRuleExplain!=''>${rule.activeRuleExplain}<#else>无</#if></td>
                                        </tr>
                                        <tr>
                                            <td style="background-color: #f9f9f9">备注</td>
                                            <td colspan="3"><#if rule.bak!=''>${rule.bak}<#else>无</#if></td>
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
                            <h5 class="page-title" style="padding-top: 20px"><b>使用信息</b></h5>
                            <hr style="height:1px;width:100%;border:none;border-top:1px dashed #ccc;"/>
                            <div class="table-responsive advert_detail_table">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th style="background-color: #f9f9f9">优惠券编号</th>
                                        <th style="background-color: #f9f9f9">所属用户</th>
                                        <th style="background-color: #f9f9f9">订单号</th>
                                        <th style="background-color: #f9f9f9">使用时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#if (records?size > 0)>
                                        <#list records as record>
                                        <tr>
                                            <td>${record.activeId}</td>
                                            <td>${record.userName}</td>
                                            <td>${record.orderId}</td>
                                            <td>${record.createTime}</td>
                                        </tr>
                                        </#list>
                                    <#else>
                                        <tr>
                                            <td colSpan="11" height="100px">
                                                <p class="text-center" style="line-height: 100px">暂无任何数据</p>
                                            </td>
                                        </tr>
                                    </#if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

