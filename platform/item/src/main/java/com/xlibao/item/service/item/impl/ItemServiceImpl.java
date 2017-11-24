package com.xlibao.item.service.item.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.GlobalConstantConfig;
import com.xlibao.common.constant.item.ItemStatusEnum;
import com.xlibao.item.data.mapper.ItemDataAccessManager;
import com.xlibao.item.service.item.ItemService;
import com.xlibao.metadata.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/3/1.
 */
@Transactional
@Service("itemService")
public class ItemServiceImpl extends BasicWebService implements ItemService {

    @Autowired
    private ItemDataAccessManager itemDataAccessManager;

    @Override
    public JSONObject loaderItemTemplates() {
        List<ItemTemplate> itemTemplates = itemDataAccessManager.loaderItemTemplates(ItemStatusEnum.NORMAL);
        JSONArray itemTemplateArray = itemTemplates.stream().map(itemTemplate -> JSONObject.parseObject(JSONObject.toJSONString(itemTemplate))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("itemTemplateArray", itemTemplateArray);
        return success(response);
    }

    @Override
    public JSONObject getItemTemplateList() {
        int pageSize = getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);
        ItemTemplate template = new ItemTemplate();
        getEntityParameter(template);


        List<ItemTemplate> itemUnit = itemDataAccessManager.getItemTemplateList(template, pageSize, pageStartIndex);
        int count = itemDataAccessManager.getItemTemplateCount(template);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(itemUnit)));
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject getItemTemplate() {
        long itemTemplateId = getLongParameter("itemTemplateId");
        ItemTemplate itemTemplate = itemDataAccessManager.getItemTemplate(itemTemplateId, ItemStatusEnum.NORMAL);

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemTemplate));
        return success(response);
    }


    public JSONObject getItemTemplateIdAndNames() {
        long itemTypeId = getLongParameter("itemTypeId");
        List<ItemTemplate> itemTemplates = itemDataAccessManager.getItemTemplateIdAndNames(itemTypeId);
        if (itemTemplates.size() > 0) {
            JSONObject response = new JSONObject();
            response.put("datas", itemTemplates);
            return success(response);
        }
        return fail("该分类下没有商品");
    }

    @Override
    public JSONObject getItemTemplateByName() {
        String itemName = getUTF("itemName");
        ItemTemplate itemTemplate = itemDataAccessManager.getItemTemplateByName(itemName, ItemStatusEnum.NORMAL);

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemTemplate));
        return success(response);
    }

    @Override
    public JSONObject getItemTemplateByBarcode() {
        String barcode = getUTF("barcode");

        ItemTemplate itemTemplate = itemDataAccessManager.getItemTemplateByBarcode(barcode);
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemTemplate));
        return success(response);
    }

    @Override
    public JSONObject relationItemTemplates() {
        long itemTypeId = getLongParameter("itemTypeId");

        List<ItemTemplate> itemTemplates = itemDataAccessManager.getItemTemplates(itemTypeId, ItemStatusEnum.NORMAL);
        JSONArray itemTemplateArray = itemTemplates.stream().map(itemTemplate -> JSONObject.parseObject(JSONObject.toJSONString(itemTemplate))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("itemTemplateArray", itemTemplateArray);
        return success(response);
    }

    @Override
    public JSONObject fuzzyMatchItemTemplate() {
        String name = getUTF("name");
        List<ItemTemplate> itemTemplates = itemDataAccessManager.fuzzyMatchItemTemplate(name);
        if (CommonUtils.isEmpty(itemTemplates)) {
            return fail("找不到名称包含【" + name + "】的商品模版");
        }
        JSONArray response = itemTemplates.stream().map(itemTemplate -> JSONObject.parseObject(JSONObject.toJSONString(itemTemplate))).collect(Collectors.toCollection(JSONArray::new));
        return success(response);
    }

    @Override
    public JSONObject loaderItemBrands() {
        List<ItemBrand> itemBrands = itemDataAccessManager.loaderItemBrands(ItemStatusEnum.NORMAL);

        JSONArray itemBrandArray = itemBrands.stream().map(itemBrand -> JSONObject.parseObject(JSONObject.toJSONString(itemBrand))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("itemBrandArray", itemBrandArray);
        return success(response);
    }

    @Override
    public JSONObject getItemBrand() {
        long itemBrandId = getLongParameter("itemBrandId");

        ItemBrand itemBrand = itemDataAccessManager.getItemBrand(itemBrandId, ItemStatusEnum.NORMAL);

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemBrand));
        return success(response);
    }

    @Override
    public JSONObject loaderItemTypes() {
        List<ItemType> itemTypes = itemDataAccessManager.loaderItemTypes(ItemStatusEnum.NORMAL);

        JSONArray itemTypeArray = itemTypes.stream().map(itemType -> JSONObject.parseObject(JSONObject.toJSONString(itemType))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("itemTypeArray", itemTypeArray);
        return success(response);
    }

    @Override
    public JSONObject getItemType() {
        long itemTypeId = getLongParameter("itemTypeId");
        ItemType itemType = itemDataAccessManager.getItemType(itemTypeId);

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemType));
        return success(response);
    }

    @Override
    public JSONObject relationItemTypes() {
        long parentItemTypeId = getLongParameter("parentItemTypeId");

        List<ItemType> itemTypes = itemDataAccessManager.relationItemTypes(parentItemTypeId, ItemStatusEnum.NORMAL);

        JSONArray itemTypeArray = itemTypes.stream().map(itemType -> JSONObject.parseObject(JSONObject.toJSONString(itemType))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("itemTypeArray", itemTypeArray);
        return success(response);
    }

    @Override
    public JSONObject loaderItemUnits() {
        List<ItemUnit> itemUnits = itemDataAccessManager.loaderItemUnits(ItemStatusEnum.NORMAL);

        JSONArray itemUnitArray = itemUnits.stream().map(itemUnit -> JSONObject.parseObject(JSONObject.toJSONString(itemUnit))).collect(Collectors.toCollection(JSONArray::new));

        JSONObject response = new JSONObject();
        response.put("itemUnitArray", itemUnitArray);

        return success(response);
    }

    @Override
    public JSONObject getItemUnit() {
        long itemUnitId = getLongParameter("itemUnitId");
        ItemUnit itemUnit = itemDataAccessManager.getItemUnit(itemUnitId);

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemUnit));
        return success(response);
    }

    @Override
    public JSONObject getItemUnitList() {
        String title = getUTF("title", null);
        int status = getIntParameter("status", -1);
        int pageSize = getIntParameter("pageSize", GlobalConstantConfig.DEFAULT_PAGE_SIZE);
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<ItemUnit> itemUnit = itemDataAccessManager.getItemUnitList(title, status, pageSize, pageStartIndex);
        int count = itemDataAccessManager.getItemUnitListCount(title, status);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(itemUnit)));
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject saveItemTemplate() {
        Long id = getLongParameter("id", 0);
        ItemTemplate template = new ItemTemplate();

        if (id != 0) template = itemDataAccessManager.getItemTemplate(id, ItemStatusEnum.ALL);
        getEntityParameter(template);
        template.setUploadTime(new Date());
        if (id != 0) {
            itemDataAccessManager.updateItemTemplate(template);
        } else {
            itemDataAccessManager.addItemTemplate(template);
        }
        template = itemDataAccessManager.getItemTemplate(template.getId(), ItemStatusEnum.ALL);
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(template));
        return success(response);
    }

    @Override
    public JSONObject saveItemUnit() {
        Long id = getLongParameter("id", 0);
        String title = getUTF("title");
        byte status = getByteParameter("status");
        if (id != 0) {
            //检查名称是否重复
            ItemUnit unit = itemDataAccessManager.getItemUnitByTitle(title);
            if (unit != null && unit.getId().longValue() != id.longValue()) {
                return fail(-1, "已存在相同名称的单位");
            }
            //更新
            itemDataAccessManager.updateItemUnit(id, title, status);
            return success("修改成功");
        }
        // 检查名称是否重复
        ItemUnit unit = itemDataAccessManager.getItemUnitByTitle(title);
        if (unit != null) {
            return fail(-1, "已存在相同名称的单位");
        }
        // 新增
        ItemUnit itemUnit = new ItemUnit();
        itemUnit.setTitle(title);
        itemUnit.setStatus(status);
        if (itemDataAccessManager.createItemUnit(itemUnit) > 0) {
            return success("添加成功");
        }
        return fail("添加失败");
    }

    @Override
    public JSONObject saveItemType() {
        Long itemTypeId = getLongParameter("id", 0);
        ItemType type = new ItemType();

        if (itemTypeId != 0) type = itemDataAccessManager.getItemType(itemTypeId);
        getEntityParameter(type);
        if (itemTypeId != 0) {
            itemDataAccessManager.updateItemType(type);
        } else {
            itemDataAccessManager.addItemType(type);
        }
        type = itemDataAccessManager.getItemType(type.getId());
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(type));
        return success(response);
    }

    @Override
    public JSONObject saveItemBrand() {
        Long id = getLongParameter("id", 0);
        ItemBrand brand = new ItemBrand();

        if (id != 0) brand = itemDataAccessManager.getItemBrand(id, ItemStatusEnum.ALL);
        getEntityParameter(brand);
        brand.setCreateTime(new Date());
        if (id != 0) {
            itemDataAccessManager.updateItemBrand(brand);
        } else {
            itemDataAccessManager.addItemBrand(brand);
        }
        brand = itemDataAccessManager.getItemBrand(brand.getId(), ItemStatusEnum.ALL);
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(brand));
        return success(response);
    }

    @Override
    public JSONObject saveItemRelationship() {
        Long id = getLongParameter("id", 0);
        ItemRelationship itemRelationship = new ItemRelationship();

        if (id != 0) {
            itemRelationship = itemDataAccessManager.getItemRelationship(id);
        }
        getEntityParameter(itemRelationship);
        itemRelationship.setCreateTime(new Date());
        if (id != 0) {
            itemDataAccessManager.updateItemRelationship(itemRelationship);
        } else {
            itemDataAccessManager.addItemRelationship(itemRelationship);
        }
        itemRelationship = itemDataAccessManager.getItemRelationship(itemRelationship.getId());
        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(itemRelationship));
        return success(response);
    }

    @Override
    public JSONObject searchItemUnitPageByName() {
        String searchKey = getUTF("searchKey", null);
        if (!CommonUtils.isNotNullString(searchKey)) {
            searchKey = null;
        }
        int pageSize = getIntParameter("pageSize", 0);//默认所有单位全部返回
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<ItemUnit> itemUnit = itemDataAccessManager.searchItemUnitByName(searchKey, pageSize, pageStartIndex);
        int count = itemDataAccessManager.searchItemUnitCountByName(searchKey);

        JSONObject response = new JSONObject();
        response.put("data", itemUnit);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject searchItemTypePageByName() {
        String searchKey = getUTF("searchKey", null);
        int pageSize = getPageSize();//必须分页
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<ItemType> types = itemDataAccessManager.searchItemTypePageByName(searchKey, pageSize, pageStartIndex);
        int count = itemDataAccessManager.searchItemTypeCountByName(searchKey);

        JSONObject response = new JSONObject();
        response.put("data", types);
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject searchItemTypePage() {
        long parentItemTypeId = getLongParameter("parentItemTypeId", 0);
        int pageSize = getIntParameter("pageSize", 0); // 默认所有类型全部返回
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);
        boolean sort = getIntParameter("sort", 0) == 1;

        List<ItemType> types = itemDataAccessManager.pageItemType(parentItemTypeId, pageSize, pageStartIndex, sort);
        int count = itemDataAccessManager.itemTypesCount(parentItemTypeId);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(types)));
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject searchItemTemplatesPage() {
        String searchKey = getUTF("searchKey", null);
        if (!CommonUtils.isNotNullString(searchKey)) {
            searchKey = null;
        }
        String searchType = getUTF("searchType", null);
        int pageSize = getPageSize(); // 必须分页
        int pageStartIndex = getPageStartIndex("pageIndex", pageSize);

        List<ItemTemplate> items = itemDataAccessManager.searchItemTemplates(searchType, searchKey, pageSize, pageStartIndex);
        int count = itemDataAccessManager.searchItemTemplateCount(searchType, searchKey);

        JSONObject response = new JSONObject();
        response.put("data", JSONObject.parseArray(JSONObject.toJSONString(items)));
        response.put("count", count);
        response.put("pageIndex", getIntParameter("pageIndex", 1) - 1);
        return success(response);
    }

    @Override
    public JSONObject updateItemTypesSort() {
        // 修改的ID们
        String ids = getUTF("ids");
        String[] idList = ids.split(CommonUtils.SPLIT_COMMA);
        for (int i = 0; i < idList.length; i += 2) {
            long id = Long.parseLong(idList[i]);
            int sort = Integer.parseInt(idList[i + 1]);
            // 更新
            itemDataAccessManager.updateItemTypeSort(id, sort);
        }
        return success("操作成功");
    }

    @Override
    public JSONObject itemEditSave() {
        long itemId = getLongParameter("itemId", 0);
        String name = getUTF("name");
        String defineCode = getUTF("defineCode", "");
        String barcode = getUTF("barcode");
        String imgUrl = getUTF("imgUrl", "");
        long typeId = getLongParameter("typeId");
        long unitId = getLongParameter("unitId");
        long costPrice = CommonUtils.changeMoney(getUTF("costPrice", "0"));
        long defaultPrice = CommonUtils.changeMoney(getUTF("defaultPrice", "0"));
        long passportId = getLongParameter("passportId", 0);

        int ilength = getIntParameter("iLength", 0);
        int iwidth = getIntParameter("iWidth", 0);
        int iheight = getIntParameter("iHeight", 0);

        ItemTemplate itemTemplate = new ItemTemplate();
        itemTemplate.setId(itemId);
        itemTemplate.setName(name);
        itemTemplate.setDefineCode(defineCode);
        itemTemplate.setBarcode(barcode);
        itemTemplate.setTypeId(typeId);
        itemTemplate.setUnitId(unitId);
        itemTemplate.setCostPrice(costPrice);
        itemTemplate.setDefaultPrice(defaultPrice);
        itemTemplate.setLength(ilength);
        itemTemplate.setWidth(iwidth);
        itemTemplate.setHeight(iheight);
        itemTemplate.setStatus((byte) 0);//直接可用
        itemTemplate.setUploaderPassportId(passportId);
        itemTemplate.setImageUrl(imgUrl);

        try {
            if (itemId == 0) {
                if (itemDataAccessManager.createTemplate(itemTemplate) > 0) {
                    return success(itemTemplate);
                } else {
                    return fail("添加失败");
                }
            } else {
                // 修改
                if (itemDataAccessManager.updateTemplate(itemTemplate) > 0) {
                    return success(itemTemplate);
                } else {
                    return fail("修改失败");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (itemId == 0) {
                return fail("数据库异常，添加失败");
            } else {
                return fail("数据库异常，修改失败");
            }
        }
    }

    @Override
    public JSONObject itemUpdateImgUrl() {
        long itemId = getLongParameter("itemId");
        String itemImgUrl = getUTF("itemImgUrl");
        //更新图片
        if (itemDataAccessManager.updateTemplateImgUrl(itemId, itemImgUrl) > 0) {
            return success("更新图片成功");
        }
        return fail("更新失败");
    }

    @Override
    public JSONObject itemTypeEditSave() {

        long itemTypeId = getLongParameter("itemTypeId", 0);
        long pid = getLongParameter("parentId", 0);
        String title = getUTF("title");
        String icon = getUTF("icon", "");

        ItemType itemType = new ItemType();
        itemType.setId(itemTypeId);
        itemType.setParentId(pid);
        itemType.setTitle(title);
        itemType.setIcon(icon);

        try {
            if (itemTypeId == 0) {

                itemType.setStatus((byte) 0);//默认可用
                itemType.setSort(0);
                itemType.setImage("");
                itemType.setTop((byte)0);

                itemDataAccessManager.addItemType(itemType);

            } else {
                int v = itemDataAccessManager.updateItemType(itemType);
                System.err.println("v= " + v);
            }
            return success(itemType);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (itemTypeId == 0) {
                return fail("数据库异常，添加失败");
            } else {
                return fail("数据库异常，修改失败");
            }
        }
    }

    @Override
    public JSONObject itemTypeUpdateIconUrl() {
        long itemTypeId = getLongParameter("itemTypeId");
        String itemTypeIconUrl = getUTF("itemTypeIconUrl");
        //更新图片
        if (itemDataAccessManager.updateItemTypeIconUrl(itemTypeId, itemTypeIconUrl) > 0) {
            return success("更新图片成功");
        }
        return fail("更新失败");
    }
}