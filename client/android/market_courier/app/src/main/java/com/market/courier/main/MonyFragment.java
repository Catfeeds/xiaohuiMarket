package com.market.courier.main;

import android.content.Intent;
import android.view.View;

import com.market.courier.R;
import com.market.courier.response.Market;
import com.zhumg.anlib.AfinalFragment;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public class MonyFragment extends AfinalFragment implements View.OnClickListener {

    @Bind(R.id.no_btn)
    View no_btn;

    @Bind(R.id.ok_btn)
    View ok_btn;

    Market market;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_many;
    }

    @Override
    protected void initViewData(View view) {
        ok_btn.setOnClickListener(this);
        no_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.no_btn) {
            startActivity(new Intent(this.getActivity(), ActivityMany.class).putExtra("type", 1).putExtra("market", market));
        } else {
            startActivity(new Intent(this.getActivity(), ActivityMany.class).putExtra("type", 0).putExtra("market", market));
        }
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
