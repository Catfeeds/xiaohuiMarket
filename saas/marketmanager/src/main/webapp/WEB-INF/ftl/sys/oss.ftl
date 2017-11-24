

<script src="${res}/assets/plugins/alioss/plupload-2.1.2/js/plupload.full.min.js"></script>

<div class="modal" tabindex="-1" role="dialog" id="upImgDialog" >
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="mySmallModalLabel">正在上传图片</h4>
            </div>
            <div class="modal-body">
                <div class="progress">
                    <div id="upImgBar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
                        <span class="sr-only"></span>
                    </div>
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

                <div class="form-group">
                    <label class="col-md-4 control-label">商品主图：</label>
                    <div class="col-md-4">
                        <div id="itemMainImgDiv" style="background: white; width:150px; height:150px;">
                            <img id="upImg"/>
                        </div>
                        <p class="m-t-10">
                            <button id="selectFileBtn" type="button"
                                    class="btn waves-effect waves-light btn-default">
                                选择主图
                            </button>
                        </p>
                    </div>
                </div>



            </div>



            <div class="row">

                <div class="form-group m-t-20">
                    <div class="col-md-4">
                    </div>
                    <button id="saveBtn" type="button"
                            class="btn waves-effect waves-light btn-primary col-md-2">确定
                    </button>
                </div>
            </div>
        </div>



        <script src="${res}/assets/plugins/alioss/plupload-2.1.2/my_upload.js"></script>


        <script type="text/javascript">
            $(document).ready(function () {

                upImgName = "upImg";
                serverUrl = "${base}/oss/uploadImg.do?targetDir=item";
                upCallback = function (hType, obj) {
                    if(hType == 0) {
                        //没有图片提交
                        alert("没有图片需要提交");
                    } else if(hType == 1) {
                        //开始提交
                        $("#upImgDialog").show();
                    } else if(hType == 2) {
                        //进度监听
                        $("#upImgBar").attr("aria-valuenow", obj);
                    } else if(hType == 100) {
                        //完成，更新回对应的图片
                        $("#upImgDialog").hide();
                        swal("上传成功");
                    } else {
                        //有错误
                    }
                };

                $("#saveBtn").on('click', function () {
                    set_upload_param(uploader, "abcdef");
                });
            });

        </script>