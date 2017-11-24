package com.xlibao.advert.data.mapper;

import com.xlibao.advert.data.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 2017/8/28.
 */
@Component
public class AdvertDataAccessManager {

    @Autowired
    private AdvertTemplateMapper advertTemplateMapper;

    @Autowired
    private AdvertPlayMapper advertPlayMapper;

    @Autowired
    private ScreenTemplateMapper screenTemplateMapper;

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @Autowired
    private AdvertGroupMapper advertGroupMapper;

    @Autowired
    private DownloadRecordMapper downloadRecordMapper;


    public List<AdvertInfo> getAdvertTemplateList(String title,int timeType,int isUsed,int pageSize,int pageStartIndex){
        return advertTemplateMapper.searchAdvertTemplates(title,timeType,isUsed,pageSize,pageStartIndex);
    }


    public List<AdvertScreenInfo> getAllAdvertInfo(String mac){
        return advertPlayMapper.getAllAdvertInfo(mac);
    }

    public int getAdvertCount(){return advertTemplateMapper.getAdvertCount();}

    public void updateIsDown(int isDown,int advertID,int screenID,int marketID,String successDownTime){
        advertPlayMapper.updateIsDown(isDown,advertID,screenID,marketID,successDownTime);
    }

    public int uploadAdvertInfo(AdvertInfo infos){
        return advertTemplateMapper.uploadAdvertInfo(infos);
    }

    public int getMaxAdvertID(){
        return advertTemplateMapper.getMaxAdvertID();
    }

    public List<AdvertInfo> getAdvertFromID(int advertID){
        return advertTemplateMapper.searchAdvertFromID(advertID);
    }

    public void updateAdvertInfo(String title,int timeSize,String remark,int advertID){advertTemplateMapper.updateAdvertInfo(title,timeSize,remark,advertID);}

    public void deleteAdvertFromID(int advertID){advertTemplateMapper.deleteAdvertFromID(advertID);}

    public List<ScreenInfo> getScreenInfoFromMAC(String mac){return screenTemplateMapper.getScreenInfoFromMAC(mac);}

    public List<ScreenInfo> getScreenTemplateList(String code,int marketId,String screenSize,int pageSize,int pageStartIndex){return screenTemplateMapper.getScreenTemplateList(code,marketId,screenSize,pageSize,pageStartIndex);}

    public List<ScreenInfo> getAllScreenInfo(int marketId){return screenTemplateMapper.getAllScreenInfo(marketId);};

    public int getMaxScreenID(){return screenTemplateMapper.getMaxScreenID();}

    public int addScreenInfo(ScreenInfo infos){return screenTemplateMapper.addScreenInfo(infos);}

    public int getScreenCount(){return screenTemplateMapper.getScreenCount();}

    public int deleteScreenFromID(int screenID,int marketID){return screenTemplateMapper.deleteScreenFromID(screenID,marketID);}

    public List<AdvertScreenInfo> getAdvertTemplates(int marketId,String code,String title,String beginTime,String endTime,int isDown,int playStatus,int pageSize,int pageStartIndex){return advertPlayMapper.getAdvertTemplates(marketId,code,title,beginTime,endTime,isDown,playStatus,pageSize,pageStartIndex);}

    public int getAdvertInfoCount(int marketId,String code,String title,String beginTime,String endTime,int isDown,int playStatus){return advertPlayMapper.getAdvertInfoCount(marketId,code,title,beginTime,endTime,isDown,playStatus);}

    public List<AdvertScreenInfo> getAdvertInfoFromID(int advertID,int screenID,int marketID){return advertPlayMapper.getAdvertInfoFromID(advertID,screenID,marketID);}

    public int addAdvertInfoForScreen(int marketID,int advertID,int screenID,String beginTime,int style,int playOrder,String remark){return advertPlayMapper.addAdvertInfoForScreen(marketID,advertID,screenID,beginTime,style,playOrder,remark);}

    public int updateAdvertInfoFromID(int advertID,int screenID,int marketId,int cMarketID,int cScreenID,String beginTime,int playOrder,String remark,int style){return advertPlayMapper.updateAdvertInfoFromID(advertID,screenID,marketId,cMarketID,cScreenID,beginTime,playOrder,remark,style);}

    public int deleteAdvertInfoFromID(int advertID,int screenID,int marketID){return advertPlayMapper.deleteAdvertInfoFromID(advertID,screenID,marketID);}

    public List<AdvertScreenInfo> getAdvertInfoFromScreenID(int screenID){return advertPlayMapper.getAdvertInfoFromScreenID(screenID);}

    public int isMarketExist(int marketID){return screenTemplateMapper.isMarketExist(marketID);}

    public int getMaxScreenIDWithMarketID(int marketID){return screenTemplateMapper.getMaxScreenIDWithMarketID(marketID);}

    public int isExistAdvert(){return advertTemplateMapper.isExistAdvert();}

    public List<ScreenInfo> getScreenInfoFromAdvertID(int advertID){return screenTemplateMapper.getScreenInfoFromAdvertID(advertID);}

    public int saveGroup(GroupInfo groupInfo){
        return groupInfoMapper.insertSelective(groupInfo);
    }

    public int updateGroup(GroupInfo groupInfo){
        return groupInfoMapper.updateByPrimaryKeySelective(groupInfo);
    }

    public GroupInfo getGroup(Long groupID){
       return groupInfoMapper.selectByPrimaryKey(groupID);
    }

    public  List<GroupInfo> searchGroupPage(String groupName,int pageSize, int pageStartIndex){
        return groupInfoMapper.searchGroupPage(groupName,pageSize, pageStartIndex);
    }

    public int searchGroupPageCount(String groupName){
        return  groupInfoMapper.searchGroupPageCount(groupName);
    }

    public int saveAdvertGroup(AdvertGroup advertGroup){
        return advertGroupMapper.insertSelective(advertGroup);
    }

    public int delAdvertGroup(AdvertGroup advertGroup){
        return advertGroupMapper.delAdvertGroup(advertGroup);
    }

    public List<AdvertInfo> getAdvertByGroupId(Long groupId){
        return advertTemplateMapper.getAdvertByGroupId(groupId);
    }

    public List<AdvertInfo> getAdvertsByParameter(Long groupId,Long screenId,Long marketId){
        return advertTemplateMapper.getAdvertsByParameter(groupId,screenId,marketId);
    }

    public int saveDownload(DownloadRecord downloadRecord){
        return downloadRecordMapper.insertSelective(downloadRecord);
    }
    public List<GroupInfo> getAllGroup(){
        return groupInfoMapper.getAllGroup();
    }

}

