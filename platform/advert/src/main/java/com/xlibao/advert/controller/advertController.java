package com.xlibao.advert.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.advert.service.item.advertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/8/29.
 */
@Controller
@RequestMapping("/advert")
public class advertController {

    @Autowired
    private advertService advertService;

    @ResponseBody
    @RequestMapping("getAdvertTemplateList")
    public JSONObject getAdvertTemplateList(){
        return advertService.getAdvertTemplateList();
    }

    @ResponseBody
    @RequestMapping("getAllAdvertInfo")
    public JSONObject getAllAdvertInfo(){return advertService.getAllAdvertInfo();}

    @ResponseBody
    @RequestMapping("updateIsDown")
    public JSONObject updateIsDown(){return advertService.updateIsDown();}

    @ResponseBody
    @RequestMapping("uploadAdvertInfo")
    public JSONObject uploadAdvertInfo(){return advertService.uploadAdvertInfo();}

    @ResponseBody
    @RequestMapping("getAdvertFromID")
    public JSONObject getAdvertFromID(){return advertService.getAdvertFromID();}

    @ResponseBody
    @RequestMapping("updateAdvertInfo")
    public JSONObject updateAdvertInfo(){return advertService.updateAdvertInfo();}

    @ResponseBody
    @RequestMapping("deleteAdvertFromID")
    public JSONObject deleteAdvertFromID(){return advertService.deleteAdvertFromID();}

    @ResponseBody
    @RequestMapping("getScreenInfoFromMAC")
    public JSONObject getScreenInfoFromMAC(){return advertService.getScreenInfoFromMAC();}

    @ResponseBody
    @RequestMapping("getScreenTemplateList")
    public JSONObject getScreenTemplateList(){return advertService.getScreenTemplateList();}

    @ResponseBody
    @RequestMapping("addScreenInfo")
    public JSONObject addScreenInfo(){return advertService.addScreenInfo();}

    @ResponseBody
    @RequestMapping("deleteScreenFromID")
    public JSONObject deleteScreenFromID(){return advertService.deleteScreenFromID();}

    @ResponseBody
    @RequestMapping("getAdvertTemplates")
    public JSONObject getAdvertTemplates(){return advertService.getAdvertTemplates();}

    @ResponseBody
    @RequestMapping("addAdvertInfoForScreen")
    public JSONObject addAdvertInfoForScreen(){return advertService.addAdvertInfoForScreen();}

    @ResponseBody
    @RequestMapping("getAdvertInfoFromID")
    public JSONObject getAdvertInfoFromID(){return advertService.getAdvertInfoFromID();}

    @ResponseBody
    @RequestMapping("updateAdvertInfoFromID")
    public JSONObject updateAdvertInfoFromID(){return advertService.updateAdvertInfoFromID();}

    @ResponseBody
    @RequestMapping("deleteAdvertInfoFromID")
    public JSONObject deleteAdvertInfoFromID(){return advertService.deleteAdvertInfoFromID();};

    @ResponseBody
    @RequestMapping("getAllScreenInfo")
    public JSONObject getAllScreenInfo(){return advertService.getAllScreenInfo();}

    @ResponseBody
    @RequestMapping("getAdvertInfoFromScreenID")
    public JSONObject getAdvertInfoFromScreenID(){return advertService.getAdvertInfoFromScreenID();}

    @ResponseBody
    @RequestMapping("getScreenInfoFromAdvertID")
    public JSONObject getScreenInfoFromAdvertID(){return advertService.getScreenInfoFromAdvertID();}

    @ResponseBody
    @RequestMapping("searchGroupPage")
    public JSONObject searchGroupPage(){
        return advertService.searchGroupPage();
    }

    @ResponseBody
    @RequestMapping("saveGroup")
    public JSONObject saveGroup(){
        return advertService.saveGroup();
    }

    @ResponseBody
    @RequestMapping("getGroup")
    public JSONObject getGroup(){
        return advertService.getGroup();
    }

    @ResponseBody
    @RequestMapping("getAdvertListByGroupID")
    public JSONObject getAdvertListByGroupID(){
        return advertService.getAdvertListByGroupID();
    }

    @ResponseBody
    @RequestMapping("getAdvertsByParameter")
    public JSONObject getAdvertsByParameter(){
        return advertService.getAdvertsByParameter();
    }

    @ResponseBody
    @RequestMapping("delGroup")
    public JSONObject delGroup(){
        return advertService.delGroup();
    }

    @ResponseBody
    @RequestMapping("delAdvertGroup")
    public JSONObject delAdvertGroup(){
        return advertService.delAdvertGroup();
    }

    @ResponseBody
    @RequestMapping("saveAdverGroup")
    public JSONObject saveAdverGroup(){
        return advertService.saveAdverGroup();
    }

    @ResponseBody
    @RequestMapping("updateAdverGroup")
    public JSONObject updateAdverGroup(){
        return advertService.updateAdverGroup();
    }

    @ResponseBody
    @RequestMapping("saveDownload")
    public JSONObject saveDownload(){
        return advertService.saveDownload();
    }

    @ResponseBody
    @RequestMapping("getAllGroup")
    public JSONObject getAllGroup(){
        return advertService.getAllGroup();
    }


}
