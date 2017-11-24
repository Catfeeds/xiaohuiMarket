package com.market.courier.main;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.market.courier.R;
import com.market.courier.response.Api;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class ActivityOpen extends AfinalActivity {

    BaseTitleBar titleBar;

    //图片
    @Bind(R.id.code_img)
    ImageView code_img;

    @Bind(R.id.kf_phone)
    View kf_phone;

    @Bind(R.id.tv_orderid)
    TextView tv_orderid;

    @Bind(R.id.kf_num)
    TextView kf_num;

    String orderSequenceNumber;

    //是否生成二维码图片了
    boolean create_img_bool = false;

    boolean runing = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_open;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        titleBar = new BaseTitleBar(view);
        titleBar.setCenterTxt("开锁取件");
        titleBar.setLeftBack(this);

        kf_num.setText(Api.KF_PHONE_TXT);

        orderSequenceNumber = getIntent().getStringExtra("orderSequenceNumber");
        tv_orderid.setText(orderSequenceNumber);

        kf_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtils.callPhone(ActivityOpen.this, Api.KF_PHONE);
            }
        });

        //生成图片
        new Thread() {
            public void run() {
                while(runing) {
                    if (!create_img_bool) {
                        create_img_bool = true;
                        final Bitmap bitmap = createQRImage(orderSequenceNumber, 300, 300);
                        if (bitmap != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    code_img.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } else {
                        getCodeResult();
                    }
                    try {
                        //2分钟搞一次
                        Thread.sleep(2000);
                    } catch (Exception ex) {

                    }
                }
            }
        }.start();
    }

    void getCodeResult() {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("orderSequenceNumber", orderSequenceNumber);
        Http.post(this, map, Api.FIND_CONTAINER_DATA, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                runing = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runing = false;
    }

    static Bitmap createQRImage(String qrcode, int widthPix, int heightPix) {
        try {
            if (qrcode == null || "".equals(qrcode)) {
                return null;
            }
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            //            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrcode, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            //if (logoBm != null) {
            //bitmap = addLogo(bitmap, logoBm);
            //}

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }
}
