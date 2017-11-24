package com.xlibao.saas.advertise.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.advertise.config.Advertise;
import com.xlibao.saas.advertise.config.ConfigFactory;
import com.xlibao.saas.advertise.config.LocalMAC;
import com.xlibao.saas.advertise.util.DateUtil;
import com.xlibao.saas.advertise.util.Download;
import com.xlibao.saas.advertise.util.FileUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 2017/8/25.
 */
public class JsonFileTool {
    private static final String PLAY_CONFIG =  "play.json";//视频播放列表文件
    private static  String playPath;

    public JsonFileTool(){
        playPath = this.getClass().getResource("/").getPath()+PLAY_CONFIG;
    }

    /**
     * 获取广告的配置信息
     * @return
     */
    public ArrayList<Advertise>  ReaderJson(){
        String jsonContext = FileUtil.ReadFile(playPath);
        ArrayList<Advertise> adverList = new ArrayList<Advertise>();
        if(!"".equals(jsonContext)&&jsonContext!=null) {
            JSONObject jsonObject = JSONObject.parseObject(jsonContext);

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            int size = jsonArray.size();
            for (int i = 0; i < size; i++) {
                JSONObject adverJson = jsonArray.getJSONObject(i);
                int style = adverJson.getInteger("style");
                Advertise adver = new Advertise();
                adver.setAdvertID(adverJson.getString("advertID"));
                if(style==2) {
                    String beginDate = adverJson.getString("beginTime").trim();
                    adver.setBeginTime(beginDate);
                }
                adver.setVideoName(adverJson.getString("videoName"));
                adver.setUrl(adverJson.getString("url"));
                adver.setIsDown(adverJson.getInteger("isDown"));
               // adver.setPlayOrder(adverJson.getInteger("playOrder"));
                adver.setScreenID(adverJson.getString("screenID"));
                adver.setMac(adverJson.getString("mac"));
                adver.setGroupID(adverJson.getInteger("groupID"));
                adver.setStyle(style);
                adverList.add(adver);
            }
        }
        return adverList;
    }

    /**
     * 获取广告的配置信息
     * @return
     */
    public JSONArray  ReaderJsont(){
        String jsonContext = FileUtil.ReadFile(playPath);
        JSONArray jsonArray = new JSONArray();
        if(!"".equals(jsonContext)&&jsonContext!=null) {
            JSONObject jsonObject = JSONObject.parseObject(jsonContext);
             jsonArray = jsonObject.getJSONArray("data");
        }
        return jsonArray;
    }

    /**
     *写入文件
     * @param jsonObject
     */
    public void WriteJson(JSONObject jsonObject){
        String localMAC= LocalMAC.getLocalMac();
        JSONObject response = jsonObject.getJSONObject("response");
        JSONArray jsonArray = response.getJSONArray("data");
        for(int  i = 0; i < jsonArray.size(); i++){
            JSONObject adverJson = jsonArray.getJSONObject(i);
            String mac = adverJson.getString("mac");
            if (!localMAC.equals(mac)){
                jsonArray.remove(i);
            }
        }
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("data",jsonArray);
        FileUtil.clearFile(playPath);
        FileUtil.writeFile(playPath,jsonobj.toString());
    }
}
