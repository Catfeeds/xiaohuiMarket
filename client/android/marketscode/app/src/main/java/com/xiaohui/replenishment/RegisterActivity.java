package com.xiaohui.replenishment;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/9/10 0010.
 */

public class RegisterActivity extends AfinalActivity implements View.OnClickListener {

    //    @Bind(R.id.et_name)
//    EditText etName;
    @Bind(R.id.et_password)
    EditText etPassword;
    //    @Bind(et_password2)
//    EditText etPassword2;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_code)
    TextView btnCode;
    @Bind(R.id.btn_next)
    TextView btnNext;

    BaseTitleBar baseTitleBar;

    int mTotalTime = 60;
    Handler handler;
    Runnable runnable;
    boolean canGetCode = true;

    String codePhone = null;

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        btnCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                mTotalTime--;
                if (mTotalTime == 0) {
                    //停止
                    canGetCode = true;
                    handler.removeCallbacks(runnable);
                    btnCode.setText("获取验证码");
                    btnCode.setBackgroundResource(R.drawable.btn_blue);
                } else {
                    btnCode.setText("重新获取(" + mTotalTime + ")");
                    handler.postDelayed(runnable, 1000);
                }
            }
        };
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("注册帐号");
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            //确定
            register();
        } else if (v.getId() == R.id.btn_code) {
//            //获取验证码
//            if (!canGetCode) {
//                return;
//            }
//            //电话
//            final String phone = etPhone.getText().toString().trim();
//            if (StringUtils.isEmpty(this, phone, "请输入手机号码")) {
//                return;
//            }
//            if(phone.length() != 11) {
//                ToastUtil.showToast(RegisterActivity.this, "请输入正确手机号");
//                return;
//            }
//            //设置获取为true
//            canGetCode = false;
//            mTotalTime = 60;
//            btnCode.setText("重新获取(" + mTotalTime + ")");
//            btnCode.setBackgroundResource(R.drawable.btn_grey);
//            handler.postDelayed(runnable, 1000);
//            //获取 验证码
//            Http.post(this, ApiRequest.requestVerificationCode(phone, String.valueOf(SmsCodeTypeEnum.REGISTER.getKey())), Api.GET_SMS, new HttpCallback() {
//                @Override
//                public void onSuccess(Object data) {
//                    if (!StringUtils.isEmpty(msg)) {
//                        ToastUtil.showToast(RegisterActivity.this, msg);
//                    }
//                    //记录获取了验证码的手机号码
//                    codePhone = phone;
//                }
//
//                @Override
//                public void onFailure() {
//                    super.onFailure();
//                    mTotalTime = 6;
//                }
//            }.setPass());
        }
    }

    void setCanGetCode() {
        handler.removeCallbacks(runnable);
        btnCode.setText("获取验证码");
        btnCode.setBackgroundResource(R.drawable.btn_blue);
        canGetCode = true;
    }

    void register() {
        //   final String user = etName.getText().toString().trim();
//        if (StringUtils.isEmpty(this, user, "请输入用户名")) {
//            return;
//        }
        final String upass = etPassword.getText().toString().trim();
        if (StringUtils.isEmpty(this, upass, "请输入密码")) {
            return;
        }
        if(upass.length()<6){
            ToastUtil.showToast(this, "密码为6位数以上的字符（可以是纯字符，可以是数字加字符）");
            return;
        }
//        String rPass = etPassword2.getText().toString().trim();
//        if (StringUtils.isEmpty(this, rPass, "请输入确认密码")) {
//            return;
//        }
//        if (!upass.equals(rPass)) {
//            ToastUtil.showToast(this, "两次密码不一致，请重新输入");
//            return;
//        }
        final String phone = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(this, phone, "请输入手机号")) {
            return;
        }
        String code = etCode.getText().toString().trim();
        if (StringUtils.isEmpty(this, code, "请输入验证码")) {
            return;
        }
        if (codePhone == null || !codePhone.equals(phone)) {
            ToastUtil.showToast(this, "验证码错误，请重新获取");
            return;
        }
        //注册
//        Map map = ApiRequest.registerPassport(this, upass, phone, code);
//        httpPost(map, Api.REGISTER_PASSPORT, new HttpCallback<Passport>() {
//            @Override
//            public void onSuccess(Passport data) {
//                //直接以这个登陆一次
//                //重置保存
//                SpUtils.saveValue("pass", upass);
//                TipDialog tipDialog = DialogUtils.createTipDialog(RegisterActivity.this, "注册成功，请牢记你的帐号和密码。", "确定", new TipClickListener() {
//                    @Override
//                    public void onClick(boolean left) {
//                        //直接
//                        login(phone, upass);
//                    }
//                });
//                tipDialog.show();
//            }
//
//            @Override
//            public void onFailure() {
//                super.onFailure();
//                //setCanGetCode();
//                mTotalTime = 6;
//            }
//        });
    }

    void login(final String uname, final String password) {
        //登陆
//        httpPost(ApiRequest.login(this, uname, password), Api.LOGIN, new HttpCallback<Passport>() {
//            @Override
//            public void onSuccess(Passport data) {
//
//                Cache.passport = data;
//                if(Cache.passport!=null)
//                    Cache.passport.setLoginName(uname);
//                //登陆成功，用passportId作为极光别名
//                JpushAliasUtil.setJpushAlias(RegisterActivity.this, data.getPassportIdStr());
//
//                //直接进入
//                if (data.isPlatformSupplier() || data.isSupplier()) {
//                    ActivityManager.startActivityAndFinish(RegisterActivity.this, SupplierActivity.class);
//                } else if (data.getRoleValue() == PassportRoleTypeEnum.MERCHANT.getKey()) {
//                    ActivityManager.startActivityAndFinish(RegisterActivity.this, MerchantActivity.class);
//                } else {
//                    ToastUtil.showToast(RegisterActivity.this, "帐号错误");
//                    return;
//                }
//            }
//
//            @Override
//            public void onFailure() {
//                finish();
//            }
//        });
    }
}
