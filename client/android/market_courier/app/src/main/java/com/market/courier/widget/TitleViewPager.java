package com.market.courier.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.market.courier.R;
import com.zhumg.anlib.widget.FragmentAdapter;

import java.util.List;


/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class TitleViewPager implements View.OnClickListener {

    ViewPager viewPager;
    FragmentAdapter adapter;
    View iv_bottom_line;

    int mSelectIndex = -1;
    int titleWidth;

    TextView[] tabs;
    Fragment nowFragment;

    public TitleViewPager(View view, FragmentManager fm, List<Fragment> fragments, int tWidth) {

        this.titleWidth = tWidth;

        ViewGroup vg = (ViewGroup) view.findViewById(R.id.title_tabll);
        tabs = new TextView[vg.getChildCount()];
        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = (TextView) vg.getChildAt(i);
            tabs[i].setTag(i);
            tabs[i].setOnClickListener(this);
        }

        iv_bottom_line = view.findViewById(R.id.iv_bottom_line);
        ViewGroup.LayoutParams lps = iv_bottom_line.getLayoutParams();
        lps.width = (int) (titleWidth / fragments.size());
        iv_bottom_line.setLayoutParams(lps);

        adapter = new FragmentAdapter(fm, fragments);
        viewPager = (ViewPager) view.findViewById(R.id.title_vp);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                if (index != mSelectIndex) {
                    switchTab(index);
                }
            }

            @Override
            public void onPageScrolled(int position, float offset, int arg2) {
                // 下面的指示条的手指跟随滑动
                int x = (int) ((position + offset) * titleWidth / adapter.getCount());
                ((View) iv_bottom_line.getParent()).scrollTo(-x, iv_bottom_line.getScrollY());
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        switchTab(0);
    }

    public boolean switchTab(int index) {

        if (mSelectIndex == index) {
            return false;
        }

        mSelectIndex = index;
        viewPager.setCurrentItem(index, true);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setSelected(index == i);
        }

        nowFragment = (Fragment) adapter.getFragments().get(mSelectIndex);
        return true;
    }

    public Fragment getNowFragment() {
        return nowFragment;
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        switchTab(tag);
    }

    public void setTitle(int index, String title) {
        tabs[index].setText(title);
    }
}
