package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.LogicConfig;
import com.xlibao.saas.market.manager.service.advermanager.AdverManagerService;
import com.xlibao.saas.market.manager.service.marketmanager.MarketManagerService;
import com.xlibao.saas.market.manager.utils.QiniuFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by admin on 2017/8/25.
 */
@Controller
@RequestMapping(value = "marketmanager/advert")
public class AdvertManagerController extends BaseController {

    @Autowired
    private AdverManagerService adverManagerService;

    @Autowired
    private QiniuFileUtil qiniuFileUtil;

    @Autowired
    private MarketManagerService marketManagerService;
    /**
     *获取视频列表
     * @param map
     * @return
     */
    @RequestMapping("/adverts")
    public String getAdvertList(ModelMap map){
        JSONObject adverJson =  adverManagerService.searchAdvertTemplatesPage();
        JSONObject response = adverJson.getJSONObject("response");
        JSONArray adverts = response.getJSONArray("data");
        map.put("count", response.getIntValue("count"));

        int pageIndex = getIntParameter("pageIndex", 1);
        map.put("title", getUTF("title",""));
        map.put("timeType", getIntParameter("timeType",-1));
        map.put("isUsed", getIntParameter("isUsed",-1));
        map.put("pageIndex", pageIndex);

        map.put("pageSize", getPageSize());
        map.put("advertList", adverts);
        return jumpPage(map, LogicConfig.FTL_ADVERT_EDIT, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_LIST);
    }

    /**
     * 添加视频附带视频文件去”七牛“
     * @param file
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAdvert",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public void addAdvert(@RequestParam("file") MultipartFile file,ModelMap map,HttpServletRequest request,HttpServletResponse response) throws IOException {
       JSONObject jsonObject = qiniuFileUtil.uploadToQiniu(file);
       String result="no";
        if("1".equals(jsonObject.getString("status"))){
            String path = jsonObject.getString("path");
            String videoName = jsonObject.getString("videoName");
            JSONObject resJson = adverManagerService.addAdvert(path,videoName, (MultipartHttpServletRequest) request);
            if(resJson.getIntValue("code") == 0){
                result="yes";
            }
        }else{
            result="no";
        }
        response.getWriter().print(result);
    }

    /**
     * 查看视频详情
     * @param map
     * @return
     */
    @RequestMapping("/goAdvert")
    public String getAdvertDetail(ModelMap map){
        JSONObject json =  adverManagerService.getAdvertByID();
        JSONObject response = json.getJSONObject("response");
        JSONArray adverts = response.getJSONArray("data");
        if(adverts.size()>0) {
            map.put("advert", adverts.get(0));
        }else {
            map.put("advert", "");
        }
        JSONObject screenJson =  adverManagerService.getScreensByAdvertID();
        JSONObject screenponse = screenJson.getJSONObject("response");
        JSONArray screens = screenponse.getJSONArray("data");

        map.put("screens", screens);


        return jumpPage(map, LogicConfig.FTL_ADVERT_MANAGET_DETAIL, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_LIST);
    }

    /**
     * 更新视频文件
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateAdvert")
    public JSONObject updateAdvert(ModelMap map){
        return adverManagerService.updateAdvert();
    }

    /**
     * 删除视频
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delAdvert")
    public JSONObject delAdvert(ModelMap map){
        return adverManagerService.delAdvertByID();
    }
    /**
     *获取屏幕列表
     * @param map
     * @return
     */
    @RequestMapping("/screens")
    public String getScreenList(ModelMap map){
        JSONObject adverJson =  adverManagerService.searchScreenTemplatePage();
        JSONObject response = adverJson.getJSONObject("response");
        JSONArray screens = response.getJSONArray("data");
        map.put("count", response.getIntValue("count"));

        JSONObject marketResponse = marketManagerService.getAllMarkets();
        if (marketResponse.getIntValue("code") == 0) {
            map.put("markets", marketResponse.getJSONObject("response").getJSONArray("datas"));
        }

        int pageIndex = getIntParameter("pageIndex", 1);
        map.put("code", getUTF("code",""));
        map.put("marketID", getIntParameter("marketID",-1));
        map.put("size", getUTF("size",""));
        map.put("pageIndex", pageIndex);
        map.put("screens", screens);
        return jumpPage(map, LogicConfig.FTL_ADVERT_MANAGET_SCREEN, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_LIST);
    }

    /**
     * 添加屏幕
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/addScreen")
    public void addScreen(ModelMap map,HttpServletResponse response) throws IOException{
        String result="no";

        JSONObject resJson =  adverManagerService.addScreen();
        if(resJson.getIntValue("code") == 0){
            result="yes";
        }
       response.getWriter().print(result);
    }


    /**
     * 删除屏幕
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delScreen", method = RequestMethod.POST)
    public JSONObject delScreen(ModelMap map){
        return adverManagerService.delScreenByID();
    }

    /**
     * 根据MAC获取屏幕信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/getScreenBy")
    public String getScreenBy(ModelMap map){
        String screenID ="-1";
        JSONObject screenJson =  adverManagerService.getScreenByMac();

        if (screenJson.getIntValue("code") == 0) {
            JSONObject jsonObject = screenJson.getJSONObject("response");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray.size()>0){
                JSONObject screen = jsonArray.getJSONObject(0);
                screenID = screen.getString("screenID");
                map.put("screen",screen);
            }else {
                map.put("screen","");
            }
        }

        JSONObject adverJson =  adverManagerService.getAdvertsByParameter();
        if (adverJson.getIntValue("code") == 0) {
            JSONObject jsonObject = adverJson.getJSONObject("response");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            map.put("adverts",jsonArray);
        }
        //

        return jumpPage(map, LogicConfig.FTL_ADVERT_MANAGET_SCREENCONFIG, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_LIST);
    }


    /**
     *获取投放列表
     * @param map
     * @return
     */
    @RequestMapping("/advertScreens")
    public String getadvertScreenList(ModelMap map){
        JSONObject adverJson =  adverManagerService.searchScreenAdvertTemplatePage();
        JSONObject response = adverJson.getJSONObject("response");
        JSONArray advertScreens = response.getJSONArray("data");
        map.put("count", response.getIntValue("count"));

        JSONObject marketResponse = marketManagerService.getAllMarkets();
        if (marketResponse.getIntValue("code") == 0) {
            map.put("markets", marketResponse.getJSONObject("response").getJSONArray("datas"));
        }

        JSONObject screenResponse = adverManagerService.getScreenListBy();
        if (screenResponse.getIntValue("code") == 0) {
            map.put("screens", screenResponse.getJSONObject("response").getJSONArray("data"));
        }

        JSONObject groupJson = adverManagerService.getAllGroup();
        JSONObject groupJsonsResponse = groupJson.getJSONObject("response");
        JSONArray groups = groupJsonsResponse.getJSONArray("datas");
        map.put("groups", groups);

         int pageIndex = getIntParameter("pageIndex", 1);
         map.put("marketID", getIntParameter("marketID",-1));
         map.put("code", getUTF("code",""));
         map.put("title", getUTF("title",""));
         map.put("beginTime", getUTF("beginTime",""));
         map.put("endTime", getUTF("endTime",""));
         map.put("isDown", getIntParameter("isDown",-1));
         map.put("playStatus", getIntParameter("playStatus",0));
         map.put("pageIndex", pageIndex);
         map.put("advertScreens", advertScreens);
        return jumpPage(map, LogicConfig.FTL_ADVERT_MANAGER, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_MANAGER);
    }
    /**
     * 添加投放视频
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/addScreenAdvert")
    public JSONObject addScreenAdvert(ModelMap map){
        return adverManagerService.addScreenAdvert();
    }
    /**
     * 更新投放视频
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateScreenAdvert")
    public JSONObject updateScreenAdvert(ModelMap map){
        return adverManagerService.updateScreenAdvert();
    }

    /**
     * 删除投放视频
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delScreenAdvert", method = RequestMethod.POST)
    public JSONObject delScreenAdvert(ModelMap map){
        return adverManagerService.delAdvertScreenByID();
    }

    /**
     * 获取店铺json
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllMarkets")
    public JSONObject getAllMarkets(ModelMap map) {
        return  marketManagerService.getAllMarkets();
    }
    /**
     * 查看播放详情
     * @param map
     * @return
     */
    @RequestMapping("/goAdvertScreen")
    public String goAdvertScreen(ModelMap map) {

        /*************广告：参数groupID=advertID*************/
        JSONObject advertJson = adverManagerService.getAdvertsByParameter();
        JSONObject advertJsonsResponse = advertJson.getJSONObject("response");
        JSONArray adverts = advertJsonsResponse.getJSONArray("datas");
        map.put("adverts", adverts);

        JSONObject jsonAS =  adverManagerService.getAdvertScreenByID();
        JSONObject responseAS = jsonAS.getJSONObject("response");
        JSONArray advertScreen = responseAS.getJSONArray("data");
        if(advertScreen.size()>0) {
            map.put("advertScreen", advertScreen.get(0));
        }else {
            map.put("advertScreen", "");
        }
        return jumpPage(map, LogicConfig.FTL_ADVERT_MANAGET_PLAYDETAIL, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_LIST);
    }
    /**
     * 根据ID获取播放信息
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAdvertScreen")
    public JSONObject getAdvertScreenByID(ModelMap map) {
        JSONObject jsonAS =  adverManagerService.getAdvertScreenByID();
        JSONObject responseAS = jsonAS.getJSONObject("response");
        JSONArray arrayAS = responseAS.getJSONArray("data");
        JSONObject response = new JSONObject();
        if(arrayAS.size()>0) {
            response = arrayAS.getJSONObject(0);
        }
        return response;
    }

    /**
     * 跳转至添加播放页
     * @param map
     * @return
     */
    @RequestMapping("/goAddAdvertScreen")
    public String goAddAdvertScreen(ModelMap map) {
        JSONObject marketResponse = marketManagerService.getAllMarkets();
        if (marketResponse.getIntValue("code") == 0) {
            map.put("markets", marketResponse.getJSONObject("response").getJSONArray("datas"));
        }
        JSONObject screenResponse = adverManagerService.getScreenListBy();
        if (screenResponse.getIntValue("code") == 0) {
            map.put("screens", screenResponse.getJSONObject("response").getJSONArray("data"));
        }
        JSONObject advertResponse = adverManagerService.getAdvertByID();
        if (advertResponse.getIntValue("code") == 0) {
            map.put("adverts", advertResponse.getJSONObject("response").getJSONArray("data"));
        }

        JSONObject groupJson = adverManagerService.getAllGroup();
        JSONObject groupJsonsResponse = groupJson.getJSONObject("response");
        JSONArray groups = groupJsonsResponse.getJSONArray("datas");
        map.put("groups", groups);

        return jumpPage(map, LogicConfig.FTL_ADVERT_MANAGET_ADDPLAY, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_LIST);
    }

    /**
     * 用户店铺与屏幕级联
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/getScreenListBy")
    public JSONObject getScreenListBy(ModelMap map) {
       return adverManagerService.getScreenListBy();
    }

    /**
     * 更新下载状态
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateIsdown")
    public JSONObject updateAdvertScreenIsdown(ModelMap map) {
        return adverManagerService.updateAdvertScreenIsdown();
    }

    /******************************/

    /**
     * 广告组列表
     * @param map
     * @return
     */
    @RequestMapping("/groupPage")
    public String searchGroupPage(ModelMap map) {
        JSONObject groupJson =  adverManagerService.searchGroupPage();
        JSONObject response = groupJson.getJSONObject("response");
        JSONArray groups = response.getJSONArray("data");
        map.put("count", response.getIntValue("count"));

        map.put("groupName", getUTF("groupName",""));
        int pageIndex = getIntParameter("pageIndex", 1);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", getPageSize());
        map.put("groups", groups);
        return jumpPage(map, LogicConfig.FTL_ADVERT_GROUPS, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_GROUPS);
    }

    /**
     * 新增广告组
     * @return
     */
    @ResponseBody
    @RequestMapping("saveGroup")
    public JSONObject saveGroup(){
        return adverManagerService.saveGroup();
    }

    /**
     * 广告组详情
     * @return
     */
    @RequestMapping("groupDetail")
    public String getGroup(ModelMap map){
        JSONObject jsonObject= adverManagerService.getGroup();
        JSONObject group = jsonObject.getJSONObject("response");
        map.put("group", group);

        /*************广告*************/
        JSONObject advertJson = adverManagerService.getAdvertListByGroupID();
        JSONObject advertJsonsResponse = advertJson.getJSONObject("response");
        JSONArray adverts = advertJsonsResponse.getJSONArray("datas");
        map.put("adverts", adverts);

        return jumpPage(map, LogicConfig.FTL_ADVERT_GROUPS_DETAIL, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_GROUPS);
    }
    /**
     * 删除广告组
     * @return
     */
    @ResponseBody
    @RequestMapping("delGroup")
    public JSONObject delGroup(){
        return adverManagerService.delGroup();
    }

    /**
     * 设置广告页
     * @param map
     * @return
     */
    @RequestMapping("/goAddgroup")
    public String goAddgroup(ModelMap map) {
        JSONObject jsonObject= adverManagerService.getGroup();
        JSONObject group = jsonObject.getJSONObject("response");
        map.put("group", group);

        JSONObject advertResponse = adverManagerService.getAdvertByID();
        if (advertResponse.getIntValue("code") == 0) {
            map.put("adverts", advertResponse.getJSONObject("response").getJSONArray("data"));
        }

        return jumpPage(map, LogicConfig.FTL_ADVERT_GROUPS_DEPLOY, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_GROUPS);
    }

    /**
     * 新增广告组
     * @return
     */
    @ResponseBody
    @RequestMapping("saveAdverGroup")
    public JSONObject saveAdverGroup(){
        return adverManagerService.saveAdverGroup();
    }

    @RequestMapping("/goEditgroup")
    public String goEditgroup(ModelMap map) {
        JSONObject jsonObject= adverManagerService.getGroup();
        JSONObject group = jsonObject.getJSONObject("response");
        map.put("group", group);

        /********所有广告*********/
        JSONObject advertResponse = adverManagerService.getAdvertByID();
        if (advertResponse.getIntValue("code") == 0) {
            map.put("advertList", advertResponse.getJSONObject("response").getJSONArray("data"));
        }

        /*************组广告*************/
        JSONObject advertJson = adverManagerService.getAdvertListByGroupID();
        JSONObject advertJsonsResponse = advertJson.getJSONObject("response");
        JSONArray adverts = advertJsonsResponse.getJSONArray("datas");
        map.put("adverts", adverts);
        return jumpPage(map, LogicConfig.FTL_ADVERT_GROUPS_EDIT, LogicConfig.TAB_ADVERT, LogicConfig.TAB_ADVERT_GROUPS);
    }
    /**
     * 更新广告组
     * @return
     */
    @ResponseBody
    @RequestMapping("updateAdverGroup")
    public JSONObject updateAdverGroup(){
        return adverManagerService.updateAdverGroup();
    }

}
