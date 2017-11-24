package com.xlibao.saas.market.manager.data.mapper;

import com.xlibao.saas.market.manager.data.model.OperationEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationDataAccessManager {

    @Autowired
    private OperationEntryMapper operationEntryMapper;

    public List<OperationEntry> searchOperations(OperationEntry searchModel, int pageSize, int pageStartIndex){
        return operationEntryMapper.searchOperations(searchModel,pageSize,pageStartIndex);
    }

    public int operationCount(){
        return operationEntryMapper.operationCount();
    }

    public int createOperation(OperationEntry entry) {
        return  operationEntryMapper.createOperation(entry);
    }

    public int updateOperation(OperationEntry entry){
        return  operationEntryMapper.updateOperation(entry);
    }

    public OperationEntry searchOperationById(long id){
        return operationEntryMapper.searchOperationById(id);
    }

    public int operationDel(OperationEntry entry){
        return operationEntryMapper.operationDel(entry);
    }
}