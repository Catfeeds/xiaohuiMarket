package com.xlibao.saas.market.controller;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.item.ItemService;
import com.xlibao.saas.market.service.market.ShelvesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/market/item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ShelvesService shelvesService;

    /**
     * <pre>
     *     <b>首页数据</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "homepage")
    public JSONObject homepage() {
        return itemService.homepage();
    }

    @ResponseBody
    @RequestMapping(value = "itemTypes")
    public JSONObject itemTypes() {
        return itemService.itemTypes();
    }

    @ResponseBody
    @RequestMapping(value = "findSubItemTypes")
    public JSONObject findSubItemTypes() {
        return itemService.findSubItemTypes();
    }

    @ResponseBody
    @RequestMapping(value = "findRecommendItems")
    public JSONObject findRecommendItems() {
        return itemService.findRecommendItems();
    }

    /**
     * <pre>
     *     <b>商品页面数据</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/pageItems.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>passportId</b> - long 查看者通行证ID，非必填参数；当匿名访问时，可为0(默认值)。
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>itemTypeId</b> - long 指定的分类ID，非必填参数；当查看全部商品时，可为0(默认值)。
     *          <b>sortType</b> - int 排序类型，非必填参数；具体参考：{@link com.xlibao.saas.market.service.item.ItemSortEnum}，默认为：{@link com.xlibao.saas.market.service.item.ItemSortEnum#DEFAULT}
     *          <b>sortValue</b> - int 排序值，非必填参数；0 -- 升序  1 -- 降序 默认为0。
     *          <b>searchKeyValue</b> - String 搜索关键字，非必填参数；不提供时表示不属于搜索行为，该参数的优先级最高，即存在搜索关键字时，那么指定类型失效。
     *
     *     <b>返回：</b>
     *          <b>pageItemMsg</b> - JSONArray 用于展示在页面上的数据，每个元素为JSONObject结构
     *              <b>itemId</b> - long 商品ID
     *              <b>itemTemplateId</b> - long 商品模版ID
     *              <b>name</b> - String 商品名字
     *              <b>imageUrl</b> - String 图片
     *              <b>unitName</b> - String 单位
     *              <b>stock</b> - int 当前库存
     *              <b>maximumSellCount</b> - int 最大购买数量限制
     *              <b>minimumSellCount</b> - int 最小购买数量限制
     *              <b>barcode</b> - String 条码
     *              <b>batchesCode</b> - String 批次编号
     *              <b>sellPrice</b> - long 销售价格
     *              <b>maxPrice</b> - long 最高价格(划线价格)
     *              <b>discountType</b> - int 优惠类型，具体参考：{@link com.xlibao.saas.market.service.item.DiscountTypeEnum}
     *              <b>discountPrice</b> - long 优惠价
     *              <b>restrictionQuantity</b> - int 限购数量；特殊值：-1时表示不限购，0时表示不出售；不可能出现其他小于0的值，其他任何大于0的值即表示限购的数量
     *              <b>actualSales</b> - int 实际销量
     *              <b>deliveryDelay</b> - int 延迟发货天数
     *              <b>status</b> - int 当前状态，如：下架、上架等；目前仅存在上架状态，前端可忽略该参数
     *              <b>itemLadderPrices</b> - JSONArray 商品的阶梯价，每个元素为JSONObject结构；空内容时表示没有阶梯价
     *                  <b>minQuantity</b> - int 阶梯价的最低起购数量(购买数量必须至少达到该数量)
     *                  <b>maxQuantity</b> - int 阶梯价的最高起购数量(当超过该数量时，必须检查是否有对应的阶梯存在，否则停留在这个阶段)
     *                  <b>expectPrice</b> - long 期望售价(单位：分)
     *                  <b>mark</b> - String 运营人员设定的描述内容
     *                  <b>showMsg</b> - String 展示给用户看到的阶梯价信息
     *
     *          <b>buyMessage</b> - JSONObject 用户的购买信息，用于控制限量和促销的数据内容；其中 key为商品的物理ID，value为JSONObject数据结构：
     *              <b>showDiscount</b> - 前端用于展示的信息，如：无限购、每天限购10件等
     *              <b>showHasBuy</b> - 用于展示给用户的当前已购买信息，如：您已购买9件
     *              <b>hasBuy</b> - int 用于逻辑计算，由于用户的每次选购都需要展示可能需要支付的价格，且计算逻辑由前端来执行；该数据用于在限购时参与逻辑处理
     *              <b>beyondControl</b> - int 超过限购时的处理方式，具体参考：{@link com.xlibao.saas.market.service.item.BeyondControllTypeEnum}
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "pageItems")
    public JSONObject pageItems() {
        return itemService.pageItems();
    }

    /**
     * <pre>
     *     <b>商品分组</b> -- 归类每个店的商品集合
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/splitItems.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>items</b> - String 分组商品ID，必填参数；格式为：itemId,itemId,itemId 如：100000,100001,100002。
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 每个元素都为一个JSONObject，结构：
     *              <b>marketId</b> - long 仓库ID
     *              <b>marketName</b> - String 仓库名字
     *              <b>items</b> - JSONArray 商品ID
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "splitItems")
    public JSONObject splitItems() {
        return itemService.splitItems();
    }

    /**
     * <pre>
     *     <b>下架商品</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/offShelves.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "offShelves")
    public JSONObject offShelves() {
        return shelvesService.offShelves();
    }

    /**
     * <pre>
     *     <b>上架商品</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/onShelves.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "onShelves")
    public JSONObject onShelves() {
        return shelvesService.onShelves();
    }

    /**
     * <pre>
     *     <b>是否存在商品</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/existItemTemplate.do
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
        return itemService.existItemTemplate();
    }

    /**
     * <pre>
     *     <b>编辑商品</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "editItem")
    public JSONObject editItem() {
        return itemService.editItem();
    }

    /**
     * <pre>
     *     <b>找到商品的位置</b>
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "findItemLocation")
    public JSONObject findItemLocation() {
        return itemService.findItemLocation();
    }

    /**
     * <pre>
     *     <b>加载热门搜索标签</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/loaderHotSearch.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>pageIndex</b> - int 页码，非必填参数。
     *          <b>pageSize</b> - int 每页显示数量，非必填参数。
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 每个元素为String，即：当前热门的查询内容
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "loaderHotSearch")
    public JSONObject loaderHotSearch() {
        return itemService.loaderHotSearch();
    }

    /**
     * <pre>
     *     <b>模糊匹配完整的商品名字</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/fuzzyMatchItemName.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>marketId</b> - long 商店ID，必填参数。
     *          <b>fuzzyItemName</b> - String 本次查询的关键字，必填参数。
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 完整的商品名字集合，每个元素为String，即商品名字
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "fuzzyMatchItemName")
    public JSONObject fuzzyMatchItemName() {
        return itemService.fuzzyMatchItemName();
    }

    /**
     *  <pre>
     *      <b>获取商品阶梯价记录</b>
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/itemLadderPrices.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>itemId</b> - long 商品ID，必填参数。
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 阶梯价的具体数据，结构为：JSONObject
     *              <b>id</b> - long 阶梯价ID
     *              <b>minQuantity</b> - int 最低购买数量
     *              <b>maxQuantity</b> - int 最高购买数量，当存在多个阶梯时，该值有效；但处于最后一级时，该值无效
     *              <b>expectPrice</b> - long 期望售价，单位：分
     *              <b>status</b> - int 当前状态，{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_TRUE} 有效；{@link com.xlibao.common.GlobalAppointmentOptEnum#LOGIC_FALSE} 无效
     *              <b>mark</b> - String 描述
     *  </pre>
     */
    @ResponseBody
    @RequestMapping(value = "itemLadderPrices")
    public JSONObject itemLadderPrices() {
        return itemService.itemLadderPrices();
    }

    /**
     * <pre>
     *     <b>建立阶梯价记录</b>
     *          <b>注意：</b>目前的结构仅支持存在一个配置
     *
     *     <b>访问地址：</b>http://domainName/market/item/openapi/createItemLadderPrice.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>itemId</b> - long 商品ID，必填参数。
     *          <b>minQuantity</b> - int 最低购买数量
     *          <b>maxQuantity</b> - int 最高购买数量，当存在多个阶梯时，该值有效；但处于最后一级时，该值无效
     *          <b>expectPrice</b> - long 期望售价，单位：分
     *          <b>mark</b> - String 描述
     *
     *     <b>返回：</b>仅提示成功或失败原因
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "createItemLadderPrice")
    public JSONObject createItemLadderPrice() {
        return itemService.createItemLadderPrice();
    }

    /**
     * <pre>
     *     <b>移除阶梯价配置</b>
     *     <b>访问地址：</b>http://domainName/market/item/openapi/removeItemLadderPrice.do
     *     <b>访问方式：</b>GET/POST；推荐使用POST
     *
     *     <b>参数：</b>
     *          <b>itemId</b> - long 商品ID，必填参数。
     *          <b>itemLadderId</b> - long 阶梯ID，必填参数。
     *
     *     <b>返回：</b>仅提示成功或失败原因
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "removeItemLadderPrice")
    public JSONObject removeItemLadderPrice() {
        return itemService.removeItemLadderPrice();
    }
}