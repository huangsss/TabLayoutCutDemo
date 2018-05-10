package com.huangasys.tablayoutcutdemo;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    private String[] tabTexts = {"首页", "旅行", "行程", "我的"};
    private int[] icons = {R.drawable.icon_tab_home, R.drawable.icon_tab_trip, R.drawable.icon_tab_plan, R.drawable.icon_tab_my};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

        viewpagerAdapter.addFragments(new HomeFragment(), tabTexts[0]);
        viewpagerAdapter.addFragments(new TwoFragment(), tabTexts[1]);
        viewpagerAdapter.addFragments(new ThreeFragment(), tabTexts[2]);
        viewpagerAdapter.addFragments(new FourFragment(), tabTexts[3]);
        mViewPager.setAdapter(viewpagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);//设置联动;
        mTabLayout.getTabAt(0).setIcon(icons[0]);
        mTabLayout.getTabAt(0).setText("");
        mTabLayout.getTabAt(0).select();
        setTabLine(mTabLayout,30,30);//设置下划线长度;
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//TabLayout联动监听;
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //被选中时
                tab.setText("");
                tab.setIcon(icons[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选中
                tab.setText(tabTexts[tab.getPosition()]);
                tab.setIcon(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class ViewpagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTabs;

        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragments = new ArrayList<>();
            this.mTabs = new ArrayList<>();
        }

        public void addFragments(Fragment fragment, String tab) {
            mFragments.add(fragment);
            mTabs.add(tab);
        }

        @Override
        public Fragment getItem(int position) {
            return HomeFragment.getInstance();
//         or   return mFragments.get(position);
        }

        /**
         * 需要重写此方法才可正常显示顶部title;
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }
    }

    /**
     * 自定义TabLayout的下划线长度.
     * @param tab
     * @param left
     * @param right
     */
    public void setTabLine(TabLayout tab,int left,int right){
        try {
            Class<?> tabLayout = tab.getClass();
            Field tabStrip = tabLayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab= (LinearLayout) tabStrip.get(tab);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0,0,0,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
                //修改两个tab的间距
                params.setMarginStart(dp2px(left));
                params.setMarginEnd(dp2px(right));
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
