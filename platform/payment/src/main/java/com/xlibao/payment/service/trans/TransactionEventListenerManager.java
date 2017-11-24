package com.xlibao.payment.service.trans;

import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.payment.data.model.PaymentTransactionLogger;
import com.xlibao.payment.listener.TransactionEventListener;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/2/3.
 */
@Component
public class TransactionEventListenerManager {

    private Map<String, TransactionEventListener> transactionEventListenerMap = new HashMap<>();

    void registerTransactionEventListener(String moduleName, TransactionEventListener transactionEventListener) {
        transactionEventListenerMap.put(moduleName, transactionEventListener);
    }

    public void notifyFinishPayment(PaymentTransactionLogger transactionLogger, TransStatusEnum transStatusEnum, boolean callback) {
        for (TransactionEventListener transactionEventListener : transactionEventListenerMap.values()) {
            transactionEventListener.notifyFinishPayment(transactionLogger, transStatusEnum, callback);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(URLEncoder.encode("{\"transCreateTime\":1502708373000,\"partnerTradeNumber\":\"190820170814185932507459782\",\"useCoupon\":0,\"sign\":\"9BC68A350DD9BDF5CD6E88F7767FE0E8\",\"discountAmount\":0,\"transSequenceNumber\":\"P90820170814185933940601200\",\"extendParameter\":\"\",\"paymentType\":\"BALANCE\",\"transType\":8,\"channelTradeNumber\":\"P90820170814185933940601200\",\"channelUserId\":\"100001\",\"appId\":\"908100000\",\"partnerUserId\":\"100000\",\"channelUserName\":\"merchant\",\"transUnitAmount\":116,\"partnerId\":\"xlb908100000\",\"transTotalAmount\":116,\"paymentTime\":1502709260684,\"transStatus\":8,\"transNumber\":1,\"channelRemark\":\"\"}", "utf-8"));
    }
}