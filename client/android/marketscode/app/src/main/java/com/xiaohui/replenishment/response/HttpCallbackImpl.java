package com.xiaohui.replenishment.response;

import android.content.Intent;

import com.xiaohui.replenishment.InputActivity;
import com.xiaohui.replenishment.LoginActivity;
import com.xiaohui.replenishment.MainActivity;
import com.xiaohui.replenishment.ScodeActivity;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;

/**
 * Created by zhumg on 9/20.
 */
public abstract class HttpCallbackImpl<T> extends HttpCallback<T> {


    public HttpCallbackImpl() {
    }

    public HttpCallbackImpl(String dataKey) {
        super(dataKey);
    }

    public boolean checkAccessToken() {
        if (code == 999) {
            TipDialog tipDialog = DialogUtils.createTipDialog(context, "温馨提示", msg, "退出应用", "重新登陆", new TipClickListener() {
                @Override
                public void onClick(boolean left) {
                    if (left) {
                        ActivityManager.AppExit(context);
                    } else {
                        //重新弹出登陆界面
                        context.startActivity(new Intent(context, LoginActivity.class).putExtra("jump", true));
                        //关闭主界面
                        ActivityManager.finishActivity(MainActivity.class);
                        //关闭所有其它界面
                        ActivityManager.finishActivity(ScodeActivity.class);
                        //输入框关闭
                        ActivityManager.finishActivity(InputActivity.class);
                    }
                }
            });
            tipDialog.show();
            return true;
        } else {
            return false;
        }
    }
}
