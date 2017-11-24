package com.xlibao.saas.market.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.market.MarketService;
import com.xlibao.saas.market.service.market.ShelvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/market")
public class MarketController {

    @Autowired
    private MarketService marketService;
    @Autowired
    private ShelvesService shelvesService;

    /***
     * <pre>
     *     <b>找到商店</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/findMarket.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "findMarket")
    public JSONObject findMarket() {
        return marketService.findMarket();
    }

    /**
     * <pre>
     *     <b>展示可用的商店</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showMarkets")
    public JSONObject showMarkets() {
        return marketService.showMarkets();
    }

    /**
     * <pre>
     *     <b>过滤商店(寻找商店)</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "filterMarket")
    public JSONObject filterMarket() {
        return marketService.filterMarket();
    }

    /**
     * <pre>
     *     <b>商店状态报告</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "marketResponse")
    public JSONObject marketResponse() {
        return marketService.marketResponse();
    }

    /**
     * <pre>
     *     <b>更新商店状态</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "marketUpdateStatus")
    public JSONObject marketUpdateStatus() {
        return marketService.marketUpdateStatus();
    }

    /**
     * <pre>
     *     <b>获取组(走道)、货架(单元)、层(楼层)的值</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/getShelvesMarks.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>groupCode</b> - String 组编码，当 <b>shelvesType</b>为{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum#GROUP}或{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum#UNIT}时必填
     *          <b>unitCode</b> - String 单元编码，当 <b>shelvesType</b>为{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum#UNIT}时必填
     *          <b>shelvesType</b> - int 获取的货架类型，具体参考：{@link com.xlibao.saas.market.service.market.ShelvesTypeEnum}
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 编码组，每个元素为String，即对应请求的数据集合
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "getShelvesMarks")
    public JSONObject getShelvesMarks() {
        return shelvesService.getShelvesMarks();
    }

    /**
     * <pre>
     *     <b>加载弹夹的数据</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/loaderClipDatas.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>groupCode</b> - String 组编码，必填参数。
     *          <b>unitCode</b> - String 单元编码，必填参数。
     *          <b>floorCode</b> - String 楼层编码，必填参数。
     *          <b>pageIndex</b> - int 页码，非必填参数；默认值为：{@link com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_INDEX}
     *          <b>pageSize</b> - int 单页数量，非必填参数；默认值为：{@link com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_SIZE}
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 弹夹数据集合，每个元素为JSONObject结构
     *              <b>locationCode</b> - String 弹夹的完整编码，如：01010101
     *              <b>itemTemplateId</b> - long 商品模版ID
     *              <b>itemName</b> - String 当前存放的商品名称
     *              <b>itemQuantity</b> - int 当前的商品库存
     *              <b>barcode</b> - String 当前存放的商品条码
     *              <b>unitName</b> - String 当前存放的商品单位
     *
     *              <b>taskId</b> - long 任务ID；当没有存在预操作任务时为0，否则为对应的任务ID
     *              <b>taskStatus</b> - int 任务状态，当<b>taskId</b>为0时无效；具体参考：{@link com.xlibao.saas.market.service.item.PrepareActionStatusEnum}
     *              <b>hasCompleteQuantity</b> - int 已完成的数量，当<b>taskId</b>为0时无效
     *              <b>hopeItemQuantity</b> - int 期望执行的数量，当<b>taskId</b>为0时无效
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "loaderClipDatas")
    public JSONObject loaderClipDatas() {
        return shelvesService.loaderClipDatas();
    }

    /**
     * <pre>
     *     <b>获取原来存在的任务数据</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/checkPrepareActionTask.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>taskId</b> - long 任务ID，必填参数；该参数必定大于0
     *
     *     <b>返回：</b>
     *          <b>taskId</b> - long 任务ID
     *          <b>locationCode</b> - String 弹夹的完整编码，如：01010101
     *          <b>itemTemplateId</b> - long 商品模版ID
     *          <b>itemName</b> - String 商品名
     *          <b>unitName</b> - String 当前存放的商品单位
     *          <b>hasCompleteQuantity</b> - int 执行完成的商品数量
     *          <b>itemQuantity</b> - int 商品数量
     *          <b>barcode</b> - String 商品的条码
     *          <b>type</b> - int 任务类型，参考：{@link com.xlibao.saas.market.service.market.ShelvesTaskTypeEnum}
     *          <b>status</b> - int 任务状态，参考：{@link com.xlibao.saas.market.service.item.PrepareActionStatusEnum}
     *          <b>hopeExecutorDate</b> - String 期望执行的日期
     *          <b>completeTime</b> - String 实际完成的时间点
     *          <b>dateTitle</b> - String 用于展示的执行日期格式标题，如：今天、昨天、2017-09-18 周一
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "checkPrepareActionTask")
    public JSONObject checkPrepareActionTask() {
        return shelvesService.checkPrepareActionTask();
    }

    /**
     * <pre>
     *     <b>取消任务</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/checkPrepareActionTask.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>taskId</b> - long 任务ID，必填参数；该参数必定大于0
     *
     *     <b>返回：</b>成功或失败的描述
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "cancelPrepareActionTask")
    public JSONObject cancelPrepareActionTask() {
        return shelvesService.cancelPrepareActionTask();
    }

    /**
     * <pre>
     *     <b>新增预操作行为</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/prepareAction.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 本次操作者的通行证ID，必填参数。
     *          <b>marketId</b> - long 需要进行补货的商店ID，必填参数。
     *          <b>actionDatas</b> - String 本次需要补货的数据，必填参数；结构为JSONArray，每个元素为JSONObject
     *                              <b>location</b> - String 位置信息编码
     *                              <b>itemTemplateId</b> - long 商品模版ID
     *                              <b>quantity</b> - int 期望存放的数量
     *          <b>hopeExecutorDate</b> - String 期望执行的日期，非必填参数；结构为：yyyy-MM-dd 00:00:00，不提供时，默认值为当天
     *
     *     <b>返回：</b>执行成功或失败的提示
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "prepareAction")
    public JSONObject prepareAction() {
        return shelvesService.prepareAction();
    }

    /**
     * <pre>
     *     <b>扫描商品获取上架任务</b>
     *     <b>注意：</b>已按上架数量进行排序：从少到多
     *
     *     <b>访问地址：</b>http://domainName/market/open/scanShelvesTask.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>barcode</b> - String 商品或货架编码，必填参数；一般为扫码获取。
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 当前的任务列表，每个元素为JSONObject，结构为：
     *              <b>taskId</b> - long 任务ID
     *              <b>locationCode</b> - String 弹夹的完整编码，如：01010101
     *              <b>itemTemplateId</b> - long 商品模版ID
     *              <b>itemName</b> - String 商品名
     *              <b>unitName</b> - String 当前存放的商品单位
     *              <b>hasCompleteQuantity</b> - int 执行完成的商品数量
     *              <b>itemQuantity</b> - int 商品数量
     *              <b>barcode</b> - String 商品的条码
     *              <b>type</b> - int 任务类型，参考：{@link com.xlibao.saas.market.service.market.ShelvesTaskTypeEnum}
     *              <b>status</b> - int 任务状态，参考：{@link com.xlibao.saas.market.service.item.PrepareActionStatusEnum}
     *              <b>hopeExecutorDate</b> - String 期望执行的日期
     *              <b>completeTime</b> - String 实际完成的时间点
     *              <b>dateTitle</b> - String 用于展示的执行日期格式标题，如：今天、昨天、2017-09-18 周一
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "scanShelvesTask")
    public JSONObject scanShelvesTask() {
        return shelvesService.scanShelvesTask();
    }

    /**
     * <pre>
     *     <b>未完成的任务列表</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/unExecutorTask.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>pageIndex</b> - int 页码，非必填参数；默认值为：{@link com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_INDEX}
     *          <b>pageSize</b> - int 单页数量，非必填参数；默认值为：{@link com.xlibao.common.GlobalConstantConfig#DEFAULT_PAGE_SIZE}
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 任务集合，每个元素为JSONObject结构：
     *              <b>taskId</b> - long 任务ID
     *              <b>locationCode</b> - String 弹夹的完整编码，如：01010101
     *              <b>itemTemplateId</b> - long 商品模版ID
     *              <b>itemName</b> - String 商品名
     *              <b>unitName</b> - String 当前存放的商品单位
     *              <b>hasCompleteQuantity</b> - int 执行完成的商品数量
     *              <b>itemQuantity</b> - int 商品数量
     *              <b>barcode</b> - String 商品的条码
     *              <b>type</b> - int 任务类型，参考：{@link com.xlibao.saas.market.service.market.ShelvesTaskTypeEnum}
     *              <b>status</b> - int 任务状态，参考：{@link com.xlibao.saas.market.service.item.PrepareActionStatusEnum}
     *              <b>hopeExecutorDate</b> - String 期望执行的日期
     *              <b>completeTime</b> - String 实际完成的时间点
     *              <b>dateTitle</b> - String 用于展示的执行日期格式标题，如：今天、昨天、2017-09-18 周一
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "unExecutorTask")
    public JSONObject unExecutorTask() {
        return shelvesService.unExecutorTask();
    }

    /**
     * <pre>
     *     <b>我关注的商店</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/myFocusMarkets.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "myFocusMarkets")
    public JSONObject myFocusMarkets() {
        return marketService.myFocusMarkets();
    }

    /**
     * <pre>
     *     <b>展示货架任务</b>
     *
     *     <b>访问地址：</b>http://domainName/market/open/showShelvesTask.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "showShelvesTask")
    public JSONObject showShelvesTask() {
        return shelvesService.showShelvesTask();
    }

    @ResponseBody
    @RequestMapping(value = "findValidTasks")
    public JSONObject findValidTasks() {
        return shelvesService.findValidTasks();
    }

    /**
     * <pre>
     *     <b>完成今天的所有预操作任务</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "finishTodayPrepareActions")
    public JSONObject finishTodayPrepareActions() {
        return shelvesService.finishTodayPrepareActions();
    }
}