package com.xiaohui.courier.widget;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xiaohui.courier.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

/**
 * Created by Administrator on 2017/9/15 0015.
 */

public class WebActivity extends AfinalActivity {

    WebView webView;
    ProgressBar progressBar;
    BaseTitleBar titleBar;


    public int getContentViewId() {
        return R.layout.activity_web;
    }

    public void initView(View view) {
        setTranslucentStatus();

        titleBar = new BaseTitleBar(view);
        titleBar.setLeftBack(this);
        titleBar.setCenterTxt(getIntent().getStringExtra("title"));

        webView = (WebView) view.findViewById(R.id.wview);
        progressBar = (ProgressBar) view.findViewById(R.id.pbar);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
        webSettings.supportMultipleWindows(); // 多窗口
        // webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 关闭webview中缓存
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        webSettings.setDefaultTextEncodingName("UTF-8");// 设置编码UTF-8
        webSettings.setSupportMultipleWindows(true);
        //webSettings.setSupportZoom(true);
        //webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //设置滚动条样式

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
//                if(url.startsWith("weixin://wap/pay?")) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//                if (AdUrlJumpHandler.handler(WebViewActivity.this, url, "my_msg_ad", null)) {
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);
//            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.loadUrl(getIntent().getStringExtra("url"));
    }
}
