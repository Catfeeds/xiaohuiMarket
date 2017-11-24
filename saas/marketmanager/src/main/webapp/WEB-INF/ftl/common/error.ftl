<#include "header.ftl" >


<#include "menu.ftl"><@menu tab="${tabName}" tab_child="${tabChild}" />

<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="content-page">
    <!-- Start content -->
    <div class="content">
        <div class="container">

            <div class="row">

                <div class="col-sm-12">
                    <h4>远程调用异常：${error}</h4>
                </div>

            </div>
            <!-- end row -->
        </div>
        <!-- end container -->
    </div>
    <!-- end content -->


<#include "bottom.ftl" >