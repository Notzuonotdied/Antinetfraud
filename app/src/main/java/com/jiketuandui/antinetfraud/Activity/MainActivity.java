package com.jiketuandui.antinetfraud.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.Constant;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.View.ContentViewPager;
import com.jiketuandui.antinetfraud.Fragment.MainTab;
import com.jiketuandui.antinetfraud.Fragment.MainTabMy;
import com.jiketuandui.antinetfraud.Fragment.MainTabSearch;
import com.jiketuandui.antinetfraud.Fragment.MainTabHot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    /**
     * List 用于存放四个Fragment
     */
    private List<Fragment> content_list = null;
    private ContentViewPager mContentViewPager;
    private CFontTitleTextView cFontTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initFragment();
        initView();
    }

    /**
     * 初始化各个Fragment
     */
    private void initFragment() {
        content_list = new ArrayList<>();
        MainTab mainTab = new MainTab();
        content_list.add(mainTab);// 主页
        content_list.add(new MainTabHot());// 热点
        content_list.add(new MainTabSearch());// 搜索
        content_list.add(new MainTabMy());// 我的

        mContentViewPager = (ContentViewPager) findViewById(R.id.id_viewpager);
        // 预加载一页的内容
        mContentViewPager.setOffscreenPageLimit(1);
        // 设置Adapter
        mContentViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return content_list.get(position);
            }

            @Override
            public int getCount() {
                return content_list.size();
            }
        });
    }

    /**
     * 底部的四个控件的响应事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_bottom_home:// 主页
                    mContentViewPager.setCurrentItem(0);
                    cFontTextView.setText(Constant.Header_TextView[0]);
                    break;
                case R.id.id_bottom_hot:// 热点
                    mContentViewPager.setCurrentItem(1);
                    cFontTextView.setText(Constant.Header_TextView[1]);
                    break;
                case R.id.id_bottom_Search:// 搜索
                    mContentViewPager.setCurrentItem(2);
                    cFontTextView.setText(Constant.Header_TextView[2]);
                    break;
                case R.id.id_bottom_my:// 我的
                    mContentViewPager.setCurrentItem(3);
                    cFontTextView.setText(Constant.Header_TextView[3]);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化头部的一些控件和空间对应的响应事件
     */
    private void initView() {
        if (content_list == null) {
            return;
        }
        // 初始化
        Button mTabTextView = (Button) findViewById(R.id.id_bottom_home);
        Button mTabHotTextView = (Button) findViewById(R.id.id_bottom_hot);
        Button mTabSearchTextView = (Button) findViewById(R.id.id_bottom_Search);
        Button mTabMyTextView = (Button) findViewById(R.id.id_bottom_my);

        // 设置监听事件
        mTabTextView.setOnClickListener(listener);
        mTabHotTextView.setOnClickListener(listener);
        mTabSearchTextView.setOnClickListener(listener);
        mTabMyTextView.setOnClickListener(listener);

        cFontTextView = (CFontTitleTextView) findViewById(R.id.header_tv);
    }

    /**
     * 双击退出效果
     * */
    private long timeMillis;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 长按效果
            if ((System.currentTimeMillis() - timeMillis) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                timeMillis = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
