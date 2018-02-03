package com.jiketuandui.antinetfraud.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiketuandui.antinetfraud.Util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章内容详情页
 *
 * @author wangyu
 */
public class ArticleDetailAdapter extends FragmentPagerAdapter {
    // 添加的Fragment的集合
    private final List<Fragment> mFragments = new ArrayList<>();

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
        return Constants.ARTICLE_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.ARTICLE_TITLE[position];
    }
}
