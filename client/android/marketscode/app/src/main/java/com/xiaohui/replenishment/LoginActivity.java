package com.xiaohui.replenishment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaohui.replenishment.response.Api;
import com.xiaohui.replenishment.response.Passport;
import com.xiaohui.replenishment.response.enums.ClientTypeEnum;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Callback;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ApkUtils;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 9/5.
 */
public class LoginActivity extends AfinalActivity implements View.OnClickListener {

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.tv_register)
    TextView tv_register;
    @Bind(R.id.tv_findpass)
    TextView tv_findpass;
    @Bind(R.id.btn_login)
    Button btnLogin;

    boolean jump = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();

        jump = getIntent().getBooleanExtra("jump", false);

        btnLogin.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_findpass.setOnClickListener(this);
        //隐藏找回密码
        //tv_findpass.setVisibility(View.INVISIBLE);

        boolean input = getIntent().getBooleanExtra("input", true);
        if (!input) {
            phone.setText("");
            pwd.setText("");
        } else {
            String n = SpUtils.loadValue("name");
            String p = SpUtils.loadValue("pass");
            if (!StringUtils.isEmpty(n)) {
                phone.setText(n);
            }
            if (!StringUtils.isEmpty(p)) {
                pwd.setText(p);
            }
            n = phone.getText().toString();
            p = pwd.getText().toString();
            if (n.length() <= 1) {
                phone.requestFocus();
            } else if (p.length() <= 1) {
                pwd.requestFocus();
            } else {
                phone.requestFocus();
                phone.setSelection(n.length());
            }
        }

        //版本检测
        UpdateManager.httpCheckUpdate(this, false);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                // 登陆
                login();
                break;
            case R.id.tv_register:
                // 注册
                //ActivityManager.startActivity(this, RegisterActivity.class);
                break;
            case R.id.tv_findpass:
                // 找回密码
                //Intent intent = new Intent(this, ResetPasswordActivity.class);
                //intent.putExtra("updateType", UpdatePasswordTypeEnum.FIND_PASSWORD.getKey());
                //startActivity(intent);
                break;
        }
    }

    void login() {
        final String uname = phone.getText().toString().trim();
        if (StringUtils.isEmpty(this, uname, "请输入用户名")) {
            return;
        }
        final String password = pwd.getText().toString().trim();
        if (StringUtils.isEmpty(this, password, "请输入密码")) {
            return;
        }
        Map map = new HashMap<>();
        map.put("username", uname);
        map.put("password", password);
        map.put("clientType", String.valueOf(ClientTypeEnum.WAREHOUSE.getKey()));
        map.put("versionIndex", String.valueOf(ApkUtils.getVersionCode(this)));
        //登陆
        httpPost(map, Api.LOGIN, new HttpCallback<Passport>() {
            @Override
            public void onSuccess(Passport data) {

                SpUtils.saveValue("name", uname);
                SpUtils.saveValue("pass", password);
                Api.passport = data;

                Api.passport.setLoginName(uname);
                Callback.ACCESS_KEY = data.getAccessToken();
                Callback.PASSPORT_ID = Api.passport.getPassportIdStr();


                if(jump) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
