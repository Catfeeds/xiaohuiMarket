package com.xlibao.saas.market.controller.marketmanager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.http.HttpRequest;
import com.xlibao.saas.market.service.manager.MarketItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/27 0027.
 */
@Controller
@RequestMapping(value = "/market/item/manager")
public class ItemManagerController {

    @Autowired
    private MarketItemService marketItemService;

    @ResponseBody
    @RequestMapping(value = "searchMarketItems")
    public JSONObject searchMarketItems() {
        return marketItemService.searchMarketItems();
    }

    @ResponseBody
    @RequestMapping(value = "getMarketItem")
    public JSONObject getMarketItem() {
        return marketItemService.getMarketItem();
    }

    @ResponseBody
    @RequestMapping(value = "marketItemEditSave", method = RequestMethod.POST)
    public JSONObject marketItemEditSave() {
        return marketItemService.marketItemEditSave();
    }
    @ResponseBody
    @RequestMapping(value = "marketItemAddSave", method = RequestMethod.POST)
    public JSONObject marketItemAddSave() {
        return marketItemService.marketItemAddSave();
    }



    //商店 编辑 保存
    @ResponseBody
    @RequestMapping("/marketItemUpdateStatus")
    public JSONObject marketItemUpdateStatus() {
        return marketItemService.marketItemUpdateStatus();
    }


    /**
     * <pre>
     *     <b>是否存在商品</b>
     *
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>itemTemplateId</b> - long 商品模版ID，必填参数。
     *
     *     <b>返回：</b>仅返回成功或失败的提示
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "existItemTemplate")
    public JSONObject existItemTemplate() {
        return marketItemService.existItemTemplate();
    }
}
