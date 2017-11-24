package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseWarehouse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

public interface PurchaseWarehouseMapper {
    /**
     * 仓库列表查询
     * @param supplierName
     * @param status
     * @param pageSize
     * @param pageStartIndex
     * @return
     */
    List<ResultMap> searchWarehousePage(@Param("warehouseName")String supplierName, @Param("status") int status, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    /**
     * 仓库总数
     * @param supplierName
     * @param status
     * @return
     */
    int searchWarehousePageCount(@Param("warehouseName")String supplierName,@Param("status") int status);


    /**
     * 仓库集合
     * @return
     */
    List<PurchaseWarehouse> getAllWarehouse();

    int deleteByPrimaryKey(Long id);

    int insert(PurchaseWarehouse record);

    int insertSelective(PurchaseWarehouse record);

    PurchaseWarehouse selectByPrimaryKey(Long id);

    PurchaseWarehouse getByWarehouseCode(String warehouseCode);

    int updateByPrimaryKeySelective(PurchaseWarehouse record);

    int updateByPrimaryKey(PurchaseWarehouse record);
}