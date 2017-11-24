package com.xlibao.saas.market.controller.marketmanager;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.market.MarketService;
import com.xlibao.saas.market.service.market.ShelvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhumg on 8/15.
 */
@Controller
@RequestMapping(value = "/market/manager")
public class MarketManagerController {

    @Autowired
    private MarketService marketService;
    @Autowired
    private ShelvesService shelvesService;

    @ResponseBody
    @RequestMapping(value = "searchMarkets")
    public JSONObject searchMarkets() {
        return marketService.searchMarkets();
    }

    @ResponseBody
    @RequestMapping(value = "getMarket")
    public JSONObject getMarket() {
        return marketService.getMarket();
    }

    @ResponseBody
    @RequestMapping(value = "marketEditSave", method = RequestMethod.POST)
    public JSONObject marketEditSave() {
        return marketService.marketEditSave();
    }

    @ResponseBody
    @RequestMapping(value = "marketUpdateStatus", method = RequestMethod.POST)
    public JSONObject marketUpdateStatus() {
        return marketService.marketUpdateStatus();
    }

    @ResponseBody
    @RequestMapping(value = "getAllMarkets")
    public JSONObject getAllMarkets() {
        return marketService.getAllMarkets();
    }

    //获取组(走道)、货架(单元)、层(楼层)的值
    @ResponseBody
    @RequestMapping(value = "getShelvesMarks")
    public JSONObject getShelvesMarks() {
        return shelvesService.getShelvesMarks();
    }

    @ResponseBody
    @RequestMapping(value = "loaderClipDatas")
    public JSONObject loaderClipDatas() {
        return shelvesService.loaderClipDatas();
    }

    /**
     * <pre>
     *     <b>获取原来存在的任务数据</b>
     *
     *     <b>访问地址：</b>http://domainName/market/manager/checkPrepareActionTask.do
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

    @ResponseBody
    @RequestMapping(value = "cancelPrepareActionTask")
    public JSONObject cancelPrepareActionTask() {
        return shelvesService.cancelPrepareActionTask();
    }

    @ResponseBody
    @RequestMapping(value = "prepareAction", method = RequestMethod.POST)
    public JSONObject prepareAction() {
        return shelvesService.prepareAction();
    }

    @ResponseBody
    @RequestMapping(value = "unExecutorTask")
    public JSONObject unExecutorTask() {
        return shelvesService.unExecutorTask();
    }
}