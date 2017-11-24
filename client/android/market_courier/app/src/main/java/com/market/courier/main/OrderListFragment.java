package com.market.courier.main;

import android.view.View;
import android.widget.ListView;

import com.market.courier.R;
import com.market.courier.response.Api;
import com.market.courier.response.Market;
import com.market.courier.response.OrderData;
import com.market.courier.response.enums.OrderRoleTypeEnum;
import com.market.courier.response.enums.OrderTypeEnum;
import com.market.courier.response.enums.StatusEnterEnum;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    StatusEnterEnum statusEnterEnum;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ptr;
    }

    @Override
    protected void initViewData(View view) {
        orderDatas = new ArrayList<>();
        adapter = new OrderAdapter(this, orderDatas, statusEnterEnum);
        listview.setAdapter(adapter);

        refreshLoad = new RefreshLoad(this.getActivity(), this.ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    ptr.setVisibility(View.GONE);
                    orderDatas.clear();
                    pageIndex = 1;
                    getDatas();
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    orderDatas.clear();
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

    @Override
    public void onVisible() {
        if(changeMarket == null) {
            return;
        }
        if(nowMarket == null || nowMarket.getId() != changeMarket.getId()) {
            nowMarket = changeMarket;
            //重置
            refreshLoad.showLoading();
        }
    }

    public Market getNowMarket() {
        return nowMarket;
    }

    public void setChangeMarket(Market market) {
        this.changeMarket = market;
    }

    //刷新数据
    public void refreshDatas() {
        if(nowMarket == null || nowMarket.getId() != changeMarket.getId()) {
            nowMarket = changeMarket;
            //重置
            if(refreshLoad == null) {
                return;
            }
            refreshLoad.showLoading();
        }
    }

    public void refresh() {
        //重置
        if(refreshLoad == null) {
            return;
        }
        refreshLoad.showLoading();
    }

    void getDatas() {

        if(this.nowMarket == null) {
            refreshLoad.showReset("当前没有数据");
            return;
        }

        Map map = new HashMap<>();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("marketId", String.valueOf(this.nowMarket.getId()));
        map.put("roleType", String.valueOf(OrderRoleTypeEnum.COURIER.getKey()));
        map.put("orderType", String.valueOf(OrderTypeEnum.SALE_ORDER_TYPE.getKey()));
        map.put("statusEnter", String.valueOf(statusEnterEnum.getKey()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));

        Http.post(this.getActivity(), map, Api.SHOW_ORDERS, new HttpCallback<List<OrderData>>("datas") {
            @Override
            public void onSuccess(List<OrderData> data) {
                if (data.size() == 0) {
                    if (orderDatas.isEmpty()) {
                        //没有了
                        refreshLoad.showReset("当前没有数据");
                        return;
                    }
                }
                orderDatas.addAll(data);
                adapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() == 0, orderDatas.isEmpty());
            }

            @Override
            public void onFailure() {
                refreshLoad.showError(msg);
            }
        });
    }

    public static OrderListFragment createOrderListFragment(StatusEnterEnum statusEnterEnum) {
        OrderListFragment fragment = new OrderListFragment();
        fragment.statusEnterEnum = statusEnterEnum;
        return fragment;
    }
}
