package com.xlibao.item.data.mapper;

import com.xlibao.metadata.item.ItemTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemTemplateMapper {

    List<ItemTemplate> loaderItemTemplates(@Param("status") int status);

    List<ItemTemplate> getItemTemplates(@Param("itemTypeId") long itemTypeId, @Param("status") int status);

    ItemTemplate getItemTemplate(@Param("itemTemplateId") long itemTemplateId, @Param("status") int status);

    ItemTemplate getItemTemplateByName(@Param("itemName") String itemName, @Param("status") int status);

    ItemTemplate getItemTemplateByBarcode(@Param("barcode") String barcode);
    
    int add(@Param("template") ItemTemplate template);
    
    void updateItemTemplate(@Param("template") ItemTemplate template);

    List<ItemTemplate> getItemTemplateList(@Param("template") ItemTemplate template, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int getItemTemplateListCount(@Param("template") ItemTemplate template);

    List<ItemTemplate> searchItemTemplates(@Param("searchType") String searchType, @Param("searchKey") String searchKey, @Param("pageSize") int pageSize, @Param("pageStartIndex") int pageStartIndex);

    int searchItemTemplateCount(@Param("searchType") String searchType, @Param("searchKey") String searchKey);

    List<ItemTemplate> getItemTemplateIdAndNames(@Param("typeId") long typeId);

    List<ItemTemplate> fuzzyMatchItemTemplate(@Param("name") String name);

    int createTemplate(ItemTemplate itemTemplate);

    int updateTemplate(ItemTemplate itemTemplate);

    int updateTemplateImgUrl(@Param("itemId") long itemId, @Param("itemImgUrl") String itemImgUrl);
}