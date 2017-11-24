package com.xlibao.advert.service.item;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by admin on 2017/8/28.
 */
public interface advertService {

    JSONObject getAdvertTemplateList();

    JSONObject getAllAdvertInfo();

    JSONObject getAdvertCount();

    JSONObject updateIsDown();

    JSONObject uploadAdvertInfo();

    JSONObject getAdvertFromID();

    JSONObject updateAdvertInfo();

    JSONObject deleteAdvertFromID();

    JSONObject getScreenInfoFromMAC();

    JSONObject getScreenTemplateList();

    JSONObject addScreenInfo();

    JSONObject deleteScreenFromID();

    JSONObject getAdvertTemplates();

    JSONObject addAdvertInfoForScreen();

    JSONObject updateAdvertInfoFromID();

    JSONObject deleteAdvertInfoFromID();

    JSONObject getAllScreenInfo();

    JSONObject getAdvertInfoFromID();

    JSONObject getAdvertInfoFromScreenID();

    JSONObject getScreenInfoFromAdvertID();

    /**
     * 广告组查询
     * @return
     */
    JSONObject searchGroupPage();

    /**
     * 新增广告组
     * @return
     */
    JSONObject saveGroup();

    /**
     * 获取广告组信息
     * @return
     */
    JSONObject getGroup();

    /**
     * 获取广告组下的广告
     * @return
     */
    JSONObject getAdvertListByGroupID();
    /**
     * 获取播放广告组下的广告
     * @return
     */
    JSONObject getAdvertsByParameter();

    /**
     * 删除广告组
     * @return
     */
    JSONObject delGroup();

    /**
     * 删除广告组下的广告
     * @return
     */
    JSONObject delAdvertGroup();

    /**
     * 设置广告组广告
     * @return
     */
    JSONObject saveAdverGroup();

    /**
     * 更新广告组广告
     * @return
     */
    JSONObject updateAdverGroup();

    /**
     * 下载记录
     * @return
     */
    JSONObject saveDownload();

    /**
     * 所有組
     * @return
     */
    JSONObject getAllGroup();
}
