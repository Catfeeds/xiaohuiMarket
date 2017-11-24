package com.xiaohui.replenishment;

import android.view.View;
import android.widget.EditText;

import com.xiaohui.replenishment.response.Api;
import com.xiaohui.replenishment.response.HttpCallbackImpl;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 9/18.
 */
public class InputActivity extends AfinalActivity implements View.OnClickListener {

    BaseTitleBar titleBar;

    @Bind(R.id.save_btn)
    View save_btn;

    @Bind(R.id.input_txt)
    EditText input_txt;

    long marketId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_input;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        titleBar = new BaseTitleBar(view);
        titleBar.setCenterTxt("异常信息");
        titleBar.setLeftBack(this);
        marketId = getIntent().getLongExtra("marketId", 0);
        save_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save_btn) {
            String txt = input_txt.getText().toString().trim();
            if(txt.length() < 1) {
                ToastUtil.showToast(this, "请输入异常信息");
                return;
            }
            //提交
            submit(txt);
        }
    }

    void submit(String txt) {
        showLoadingDialog();
        //通知修改
        Map map = new HashMap<>();
        map.put("marketId", String.valueOf(marketId));
        map.put("passportId", String.valueOf(Api.passport.getPassportIdStr()));
        map.put("mark", txt);
        //登陆
        httpPost(map, Api.FINISH_DAY_TASK, new HttpCallbackImpl() {
            @Override
            public void onSuccess(Object data) {
                closeLoadingDialog();
                setResult(100);
                finish();
                ToastUtil.showToast(InputActivity.this, msg);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                closeLoadingDialog();
            }
        });
    }
}
