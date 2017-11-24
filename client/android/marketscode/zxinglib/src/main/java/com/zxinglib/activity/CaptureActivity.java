package com.zxinglib.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zxinglib.R;

/**
 * Initial the camera
 * <p>
 * 默认的二维码扫描Activity
 */
public class CaptureActivity extends AppCompatActivity {

    public static CaptureActivityListener listener;

    private CaptureViewHandler captureViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        View view = getWindow().getDecorView();
        view.findViewById(R.id.title_left_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });

        captureViewHandler = new CaptureViewHandler();
        captureViewHandler.create(this);
        captureViewHandler.initView(view);
        captureViewHandler.setAnalyzeCallback(analyzeCallback);
        captureViewHandler.setCameraInitCallBack(new CaptureViewHandler.CameraInitCallBack() {
            @Override
            public void callBack(Exception e) {
                if (e == null) {

                } else {
                    Log.e("TAG", "callBack: ", e);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureViewHandler.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureViewHandler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureViewHandler.onDestroy();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
//            CaptureActivity.this.finish();
            if(listener != null) {
                if(listener.notifyCode(mBitmap, result)) {
                    CaptureActivity.this.finish();
                }
            }
        }

        @Override
        public void onAnalyzeFailed() {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//            bundle.putString(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
//            CaptureActivity.this.finish();
            if(listener != null) {
                if(listener.notifyCode(null, null)) {
                    CaptureActivity.this.finish();
                }
            }
        }
    };
}