package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseSupplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseSupplierMapper {

    /**
     * 供应商列表查询
     * @param supplierName
     * @param supplierType
     * @param status
     * @param pageSize
     * @param pageStartIndex
     * @return
     */
    List<PurchaseSupplier> searchSupplierPage(@Param("supplierName")String supplierName,@Param("supplierType") int supplierType,@Param("status") int status,@Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    /**
     * 供应商总数
     * @param supplierName
     * @param supplierType
     * @param status
     * @return
     */
    int searchSupplierPageCount(@Param("supplierName")String supplierName,@Param("supplierType") int supplierType,@Param("status") int status);

    /**
     * 供应商集合
     * @return
     */
    List<PurchaseSupplier> getAllSupplier();

    int deleteByPrimaryKey(Long id);

    int insert(PurchaseSupplier record);

    int insertSelective(PurchaseSupplier record);

    PurchaseSupplier selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseSupplier record);

    int updateByPrimaryKey(PurchaseSupplier record);
}