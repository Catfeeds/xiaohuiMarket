
accessid = '';
accesskey = '';
host = '';
policyBase64 = '';
signature = '';
callbackbody = '';
filename = '';
key = '';
sffix = '';
expire = 0;
g_object_name = '';
g_object_name_type = '';
now = timestamp = Date.parse(new Date()) / 1000;

//可以更改的内容
var upImgName = "upimg";//图片预览控件
var serverUrl = './php/get.php';//拿key上传路径
var upCallback = null;//回调方法
//是否有上传图片，默认没有，如果有选择过图片，则为true
var upImgBool = false;
//原来是否有图片
var srcImgBool = false;
//是否执行过清除图片
var clearImgBool = false;

// function send_request()
// {
//     var xmlhttp = null;
//     if (window.XMLHttpRequest)
//     {
//         xmlhttp=new XMLHttpRequest();
//     }
//     else if (window.ActiveXObject)
//     {
//         xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
//     }
//
//     if (xmlhttp!=null)
//     {
//         xmlhttp.open( "GET", serverUrl, false );
//         xmlhttp.send( null );
//         return xmlhttp.responseText
//     }
//     else
//     {
//         alert("Your browser does not support XMLHTTP.");
//     }
// };

// function check_object_radio() {
//     var tt = document.getElementsByName('myradio');
//     for (var i = 0; i < tt.length ; i++ )
//     {
//         if(tt[i].checked)
//         {
//             g_object_name_type = tt[i].value;
//             break;
//         }
//     }
// }

// function get_signature()
// {
//     //可以判断当前expire是否超过了当前时间,如果超过了当前时间,就重新取一下.3s 做为缓冲
//     now = timestamp = Date.parse(new Date()) / 1000;
//     if (expire < now + 3)
//     {
//         body = send_request()
//         var obj = eval ("(" + body + ")");
//         host = obj['host'];
//         policyBase64 = obj['policy'];
//         accessid = obj['accessid'];
//         signature = obj['signature'];
//         expire = parseInt(obj['expire']);
//         callbackbody = obj['callback'];
//         key = obj['dir'];
//         return true;
//     }
//     return false;
// };

function handler_signature(body) {
    //var obj = eval ("(" + body + ")");
    host = body.host;//obj['host'];
    policyBase64 = body.policy;//obj['policy'];
    accessid = body.accessid;//obj['accessid'];
    signature = body.signature;//obj['signature'];
    expire = parseInt(body.expire);//parseInt(obj['expire']);
    callbackbody = body.callback;//obj['callback'];
    key = body.dir;//obj['dir'];
}

// function random_string(len) {
//     len = len || 32;
//     var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
//     var maxPos = chars.length;
//     var pwd = '';
//     for (i = 0; i < len; i++) {
//         pwd += chars.charAt(Math.floor(Math.random() * maxPos));
//     }
//     return pwd;
// }
//
// function get_suffix(filename) {
//     pos = filename.lastIndexOf('.')
//     suffix = '';
//     if (pos != -1) {
//         suffix = filename.substring(pos)
//     }
//     return suffix;
// }

// function calculate_object_name(filename)
// {
//     if (g_object_name_type == 'local_name')
//     {
//         g_object_name += "${filename}"
//     }
//     else if (g_object_name_type == 'random_name')
//     {
//         suffix = get_suffix(filename)
//         g_object_name = key + random_string(10) + suffix
//     }
//     return ''
// }
//
// function get_uploaded_object_name(filename)
// {
//     if (g_object_name_type == 'local_name')
//     {
//         tmp_name = g_object_name
//         tmp_name = tmp_name.replace("${filename}", filename);
//         return tmp_name
//     }
//     else if(g_object_name_type == 'random_name')
//     {
//         return g_object_name
//     }
// }

function handler_start(up, filename) {
    g_object_name = key + filename;

    new_multipart_params = {
        'key' : g_object_name,
        'policy': policyBase64,
        'OSSAccessKeyId': accessid,
        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
        'callback' : callbackbody,
        'signature': signature,
    };

    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });

    if(upCallback != null){
        upCallback(1);
    }

    up.start();
}

function set_upload_param(up, filename)
{
    //3 S内
    now = timestamp = Date.parse(new Date()) / 1000;
    if (expire < now + 3) {
        //重新获取
        tokenPost(serverUrl, function (data) {
            handler_signature(data);
            handler_start(up, filename);
        });
    } else {
        handler_start(up, filename);
    }

}

//清除图片
function clear_img() {
    //是传过
    upImgBool = false;
    //删除过
    if(srcImgBool) {
        clearImgBool = true;
    }
    //清空图片
    $("#" + upImgName).attr("src", "");
}

function clacImgZoomParam(maxWidth, maxHeight, width, height) {

    var param = {top: 0, left: 0, width: width, height: height};
    if (width > maxWidth || height > maxHeight) {
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;

        if (rateWidth > rateHeight) {
            param.width = maxWidth;
            param.height = Math.round(height / rateWidth);
        } else {
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }

    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}

var uploader = new plupload.Uploader({
    browse_button : 'selectFileBtn',
    url: 'http://oss.aliyuncs.com',
    multi_selection: false,//设置只能单选文件

    filters: {
        mime_types : [ //只允许上传图片和zip,rar文件
            { title : "Image files", extensions : "jpg,png" },
        ],
        max_file_size : '1mb', //最大只能上传1mb的文件
        prevent_duplicates : false //不允许选取重复文件
    },

    init: {
        // PostInit: function() {
            // document.getElementById('ossfile').innerHTML = '';
            // document.getElementById('postfiles').onclick = function() {
            //     set_upload_param(uploader, '', false);
            //     return false;
            // };
        // },

        FilesAdded: function(up, files) {
            //保证只上传1张图片，这个方法的意思是删除旧的图片
            $.each(up.files, function (i, file) {
                if (up.files.length <= 1) {
                    return;
                }
                up.removeFile(file);
            });
            var file = files[0];

            //上传过图片
            upImgBool = true;

            var preloader = new mOxie.Image();
            preloader.onload = function () {
                var imgsrc = preloader.type == 'image/jpeg' ? preloader.getAsDataURL('image/jpeg', 80) : preloader.getAsDataURL(); //得到图片src,实质为一个base64编码的数据
                file.imgsrc = imgsrc;
                var targetImg = $("#" + upImgName);
                targetImg.attr("src", imgsrc);
                var rect = clacImgZoomParam(150, 150, preloader.width, preloader.height);
                targetImg.css({
                    "width": rect.width + "px",
                    "height": rect.height + "px",
                    "marginTop": rect.top + "px",
                    "marginLeft": rect.left + "px",
                    "border": 0,
                });
                preloader.destroy();
                preloader = null;
            };
            preloader.load(file.getSource());
        },

        BeforeUpload: function(up, file) {
            //check_object_radio();
            //set_upload_param(up, file.name, true);
        },

        UploadProgress: function(up, file) {
            //var d = document.getElementById(file.id);
            //d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            //var prog = d.getElementsByTagName('div')[0];
            //var progBar = prog.getElementsByTagName('div')[0]
            //progBar.style.width= 2*file.percent+'px';
            //progBar.setAttribute('aria-valuenow', file.percent);
            if(upCallback!=null){
                upCallback(2, file.percent);
            }
        },

        FileUploaded: function(up, file, info) {
            if (info.status == 200)
            {
                if(upCallback != null){
                    upCallback(100, host + "/" + g_object_name);
                }
                //document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'upload to oss success, object name:' + get_uploaded_object_name(file.name);
            }
            else
            {
                //document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
            }
        },

        Error: function(up, err) {
            if (err.code == -600) {
                //document.getElementById('console').appendChild(document.createTextNode("\n选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小"));
            }
            else if (err.code == -601) {
                //document.getElementById('console').appendChild(document.createTextNode("\n选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型"));
            }
            else if (err.code == -602) {
                //document.getElementById('console').appendChild(document.createTextNode("\n这个文件已经上传过一遍了"));
            }
            else
            {
                //document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
            }
        }
    }
});

uploader.init();
