package com.jiketuandui.antinetfraud.activity.main;

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
import com.jiketuandui.antinetfraud.activity.main.page.MainTab;
import com.jiketuandui.antinetfraud.activity.main.page.MainTabClasscial;
import com.jiketuandui.antinetfraud.activity.main.page.MainTabHotNews;
import com.jiketuandui.antinetfraud.activity.main.page.MainTabSetting;
import com.jiketuandui.antinetfraud.activity.setting.update.getAppUpdate;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.View.ContentViewPager;
import com.jiketuandui.antinetfraud.Util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 入口Activity
 *
 * @author wangyu
 */
public class MainActivity extends AppCompatActivity {
    /**
     * List 用于存放四个Fragment
     */
    private List<Fragment> contentList;
    private ContentViewPager mContentViewPager;
    /**
     * 底部的四个控件的响应事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // 主页
                case R.id.id_bottom_home:
                    mContentViewPager.setCurrentItem(0);
                    break;
                // 热点
                case R.id.id_bottom_hot:
                    mContentViewPager.setCurrentItem(1);
                    break;
                // 搜索
                case R.id.id_bottom_Search:
                    mContentViewPager.setCurrentItem(2);
                    break;
                // 我的
                case R.id.id_bottom_my:
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
        StatusBarUtil.StatusBarLightMode(this);

        setContentView(R.layout.activity_main);
        initFragment();
        initView();
        new getAppUpdate(false).startUpdate();
    }

    /**
     * 初始化各个Fragment
     */
    private void initFragment() {
        contentList = new ArrayList<>();
        MainTab mainTab = new MainTab();
        // 主页
        contentList.add(mainTab);
        // 热点
        contentList.add(new MainTabHotNews());
        // 搜索
        contentList.add(new MainTabClasscial());
        // 我的
        contentList.add(new MainTabSetting());

        mContentViewPager = findViewById(R.id.id_viewpager);
        // 预加载一页的内容
        mContentViewPager.setOffscreenPageLimit(1);
        // 设置Adapter
        mContentViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return contentList.get(position);
            }

            @Override
            public int getCount() {
                return contentList.size();
            }
        });
    }

    /**
     * 初始化头部的一些控件和空间对应的响应事件
     */
    private void initView() {
        if (contentList == null) {
            return;
        }
        // 初始化
        Button mTabTextView = findViewById(R.id.id_bottom_home);
        Button mTabHotTextView = findViewById(R.id.id_bottom_hot);
        Button mTabSearchTextView = findViewById(R.id.id_bottom_Search);
        Button mTabMyTextView = findViewById(R.id.id_bottom_my);

        // 设置监听事件
        mTabTextView.setOnClickListener(listener);
        mTabHotTextView.setOnClickListener(listener);
        mTabSearchTextView.setOnClickListener(listener);
        mTabMyTextView.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        final NormalDialog dialog = new NormalDialog(this);
        dialog.content("亲,真的要走吗?")
                .style(NormalDialog.STYLE_TWO)
                .titleTextSize(23)
                .btnText("继续看看", "残忍退出")
                .btnTextColor(Color.parseColor("#00BDD0"),
                        Color.parseColor("#D4D4D4"))
                .btnTextSize(16f, 16f)
                .showAnim(new Swing())
                .dismissAnim(new SlideBottomExit())
                .show();

        dialog.setOnBtnClickL(dialog::dismiss, () -> {
            dialog.superDismiss();
            finish();
        });
    }
}
