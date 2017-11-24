package com.xiaohui.courier.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaohui.courier.R;
import com.xiaohui.courier.response.Api;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

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

    @Bind(R.id.kf_num)
    TextView kf_num;

    @Override
    public int getContentViewId() {
        return R.layout.activity_open;
    }

    @Override
    public void initView(View view) {

        titleBar = new BaseTitleBar(view);
        titleBar.setCenterTxt("开锁取件");
        titleBar.setLeftBack(this);

        kf_num.setText(Api.KF_PHONE_TXT);

        String code = getIntent().getStringExtra("code");
        //直接显示到图片
        ImageLoader.getInstance().displayImage(code, code_img);

        kf_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUtils.callPhone(ActivityOpen.this, Api.KF_PHONE);
            }
        });
    }
}
