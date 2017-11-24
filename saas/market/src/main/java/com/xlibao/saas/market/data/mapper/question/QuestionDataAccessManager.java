package com.xlibao.saas.market.data.mapper.question;

import com.xlibao.market.data.model.MarketLabel;
import com.xlibao.market.data.model.MarketProblemType;
import com.xlibao.market.data.model.MarketProblems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chinahuangxc on 2017/9/7.
 */
@Component
public class QuestionDataAccessManager {

    @Autowired
    private MarketProblemTypeMapper problemTypeMapper;
    @Autowired
    private MarketProblemsMapper problemsMapper;
    @Autowired
    private MarketLabelMapper labelMapper;

    public List<MarketProblemType> loaderProblemTypes() {
        return problemTypeMapper.loaderProblemTypes();
    }

    public List<MarketProblems> findProblems(long problemTypeId) {
        return problemsMapper.findProblems(problemTypeId);
    }

    public List<MarketLabel> loaderLabels(int type) {
        return labelMapper.loaderLabels(type);
    }
}