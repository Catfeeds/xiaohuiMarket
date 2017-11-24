package com.xlibao.saas.market.manager.service.purchasemanager;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by user on 2017/9/22.
 */
public interface PurchaseManagerService {
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
     * 供应商产品list数据
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
    JSONObject savePurchaseCommodity(Map<String, String> parameters);

    /**
     * 更新采购单
     * @return
     */
    JSONObject updatePurchase();

    /**
     *删除采购单物品
     * @return
     */
    JSONObject delPurchaseCommodity(long purchaseID);
}
