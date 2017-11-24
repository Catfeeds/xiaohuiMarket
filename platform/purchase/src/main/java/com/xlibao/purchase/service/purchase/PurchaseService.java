package com.xlibao.purchase.service.purchase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by admin on 2017/8/28.
 */
public interface PurchaseService {
    /**
     * 供应商列表查询
     * @return
     */
    JSONObject searchSupplierPage();

    /**
     * 添加供应商
     * @return
     */
    JSONObject saveSupplier();

    /**
     * 供应商集合
     * @return
     */
    JSONObject getAllSupplier();

    /**
     * 修改供应商信息
     * @return
     */
    JSONObject updateSupplier();
    /**
     * 停用启用供应商
     * @return
     */
    JSONObject updateSupplierStatus();

    /**
     * 获取供应商详情
     * @return
     */
    JSONObject getSupplier();

    /**
     * 仓库表查询
     * @return
     */
    JSONObject searchWarehousePage();

    /**
     * 添加仓库
     * @return
     */
    JSONObject saveWarehouse();

    /**
     * 仓库集合
     * @return
     */
    JSONObject getAllWarehouse();

    /**
     * 修改仓库信息
     * @return
     */
    JSONObject updateWarehouse();
    /**
     * 停用启用仓库
     * @return
     */
    JSONObject updateWarehouseStatus();

    /**
     * 获取仓库详情
     * @return
     */
    JSONObject getWarehouse();


    /**
     * 添加仓管员
     * @return
     */
    JSONObject saveWarehouseUser();

    /**
     * 查看仓管员信息
     * @return
     */
    JSONObject getWarehouseUsers();
    /**
     * 初始化仓管员密码
     * @return
     */
    JSONObject updateWarehouseUserPassword();

    /**
     * 删除仓库管理员
     * @return
     */
    JSONObject deleteWarehouseUser();

    /**
     * 采购单列表
     * @return
     */
    JSONObject searchPurchasePage();

    /**
     * 采购单
     * @return
     */
    JSONObject getPurchase();

    /**
     * 采购单list数据
     * @return
     */
    JSONObject getPurchaseCommodityS();

    /**
     * 供应商产品订单
     * @return
     */
    JSONObject getSupplierCommodityS();

    /**
     * 删除采购单
     * @return
     */
    JSONObject deletePurchase();

    /**
     * 添加采购单
     * @return
     */
    JSONObject savePurchase();

    /**
     * 采购物品
     * @return
     */
    JSONObject savePurchaseCommodity();

    /**
     * 更新采购单
     * @return
     */
    JSONObject updatePurchase();

    /**
     *删除采购单物品
     * @return
     */
    JSONObject delPurchaseCommodity();

    /***
     * 入库
     * @return
     */
    JSONObject purchasePutIn();

    /**
     * 商品库存列表
     * @return
     */
    JSONObject searchCommodityStoresPage();

    /**
     * 修改预警库存数
     * @return
     */
    JSONObject updateCommodityStores();

    /**
     * 更新商品库存
     * @return
     */
    JSONObject updateStock();
}
