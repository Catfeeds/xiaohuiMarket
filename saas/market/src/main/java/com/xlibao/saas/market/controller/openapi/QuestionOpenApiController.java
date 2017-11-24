package com.xlibao.saas.market.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.saas.market.service.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chinahuangxc on 2017/9/7.
 */
@Controller
@RequestMapping(value = "/market/question/openapi")
public class QuestionOpenApiController {

    @Autowired
    private QuestionService questionService;

    /**
     * <pre>
     *     <b>加载问题类目</b>
     *
     *     <b>访问地址：</b>http://domainName/market/question/openapi/loaderProblemTypes.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>无需请求参数
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 问题分类列表，每个元素为JSONObject结构
     *              <b>id</b> - long 分类ID
     *              <b>title</b> - String 问题类目标题
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "loaderProblemTypes")
    public JSONObject loaderProblemTypes() {
        return questionService.loaderProblemTypes();
    }

    /**
     * <pre>
     *     <b>找到某个类目下的所有问题</b>
     *
     *     <b>访问地址：</b>http://domainName/market/question/openapi/findProblems.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>无需请求参数
     *          <b>problemTypeId</b> - long 问题类目ID，必填参数。
     *
     *     <b>返回：</b>JSONArray 具体问题列表，每个元素为JSONObject结构
     *          <b>id</b> - long 问题ID
     *          <b>title</b> - String 标题
     *          <b>question</b> - String 问题
     *          <b>answer</b> - String 答案
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "findProblems")
    public JSONObject findProblems() {
        return questionService.findProblems();
    }

    /**
     * <pre>
     *     <b>加载退款标签</b>
     *
     *     <b>访问地址：</b>http://domainName/market/question/openapi/loaderRefundLabel.do
     *     <b>访问方式：</b>GET/POST 推荐使用POST
     *
     *     <b>参数：</b>无需请求参数
     *
     *     <b>返回：</b>
     *          <b>datas</b> - JSONArray 问题分类列表，每个元素为JSONObject结构
     *              <b>id</b> - long 分类ID
     *              <b>title</b> - String 问题类目标题
     * </pre>
     */
    @ResponseBody
    @RequestMapping(value = "loaderRefundLabel")
    public JSONObject loaderRefundLabel() {
        return questionService.loaderRefundLabel();
    }
}