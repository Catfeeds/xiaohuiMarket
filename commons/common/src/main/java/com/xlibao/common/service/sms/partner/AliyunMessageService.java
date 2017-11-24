package com.xlibao.common.service.sms.partner;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.xlibao.common.DefineRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chinahuangxc on 2017/5/18.
 */
public class AliyunMessageService {

    private static final Logger logger = LoggerFactory.getLogger(AliyunMessageService.class);

    private static String aliyunAccessKeyId = "LTAID6hYVA5uzY4c";
    private static String aliyunAccessKeySecret = "xmxtP7WAvEkkEh0QPeP221tpj0zuYg";
    private static String aliyunAccountEndpoint = "http://1787893860807092.mns.cn-shenzhen.aliyuncs.com/";
    private static String smsSignName = "心礼宝";
    private static String smsTopic = "sms.topic-cn-shenzhen";

    public static void setAliyunAccessParameters(String aliyunAccessKeyId, String aliyunAccessKeySecret, String aliyunAccountEndpoint, String smsSignName, String smsTopic) {
        AliyunMessageService.aliyunAccessKeyId = aliyunAccessKeyId;
        AliyunMessageService.aliyunAccessKeySecret = aliyunAccessKeySecret;
        AliyunMessageService.aliyunAccountEndpoint = aliyunAccountEndpoint;
        AliyunMessageService.smsSignName = smsSignName;
        AliyunMessageService.smsTopic = smsTopic;
    }

    public static void sendSmsVerifyCode(String phone, String templateCode, String smsCode) {
        /** Step 1. 获取主题引用 */
        CloudAccount account = new CloudAccount(aliyunAccessKeyId, aliyunAccessKeySecret, aliyunAccountEndpoint);
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(smsTopic);
        /**
         * Step 2. 设置SMS消息体（必须）
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");

        /** Step 3. 生成SMS消息属性 */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(smsSignName);
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(templateCode);
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("code", smsCode);
        // 3.4 增加接收短信的号码
        batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);

        try {
            /** Step 4. 发布SMS消息 */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            logger.info("MessageId: " + ret.getMessageId());
            logger.info("MessageMD5: " + ret.getMessageBodyMD5());

            logger.info("手机号：" + phone + "，请求模版：" + templateCode + "，验证码：" + smsCode);
        } catch (ServiceException se) {
            logger.error(se.getErrorCode() + se.getRequestId());
            logger.error(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }

    public static void main(String[] args) {
        String smsCode = DefineRandom.randomNumber(4);
        AliyunMessageService.sendSmsVerifyCode("13668928575", "SMS_67176279", smsCode);

        smsCode = DefineRandom.randomNumber(4);
        AliyunMessageService.sendSmsVerifyCode("18680233352", "SMS_67176279", smsCode);

        smsCode = DefineRandom.randomNumber(4);
        AliyunMessageService.sendSmsVerifyCode("15820210284", "SMS_67176279", smsCode);
    }
}
