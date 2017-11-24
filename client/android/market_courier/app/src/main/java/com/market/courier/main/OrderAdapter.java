package com.market.courier.main;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.market.courier.R;
import com.market.courier.response.Api;
import com.market.courier.response.Market;
import com.market.courier.response.OrderData;
import com.market.courier.response.enums.OrderStatusEnum;
import com.market.courier.response.enums.StatusEnterEnum;
import com.market.courier.response.event.RefreshEvent;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class OrderAdapter extends AfinalAdapter<OrderData> {

    StatusEnterEnum statusEnterEnum;
    OrderListFragment orderListFragment;

    public OrderAdapter(OrderListFragment orderListFragment, List<OrderData> orderDatas, StatusEnterEnum statusEnterEnum) {
        super(orderListFragment.getActivity(), orderDatas);
        this.statusEnterEnum = statusEnterEnum;
        this.orderListFragment = orderListFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_order, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.refresh(getItem(position));

        return convertView;
    }

    class Holder implements View.OnClickListener {


        TextView tv_orderid;
        TextView tv_time;
        TextView tv_address;
        TextView tv_deliverDistance;
        TextView btn1;
        TextView btn2;
        OrderData orderData;

        int btn1Type;
        int btn2Type;

        public Holder(View view) {
            tv_orderid = ViewUtils.find(view, R.id.tv_orderid);
            tv_time = ViewUtils.find(view, R.id.tv_time);
            tv_address = ViewUtils.find(view, R.id.tv_address);
            tv_deliverDistance = ViewUtils.find(view, R.id.iv_img);
            btn1 = ViewUtils.find(view, R.id.btn1);
            btn2 = ViewUtils.find(view, R.id.btn2);
            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);

        }

        public void refresh(OrderData orderData) {
            this.orderData = orderData;
            tv_orderid.setText(orderData.getOrderSequenceNumber());
            tv_time.setText(orderData.getPaymentTime());
            tv_address.setText(orderData.getFormatReceiptAddress());
            tv_deliverDistance.setText(orderData.getDeliverDistance());

            if (statusEnterEnum == StatusEnterEnum.COURIER_WAIT_ACCEPT) {
                btn2.setVisibility(View.GONE);
                btn1.setText("抢单");
                btn1Type = 0;
            } else if (statusEnterEnum == StatusEnterEnum.COURIER_WAIT_PICK_UP) {

                if (orderData.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ACCEPT.getKey()) {
                    btn2.setVisibility(View.GONE);
                    btn1.setText("开锁取货");
                    btn1Type = 1;
                } else if (orderData.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_DELIVER.getKey()) {
                    btn2.setVisibility(View.GONE);
                    btn1.setText("配送清单");
                    btn1Type = 2;
                }

            } else if (statusEnterEnum == StatusEnterEnum.COURIER_DELIVER) {
                btn1.setText("联系收货人");
                btn1Type = 3;

                btn2.setText("确认送达");
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn1) {
                switch (btn1Type) {
                    case 0://抢单
                        acceptOrder(orderData);
                        return;
                    case 1://开锁取货
                        context.startActivity(new Intent(context, ActivityOpen.class).putExtra("orderSequenceNumber", orderData.getOrderSequenceNumber()));
                        return;
                    case 2://配送清单
                        Market market = orderListFragment.getNowMarket();
                        String name = market != null ? "" : market.getName();
                        context.startActivity(new Intent(context, ActivityOrderDetail.class).putExtra("orderData", orderData)
                                .putExtra("market", name));
                        return;
                    case 3://收货人电话
                        DeviceUtils.callPhone(context, "");
                        return;
                }
            }
        }
    }

    void acceptOrder(OrderData orderData) {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("orderId", String.valueOf(orderData.getOrderId()));
        Http.post(context, map, Api.ACCEPT_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    EventBus.getDefault().post(new RefreshEvent());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    void deliverOrder(OrderData orderData) {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("orderSequenceNumber", orderData.getOrderSequenceNumber());
        Http.post(context, map, Api.ACCEPT_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    EventBus.getDefault().post(new RefreshEvent());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    void arriveOrder(OrderData orderData) {
        Map map = new HashMap();
        map.put("passportId", Api.passport.getPassportIdStr());
        map.put("orderSequenceNumber", orderData.getOrderSequenceNumber());
        Http.post(context, map, Api.ACCEPT_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    EventBus.getDefault().post(new RefreshEvent());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
