package com.xlibao.saas.market.adver.service.uploadService.impl;

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
import com.xlibao.saas.market.adver.config.ConfigFactory;
import com.xlibao.saas.market.adver.service.uploadService.uploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by admin on 2017/8/24.
 */
@Service
public class uploadServiceImpl implements uploadService {

    private final String STATUS_ERROR = "0";
    private final String STATUS_SUCCESS = "1";

    @Override
    public JSONObject uploadToQiniu(MultipartFile[] files) {

        JSONObject json = new JSONObject();

        String key = null;

        for (MultipartFile item : files)
        {
            if(item.getSize()>0){
                String filename = item.getOriginalFilename();
                json.put("name",filename);
                json.put("size",item.getSize());
                if(filename.endsWith("mp4")||filename.endsWith("wmv")||filename.endsWith("jpg")||filename.endsWith("png")){
                    try {
                        byte[] uploadBytes = item.getBytes();

                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(uploadBytes);

                        Configuration cfg = new Configuration(Zone.zone2());

                        UploadManager uploadManager = new UploadManager(cfg);
                        Auth auth = Auth.create(ConfigFactory.getQiNiuConfig().getAccessKey(),ConfigFactory.getQiNiuConfig().getSecretKey());
                        String upToken = auth.uploadToken(ConfigFactory.getQiNiuConfig().getBucket());

                        Response response =  uploadManager.put(byteArrayInputStream,key,upToken,null,null);
                        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
                        System.out.println(putRet.key);
                        System.out.println(putRet.hash);
                        json.put("status",STATUS_SUCCESS);
                    } catch (QiniuException e) {
                        Response r = e.response;
                        json.put("status",STATUS_ERROR);
                        json.put("error",r.toString());
                    } catch (IOException e) {
                        json.put("status",STATUS_ERROR);
                        json.put("error","IO Exception,请重试");
                    }
                }else{
                    json.put("status",STATUS_ERROR);
                    json.put("error","上传文件格式错误");
                }
            }else{
                json.put("status",STATUS_ERROR);
                json.put("error","上传文件大小小于零");
            }
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(0,json);
        JSONObject results = new JSONObject();
        results.put("files",jsonArray);
        return results;

    }
}
