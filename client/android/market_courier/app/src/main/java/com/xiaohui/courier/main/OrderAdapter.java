package com.xiaohui.courier.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaohui.courier.R;
import com.xiaohui.courier.response.OrderData;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/9/24 0024.
 */

public class OrderAdapter extends AfinalAdapter<OrderData> {

    public OrderAdapter(Context context, List<OrderData> orderDatas) {
        super(context, orderDatas);
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

    class Holder {

        TextView tv_orderid;
        TextView tv_time;
        TextView tv_address;
        TextView btn1;
        TextView btn2;

        int btn1Type;
        int btn2Type;


        public Holder(View view) {
            tv_orderid = ViewUtils.find(view, R.id.tv_orderid);
            tv_time = ViewUtils.find(view, R.id.tv_time);
            tv_address = ViewUtils.find(view, R.id.tv_address);
            btn1 = ViewUtils.find(view, R.id.btn1);
            btn2 = ViewUtils.find(view, R.id.btn2);
        }

        public void refresh(OrderData orderData) {
            tv_orderid.setText(orderData.getOrderId());
            tv_time.setText(orderData.getTime());
            tv_address.setText(orderData.getAddress());
        }
    }
}
