package com.xlibao.item.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.item.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/26.
 */
@Controller
@RequestMapping(value = "/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ResponseBody
    @RequestMapping(value = "loaderItemTemplates")
    public JSONObject loaderItemTemplates() {
        return itemService.loaderItemTemplates();
    }

    @ResponseBody
    @RequestMapping(value = "getItemTemplate")
    public JSONObject getItemTemplate() {
        return itemService.getItemTemplate();
    }

    @ResponseBody
    @RequestMapping(value = "getItemTemplateIdAndNames")
    public JSONObject getItemTemplateIdAndNames() {
        return itemService.getItemTemplateIdAndNames();
    }

    @ResponseBody
    @RequestMapping(value = "getItemTemplateByName")
    public JSONObject getItemTemplateByName() {
        return itemService.getItemTemplateByName();
    }

    @ResponseBody
    @RequestMapping(value = "getItemTemplateByBarcode")
    public JSONObject getItemTemplateByBarcode() {
        return itemService.getItemTemplateByBarcode();
    }

    @ResponseBody
    @RequestMapping(value = "relationItemTemplates")
    public JSONObject relationItemTemplates() {
        return itemService.relationItemTemplates();
    }

    @ResponseBody
    @RequestMapping(value = "loaderItemBrands")
    public JSONObject loaderItemBrands() {
        return itemService.loaderItemBrands();
    }

    @ResponseBody
    @RequestMapping(value = "getItemBrand")
    public JSONObject getItemBrand() {
        return itemService.getItemBrand();
    }

    @ResponseBody
    @RequestMapping(value = "loaderItemTypes")
    public JSONObject loaderItemTypes() {
        return itemService.loaderItemTypes();
    }

    @ResponseBody
    @RequestMapping(value = "getItemType")
    public JSONObject getItemType() {
        return itemService.getItemType();
    }

    @ResponseBody
    @RequestMapping(value = "relationItemTypes")
    public JSONObject relationItemTypes() {
        return itemService.relationItemTypes();
    }

    @ResponseBody
    @RequestMapping(value = "loaderItemUnits")
    public JSONObject loaderItemUnits() {
        return itemService.loaderItemUnits();
    }

    @ResponseBody
    @RequestMapping(value = "getItemUnit")
    public JSONObject getItemUnit() {
        return itemService.getItemUnit();
    }

    @ResponseBody
    @RequestMapping(value = "getItemUnitList")
    public JSONObject getItemUnitList() {
        return itemService.getItemUnitList();
    }

    @ResponseBody
    @RequestMapping(value = "getItemTemplateList")
    public JSONObject getItemTemplateList() {
        return itemService.getItemTemplateList();
    }

    /**
     * <pre>
     *     <b>模糊匹配商品模版记录</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "fuzzyMatchItemTemplate")
    public JSONObject fuzzyMatchItemTemplate() {
        return itemService.fuzzyMatchItemTemplate();
    }

    /**
     * update data
     **/
    @ResponseBody
    @RequestMapping(value = "saveItemTemplate", method = RequestMethod.POST)
    public JSONObject saveItemTemplate() {
        return itemService.saveItemTemplate();
    }

    @ResponseBody
    @RequestMapping(value = "saveItemUnit", method = RequestMethod.POST)
    public JSONObject saveItemUnit() {
        return itemService.saveItemUnit();
    }

    @ResponseBody
    @RequestMapping(value = "saveItemType", method = RequestMethod.POST)
    public JSONObject saveItemType() {
        return itemService.saveItemType();
    }

    @ResponseBody
    @RequestMapping(value = "saveItemBrand", method = RequestMethod.POST)
    public JSONObject saveItemBrand() {
        return itemService.saveItemBrand();
    }

    @ResponseBody
    @RequestMapping(value = "saveItemRelationship", method = RequestMethod.POST)
    public JSONObject saveItemRelationship() {
        return itemService.saveItemRelationship();
    }

    /**
     * zhumg
     */
    @ResponseBody
    @RequestMapping(value = "searchItemUnitPageByName")
    public JSONObject searchItemUnitPageByName() {
        return itemService.searchItemUnitPageByName();
    }

    @ResponseBody
    @RequestMapping(value = "searchItemTypePageByName")
    public JSONObject searchItemTypePageByName() {
        return itemService.searchItemTypePageByName();
    }

    @ResponseBody
    @RequestMapping(value = "searchItemTypePage")
    public JSONObject searchItemTypePage() {
        return itemService.searchItemTypePage();
    }

    @ResponseBody
    @RequestMapping(value = "searchItemTemplatesPage")
    public JSONObject searchItemTemplatesPage() {
        return itemService.searchItemTemplatesPage();
    }

    @ResponseBody
    @RequestMapping(value = "/updateItemTypesSort", method = RequestMethod.POST)
    public JSONObject updateItemTypesSort() {
        return itemService.updateItemTypesSort();
    }



    @ResponseBody
    @RequestMapping(value = "/itemEditSave", method = RequestMethod.POST)
    public JSONObject itemEditSave() {
        return itemService.itemEditSave();
    }

    @ResponseBody
    @RequestMapping("/itemUpdateImgUrl")
    public JSONObject itemUpdateImgUrl() {
        return itemService.itemUpdateImgUrl();
    }


    @ResponseBody
    @RequestMapping(value = "/itemTypeEditSave", method = RequestMethod.POST)
    public JSONObject itemTypeEditSave() {
        return itemService.itemTypeEditSave();
    }

    @ResponseBody
    @RequestMapping(value = "/itemTypeUpdateIconUrl", method = RequestMethod.POST)
    public JSONObject itemTypeUpdateIconUrl() {
        return itemService.itemTypeUpdateIconUrl();
    }
}