package com.market.courier.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.market.courier.R;
import com.market.courier.response.Api;
import com.market.courier.response.Market;
import com.market.courier.response.OrderData;
import com.market.courier.response.enums.OrderRoleTypeEnum;
import com.market.courier.response.enums.OrderTypeEnum;
import com.market.courier.response.enums.StatusEnterEnum;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<OrderData> manyDatas;
    ManyAdapter adapter;

    int pageIndex = 1;
    Market nowMarket;

    StatusEnterEnum statusEnterEnum;

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
        statusEnterEnum = type == 0 ? StatusEnterEnum.COMPLETE : StatusEnterEnum.COURIER_CANCEL;
        nowMarket = (Market) getIntent().getSerializableExtra("market");

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
                    pageIndex = 1;
                    manyDatas.clear();
                    getDatas();
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    manyDatas.clear();
                    getDatas();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    getDatas();
                }
            }
        });

        refreshLoad.showLoading();
    }

    void getDatas() {

        Map map = new HashMap<>();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("marketId", String.valueOf(this.nowMarket.getId()));
        map.put("roleType", String.valueOf(OrderRoleTypeEnum.COURIER.getKey()));
        map.put("orderType", String.valueOf(OrderTypeEnum.SALE_ORDER_TYPE.getKey()));
        map.put("statusEnter", String.valueOf(statusEnterEnum.getKey()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));

        Http.post(this, map, Api.SHOW_ORDERS, new HttpCallback<List<OrderData>>("datas") {
            @Override
            public void onSuccess(List<OrderData> data) {
                if (data.size() == 0) {
                    if (manyDatas.isEmpty()) {
                        //没有了
                        refreshLoad.showReset("当前没有数据");
                        return;
                    }
                }
                manyDatas.addAll(data);
                adapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() == 0, manyDatas.isEmpty());
            }

            @Override
            public void onFailure() {
                refreshLoad.showError(msg);
            }
        });
    }

    class ManyAdapter extends AfinalAdapter<OrderData> {

        public ManyAdapter(Context context, List<OrderData> manyDatas) {
            super(context, manyDatas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            OrderData manyData = getItem(position);
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

        public void refresh(OrderData manyData) {
            tv_address.setText(manyData.getFormatReceiptAddress());
            tv_order.setText(manyData.getOrderSequenceNumber());
            tv_time.setText(manyData.getPaymentTime());
            if (type == 1) {
                tv_status.setText("已取消");
                tv_status.setTextColor(g_color);
                tv_status.setBackgroundResource(R.drawable.btn_border_grey);
            } else {
                tv_status.setText("已完成");
                tv_status.setTextColor(b_color);
                tv_status.setBackgroundResource(R.drawable.btn_border_blue);
            }
//            String error = manyData.getOrderStatusTitle();
//            if (error != null && error.length() > 0) {
//                tv_error.setText(error);
//                tv_error.setVisibility(View.VISIBLE);
//            } else {
                tv_error.setVisibility(View.GONE);
//            }
        }
    }
}
