package com.xlibao.saas.market.core.service.support;

import com.alibaba.fastjson.JSONObject;
import com.xlibao.common.exception.XlibaoRuntimeException;
import com.xlibao.common.support.BasicRemoteService;
import com.xlibao.saas.market.core.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/8/19.
 */
public class MarketApplicationRemoteService extends BasicRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(MarketApplicationRemoteService.class);

    /**
     * <pre>
     *     <b>0001 -- 通知配货结果</b>
     * </pre>
     *
     * @param orderSequenceNumber 订单号
     * @param serialNumber        订单副号
     */
    public static JSONObject notifyShipment(long passportId, String orderSequenceNumber, String serialNumber) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("orderSequenceNumber", orderSequenceNumber);
        parameters.put("serialNumber", serialNumber);

        String url = ConfigFactory.getDomainName().marketRemoteURL + "market/message/callback/notifyShipment.do";
        JSONObject response = executor(url, parameters);

        logger.info(orderSequenceNumber + "[" + serialNumber + "]通知商店取货结果：" + response);
        return response;
    }

    /**
     * <pre>
     *     <b>0002 -- 通知货架数据</b> -- 一般在初始化时使用
     * </pre>
     *
     * @param passportId    商店的通行证ID
     * @param content       货架数据
     */
    public static JSONObject notifyShelvesData(long passportId, String content) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("content", content);

        String url = ConfigFactory.getDomainName().marketRemoteURL + "market/message/callback/notifyShelvesData.do";
        JSONObject response = executor(url, parameters);

        logger.info(passportId + " -- 通知商店货架信息结果：" + response);
        return response;
    }

    /**
     * <pre>
     *     <b>0003 -- 通知订单数据信息</b>
     * </pre>
     *
     * @param passportId            店铺的通行证ID
     * @param orderSequenceNumber   反馈的订单编号
     * @param statusCode            状态码 --------- 00表示订单还未执行
     *                                      --------- 01表示订单正在配送中
     *                                      --------- 10表示已完成
     *                                      --------- EE表示订单不存在
     */
    public static JSONObject notifyOrderData(Long passportId, String orderSequenceNumber, String statusCode) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("orderSequenceNumber", orderSequenceNumber);
        parameters.put("statusCode", statusCode);

        String url = ConfigFactory.getDomainName().marketRemoteURL + "market/message/callback/notifyOrderData.do";
        JSONObject response = executor(url, parameters);

        logger.info(passportId + " -- 通知商店订单信息结果：" + response);
        return response;
    }

    /**
     * <pre>
     *     <b>0004 -- 通知退款结果</b>
     * </pre>
     *
     * @param passportId            店铺的通行证ID
     * @param orderSequenceNumber   完成退货的订单编号
     * @param statusCode            状态码 --------- 00表示退货完成
     *                                      --------- 01表示退货发生了故障
     */
    public static JSONObject notifyRefund(Long passportId, String orderSequenceNumber, String statusCode) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("orderSequenceNumber", orderSequenceNumber);
        parameters.put("statusCode", statusCode);

        String url = ConfigFactory.getDomainName().marketRemoteURL + "market/message/callback/notifyRefund.do";
        JSONObject response = executor(url, parameters);

        logger.info(passportId + " -- 通知订单退款结果：" + response);
        return response;
    }

    /**
     * <pre>
     *     <b>0005 -- 通知取货结果</b>
     * </pre>
     *
     * @param passportId            店铺的通行证ID
     * @param orderSequenceNumber   取货的订单编号
     * @param statusCode            状态码 --------- 00表示柜门打开
     *                                      --------- 01表示柜门打开发生了故障
     *                                      --------- 02表示还没有配送完成
     *                                      --------- EE表示订单不存在
     */
    public static JSONObject notifyPickUp(long passportId, String orderSequenceNumber, String statusCode) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));   // 店铺的通行证ID
        parameters.put("orderSequenceNumber", orderSequenceNumber); // 取货的单号
        parameters.put("statusCode", statusCode);                   // 状态码

        String url = ConfigFactory.getDomainName().marketRemoteURL + "market/message/callback/notifyPickUp.do";
        JSONObject response = executor(url, parameters);

        logger.info(passportId + " -- 通知商店提货信息结果：" + response);
        return response;
    }

    public static boolean askOrderPickUp(long passportId, String orderSequenceNumber) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("passportId", String.valueOf(passportId));
        parameters.put("orderSequenceNumber", orderSequenceNumber);

        String url = ConfigFactory.getDomainName().marketRemoteURL + "market/message/callback/askOrderPickUp.do";
        try {
            executor(url, parameters);
        } catch (Exception ex) {
            if (ex instanceof XlibaoRuntimeException) {
                if (((XlibaoRuntimeException) ex).getCode() != -1) {
                    return false;
                }
            }
            logger.error("询问是否可进行获取时发生了异常，此时因不知处于什么状态，先按正常处理(即通知硬件出货)；异常信息：" + ex.getMessage(), ex);
        }
        return true;
    }
}