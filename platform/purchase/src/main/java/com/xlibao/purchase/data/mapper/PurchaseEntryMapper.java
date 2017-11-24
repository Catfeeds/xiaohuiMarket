package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseEntry;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;


import java.util.HashMap;
import java.util.List;

public interface PurchaseEntryMapper {

    /**
     * 采购单列表查询
     * @param supplierName
     * @param status
     * @param pageSize
     * @param pageStartIndex
     * @return
     */
    List<ResultMap> searchPurchasePage(@Param("supplierName")String supplierName,@Param("warehouseCode") String warehouseCode, @Param("statusList") String [] statusList, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);
    /**
     * 采购count
     * @param supplierName
     * @param status
     * @return
     */
    int searchPurchasePageCount(@Param("supplierName")String supplierName,@Param("warehouseCode") String warehouseCode, @Param("statusList") String [] statusList);

    /**
     * 采购单
     * @param id
     * @return
     */
    HashMap getPurchase(Long id);

    int deleteByPrimaryKey(Long id);

    int insert(PurchaseEntry record);

    int insertSelective(PurchaseEntry record);

    PurchaseEntry selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseEntry record);

    int updateByPrimaryKey(PurchaseEntry record);
}