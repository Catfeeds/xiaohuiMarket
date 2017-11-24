package com.xlibao.saas.market.service.market;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.market.data.model.MarketEntry;

/**
 * @author chinahuangxc on 2017/8/16.
 */
public interface ShelvesService {

    void builderShelvesData(MarketEntry marketEntry, String content);

    JSONObject getShelvesMarks();

    JSONObject loaderClipDatas();

    JSONObject scanShelvesTask();

    JSONObject prepareAction();

    JSONObject checkPrepareActionTask();

    JSONObject unExecutorTask();

    JSONObject cancelPrepareActionTask();

    JSONObject showShelvesTask();

    JSONObject offShelves();

    JSONObject onShelves();

    JSONObject finishTodayPrepareActions();

    JSONObject findValidTasks();

    JSONObject showExceptionTask();
}