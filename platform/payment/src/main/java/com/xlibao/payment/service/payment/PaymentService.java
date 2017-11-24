package com.xlibao.payment.service.payment;

import com.alibaba.fastjson.JSONObject;

/**
 * @author chinahuangxc on 2017/2/2.
 */
public interface PaymentService {

    JSONObject unifiedOrder();

    JSONObject balancePayment();

    JSONObject offsetBalance();

    JSONObject isSetPaymentPassword();

    JSONObject firstSetPaymentPassword();

    JSONObject setPaymentPassword();

    JSONObject passportCurrency();

    JSONObject rechargeActivityTemplates();

    JSONObject rechargeBalance();

    JSONObject rechargeFlows();

    JSONObject rechargeDetail();

    JSONObject showAmountBalanceOfPayments();

    JSONObject balanceFlows();

    JSONObject drawCashFlows();

    JSONObject paymentDetail();

    JSONObject incomeStatistics();

    JSONObject showBankTemplates();

    JSONObject bindBankAccount();

    JSONObject showDrawCashMode();

    JSONObject beforeTakeCash();

    JSONObject drawCash();

    JSONObject showBanks();

    JSONObject setDefaultBank();

    JSONObject refund();
}