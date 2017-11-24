package com.xlibao.saas.market.manager.service.operationmanager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.manager.data.model.OperationEntry;

public interface OperationManagerService {

    /**
     * 分页查询
     * @param entry
     * @param pageSize
     * @param pageIndex
     * @return
     */
    public JSONObject searchOperations(OperationEntry entry, int pageSize, int pageIndex);

    JSONObject operationEditSave();

    JSONObject searchOperationById(long id);

    JSONObject operationDel();
}