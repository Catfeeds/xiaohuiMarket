package com.xlibao.item.service.item;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/3/1.
 */
public interface ItemService {

    JSONObject loaderItemTemplates();

    JSONObject getItemTemplateList();

    JSONObject getItemTemplate();

    JSONObject getItemTemplateIdAndNames();

    JSONObject getItemTemplateByName();

    JSONObject getItemTemplateByBarcode();

    JSONObject relationItemTemplates();

    JSONObject fuzzyMatchItemTemplate();

    JSONObject loaderItemBrands();

    JSONObject getItemBrand();

    JSONObject loaderItemTypes();

    JSONObject getItemType();

    JSONObject relationItemTypes();

    JSONObject loaderItemUnits();

    JSONObject getItemUnit();

    JSONObject getItemUnitList();

    JSONObject saveItemTemplate();

    JSONObject saveItemUnit();

    JSONObject saveItemType();

    JSONObject saveItemBrand();

    JSONObject saveItemRelationship();

    JSONObject searchItemUnitPageByName();

    JSONObject searchItemTypePageByName();

    JSONObject searchItemTypePage();

    JSONObject searchItemTemplatesPage();

    JSONObject updateItemTypesSort();

    JSONObject itemEditSave();

    JSONObject itemUpdateImgUrl();

    JSONObject itemTypeEditSave();

    JSONObject itemTypeUpdateIconUrl();
}