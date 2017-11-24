package com.market.courier.main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.market.courier.R;
import com.market.courier.response.Api;
import com.market.courier.response.OrderData;
import com.market.courier.response.OrderItemData;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class ActivityOrderDetail extends AfinalActivity implements View.OnClickListener {

    BaseTitleBar titleBar;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.item_ll)
    LinearLayout item_ll;

    @Bind(R.id.item_count)
    TextView item_count;

    @Bind(R.id.ok_btn)
    View ok_btn;

    @Bind(R.id.kf_phone)
    View kf_phone;

    @Bind(R.id.kf_num)
    TextView kf_num;

    OrderData orderData;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView(View view) {

        titleBar = new BaseTitleBar(view);
        titleBar.setCenterTxt("配送清单");
        titleBar.setLeftBack(this);

        kf_phone.setOnClickListener(this);
        kf_num.setText(Api.KF_PHONE_TXT);

        tv_name.setText(getIntent().getStringExtra("market"));
        orderData = (OrderData)getIntent().getSerializableExtra("orderData");

        List<OrderItemData> itemDatas = orderData.getItems();
        for (int i = 0; i < itemDatas.size(); i++) {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.kf_phone) {
            DeviceUtils.callPhone(this, Api.KF_PHONE);
        } else if(id == R.id.ok_btn) {
            //确认配送

        }
    }
}
