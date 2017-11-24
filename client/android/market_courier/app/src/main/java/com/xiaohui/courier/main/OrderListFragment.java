package com.xiaohui.courier.main;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.xiaohui.courier.R;
import com.xiaohui.courier.response.Market;
import com.xiaohui.courier.response.OrderData;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by zhumg on 9/22.
 */
public class OrderListFragment extends AfinalFragment {

    //当前选中的商店
    Market nowMarket;
    //将要替换的商店
    Market changeMarket;

    @Bind(R.id.fr_listview)
    ListView listview;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    int pageIndex = 1;

    RefreshLoad refreshLoad;
    List<OrderData> orderDatas;
    OrderAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ptr;
    }

    @Override
    protected void initViewData(View view) {
        orderDatas = new ArrayList<>();
        adapter = new OrderAdapter(this.getActivity(), orderDatas);
        listview.setAdapter(adapter);

        refreshLoad = new RefreshLoad(this.getActivity(), this.ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    ptr.setVisibility(View.GONE);
                    pageIndex = 1;
                    getDatas();
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
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

    //刷新数据
    public void refreshDatas() {

    }

    public void setChangeMarket(Market market) {
        this.changeMarket = market;
    }

    void getDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                orderDatas.clear();
                for (int i = 0; i < 10; i++) {
                    OrderData od = new OrderData();
                    od.setAddress("地址啪啪啪");
                    od.setOrderId("123123123");
                    od.setTime("2017-10-10");
                    orderDatas.add(od);
                }
                adapter.notifyDataSetChanged();
                refreshLoad.complete(false, false);
            }
        }, 1000);
    }
}
