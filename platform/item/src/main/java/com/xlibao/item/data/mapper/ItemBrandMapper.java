package com.xlibao.item.data.mapper;

import com.xlibao.metadata.item.ItemBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemBrandMapper {

    List<ItemBrand> loaderItemBrands(@Param("status") int status);

    ItemBrand geItemBrand(@Param("itemBrandId") long itemBrandId, @Param("status") int status);

    Long add(@Param("brand") ItemBrand brand);

    int update(@Param("brand") ItemBrand brand);
}