package com.jiketuandui.antinetfraud.Activity.MainActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.NormalDialog;
import com.jiketuandui.antinetfraud.Fragment.MainPageFragment.MainTab;
import com.jiketuandui.antinetfraud.Fragment.MainPageFragment.MainTabClasscial;
import com.jiketuandui.antinetfraud.Fragment.MainPageFragment.MainTabHotNews;
import com.jiketuandui.antinetfraud.Fragment.MainPageFragment.MainTabSetting;
import com.jiketuandui.antinetfraud.HTTP.getAppUpdate;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.View.ContentViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    /**
     * List 用于存放四个Fragment
     */
    private List<Fragment> content_list;
    private ContentViewPager mContentViewPager;
    /**
     * 底部的四个控件的响应事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_bottom_home:// 主页
                    mContentViewPager.setCurrentItem(0);
                    break;
                case R.id.id_bottom_hot:// 热点
                    mContentViewPager.setCurrentItem(1);
                    break;
                case R.id.id_bottom_Search:// 搜索
                    mContentViewPager.setCurrentItem(2);
                    break;
                case R.id.id_bottom_my:// 我的
                    mContentViewPager.setCurrentItem(3);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this);

        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        getAppUpdate update = new getAppUpdate(MainActivity.this, false);
        update.startUpdate();
    }

    /**
     * 初始化各个Fragment
     */
    private void initFragment() {
        content_list = new ArrayList<>();
        MainTab mainTab = new MainTab();
        content_list.add(mainTab);// 主页
        //content_list.add(new MainTabHot());// 热点
        content_list.add(new MainTabHotNews());// 热点
        content_list.add(new MainTabClasscial());// 搜索
        content_list.add(new MainTabSetting());// 我的

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
    }

    @Override
    public void onBackPressed() {
        final NormalDialog dialog = new NormalDialog(this);
        dialog.content("亲,真的要走吗?")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .btnText("继续看看", "残忍退出")//
                .btnTextColor(Color.parseColor("#00BDD0"), Color.parseColor("#D4D4D4"))//
                .btnTextSize(16f, 16f)//
                .showAnim(new Swing())//
                .dismissAnim(new SlideBottomExit())//
                .show();

        dialog.setOnBtnClickL(
                dialog::dismiss,
                () -> {
                    dialog.superDismiss();
                    finish();
                });
    }
}
