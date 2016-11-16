package com.jiketuandui.antinetfraud.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jiketuandui.antinetfraud.Activity.SearchActivity;
import com.jiketuandui.antinetfraud.Activity.SearchDetailActivity;
import com.jiketuandui.antinetfraud.Adapter.MainTabAdapter;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.View.CFontTitleTextView;
import com.jiketuandui.antinetfraud.View.MyTabPageIndicator;

import java.lang.reflect.Field;


public class MainTab extends Fragment {

    public MainTab() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 因为要在Fragment中嵌套使用ViewPager,所以需要进行初始化一些变量
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /**
         * 使用LayoutInflate将main_tab.xml的加载进来之后,为TabPageIndicator,
         * ViewPager进行初始化
         * */
        View v = inflater.inflate(R.layout.main_tab, container, false);
        initData(v);
        return v;
    }

    /**
     * 初始化一些变量
     */
    private void initData(View v) {
        /**
         * 先实例化ViewPager,然后实例化TabPageIndicator，
         * 并且要设置TabPageIndicator和ViewPager关联，
         * 就是调用TabPageIndicator的setViewPager(ViewPager view)方法，
         * 这样子我们就实现了点击上面的Tab，下面的ViewPager切换，
         * 滑动ViewPager上面的Tab跟着切换，ViewPager的每一个Item我们使用的是Fragment，
         * 使用Fragment可以使我们的布局更加灵活一点
         * */
        MyTabPageIndicator myTabPageIndicator = (MyTabPageIndicator)
                v.findViewById(R.id.mainTab_indicator);
        ViewPager mViewPager = (ViewPager) v.findViewById(R.id.mainTab_viewpager);
        mViewPager.setOffscreenPageLimit(2);
        MainTabAdapter mainTabAdapter = new MainTabAdapter(getChildFragmentManager());
        /**
         * 绑定
         * */
        mViewPager.setAdapter(mainTabAdapter);
        myTabPageIndicator.setViewPager(mViewPager, 0);


        ImageButton imageButton = (ImageButton) v.findViewById(R.id.gotoSearch);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 这段可以解决fragment嵌套fragment会崩溃的问题
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            //参数是固定写法
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
