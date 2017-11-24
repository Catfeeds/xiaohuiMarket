package com.xlibao.item.data.mapper;

import com.xlibao.metadata.item.ItemUnit;
import com.xlibao.metadata.passport.Passport;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface ItemUnitMapper {

    List<ItemUnit> loaderItemUnits(@Param("status") int status);

    ItemUnit getItemUnit(@Param("itemUnitId") long itemUnitId);

    List<ItemUnit> getItemUnitList(@Param("title") String title, @Param("status") int status, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);
    int getItemUnitListCount(@Param("title") String title, @Param("status") int status);

    int createItemUnit(ItemUnit itemUnit);

    @Update("update item_unit SET title = #{title}, status = #{status} where id = #{id}")
    void update(@Param("id") long id, @Param("title") String title, @Param("status") byte status);

    /** 根据 商品类型 名称 搜索 返回数量 */
    List<ItemUnit> searchItemUnitByName(@Param("searchKey") String searchKey, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    /** 根据 商品类型 名称 搜索 返回数量 */
    Integer searchItemUnitCountByName(@Param("searchKey") String searchKey);

    ItemUnit getItemUnitByTitle(@Param("title") String title);

}