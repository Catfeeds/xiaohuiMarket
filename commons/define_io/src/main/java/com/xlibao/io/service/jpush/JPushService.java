package com.xlibao.io.service.jpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.xlibao.common.CommonUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JPushService {

    private static final Logger logger = Logger.getLogger(JPushService.class);

    /**
     * <pre>
     *      <b>极光推送API</b>
     * </pre>
     */
    public static String pushMessage(JPushClient jpushClient, String title, String content, String voiceFile, Map<String, String> message, String... targets) {
        logger.info("JPush title " + title + " content " + content + " message " + message + " targets " + Arrays.toString(targets));
        if (message == null) {
            message = new HashMap<>();
        }
        title = CommonUtils.isNullString(title) ? JPushConfig.DEFAULT_TITLE : title;
        content = CommonUtils.isNullString(content) ? JPushConfig.DEFAULT_CONTENT : content;

        PushPayload.Builder payload = PushPayload.newBuilder();
        // 语音提醒
        if (CommonUtils.isNotNullString(voiceFile)) {
            message.put("voiceFile", voiceFile);
        }
        // 推送类型默认推送通知，如果 pushType：Message 则消息推送
        String pushType = message.get("pushType");
        payload.setOptions(Options.newBuilder().setApnsProduction(JPushConfig.APN_PRODUCTION).setTimeToLive(JPushConfig.TIME_LIVE).build()).setPlatform(Platform.android_ios()).setAudience(Audience.alias(targets));
        if ("Message".equals(pushType)) {
            payload.setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(message).build());
        } else {
            payload.setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(title).setSound(voiceFile).addExtras(message).build()).
                    addPlatformNotification(AndroidNotification.newBuilder().setAlert(title).setTitle(content).addExtras(message).build()).build());
        }
        PushPayload pushPayload = payload.build();
        logger.info("JPush pay load: " + pushPayload.toString());
        try {
            PushResult result = jpushClient.sendPush(pushPayload);
            logger.info("JPush消息接受者：" + ArrayUtils.toString(targets));
            logger.info("JPush消息推送结果：" + result);
            return result.toString();
        } catch (APIConnectionException e) {
            logger.error("推送服务连接失败，请及时联系极光服务团队【QQ:2501528415】！！！\r\n", e);
        } catch (APIRequestException e) {
            logger.info("JPush api request code : " + e.getErrorCode());
            int errorCode = e.getErrorCode();
            if (errorCode != 0) {
                logger.error(e.getMessage() + " 错误码：" + errorCode);
            }
            return "Error code: " + errorCode + ", msg: " + e.getErrorMessage();
        }
        return "failed";
    }
}