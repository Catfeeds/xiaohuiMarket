package com.xlibao.saas.market.data.mapper.question;

import com.xlibao.market.data.model.MarketProblems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketProblemsMapper {

    List<MarketProblems> findProblems(@Param("problemTypeId") long problemTypeId);
}