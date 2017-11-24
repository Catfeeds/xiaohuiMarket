package com.xiaohui.replenishment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaohui.replenishment.response.Market;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * @author hellozmg
 *         Created on 8/29.
 */
public class MarketAdapter extends AfinalAdapter<Market> {

    public MarketAdapter(Context context, List<Market> markets) {
        super(context, markets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_market, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.refresh(getItem(position));

        return convertView;
    }

    class Holder {

        TextView nameTxt;
        TextView addressTxt;
        TextView statusTxt;

        public Holder(View view) {
            nameTxt = ViewUtils.find(view, R.id.market_name);
            addressTxt = ViewUtils.find(view, R.id.market_address);
            statusTxt = ViewUtils.find(view, R.id.market_status);
        }

        public void refresh(Market market) {
            nameTxt.setText(market.getName());
            addressTxt.setText(market.getAddress());
            int status = market.getStatus();
            String statusOffline = null;
            StringBuilder sb = new StringBuilder();
            //如果与硬件断连
            if ((status & 16) == 16) {
                status = (status ^ 16);
                statusOffline = "与硬件断连";
            }
            if (status == 1) {
                //正常
                sb.append("正常");
            } else if (status == 2) {
                //关店
                sb.append("关店");
            } else if (status == 4) {
                //维护
                sb.append("维护");
            } else if (status == 0) {
                //无效
                sb.append("无效");
            }
            if (statusOffline != null) {
                statusTxt.setBackgroundResource(R.drawable.btn_red);
                sb.append(" ").append(statusOffline);
            } else if(status != 1) {
                statusTxt.setBackgroundResource(R.drawable.btn_red);
            } else {
                statusTxt.setBackgroundResource(R.drawable.btn_blue);
            }
            statusTxt.setText(sb.toString());
        }
    }
}
