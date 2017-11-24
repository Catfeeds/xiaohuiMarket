package com.zhumg.anlib;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.PermissionUtils;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by zhumg on 3/15.
 */
public abstract class AfinalActivity extends FragmentActivity implements AfinalActivityHttpLife {

    public static int top_bg_color_resid = 0;

    public abstract int getContentViewId();

    public abstract void initView(View view);

    protected Dialog loadingDialog;

    protected boolean httpLife = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        int viewid = getContentViewId();
        View rootView = null;
        if (viewid != 0) {
            rootView = View.inflate(this, viewid, null);
            setContentView(rootView);
            ButterKnife.bind(this);
            initView(rootView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
        httpLife = true;
        OkGo.getInstance().cancelTag(this);
        ActivityManager.removeActivity(this);
    }

    public void setLeftBack() {
//        final AfinalActivity activity = this;
//        setLeftIco(R.drawable.back);
//        setLeftListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                finish();
//            }
//        });
    }

    public boolean isHttpFinish() {
        return httpLife;
    }

    /**
     * 设置状态栏背景状态
     */
    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        int resid = top_bg_color_resid;
        if (resid != 0) {
            tintManager.setStatusBarTintResource(resid);// 状态栏无背景
        }
    }

    //网络请求
    public void httpGet(Map map, String url, HttpCallback callback) {
        showLoadingDialog();
        Http.get(this, map, url, callback, loadingDialog);
    }

    //POS
    public void httpPost(Map map, String url, HttpCallback callback) {
        showLoadingDialog();
        Http.post(this, map, url, callback, loadingDialog);
    }

    protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.createLoadingDialog(this, true, true);
        }
        loadingDialog.show();
    }

    protected void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    // ImageLoad
    @Override
    protected void onResume() {
        super.onResume();
        ImageLoader loader = ImageLoader.getInstance();
        if (loader != null) {
            loader.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImageLoader loader = ImageLoader.getInstance();
        if (loader != null) {
            loader.pause();
        }
    }


    //权限
    protected PermissionUtils permissionUtils;

    public void setPermissionUtils(PermissionUtils permissionUtils) {
        this.permissionUtils = permissionUtils;
    }

    /**
     * 处理权限
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        if (permissionUtils != null) {
            permissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }
}
