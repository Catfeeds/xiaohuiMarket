package com.xiaohui.courier.main;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xiaohui.courier.R;
import com.xiaohui.courier.response.Api;
import com.xiaohui.courier.response.HttpCallbackImpl;
import com.xiaohui.courier.response.Market;
import com.xiaohui.courier.response.event.LoginEvent;
import com.xiaohui.courier.widget.TitleViewPager;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
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

    LoadingView loadingView;

    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();

    Market selectMarket;

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

        fragments.add(new OrderListFragment());
        fragments.add(new OrderListFragment());
        fragments.add(new OrderListFragment());
        fragments.add(new MonyFragment());

        titleViewPager = new TitleViewPager(view, getSupportFragmentManager(), fragments, DeviceUtils.screenWidth(this));

        //如果登录
        String accessToken = SpUtils.loadValue("accessToken");
        if (accessToken != null && accessToken.length() > 0) {
            //直接访问拿店铺数据
            loadMarkets();
        } else {
            //TODO 暂时不显示登录
            //startActivityForResult(new Intent(this, LoginActivity.class), 1);
            //return;
            loadingView.hibe();
            cpane.setVisibility(View.VISIBLE);
        }

        //检测版本
        UpdateManager.httpCheckUpdate(this, false);
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
    public void onEventMainThread(LoginEvent event) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == 100) {
            //刷新 标题
            changeMarket((Market) data.getSerializableExtra("market"));
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
        //刷新 界面
        Fragment fragment = titleViewPager.getNowFragment();
        if(fragment instanceof OrderListFragment) {
            ((OrderListFragment) fragment).refreshDatas();
        }
    }

    //加载所有商店
    void loadMarkets() {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
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
            }
        });
    }
}
