package com.jiketuandui.antinetfraud.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiketuandui.antinetfraud.Util.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();//添加的Fragment的集合

    public ArticleDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param fragment 添加Fragment
     */
    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return MyApplication.getInstance().getArticleTitle().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MyApplication.getInstance().getArticleTitle()[position];
    }
}
