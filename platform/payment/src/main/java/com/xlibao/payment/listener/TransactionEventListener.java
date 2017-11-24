package com.xlibao.payment.listener;

import com.xlibao.common.constant.payment.TransStatusEnum;
import com.xlibao.payment.data.model.PaymentTransactionLogger;

/**
 * @author chinahuangxc on 2017/2/3.
 */
public interface TransactionEventListener {

    void notifyFinishPayment(PaymentTransactionLogger transactionLogger, TransStatusEnum transStatusEnum, boolean callback);
}