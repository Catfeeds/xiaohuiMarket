package com.xlibao.saas.market.manager.service.couponmanager.impl;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoIllegalArgumentException;
import com.xlibao.common.support.BasicRemoteService;
import com.xlibao.saas.market.manager.config.ConfigFactory;
import com.xlibao.saas.market.manager.service.couponmanager.CouponManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/10/16.
 */
@Transactional
@Service("couponManagerService")
public class CouponManagerServiceImpl extends BasicRemoteService implements CouponManagerService {

    @Override
    public JSONObject searchActivePage(){
        String activeRuleName = getUTF("activeRuleName", null);
        int activeRuleType = getIntParameter("activeRuleType", -1);
        int activeStatus = getIntParameter("activeStatus", -1);
        int pageSize = getPageSize();
        int pageIndex = getIntParameter("pageIndex", 1);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeRuleName", activeRuleName);
        parameters.put("activeRuleType", String.valueOf(activeRuleType));
        parameters.put("activeStatus", String.valueOf(activeStatus));
        parameters.put("pageSize", String.valueOf(pageSize));
        parameters.put("pageIndex", String.valueOf(pageIndex));

        try {
            String url = ConfigFactory.getDomainNameConfig().couponRemoteURL + "coupon/searchCouponPage.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

    @Override
    public JSONObject saveActiveRule(){
        String activeRuleName = getUTF("activeRuleName","");
        String activeRuleTitle = getUTF("activeRuleTitle","");
        int activeRuleType = getIntParameter("activeRuleType", -1);
        int activeDistributeCount = getIntParameter("activeDistributeCount", 0);
        Double activeMonery = getDoubleParameter("activeMonery",0);
        int activeRuleOrderMoney = getIntParameter("activeRuleOrderMoney",0);
        int activeRuleNum = getIntParameter("activeRuleNum",0);
        int activeScene = getIntParameter("activeScene",-1);
        int effectiveType = getIntParameter("effectiveType",-1);
        int activeRuleEffective = getIntParameter("activeRuleEffective",0);
        String activeBeginTime = getUTF("activeBeginTime","");
        String activeEndTime = getUTF("activeEndTime","");
        String activeRuleExplain = getUTF("activeRuleExplain","");
        String bak = getUTF("bak","");

        if(activeRuleName == null || activeRuleName.isEmpty()){
            return fail("缺少优惠券名称activeRuleName");
        }else if(activeRuleType==-1){
            return fail("缺少优惠券类型activeRuleType");
        }else if(activeDistributeCount==0){
            return fail("缺少优惠券总数activeDistributeCount");
        }else if(activeMonery==-1){
            return fail("缺少优惠金额activeMonery");
        }else if(activeRuleOrderMoney == 0){
            return fail("缺少使用门槛activeRuleOrderMoney");
        }else if(activeRuleNum==0){
            return fail("缺少每人限领activeRuleNum");
        }else if(activeScene==-1){
            return fail("缺少可使用商品activeScene");
        }else if(effectiveType==0){
            return fail("缺少优惠券有效时间类型effectiveType");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeRuleName", activeRuleName);
        parameters.put("activeRuleTitle", activeRuleTitle);
        parameters.put("activeRuleType", String.valueOf(activeRuleType));
        parameters.put("activeDistributeCount", String.valueOf(activeDistributeCount));
        parameters.put("activeMonery", String.valueOf(activeMonery));
        parameters.put("activeRuleOrderMoney", String.valueOf(activeRuleOrderMoney));
        parameters.put("activeRuleNum", String.valueOf(activeRuleNum));
        parameters.put("activeScene", String.valueOf(activeScene));
        parameters.put("effectiveType", String.valueOf(effectiveType));
        if(effectiveType==1){
            parameters.put("activeRuleEffective", String.valueOf(activeRuleEffective));
        }else{
            parameters.put("activeBeginTime", activeBeginTime);
            parameters.put("activeEndTime",activeEndTime);
        }
        parameters.put("activeRuleExplain", activeRuleExplain);
        parameters.put("bak",bak);

        try {
            String url = ConfigFactory.getDomainNameConfig().couponRemoteURL + "coupon/saveActiveRule.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }
    @Override
    public JSONObject updateActiveRule(){
        Long activeRuleID = getLongParameter("activeRuleID", -1);
        String activeRuleName = getUTF("activeRuleName","");
        String activeRuleTitle = getUTF("activeRuleTitle","");
        int activeRuleType = getIntParameter("activeRuleType", -1);
        int activeDistributeCount = getIntParameter("activeDistributeCount", 0);
        Double activeMonery = getDoubleParameter("activeMonery",0);
        int activeRuleOrderMoney = getIntParameter("activeRuleOrderMoney",0);
        int activeRuleNum = getIntParameter("activeRuleNum",0);
        int activeScene = getIntParameter("activeScene",-1);
        int effectiveType = getIntParameter("effectiveType",-1);
        int activeRuleEffective = getIntParameter("activeRuleEffective",0);
        String activeBeginTime = getUTF("activeBeginTime","");
        String activeEndTime = getUTF("activeEndTime","");
        String activeRuleExplain = getUTF("activeRuleExplain","");
        String bak = getUTF("bak","");

        if(activeRuleName == null || activeRuleName.isEmpty()){
            return fail("缺少优惠券名称activeRuleName");
        }else if(activeRuleType==-1){
            return fail("缺少优惠券类型activeRuleType");
        }else if(activeDistributeCount==0){
            return fail("缺少优惠券數量activeDistributeCount");
        }else if(activeMonery==-1){
            return fail("缺少优惠金额activeMonery");
        }else if(activeRuleOrderMoney == 0){
            return fail("缺少使用门槛activeRuleOrderMoney");
        }else if(activeRuleNum==0){
            return fail("缺少每人限领activeRuleNum");
        }else if(activeScene==-1){
            return fail("缺少可使用商品activeScene");
        }else if(effectiveType==0){
            return fail("缺少优惠券有效时间类型effectiveType");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeRuleID", String.valueOf(activeRuleID));
        parameters.put("activeRuleName", activeRuleName);
        parameters.put("activeRuleTitle", activeRuleTitle);
        parameters.put("activeRuleType", String.valueOf(activeRuleType));
        parameters.put("activeDistributeCount", String.valueOf(activeDistributeCount));
        parameters.put("activeMonery", String.valueOf(activeMonery));
        parameters.put("activeRuleOrderMoney", String.valueOf(activeRuleOrderMoney));
        parameters.put("activeRuleNum", String.valueOf(activeRuleNum));
        parameters.put("activeScene", String.valueOf(activeScene));
        parameters.put("effectiveType", String.valueOf(effectiveType));
        if(effectiveType==1){
            parameters.put("activeRuleEffective", String.valueOf(activeRuleEffective));
        }else{
            parameters.put("activeBeginTime", activeBeginTime);
            parameters.put("activeEndTime",activeEndTime);
        }
        parameters.put("activeRuleExplain", activeRuleExplain);
        parameters.put("bak",bak);

        try {
            String url = ConfigFactory.getDomainNameConfig().couponRemoteURL + "coupon/updateActiveRule.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }
    @Override
    public JSONObject getActiveRule(){
        long activeRuleID = getLongParameter("activeRuleID",-1);
        if(activeRuleID==-1){
            return fail("缺少优惠券規則ID=activeRuleID");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeRuleID", String.valueOf(activeRuleID));

        try {
            String url = ConfigFactory.getDomainNameConfig().couponRemoteURL + "coupon/getActiveRule.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }
    @Override
    public JSONObject getActiveRecords(){
        Long activeID = getLongParameter("activeRuleID",-1);
        if(activeID==-1){
            return fail("缺少优惠券ID=activeID");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeID", String.valueOf(activeID));

        try {
            String url = ConfigFactory.getDomainNameConfig().couponRemoteURL + "coupon/getActiveRecords.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }


    @Override
    public JSONObject delActive(){

        Long activeRuleID = getLongParameter("activeRuleID",-1);

        if(activeRuleID==-1){
            return fail("缺少优惠券规则ID=activeRuleID");
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("activeRuleID", String.valueOf(activeRuleID));

        try {
            String url = ConfigFactory.getDomainNameConfig().couponRemoteURL + "coupon/delActive.do";
            JSONObject response = executor(url, parameters);
            return response;
        }catch (XlibaoIllegalArgumentException ex){
            throw new XlibaoIllegalArgumentException("远程接口通信异常");
        }
    }

}
