package com.xlibao.saas.market.service.question.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.CommonUtils;
import com.xlibao.common.exception.PlatformErrorCodeEnum;
import com.xlibao.market.data.model.MarketLabel;
import com.xlibao.market.data.model.MarketProblemType;
import com.xlibao.market.data.model.MarketProblems;
import com.xlibao.saas.market.data.DataAccessFactory;
import com.xlibao.saas.market.service.question.LabelTypeEnum;
import com.xlibao.saas.market.service.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chinahuangxc on 2017/9/7.
 */
@Transactional
@Service("questionService")
public class QuestionServiceImpl extends BasicWebService implements QuestionService {

    @Autowired
    private DataAccessFactory dataAccessFactory;

    @Override
    public JSONObject loaderProblemTypes() {
        List<MarketProblemType> problemTypes = dataAccessFactory.getQuestionDataAccessManager().loaderProblemTypes();

        if (CommonUtils.isEmpty(problemTypes)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        JSONArray response = new JSONArray();

        for (MarketProblemType problemType : problemTypes) {
            JSONObject data = new JSONObject();

            data.put("id", problemType.getId());
            data.put("title", problemType.getTitle());

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject findProblems() {
        long problemTypeId = getLongParameter("problemTypeId");
        List<MarketProblems> problemsList = dataAccessFactory.getQuestionDataAccessManager().findProblems(problemTypeId);
        if (CommonUtils.isEmpty(problemsList)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        JSONArray response = new JSONArray();

        for (MarketProblems problems : problemsList) {
            JSONObject data = new JSONObject();

            data.put("id", problems.getId());
            data.put("title", problems.getTitle());
            data.put("question", problems.getQuestion());
            data.put("answer", problems.getAnswer());

            response.add(data);
        }
        return success(response);
    }

    @Override
    public JSONObject loaderRefundLabel() {
        List<MarketLabel> labels = dataAccessFactory.getQuestionDataAccessManager().loaderLabels(LabelTypeEnum.REFUND.getKey());
        if (CommonUtils.isEmpty(labels)) {
            return PlatformErrorCodeEnum.NO_MORE_DATA.response();
        }
        JSONArray response = new JSONArray();

        for(MarketLabel label : labels) {
            JSONObject data = new JSONObject();

            data.put("id", label.getId());
            data.put("title", label.getTitle());

            response.add(data);
        }
        return success(response);
    }
}