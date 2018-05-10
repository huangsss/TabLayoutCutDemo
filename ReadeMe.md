#### TabLayout实现切换更换顶部图标按钮
> 1.添加依赖
```
    compile 'com.android.support:design:25.4.0'
```
> 2. 布局文件就是TabLayout+Viewpager搭配使用 Tablayout的基本属性的使用也很简单

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.huangasys.tablayoutcutdemo.MainActivity">

    <android.support.design.widget.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/appcolor"
        app:tabTextColor="@color/colorw"
        app:tabSelectedTextColor="@color/colorw"
        app:tabIndicatorColor="@color/colorw"
        app:tabMode="fixed">

    </android.support.design.widget.TabLayout>

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </android.support.v4.view.ViewPager>
</LinearLayout>

```

> 3.将数据跟需要切换的图标写好

```
private String[] tabTexts = {"首页", "旅行", "行程", "我的"};
private int[] icons = {R.drawable.icon_tab_home, R.drawable.icon_tab_trip, R.drawable.icon_tab_plan, R.drawable.icon_tab_my};

```

> 4.Viewpager的适配器

```

 class ViewpagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;//联动的Fragment;
        private List<String> mTabs;//顶部文字

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
    
```

> 5.基本的设置TabLayout与Viewpager的联动

```
    mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
    mViewPager = (ViewPager) findViewById(R.id.viewPager);
    ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

    viewpagerAdapter.addFragments(new HomeFragment(), tabTexts[0]);
    viewpagerAdapter.addFragments(new TwoFragment(), tabTexts[1]);
    viewpagerAdapter.addFragments(new ThreeFragment(), tabTexts[2]);
    viewpagerAdapter.addFragments(new FourFragment(), tabTexts[3]);
    mViewPager.setAdapter(viewpagerAdapter);
    mTabLayout.setupWithViewPager(mViewPager);//设置联动;
```

> 6. 主要是设置addOnTabSelectedListener改变图标+文字

```
        mTabLayout.getTabAt(0).setIcon(icons[0]);
        mTabLayout.getTabAt(0).setText("");
        mTabLayout.getTabAt(0).select();
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

```

#### 拓展.自定义TabLayout的下划线长度

```

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

```
#### 使用: setTabLine(mTabLayout,30,30);//设置下划线长度;

![20180510_105535.gif](https://upload-images.jianshu.io/upload_images/3523048-c68ae68845b2274c.gif?imageMogr2/auto-orient/strip)
