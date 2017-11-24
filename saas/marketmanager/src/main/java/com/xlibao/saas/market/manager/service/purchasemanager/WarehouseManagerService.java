package com.xlibao.saas.market.manager.service.purchasemanager;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by user on 2017/9/20.
 */
public interface WarehouseManagerService {

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

    /***
     * 商品库存列表
     * @return
     */
    JSONObject searchCommodityStoresPage();

    /**
     * 修改预警库存数量
     * @return
     */
    JSONObject updateCommodityStores();
}
