package com.xiaohui.replenishment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaohui.replenishment.response.Api;
import com.xiaohui.replenishment.response.HttpCallbackImpl;
import com.xiaohui.replenishment.response.Task;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;
import com.zxinglib.activity.CaptureViewHandler;
import com.zxinglib.activity.CodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author hellozmg
 *         重写的扫码Activity
 */
public class ScodeActivity extends AfinalActivity implements View.OnClickListener {

    CaptureViewHandler captureViewHandler;
    BaseTitleBar titleBar;

    @Bind(R.id.info_view)
    View info_view;

    @Bind(R.id.clipId)
    TextView clipId;

    @Bind(R.id.itemName)
    TextView itemName;

    @Bind(R.id.itemCount)
    TextView itemCount;

    @Bind(R.id.hasItemCount)
    TextView hasItemCount;

    @Bind(R.id.itemCode)
    TextView itemCode;

    @Bind(R.id.clip_simg)
    ImageView clip_simg;

    @Bind(R.id.item_simg)
    ImageView item_simg;

    @Bind(R.id.clip_ll)
    View clip_ll;

    @Bind(R.id.item_ll)
    View item_ll;

    @Bind(R.id.task_status)
    TextView task_status;

    @Bind(R.id.maxItemCount)
    TextView maxItemCount;

    @Bind(R.id.red_line_ll)
    View red_line_ll;

    @Bind(R.id.red_line_input)
    EditText red_line_input;

    Dialog taskListDailog;

    //是否已扫商品
    boolean isHaveItem = false;

    //是否已扫货架
    boolean isHaveClip = false;

    //商品库存
    int itemStock;

    //扫码后，是否正在网络处理
    boolean scan_bool = false;

    //商品条码
    String scode_itemCode = "6928459921384";//"6926475201312";//"6901285991219";
    //货架条码
    String scode_clipCode = "01010101";//"01010101";//"6921294305401";

    //商品和货架是否一样
    boolean itemClipCodeBool = false;
    //条码相同时， 是否选择了扫货架
    boolean selectScodeClip = false;

    long marketId;

    List<Task> tasks = new ArrayList<>();
    //当前处理的任务
    Task nowTask = null;

    //是否以红外线进行
    boolean red_line_bool = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_scode;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        marketId = getIntent().getLongExtra("marketId", 0);

        titleBar = new BaseTitleBar(view);
        titleBar.setLeftImg(R.drawable.ico_back);
        titleBar.setLeftBack(this);
        titleBar.setCenterTxt("扫码补货");

//          点击 测试
//        titleBar.setCenterTxt("扫商品", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //扫商品
//                scode(scode_itemCode);
//            }
//        });
//        titleBar.setRightImg(R.drawable.ico_back, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //扫货架
//                scode(scode_clipCode);
//            }
//        });

        captureViewHandler = new CaptureViewHandler();
        captureViewHandler.create(this);
        captureViewHandler.initView(view);
        captureViewHandler.setAnalyzeCallback(analyzeCallback);
        captureViewHandler.setCameraInitCallBack(new CaptureViewHandler.CameraInitCallBack() {
            @Override
            public void callBack(Exception e) {
                if (e == null) {

                } else {
                    startRedLine();
                }
            }
        });

        //先隐藏
        info_view.setVisibility(View.GONE);
        //隐藏选中
        clip_simg.setVisibility(View.GONE);
        item_simg.setVisibility(View.GONE);

        clip_ll.setOnClickListener(this);
        item_ll.setOnClickListener(this);

        taskListDailog = DialogUtils.createListDialog(this, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskListDailog.dismiss();
                setNowTask(tasks.get(position));
                if (itemClipCodeBool) {
                    //显示
                    changeImageView(false);
                    clip_simg.setVisibility(View.VISIBLE);
                    item_simg.setVisibility(View.VISIBLE);
                    DialogUtils.createTipDialogOk(ScodeActivity.this, "由于当前任务条码相同，默认先扫商品，如果需要扫货架，请在上面选择！", new TipClickListener() {
                        @Override
                        public void onClick(boolean left) {
                            //使其继续扫描
                            resetCapture();
                        }
                    }).show();
                } else {
                    //使其继续扫描
                    resetCapture();
                }
            }
        }, new BaseAdapter() {
            @Override
            public int getCount() {
                return tasks.size();
            }

            @Override
            public Object getItem(int position) {
                Task task = tasks.get(position);
                return task.getItemName() + "  " + task.getItemQuantity() + task.getUnitName() + "\n" + "货架位置：" + task.getLocationCode();
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = null;
                if (convertView == null) {
                    convertView = View.inflate(ScodeActivity.this,
                            R.layout.widget_line_txt, null);
                }
                tv = (TextView) convertView.findViewById(R.id.wlt_txt);
                tv.setText(getItem(position).toString());
                return convertView;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureViewHandler.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureViewHandler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureViewHandler.onDestroy();
    }

    // 红外线扫描
    void startRedLine() {
        ToastUtil.showToast(ScodeActivity.this, "摄像头初始化失败，以红外线进行扫描");
        red_line_bool = true;
        red_line_ll.setVisibility(View.VISIBLE);
        captureViewHandler.hideCapture();
        red_line_input.requestFocus();
        red_line_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                scode(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            scode(result);
        }

        @Override
        public void onAnalyzeFailed() {

        }
    };

    /**
     * 设置当前任务
     * 如果是上架任务，先扫商品进行搜索的，所以直接商品+1
     * 如果是下架任务，先扫货架进行搜索的，所以直接 已扫货架=true
     *
     * @param task
     */
    void setNowTask(Task task) {
        nowTask = task;
        ITEM_STOCK_MAX = task.getItemQuantity();
        scode_itemCode = task.getBarcode();
        scode_clipCode = task.getLocationCode();
        itemClipCodeBool = scode_itemCode.equals(scode_clipCode);
        hasItemCount.setText(String.valueOf(nowTask.getHasCompleteQuantity()));
        maxItemCount.setText("/" + String.valueOf(ITEM_STOCK_MAX) + " " + task.getUnitName());
        clipId.setText(String.valueOf(task.getLocationCode()));
        itemCode.setText(" 条码: " + task.getBarcode());
        itemName.setText(task.getItemName());

        info_view.setVisibility(View.VISIBLE);

        //如果商品条码和货架条码一致,则显示选择项
        if (itemClipCodeBool) {
            //隐藏选中
            clip_simg.setVisibility(View.VISIBLE);
            item_simg.setVisibility(View.VISIBLE);
        }

        //根据当前任务状态，上架和下架处理不一样
        if (nowTask.isUpTask()) {
            task_status.setText("上架任务");
            task_status.setBackgroundResource(R.drawable.btn_blue);
            itemStock = 1;//增加1件
            isHaveItem = true;//已扫商品
        } else {
            task_status.setText("下架任务");
            task_status.setBackgroundResource(R.drawable.btn_red);
            itemStock = 0;
            isHaveClip = true;//已扫货架
        }

        if (itemClipCodeBool) {
            changeImageView(false);
        }

        //修改当前扫码库存
        itemCount.setText(String.valueOf(itemStock));
    }

    void showTaskDialog() {
        taskListDailog.show();
    }

    void httpGetTasks(String barcode) {
        //检测任务
        showLoadingDialog();
        Map map = new HashMap<>();
        map.put("marketId", String.valueOf(marketId));
        map.put("barcode", barcode);
        Http.post(this, map, Api.SCAN_TASK, new HttpCallbackImpl<List<Task>>("datas") {
            @Override
            public void onSuccess(List<Task> datas) {
                closeLoadingDialog();
                scan_bool = false;
                //如果只有1个
                if (datas.size() == 1) {
                    //直接显示任务
                    setNowTask(datas.get(0));
                    //使其继续扫描
                    resetCapture();
                } else if (datas.size() > 1) {
                    tasks.clear();
                    tasks.addAll(datas);
                    //显示列表，选择一个
                    showTaskDialog();
                } else {
                    DialogUtils.createTipDialogOk(ScodeActivity.this, msg, null).show();
                    //使其继续扫描
                    resetCapture();
                }
            }

            @Override
            public void onFailure() {
                //super.onFailure();
                scan_bool = false;
                closeLoadingDialog();
                if (checkAccessToken()) {
                    return;
                }
                DialogUtils.createTipDialogOk(ScodeActivity.this, msg, null).show();
                //使其继续扫描
                resetCapture();

            }
        });
    }

    void showUpTaskItemStockFull() {
        DialogUtils.createTipDialogOk(ScodeActivity.this, "上架商品数量 已满 ，请扫货架条码执行上架操作", new TipClickListener() {
            @Override
            public void onClick(boolean left) {
                //自动切换去货架
                if (itemClipCodeBool) {
                    selectScodeClip = true;
                    //切换去扫货架
                    changeImageView(true);
                }
                //重新允许可扫描
                resetCapture();
            }
        }).show();
    }

    void checkUpTaskItemStock(final Runnable no) {
        if (itemStock + nowTask.getHasCompleteQuantity() < ITEM_STOCK_MAX) {
            String str = "";
            if (nowTask.getHasCompleteQuantity() > 0) {
                str = str + "货架已有商品数量：" + nowTask.getHasCompleteQuantity() + "\n";
            }
            str = str + "当前上架商品数量：" + itemStock + "\n";
            str = str + "不足任务上架数量：" + ITEM_STOCK_MAX + "\n";
            str = str + "是否强制执行上架任务？";

            TipDialog tipDialog = DialogUtils.createTipDialog(ScodeActivity.this, str, new TipClickListener() {
                @Override
                public void onClick(boolean left) {
                    if (!left) {
                        submitUpTask();
                    } else {
                        if (no != null) {
                            no.run();
                        }
                    }
                }
            });
            tipDialog.show();
        } else {
            submitUpTask();
        }
    }

    void upTaskHandler(final String barcode) {
        //扫中商品条码
        if (scode_itemCode.equals(barcode)) {

            //如果商品条码 与 货架条码一致，当前又选中 货架条码，则直接执行
            if (itemClipCodeBool && selectScodeClip) {
                //直接执行
                checkUpTaskItemStock(new Runnable() {
                    @Override
                    public void run() {
                        resetCapture();
                    }
                });
                return;
            }

            //如果当前扫中商品，如果超过库存
            int nowv = itemStock + 1 + nowTask.getHasCompleteQuantity();
            if (nowv > ITEM_STOCK_MAX) {
                //提示扫货架，商品已满
                showUpTaskItemStockFull();
            } else {
                itemStock++;
                //刷新界面
                itemCount.setText(String.valueOf(itemStock));
                //重新允许可扫描
                resetCapture();
            }
            return;
        } else if (scode_clipCode.equals(barcode)) {
            //扫中货架，判断是否强制提交
            checkUpTaskItemStock(new Runnable() {
                @Override
                public void run() {
                    resetCapture();
                }
            });
            return;
        }

        ToastUtil.showToast(ScodeActivity.this, "扫描内容：" + barcode + " 不是当前上架任务的商品条码或货架条码");
        resetCapture();
    }

    void downTaskHandler(final String barcode) {

        //扫中商品条码
        if (scode_itemCode.equals(barcode)) {

            //如果商品条码 与 货架条码一致，当前又选中 货架条码，则直接执行
            if (itemClipCodeBool && selectScodeClip) {
                //直接执行
                checkDownTaskItemStock(new Runnable() {
                    @Override
                    public void run() {
                        resetCapture();
                    }
                });
                return;
            }

            //如果当前扫中商品，如果超过库存
            int nowv = itemStock + 1;
            if (nowv > ITEM_STOCK_MAX) {
                //提示扫货架，商品已满
                showDownTaskItemStockFull(" 超过 ");
            } else {
                itemStock++;
                //刷新界面
                itemCount.setText(String.valueOf(itemStock));
                //重新允许可扫描
                resetCapture();
            }
            return;

        } else if (scode_clipCode.equals(barcode)) {
            //直接执行
            checkDownTaskItemStock(new Runnable() {
                @Override
                public void run() {
                    resetCapture();
                }
            });
            return;
        }

        ToastUtil.showToast(ScodeActivity.this, "扫描内容：" + barcode + " 不是当前上架任务的商品条码或货架条码");
        resetCapture();
    }

    void showDownTaskItemStockFull(String ti) {
        TipDialog tip = DialogUtils.createTipDialog(ScodeActivity.this, "温馨提示", "下架商品数量已" + ti + "任务货架商品数量\n\n是否添加下架数量?", "取消", "确定", new TipClickListener() {
            @Override
            public void onClick(boolean left) {
                if (!left) {
                    itemStock++;
                    //刷新界面
                    itemCount.setText(String.valueOf(itemStock));
                }
                //重新允许可扫描
                resetCapture();
            }
        });
        tip.show();
    }

    void checkDownTaskItemStock(final Runnable no) {
        if (itemStock != ITEM_STOCK_MAX) {
            TipDialog tipDialog = DialogUtils.createTipDialog(ScodeActivity.this, "下架商品数量(" + itemStock + ")与任务货架商品数量(" + ITEM_STOCK_MAX + ")不一致：\n是否强制执行下架任务？", new TipClickListener() {
                @Override
                public void onClick(boolean left) {
                    if (!left) {
                        submitDownTask();
                    } else {
                        if (no != null) {
                            no.run();
                        }
                    }
                }
            });
            tipDialog.show();
        } else {
            submitDownTask();
        }
    }

    void scode(final String result) {
        //如果没有任务
        if (nowTask == null) {
            //获取任务
            httpGetTasks(result);
            return;
        }
        //有任务，则分上架和下架处理
        if (nowTask.isUpTask()) {
            //上架扫描处理
            upTaskHandler(result);
        } else {
            //下架扫描处理
            downTaskHandler(result);
        }
    }

    int ITEM_STOCK_MAX = 5;

    //强制提交任务
    void submitUpTask() {
        //强制执行任务
        showLoadingDialog();
        Map map = new HashMap<>();
        map.put("passportId", String.valueOf(Api.passport.getPassportIdStr()));
        map.put("marketId", String.valueOf(marketId));
        map.put("barcode", scode_itemCode);
        map.put("location", scode_clipCode);
        map.put("onShelvesQuantity", String.valueOf(itemStock));

        Http.post(this, map, Api.SCAN_OK_UP, new HttpCallbackImpl() {
            @Override
            public void onSuccess(Object obj) {
                closeLoadingDialog();
                DialogUtils.createTipDialogOk(ScodeActivity.this, "操作成功", new TipClickListener() {
                    @Override
                    public void onClick(boolean left) {
                        ScodeActivity.this.setResult(RESULT_OK);
                        ScodeActivity.this.finish();
                    }
                }).show();
            }

            @Override
            public void onFailure() {
                closeLoadingDialog();
                if (checkAccessToken()) {
                    return;
                }
                DialogUtils.createTipDialogOk(ScodeActivity.this, msg, new TipClickListener() {
                    @Override
                    public void onClick(boolean left) {
                        resetCapture();
                    }
                }).show();
            }
        });
    }

    //强制提交任务
    void submitDownTask() {
        //强制执行任务
        showLoadingDialog();
        Map map = new HashMap<>();
        map.put("passportId", String.valueOf(Api.passport.getPassportIdStr()));
        map.put("marketId", String.valueOf(marketId));
        map.put("barcode", scode_itemCode);
        map.put("location", scode_clipCode);
        map.put("offShelvesQuantity", String.valueOf(itemStock));

        Http.post(this, map, Api.SCAN_OK_DOWN, new HttpCallbackImpl() {
            @Override
            public void onSuccess(Object obj) {
                closeLoadingDialog();
                DialogUtils.createTipDialogOk(ScodeActivity.this, "操作成功", new TipClickListener() {
                    @Override
                    public void onClick(boolean left) {
                        ScodeActivity.this.setResult(RESULT_OK);
                        ScodeActivity.this.finish();
                    }
                }).show();
            }

            @Override
            public void onFailure() {
                closeLoadingDialog();
                if (checkAccessToken()) {
                    return;
                }
                DialogUtils.createTipDialogOk(ScodeActivity.this, msg, new TipClickListener() {
                    @Override
                    public void onClick(boolean left) {
                        resetCapture();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.clip_ll) {
            changeImageView(true);
        } else if (id == R.id.item_ll) {
            changeImageView(false);
        }
    }

    void changeImageView(boolean clip) {
        selectScodeClip = clip;
        if (clip) {
            clip_simg.setImageResource(R.drawable.choice_click_s);
            item_simg.setImageResource(R.drawable.choice_click);
        } else {
            clip_simg.setImageResource(R.drawable.choice_click);
            item_simg.setImageResource(R.drawable.choice_click_s);
        }
    }

    //重启 重新 扫描
    boolean resetBool = false;
    Handler resetHandler = new Handler();

    void resetCapture() {
        //如果是红外，不需要
        if (red_line_bool) {
            return;
        }
        if (resetBool) {
            return;
        }
        resetBool = true;
        resetHandler.postDelayed(resetRunnable, 1000);

    }

    private Runnable resetRunnable = new Runnable() {
        @Override
        public void run() {
            //使其继续扫描
            captureViewHandler.getHandler().restartPreviewAndDecode();
            resetBool = false;
        }
    };

    @Override
    public void finish() {
        super.finish();
        //如果是红外，不需要
        if (red_line_bool) {
            return;
        }
        if (resetHandler != null) {
            resetHandler.removeCallbacks(resetRunnable);
        }
    }
}