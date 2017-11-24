package com.xlibao.purchase.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.purchase.service.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by admin on 2017/8/29.
 */
@Controller
@RequestMapping("/purchaseAPP")
public class PurchaseAppController {

    @Autowired
    private PurchaseService purchaseService;


    /**
     * 采购单查询
     * @return
     */
    @ResponseBody
    @RequestMapping("searchPurchasePage")
    public JSONObject searchPurchasePage(){
        return purchaseService.searchPurchasePage();
    }
    /**
     * 采购单查询
     * @return
     */
    @ResponseBody
    @RequestMapping("getPurchase")
    public JSONObject getPurchase(){
        return purchaseService.getPurchase();
    }

    /**
     * 采购单list数据
     * @return
     */
    @ResponseBody
    @RequestMapping("getPurchaseCommodityS")
    public JSONObject getPurchaseCommodityS(){
        return purchaseService.getPurchaseCommodityS();
    }



    /**
     * 入库
     * @return
     */
    @ResponseBody
    @RequestMapping("purchasePutIn")
    public JSONObject purchasePutIn(){
        return purchaseService.purchasePutIn();
    }




}
