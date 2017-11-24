<#macro menu tab="index" tab_child="z">

</head>


<body class="fixed-left">

<!-- Begin page -->
<div id="wrapper">

    <!-- Top Bar Start -->
    <div class="topbar" style="height: 58px">

        <!-- LOGO -->
        <div class="topbar-left">
            <div class="text-center">
                <a href="${base}/passport/main.do" class="logo"><i class="fa fa-windows"></i> <span>智能超市后台管理系统</span>
                </a>
            </div>
        </div>

        <!-- Navbar -->
        <div class="navbar navbar-default" role="navigation">
            <div class="container">
                <div class="">
                    <div class="pull-left">
                        <button class="button-menu-mobile open-left waves-effect">
                            <i class="fa fa-bars"></i>
                        </button>
                        <span class="clearfix"></span>
                    </div>

                    <ul class="nav navbar-nav navbar-right pull-right">
                        <li><a href=""/>当前用户：</a></li>
                        <li class="dropdown">
                            <a id="loginUserUi" href="#" class="waves-effect"
                               role="button"></a>
                            <#--<a id="loginUserUi" href="#" class="dropdown-toggle waves-effect" data-toggle="dropdown"-->
                               <#--role="button" aria-haspopup="true" aria-expanded="false"></a>-->
                            <#--<ul class="dropdown-menu">-->
                                <#--<li><a href="#" id="userInfoBtn"><i class="fa fa-user"></i>&nbsp;帐户信息</a></li>-->
                                <#--<li><a href="#" id="logoutBtn"><i class="fa fa-sign-out"></i>&nbsp;退出</a></li>-->
                            <#--</ul>-->
                        </li>
                    </ul>

                </div>
                <!--/.nav-collapse -->
            </div>
        </div>
    </div>
    <!-- Top Bar End -->


    <!-- ========== Left Sidebar Start ========== -->
    <div class="left side-menu">
        <div class="sidebar-inner slimscrollleft">

            <div id="sidebar-menu">
                <ul>

                    <li>
                        <a href="${base}/passport/main.do"
                           class="waves-effect waves-primary <#if tab=="index"> active </#if> "><i
                                class="fa fa-home"></i><span> 首  页 </span></a>
                    </li>


                    <li class="has_sub">
                        <a href="javascript:void(0);"
                           class="waves-effect waves-primary <#if tab=="order"> active </#if> "><i
                                class="fa fa-th-list"></i>
                            <span> 订单管理 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled">
                            <li <#if tab_child=="orders"> class="active" </#if>><a
                                    href="${base}/order/orders.do">订单列表</a></li>
                            <li <#if tab_child=="torders"> class="active" </#if>><a
                                    href="${base}/order/orders.do?orderState=2048">退款订单</a></li>
                        </ul>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);"
                           class="waves-effect waves-primary <#if tab=="item"> active </#if>"><i
                                class="fa fa-cubes"></i><span> 商品管理 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled">
                            <li <#if tab_child=="itemplates"> class="active" </#if>><a href="${base}/item/itemList.do">商品模板库</a>
                            </li>
                            <li <#if tab_child=="itypes"> class="active" </#if>><a
                                    href="${base}/item/itemTypes.do">商品分类</a></li>
                            <li <#if tab_child=="iunits"> class="active" </#if>><a
                                    href="${base}/item/itemUnits.do">商品单位</a></li>
                        </ul>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);"
                           class="waves-effect waves-primary <#if tab=="market"> active </#if>"><i
                                class="fa fa-sitemap"></i>
                            <span> 店铺管理 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled">
                            <li <#if tab_child=="markets"> class="active" </#if>><a
                                    href="javascript:open({url:'${base}/market/markets.do'})">店铺列表</a>
                            </li>
                            <li <#if tab_child=="mitems"> class="active" </#if>><a href="${base}/market/marketItems.do">店铺商品</a>
                            </li>
                            <li <#if tab_child=="shelves"> class="active" </#if>><a
                                    href="${base}/market/marketShelves.do">货架商品</a></li>
                            <li <#if tab_child=="stasks"> class="active" </#if>><a href="${base}/market/marketTasks.do">店铺任务</a>
                            <li <#if tab_child=="staskerrors"> class="active" </#if>><a
                                    href="${base}/market/marketErrorTasks.do">店铺异常任务</a>
                            </li>
                            <li <#if tab_child=="mmac"> class="active" </#if>><a href="${base}/market/marketBindMacList.do">店铺绑定硬件</a>
                            </li>
                            <li <#if tab_child=="muser"> class="active" </#if>><a href="${base}/market/marketBindUserList.do">店铺绑定管理员</a>
                            </li>

                            <li <#if tab_child=="streets"> class="active" </#if>><a href="${base}/market/streets.do">街道信息</a>
                            </li>
                        </ul>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect waves-primary <#if tab=="purchase"> active </#if>"><i class="fa fa-shopping-cart"></i>
                            <span> 采购管理 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled" <#if tab=="purchase"> style="display: block;" </#if> >
                            <li <#if tab_child=="purchases"> class="active" </#if> ><a href="${base}/purchase/purchasePage.do">采购单列表</a></li>
                            <li <#if tab_child=="suppliers"> class="active" </#if> ><a href="${base}/purchase/supplierPage.do">供应商列表</a></li>
                            <li <#if tab_child=="stocks"> class="active" </#if> ><a href="${base}/purchase/commodityStoresPage.do">商品库存列表</a></li>
                            <li <#if tab_child=="houses"> class="active" </#if> ><a href="${base}/purchase/warehousePage.do">仓库列表</a></li>
                        </ul>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect waves-primary <#if tab=="advert"> active </#if>"><i class="fa fa-youtube-play"></i>
                            <span> 广告管理 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled" <#if tab=="advert"> style="display: block;" </#if> >
                            <li <#if tab_child=="adverts"> class="active" </#if> ><a href="${base}/advert/adverts.do">广告列表</a></li>
                            <li <#if tab_child=="admanager"> class="active" </#if> ><a href="${base}/advert/advertScreens.do">投放管理</a></li>
                            <li <#if tab_child=="adgroups"> class="active" </#if> ><a href="${base}/advert/groupPage.do">分组管理</a></li>
                        </ul>
                    </li>


                <#--<li class="has_sub">-->
                <#--<a href="javascript:void(0);" class="waves-effect waves-primary <#if tab=="user"> active </#if>"><i-->
                <#--class="fa fa-users"></i><span> 用户管理 </span>-->
                <#--<span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>-->
                <#--<ul class="list-unstyled" <#if tab=="user"> style="display: block;" </#if> >-->
                <#--<li><a href="${base}/passport/userlist.do">用户列表</a></li>-->
                <#--<li><a href="form-advanced.html">活跃用户</a></li>-->
                <#--<li><a href="form-validation.html">沉默用户</a></li>-->
                <#--</ul>-->
                <#--</li>-->

                <#--<li class="has_sub">-->
                <#--<a href="javascript:void(0);" class="waves-effect waves-primary <#if tab=="table"> active </#if>"><i-->
                <#--class="fa fa-line-chart"></i><span> 统计报表 </span>-->
                <#--<span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>-->
                <#--<ul class="list-unstyled" <#if tab=="table"> style="display: block;" </#if> >-->
                <#--<li><a href="tables-basic.html">订单数据统计</a></li>-->
                <#--<li><a href="tables-datatable.html">店铺数据统计</a></li>-->
                <#--<li><a href="tables-editable.html">用户数据统计</a></li>-->
                <#--</ul>-->
                <#--</li>-->

                    <li class="has_sub">
                        <a href="javascript:void(0);"
                           class="waves-effect waves-primary <#if tab=="admin"> active </#if>"><i
                                class="fa fa-user"></i><span> 帐号管理 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled" <#if tab=="admin"> style="display: block;" </#if>>
                            <li <#if tab_child=="addpassport"> class="active" </#if>><a
                                    href="${base}/passport/addpassport.do">分配帐号</a>
                            </li>
                            <li <#if tab_child=="passports"> class="active" </#if>><a
                                    href="${base}/passport/passportList.do">帐号列表</a>
                            </li>
                        </ul>
                    </li>

                    <#--<li class="has_sub">-->
                        <#--<a href="javascript:void(0);"-->
                           <#--class="waves-effect waves-primary <#if tab=="sys"> active </#if>"><i-->
                                <#--class="fa fa-cog"></i><span> 系统设置 </span>-->
                            <#--<span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>-->
                        <#--<ul class="list-unstyled" <#if tab=="userinfo"> style="display: block;" </#if>>-->
                            <#--<li><a href="chart-flot.html">个人信息</a></li>-->
                        <#--</ul>-->
                        <#--<ul class="list-unstyled" <#if tab=="appver"> style="display: block;" </#if>>-->
                            <#--<li><a href="chart-flot.html">App版本管理</a></li>-->
                        <#--</ul>-->
                    <#--</li>-->

                    <li class="has_sub">
                        <a href="javascript:void(0);"
                           class="waves-effect waves-primary <#if tab=="sys"> active subdrop </#if>"><i
                                class="fa fa-cog"></i><span> 系统设置 </span>
                            <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                        <ul class="list-unstyled" <#if tab=="sys"> style="display: block;" </#if>>
                        <#--<li><a href="chart-flot.html">个人信息</a></li>-->
                            <li class="has_sub">
                                <a href="javascript:void(0);"
                                   class="waves-effect waves-primary <#if tab=="role"> active </#if>"><i
                                        class="fa fa-users"></i><span> 角色管理 </span>
                                    <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                                <ul class="list-unstyled" <#if tab=="role"> style="display: block;" </#if>>
                                    <li <#if tab_child=="rolelist"> class="active" </#if>><a href="${base}/role/roles.do">角色列表</a></li>
                                </ul>
                            </li>
                            <li class="has_sub">
                                <a href="javascript:void(0);"
                                   class="waves-effect waves-primary <#if tab=="operation"> active </#if>"><i
                                        class="fa fa-wrench"></i><span> 权限管理 </span>
                                    <span class="menu-arrow"><i class="fa fa-angle-down"></i></span></a>
                                <ul class="list-unstyled" <#if tab=="operation"> style="display: block;" </#if>>
                                    <li <#if tab_child=="operationlist"> class="active" </#if>><a href="${base}/operation/operations.do">权限列表</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>

                </ul>
            </div>
        </div>
    </div>
    <!-- Left Sidebar End -->

    <script type="text/javascript">

        $(document).ready(function () {

            var loginShowName = $.cookie("showName");
            if (loginShowName != null) {
                $("#loginUserUi").html(loginShowName);//+ " <span class=\"caret\"></span>"
            }
//
//            //帐号信息
//            $("#userInfoBtn").on('click', function () {
//
//            });
//
//            //退出
//            $("#logoutBtn").on('click', function () {
//
//            });

        });
    </script>


</#macro>