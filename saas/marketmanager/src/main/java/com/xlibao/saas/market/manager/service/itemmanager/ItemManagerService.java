package com.xlibao.saas.market.manager.service.itemmanager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.metadata.item.ItemTemplate;
import com.xlibao.metadata.item.ItemType;
import com.xlibao.metadata.item.ItemUnit;

import java.util.List;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public interface ItemManagerService {

    public JSONObject searchItemUnitPageByName(String searchKey, int pageSize, int pageIndex);

    public JSONObject getItemUnit(long itemUnitId);

    public List<ItemUnit> getItemUnits();

    public Map<Long, ItemUnit> itemUnitsToMap(List<ItemUnit> itemUnits);

    public JSONObject updateItemUnit(long itemUnitId, String title, byte status);

    public JSONObject searchItemTypePageByName(String searchKey, int pageSize, int pageIndex);

    public JSONObject searchItemTypePage(long parentItemTypeId, int pageSize, int pageIndex);

    public Map<Long, ItemType> itemTypesToMap(List<ItemType> itemTypes);

    public List<ItemType> getItemTypes(long parentItemTypeId);

    public List<ItemType> getSortItemTypes(long parentItemTypeId);

    public JSONObject itemTypeSortEditSave(String ids);

    public ItemType getItemType(long itemTypeId);

    public List<ItemType> getSelectItemTypes();

    public JSONObject searchItemTemplatesPage(String searchType, String searchKey, int pageSize, int pageIndex);

    public JSONObject getItemTemplateJson(long itemTemplateId);

    public ItemTemplate getItemTemplate(long itemTemplateId);

    public JSONObject removeItemTemplates(String[] ids);

}
