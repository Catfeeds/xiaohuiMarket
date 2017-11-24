package com.xiaohui.courier.main;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiaohui.courier.R;
import com.xiaohui.courier.response.Api;
import com.xiaohui.courier.response.HttpCallbackImpl;
import com.xiaohui.courier.response.Market;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
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
public class SelectMarketActivity extends AfinalActivity {

    @Bind(R.id.fr_listview)
    ListView fr_listview;
    //店铺内容
    List<Market> markets;
    MarketAdapter marketAdapter;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    RefreshLoad refreshLoad;

    @Override
    public int getContentViewId() {
        return R.layout.activity_select_market;
    }

    @Override
    public void initView(View view) {

        markets = new ArrayList<Market>();
        marketAdapter = new MarketAdapter(this, markets);
        fr_listview.setAdapter(marketAdapter);
        fr_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭
                Market market = markets.get(position);
            }
        });

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    ptr.setVisibility(View.GONE);
                    loadMarkets();
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    loadMarkets();
                }
            }
        });

        refreshLoad.showLoading();
    }

    //加载所有商店
    void loadMarkets() {
        Map map = new HashMap();
        Http.get(this, map, Api.MARKET_LIST, new HttpCallbackImpl<List<Market>>("datas") {
            @Override
            public void onSuccess(List<Market> datas) {
                //默认选中第一个
                markets.clear();
                markets.addAll(datas);
                marketAdapter.notifyDataSetChanged();
                refreshLoad.complete(false, false);
            }

            @Override
            public void onFailure() {
                refreshLoad.showError(msg);
                if(checkAccessToken()) {
                    return;
                }
            }
        });
    }
}
