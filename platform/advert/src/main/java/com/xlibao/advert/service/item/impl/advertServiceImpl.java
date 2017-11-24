package com.xlibao.advert.service.item.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.advert.data.mapper.AdvertDataAccessManager;
import com.xlibao.advert.data.model.*;
import com.xlibao.advert.service.item.advertService;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalConstantConfig;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/8/28.
 */
@Transactional
@Service("advertService")
public class advertServiceImpl extends BasicWebService implements advertService {

    @Autowired
    private AdvertDataAccessManager advertDataAccessManager;

    @Override
    public JSONObject getAdvertTemplateList() {
        String title = getUTF("title",null);
        int timeType = getIntParameter("timeType",-1);
        int isUsed = getIntParameter("isUsed",-1);
        int pageSize = getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);


        List<AdvertInfo> infos = advertDataAccessManager.getAdvertTemplateList(title,timeType,isUsed,pageSize,pageStartIndex);
        int count = advertDataAccessManager.getAdvertCount();

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));
        response.put("count", count);

        return success(response);

    }

    public JSONObject getAllAdvertInfo(){
        String mac = getUTF("mac",null);
        List<AdvertScreenInfo> infos = advertDataAccessManager.getAllAdvertInfo(mac);
        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));

        return success(response);
    }

    public JSONObject getAdvertCount(){

        int count = advertDataAccessManager.getAdvertCount();
        System.out.println(count);
        JSONObject response = new JSONObject();
        response.put("data", count);

        return success(response);
    }

    @Override
    public JSONObject updateIsDown() {
        int isDown = getIntParameter("isDown",0);
        int advertID = getIntParameter("advertID",0);
        int screenID = getIntParameter("screenID",0);
        int marketID = getIntParameter("marketID",0);

        if(advertID == 0 ||screenID == 0){
            return fail("参数不允许为空");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:m:ss");
        advertDataAccessManager.updateIsDown(isDown,advertID,screenID,marketID,df.format(new Date()));

        return success("更新状态成功");
    }

    @Override
    public JSONObject uploadAdvertInfo() {
        String title = getUTF("title",null);
        String timeSize = getUTF("timeSize",null);
        String remark = getUTF("remark",null);
        String url = getUTF("url",null);
        String videoName = getUTF("videoName",null);
        int time = 0;
        int timeType = 0;
        int advertID = 0;

        if(title==null||url==null||videoName==null){
            return fail("传入参数不能为空");
        }
        try{
            time = Integer.parseInt(timeSize);
            if(time<0){
                return fail("广告时长不能小于零");
            }
            timeType = getTimeType(time);
        }catch (NumberFormatException e){
            return fail("广告时长参数错误");
        }

        int status = advertDataAccessManager.isExistAdvert();
        if(status==0){
            advertID = 1000;
        }else{
            advertID = advertDataAccessManager.getMaxAdvertID()+1;
        }

        AdvertInfo infos = new AdvertInfo();

        infos.setAdvertID(advertID);
        infos.setRemark(remark);
        infos.setTimeSize(time);
        infos.setTimeType(timeType);
        infos.setTitle(title);
        infos.setUrl(url);
        infos.setVideoName(videoName);

        advertDataAccessManager.uploadAdvertInfo(infos);


        return success("上传成功");
    }

    public JSONObject getAdvertFromID(){
        int advertID = getIntParameter("advertID",0);

        List<AdvertInfo> infos = advertDataAccessManager.getAdvertFromID(advertID);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));

        return success(response);
    }

    public JSONObject updateAdvertInfo(){
        String title = getUTF("title",null);
        String timeSize = getUTF("timeSize",null);
        String remark = getUTF("remark",null);
        int advertID = getIntParameter("advertID",0);

        int time = 0;

        if(title==null || timeSize==null || advertID == 0){
            return fail("传入参数不能为空");
        }

        try{
            time = Integer.parseInt(timeSize);
            if(time<0){
                return fail("广告时长不能小于零");
            }
        }catch (NumberFormatException e){
            return fail("广告时长参数错误");
        }

        advertDataAccessManager.updateAdvertInfo(title,time,remark,advertID);

        return success("更新成功");
    }

    public JSONObject deleteAdvertFromID(){
        int advertID = getIntParameter("advertID",0);

        if(advertID==0){
            return fail("传入参数不能为空");
        }

        advertDataAccessManager.deleteAdvertFromID(advertID);

        return success("操作成功");
    }

    public JSONObject getScreenInfoFromMAC(){
        String mac = getUTF("mac",null);
        if(mac==null){
            return fail("参数出错");
        }

        List<ScreenInfo> infos = advertDataAccessManager.getScreenInfoFromMAC(mac);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));

        return success(response);
    }

    public JSONObject getScreenTemplateList(){
        String code = getUTF("code",null);
        int marketId = getIntParameter("marketID",-1);
        String screenSize = getUTF("screenSize",null);
        int pageSize = getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<ScreenInfo> infos = advertDataAccessManager.getScreenTemplateList(code,marketId,screenSize,pageSize,pageStartIndex);

        int count = advertDataAccessManager.getScreenCount();

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));
        response.put("count",count);

        return success(response);
    }


    public JSONObject addScreenInfo(){
        int marketId = getIntParameter("marketId",0);
        //int ScreenID = advertDataAccessManager.getMaxScreenID() + 1;
        int screenID = getScreenID(marketId);
        String marketName = getUTF("marketName",null);
        String requireTime = getUTF("requireTime",null);
        String screenSize = getUTF("screenSize",null);
        String code = getCode(marketId);
        String mac = getUTF("mac",null);
        int advertCount = getIntParameter("advertCount",5);
        int used = getIntParameter("used",1);
        String screenRemark = getUTF("screenRemark",null);

        if(marketName==null || screenSize ==null || code ==null || mac ==null){
            return fail("参数错误");
        }

        ScreenInfo screenInfo = new ScreenInfo();
        screenInfo.setScreenID(screenID);
        screenInfo.setMarketId(marketId);
        screenInfo.setMarketName(marketName);
        screenInfo.setRequireTime(requireTime);
        screenInfo.setScreenSize(screenSize);
        screenInfo.setCode(code);
        screenInfo.setMac(mac);
        screenInfo.setAdvertCount(advertCount);
        screenInfo.setUsed(used);
        screenInfo.setScreenRemark(screenRemark);

        advertDataAccessManager.addScreenInfo(screenInfo);

        return success("添加成功");
    }

    public JSONObject deleteScreenFromID(){
        int screenID = getIntParameter("screenID",0);
        int marketID = getIntParameter("marketID",0);
        if(screenID ==0||marketID==0){
            return fail("参数错误");
        }

        advertDataAccessManager.deleteScreenFromID(screenID,marketID);

        return success("删除成功");
    }

    public JSONObject getAdvertTemplates(){
        int marketId = getIntParameter("marketID",-1);
        String code = getUTF("code",null);
        String title = getUTF("title",null);
        String beginTime = getUTF("beginTime",null);
        String endTime = getUTF("endTime",null);
        int isDown = getIntParameter("isDown",-1);
        int playStatus = getIntParameter("playStatus",-1);
        int pageSize = getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);


        List<AdvertScreenInfo> infos = advertDataAccessManager.getAdvertTemplates(marketId,code,title,beginTime,endTime,isDown,playStatus,pageSize,pageStartIndex);

        int count = advertDataAccessManager.getAdvertInfoCount(marketId,code,title,beginTime,endTime,isDown,playStatus);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));
        response.put("count",count);

        return success(response);
    }


    public JSONObject addAdvertInfoForScreen(){
        int marketID = getIntParameter("marketID",-1);
        int screenID = getIntParameter("screenID",-1);
        int advertID = getIntParameter("advertID",-1);
        int style = getIntParameter("style",-1);
        String beginTime = getUTF("beginTime",null);
        int playOrder = getIntParameter("playOrder",-1);
        String remark = getUTF("remark",null);

        if(marketID==-1){
            return  fail("缺少参数：marketID");
        }else if(screenID==-1){
            return  fail("缺少参数：screenID");
        }else if(advertID==-1){
            return  fail("缺少参数：advertID");
        }else if(style==-1){
            return  fail("缺少参数：style");
        }

        advertDataAccessManager.addAdvertInfoForScreen(marketID,advertID,screenID,beginTime,style,playOrder,remark);

        return success("添加成功");
    }

    public JSONObject updateAdvertInfoFromID(){
        int screenID = getIntParameter("screenID",-1);
        int advertID = getIntParameter("advertID",-1);
        int marketId = getIntParameter("marketID",-1);
        int style = getIntParameter("style",-1);
        String code = getUTF("sCode",null);
        String beginTime = getUTF("beginTime",null);
        int playOrder = getIntParameter("playOrder",-1);
        String remark = getUTF("remark",null);
        if(marketId==-1){
            return  fail("缺少参数：marketID");
        }else if(screenID==-1){
            return  fail("缺少参数：screenID");
        }else if(advertID==-1){
            return  fail("缺少参数：advertID");
        }else if(style==-1){
            return  fail("缺少参数：style");
        }

        String[] rs = split(code);
        int cMarketID = Integer.parseInt(rs[0]);
        int cScreenID = Integer.parseInt(rs[1]);

        advertDataAccessManager.updateAdvertInfoFromID(advertID,screenID,marketId,cMarketID,cScreenID,beginTime,playOrder,remark,style);
        return success("更新成功");
    }

    public JSONObject deleteAdvertInfoFromID(){
        int screenID = getIntParameter("screenID");
        int advertID = getIntParameter("advertID");
        int marketID = getIntParameter("marketID");

        if(marketID==-1){
            return  fail("缺少参数：marketID");
        }else if(screenID==-1){
            return  fail("缺少参数：screenID");
        }else if(advertID==-1){
            return  fail("缺少参数：advertID");
        }

        advertDataAccessManager.deleteAdvertInfoFromID(advertID,screenID,marketID);

        return success("删除成功");
    }

    public JSONObject getScreenInfoFromAdvertID(){
        int advertID = getIntParameter("advertID");

        List<ScreenInfo> infos = advertDataAccessManager.getScreenInfoFromAdvertID(advertID);
        JSONObject response = new JSONObject();
        response.put("data",JSONObject.parseArray(JSONObject.toJSONString(infos)));
        return success(response);
    }


    public JSONObject getAllScreenInfo(){
        int marketId = getIntParameter("marketId",-1);

        List<ScreenInfo> infos = advertDataAccessManager.getAllScreenInfo(marketId);
        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(infos)));

        return success(response);
    }

    public JSONObject getAdvertInfoFromID(){
        int advertID = getIntParameter("advertID",-1);
        int screenID = getIntParameter("screenID",-1);
        int marketID = getIntParameter("marketID",-1);

        if(marketID==-1){
            return  fail("缺少参数：marketID");
        }else if(screenID==-1){
            return  fail("缺少参数：screenID");
        }else if(advertID==-1){
            return  fail("缺少参数：advertID");
        }
        List<AdvertScreenInfo> infos = advertDataAccessManager.getAdvertInfoFromID(advertID,screenID,marketID);
        JSONObject response = new JSONObject();
        response.put("data",JSONObject.parseArray(JSONObject.toJSONString(infos)));

        return success(response);
    }

    public JSONObject getAdvertInfoFromScreenID(){

        int screenID = getIntParameter("screenID",-1);
        if(screenID==-1){
            return  fail("缺少参数：screenID");
        }

        List<AdvertScreenInfo> infos = advertDataAccessManager.getAdvertInfoFromScreenID(screenID);
        JSONObject response = new JSONObject();
        response.put("data",JSONObject.parseArray(JSONObject.toJSONString(infos)));

        return success(response);
    }

    public int getTimeType(int timeSize){

        int timeType = 0;

        if(timeSize<=15){timeType=0;}else if (timeSize<=30 && timeSize>15){timeType=1;}
        else if(timeSize>30 && timeSize<=60){timeType=3;}
        else if(timeSize>60 && timeSize<=90){timeType=4;}
        else if(timeSize>90 && timeSize<=120){timeType=5;}
        else {timeType=6;}

        return timeType;
    }


    public String[] split(String code){
        System.out.println(code);
        String[] rs = code.split("-");
        return rs;
    }

    public String getCode(int marketID){
        int status = advertDataAccessManager.isMarketExist(marketID);
        String code = "";
        int screenID = 0;
        if(status==0){
            code = marketID+"-101";
        }else {
            screenID = advertDataAccessManager.getMaxScreenIDWithMarketID(marketID) +1;
            code = marketID+ "-"+screenID;
        }
        return code;
    }

    public int getScreenID(int marketID){
        int screenID = 0;
        int status = advertDataAccessManager.isMarketExist(marketID);
        if(status==0){
            screenID = 101;
        }else{
            screenID = advertDataAccessManager.getMaxScreenIDWithMarketID(marketID) + 1;
        }
        return screenID;
    }

    @Override
    public JSONObject searchGroupPage() {
        String groupName = getUTF("groupName", null);
        int pageSize = getPageSize();
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<GroupInfo> groupInfos = advertDataAccessManager.searchGroupPage(groupName,pageSize,pageStartIndex);
        int count = advertDataAccessManager.searchGroupPageCount(groupName);

        JSONObject response = new JSONObject();
        response.put("data", groupInfos);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1));
        return success(response);
    }

    @Override
    public JSONObject saveGroup(){
        String groupName = getUTF("groupName",null);
        String remark = getUTF("remark",null);
        if(groupName==null){
            return fail("缺少分组名称groupName");
        }
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupName(groupName);
        groupInfo.setRemark(remark);

        if(advertDataAccessManager.saveGroup(groupInfo)>0){
            return success("添加成功");
        }
        return fail("添加失败");
    }
    @Override
    public JSONObject getGroup(){
        Long groupID = getLongParameter("groupID",-1);
        if(groupID==-1){
            return fail("缺少分组IDgroupID");
        }

        GroupInfo groupInfo = advertDataAccessManager.getGroup(groupID);
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(groupInfo));
        return success(response);
    }
    @Override
    public JSONObject getAdvertListByGroupID(){
        Long groupID = getLongParameter("groupID",-1);
        if(groupID==-1){
            return fail("缺少分组ID:groupID");
        }
        List<AdvertInfo> advertList = advertDataAccessManager.getAdvertByGroupId(groupID);
        return success(advertList);
    }
    @Override
    public JSONObject getAdvertsByParameter(){
        Long groupID = getLongParameter("groupID",-1);
        Long screenID = getLongParameter("screenID",-1);
        Long marketID = getLongParameter("marketID",-1);
        if(screenID==-1){
            return fail("缺少屏幕ID:screenID");
        }else if(marketID==-1){
            return fail("缺少店鋪ID:marketID");
        }
        List<AdvertInfo> advertList = advertDataAccessManager.getAdvertsByParameter(groupID,screenID,marketID);
        return success(advertList);
    }

    @Override
    public JSONObject delGroup(){
        Long groupID = getLongParameter("groupID",-1);
        if(groupID==-1){
            return fail("缺少分组IDgroupID");
        }
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setIsDelete(1);
        groupInfo.setId(groupID);

        if(advertDataAccessManager.updateGroup(groupInfo)>0){
            try{
                AdvertGroup advertGroup =new AdvertGroup();
                advertGroup.setIsDelete(1);
                advertGroup.setGroupId(groupID);
                if(advertDataAccessManager.delAdvertGroup(advertGroup)>0){
                    return success("删除成功");
                }
            } catch (Exception ex) {
                throw new RuntimeException("删除失败");
            }
        }
        return fail("删除失败");
    }
    @Override
    public JSONObject delAdvertGroup(){
        Long groupID = getLongParameter("groupID",-1);
        if(groupID==-1){
            return fail("缺少分组IDgroupID");
        }
        AdvertGroup advertGroup =new AdvertGroup();
        advertGroup.setIsDelete(1);
        advertGroup.setGroupId(groupID);
        if(advertDataAccessManager.delAdvertGroup(advertGroup)>0){
            return success("删除成功");
        }
        return fail("删除失败");
    }
    @Override
    public JSONObject saveAdverGroup(){
        Long groupID = getLongParameter("groupID",-1);
        String advertIDs = getUTF("advertIDs",null);
        if(groupID==-1){
            return fail("缺少分组ID:groupID");
        }else if(advertIDs==null){
            return fail("缺少广告ID:advertIDs");
        }
        try{
            String [] advertIDList= advertIDs.split(CommonUtils.SPLIT_COMMA);
            for (int i = 0; i < advertIDList.length; i++) {
                AdvertGroup advertGroup = new AdvertGroup();
                advertGroup.setGroupId(groupID);
                advertGroup.setAdvertId(Long.parseLong(advertIDList[i]));
                advertDataAccessManager.saveAdvertGroup(advertGroup);
            }
        } catch (Exception ex) {
            throw new RuntimeException("广告组添加失败");
        }
        return success("广告组添加成功");
    }
    @Override
    public JSONObject updateAdverGroup(){
        Long groupID = getLongParameter("groupID",-1);
        String advertIDs = getUTF("advertIDs",null);
        if(groupID==-1){
            return fail("缺少分组ID:groupID");
        }else if(advertIDs==null){
            return fail("缺少广告ID:advertIDs");
        }
        try{
            AdvertGroup advertGroupA =new AdvertGroup();
            advertGroupA.setIsDelete(1);
            advertGroupA.setGroupId(groupID);
            advertDataAccessManager.delAdvertGroup(advertGroupA);

            String [] advertIDList= advertIDs.split(CommonUtils.SPLIT_COMMA);
            for (int i = 0; i < advertIDList.length; i++) {
                AdvertGroup advertGroup = new AdvertGroup();
                advertGroup.setGroupId(groupID);
                advertGroup.setAdvertId(Long.parseLong(advertIDList[i]));
                advertDataAccessManager.saveAdvertGroup(advertGroup);
            }
        } catch (Exception ex) {
            throw new RuntimeException("广告组更新失败");
        }
        return success("广告组更新成功");
    }
    @Override
    public JSONObject saveDownload(){
        Long marketID = getLongParameter("marketID",-1);
        Long screenID = getLongParameter("screenID",-1);
        Long groupID = getLongParameter("groupID",-1);
        Long advertID = getLongParameter("advertID",-1);
        int isDown = getIntParameter("isDown",0);
        if(groupID==-1){
            return fail("缺少分组ID:groupID");
        }else if(advertID==-1){
            return fail("缺少广告ID:advertID");
        }
        DownloadRecord downloadRecord = new DownloadRecord();
        downloadRecord.setGroupId(groupID);
        downloadRecord.setAdvertId(advertID);
        downloadRecord.setMarketId(marketID);
        downloadRecord.setScreenId(screenID);
        downloadRecord.setIsDown(isDown);
        if(advertDataAccessManager.saveDownload(downloadRecord)>0){
            return success("添加成功");
        }
        return fail("删除失败");
    }
    @Override
    public JSONObject getAllGroup(){
        List<GroupInfo> groupList = advertDataAccessManager.getAllGroup();
        return success(groupList);
    }

}
