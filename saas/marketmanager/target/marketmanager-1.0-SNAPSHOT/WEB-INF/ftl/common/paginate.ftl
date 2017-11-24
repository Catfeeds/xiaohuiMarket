<#--
  自定义的分页指令 (powered by zhumg)
   属性：
   nowPage
   itemCount
   pageItemCount
   action
 -->
<#macro paginate nowPage=1 itemCount=1 pageItemCount=10 action="">

    <#local allPage = (itemCount / pageItemCount)?int >
    <#if (itemCount/pageItemCount > 0) >
        <#local allPage = allPage + 1>
    </#if>

    <#if (allPage < 1) >
        <#local allPage = 1>
    </#if>

    <#local startPage = 0>
    <#local endPage = 0>

    <#if (nowPage <= 3) >
        <#local startPage = 1>
    <#else>
        <#local startPage = nowPage - 2>
    </#if>

    <#local endPage = nowPage + 2>

    <#if (endPage >= allPage) >
        <#local endPage = allPage>
    </#if>

<form class="form-inline pull-right">
    <ul class="pagination">
        <#if ((endPage-startPage)>0) >
            <#if (nowPage > 1) >
                <#assign apage=nowPage-1>
                <li class="paginate_button previous">
                    <a href="javascript:void(0);" onclick="clickGoto(${apage})">上一页</a></li>
            </#if>

            <#list startPage..endPage as i>
                <#if nowPage == i>
                    <li class="paginate_button active"><a href="#">${i}</a></li>
                <#else>
                    <li class="paginate_button "><a href="javascript:void(0);" onclick="clickGoto(${i})">${i}</a>
                    </li>
                </#if>
            </#list>

            <#if (nowPage < allPage) >
                <#assign apage=nowPage+1>
                <li class="paginate_button next"><a href="javascript:void(0);" onclick="clickGoto(${apage})">下一页</a>
                </li>
            </#if>

            <li>
                <div class="input-group">
                    <span class="input-group-addon">共${allPage}页</span>
                    <input type="text" class="form-control" id="pageNum" style="width: 60px;">
                    <span class="input-group-addon">页</span>
                    <span class="input-group-btn">
			             <button type="button" class="btn btn-primary waves-effect waves-light" onclick="clickGoto(0)">跳转</button>
			         </span>
                </div>
            </li>
        <#else>
            <li>
                <div class="input-group">
                    <span class="input-group-addon">共${allPage}页</span>
                </div>
            </li>
        </#if>
    </ul>
</form>

<script type="text/javascript">
    function clickGoto(nowPage) {
        if (nowPage == 0) {
            nowPage = $('#pageNum').val();
        }
        if (nowPage == "" || containSpecial.test(nowPage)) {
            return;
        }
        if (nowPage < 0 || nowPage > ${allPage}) {
            swal("页码超出范围!");
            return;
        }
        open({url:"${action}&pageSize=${pageItemCount}&pageIndex=" + nowPage});
        //location.href = "${action}&pageSize=${pageItemCount}&pageIndex=" + nowPage;
    }
</script>

</#macro>