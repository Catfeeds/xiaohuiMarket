package com.market.courier.main;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.market.courier.R;
import com.market.courier.response.Api;
import com.market.courier.response.HttpCallbackImpl;
import com.market.courier.response.Market;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
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

    BaseTitleBar titleBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_select_market;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        titleBar = new BaseTitleBar(view);
        titleBar.setLeftBack(this);
        titleBar.setCenterTxt("店铺列表");
        markets = new ArrayList<Market>();
        marketAdapter = new MarketAdapter(this, markets);
        fr_listview.setAdapter(marketAdapter);
        fr_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭
                Market market = markets.get(position);
                Intent intent = new Intent();
                intent.putExtra("market", market);
                setResult(RESULT_OK, intent);
                finish();
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
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("roleType", "1");
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
