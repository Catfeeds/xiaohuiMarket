package com.xlibao.item.data.mapper;

import com.xlibao.metadata.item.ItemType;
import com.xlibao.metadata.item.ItemUnit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface ItemTypeMapper {

    List<ItemType> loaderItemTypes(@Param("status") int status);

    List<ItemType> relationItemTypes(@Param("parentItemTypeId") long parentItemTypeId, @Param("status") int status);

    ItemType getItemType(@Param("itemTypeId") long itemTypeId);

    @Insert("insert into item_type (title, parent_id, status, sort, top, icon, image) values (#{type.title}, #{type.parentId}, #{type.status}, #{type.sort}, #{type.top}, #{type.icon}, #{type.image})")
    @SelectKey(before=false,keyProperty="type.id",resultType=Long.class,statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
    Long add(@Param("type") ItemType type);


    /** 根据 父分类 ID 获取数量 */
    Integer itemTypesCount(@Param("parentItemTypeId") long parentItemTypeId);

    /** 根据 父分类 ID 获取分页 */
    List<ItemType> pageItemType(@Param("parentItemTypeId") long parentItemTypeId, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex, @Param("sort") boolean sort);

    /** 根据 商品类型 名称 搜索 返回列表 */
    List<ItemType> searchItemTypePageByName(@Param("searchKey") String searchKey, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    /** 根据 商品类型 名称 搜索 返回数量 */
    Integer searchItemTypeCountByName(@Param("searchKey") String searchKey);

    //返回排序值最大的
    ItemType getItemTypeSortMaxId();

    void updateItemTypeSort(@Param("id") long id, @Param("sort") int sort);

    int updateItemTypeIconUrl(@Param("id") long id, @Param("icon") String iconUrl);

    int updateItemType(@Param("type") ItemType type);
}