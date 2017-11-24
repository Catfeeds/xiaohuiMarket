package com.market.courier.main;


import android.content.Context;
import android.os.Handler;

import com.market.courier.response.Api;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhumg on 9/29.
 */
public class RefreshAccessToken implements Runnable {

    //刷新 accessToken
    public static final String REFRESH_ACCESSTOKEN = Api.PASSPORT_URL + "passport/extendAccessToken";

//    public static final int TIME = 1000 * 60 * 40;
    public static final int TIME = 1000 * 10;

    private Handler handler;
    private Context context;

    public void start(Context context) {
        this.context = context;
        if(handler == null) {
            handler = new Handler();
        }
        //10分钟刷新一次
        handler.postDelayed(this, TIME);
    }

    @Override
    public void run() {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        Http.post(context, map, REFRESH_ACCESSTOKEN, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                handler.postDelayed(RefreshAccessToken.this, TIME);
            }

            @Override
            public void onFailure() {
                handler.postDelayed(RefreshAccessToken.this, TIME);
            }
        });
    }

    public void close() {
        handler.removeCallbacks(RefreshAccessToken.this);
    }
}
