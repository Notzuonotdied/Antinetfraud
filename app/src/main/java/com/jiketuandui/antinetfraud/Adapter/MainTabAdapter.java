package com.jiketuandui.antinetfraud.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.Fragment.MainTab_news;


/**
 * Created by Notzuonotdied on 2016/7/31.
 * 这个是主页的PageAdapter
 */
public class MainTabAdapter extends FragmentPagerAdapter {

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 使用子Fragment作为新闻的承载体
     */
    @Override
    public Fragment getItem(int position) {
        MainTab_news mainTab_news = new MainTab_news();
        Bundle mbundle = new Bundle();
        mbundle.putInt(Constant.MAINPAGEPOSITON, position);
        mainTab_news.setArguments(mbundle);
        return mainTab_news;
    }

    @Override
    public int getCount() {
        return Constant.TabBigTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.TabBigTitle[position];
    }
}
