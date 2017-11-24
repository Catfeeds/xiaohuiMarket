package com.xlibao.saas.market.manager.data.mapper;

import com.xlibao.saas.market.manager.data.model.OperationEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperationEntryMapper {

    List<OperationEntry> searchOperations(@Param("searchModel") OperationEntry searchModel, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int operationCount();

    int createOperation(OperationEntry entry);

    int updateOperation(OperationEntry entry);

    OperationEntry searchOperationById(@Param("id") long id);

    int operationDel(OperationEntry entry);
}