package com.xiaohui.courier.main;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaohui.courier.R;
import com.xiaohui.courier.response.ManyData;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public class ActivityMany extends AfinalActivity {

    //类型
    int type;

    int b_color;
    int g_color;

    BaseTitleBar titleBar;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    RefreshLoad refreshLoad;
    List<ManyData> manyDatas;
    ManyAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_many;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();

        b_color = getResources().getColor(R.color.blue);
        g_color = getResources().getColor(R.color.font_9);

        type = getIntent().getIntExtra("type", 0);

        titleBar = new BaseTitleBar(view);
        titleBar.setLeftBack(this);
        titleBar.setCenterTxt(type == 0 ? "已完成" : "已取消");

        manyDatas = new ArrayList<>();
        adapter = new ManyAdapter(this, manyDatas);
        listView.setAdapter(adapter);

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {

            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    ptr.setVisibility(View.GONE);
                    getDatas();
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    getDatas();
                }
            }
        });
    }

    void getDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ManyData manyData = new ManyData();
                    manyData.setAddress("天河广州");
                    manyData.setError(type == 1 ? "取消原因：未知" : "");
                    manyData.setTime("2017-10-10");
                    manyData.setOrderId("56461321");
                    manyDatas.add(manyData);
                }
                adapter.notifyDataSetChanged();
                refreshLoad.complete(false, false);
            }
        }, 1000);
    }

    class ManyAdapter extends AfinalAdapter<ManyData> {

        public ManyAdapter(Context context, List<ManyData> manyDatas) {
            super(context, manyDatas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ManyData manyData = getItem(position);
            Holder holder = null;

            if (convertView == null) {
                convertView = View.inflate(ActivityMany.this, R.layout.adapter_many, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.refresh(manyData);

            return convertView;
        }
    }

    class Holder {

        TextView tv_address;
        TextView tv_order;
        TextView tv_time;
        TextView tv_status;
        TextView tv_error;

        public Holder(View view) {
            tv_address = ViewUtils.find(view, R.id.tv_address);
            tv_order = ViewUtils.find(view, R.id.tv_orderid);
            tv_time = ViewUtils.find(view, R.id.tv_time);
            tv_status = ViewUtils.find(view, R.id.iv_img);
            tv_error = ViewUtils.find(view, R.id.tv_cal);
        }

        public void refresh(ManyData manyData) {
            tv_address.setText(manyData.getAddress());
            tv_order.setText(manyData.getOrderId());
            tv_time.setText(manyData.getTime());
            if (type == 1) {
                tv_status.setText("已取消");
                tv_status.setTextColor(g_color);
                tv_status.setBackgroundResource(R.drawable.btn_border_grey);
            } else {
                tv_status.setText("已完成");
                tv_status.setTextColor(b_color);
                tv_status.setBackgroundResource(R.drawable.btn_border_blue);
            }
            String error = manyData.getError();
            if (error != null && error.length() > 0) {
                tv_error.setText(error);
                tv_error.setVisibility(View.VISIBLE);
            } else {
                tv_error.setVisibility(View.GONE);
            }
        }
    }
}
