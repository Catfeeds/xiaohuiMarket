package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseCommodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseCommodityMapper {
    /**
     * 采购单数据
      * @param purchaseId
     * @return
     */
   List<PurchaseCommodity> getPurchaseCommodityS(@Param("purchaseId") Long purchaseId);
    /**
     * 供应商产品数据
     * @param supplierId
     * @return
     */
    List<PurchaseCommodity> getSupplierCommodityS(@Param("supplierId") Long supplierId);


   int deleteByPrimaryKey(@Param("purchaseId") Long purchaseId);

    int insert(PurchaseCommodity record);

    int insertSelective(PurchaseCommodity record);

    PurchaseCommodity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseCommodity record);

    int updatePurchaseCommodity(PurchaseCommodity record);

    int updateByPrimaryKey(PurchaseCommodity record);
}