package com.jiketuandui.antinetfraud.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiketuandui.antinetfraud.activity.main.page.MainTabNews;
import com.jiketuandui.antinetfraud.Util.Constants;


/**
 * 这个是主页的PageAdapter
 *
 * @author Notzuonotdied
 * @date 2016/7/31
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
        MainTabNews mainTabNews = new MainTabNews();
        Bundle mBundle = new Bundle();
        mBundle.putInt(Constants.MAIN_PAGE_POSITION, position);
        mainTabNews.setArguments(mBundle);
        return mainTabNews;
    }

    @Override
    public int getCount() {
        return Constants.TAB_BIG_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.TAB_BIG_TITLE[position];
    }
}
