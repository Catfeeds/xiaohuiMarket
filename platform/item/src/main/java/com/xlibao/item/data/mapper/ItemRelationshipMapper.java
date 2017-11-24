package com.xlibao.item.data.mapper;

import com.xlibao.metadata.item.ItemRelationship;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

public interface ItemRelationshipMapper {

    @Select("select * from item_relationship where id = #{id}")
    ItemRelationship getItemRelationship(@Param("id") long id);

    @Insert("insert into item_relationship (title, source_barcode, target_barcode, relation_coefficient, create_time) value (#{rship.title}, #{rship.sourceBarcode}, #{rship.targetBarcode}, #{rship.relationCoefficient}, #{rship.createTime})")
    @SelectKey(before=false,keyProperty="brand.id",resultType=Long.class,statementType= StatementType.STATEMENT,statement="SELECT LAST_INSERT_ID() AS id")
    Long add(@Param("rship") ItemRelationship rship);

    @Update("update into item_relationship set title = #{rship.title}, source_barcode = #{rship.sourceBarcode}, target_barcode = #{rship.targetBarcode}, relation_coefficient = #{rship.relationCoefficient}, create_time = #{rship.createTime} where id = #{rship.id}")
    void update(@Param("rship") ItemRelationship rship);
}