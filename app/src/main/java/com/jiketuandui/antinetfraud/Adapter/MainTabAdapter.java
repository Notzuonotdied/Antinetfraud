package com.jiketuandui.antinetfraud.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiketuandui.antinetfraud.Fragment.MainPageFragment.MainTabNews;
import com.jiketuandui.antinetfraud.Util.MyApplication;


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
        MainTabNews mainTab_news = new MainTabNews();
        Bundle mbundle = new Bundle();
        mbundle.putInt(MyApplication.getInstance().getMAINPAGEPOSITON(), position);
        mainTab_news.setArguments(mbundle);
        return mainTab_news;
    }

    @Override
    public int getCount() {
        return MyApplication.getInstance().getTabBigTitle().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MyApplication.getInstance().getTabBigTitle()[position];
    }
}
