package com.xlibao.saas.market.service.question;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/9/7.
 */
public interface QuestionService {

    JSONObject loaderProblemTypes();

    JSONObject findProblems();

    JSONObject loaderRefundLabel();
}