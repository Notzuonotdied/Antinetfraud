package com.jiketuandui.antinetfraud.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.View.MainTabHot_news;


/**
 * Created by Notzuonotdied on 2016/7/31.
 * 这个是主页的PageAdapter
 */
public class MainTabHotAdapter extends FragmentPagerAdapter {

    public MainTabHotAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 使用子Fragment作为新闻的承载体
     */
    @Override
    public Fragment getItem(int position) {
        MainTabHot_news mainTabHotNews = new MainTabHot_news();
        Bundle mbundle = new Bundle();
        mbundle.putInt(Constant.MAINPAGEPOSITONHOT, position);
        mainTabHotNews.setArguments(mbundle);
        return mainTabHotNews;
    }

    @Override
    public int getCount() {
        return Constant.TabBigTitle_hot.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.TabBigTitle_hot[position];
    }
}
