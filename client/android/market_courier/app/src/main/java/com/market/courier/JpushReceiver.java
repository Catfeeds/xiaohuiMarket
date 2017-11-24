package com.market.courier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.market.courier.main.MainActivity;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhumg on 9/26.
 */
public class JpushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    //当有推送过来时，回调这个
    //public static Runnable callback;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            processOpen(context, bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    // send msg to MainActivity
    private void processCustomMessage(final Context context, Bundle bundle) {
        if (bundle != null) {
            String message = bundle.getString(JPushInterface.EXTRA_EXTRA);// 获取自定义通知的消息
            try {
                JSONObject obj = new JSONObject(message);
                String voiceFile = obj.optString("voiceFile", null);
                if (voiceFile != null && voiceFile.trim().length() > 0) {
                    String jpushLogId = obj.optString("jpushLogId");
                    processMp3(obj, voiceFile, jpushLogId);
                }
                //刷新
                //if (callback != null) {
                //    callback.run();
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void processOpen(final Context context, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        String message = bundle.getString(JPushInterface.EXTRA_EXTRA);// 获取自定义通知的消息
        try {
            JSONObject obj = new JSONObject(message);
            String msg = obj.optString("msg", null);
            if (msg == null) {
                openMainActivity(context);
                return;
            }

            //直接刷新 拼音数量
            //EventBus.getDefault().post(new MainActivityEvent(0, MainActivityEvent.Refresh_Accept_Order_Count));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openMainActivity(Context context) {
        Intent newintent = new Intent(context, MainActivity.class);
        newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newintent);
    }

    void processMp3(JSONObject obj, String voiceFile, String jpushLogId) {

    }
}
