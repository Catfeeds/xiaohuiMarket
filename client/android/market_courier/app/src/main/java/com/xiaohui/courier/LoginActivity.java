package com.xiaohui.courier;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xiaohui.courier.response.Api;
import com.xiaohui.courier.response.enums.SmsCodeTypeEnum;
import com.xiaohui.courier.widget.WebActivity;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

import static com.xiaohui.courier.R.id.phone;

/**
 * Created by zhumg on 9/22.
 */
public class LoginActivity extends AfinalActivity implements View.OnClickListener {


    @Bind(R.id.code_btn)
    TextView code_btn;

    @Bind(R.id.btn_login)
    View btn_login;

    @Bind(R.id.tv_info)
    View tv_info;

    @Bind(R.id.tv_change)
    View tv_change;

    @Bind(R.id.pwd)
    TextView pwd;

    @Bind(phone)
    TextView phoneTxt;

    @Bind(R.id.get_code_ll)
    View get_code_ll;

    int mTotalTime = 60;
    Handler handler;
    Runnable runnable;
    boolean canGetCode = true;

    String db_phone = null;


    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {

        tv_info.setOnClickListener(this);
        code_btn.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_change.setOnClickListener(this);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                mTotalTime--;
                if (mTotalTime == 0) {
                    //停止
                    canGetCode = true;
                    handler.removeCallbacks(runnable);
                    code_btn.setText("获取验证码");
                    //code_btn.setBackgroundResource(R.drawable.btn_blue);
                } else {
                    code_btn.setText("重新获取(" + mTotalTime + ")");
                    handler.postDelayed(runnable, 1000);
                }
            }
        };

        String phone = SpUtils.loadValue("userPhone");
        if (!StringUtils.isEmpty(phone)) {
            db_phone = phone;
            phoneTxt.setText(phone);
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_info) {
            //获取用户协议
            startActivity(new Intent(this, WebActivity.class).putExtra("title", "小惠配送平台服务协议").putExtra("url", "http://www.baidu.com"));
        } else if (id == R.id.code_btn) {
            //获取 验证码
            getCode();
        } else if (id == R.id.btn_login) {
            login();
        }
    }

    void login() {
        final String mobile = phoneTxt.getText().toString();
        String code = pwd.getText().toString();
        if (StringUtils.isEmpty(mobile)) {
            ToastUtil.showToast(this, "请输入手机号");
            return;
        }
        if (StringUtils.isEmpty(code)) {
            ToastUtil.showToast(this, "请输入验证码");
            return;
        }
        String referee = "11111111111";
        Map map = new HashMap();
        map.put("mobileNo", mobile);
        map.put("code", code);
        map.put("referee", referee);

//        Http.post(this, map, Api.LOGIN, new HttpCallback<Login>() {
//            @Override
//            public void onSuccess(Login data) {
//                Log.e("zzzzz", "登陆成功，sid="+data.getSid()+", key="+data.getKey()+", mobile="+mobile);
//                Http.setSid(data.getSid(), data.getKey(), mobile);
//                finish();
//                //通知登陆成功
//                EventBus.getDefault().post(new LoginEvent());
//            }
//
//            @Override
//            public void onFailure() {
//                ToastUtil.showToast(LoginActivity.this, msg);
//            }
//        });
    }

    void getCode() {
        //获取验证码
        if (!canGetCode) {
            return;
        }
        String input_phone = phoneTxt.getText().toString().trim();
        if (StringUtils.isEmpty(this, input_phone, "请输入手机号码")) {
            return;
        }
        final String phone_str = input_phone;
        //设置获取为true
        canGetCode = false;
        mTotalTime = 60;
        code_btn.setText("重新获取(" + mTotalTime + ")");
        handler.postDelayed(runnable, 1000);

        Map map = new HashMap();
        map.put("phoneNumber", input_phone);
        map.put("type", String.valueOf(SmsCodeTypeEnum.LOGIN.getKey()));
        //获取 验证码
        Http.post(this, map, Api.GET_SMS, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                if (!StringUtils.isEmpty(msg)) {
                    ToastUtil.showToast(LoginActivity.this, msg);
                }
            }
            @Override
            public void onFailure() {
                super.onFailure();
                mTotalTime = 6;
            }
        }.setPass());
    }

    @Override
    public void finish() {
        super.finish();
        if(handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    int num = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (num == 1) {
                ++num;
                countDownTimer.start();
                ToastUtil.showToast(LoginActivity.this, "再按一次退出应用");
            } else {
                ActivityManager.AppExit(getApplicationContext());
            }
        }
        return false;
    }

    private CountDownTimer countDownTimer = new CountDownTimer(3000, 3000) {

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            num = 1;
        }
    };
}
