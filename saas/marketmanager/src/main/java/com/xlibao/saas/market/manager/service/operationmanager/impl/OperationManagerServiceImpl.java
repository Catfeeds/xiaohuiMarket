package com.xlibao.saas.market.manager.service.operationmanager.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xlibao.common.BasicWebService;
import com.xlibao.saas.market.manager.data.mapper.OperationDataAccessManager;
import com.xlibao.saas.market.manager.data.model.OperationEntry;
import com.xlibao.saas.market.manager.service.operationmanager.OperationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service("operationManagerService")
public class OperationManagerServiceImpl extends BasicWebService implements OperationManagerService {

    @Autowired
    private OperationDataAccessManager operationDataAccessManager;

    @Override
    public JSONObject searchOperations(OperationEntry entry, int pageSize, int pageIndex) {
        List<OperationEntry> operations = operationDataAccessManager.searchOperations(entry , pageSize,pageIndex);
        int count = operationDataAccessManager.operationCount();

        JSONObject response = new JSONObject();
        response.put("data", operations);
        response.put("count", count);

        return success(response);
    }

    @Override
    public JSONObject operationEditSave() {
        long operationId = getLongParameter("operationId", 0);
        long fatherId = getLongParameter("fatherId" , -1);

        String operationKey = getUTF("operationKey");
        String operationValue = getUTF("operationValue");
        String url = getUTF("url" , "");
        String explain = getUTF("explain" , "");

        OperationEntry entry = new OperationEntry();
        entry.setFatherId(fatherId);
        entry.setOperationKey(operationKey);
        entry.setOperationValue(operationValue);
        entry.setUrl(url);
        entry.setIsDelete(1); // 默认未删除
        entry.setExplain(explain);
        entry.setCreateTime(new Date());
        entry.setUpdateTime(new Date());

        if(operationId > 0) {
            // 编辑
            entry.setId(operationId);
            if(operationDataAccessManager.updateOperation(entry) > 0)
                return success("编辑成功");

            return fail("编辑失败");
        } else {
            // 新增
            if(operationDataAccessManager.createOperation(entry) > 0)
                return success("添加成功");

            return fail("添加失败");
        }
    }

    @Override
    public JSONObject searchOperationById(long id) {
        OperationEntry entry = operationDataAccessManager.searchOperationById(id);
        if(entry == null)
            return fail("未找到权限信息");

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(entry ,SerializerFeature.WriteMapNullValue));

        return response;
    }

    @Override
    public JSONObject operationDel() {
        long operationId = getLongParameter("operationId", 0);

        OperationEntry entry = new OperationEntry();
        entry.setId(operationId);
        entry.setIsDelete(0);
        entry.setUpdateTime(new Date());

        if(operationDataAccessManager.operationDel(entry) > 0)
            return success("删除成功");

        return fail("删除失败");
    }
}