package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseRepertoryRecord;

public interface PurchaseRepertoryRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PurchaseRepertoryRecord record);

    int insertSelective(PurchaseRepertoryRecord record);

    PurchaseRepertoryRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseRepertoryRecord record);

    int updateByPrimaryKey(PurchaseRepertoryRecord record);
}