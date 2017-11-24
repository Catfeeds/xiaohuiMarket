package com.xlibao.saas.market.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.metadata.item.ItemIdName;
import com.xlibao.metadata.item.ItemType;
import com.xlibao.metadata.item.ItemUnit;
import com.xlibao.saas.market.manager.BaseController;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.config.LogicConfig;
import com.xlibao.saas.market.manager.service.itemmanager.ItemManagerService;
import com.xlibao.saas.market.manager.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/marketmanager/item")
public class ItemManagerController extends BaseController {

    @Autowired
    private ItemManagerService itemManagerService;

    @RequestMapping("/itemList")
    public String getItemList(ModelMap map) {

        //取得商品列表
        String searchType = getUTF("searchType", "");
        String searchKey = getUTF("searchKey", "");
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject itemJson = itemManagerService.searchItemTemplatesPage(searchType, searchKey, pageSize, pageIndex);

        if (itemJson.getIntValue("code") != 0) {
            return remoteFail(map, itemJson, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
        }

        //类型列表
        Map<Long, ItemType> itemTypeMap = itemManagerService.itemTypesToMap(itemManagerService.getItemTypes(-1));

        //单位列表
        Map<Long, ItemUnit> itemUnitMap = itemManagerService.itemUnitsToMap(itemManagerService.getItemUnits());


        JSONObject response = itemJson.getJSONObject("response");

        JSONArray items = response.getJSONArray("data");
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = items.getJSONObject(i);
            long type_id = item.getLong("typeId");//类型ID
            ItemType itemType = itemTypeMap.get(type_id);
            if (itemType != null) {
                item.put("typeName", itemType.getTitle());
            } else {
                item.put("typeName", "无");
            }

            long unit_id = item.getLong("unitId");//单位ID
            ItemUnit itemUnit = itemUnitMap.get(unit_id);
            if (itemUnit != null) {
                item.put("unitName", itemUnit.getTitle());
            } else {
                item.put("typeName", "无");
            }
            //重置成本价
            Utils.changePrice(item, "defaultPrice");
            //重置零售价
            Utils.changePrice(item, "costPrice");
            //重置日期
            Utils.changeData(item, "uploadTime");
        }

        map.put("itemlist", items);
        map.put("searchType", searchType);
        map.put("searchKey", searchKey);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);

        map.put("count", response.getIntValue("count"));

        return jumpPage(map, LogicConfig.FTL_ITEM_LIST, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
    }

    @RequestMapping("/itemEdit")
    public String itemEdit(ModelMap map) {

        long itemTemplaterId = getLongParameter("id", 0);

        if (itemTemplaterId != 0) {
            JSONObject itemJson = itemManagerService.getItemTemplateJson(itemTemplaterId);

            if (itemJson.getIntValue("code") != 0) {
                return remoteFail(map, itemJson, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
            }
            JSONObject item = itemJson.getJSONObject("response");
            //重置成本价
            Utils.changePrice(item, "defaultPrice");
            //重置零售价
            Utils.changePrice(item, "costPrice");

            map.put("item", item);
        }

        //所有单位
        map.put("itemUnits", itemManagerService.getItemUnits());

        //所有类型
        map.put("itemTypes", itemManagerService.getSelectItemTypes());

        return jumpPage(map, LogicConfig.FTL_ITEM_EDIT, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TEMPLATE);
    }

    //商店 编辑 保存
    @ResponseBody
    @RequestMapping("/itemEditSave")
    public JSONObject itemEditSave() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().itemRemoteURL + "item/itemEditSave.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    @ResponseBody
    @RequestMapping("/itemUpdateImgUrl")
    public JSONObject itemUpdateImgUrl() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().itemRemoteURL + "item/itemUpdateImgUrl.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    @RequestMapping("/itemTypes")
    public String getItemTypes(ModelMap map) {

        String searchKey = getUTF("searchKey", "");
        long parentItemTypeId = getLongParameter("id", 0);
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject typesJson = null;
        if (CommonUtils.isNotNullString(searchKey)) {
            //直接搜索
            typesJson = itemManagerService.searchItemTypePageByName(searchKey, pageSize, pageIndex);
        } else {
            //以父分类ID进行获取
            typesJson = itemManagerService.searchItemTypePage(parentItemTypeId, pageSize, pageIndex);
            //如果是查子，则将父也查出来
            if (parentItemTypeId != 0) {
                ItemType itemType = itemManagerService.getItemType(parentItemTypeId);
                if (itemType != null) {
                    map.put("itemType", itemType);
                }
            }
        }

        if (typesJson.getIntValue("code") != 0) {
            return remoteFail(map, typesJson, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TYPE);
        }

        JSONObject response = typesJson.getJSONObject("response");

        map.put("searchKey", searchKey);
        map.put("itemTypes", response.getJSONArray("data"));
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("count", response.getIntValue("count"));

        return jumpPage(map, LogicConfig.FTL_ITEM_TYPES, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TYPE);
    }

    @RequestMapping("/itemTypeEdit")
    public String itemTypeEdit(ModelMap map) {

        long itemTypeId = getLongParameter("id", 0);//修改某个分类，这个指定了某个分类的值
        long pid = getLongParameter("parentId", 0);//添加子类时，这个值指定了父ID

        //是否是编辑一级
        boolean one = false;

        if (itemTypeId != 0) {
            ItemType itemType = itemManagerService.getItemType(itemTypeId);
            map.put("itemType", itemType);
            if(itemType.getParentId() == 0) {
                one = true;
            }
        }

        if (pid != 0) {
            ItemType itemType = itemManagerService.getItemType(pid);
            map.put("pItemType", itemType);
        }

        //发回所有1级，以添加2级时，绑定
        if(!one) {
            map.put("itemTypes", itemManagerService.getItemTypes(0));
        }

        return jumpPage(map, LogicConfig.FTL_ITEM_TYPE_EDIT, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TYPE);
    }


    @ResponseBody
    @RequestMapping("/itemTypeEditSave")
    public JSONObject itemTypeEditSave() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().itemRemoteURL + "item/itemTypeEditSave.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    @ResponseBody
    @RequestMapping("/itemTypeUpdateIconUrl")
    public JSONObject itemTypeUpdateIconUrl() {
        Map map = getMapParameter();
        String url = ConfigFactory.getDomainNameConfig().itemRemoteURL + "item/itemTypeUpdateIconUrl.do";
        String json = HttpRequest.post(url, map);
        return JSONObject.parseObject(json);
    }

    @RequestMapping("/itemTypeSort")
    public String itemTypeSort(ModelMap map) {

        Long id = getLongParameter("id", 0);

        List<ItemType> types = itemManagerService.getSortItemTypes(0);//取得所有一级

        //如果有选中其中的一级进行排序子类
        if(id != 0) {
            //排序
            map.put("cItemTypes", itemManagerService.getSortItemTypes(id));
        } else {
            map.put("cItemTypes", types);
        }
        map.put("itemTypes", types);
        map.put("selectId", id);

        return jumpPage(map, LogicConfig.FTL_ITEM_TYPE_SORT, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_TYPE);
    }

    @ResponseBody
    @RequestMapping(value = "/itemTypeSortEditSave", method = RequestMethod.POST)
    public JSONObject itemTypeSortEditSave() {
        return itemManagerService.itemTypeSortEditSave(getUTF("ids"));
    }

    @RequestMapping("/itemUnits")
    public String getItemUnit(ModelMap map) {

        String searchKey = getUTF("searchKey", "");
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        JSONObject unitJson = itemManagerService.searchItemUnitPageByName(searchKey, pageSize, pageIndex);

        if (unitJson.getIntValue("code") != 0) {
            return remoteFail(map, unitJson, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_UNIT);
        }

        JSONArray units = unitJson.getJSONObject("response").getJSONArray("data");

        map.put("itemUnits", units);
        map.put("searchKey", searchKey);
        map.put("pageSize", pageSize);
        map.put("pageIndex", pageIndex);
        map.put("count", unitJson.getJSONObject("response").getIntValue("count"));

        return jumpPage(map, LogicConfig.FTL_ITEM_UNITS, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_UNIT);
    }

    @RequestMapping("/itemUnitEdit")
    public String itemUnitEdit(ModelMap map) {
        long itemUnitId = getLongParameter("id", 0);
        if (itemUnitId != 0) {
            JSONObject unitJson = itemManagerService.getItemUnit(itemUnitId);
            if (unitJson.getIntValue("code") != 0) {
                return remoteFail(map, unitJson, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_UNIT);
            }
            map.put("itemUnit", unitJson.getJSONObject("response"));
        }
        return jumpPage(map, LogicConfig.FTL_ITEM_UNIT_EDIT, LogicConfig.TAB_ITEM, LogicConfig.TAB_ITEM_UNIT);
    }

    @ResponseBody
    @RequestMapping(value = "/itemUnitEditSave", method = RequestMethod.POST)
    public JSONObject itemUnitEditSave() {
        long itemUnitId = getLongParameter("id");
        String title = getUTF("title");
        byte status = getByteParameter("status");
        //通知修改
        return itemManagerService.updateItemUnit(itemUnitId, title, status);
    }

    @ResponseBody
    @RequestMapping("/idNameItems")
    public JSONObject idNameItems() {

        long itemTypeId = getLongParameter("itemTypeId", 0);

        if(itemTypeId == 0) {
            return fail("该分类下没有商品");
        }
        String json = HttpRequest.get(ConfigFactory.getDomainNameConfig().itemRemoteURL + "item/getItemTemplateIdAndNames.do?itemTypeId=" + itemTypeId);
        JSONObject response = parseObject(json);

        if(response.getIntValue("code") == 0) {
            List<ItemIdName> templates = JSONObject.parseArray(response.getJSONObject("response").getString("datas"), ItemIdName.class);
            Map map = new HashMap();
            for (int i = 0; i < templates.size(); i++) {
                ItemIdName template = templates.get(i);
                map.put(template.getId(), template.getName());
            }
            JSONObject result = new JSONObject();
            result.put("datas", map);
            return success(result);
        } else {
            return fail("该分类下没有商品");
        }
    }
}