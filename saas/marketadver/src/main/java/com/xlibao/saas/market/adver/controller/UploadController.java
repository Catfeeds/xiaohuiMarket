package com.xlibao.saas.market.adver.controller;


import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.saas.market.adver.config.ConfigFactory;
import com.xlibao.saas.market.adver.service.uploadService.uploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by admin on 2017/8/22.
 */
@Controller
public class UploadController extends BasicWebService {

    @Autowired
    private uploadService uploadService;

    @RequestMapping("/upload")
    public String index(){
        /*getHttpServletResponse().setHeader("content-type", "text/html");*/
        return "upload";
    }

    @RequestMapping(value = "/uploadfile",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String fileUpload(@RequestParam("files[]") MultipartFile[] files){

        JSONObject object = uploadService.uploadToQiniu(files);

        return object.toString();
    }

}
