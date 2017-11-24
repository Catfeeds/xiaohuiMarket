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
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    /**
     * 供应商列表查询
     * @return
     */
    @ResponseBody
    @RequestMapping("searchSupplierPage")
    public JSONObject searchSupplierPage(){
        return purchaseService.searchSupplierPage();
    }

    /**
     * 供应商集合
     * @return
     */
    @ResponseBody
    @RequestMapping("getAllSupplier")
    public JSONObject getAllSupplier(){
        return purchaseService.getAllSupplier();
    }



    /**
     * 添加供应商
     * @return
     */
    @ResponseBody
    @RequestMapping("saveSupplier")
    public JSONObject saveSupplier(){
        return purchaseService.saveSupplier();
    }
    /**
     * 修改供应商
     * @return
     */
    @ResponseBody
    @RequestMapping("updateSupplier")
    public JSONObject updateSupplier(){
        return purchaseService.updateSupplier();
    }


    /**
     * 停用启动供应商
     * @return
     */
    @ResponseBody
    @RequestMapping("updateSupplierStatus")
    public JSONObject updateSupplierStatus(){
        return purchaseService.updateSupplierStatus();
    }

    /**
     * 获取供应商详情
     * @return
     */
    @ResponseBody
    @RequestMapping("getSupplier")
    public JSONObject getSupplier(){
        return purchaseService.getSupplier();
    }

    /**
     * 仓库表查询
     * @return
     */
    @ResponseBody
    @RequestMapping("searchWarehousePage")
    public JSONObject searchWarehousePage(){
        return purchaseService.searchWarehousePage();
    }

    /**
     * 添加仓库
     * @return
     */
    @ResponseBody
    @RequestMapping("saveWarehouse")
    public JSONObject saveWarehouse(){
        return purchaseService.saveWarehouse();
    }

    /**
     * 仓库集合
     * @return
     */
    @ResponseBody
    @RequestMapping("getAllWarehouse")
    public JSONObject getAllWarehouse(){
        return purchaseService.getAllWarehouse();
    }

    /**
     * 修改仓库信息
     * @return
     */
    @ResponseBody
    @RequestMapping("updateWarehouse")
    public JSONObject updateWarehouse(){
        return purchaseService.updateWarehouse();
    }
    /**
     * 停用启用仓库
     * @return
     */
    @ResponseBody
    @RequestMapping("updateWarehouseStatus")
    public JSONObject updateWarehouseStatus(){
        return purchaseService.updateWarehouseStatus();
    }

    /**
     * 获取仓库详情
     * @return
     */
    @ResponseBody
    @RequestMapping("getWarehouse")
    public JSONObject getWarehouse(){
        return purchaseService.getWarehouse();
    }


    /**
     * 添加仓管员
     * @return
     */
    @ResponseBody
    @RequestMapping("saveWarehouseUser")
    public JSONObject saveWarehouseUser(){
        return purchaseService.saveWarehouseUser();
    }

    /**
     * 查看仓管员信息
     * @return
     */
    @ResponseBody
    @RequestMapping("getWarehouseUsers")
    public JSONObject getWarehouseUsers() {
        return purchaseService.getWarehouseUsers();
    }
    /**
     * 初始化仓管员密码
     * @return
     */
    @ResponseBody
    @RequestMapping("updateWarehouseUserPwd")
    public JSONObject updateWarehouseUserPwd() {
        return purchaseService.updateWarehouseUserPassword();
    }

    /**
     * 删除仓库管理员
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteWarehouseUser")
    public JSONObject deleteWarehouseUser() {
        return purchaseService.deleteWarehouseUser();
    }
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
     * 供应商产品list数据
     * @return
     */
    @ResponseBody
    @RequestMapping("getSupplierCommodityS")
    public JSONObject getSupplierCommodityS(){
        return purchaseService.getSupplierCommodityS();
    }

    /**
     * 采购单删除
     * @return
     */
    @ResponseBody
    @RequestMapping("deletePurchase")
    public JSONObject deletePurchase(){
        return purchaseService.deletePurchase();
    }
    /**
     * 添加采购单
     * @return
     */
    @ResponseBody
    @RequestMapping("savePurchase")
    public JSONObject savePurchase(){
        return purchaseService.savePurchase();
    }
    /**
     * 添加采购单物品
     * @return
     */
    @ResponseBody
    @RequestMapping("savePurchaseCommodity")
    public JSONObject savePurchaseCommodity(){
        return purchaseService.savePurchaseCommodity();
    }
    /**
     * 修改采购单
     * @return
     */
    @ResponseBody
    @RequestMapping("updatePurchase")
    public JSONObject updatePurchase(){
        return purchaseService.updatePurchase();
    }
    /**
     * 删除采购单物品
     * @return
     */
    @ResponseBody
    @RequestMapping("delPurchaseCommodity")
    public JSONObject delPurchaseCommodity(){
        return purchaseService.delPurchaseCommodity();
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


    /**
     * 商品库存列表查询
     * @return
     */
    @ResponseBody
    @RequestMapping("searchCommodityStoresPage")
    public JSONObject searchcommodityStoresPage(){
        return purchaseService.searchCommodityStoresPage();
    }

    /**
     * 设置商品库存预警数
     * @return
     */
    @ResponseBody
    @RequestMapping("updateCommodityStores")
    public JSONObject updateCommodityStores(){
        return purchaseService.updateCommodityStores();
    }


    /**
     * 商品出库更新库存
     * @return
     */
    @ResponseBody
    @RequestMapping("updateStock")
    public JSONObject updateStock(){
        return purchaseService.updateStock();
    }

}
