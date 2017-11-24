package com.xiaohui.replenishment;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaohui.replenishment.response.Api;
import com.xiaohui.replenishment.response.HttpCallbackImpl;
import com.xiaohui.replenishment.response.Market;
import com.xiaohui.replenishment.response.Passport;
import com.xiaohui.replenishment.response.Task;
import com.xiaohui.replenishment.response.enums.ClientTypeEnum;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Callback;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.utils.ApkUtils;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by zhumg on 8/28.
 */
public class MainActivity extends AfinalActivity implements View.OnClickListener {

    //拍摄权限
    static final String[] CAMERA_PERMISSIONS = new String[]{
            android.Manifest.permission.CAMERA,
    };

    @Bind(R.id.tv_no)
    TextView tv_no;
    @Bind(R.id.tv_ok)
    TextView tv_ok;
    @Bind(R.id.iv_video)
    ImageView iv_video;

    @Bind(R.id.view_mask_bg)
    View view_mask_bg;

    @Bind(R.id.ttask_btn)
    View ttask_btn;

    @Bind(R.id.market_listview)
    ListView market_listview;
    //店铺内容
    List<Market> markets;
    MarketAdapter marketAdapter;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView task_listview;
    //任务
    List<Task> tasks;
    TaskAdapter taskAdapter;

    @Bind(R.id.as_list_layout)
    View list_layout;

    @Bind(R.id.txt_username)
    TextView txt_username;

    @Bind(R.id.title_status)
    View title_status;

    @Bind(R.id.base_drawer)
    DrawerLayout mDrawerLayout;

    BaseTitleBar titleBar;

    RefreshLoad refreshLoad;

    //当前列表
    int nowIndex = 0;
    //当前选中的店铺
    Market selectMarket;

    //加载店铺列表标志
    boolean initMarketBool = false;

    //任务页码
    int taskPageIndex = 1;

    //加载中标志
    boolean loadTaskBool = false;

    //登陆标志, true代表已登录，false代表未登录
    boolean login = true;

    //未检测
    boolean checkTaskBool = false;

    Dialog updateStatusDialog;

    RefreshAccessToken refreshAccessToken;

    //按日期区分的
    private Map<String, String> dataTitle = new HashMap<>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();
        titleBar = new BaseTitleBar(view);
        titleBar.setLeftImg(R.drawable.ico_user);
        titleBar.setLeftListener(this);
        titleBar.setRightImg(R.drawable.icon_setting, this);
        view.findViewById(R.id.title_center).setOnClickListener(this);

        tv_no.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        iv_video.setOnClickListener(this);
        view_mask_bg.setOnClickListener(this);
        ttask_btn.setOnClickListener(this);

        //默认先隐藏
        ttask_btn.setVisibility(View.GONE);

        Drawable nod = getResources().getDrawable(
                R.drawable.selector_tab_no);
        nod.setBounds(0, 0, DeviceUtils.dip2px(this, 22), DeviceUtils.dip2px(this, 22));
        tv_no.setCompoundDrawables(null, nod, null, null);

        Drawable okd = getResources().getDrawable(
                R.drawable.selector_tab_ok);
        okd.setBounds(0, 0, DeviceUtils.dip2px(this, 22), DeviceUtils.dip2px(this, 22));
        tv_ok.setCompoundDrawables(null, okd, null, null);

        switchTab(0);

        markets = new ArrayList<Market>();
        marketAdapter = new MarketAdapter(this, markets);
        market_listview.setAdapter(marketAdapter);
        market_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭
                if (loadTaskBool) {
                    ToastUtil.showToast(MainActivity.this, "数据加载中，请稍后...");
                    return;
                }
                hideMarketList();
                setSelectMarket(markets.get(position));
            }
        });

        tasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, tasks, 0);
        task_listview.setAdapter(taskAdapter);

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    ptr.setVisibility(View.GONE);
                    if (!login) {
                        //未登录，则去登录
                        gotoLogin();
                    } else if (!initMarketBool) {
                        //如果未加载店铺，加载店铺
                        titleBar.setCenterTxt("加载店铺中...");
                        loadMarkets();
                    } else {
                        clearTaskDatas();
                        loadTasks();
                    }
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    clearTaskDatas();
                    loadTasks();
                }
            }
        });

        updateStatusDialog = DialogUtils.createListDialog(this, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateStatusDialog.dismiss();
                int targetStatus = position == 0 ? 1 : 4;//1正常，4维护
                updateMarketStatus(targetStatus);
            }
        }, new BaseAdapter() {

            private String names[] = {"切换店铺状态 -> 正常", "切换店铺状态 -> 维护"};

            @Override
            public int getCount() {
                return names.length;
            }

            @Override
            public Object getItem(int position) {
                return names[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = null;
                if (convertView == null) {
                    convertView = View.inflate(MainActivity.this,
                            R.layout.widget_line_txt, null);
                }
                tv = (TextView) convertView.findViewById(R.id.wlt_txt);
                tv.setText(getItem(position).toString());
                return convertView;
            }
        });
        updateStatusDialog.setCanceledOnTouchOutside(true);

        String name = SpUtils.loadValue("name");
        if (name != null && name.length() > 0) {
            login = false;
            refreshLoad.showLoading("登陆中");
        } else {
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
            return;
        }

        getPersimmions(this, CAMERA_PERMISSIONS, 1);
    }

    void clearTaskDatas() {
        taskPageIndex = 1;
        tasks.clear();
        dataTitle.clear();
    }

    //更改店铺状态
    void updateMarketStatus(final int targetStatus) {
        if (selectMarket == null) {
            ToastUtil.showToast(this, "当前没有选择店铺");
            return;
        }
        if (selectMarket.getStatus() == targetStatus) {
            ToastUtil.showToast(this, "当前状态一致，不需要切换");
            return;
        }
        //通知修改
        Map map = new HashMap<>();
        map.put("marketId", String.valueOf(selectMarket.getId()));
        map.put("beforeStatus", String.valueOf(selectMarket.getStatus()));
        map.put("status", String.valueOf(targetStatus));

        httpPost(map, Api.UPDATE_MARKET_STATUS, new HttpCallbackImpl<Integer>("status") {
            @Override
            public void onSuccess(Integer data) {
                //修改完成，重新设置
                selectMarket.changeStatus(data.intValue());
                changeMarketTitleStatus();
                ToastUtil.showToast(MainActivity.this, "状态切换成功");
            }

            @Override
            public void onFailure() {
                if (checkAccessToken()) {
                    return;
                }
                super.onFailure();
            }
        });
    }

    //检测当天是否需要提交所有任务
    void checkAllTask() {
        Map map = new HashMap<>();
        map.put("marketId", String.valueOf(selectMarket.getId()));
        //登陆
        Http.post(this, map, Api.FIND_VALID_TASKS, new HttpCallbackImpl() {
            @Override
            public void onSuccess(Object data) {
                ttask_btn.setVisibility(View.GONE);
            }

            @Override
            public void onFailure() {
                ttask_btn.setVisibility(View.VISIBLE);
            }
        });
    }

    //提交今日所有任务
    void submitDayAllTask() {
        Map map = new HashMap<>();
        map.put("marketId", String.valueOf(selectMarket.getId()));
        //登陆
        httpPost(map, Api.FIND_VALID_TASKS, new HttpCallbackImpl() {
            @Override
            public void onSuccess(Object data) {
                closeLoadingDialog();
                ttask_btn.setVisibility(View.GONE);
                ToastUtil.showToast(MainActivity.this, "今日任务已全部完成");
            }

            @Override
            public void onFailure() {
                if (checkAccessToken()) {
                    return;
                }
                if (code == 2017) {//2017代表需要提交当日任务
                    if (tasks.size() > 0) {
                        //让用户输入一个信息
                        closeLoadingDialog();
                        startActivityForResult(new Intent(MainActivity.this, InputActivity.class).putExtra("marketId", selectMarket.getId()), 200);
                    } else {
                        //直接提交
                        submit();
                    }
                } else {
                    super.onFailure();
                }
            }
        });
    }

    void submit() {
        //通知修改
        Map map = new HashMap<>();
        map.put("marketId", String.valueOf(selectMarket.getId()));
        //登陆
        httpPost(map, Api.FINISH_DAY_TASK, new HttpCallbackImpl() {
            @Override
            public void onSuccess(Object data) {
                closeLoadingDialog();
            }

            @Override
            public void onFailure() {
                closeLoadingDialog();
                if (checkAccessToken()) {
                    return;
                }
            }
        });
    }

    void changeMarketTitleStatus() {
        if (selectMarket.isEdit()) {
            title_status.setVisibility(View.VISIBLE);
        } else {
            title_status.setVisibility(View.GONE);
        }
    }

    //点击 选中 某个店铺
    void setSelectMarket(Market market) {
        //如果选中的是原来的
        if (selectMarket != null && selectMarket.getId() == market.getId()) {
            return;
        }
        //设置新的
        if (selectMarket != null) {
            selectMarket.setSelected(false);
        }
        selectMarket = market;
        selectMarket.setSelected(true);
        changeMarketTitleStatus();
        //设置标题
        titleBar.setCenterTxt(selectMarket.getName());
        //刷新 界面
        refreshLoad.showLoading();
    }

    //登录
    void gotoLogin() {
        final String name = SpUtils.loadValue("name");
        final String password = SpUtils.loadValue("pass");
        Map map = new HashMap<>();
        map.put("username", name);
        map.put("password", password);
        map.put("clientType", String.valueOf(ClientTypeEnum.WAREHOUSE.getKey()));
        map.put("versionIndex", String.valueOf(ApkUtils.getVersionCode(this)));
        //登陆
        httpPost(map, Api.LOGIN, new HttpCallbackImpl<Passport>() {
            @Override
            public void onSuccess(Passport data) {

                SpUtils.saveValue("name", name);
                SpUtils.saveValue("pass", password);


                Api.passport = data;
                Api.passport.setLoginName(name);

                Callback.ACCESS_KEY = data.getAccessToken();
                Callback.PASSPORT_ID = Api.passport.getPassportIdStr();

                //检查版本
                UpdateManager.checkUpdate(MainActivity.this, false, Api.passport.getVersionMessage(), null);

                //刷新 token
                refreshAccessToken = new RefreshAccessToken();
                refreshAccessToken.start(MainActivity.this);

                txt_username.setText(name);

                login = true;

                //检测
                loadMarkets();
            }

            @Override
            public void onFailure() {
                refreshLoad.showError(msg);
            }
        });
    }

    //加载所有任务
    void loadTasks() {
        //加载任务，清除原来的
        loadTaskBool = true;
        Map map = new HashMap();
        map.put("status", String.valueOf(nowIndex));
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("pageIndex", String.valueOf(taskPageIndex));
        map.put("pageSize", String.valueOf(10));
        map.put("marketId", String.valueOf(selectMarket.getId()));
        Http.get(this, map, Api.TASK_LIST, new HttpCallbackImpl<List<Task>>("datas") {
            @Override
            public void onSuccess(List<Task> datas) {
                //刷新
                for (int i = 0; i < datas.size(); i++) {
                    Task task = datas.get(i);
                    if (dataTitle.get(task.getDateTitle()) == null) {
                        //添加一个标题
                        Task t = new Task();
                        t.setUiType(1);
                        t.setItemName(task.getDateTitle());
                        t.setUnitName(task.getHopeExecutorDate());
                        dataTitle.put(task.getDateTitle(), task.getDateTitle());
                        tasks.add(t);
                    }
                    tasks.add(task);
                }

                taskAdapter.notifyDataSetChanged();
                refreshLoad.complete(true, taskAdapter.isEmpty());
                loadTaskBool = false;

                //检测一次
                if (nowIndex == 0) {
                    checkAllTask();
                } else {
                    ttask_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {
                loadTaskBool = false;
                refreshLoad.showError(msg);

                //检测一次
                checkAllTask();

                if (checkAccessToken()) {
                    return;
                }
            }
        });
    }

    //加载所有商店
    void loadMarkets() {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        Http.get(this, map, Api.MARKET_LIST, new HttpCallbackImpl<List<Market>>("datas") {
            @Override
            public void onSuccess(List<Market> datas) {
                //默认选中第一个
                if (datas.size() > 0) {
                    markets.addAll(datas);
                    marketAdapter.notifyDataSetChanged();
                    selectMarket = markets.get(0);
                    changeMarketTitleStatus();
                    titleBar.setCenterTxt(selectMarket.getName());
                    //加载
                    initMarketBool = true;
                    loadTasks();
                } else {
                    titleBar.setCenterTxt("未绑定店铺");
                    refreshLoad.showError("你还没有绑定任何店铺");
                }
            }

            @Override
            public void onFailure() {
                refreshLoad.showError(msg);
                if (checkAccessToken()) {
                    return;
                }
            }
        });
    }

    //切换tab
    void switchTab(int index) {
        if (index < 0 || index > 1)
            return;
        tv_no.setSelected(index == 0);
        tv_ok.setSelected(index == 1);
        handlerTab(index);
    }

    //处理点击
    void handlerTab(int index) {
        if (nowIndex == index) {
            return;
        }
        nowIndex = index;
        taskAdapter.changeTaskType(nowIndex);
        //加载任务
        refreshLoad.showLoading();
    }

    int panelHeight = 0;
    boolean showList = false;

    void showMarketList() {
        showList = true;
        view_mask_bg.setVisibility(View.VISIBLE);
        list_layout.setVisibility(View.VISIBLE);
        list_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                list_layout.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                panelHeight = list_layout.getHeight();
                Log.e("jzht", "panelHeight=" + panelHeight);
                ObjectAnimator
                        .ofFloat(list_layout, "translationY",
                                -panelHeight, 0).setDuration(200)
                        .start();
            }
        });
        //刷新 界面
        marketAdapter.notifyDataSetChanged();
    }

    void hideMarketList() {
        showList = false;
        view_mask_bg.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(list_layout, "translationY", 0, -panelHeight)
                .setDuration(200).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (refreshAccessToken != null) {
            refreshAccessToken.close();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_left) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.title_center) {
            //显示列表
            if (!showList) {
                showMarketList();
            } else {
                hideMarketList();
            }
        } else if (id == R.id.title_right) {
            //修改状态
            if (selectMarket == null) {
                return;
            }
            //修改状态
            if (showList) {
                hideMarketList();
            }
            updateStatusDialog.show();
        } else if (id == R.id.tv_no) {
            if (loadTaskBool) {
                ToastUtil.showToast(this, "数据加载中，请稍后...");
                return;
            }
            switchTab(0);
        } else if (id == R.id.tv_ok) {
            if (loadTaskBool) {
                ToastUtil.showToast(this, "数据加载中，请稍后...");
                return;
            }
            switchTab(1);
        } else if (id == R.id.view_mask_bg) {
            if (showList) {
                hideMarketList();
            }
        } else if (id == R.id.iv_video) {
            Intent intent = new Intent(getApplication(), ScodeActivity.class);
            intent.putExtra("marketId", selectMarket.getId());
            startActivityForResult(intent, 100);
        } else if (id == R.id.ttask_btn) {
            submitDayAllTask();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            //登陆成功标志
            login = true;
            refreshLoad.showLoading();
            //检查版本
            UpdateManager.checkUpdate(MainActivity.this, false, Api.passport.getVersionMessage(), null);
            //刷新 token
            refreshAccessToken = new RefreshAccessToken();
            refreshAccessToken.start(MainActivity.this);

        } else if (requestCode == 100) {
            //需要刷新的标志,扫码,输入异常信息
            refreshLoad.showLoading();
        } else if (requestCode == 200) {
            //提交所有任务,输入异常信息
            refreshLoad.showLoading();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onPermissionsResult(this, requestCode, permissions, grantResults);
    }


    //6.0系统以后部分权限要自动获取
    @TargetApi(23)
    public static boolean getPersimmions(Activity activity, String permission[], int request_code) {
        if (Build.VERSION.SDK_INT < 23) {
            // TODO
            return true;
        }
        ArrayList<String> strs = new ArrayList<>();
        for (int i = 0; i < permission.length; i++) {
            if (activity.checkSelfPermission(permission[i]) != PackageManager.PERMISSION_GRANTED) {
                strs.add(permission[i]);
            }
        }
        if (strs.size() < 1) {
            return true;
        }
        String ps[] = new String[strs.size()];
        for (int i = 0; i < strs.size(); i++) {
            ps[i] = strs.get(i);
        }
        //请求
        activity.requestPermissions(ps, request_code);
        return false;
    }

    //6.0系统，处理获取权限结果
    public static void onPermissionsResult(final Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //加入提示
                    String s = getPermissionName(permissions[i]);
                    if (s.length() > 1) {
                        sb.append(s).append("\n");
                    }
                }
            }
            String s = sb.toString();
            if (s.length() > 0) {
                TipDialog tipDialog = new TipDialog(activity);
                tipDialog.setTipClickListener(new TipClickListener() {
                    @Override
                    public void onClick(boolean left) {
                        if (left) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            activity.startActivity(intent);
                        }
                    }
                });
                tipDialog.setContentMsg("部分功能需要获取以下权限\n\n" + s);
                tipDialog.setLeftBtn("设置权限");
                tipDialog.setRightBtn("取消");
                tipDialog.show();
            }
        }
    }

    public static String getPermissionName(String s) {
        if ("android.permission.ACCESS_COARSE_LOCATION".equals(s)) {
            return "位置信息权限";
        }
        if ("android.permission.CAMERA".equals(s)) {
            return "相机权限";
        }
        if ("android.permission.READ_EXTERNAL_STORAGE".equals(s) || "android.permission.WRITE_EXTERNAL_STORAGE".equals(s)) {
            return "读写SD卡权限";
        }
        return "";
    }

}
