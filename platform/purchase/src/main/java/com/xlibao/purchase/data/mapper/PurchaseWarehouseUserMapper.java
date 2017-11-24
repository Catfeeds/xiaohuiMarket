package com.xlibao.purchase.data.mapper;

import com.xlibao.purchase.data.model.PurchaseWarehouseUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseWarehouseUserMapper {


    /**
     * 仓库管理员集合
     * @return
     */
    List<PurchaseWarehouseUser> getAllWarehouseUser(@Param("warehouseId")Long warehouseId);

    int deleteByPrimaryKey(Long id);

    int insert(PurchaseWarehouseUser record);

    int insertSelective(PurchaseWarehouseUser record);

    PurchaseWarehouseUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PurchaseWarehouseUser record);

    int updateByPrimaryKey(PurchaseWarehouseUser record);
}