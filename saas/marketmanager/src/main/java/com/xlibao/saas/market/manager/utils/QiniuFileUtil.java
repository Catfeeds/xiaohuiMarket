package com.xlibao.saas.market.manager.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by user on 2017/9/1.
 */
@Component
public class QiniuFileUtil {
    private final String STATUS_ERROR = "0";
    private final String STATUS_SUCCESS = "1";
    String  accessKey = "-H7UsuTzqZpl5hShnGsKrLN7PeK3X1AsFWmnf7r-";
    String  secretKey = "-_mtaspBa3qLlVKxXhbjIjvIQ-mNl3OvfbhLEDLb";
    String  bucket = "marketadver";
    String path = "http://ov2oajh50.bkt.clouddn.com/";



    public JSONObject uploadToQiniu(MultipartFile item) {

        JSONObject json = new JSONObject();

        String key = null;
        if (item.getSize() > 0) {
            String filename = item.getOriginalFilename();
            json.put("name", filename);
            json.put("size", item.getSize());
            if (filename.endsWith("mp4") || filename.endsWith("wmv") || filename.endsWith("mkv") || filename.endsWith("rmvb")||filename.endsWith("avi")||filename.endsWith("flv")||filename.endsWith("mpg")||filename.endsWith("swf")||filename.endsWith("qlv")) {
                try {
                    byte[] uploadBytes = item.getBytes();

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(uploadBytes);

                    Configuration cfg = new Configuration(Zone.zone2());

                    UploadManager uploadManager = new UploadManager(cfg);
                    Auth auth = Auth.create(accessKey, secretKey);
                    String upToken = auth.uploadToken(bucket);

                    Response response = uploadManager.put(byteArrayInputStream, key, upToken, null, null);
                    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                    System.out.println("path:"+path);
                    json.put("status", STATUS_SUCCESS);
                    json.put("path",path);
                    json.put("videoName",putRet.key);
                    System.out.println("putRet.key:"+putRet.key);
                } catch (QiniuException e) {
                    Response r = e.response;
                    json.put("status", STATUS_ERROR);
                    json.put("error", r.toString());
                    json.put("path","");
                    json.put("videoName","");
                } catch (IOException e) {
                    json.put("status", STATUS_ERROR);
                    json.put("error", "IO Exception,请重试");
                    json.put("path","");
                    json.put("videoName","");
                }
            } else {
                json.put("status", STATUS_ERROR);
                json.put("error", "上传文件格式错误");
            }
        } else {
            json.put("status", STATUS_ERROR);

        }

        return json;
    }
}
