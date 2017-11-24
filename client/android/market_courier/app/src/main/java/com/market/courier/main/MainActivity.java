package com.market.courier.main;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.market.courier.LoginActivity;
import com.market.courier.R;
import com.market.courier.response.Api;
import com.market.courier.response.HttpCallbackImpl;
import com.market.courier.response.Market;
import com.market.courier.response.Passport;
import com.market.courier.response.enums.StatusEnterEnum;
import com.market.courier.response.event.RefreshEvent;
import com.market.courier.widget.JpushAliasUtil;
import com.market.courier.widget.TitleViewPager;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Callback;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.mvc.LoadingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 9/22.
 */
public class MainActivity extends AfinalActivity implements View.OnClickListener {

    @Bind(R.id.title_left)
    View title_left;

    @Bind(R.id.title_right)
    View title_right;

    @Bind(R.id.title_center)
    View title_center;

    @Bind(R.id.title_center_txt)
    TextView title_center_txt;

    @Bind(R.id.title_status)
    TextView title_status;

    @Bind(R.id.cpane)
    View cpane;

    @Bind(R.id.base_drawer)
    DrawerLayout mDrawerLayout;

    /////////////////////////////
    @Bind(R.id.ll_detailinfo)
    View ll_detailinfo;
    @Bind(R.id.ll_work)
    View ll_work;
    @Bind(R.id.ll_Market_development)
    View ll_Market_development;

    LoadingView loadingView;

    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();

    Market selectMarket;

    RefreshAccessToken refreshAccessToken;
    MonyFragment monyFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        loadingView = new LoadingView(view);
        cpane.setVisibility(View.GONE);

        EventBus.getDefault().register(this);

        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_center.setOnClickListener(this);

        title_center_txt.setText("店铺");

        fragments.add(OrderListFragment.createOrderListFragment(StatusEnterEnum.COURIER_WAIT_ACCEPT));
        fragments.add(OrderListFragment.createOrderListFragment(StatusEnterEnum.COURIER_WAIT_PICK_UP));
        fragments.add(OrderListFragment.createOrderListFragment(StatusEnterEnum.COURIER_DELIVER));
        monyFragment = new MonyFragment();
        fragments.add(monyFragment);

        titleViewPager = new TitleViewPager(view, getSupportFragmentManager(), fragments, DeviceUtils.screenWidth(this));


        boolean login = getIntent().getBooleanExtra("login", false);

        //如果未登录
        if(!login) {

            String accessToken = SpUtils.loadValue("accessToken");

            if (accessToken != null && accessToken.length() > 0) {
                //取passport
                Api.passport = SpUtils.loadJson("passport", Passport.class);
                //无法改变，则重新登陆
                if (Api.passport == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class), 1);
                    return;
                }
                //赋值
                Callback.ACCESS_KEY = accessToken;
                Callback.PASSPORT_ID = Api.passport.getPassportIdStr();
                //注册极光
                JpushAliasUtil.setJpushAlias(this, Api.passport.getPassportIdStr());
                //直接访问拿店铺数据
                loadMarkets();

            } else {
                //TODO 暂时不显示登录
                startActivityForResult(new Intent(this, LoginActivity.class), 1);
                return;
            }

        } else {
            loadMarkets();
            //登陆成功
            refreshAccessToken = new RefreshAccessToken();
            refreshAccessToken.start(this);
            //检测版本
            UpdateManager.checkUpdate(this, false, Api.passport.getVersionMessage(), null);
        }

    }

    int num = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (num == 1) {
                ++num;
                countDownTimer.start();
                ToastUtil.showToast(MainActivity.this, "再按一次退出应用");
            } else {
                ActivityManager.AppExit(getApplicationContext());
            }
        }
        return false;
    }

    private CountDownTimer countDownTimer = new CountDownTimer(3000, 3000) {

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            num = 1;
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_left) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.title_center) {
            startActivityForResult(new Intent(this, SelectMarketActivity.class), 100);
        } else if (id == R.id.title_right) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        if (event instanceof RefreshEvent) {
            //刷新 界面
            Fragment fragment = titleViewPager.getNowFragment();
            if (fragment instanceof OrderListFragment) {
                ((OrderListFragment) fragment).refresh();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == 100) {
            //刷新 标题
            changeMarket((Market) data.getSerializableExtra("market"));
        } else if(requestCode == 1) {
            //登陆成功
            refreshAccessToken = new RefreshAccessToken();
            refreshAccessToken.start(this);

            loadingView.showLoading();
            loadMarkets();

            //检测版本
            UpdateManager.checkUpdate(this, false, Api.passport.getVersionMessage(), null);
        }
    }

    void changeMarket(Market market) {
        //刷新 标题
        selectMarket = market;
        //刷新标题
        if (selectMarket.isEdit()) {
            title_status.setVisibility(View.VISIBLE);
        } else {
            title_status.setVisibility(View.GONE);
        }
        title_center_txt.setText(selectMarket.getName());
        //切换所有的界面
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            if (fragment instanceof OrderListFragment) {
                ((OrderListFragment) fragment).setChangeMarket(selectMarket);
            }
        }
        //刷新 界面
        Fragment fragment = titleViewPager.getNowFragment();
        if (fragment instanceof OrderListFragment) {
            ((OrderListFragment) fragment).refreshDatas();
        }

        monyFragment.setMarket(market);
    }

    //加载所有商店
    void loadMarkets() {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("roleType", "1");
        Http.get(this, map, Api.MARKET_LIST, new HttpCallbackImpl<List<Market>>("datas") {
            @Override
            public void onSuccess(List<Market> datas) {
                if (datas != null && datas.size() > 0) {
                    //先显示界面
                    cpane.setVisibility(View.VISIBLE);
                    loadingView.hibe();
                    changeMarket(datas.get(0));
                }
            }

            @Override
            public void onFailure() {
                if (checkAccessToken()) {
                    return;
                }
                final int cc = code;
                loadingView.showReset(msg);
                loadingView.setResetListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cc == 100) {
                            //需要重新登陆
                            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 1);
                        } else {
                            loadingView.showLoading();
                            loadMarkets();
                        }
                    }
                });
            }
        });
    }
}
