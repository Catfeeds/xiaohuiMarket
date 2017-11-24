package com.zxinglib.activity;

import android.graphics.Bitmap;

/**
 * Created by zhumg on 8/30.
 */
public interface CaptureActivityListener {
    public boolean notifyCode(Bitmap mBitmap, String result);
}
