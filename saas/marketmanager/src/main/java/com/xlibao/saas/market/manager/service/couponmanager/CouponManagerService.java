package com.xlibao.saas.market.manager.service.couponmanager;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by user on 2017/10/16.
 */
public interface CouponManagerService {

    /**
     * 优惠券列表
     * @return
     */
    JSONObject searchActivePage();

    /**
     * 优惠券使用者
     * @return
     */
    JSONObject getActiveRecords();


    /**
     * 修改优惠券
     * @return
     */
    JSONObject saveActiveRule();

    /**
     * 修改优惠券规则
     * @return
     */
    JSONObject updateActiveRule();


    /**
     * 获取优惠券规则
     * @return
     */
    JSONObject getActiveRule();

    /**
     * 删除优惠券
     * @return
     */
    JSONObject delActive();
}
