package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseCommodityStores;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Locale;

public interface PurchaseCommodityStoresMapper {

    /**
     * 商品库存列表查询
     * @param warehouseCode
     * @param itemName
     * @param barcode
     * @param pageSize
     * @param pageStartIndex
     * @return
     */
    List<PurchaseCommodityStores> searchCommodityStoresPage(@Param("warehouseCode")String warehouseCode, @Param("itemName") String itemName, @Param("barcode") String barcode, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    /**
     * 商品库存总数
     * @param warehouseCode
     * @param itemName
     * @param barcode
     * @return
     */
    int searchCommodityStoresPageCount(@Param("warehouseCode")String warehouseCode,@Param("itemName") String itemName,@Param("barcode") String barcode);


    int deleteByPrimaryKey(Long id);

    int insert(PurchaseCommodityStores record);

    int insertSelective(PurchaseCommodityStores record);

    PurchaseCommodityStores selectByPrimaryKey(Long id);


    PurchaseCommodityStores getByParameterID(@Param("warehouseCode")String warehouseCode,@Param("itemId") Long itemId);

    int updateByPrimaryKeySelective(PurchaseCommodityStores record);

    int updateByPrimaryKey(PurchaseCommodityStores record);
}